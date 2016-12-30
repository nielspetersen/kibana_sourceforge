FROM sebp/elk:502

ADD ./start.sh /usr/local/bin/start.sh
RUN chmod +x /usr/local/bin/start.sh

EXPOSE 5601 9200 9300
VOLUME /var/lib/elasticsearch

ADD ./setup.json /tmp/setup.json
ADD ./data /data

CMD [ "/usr/local/bin/start.sh" ]
