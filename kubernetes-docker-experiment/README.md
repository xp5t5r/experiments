A playground for test driving Kubernetes and Docker (on CoreOS).

Note. This is basically a "vagrantified" version of [Kubernetes CoreOS - Quick Start](https://github.com/kelseyhightower/kubernetes-coreos#quick-start)

#### Prerequisites
* Vagrant 1.6.3
* Virtualbox 4.3.12

#### Usage
```
vagrant up
vagrant ssh
docker pull dockerfile/redis
kubecfg list pods
kubecfg -c redis.json create pods
docker run -t -i dockerfile/redis /usr/local/bin/redis-cli -h 172.17.42.1
```
