# GeoNetwork HNAP Web Application

An example web application using maven war overlay functionality to reuse
geonetwork war and hnap schema plugin.

This example shows two things:

1. How to use maven overlays to reuse geonetwork `war` and `HNAP` as a base

2. Combine with local configuration

3. Use of an additional schema plugin:
   
   * `jar` managed as a dependency
   * `zip` unpacked into appropriate schema_plugin folder

References:

* https://maven.apache.org/plugins/maven-war-plugin/overlays.html

## What This is Doing

This does a three-way layering of the WAR.
Please see the root `pom.xml`.

### Layer One

The first layer is contained in this repository and does the following;

a) overrides the default view (as well as providing a new icon)
  * See `src/main/webapp/catalog/views/default`
  
b) sets up UI configuration options to make the UI nicer  
  * See java code: `src/main/java/org/geonetwork/schemainit/UISettingsInitialization`
  * See spring config: `src/main/java/resources/config-spring-geonetwork.xml`
  * See settings json: `src/main/webapp/initialization/settings/uisettings.json`

c) sets up HNAP configuration settings  
  * See java code: `src/main/java/org/geonetwork/schemainit/SettingsInitialization`
  * See spring config: `src/main/java/resources/config-spring-geonetwork.xml`
  * See settings json: `src/main/webapp/initialization/settings/settings.json`


### Layer Two

Layer two is the standard HNAP build from https://github.com/metadata101/iso19139.ca.HNAP, and pulled from the OS Geo repository.

### Layer Three

Layer three is the standard GeoNetwork Open Source WAR from https://github.com/geonetwork/core-geonetwork, and pulled from the OS Geo repository.

## Build

1. Build locally
   
   ```
   mvn install
   ```
   
2. Test the resulting war: `target/geonetwork-hnap-3.11.0-SNAPSHOT.war`.

## Run this repo's tests
  
   ```
   mvn test
   ```

## Jetty

1. Test war:
   
   ```
   mvn jetty:run-war
   ```
 
Then go to http://localhost:8080/
 
