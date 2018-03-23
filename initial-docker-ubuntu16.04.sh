#!/usr/bin/env bash
apt-get update
apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common

wget https://download.docker.com/linux/ubuntu/gpg
apt-key add gpg
echo "deb [arch=amd64] https://download.docker.com/linux/ubuntu xenial stable" | sudo tee -a /etc/apt/sources.list.d/docker.list
apt-get update
apt-get -y install docker-ce
chmod 777 /var/run/docker.sock