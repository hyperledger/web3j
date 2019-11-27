set -eo pipefail

[[ "$TRACE" ]] && set -x

configure_github_user() {
    git config --global user.email "git@antonydenyer.co.uk"
    git config --global user.name "Antony Denyer"
}

github_clone() {
    git clone https://antonydenyer:${GITHUB_PERSONAL_ACCESS_TOKEN}@github.com/web3j/$1.git
    cd $1
}

ensure_version() {
    if [[ -z "$VERSION" ]]; then
        VERSION="${TRAVIS_BRANCH//release\/}"
    fi

    if [[ "$VERSION" = "" ]]; then
        echo "ERROR: Missing VERSION specify it using an env variable"
        exit 1
    fi
}

ensure_product() {
    if [[ -z "$PRODUCT" ]]; then
        PRODUCT="${TRAVIS_REPO_SLUG//release\/}"
    fi

    if [[ "$PRODUCT" = "" ]]; then
        echo "ERROR: Missing PRODUCT specify it using an env variable"
        exit 1
    fi
}
