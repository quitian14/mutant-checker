FROM amazoncorretto:17

RUN mkdir /code
COPY target /code

ENTRYPOINT [ "sh", "-c", "java -jar /code/*.jar" ]