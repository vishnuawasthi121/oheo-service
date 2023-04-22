#!/bin/bash
kill $(cat ./app.pid)
if [ "$?" -eq 0 ];
then
    echo "Service stopped successfully!"
    exit 0
else
    echo "Service did not stop. Please check service status!"
    exit 1
fi