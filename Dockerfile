FROM bellsoft/liberica-openjdk-centos:11.0.6

ADD build/libs/UsersProvider.jar /app/UsersProvider.jar

ENTRYPOINT java $JAVA_OPTS -jar /app/UsersProvider.jar

EXPOSE 9000
