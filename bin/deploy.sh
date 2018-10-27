#!/bin/bash

# packages project into tarball
# and copies to server with SCP

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PROJECT_DIR="$(dirname "${DIR}")"
PKG_NAME="spark-consumer-pkg"
PKG_DIR="${PROJECT_DIR}/${PKG_NAME}"
ZIP_NAME="${PKG_NAME}.tar.gz"

cd ${PROJECT_DIR}
mkdir "${PKG_NAME}"
mkdir "${PKG_NAME}/bin"
mkdir "${PKG_NAME}/lib"
mkdir "${PKG_NAME}/lib/target/scala-2.11"

echo "packaging from $PROJECT_DIR"

sbt package

echo "copying necessary files"
cp bin/run.sh "${PKG_DIR}/bin"
cp bin/install.sh "${PKG_DIR}/bin"
cp target/scala-2.11/spark-kafka-consumer_2.11-1.0.jar "${PKG_DIR}/lib/target/scala-2.11"
cp config.json "${PKG_DIR}"

echo "creating gzipped tarball"
tar -czvf "${PKG_NAME}.tar.gz" "${PKG_NAME}"

echo "removing project dir"
rm -rf "${PKG_NAME}"

echo "copying tarball to remote server"
scp  bdc:/tmp

echo "run.sh and jars stored on bdc:/tmp"
