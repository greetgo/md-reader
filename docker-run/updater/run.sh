#!/bin/sh

if [ -d /repo/mybpm-doc ] ; then
  cd /repo/mybpm-doc || exit 111
  git pull

  sleep 5
else
  cd /repo || exit 110

  git clone git@gitlab.com:ekolpakov/mybpm-doc.git mybpm-doc || exit 200
fi
