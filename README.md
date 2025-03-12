#EXPERIENCIA_VERT.X
Se crea el proyecto a partir del mvn:
mvn archetype:generate -DarchetypeGroupId=io.vertx \
  -DarchetypeArtifactId=vertx-archetype-simple \
  -DgroupId=com.example \
  -DartifactId=my-vertx-app \
  -Dversion=1.0-SNAPSHOT

Ya esto lo puedes modificar en un IDE de desarrollo, con las dependencias correspondientes y con la configuración necesaria.

Luego ejecutamos Kafka en docker con los siguientes comandos: 
docker run -d --name zookeeper -p 2181:2181 \ -e ZOOKEEPER_CLIENT_PORT=2181 \ confluentinc/cp-zookeeper 

docker run -d --name kafka --link zookeeper -p 9092:9092 \ -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \ -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \ -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \ confluentinc/cp-kafka

Se verifica el broker de Kafka:
docker exec -it kafka kafka-broker-api-versions --bootstrap-server localhost:9092

Vemos cual ha sido el topic creado:
docker exec -it kafka kafka-topics --list --bootstrap-server localhost:9092
Para Luego obtener los mensajes enviados:

Podemos ver los mensajes enviados a un topic en general:
docker exec -it kafka kafka-console-consumer --topic profession --from-beginning --bootstrap-server localhost:9092
