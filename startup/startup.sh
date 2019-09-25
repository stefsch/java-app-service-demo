#!/bin/sh
echo "Executing startup script..."

# Start the SSH daemon
/usr/sbin/sshd

echo "working directory: $(pwd)"
echo "contents:"
echo "$(ls)"

# Start the Java app (full path given)
java -jar app.jar