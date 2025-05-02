import os
import subprocess

SOURCE_BRANCH = os.getenv("SOURCE_BRANCH")
TARGET_REPO = os.getenv("TARGET_REPO")
TARGET_BRANCH = os.getenv("TARGET_BRANCH", SOURCE_BRANCH)
TOKEN = os.getenv("MIRROR_TOKEN")

def run(cmd, cwd=None):
    print(f"Running: {cmd}")
    subprocess.run(cmd, shell=True, check=True, cwd=cwd)

def main():
    repo_url = f"https://x-access-token:{TOKEN}@github.com/{TARGET_REPO}.git"    
    run(f"git clone --single-branch --branch {SOURCE_BRANCH} . repo")
    run(f"cd repo && git remote add mirror {repo_url}")
    run(f"cd repo && git push mirror {SOURCE_BRANCH}:{TARGET_BRANCH} --force")

if __name__ == "__main__":
    main()