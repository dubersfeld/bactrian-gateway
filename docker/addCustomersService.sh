#!/bin/bash

docker run --rm -it \
--network docker_default \
-e CONFIGSERVER_HOST=bactrian-config \
-e CONFIGSERVER_PORT=8888 \
-e DATABASE_HOST=customers-mysql \
-e DATABASE_PORT=3306 \
-e BACTRIAN_CONFIG_URI=http://bactrian-config:8888 \
-e PROFILE=dev \
-e ENABLE_SLOW=false \
bactrian/customers-service2



