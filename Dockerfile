# syntax = docker/dockerfile:1.0-experimental
# Build with multistage build on jdk-11


## Build production image with only outputs
FROM integration-connector-base:0.5.9_local

LABEL Description="Sample Ongoing User Sync Connector"

COPY ./scheduled/build/distributions/connector*.tar /opt/bkpr/
COPY ./ongoing/build/distributions/connector*.tar /opt/bkpr/

RUN tar -xvf /opt/bkpr/connector-ongoing-0.0.1.tar --directory /opt/bkpr  && \
          rm /opt/bkpr/connector-ongoing-0.0.1.tar

RUN mv -f /opt/bkpr/connector-ongoing-0.0.1/lib/*.jar /opt/bkpr/service/lib/connector/

RUN tar -xvf /opt/bkpr/connector-scheduled-0.0.1.tar --directory /opt/bkpr && \
          rm /opt/bkpr/connector-scheduled-0.0.1.tar

RUN mv -f /opt/bkpr/connector-scheduled-0.0.1/lib/*.jar /opt/bkpr/service/lib/connector/
