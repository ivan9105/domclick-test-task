#!/usr/bin/env bash

IMAGE_NAME=domclick
TAG=latest


DOCKER_DIR=../docker

DOCKER_USER=$(cat config/docker_login.json | jq '.login' | sed "s/\"/ /g" | tr -d [:space:])
DOCKER_PASSWORD=$(cat config/docker_login.json | jq '.password' | sed "s/\"/ /g" | tr -d [:space:])

docker login -p $DOCKER_PASSWORD -u $DOCKER_USER https://index.docker.io/v1/
docker tag domclick $DOCKER_USER/domclick:latest
docker push $DOCKER_USER/domclick:latest
docker logout https://index.docker.io/v1/
kubectl create secret docker-registry docker-hub-key --docker-server=https://index.docker.io/v1/ --docker-username=$DOCKER_USER --docker-password=$DOCKER_PASSWORD
helm del --purge domclick
helm install --name domclick . --set service.type=NodePort
helm upgrade domclick --kube-context minikube --reset-values --values values.yaml -i --force --set docker.user="$DOCKER_USER" --debug .