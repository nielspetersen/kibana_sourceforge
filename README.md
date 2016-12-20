*Originally imported from https://bitbucket.org/nshou/elasticsearch-kibana/overview*

## Elasticsearch and Kibana in one container

Simple and lightweight docker image for previewing Elasticsearch and Kibana.

### Usage

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

### Tags

Tag/Branch     | Elasticsearch | Kibana
------- | ------------- | ------
master  |  2.4.1        | 4.6.2
