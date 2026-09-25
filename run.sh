#!/bin/bash
set -e
cd "$(dirname "$0")"

if command -v mvn &>/dev/null; then
    mvn spring-boot:run
elif [ -f "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn" ]; then
    "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn" spring-boot:run
else
    echo "Maven not found. Install Maven or open the project in IntelliJ IDEA."
    exit 1
fi
