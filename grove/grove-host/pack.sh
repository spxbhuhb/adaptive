#/bin/bash

# $1 = project dir
# $2 = version

cd $1

if [ ! -d "./build/app" ]; then
  echo "Directory ./build/app does not exist."
  exit 1
fi

rm -rf ./build/app/$2/*
rm ./build/app/supervisor-$2.zip

mkdir ./build/app/$2/etc
cp ./etc/supervisor.properties ./build/app/$2/etc/supervisor.properties

mkdir ./build/app/$2/lib
cp ./build/libs/supervisor-jvm-$2-all.jar ./build/app/$2/lib

mkdir ./build/app/$2/var

mkdir ./build/app/$2/var/static
cp -r ./build/dist/js/productionExecutable/* ./build/app/$2/var/static/

mkdir ./build/app/$2/var/release
cp -r ./var/release/* ./build/app/$2/var/release

mkdir ./build/app/$2/var/log
mkdir ./build/app/$2/var/log/archive

cd ./build/app/$2
zip -r ../supervisor-$2.zip *