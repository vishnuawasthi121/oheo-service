#!/bin/bash
ACTIVE_PROFILE="$1"
echo "Staring application in profile : $ACTIVE_PROFILE"
nohup java -jar oheo-service-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=$ACTIVE_PROFILE -Dspring-boot.run.jvmArguments="-Xms10m -Xmx50m" > ./stdout.log 2>&1 &

echo $! > ./app.pid

if [ "$?" -eq 0 ];
then
    echo "Service started successfully!"
    exit 0
else
    echo "Service did not start. Please check service status!"
    exit 1
fi


