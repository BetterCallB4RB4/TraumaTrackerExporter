# Trauma Tracker Exporter 

Deployment management has done with Docker Compose. Make sure Docker and Docker-compose are installed.

Initialize the monitoring architecture only after the Trauma Tracker service is up and running.

To launch Docker compose execute the command:

```
docker-compose up -d
```

Make sure that the network where the containers are launched is the same as the TraumaTracker.

It is possible to modify the exporter configuration file by contacting the dockerhost in the mapped port used to expose the Trauma Tracker service.

Contact localhost via browser using prometheus default port (9090) and grafana (3000). 
By calling these addresses it will be possible to access the Prometheus graphic interface and data viewing services.
