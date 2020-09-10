# HNAP GeoNetwok 3.10.x

This release includes:

* geonetwork.war - a release of geonetwork with custom municiple skin and configuration
* geonetwork.xml - jndi configuration for tomcat
* INSTALLATION.txt - these installation notes
* LICENSE.txt
* 

## Release Notes

* Development: GeoNetwork 3.10 "snapshot"
  
  * core-geonetwork:
  
    Rewrote GeoNetwork LDAP support, required to match municiple multi-directory approach
    
  * iso19139.ca.HNAP:
  * webapp: customizations for visual appearance and reports
  
* Production: GeoNetwork 3.10.4

  * core-geonetwork: to be scheduled with community
  * iso19139.ca.HNAP: to be released with core-genetwork above
  * webapp: customizations for visual appearance and reports

## OpenJDK 8

Installation notes:

1. Installing OpenJDK 8 from AdoptOpenJDK: `OpenJDK8U-jdk_x64_windows_hotspot_8u252b09.msi`

2. Set JAVA_HOME variable option

## Apache Tomcat 9

Installation notes:

1. Download:
   
   * https://tomcat.apache.org/download-90.cgi 
   * "32-bit/64-bit Windows Service Installer"
   
2. Installation options
   
   * Installing as a service
   * Native - yes
   * Port: 80
   
   Development environment only:
   
   * Tomcat manager: using manager/geonetwork-opensource credentials

   Test:
   
   * http://127.127.0.1
   
3. Apache Tomcat 9.0 Tomcat Properties
   
   * Adjust memory to 4096m
   
   Development environment only:
   
   * Local System account: allow service to interact with desktop
 
4. Navigate to the Apache Tomcat folder (there is a short cut in the start menu.)

5. Oracle JDBC driver:

   * https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/12.2.0.1/
   * `ojdbc8.jar`
   
   Copy `ojdbc8.jar` into the tomcat `libs` folder.

6. Create `conf/Catalina/localhost/geonetwork.xml` using contents of [geonetwork.xml](geonetwork.xml)
   
   This file defines:
   
   * `jdbc/geonetwork`: database connection pool connecting to `GISEDITD`
   * `gnDatabaseDialect`: `ORACLE`
   * `ldap.password`: password used to acces Municiple LDAP directory.
   
   Details provided below.

## LDAP

1. The following groups have been configured for GeoNetwork use:
   
   * HGIS_GeoNetwork_Admin
   * HGIS_GeoNetwork_Editor

2. Please provide the correct `ldap.password` in the provided `geonetwork.xml` file.

    ```
    <Environment name="ldap.password" value="(password)" type="java.lang.String" override="false"/>
    ```
    
3. This can also be specified as a java system property using Apache Tomcat Configuration:
   
   ```
   -Dldap.password=(password)
   ```

## Database

1. The application checks for a `jdbc/geonetwork` DataSource on first start.
   
   If not found a local `h2` database will be created for testing.
   
2. Please update `geonetwork.xml` file during installation with the correct username and password.
   
   ```
   <Environment name="gnDatabaseDialect" value="ORACLE" type="java.lang.String" override="false"/>
   <Resource name="jdbc/geonetwork" auth="Container"
             factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
             type="javax.sql.DataSource" driverClassName="oracle.jdbc.OracleDriver"
             url="jdbc:oracle:thin:@DELTADEV:1521:GISEDITD"
             username="(username))" password="(password)"
             maxActive="20" maxIdle="10"
             validationQuery="select 1 from dual"
             defaultAutoCommit="false" maxWait="-1"/>
   ```

## Data Directory

1. The application uses a data directory to manage search index, attachments and other files.

2. Create the folder `%ProgramData%\\GeoNetwork\Data`

3. Update the `geonetwork.xml` file with the correct `geonetwork.dir` location.

   ```
   <Environment name="geonetwork.dir" value="C:\\ProgramData\\GeoNetwork\\Data" type="java.lang.String" override="false"/>
   ```

## GeoNetwork 3.10.x
   
1. Copy the `geonetwork.war` into the `webapps` folder.

2. Start the service.
   
   * The `war` will be unpacked into a `geoserver` directory when first started.

3. Logs are available in the Tomcat logs folder.

   Adjust logging level using `geonetwork/WEB-INF/classes/log4j.xml`
