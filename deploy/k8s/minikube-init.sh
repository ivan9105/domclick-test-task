#!/usr/bin/env bash

minikube delete
eval $(minikube docker-env)
minikube start
minikube addons enable ingress
#see helm init --output yaml
kubectl apply -f helm/patched_tiller_deployment.yaml

#TOdo elk stack + kafka appender