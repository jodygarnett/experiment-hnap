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

import org.fao.geonet.repository.GroupRepository;
import org.json.JSONException;
import org.json.JSONObject;

//handles the details of initializing Groups
// {
//    "name":"CorporateGroup",
//    "description":"Group for everyone in the Corporation",
//    "email":"",
//    "logo":"",
//    "website": ""
//  }
public class GroupName {

    String _groupName;
    String _description;
    String _email;
    String _logo;
    String _website;
    GroupRepository _groupRepository;

    //parses the JSONObject
    public GroupName (JSONObject obj, GroupRepository  groupRepository) throws JSONException {
        this._groupName = (String) obj.get("name");;
        this._description = (String) obj.get("description");
        this._email = (String) obj.get("email");;
        this._logo = (String) obj.get("logo");;
        this._website = (String) obj.get("website");;

        this._groupRepository = groupRepository;
    }

    //converts the parsed JSON values to a GN domain Group
    public org.fao.geonet.domain.Group asGroup() {
        org.fao.geonet.domain.Group group = new org.fao.geonet.domain.Group();
        group.setName(this._groupName);
        if (!strEmpty(_description))
            group.setDescription(_description);
        if (!strEmpty(_email))
            group.setEmail(_email);
        if (!strEmpty(_logo))
            group.setLogo(_logo);
        if (!strEmpty(_website))
            group.setWebsite(_website);
        return group;
    }

    // adds a group to the DB
    public void addGroup() {
        if (!groupExist()) {
            _groupRepository.save(asGroup());
        }
    }

    // updates a group
    //   if not exists in DB --> add
    //   if exists in DB --> update its values and save
    public void updateGroup() {
        if (!groupExist()) {
            addGroup();
            return;
        }

        org.fao.geonet.domain.Group group = _groupRepository.findByName(_groupName);

        group.setName(this._groupName);
        if (!strEmpty(_description))
            group.setDescription(_description);
        if (!strEmpty(_email))
            group.setEmail(_email);
        if (!strEmpty(_logo))
            group.setLogo(_logo);
        if (!strEmpty(_website))
            group.setWebsite(_website);

        _groupRepository.save(group);
    }

    //see if the group exists (based on group name)
    public boolean groupExist() {
        if (_groupRepository == null)
            return false;
        return  !(_groupRepository.findByName(_groupName) == null);
    }


    //sees if a string is null, empty, or  just whitespace
    public boolean strEmpty(String str) {
        return (str == null) || (str.trim().isEmpty());
    }
}
