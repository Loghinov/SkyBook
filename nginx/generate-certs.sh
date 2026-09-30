#!/bin/sh
# Run once before first `docker compose up`:
#   sh nginx/generate-certs.sh
set -e
CERTS_DIR="$(dirname "$0")/certs"
mkdir -p "$CERTS_DIR"
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout "$CERTS_DIR/localhost.key" \
  -out    "$CERTS_DIR/localhost.crt" \
  -subj "/CN=localhost"
echo "Certificates generated in $CERTS_DIR"