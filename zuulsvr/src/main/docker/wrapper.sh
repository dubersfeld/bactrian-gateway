#!/bin/sh

while ! `nc -z $CONFIGSERVER_HOST $CONFIGSERVER_PORT`; do 
    echo "********************************************************"
    echo "Waiting for the configuration server to start on port 8888"
    echo "********************************************************"
    sleep 4; 
done

echo "Config Server $CONFIGSERVER_HOST up and running at $CONFIGSERVER_PORT"

echo "********************************************************"
echo "Starting Bactrian Zuul Server"
echo "********************************************************"


java -Djava.security.egd=file:/dev/./urandom -Dspring.cloud.config.uri=$BACTRIAN_CONFIG_URI -Dspring.profiles.active=$PROFILE -jar /app.jar

