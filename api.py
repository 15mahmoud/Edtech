import os
import sys
from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel, HttpUrl
import uvicorn
import dotenv
import logging
from pathlib import Path

# Add project root to sys.path to allow importing flow and nodes
project_root = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, project_root)

# Import the flow creation function and default patterns
from flow import create_tutorial_flow
from main import DEFAULT_INCLUDE_PATTERNS, DEFAULT_EXCLUDE_PATTERNS

dotenv.load_dotenv()

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI()

class TutorialRequest(BaseModel):
    repo_url: HttpUrl
    github_token: str | None = None
    project_name: str | None = None
    output_dir: str = "output"
    include_patterns: set[str] | None = DEFAULT_INCLUDE_PATTERNS
    exclude_patterns: set[str] | None = DEFAULT_EXCLUDE_PATTERNS
    max_file_size: int = 100000

@app.post("/generate_tutorial/")
async def generate_tutorial(request: TutorialRequest):
    """
    Endpoint to generate a tutorial for a given GitHub repository.
    Takes a JSON body with 'repo_url' and optional parameters.
    """
    logger.info(f"Received request for repo: {request.repo_url}")

    # Determine GitHub token
    github_token = request.github_token or os.environ.get('GITHUB_TOKEN')
    if not github_token:
        logger.warning("No GitHub token provided in request or environment variables. Rate limits may apply.")

    # Initialize the shared dictionary
    shared = {
        "repo_url": str(request.repo_url), # Convert Pydantic HttpUrl to string
        "local_dir": None, # API only supports repo URL for now
        "project_name": request.project_name,
        "github_token": github_token,
        "output_dir": request.output_dir,
        "include_patterns": request.include_patterns if request.include_patterns else DEFAULT_INCLUDE_PATTERNS,
        "exclude_patterns": request.exclude_patterns if request.exclude_patterns else DEFAULT_EXCLUDE_PATTERNS,
        "max_file_size": request.max_file_size,
        "files": [],
        "abstractions": [],
        "relationships": {},
        "chapter_order": [],
        "chapters": [],
        "final_output_dir": None
    }

    try:
        # Create and run the flow
        logger.info("Creating and running the tutorial flow...")
        tutorial_flow = create_tutorial_flow()
        # Note: Running the flow might be synchronous and block the event loop.
        # For long-running tasks, consider using background tasks (e.g., Celery, FastAPI's BackgroundTasks).
        tutorial_flow.run(shared)
        logger.info("Tutorial flow completed.")

        # --- Retrieve in-memory content --- 
        markdown_files = shared.get("markdown_files_content")
        
        if not markdown_files or not isinstance(markdown_files, dict):
            logger.error(f"Markdown content not found or not in expected format in shared state.")
            raise HTTPException(status_code=500, detail="Failed to retrieve generated tutorial content.")
        
        logger.info(f"Retrieved content for {len(markdown_files)} markdown files from memory.")

        # Return the content of the files
        return {
            "message": "Tutorial generated successfully",
            "data": markdown_files # Dictionary {filename: content}
        }
        # --- End Retrieve in-memory content ---

    except Exception as e:
        logger.exception(f"Error during tutorial generation for {request.repo_url}") # Log traceback
        raise HTTPException(status_code=500, detail=f"Internal server error during tutorial generation: {e}")

if __name__ == "__main__":
    # Get port from environment variable or default to 8000
    port = int(os.environ.get("PORT", 8000))
    # Use 0.0.0.0 to make it accessible on the network
    uvicorn.run(app, host="0.0.0.0", port=port)
