This repository contains a test application to reproduce [GEODE-8344](https://issues.apache.org/jira/browse/GEODE-8344)


### Setup cluster
2 sites, 1 locator + 1 server per site, gw senders and receivers, `/multisiteData` region:
```
$ ./start.sh
```

### Compile clients

Substitute X by 1 or 2:
```
$ cd cpp/ClientX
$ mkdir build
$ cd build
$ cmake ..
$ cmake --build .
```

### Reproduce the issue

Start Client2 first. When Client1 is executed, the serialization exception can be seen on logs.


### Stop and destroy cluster:
```
$ ./stop.sh
```
