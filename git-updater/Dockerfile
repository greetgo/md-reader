FROM alpine:3.14.6

RUN apk add --no-cache git openssh

COPY md-reader /ssh/md-reader
RUN  chmod u=r /ssh/md-reader

COPY ssh_config /etc/ssh/ssh_config
