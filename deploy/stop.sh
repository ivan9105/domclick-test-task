#!/bin/bash
docker ps | grep domclick | awk '{print $1}' | xargs docker stop