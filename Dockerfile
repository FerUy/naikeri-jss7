FROM amazoncorretto:11-alpine

# maintainer
MAINTAINER Fernando Mendioroz - fernando.mendioroz@naikeri.com

# install dependencies
RUN apk add net-tools lksctp-tools supervisor lksctp-tools-dev

# create and set workspace
RUN mkdir -p /opt/naikeri/jss7
WORKDIR /opt/naikeri/jss7

# the version number will be changed during the CI/CD build
COPY Naikeri-jSS7-8.5.0-314-wildfly/. .

RUN chmod +x wildfly-24.0.1.Final/bin/standalone.sh

# run application
ENTRYPOINT ["/bin/"]
CMD ["standalone.sh","-b 0.0.0.0"]