Web Application
---------------

Web application defined using war-overlay approach:

* war: `org.geonetwork-opensource:web-app`
* overlay: `hnap-experiment:web`

The final war is formed by in-effect unpacking the origional war, and overlaying the files defined by this `web` module on top.
This can easily be used to *add* or *replace* the files provided by core-geonetwork. Removing files is slightly more tricky but
can be accomplished using by using an exclude pattern in the `pom.ml`.

The files for the overlay included in this example:

* `src/java` - compiled into jar
* `src/resources` - files included into jar
* `src/webapp` - directly used
* `test` - testing and integration tests can be performed like normal

Additinal external content is also gathered:

* `org.geonetwork-opensource:schema-iso19139.ca.HNAP:3.7` jar included as a dependency
* maven `maven-resources-plugin` used to copy contents of `iso19139.ca.HNAP/src/main/plugin` folder into place

As the base core-geonetwork `war` already includes some components:

* Care is taken to exlcude dependencies from `org.geonetwork-opensource:schema-iso19139.ca.HNAP:3.7` above
* Any dependencies needed to compile are marked as scope `provided` to avoid including twice

This can be improved when core-geonetwork schema-plugins uses stable version numbering.

Why do this?
------------

This approach is *great* as it provides an alternative to forking core-geonetwork:

* All the same customizations you would perform when forking can still be accomplished
* Upgrading to a newer version of geonetwork involves changing the submodule and `project.version` to the new release tag
