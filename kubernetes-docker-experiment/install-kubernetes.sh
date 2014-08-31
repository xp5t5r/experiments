#!/bin/bash

# Install Kubernetes binaries
sudo mkdir -p /opt/bin
sudo wget -q http://storage.googleapis.com/kubernetes/binaries.tar.gz
sudo tar -xvf binaries.tar.gz -C /opt/bin

# Add the Kubernetes systemd units
git clone https://github.com/kelseyhightower/kubernetes-coreos.git
sudo cp kubernetes-coreos/units/* /etc/systemd/system/

# Start the Kubernetes services
sudo systemctl start apiserver
sudo systemctl start controller-manager
sudo systemctl start kubelet
sudo systemctl start proxy
