# Spark API example

Here you will find an example demonstrating spark api. This example shows how to read timeseries data, transfome it 
into a chunk and inject it into Solr.

## Prerequisites:
* [Java 1.8](https://www.java.com/fr/download/) 
* [data historian](https://github.com/Hurence/historian/releases/download/v1.3.5/install.sh) 
* [Apache Solr](https://archive.apache.org/dist/lucene/solr/8.2.0/solr-8.2.0.tgz)
## Setup environnement

Start by creating a directory hdh_workspace for example that we can call it $HDH_HOME.
```
# create the workspace anywhere you want
mkdir ~/hdh_workspace
export HDH_HOME=~/hdh_workspace
```
## Historian setup


Data Historian is a project containing scripts and files to manipulate time-series and chunk data. After downloading it
unzip the file and get in the directory to start the setup using this commands :

```
sudo ./bin/install.sh
```
## Setup Apache Solr
Apache solr is a database used by solr that could be replaced by other databases.
After downloading the 8.2.0 verion and unzipping it, get into solr
directory and run these command to start two solr servers:
```
./bin/solr start -cloud -s ./data/solr/node1 -p 8983
./bin/solr start -cloud -s ./data/solr/node2 -p 7574 -z localhost:9983
```

## Maven build 
Clone this project from github, open it with your IDE and built the project using this command :
```
mvn clean install
```


