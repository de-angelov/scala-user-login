#
FROM sbtscala/scala-sbt:graalvm-community-22.0.0_1.10.0_3.4.2 as base

WORKDIR /app

FROM base as builder
WORKDIR /app

COPY build.sbt /app/
COPY src /app/src

# Run the application
CMD ["sbt", "run"]



