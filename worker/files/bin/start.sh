#!/usr/bin/env bash

rsyslogd
cron

env > /tmp/env.list

tail -F /var/log/worker.log
