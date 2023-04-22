#!/bin/bash
ACTIVE_PROFILE="$0"

echo "Staring application in profile : $ACTIVE_PROFILE"

nohup java -jar target/oheo-service-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=$ACTIVE_PROFILE > ./stdout.log 2>&1 &

if [ "$?" -eq 0 ];
then
    echo "Service started successfully!"
    exit 0
else
    echo "Service did not start. Please check service status!"
    exit 1
fi

echo $! > ./app.pid
