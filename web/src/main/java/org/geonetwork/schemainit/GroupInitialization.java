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

import org.fao.geonet.events.server.ServerStartup;
import org.fao.geonet.repository.GroupRepository;
import org.fao.geonet.repository.SettingRepository;
import org.fao.geonet.utils.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//reads in the groups.json file and updates in the Groups in the DB
public class GroupInitialization  implements
        ApplicationListener<ServerStartup> {


    @Autowired
    private GroupRepository _groupRepository;

    public GroupInitialization() {

    }


    //when the server starts up, we are ready to
    public void onApplicationEvent(ServerStartup event) {
        javax.servlet.ServletContext sc = ((org.springframework.web.context.support.XmlWebApplicationContext)event.getSource() ).getServletContext();
        String rootDir = sc.getRealPath("/");
        try {
            initGroups(rootDir);
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("startup",e);
        }
    }

    //parses the groups.json file -> list of <GroupName>
    public List<GroupName> parseFile(Path fname) throws  Exception {
        List<GroupName> result = new ArrayList<>();
        String fileString = new String(Files.readAllBytes(fname), StandardCharsets.UTF_8);

        JSONArray list = new JSONArray(fileString);
        for (int t=0;t<list.length();t++) {
            JSONObject obj = (JSONObject) list.get(t);
            GroupName group = new  GroupName(obj, _groupRepository);
            result.add(group);
        }
        return result;
    }

    //reads the file, then updates the DB
    private void initGroups(String dir) throws  Exception {
        Path path  = Paths.get(dir,"initialization","settings","groups.json");
        if (!Files.exists(path)) {
            return; //nothing to do
        }
        Log.info("startup", "Initializing groups from : " + path);

        List<GroupName> groups = parseFile(path);
        for (GroupName group:groups) {
            group.updateGroup();
        }
    }

}
