#!/bin/bash

# Needs opened Device
if ./gradlew :activitystarter:test -i && ./gradlew :activitystarter-compiler:test -i && ./gradlew :sample:app:connectedDebugAndroidTest && ./gradlew :sample:kotlinapp:connectedDebugAndroidTest;
then
   echo "Success";
else
   echo "Nope";
fi