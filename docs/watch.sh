#!/bin/bash

command -v watchmedo >/dev/null 2>&1 || { printf >&2 "Please install watch via pip:\npip install watch\n"; exit 1; }

make clean html

watchmedo shell-command \
              --patterns='*.rst;*.py' \
              --ignore-pattern='_build/*' \
              --recursive \
              --command='make html' \
              --wait \
              source

