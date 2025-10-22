FROM postgres:16

COPY src/main/resources/persistence/init.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_USER=pizzauser
ENV POSTGRES_PASSWORD=pizzapass
ENV POSTGRES_DB=pizza
