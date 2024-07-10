FROM nginx:1-otel
COPY . /usr/share/nginx/html

ARG now
ARG version
ARG revision
ARG organization_name
ARG description
# Erase Nginx maintainer.
LABEL maintainer=""
LABEL org.opencontainers.image.base.name="nginx:1-otel"
LABEL org.opencontainers.image.created="$now"
LABEL org.opencontainers.image.authors="https://github.com/performantdata/"
LABEL org.opencontainers.image.url="https://github.com/performantdata/democracy-vouchers/static/"
LABEL org.opencontainers.image.documentation="https://github.com/performantdata/democracy-vouchers/static/README.md"
LABEL org.opencontainers.image.source="https://github.com/performantdata/democracy-vouchers/static/"
LABEL org.opencontainers.image.version="$version"
LABEL org.opencontainers.image.revision="$revision"
LABEL org.opencontainers.image.vendor="$organization_name"
LABEL org.opencontainers.image.title="Democracy vouchers test system static content"
LABEL org.opencontainers.image.description="$description"
