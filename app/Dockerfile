FROM gradle:8.5.0-jdk17

WORKDIR /app

COPY . .

RUN gradle installDist

CMD ["/app/build/install/app/bin/app"]