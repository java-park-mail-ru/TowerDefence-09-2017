#!/bin/sh

PORT=228
DIST_PATH=./target
DEPLOY_PATH=/home/td/td
SERVER_IP=$1
echo "Deploying to $SERVER_IP"

echo "Setting up ssh..."
eval "$(ssh-agent -s)"
ssh-keyscan -p $PORT -H $SERVER_IP >> ~/.ssh/known_hosts
chmod 600 $HOME/.ssh/server
ssh-add $HOME/.ssh/server

echo "Cleaning up..."
ssh -p $PORT td@$SERVER_IP "rm -rf $DEPLOY_PATH/*"

echo "Uploading..."
scp -P $PORT -r $DIST_PATH/* td@$SERVER_IP:$DEPLOY_PATH
