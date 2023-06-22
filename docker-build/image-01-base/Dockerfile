FROM ubuntu:20.04

COPY sources.list /etc/apt/sources.list

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
 && apt-get install -y locales openjdk-17-jre pandoc wkhtmltopdf \
 && apt-get clean

RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8

ENV LANG=C.UTF-8
