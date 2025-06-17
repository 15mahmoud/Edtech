# AI Codebase Tutor: Generate Tutorials Instantly

Ever felt lost jumping into a complex codebase? This project uses the power of AI to automatically analyze GitHub repositories (or local code) and generate clear, beginner-friendly tutorials – all served through a convenient API!

Forget cloning and manual steps; get structured markdown tutorials explaining core concepts and code relationships directly from an endpoint.


Built with [Pocket Flow](https://github.com/The-Pocket/PocketFlow), this project demonstrates how AI can rapidly understand and document software projects.

## Features

* AI-Powered Analysis: Understands code structure, identifies key abstractions, and explains their relationships.
* FastAPI Integration: Runs as a web service for easy integration and on-demand tutorial generation.
* In-Memory Processing: Generates tutorials entirely in memory – perfect for serverless or containerized deployments. No intermediate files needed!
* JSON Output: Returns the complete tutorial (index and chapters) as structured JSON, ready for display or further processing.
* Supports GitHub & Local: Analyze public GitHub repositories or codebases on your local machine.

## Setup

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/The-Pocket/Tutorial-Codebase-Knowledge.git
   cd Tutorial-Codebase-Knowledge
   ```

2. **Install Dependencies:**

   ```bash
   pip install -r requirements.txt
   ```

3. **Configure Environment Variables:**

   Create a `.env` file in the project root directory and add your API keys:

   ```dotenv
   # Required: For Google Gemini API access
   GEMINI_API_KEY=YOUR_GEMINI_API_KEY_HERE

   # Optional: For accessing private GitHub repositories
   GITHUB_TOKEN=YOUR_GITHUB_TOKEN_HERE
   ```

   *(Ensure your Gemini API key is set up correctly in `utils/call_llm.py` if you modified the default client.)*

## Usage: API Mode (Recommended)

1. **Start the FastAPI Server:**

   ```bash
   uvicorn api:app --reload
   ```

   The server will start, usually at `http://127.0.0.1:8000`.

2. **Generate a Tutorial via API:**

   Send a POST request to the `/generate_tutorial/` endpoint with the target repository URL.

   * **Using `curl` (Command Prompt):**

     ```bash
     curl -X POST "http://127.0.0.1:8000/generate_tutorial/" ^
          -H "Content-Type: application/json" ^
          -d "{\"repo_url\": \"https://github.com/The-Pocket/PocketFlow\"}"
     ```

     *(Use `` ` `` for line breaks in PowerShell/Git Bash)*

   * **Using PowerShell:**

     ```powershell
     Invoke-WebRequest -Uri "http://127.0.0.1:8000/generate_tutorial/" -Method POST -ContentType "application/json" -Body '{"repo_url": "https://github.com/The-Pocket/PocketFlow"}'
     ```

   * **Using the Interactive Docs:**

     Open [http://127.0.0.1:8000/docs](http://127.0.0.1:8000/docs) in your browser, find the `/generate_tutorial/` endpoint, click "Try it out", enter the JSON body (`{"repo_url": "YOUR_REPO_URL"}`), and click "Execute".

3. **Receive the Tutorial:**

   The API will return a JSON response containing the generated tutorial files:

   ```json
   {
     "message": "Tutorial generated successfully",
     "tutorial_files": {
       "index.md": "# Tutorial: Project Name...",
       "01_concept_one.md": "# Chapter 1: Concept One...",
       "02_concept_two.md": "# Chapter 2: Concept Two...",
       // ... etc.
     }
   }
   ```

## Legacy CLI Mode (Optional)

You can still generate tutorial files directly to disk using the original command-line script:

```bash
# Analyze a GitHub repository and write files to ./output/repo_name
python main.py --repo https://github.com/username/repo

# Analyze a local directory
python main.py --dir /path/to/local/code --output ./my_tutorials
```

*(See `python main.py --help` for all options.)*
