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

import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.GeonetworkDataDirectory;
import org.fao.geonet.utils.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ThesaurusInitialization implements
        ApplicationListener<GeonetworkDataDirectory.GeonetworkDataDirectoryInitializedEvent>,
        Ordered {


    //parses the thesaurus.json file -> list of <ThesaurusItem>
    public List<ThesaurusItem> parseFile(Path fname, Path webappDir, Path thesauriDir) throws Exception {
        List<ThesaurusItem> result = new ArrayList<>();
        String fileString = new String(Files.readAllBytes(fname), StandardCharsets.UTF_8);

        JSONArray list = new JSONArray(fileString);
        for (int t = 0; t < list.length(); t++) {
            JSONObject obj = (JSONObject) list.get(t);
            ThesaurusItem titem = new ThesaurusItem(obj, webappDir, thesauriDir);
            result.add(titem);
        }
        return result;
    }

    // for each of the RDF files included in this JAR, if its not in the corresponding location in datadir, then we copy it in
    public void ConfigureThesauruses(String webAppDir, Path thesauriDir) throws Exception {
        Path path = Paths.get(webAppDir, "initialization", "settings", "thesaurus", "thesaurus.json");
        if (!Files.exists(path)) {
            return; //nothing to do
        }
        Log.info("startup", "Initializing thesaurus from : " + path);
        List<ThesaurusItem> items = parseFile(path, Paths.get(webAppDir), thesauriDir);
        for (ThesaurusItem item : items) {
            item.handle();
        }
    }


    //when the data dir is configured, we're good to copy in our thesauruses
    @Override
    public void onApplicationEvent(GeonetworkDataDirectory.GeonetworkDataDirectoryInitializedEvent event) {
        Path thesauriDir = event.getSource().getThesauriDir();
        Path webAppDir = event.getSource().getWebappDir();
        try {
            ConfigureThesauruses(webAppDir.normalize().toString(), thesauriDir);
        } catch (Exception e) {
            Log.error(Geonet.THESAURUS, "ThesaurusInitialization: error preloading RDF files", e);
        }
    }

    //do this last!  We want this to happen AFTER the HNAP init.
    @Override
    public int getOrder() {
        return 10;
    }
}
