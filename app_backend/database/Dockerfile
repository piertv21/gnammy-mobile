FROM postgres:latest

COPY dbexport.sql /docker-entrypoint-initdb.d/

CMD chmod -R 0755 /docker-entrypoint-initdb.d