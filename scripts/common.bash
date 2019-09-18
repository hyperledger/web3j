set -eo pipefail

[[ "$TRACE" ]] && set -x

configure_github_user() {
    git config --global user.email "email@antonydenyer.com"
    git config --global user.name "Antony Denyer"
}

github_clone() {
    git clone https://antonydenyer:${GITHUB_PERSONAL_ACCESS_TOKEN}@github.com/web3j/$1.git
    cd $1
}

ensure_version() {
    if [[ -z "$VERSION" ]]; then
        VERSION="${CI_COMMIT_REF_NAME//release\/}"
    fi

    if [[ "$VERSION" = "" ]]; then
        echo "ERROR: Missing VERSION specify it using an env variable"
        exit 1
    fi
}