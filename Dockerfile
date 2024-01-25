FROM openjdk:17-oracle
ARG ARTIFACT_NAME
ARG IMAGE_VERSION
EXPOSE 8080
ADD target/${ARTIFACT_NAME}*.jar ${ARTIFACT_NAME}.jar
ADD java.security usr/java/openjdk-17/conf/security/
RUN printf "IMAGE_VERSION=${IMAGE_VERSION}" > version.properties
COPY entrypoint.sh ./entrypoint.sh
RUN chmod +x ./entrypoint.sh
ENTRYPOINT ["/bin/bash", "./entrypoint.sh"]
