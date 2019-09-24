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

export SCRIPTS_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

source "$SCRIPTS_DIR/common.bash"

ensure_version

export PREVIOUS_RELEASE=$(curl -H "Authorization: token ${GITHUB_PERSONAL_ACCESS_TOKEN}" -s https://api.github.com/repos/web3j/web3j/releases/latest | jq -r '.target_commitish' )
export CHANGELOG=$(git rev-list --format=oneline --abbrev-commit --max-count=50 ${PREVIOUS_RELEASE}..HEAD | jq --slurp --raw-input . )

echo "Creating a new release on GitHub"
API_JSON="{
  \"tag_name\": \"v${VERSION}\",
  \"target_commitish\": \"$(git rev-parse HEAD)\",
  \"name\": \"v${VERSION}\",
  \"body\": \"Release of version ${VERSION}: \n\n ${CHANGELOG:1:-1}\",
  \"draft\": false,
  \"prerelease\": false
}"

export RESULT=$(curl -H "Authorization: token ${GITHUB_PERSONAL_ACCESS_TOKEN}" --data "$API_JSON" -s https://api.github.com/repos/web3j/web3j/releases)

export UPLOAD_URL=$(echo ${RESULT} | jq -r ".upload_url")

mkdir -p "${SCRIPTS_DIR}/build"

curl -L -s -o "${SCRIPTS_DIR}/build/web3j-${VERSION}.tar" "https://dl.bintray.com/web3j/maven/org/web3j/console/${VERSION}/console-${VERSION}.tar",
curl -L -s -o "${SCRIPTS_DIR}/build/web3j-${VERSION}.tar.asc" "https://dl.bintray.com/web3j/maven/org/web3j/console/${VERSION}/console-${VERSION}.tar.asc",
curl -L -s -o "${SCRIPTS_DIR}/build/web3j-${VERSION}.zip" "https://dl.bintray.com/web3j/maven/org/web3j/console/${VERSION}/console-${VERSION}.zip",
curl -L -s -o "${SCRIPTS_DIR}/build/web3j-${VERSION}.zip.asc" "https://dl.bintray.com/web3j/maven/org/web3j/console/${VERSION}/console-${VERSION}.zip.asc"

for FILE in `find ${SCRIPTS_DIR}/build -type f -name "web3j-*${VERSION}.*"`;
do
  curl -H "Authorization: token ${GITHUB_PERSONAL_ACCESS_TOKEN}" -s "${UPLOAD_URL:0:-13}?name=$(basename -- $FILE)" -H "Content-Type: $(file -b --mime-type $FILE)" --data-binary @"${FILE}"
done
