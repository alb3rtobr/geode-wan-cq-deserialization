#!/bin/bash

GEODE_HOME=$1

javac -cp ".:$GEODE_HOME/../lib/geode-core-1.14.0-build.0.jar:" ExampleCacheListener.java ClientB.java ExamplePdxSerializable.java

java -cp ".:$GEODE_HOME/../lib/geode-core-1.14.0-build.0.jar:$GEODE_HOME/../lib/geode-membership-1.14.0-build.0.jar:$GEODE_HOME/../lib/geode-serialization-1.14.0-build.0.jar:$GEODE_HOME/../lib/geode-logging-1.14.0-build.0.jar:$GEODE_HOME/../lib/geode-management-1.14.0-build.0.jar:$GEODE_HOME/../lib/geode-tcp-server-1.14.0-build.0.jar:$GEODE_HOME/../lib/geode-common-1.14.0-build.0.jar" ClientB
