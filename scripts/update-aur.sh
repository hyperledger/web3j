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

STATUS_CODE=`curl --silent --connect-timeout 8 --output /dev/null https://github.com/web3j/web3j/releases/download/v${VERSION}/web3j-${VERSION}.zip -I -w "%{http_code}\n"`

echo $STATUS_CODE

if [[ $STATUS_CODE -ne "302" ]]; then
    echo "ERROR: Missing release has the version ${VERSION} been released yet?"
    exit 1
fi

configure_github_user

echo $AUR_SSH > ~/.ssh/aur.pub
echo $AUR_SSH_PRIV > ~/.ssh/aur

echo "Host aur.archlinux.org
  IdentityFile ~/.ssh/aur
  User aur" > ~/.ssh/config

git clone ssh://aur@aur.archlinux.org/web3j.git

cd web3j || echo "ERROR: Failed to clone"; exit 1

./update $VERSION