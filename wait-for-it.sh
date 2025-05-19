#!/usr/bin/env bash

host="$1"
port="$2"
shift 2

echo "Waiting for $host:$port to become available..."

while ! nc -z "$host" "$port"; do
  echo "Still waiting for $host:$port..."
  sleep 1
done

echo "$host:$port is available — continuing"
exec "$@"
