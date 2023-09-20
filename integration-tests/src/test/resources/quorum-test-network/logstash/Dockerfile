FROM docker.elastic.co/logstash/logstash:7.14.1

ENV ES_HOST http://localhost:9200
ENV REDIS_HOST localhost
ENV REDIS_PORT 6379

# Remove exisiting file
RUN mkdir -p /usr/share/logstash/logs && \
    rm -f /usr/share/logstash/pipeline/logstash.conf

# Add pipeline files
ADD pipeline/ /usr/share/logstash/pipeline/

# Add configuration files
ADD config/ /usr/share/logstash/config/

# Test the configuration
RUN /usr/share/logstash/bin/logstash -t
