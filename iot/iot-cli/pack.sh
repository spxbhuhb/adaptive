#/bin/bash

# $1 = project dir
# $2 = version

cd $1

if [ ! -d "./build/app" ]; then
  echo "Directory ./build/app does not exist."
  exit 1
fi

rm -rf ./build/app/$2/*
rm ./build/app/aio-cli-$2.zip

mkdir ./build/app/$2
cp ./build/libs/adaptive-iot-cli-jvm-$2-all.jar ./build/app/$2

cd ./build/app/$2
zip -r ../aio-cli-$2.zip *

if [ -d "$HOME/Desktop" ]; then
  cp ../aio-cli-$2.zip ~/Desktop
else
  echo "Warning: ~/Desktop directory does not exist. Skipping copy."
fi