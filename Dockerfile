# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package

FROM tomcat:11.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/shop-mvc.war /tmp/ROOT.war
RUN mkdir -p /usr/local/tomcat/webapps/ROOT \
    && cd /usr/local/tomcat/webapps/ROOT \
    && jar xf /tmp/ROOT.war \
    && rm /tmp/ROOT.war

RUN cat > /usr/local/bin/entrypoint.sh <<'SCRIPT'
#!/bin/sh
set -e

cat > /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/db.properties <<EOF
db.url=${DB_URL:-jdbc:postgresql://db:5432/shop_db}
db.user=${DB_USER:-postgres}
db.password=${DB_PASSWORD:-postgres}
db.pool.maxSize=${DB_POOL_MAX_SIZE:-10}
db.pool.minIdle=${DB_POOL_MIN_IDLE:-2}
db.pool.connectionTimeoutMs=${DB_POOL_CONNECTION_TIMEOUT_MS:-30000}
db.pool.idleTimeoutMs=${DB_POOL_IDLE_TIMEOUT_MS:-600000}
db.pool.maxLifetimeMs=${DB_POOL_MAX_LIFETIME_MS:-1800000}
EOF

exec "$@"
SCRIPT

RUN chmod +x /usr/local/bin/entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["catalina.sh", "run"]
