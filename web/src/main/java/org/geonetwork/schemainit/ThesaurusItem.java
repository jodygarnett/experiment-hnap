/*
 * Copyright (C) 2001-2016 Food and Agriculture Organization of the
 * United Nations (FAO-UN), United Nations World Food Programme (WFP)
 * and United Nations Environment Programme (UNEP)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *
 * Contact: David Blasby, GeoCat (The Netherlands)
 */
package org.geonetwork.schemainit;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThesaurusItem {

    public enum ActionType {
        createOnly, // if the file exists, do NOT overwrite
        overwrite,  // if the file exists, overwrite it
        delete      // always delete
    }

    public Path getThesaurusDir() {
        return thesaurusDir;
    }

    public ActionType getAction() {
        return action;
    }

    public Path getWebappDir() {
        return webappDir;
    }

    Path thesaurusDir;
    String source;
    ActionType action;
    String destination;
    Path webappDir;

    /*parses the JSON object into its values
    // {
    //      "initialization/settings/thesaurus/EC_Org_Names.rdf",  // location of resource - always use "/"
    //      "action":"overwrite",                                  // createOnly,overwrite,delete
    //      "destination": "external/theme/EC_Org_Names.rdf"       // relative to thesaurusDir, always use "/"
    // }
     */
    public ThesaurusItem(JSONObject obj, Path webappDir, Path thesaurusDir) throws Exception {
        this.thesaurusDir = thesaurusDir;
        this.webappDir = webappDir;
        this.source = (String) obj.get("source");
        this.action = ActionType.valueOf((String) obj.get("action"));
        this.destination = (String) obj.get("destination");
    }

    /**
     * Get the Path for the source
     * This will start in the webAppDir.
     * handles OS separator (/\) differences.
     * @return
     */
    public Path getSource() {
        return Paths.get(webappDir.normalize().toString(), source.split("/"));
    }

    /**
     * Get the Path for the destination
     * This will start in the thesaurus dir (cf. GeonetworkDataDir).
     * handles OS separator (/\) differences.
     * @return
     */
    public Path getDestination() {
        return Paths.get(thesaurusDir.normalize().toString(), destination.split("/"));
    }

    /**
     * if destination file exists, delete it
     * @throws IOException
     */
    public void deleteDestination() throws IOException {
        if (Files.exists(getDestination())) {
            Files.delete(getDestination());
        }
    }

    /**
     * copy from source to destination
     * If destination exists, do NOTHING
     * @throws IOException
     */
    public void copyToDestination() throws IOException {
        if (!Files.exists(getDestination())) {
            Files.copy(getSource(), getDestination());
        }
    }

    /**
     * do the thesaurus' action
     * @throws IOException
     */
    public void handle() throws IOException {
        switch (this.action) {
            case delete:
                deleteDestination();
                break;
            case overwrite:
                deleteDestination();
                //fall through to next one
            case createOnly:
                copyToDestination();
                break;
        }
    }

}
