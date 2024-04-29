FROM postgres:latest

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=gnammy

COPY mydatabase_backup.sql /docker-entrypoint-initdb.d/