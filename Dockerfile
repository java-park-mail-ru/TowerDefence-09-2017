FROM ubuntu:rolling

RUN apt update  && apt install -y postgresql-9.6 postgresql-client-9.6

USER postgres

RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER td WITH SUPERUSER PASSWORD 'td';" &&\
    createdb -O td td &&\
    /etc/init.d/postgresql stop

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.6/main/pg_hba.conf
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.6/main/postgresql.conf

VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

EXPOSE 5432
CMD ["/usr/lib/postgresql/9.6/bin/postgres", "-D", "/var/lib/postgresql/9.6/main", "-c", "config_file=/etc/postgresql/9.6/main/postgresql.conf"]



#RUN apt install -y git wget

# RUN tar -C /usr/local -xzf go1.9.linux-amd64.tar.gz && \
#    mkdir go && mkdir go/src && mkdir go/bin && mkdir go/pkg && \
#    mkdir go/src/

#ENV GOPATH /root/go
#ENV GOROOT /usr/local.go

#ENV PATH=${PATH}/bin:/usr/local/go/bin

#RUN go get -u github.com/Dnnd/tech_db

#CMD ./go/bin/tech_db



