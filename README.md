

Setup cluster (2 sites, 1 locator + 1 server per site, gw senders and receivers, /test-region):
```
$ ./start.sh
```

Stop and destroy cluster:
```
$ ./stop.sh
```

Compile clients (just change X by 1 or 2):
```
$ cd cpp/ClientX
$ mkdir build
$ cd build
$ cmake ..
$ cmake --build .
```
