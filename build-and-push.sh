#!/bin/bash
set -e

# Usage: ./build-and-push.sh <registry/repo:tag>
# Example: ./build-and-push.sh your-dockerhub-username/appender-java:latest

IMAGE_NAME="achalbi/appender-java:latest"
PUSH_IMAGE="$IMAGE_NAME"

if [ -z "$PUSH_IMAGE" ]; then
  echo "Usage: $0 <registry/repo:tag>"
  echo "Example: $0 achalbi/appender-java:latest"
  exit 1
fi

echo "[1/2] Building local image: $IMAGE_NAME"
docker build -t "$IMAGE_NAME" .

echo "[2/2] Pushing image to: $PUSH_IMAGE"
docker push "$PUSH_IMAGE"

echo "Done." 