# HNAP GeoNetwork Web Application

This defines web application using war overlay approach:

* core-geonetwork war base: included as a submodule
* hnap schema plugin: included as a submodule
* web: war overlay application
* release: release assembly including installation instructions

## Repository Overview

* `web/` - web application
* `geonetwork/` - geonetwork submodule
* `iso19139.ca.HNAP` - hnap schema submodule

Use of submodules:

```
git submodule update --init --recursive
```

## Build

1. Build locally
   
   ```
   mvn install -DskipTests
   ```
   
2. Build the war:

   ```
   cd web
   mvn war:war
   ```
   
3. Test locally:

   ```
   cd web
   mvn jetty:run-war
   ```

## Release

A release bundle is made to copy to customer's machine:

1. Build release bundle:

   ```
   cd release
   mvn assembly:single
   ```

2. The `zip` bundle is available in the `target` folder.