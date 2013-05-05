git clone https://github.com/Gottox/socket.io-java-client.git
mvn -DartifactId=websocket -DgroupId=org.java_websocket -Dversion=0.1-SNAPSHOT -Dpackaging=jar -Dfile=..\\socket.io-java-client\\libs\\WebSocket.jar -DgeneratePom=false install:install-file
mvn -DartifactId=json-org -DgroupId=org.json -Dversion=0.1-SNAPSHOT -Dpackaging=jar -Dfile=..\\socket.io-java-client\\libs\\json-org.jar -DgeneratePom=false install:install-file
mvn -DartifactId=java-client -DgroupId=io.socket -Dversion=0.1-SNAPSHOT -Dpackaging=jar -Dfile=..\\socket.io-java-client\\dist\\socket.io-java-client.jar -DgeneratePom=false install:install-file
