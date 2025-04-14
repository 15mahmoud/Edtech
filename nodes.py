import os
import yaml
from pocketflow import Node, BatchNode
from utils.crawl_github_files import crawl_github_files
from utils.call_llm import call_llm 
from utils.crawl_local_files import crawl_local_files

# Helper to get content for specific file indices
def get_content_for_indices(files_data, indices):
    content_map = {}
    for i in indices:
        if 0 <= i < len(files_data):
            path, content = files_data[i]
            content_map[f"{i} # {path}"] = content # Use index + path as key for context
    return content_map

class FetchRepo(Node):
    def prep(self, shared):
        repo_url = shared.get("repo_url")
        local_dir = shared.get("local_dir")
        project_name = shared.get("project_name")
        
        if not project_name:
            # Basic name derivation from URL or directory
            if repo_url:
                project_name = repo_url.split('/')[-1].replace('.git', '')
            else:
                project_name = os.path.basename(os.path.abspath(local_dir))
            shared["project_name"] = project_name

        # Get file patterns directly from shared
        include_patterns = shared["include_patterns"]
        exclude_patterns = shared["exclude_patterns"]
        max_file_size = shared["max_file_size"]

        return {
            "repo_url": repo_url,
            "local_dir": local_dir,
            "token": shared.get("github_token"),
            "include_patterns": include_patterns,
            "exclude_patterns": exclude_patterns,
            "max_file_size": max_file_size,
            "use_relative_paths": True
        }

    def exec(self, prep_res):
        if prep_res["repo_url"]:
            print(f"Crawling repository: {prep_res['repo_url']}...")
            result = crawl_github_files(
                repo_url=prep_res["repo_url"],
                token=prep_res["token"],
                include_patterns=prep_res["include_patterns"],
                exclude_patterns=prep_res["exclude_patterns"],
                max_file_size=prep_res["max_file_size"],
                use_relative_paths=prep_res["use_relative_paths"]
            )
        else:
            print(f"Crawling directory: {prep_res['local_dir']}...")
            result = crawl_local_files(
                directory=prep_res["local_dir"],
                include_patterns=prep_res["include_patterns"],
                exclude_patterns=prep_res["exclude_patterns"],
                max_file_size=prep_res["max_file_size"],
                use_relative_paths=prep_res["use_relative_paths"]
            )
            
        # Convert dict to list of tuples: [(path, content), ...]
        files_list = list(result.get("files", {}).items())
        print(f"Fetched {len(files_list)} files.")
        return files_list

    def post(self, shared, prep_res, exec_res):
        shared["files"] = exec_res # List of (path, content) tuples

class IdentifyAbstractions(Node):
    def prep(self, shared):
        files_data = shared["files"]
        project_name = shared["project_name"]  # Get project name
        
        # Helper to create context from files, respecting limits (basic example)
        def create_llm_context(files_data):
            context = ""
            file_info = [] # Store tuples of (index, path)
            for i, (path, content) in enumerate(files_data):
                entry = f"--- File Index {i}: {path} ---\n{content}\n\n"
                context += entry
                file_info.append((i, path))

            return context, file_info # file_info is list of (index, path)

        context, file_info = create_llm_context(files_data)
        # Format file info for the prompt (comment is just a hint for LLM)
        file_listing_for_prompt = "\n".join([f"- {idx} # {path}" for idx, path in file_info])
        return context, file_listing_for_prompt, len(files_data), project_name  # Return project name

    def exec(self, prep_res):
        context, file_listing_for_prompt, file_count, project_name = prep_res  # Unpack project name
        print("Identifying abstractions using LLM...")
        prompt = f"""
For the project `{project_name}`:

Codebase Context:
{context}

Analyze the codebase context.
Identify the top 5-10 core most important abstractions to help those new to the codebase.

For each abstraction, provide:
1. A concise `name`.
2. A beginner-friendly `description` explaining what it is with a simple analogy, in around 100 words.
3. A list of relevant `file_indices` (integers) using the format `idx # path/comment`.

List of file indices and paths present in the context:
{file_listing_for_prompt}

Format the output as a YAML list of dictionaries:

```yaml
- name: Query Processing
  description: | 
    Explains what the abstraction does.
    It's like a central dispatcher routing requests.
  file_indices:
    - 0 # path/to/file1.py
    - 3 # path/to/related.py
- name: Query Optimization
  description: |
    Another core concept, similar to a blueprint for objects.
  file_indices:
    - 5 # path/to/another.js
# ... up to 10 abstractions
```"""
        response = call_llm(prompt)

        # --- Validation ---
        yaml_str = response.strip().split("```yaml")[1].split("```")[0].strip()
        abstractions = yaml.safe_load(yaml_str)

        if not isinstance(abstractions, list):
            raise ValueError("LLM Output is not a list")

        validated_abstractions = []
        for item in abstractions:
            if not isinstance(item, dict) or not all(k in item for k in ["name", "description", "file_indices"]):
                raise ValueError(f"Missing keys in abstraction item: {item}")
            if not isinstance(item["description"], str):
                 raise ValueError(f"description is not a string in item: {item}")
            if not isinstance(item["file_indices"], list):
                 raise ValueError(f"file_indices is not a list in item: {item}")

            # Validate indices
            validated_indices = []
            for idx_entry in item["file_indices"]:
                 try:
                     if isinstance(idx_entry, int):
                         idx = idx_entry
                     elif isinstance(idx_entry, str) and '#' in idx_entry:
                          idx = int(idx_entry.split('#')[0].strip())
                     else:
                          idx = int(str(idx_entry).strip())

                     if not (0 <= idx < file_count):
                         raise ValueError(f"Invalid file index {idx} found in item {item['name']}. Max index is {file_count - 1}.")
                     validated_indices.append(idx)
                 except (ValueError, TypeError):
                      raise ValueError(f"Could not parse index from entry: {idx_entry} in item {item['name']}")

            item["files"] = sorted(list(set(validated_indices)))
            # Store only the required fields
            validated_abstractions.append({
                "name": item["name"],
                "description": item["description"],
                "files": item["files"]
            })

        print(f"Identified {len(validated_abstractions)} abstractions.")
        return validated_abstractions

    def post(self, shared, prep_res, exec_res):
        shared["abstractions"] = exec_res # List of {"name": str, "description": str, "files": [int]}

class AnalyzeRelationships(Node):
    def prep(self, shared):
        abstractions = shared["abstractions"] # Now contains 'files' list of indices
        files_data = shared["files"]
        project_name = shared["project_name"]  # Get project name

        # Create context with abstraction names, indices, descriptions, and relevant file snippets
        context = "Identified Abstractions:\n"
        all_relevant_indices = set()
        abstraction_info_for_prompt = []
        for i, abstr in enumerate(abstractions):
            # Use 'files' which contains indices directly
            file_indices_str = ", ".join(map(str, abstr['files']))
            info_line = f"- Index {i}: {abstr['name']} (Relevant file indices: [{file_indices_str}])\n  Description: {abstr['description']}"
            context += info_line + "\n"
            abstraction_info_for_prompt.append(f"{i} # {abstr['name']}")
            all_relevant_indices.update(abstr['files'])

        context += "\nRelevant File Snippets (Referenced by Index and Path):\n"
        # Get content for relevant files using helper
        relevant_files_content_map = get_content_for_indices(
            files_data,
            sorted(list(all_relevant_indices))
        )
        # Format file content for context
        file_context_str = "\n\n".join(
            f"--- File: {idx_path} ---\n{content}"
            for idx_path, content in relevant_files_content_map.items()
        )
        context += file_context_str

        return context, "\n".join(abstraction_info_for_prompt), project_name  # Return project name

    def exec(self, prep_res):
        context, abstraction_listing, project_name = prep_res  # Unpack project name
        print("Analyzing relationships using LLM...")
        prompt = f"""
Based on the following abstractions and relevant code snippets from the project `{project_name}`:

List of Abstraction Indices and Names:
{abstraction_listing}

Context (Abstractions, Descriptions, Code):
{context}

Please provide:
1. A high-level `summary` of the project's main purpose and functionality in a few beginner-friendly sentences. Use markdown formatting with **bold** and *italic* text to highlight important concepts.
2. A list (`relationships`) describing the key interactions between these abstractions. For each relationship, specify:
    - `from_abstraction`: Index of the source abstraction (e.g., `0 # AbstractionName1`)
    - `to_abstraction`: Index of the target abstraction (e.g., `1 # AbstractionName2`)
    - `label`: A brief label for the interaction **in just a few words** (e.g., "Manages", "Inherits", "Uses").
    Ideally the relationship should be backed by one abstraction calling or passing parameters to another.
    Simplify the relationship and exclude those non-important ones.

IMPORTANT: Make sure EVERY abstraction is involved in at least ONE relationship (either as source or target). Each abstraction index must appear at least once across all relationships.

Format the output as YAML:

```yaml
summary: |
  A brief, simple explanation of the project.
  Can span multiple lines with **bold** and *italic* for emphasis.
relationships:
  - from_abstraction: 0 # AbstractionName1
    to_abstraction: 1 # AbstractionName2
    label: "Manages"
  - from_abstraction: 2 # AbstractionName3
    to_abstraction: 0 # AbstractionName1
    label: "Provides config"
  # ... other relationships
```

Now, provide the YAML output:
"""
        response = call_llm(prompt)

        # --- Validation ---
        yaml_str = response.strip().split("```yaml")[1].split("```")[0].strip()
        relationships_data = yaml.safe_load(yaml_str)

        if not isinstance(relationships_data, dict) or not all(k in relationships_data for k in ["summary", "relationships"]):
            raise ValueError("LLM output is not a dict or missing keys ('summary', 'relationships')")
        if not isinstance(relationships_data["summary"], str):
             raise ValueError("summary is not a string")
        if not isinstance(relationships_data["relationships"], list):
             raise ValueError("relationships is not a list")

        # Validate relationships structure
        validated_relationships = []
        num_abstractions = len(abstraction_listing.split('\n'))
        for rel in relationships_data["relationships"]:
             # Check for 'label' key
             if not isinstance(rel, dict) or not all(k in rel for k in ["from_abstraction", "to_abstraction", "label"]):
                  raise ValueError(f"Missing keys (expected from_abstraction, to_abstraction, label) in relationship item: {rel}")
             # Validate 'label' is a string
             if not isinstance(rel["label"], str):
                  raise ValueError(f"Relationship label is not a string: {rel}")

             # Validate indices
             try:
                 from_idx = int(str(rel["from_abstraction"]).split('#')[0].strip())
                 to_idx = int(str(rel["to_abstraction"]).split('#')[0].strip())
                 if not (0 <= from_idx < num_abstractions and 0 <= to_idx < num_abstractions):
                      raise ValueError(f"Invalid index in relationship: from={from_idx}, to={to_idx}. Max index is {num_abstractions-1}.")
                 validated_relationships.append({
                     "from": from_idx,
                     "to": to_idx,
                     "label": rel["label"] 
                 })
             except (ValueError, TypeError):
                  raise ValueError(f"Could not parse indices from relationship: {rel}")

        print("Generated project summary and relationship details.")
        return {
            "summary": relationships_data["summary"],
            "details": validated_relationships # Store validated, index-based relationships
        }


    def post(self, shared, prep_res, exec_res):
        # Structure is now {"summary": str, "details": [{"from": int, "to": int, "label": str}]}
        shared["relationships"] = exec_res

class OrderChapters(Node):
    def prep(self, shared):
        abstractions = shared["abstractions"]
        relationships = shared["relationships"]
        project_name = shared["project_name"]  # Get project name

        # Prepare context for the LLM
        abstraction_info_for_prompt = []
        for i, a in enumerate(abstractions):
            abstraction_info_for_prompt.append(f"- {i} # {a['name']}")
        abstraction_listing = "\n".join(abstraction_info_for_prompt)

        context = f"Project Summary:\n{relationships['summary']}\n\n"
        context += "Relationships (Indices refer to abstractions above):\n"
        for rel in relationships['details']:
             from_name = abstractions[rel['from']]['name']
             to_name = abstractions[rel['to']]['name']
             # Use 'label' instead of 'desc'
             context += f"- From {rel['from']} ({from_name}) to {rel['to']} ({to_name}): {rel['label']}\n"

        return abstraction_listing, context, len(abstractions), project_name

    def exec(self, prep_res):
        abstraction_listing, context, num_abstractions, project_name = prep_res
        print("Determining chapter order using LLM...")
        prompt = f"""
Given the following project abstractions and their relationships for the project ```` {project_name} ````:

Abstractions (Index # Name):
{abstraction_listing}

Context about relationships and project summary:
{context}

If you are going to make a tutorial for ```` {project_name} ````, what is the best order to explain these abstractions, from first to last?
Ideally, first explain those that are the most important or foundational, perhaps user-facing concepts or entry points. Then move to more detailed, lower-level implementation details or supporting concepts.

Output the ordered list of abstraction indices, including the name in a comment for clarity. Use the format `idx # AbstractionName`.

```yaml
- 2 # FoundationalConcept
- 0 # CoreClassA
- 1 # CoreClassB (uses CoreClassA)
- ...
```

Now, provide the YAML output:
"""
        response = call_llm(prompt)

        # --- Validation ---
        # Rely on Node's built-in retry/fallback
        yaml_str = response.strip().split("```yaml")[1].split("```")[0].strip()
        ordered_indices_raw = yaml.safe_load(yaml_str)

        if not isinstance(ordered_indices_raw, list):
            raise ValueError("LLM output is not a list")

        ordered_indices = []
        seen_indices = set()
        for entry in ordered_indices_raw:
            try:
                 if isinstance(entry, int):
                     idx = entry
                 elif isinstance(entry, str) and '#' in entry:
                      idx = int(entry.split('#')[0].strip())
                 else:
                      idx = int(str(entry).strip())

                 if not (0 <= idx < num_abstractions):
                      raise ValueError(f"Invalid index {idx} in ordered list. Max index is {num_abstractions-1}.")
                 if idx in seen_indices:
                     raise ValueError(f"Duplicate index {idx} found in ordered list.")
                 ordered_indices.append(idx)
                 seen_indices.add(idx)

            except (ValueError, TypeError):
                 raise ValueError(f"Could not parse index from ordered list entry: {entry}")

        # Check if all abstractions are included
        if len(ordered_indices) != num_abstractions:
             raise ValueError(f"Ordered list length ({len(ordered_indices)}) does not match number of abstractions ({num_abstractions}). Missing indices: {set(range(num_abstractions)) - seen_indices}")

        print(f"Determined chapter order (indices): {ordered_indices}")
        return ordered_indices # Return the list of indices

    def post(self, shared, prep_res, exec_res):
        # exec_res is already the list of ordered indices
        shared["chapter_order"] = exec_res # List of indices

class WriteChapters(BatchNode):
    def prep(self, shared):
        chapter_order = shared["chapter_order"] # List of indices
        abstractions = shared["abstractions"]   # List of dicts, now using 'files' with indices
        files_data = shared["files"]
        # Get already written chapters to provide context
        # We store them temporarily during the batch run, not in shared memory yet
        # The 'previous_chapters_summary' will be built progressively in the exec context
        self.chapters_written_so_far = [] # Use instance variable for temporary storage across exec calls

        # Create a complete list of all chapters
        all_chapters = []
        chapter_filenames = {} # Store chapter filename mapping for linking
        for i, abstraction_index in enumerate(chapter_order):
            if 0 <= abstraction_index < len(abstractions):
                chapter_num = i + 1
                chapter_name = abstractions[abstraction_index]["name"]
                # Create safe filename
                safe_name = "".join(c if c.isalnum() else '_' for c in chapter_name).lower()
                filename = f"{i+1:02d}_{safe_name}.md"
                # Format with link
                all_chapters.append(f"{chapter_num}. [{chapter_name}]({filename})")
                # Store mapping of chapter index to filename for linking
                chapter_filenames[abstraction_index] = {"num": chapter_num, "name": chapter_name, "filename": filename}
        
        # Create a formatted string with all chapters
        full_chapter_listing = "\n".join(all_chapters)

        items_to_process = []
        for i, abstraction_index in enumerate(chapter_order):
            if 0 <= abstraction_index < len(abstractions):
                abstraction_details = abstractions[abstraction_index]
                # Use 'files' (list of indices) directly
                related_file_indices = abstraction_details.get("files", [])
                # Get content using helper, passing indices
                related_files_content_map = get_content_for_indices(files_data, related_file_indices)
                
                # Get previous chapter info for transitions
                prev_chapter = None
                if i > 0:
                    prev_idx = chapter_order[i-1]
                    prev_chapter = chapter_filenames[prev_idx]
                
                # Get next chapter info for transitions
                next_chapter = None
                if i < len(chapter_order) - 1:
                    next_idx = chapter_order[i+1]
                    next_chapter = chapter_filenames[next_idx]

                items_to_process.append({
                    "chapter_num": i + 1,
                    "abstraction_index": abstraction_index,
                    "abstraction_details": abstraction_details,
                    "related_files_content_map": related_files_content_map,
                    "project_name": shared["project_name"],  # Add project name
                    "full_chapter_listing": full_chapter_listing,  # Add the full chapter listing
                    "chapter_filenames": chapter_filenames,  # Add chapter filenames mapping
                    "prev_chapter": prev_chapter,  # Add previous chapter info
                    "next_chapter": next_chapter,  # Add next chapter info
                    # previous_chapters_summary will be added dynamically in exec
                })
            else:
                print(f"Warning: Invalid abstraction index {abstraction_index} in chapter_order. Skipping.")

        print(f"Preparing to write {len(items_to_process)} chapters...")
        return items_to_process # Iterable for BatchNode

    def exec(self, item):
        # This runs for each item prepared above
        abstraction_name = item["abstraction_details"]["name"]
        chapter_num = item["chapter_num"]
        project_name = item.get("project_name")  # Get from item
        print(f"Writing chapter {chapter_num} for: {abstraction_name} using LLM...")

        # Prepare file context string from the map
        file_context_str = "\n\n".join(
            f"--- File: {idx_path.split('# ')[1] if '# ' in idx_path else idx_path} ---\n{content}"
            for idx_path, content in item["related_files_content_map"].items()
        )

        # Get summary of chapters written *before* this one
        # Use the temporary instance variable
        previous_chapters_summary = "\n---\n".join(self.chapters_written_so_far)


        prompt = f"""
Write a very beginner-friendly tutorial chapter (in Markdown format) for the project `{project_name}` about the concept: "{abstraction_name}". This is Chapter {chapter_num}.

Concept Details:
- Description:
{item["abstraction_details"]["description"]}

Complete Tutorial Structure:
{item["full_chapter_listing"]}

Context from previous chapters (summary):
{previous_chapters_summary if previous_chapters_summary else "This is the first chapter."}

Relevant Code Snippets:
{file_context_str if file_context_str else "No specific code snippets provided for this abstraction."}

Instructions for the chapter:
- Start with a clear heading (e.g., `# Chapter {chapter_num}: {abstraction_name}`).

- If this is not the first chapter, begin with a brief transition from the previous chapter, referencing it with a proper Markdown link.

- Begin with a high-level motivation explaining what problem this abstraction solves. Start with a central use case as a concrete example. The whole chapter should guide the reader to understand how to solve this use case. Make it very minimal and friendly to beginners.

- If the abstraction is complex, break it down into key concepts. Explain each concept one-by-one in a very beginner-friendly way.

- Explain how to use this abstraction to solve the use case. Give example inputs and outputs for code snippets (if the output isn't values, describe at a high level what will happen). 

- Each code block should be BELOW 20 lines! If longer code blocks are needed, break them down into smaller pieces and walk through them one-by-one. Aggresively simplify the code to make it minimal. Use comments to skip non-important implementation details. Each code block should have a beginner friendly explanation right after it.

- Describe the internal implementation to help understand what's under the hood. First provide a non-code or code-light walkthrough on what happens step-by-step when the abstraction is called. It's recommended to use a simple sequenceDiagram with a dummy example - keep it minimal with at most 5 participants to ensure clarity. If participant name has space, use: 
`participant QP as Query Processing`

- Then dive deeper into code for the internal implementation with references to files. Provide example code blocks, but make them similarly simple and beginner-friendly.

- IMPORTANT: When you need to refer to other core abstractions covered in other chapters, ALWAYS use proper Markdown links like this: [Chapter Title](filename.md). Use the Complete Tutorial Structure above to find the correct filename. Example: "we will talk about [Query Processing](03_query_processing.md) in Chapter 3".

- Use mermaid diagrams to illustrate complex concepts (```mermaid``` format).

- Heavily use analogies and examples throughout to help beginners understand.

- End the chapter with a brief conclusion that summarizes what was learned and provides a transition to the next chapter. If there is a next chapter, use a proper Markdown link: [Next Chapter Title](next_chapter_filename).

- Ensure the tone is welcoming and easy for a newcomer to understand.

- Output *only* the Markdown content for this chapter.

Now, directly provide a super beginner-friendly Markdown output (DON'T need ```markdown``` tags):
"""
        chapter_content = call_llm(prompt)
        # Basic validation/cleanup
        actual_heading = f"# Chapter {chapter_num}: {abstraction_name}"
        if not chapter_content.strip().startswith(f"# Chapter {chapter_num}"):
             # Add heading if missing or incorrect, trying to preserve content
             lines = chapter_content.strip().split('\n')
             if lines and lines[0].strip().startswith("#"): # If there's some heading, replace it
                 lines[0] = actual_heading
                 chapter_content = "\n".join(lines)
             else: # Otherwise, prepend it
                 chapter_content = f"{actual_heading}\n\n{chapter_content}"

        # Add the generated content to our temporary list for the next iteration's context
        self.chapters_written_so_far.append(chapter_content)

        return chapter_content # Return the Markdown string

    def post(self, shared, prep_res, exec_res_list):
        # exec_res_list contains the generated Markdown for each chapter, in order
        shared["chapters"] = exec_res_list
        # Clean up the temporary instance variable
        del self.chapters_written_so_far
        print(f"Finished writing {len(exec_res_list)} chapters.")

class CombineTutorial(Node):
    def prep(self, shared):
        project_name = shared.get("project_name", "Unknown Project") # Safer get
        # repo_url is needed for the index header, get it safely
        repo_url = shared.get("repo_url", "") 

        relationships_data = shared.get("relationships", {"summary": "N/A", "details": []}) # Safer get with defaults
        chapter_order = shared.get("chapter_order", []) 
        abstractions = shared.get("abstractions", [])   
        chapters_content = shared.get("chapters", [])   
        
        # Basic check if essential data is missing
        if not chapter_order or not abstractions or not chapters_content:
             print("Warning: Missing essential data (order, abstractions, or chapters) in CombineTutorial.prep. Cannot generate full content.")
             # Still proceed to generate what we can (e.g., index without chapters)
             
        # --- Generate Mermaid Diagram --- 
        mermaid_lines = ["flowchart TD"]
        # Add nodes safely
        for i, abstr in enumerate(abstractions):
            node_id = f"A{i}"
            # Use .get() for safer access to name, provide default
            sanitized_name = abstr.get('name', f'Abstraction_{i}').replace('"', '') 
            node_label = sanitized_name
            mermaid_lines.append(f'    {node_id}["{node_label}"]')
        # Add edges safely
        for rel in relationships_data.get('details', []): # Use .get() for details list
             # Check keys and types robustly before accessing indices
            if isinstance(rel, dict) and \
               all(k in rel for k in ['from', 'to', 'label']) and \
               isinstance(rel.get('from'), int) and isinstance(rel.get('to'), int) and \
               0 <= rel['from'] < len(abstractions) and 0 <= rel['to'] < len(abstractions):
                
                from_node_id = f"A{rel['from']}"
                to_node_id = f"A{rel['to']}"
                # Ensure label is a string before replacing
                edge_label = str(rel.get('label', '')).replace('"', '').replace('\\n', ' ') 
                max_label_len = 30 
                if len(edge_label) > max_label_len:
                    edge_label = edge_label[:max_label_len-3] + "..."
                mermaid_lines.append(f'    {from_node_id} -- "{edge_label}" --> {to_node_id}')
            else:
                print(f"Warning: Skipping invalid relationship detail: {rel}")
                
        mermaid_diagram = "\\n".join(mermaid_lines)
        # --- End Mermaid ---

        # Prepare index.md content string
        index_content_str = f"# Tutorial: {project_name}\\n\\n"
        index_content_str += f"{relationships_data.get('summary', 'Project summary not available.')}\\n\\n" # Use .get()
        if repo_url: # Only add if repo_url is not empty
            index_content_str += f"**Source Repository:** [{repo_url}]({repo_url})\\n\\n"
        else:
             index_content_str += "**Source Repository:** Not specified\\n\\n"
             
        index_content_str += "```mermaid\\n"
        index_content_str += mermaid_diagram + "\\n"
        index_content_str += "```\\n\\n"
        index_content_str += "## Chapters\\n\\n"

        # Dictionary to hold filename -> content strings
        markdown_files_content = {}
        valid_chapters_count = 0

        # Generate chapter links and store chapter content in the dictionary
        for i, abstraction_index in enumerate(chapter_order):
            # Robust check for valid indices and available content
            if isinstance(abstraction_index, int) and \
               0 <= abstraction_index < len(abstractions) and \
               i < len(chapters_content):
                 
                abstraction_details = abstractions[abstraction_index]
                # Safer access to name
                abstraction_name = abstraction_details.get("name", f"Chapter {i+1}") 
                # Sanitize name and limit length for filename
                safe_name = "".join(c if c.isalnum() else '_' for c in abstraction_name).lower()
                filename = f"{i+1:02d}_{safe_name[:50]}.md" # Limit base name length
                
                index_content_str += f"{i+1}. [{abstraction_name}]({filename})\\n" 
                
                # Get chapter content (no attribution added here)
                chapter_content_str = chapters_content[i] 
                markdown_files_content[filename] = chapter_content_str
                valid_chapters_count += 1
            else:
                 print(f"Warning: Invalid index or missing content at chapter pos {i} (abs_idx: {abstraction_index}). Skipping.")

        # Add final attribution to index content (optional)
        index_content_str += "\\n\\n---\\n\\nGenerated by [AI Codebase Knowledge Builder](https://github.com/The-Pocket/Tutorial-Codebase-Knowledge)"
        markdown_files_content["index.md"] = index_content_str

        print(f"Prepared content for index.md and {valid_chapters_count} chapter file(s) in memory.")
        # Return the dictionary containing all markdown content
        return {
            "markdown_files_content": markdown_files_content
        }

    def exec(self, prep_res):
        # prep_res should contain {"markdown_files_content": {...}}
        # No file writing needed, just pass the content dictionary through
        print("Passing prepared markdown content dictionary...")
        # Handle case where prep might have returned an empty dict due to errors
        return prep_res.get("markdown_files_content", {}) 

    def post(self, shared, prep_res, exec_res):
        # exec_res is the dictionary {"filename": content, ...}
        shared["markdown_files_content"] = exec_res 
        # Remove the old output dir key if it exists to avoid confusion
        shared.pop("final_output_dir", None) 
        print(f"\\nTutorial content generated successfully and stored in memory under 'markdown_files_content'. Contains {len(exec_res)} entries.")