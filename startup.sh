#!/bin/bash
echo "Executing startup script..."

# Start the SSH daemon
/usr/sbin/sshd

# Start the Java app (full path given)
java -jar usr/src/app.jar