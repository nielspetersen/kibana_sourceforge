FROM openjdk:jre-alpine

MAINTAINER nshou <nshou@coronocoya.net>

ENV ES_VERSION=2.4.1 \
    KIBANA_VERSION=4.6.2

RUN apk add --quiet --no-progress --no-cache nodejs wget \
 && adduser -D elasticsearch

USER elasticsearch

WORKDIR /home/elasticsearch

RUN wget -q -O - https://download.elastic.co/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/${ES_VERSION}/elasticsearch-${ES_VERSION}.tar.gz \
 |  tar -zx \
 && mv elasticsearch-${ES_VERSION} elasticsearch \
 && wget -q -O - https://download.elastic.co/kibana/kibana/kibana-${KIBANA_VERSION}-linux-x86_64.tar.gz \
 |  tar -zx \
 && mv kibana-${KIBANA_VERSION}-linux-x86_64 kibana \
 && rm -f kibana/node/bin/node kibana/node/bin/npm \
 && ln -s $(which node) kibana/node/bin/node \
 && ln -s $(which npm) kibana/node/bin/npm

CMD elasticsearch/bin/elasticsearch --es.logger.level=OFF --network.host=0.0.0.0 & kibana/bin/kibana -Q

EXPOSE 9200 5601
