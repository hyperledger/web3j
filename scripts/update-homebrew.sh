#!/bin/bash
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done

trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT

export DEPLOY_ROOT_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

source "$DEPLOY_ROOT_DIR/common.bash"

if [[ -z "$VERSION" ]]; then
    VERSION="${CI_COMMIT_REF_NAME//release\/}"
fi

if [[ "$VERSION" = "" ]]; then
    echo "ERROR: Missing VERSION specify it using an env variable"
    exit 1
fi

STATUS_CODE=$(wget --server-response --spider --quiet "https://github.com/web3j/web3j/releases/download/v${VERSION}/web3j-${VERSION}.zip" 2>&1 | awk 'NR==1{print $2}')

if [[ $STATUS_CODE -ne "200" ]]; then
    echo "ERROR: Missing release has the version been released yet?"
    exit 1
fi

cd /tmp
rm -Rf homebrew-web3j

configure_github_user

github_clone "homebrew-web3j"

sed -i "5s/.*/  url \"https:\/\/github.com\/web3j\/web3j\/releases\/download\/v${VERSION}\/web3j-${VERSION}.zip\"/" web3j.rb
SHA=$(curl -L https://github.com/web3j/web3j/releases/download/v${VERSION}/web3j-${VERSION}.zip | shasum -a 256 | tr -d ' ' | tr -d '-')
sed -i "7s/.*/  sha256 \"${SHA}\"/" web3j.rb
git commit -am "Change web3j version to ${VERSION}"
git push
