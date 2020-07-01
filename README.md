# GeoNetwork HNAP Web Application

An example web application using maven war overlay functionality to reuse
geonetwork war and hnap schema plugin.

This example shows two things:

1. How to use maven overlays to reuse geonetwork `war` as a base

2. Combine with local configuration

3. Use of an additional schema plugin:
   
   * `jar` managed as a dependency
   * `zip` unpacked into appropriate schema_plugin folder

References:

* https://maven.apache.org/plugins/maven-war-plugin/overlays.html

## Build

1. Build locally
   
   ```
   mvn install
   ```
   
2. Test the resulting war: `target/geonetwork-hnap-3.11.0-SNAPSHOT.war`

## Quicktest

1. Use of process-resources:
   
   ```
   mvn process-resources
   ```


## Jetty

1. Test war:
   
   ```
   mvn jetty:run-war`
   ```

2. Test classpath -- not working yet:
   
   ```
   mvn jetty:run
   ```
   
   We are having some trouble working with jetty locally due to:

   * Conflicts with transitive dependencies, example `pdfbox` from `mapfish`.

   * Inclusion of `jetty` artifacts in geonetwork `core`.
   
   * Some confusion between jars on classpath and in libs
