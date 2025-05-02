import os
import subprocess

SOURCE_BRANCH = os.getenv("SOURCE_BRANCH")
TARGET_REPO = os.getenv("TARGET_REPO")
SOURCE_REPO = os.getenv("SOURCE_REPO")
TARGET_BRANCH = os.getenv("TARGET_BRANCH", SOURCE_BRANCH)
TOKEN = os.getenv("MIRROR_TOKEN")

def run(cmd, cwd=None):
    print(f"Running: {cmd}")
    subprocess.run(cmd, shell=True, check=True, cwd=cwd)

def main():
    
    
    repo_url_source = f"https://x-access-token:{TOKEN}@github.com/{TARGET_REPO}.git"    
    run(f"git clone --single-branch --branch {SOURCE_BRANCH} {repo_url_source} repo")

    repo_url_target = f"https://x-access-token:{TOKEN}@github.com/{TARGET_REPO}.git"    
    run(f"git remote add mirror {repo_url_target}", cwd="repo")
    
    run("git remote -v", cwd="repo")
    
    run(f"git push mirror {SOURCE_BRANCH}:{TARGET_BRANCH} --force", cwd="repo")

if __name__ == "__main__":
    main()