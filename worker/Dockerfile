FROM java:8

RUN apt-get update
RUN apt-get install -y rsyslog cron

ADD build/distributions/worker-1.0.0.tar /usr/tgp

RUN mv /usr/tgp/worker-1.0.0/* /usr/tgp
RUN rmdir /usr/tgp/worker-1.0.0

RUN chmod +x /usr/tgp/bin/worker
RUN touch /var/log/worker.log

# Add in CRON stuff
ADD files/etc/crontab /etc/crontab
ADD files/bin/start.sh /usr/tgp/bin/start.sh
ADD files/bin/cron.sh /usr/tgp/bin/cron.sh

RUN chmod +x /usr/tgp/bin/start.sh
RUN chmod +x /usr/tgp/bin/cron.sh

# CMD to start the cron
CMD /usr/tgp/bin/start.sh
