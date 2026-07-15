FROM amazoncorretto:11-alpine

LABEL maintainer="Fernando Mendioroz <fernando.mendioroz@gmail.com>"

# install dependencies (bash is required by WildFly's standalone.sh)
RUN apk add --no-cache bash net-tools lksctp-tools supervisor lksctp-tools-dev

# create and set workspace
RUN mkdir -p /opt/naikeri/jss7
WORKDIR /opt/naikeri/jss7

# produced by the Naikeri-jSS7-WildFly Jenkins job; version subject to change
COPY Naikeri-jSS7-WildFly-9.0.0-1557/. .

RUN chmod +x wildfly-24.0.1.Final/bin/standalone.sh

# run application
ENTRYPOINT ["/opt/naikeri/jss7/wildfly-24.0.1.Final/bin/standalone.sh"]
CMD ["-b", "0.0.0.0"]