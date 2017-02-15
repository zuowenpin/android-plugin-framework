#!/usr/bin/env bash
set -u
set -e
plugin='plugina'

#build plugin_base
cd ..
./gradlew clean
./gradlew assembleDebug
cd -
cp -f ../plugin_base/build/outputs/aar/plugin_base-debug.aar libs/plugin_base.aar
trap " rm -rf libs/*.aar" EXIT

cd ..
./gradlew -b ${plugin}/build-apk.gradle assembleDebug
cd -
cp -rf build/outputs/apk/${plugin}-debug.apk  "../app/assets/${plugin}.jar"
md5=`md5sum build/outputs/apk/${plugin}-debug.apk|cut -d' ' -f1`
echo ${md5}>"../app/assets/${plugin}.md5"
echo 'done'