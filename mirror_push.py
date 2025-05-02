import os
import subprocess
from github import Github
from github import Auth
from github.GithubException import GithubException

SOURCE_BRANCH = os.getenv("SOURCE_BRANCH")
TARGET_BRANCH = os.getenv("TARGET_BRANCH", SOURCE_BRANCH)
SOURCE_REPO = os.getenv("SOURCE_REPO")  # format: org/source-repo
TARGET_REPO = os.getenv("TARGET_REPO")  # format: org/target-repo
TOKEN = os.getenv("MIRROR_TOKEN")

def run(cmd, cwd=None):
    print(f"Running: {cmd}")
    subprocess.run(cmd, shell=True, check=True, cwd=cwd)

def branch_exists(repo, branch_name):
    try:
        repo.get_branch(branch_name)
        return True
    except GithubException:
        return False

def create_branch_from(repo_src, repo_dst, src_branch, dst_branch):
    print(f"Creating branch '{dst_branch}' in target repo from source '{src_branch}'")
    src_ref = repo_src.get_git_ref(f"heads/{src_branch}")
    repo_dst.create_git_ref(ref=f"refs/heads/{dst_branch}", sha=src_ref.object.sha)

def main():
    gh = Github(auth=Auth.Token(TOKEN))
    repo_src = gh.get_repo(SOURCE_REPO)
    repo_dst = gh.get_repo(TARGET_REPO)

    # Vérifie si le script s'exécute dans le dépôt cible
    current_repo_url = subprocess.check_output("git config --get remote.origin.url", shell=True).decode().strip()

    if TARGET_REPO in current_repo_url:
        print("🛑 Ce script est en cours d'exécution dans le dépôt cible. Arrêt.")
        exit(0)

    # Vérifie si la branche source existe
    if not branch_exists(repo_src, SOURCE_BRANCH):
        print(f"❌ La branche source '{SOURCE_BRANCH}' n'existe pas dans {SOURCE_REPO}")
        exit(1)

    # Crée la branche cible si nécessaire
    if not branch_exists(repo_dst, TARGET_BRANCH):
        print(f"ℹ️ La branche '{TARGET_BRANCH}' n'existe pas dans {TARGET_REPO}, création en cours...")
        create_branch_from(repo_src, repo_dst, SOURCE_BRANCH, TARGET_BRANCH)

    # Cloner depuis le dépôt source
    repo_url_source = f"https://x-access-token:{TOKEN}@github.com/{SOURCE_REPO}.git"
    run(f"git clone --single-branch --branch {SOURCE_BRANCH} {repo_url_source} repo")

    # Ajouter remote et push forcé vers la branche cible
    repo_url_target = f"https://x-access-token:{TOKEN}@github.com/{TARGET_REPO}.git"
    run(f"git remote add mirror {repo_url_target}", cwd="repo")
    run("git remote -v", cwd="repo")
    run(f"git push mirror {SOURCE_BRANCH}:{TARGET_BRANCH} --force", cwd="repo")

if __name__ == "__main__":
    main()
