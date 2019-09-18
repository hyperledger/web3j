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