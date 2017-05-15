*Base image originally imported from https://bitbucket.org/nshou/elasticsearch-kibana/overview*

## Elasticsearch and Kibana in one container

Simple and lightweight docker image for previewing Elasticsearch and Kibana.

### SetUp / Usage

Build the image and push it to your (local) registry server. Then you can use:

```
docker run -d -p 9200:9200 -p 5601:5601 <path to the image in the registry>
```

to pull and start the container.

After the successful start of you docker container, you can connect to Elasticsearch by `localhost:9200` and its Kibana front-end by `localhost:5601`.


Browse http://localhost:5601:
* Uncheck "Index contains time-based events"
* Replace "logstash-*" by "projects" and click on "Create"
* Click on "Saved Objects":
* Click on "Import" and select the file in kibana_export/views.json (within the project directory)
* Click on "Import" and select the file in kibana_export/dashboard.json (within the project directory)


### Troubleshooting

In case the container could not be started or stops after a few seconds, you should consider to check the `vm_map_max_count` setting.

```
grep vm.max_map_count /etc/sysctl.conf
```
The value has to be at least set to 262144 for production use. (see https://www.elastic.co/guide/en/elasticsearch/reference/5.x/docker.html)
