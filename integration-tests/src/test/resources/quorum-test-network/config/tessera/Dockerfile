ARG TESSERA_VERSION=latest

FROM --platform=linux/amd64 quorumengineering/tessera:${TESSERA_VERSION}

# develop uses a debian container, all releases use an alpine container - this allows both to be used for the quickstart
# set the version in ../../.env
RUN if [ -e /sbin/apk ] ; then apk add gettext --no-cache ; else apt-get update && apt-get install -y gettext && rm -rf /var/lib/apt/lists/* ; fi

ENV JAVA_OPTS="-Dlogback.configurationFile=/data/logback.xml"

COPY docker-entrypoint.sh /usr/bin/
COPY data data

ENTRYPOINT ["docker-entrypoint.sh"]

