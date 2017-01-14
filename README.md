*Originally imported from https://bitbucket.org/nshou/elasticsearch-kibana/overview*

## Elasticsearch and Kibana in one container

Simple and lightweight docker image for previewing Elasticsearch and Kibana.

### Installation / SetUp

First of all you have to login to the Docker registry of Gitlab to get access to the image.
Execute the following command and enter your Gitlab credentials:

```
docker login registry.gitlab.com
```

Once you are successfully logged in run:

```
docker run -d -p 9200:9200 -p 5601:5601 registry.gitlab.com/jeremiealbert/2016_efrei_bigdataforcompanies_06
```

to download the image, to build the container (from the image) and start the server.

Then you can connect to Elasticsearch by `localhost:9200` and its Kibana front-end by `localhost:5601`.

### Usage

5. Browse http://localhost:5601
* Uncheck "Index contains time-based events"
* Replace "logstash-*" by "projects" and click on "Create"
* Click on "Saved Objects":
* Click on "Import" and select the file in kibana_export/views.json
* Click on "Import" and select the file in kibana_export/dashboard.json


### Troubleshooting

In case the container could not be started or stops after a few seconds, you should consider to check the `vm_map_max_count` setting.

```
grep vm.max_map_count /etc/sysctl.conf
```
The value has to be at least set to 262144 for production use. (see https://www.elastic.co/guide/en/elasticsearch/reference/5.x/docker.html)
