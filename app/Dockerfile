FROM gradle:8.5.0-jdk17

WORKDIR /app

COPY /app .

RUN gradle installDist
RUN ls -R /app/build/install
RUN chmod +x /app/build/install/app/bin/app

CMD ["gradle", "run"]