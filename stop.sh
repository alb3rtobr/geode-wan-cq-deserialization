#!/bin/bash

gfsh < ./gfsh/siteA-stop.gfsh
gfsh < ./gfsh/siteB-stop.gfsh

rm -rf siteA-locator
rm -rf siteA-server1
rm -rf siteA-server2
rm -rf siteB-locator
rm -rf siteB-server1
rm -rf siteB-server2
