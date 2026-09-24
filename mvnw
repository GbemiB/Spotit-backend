#!/bin/bash
# This project targets Java 21 (see pom.xml <release>21</release>). Homebrew's
# default `mvn` falls back to the generic `openjdk` formula (currently v23) when
# JAVA_HOME isn't set, and Lombok's annotation processing silently breaks under
# that mismatch (getters/setters/builders/@Slf4j all "disappear"). Pin JAVA_HOME
# to the matching JDK 21 install here so `./mvnw <goal>` always builds correctly.
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home}"
exec mvn "$@"
