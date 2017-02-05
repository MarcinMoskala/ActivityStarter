#!/usr/bin/env bash

gradle :activitystarter:test -i

# Needs opened Device
gradle :sample:app:connectedDebugAndroidTest
gradle :sample:kotlinapp:connectedDebugAndroidTest