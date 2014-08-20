#!/bin/bash

scp "./out/artifacts/percival.jar/percival.jar" root@samism.net:../var/www/html/
ssh root@samism.net 'java -jar "../var/www/html/percival.jar"'