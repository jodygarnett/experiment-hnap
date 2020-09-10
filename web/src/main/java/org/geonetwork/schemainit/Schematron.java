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

import org.fao.geonet.domain.SchematronCriteria;
import org.fao.geonet.domain.SchematronCriteriaGroup;
import org.fao.geonet.domain.SchematronCriteriaType;
import org.fao.geonet.repository.SchematronCriteriaGroupRepository;
import org.fao.geonet.repository.SchematronCriteriaRepository;
import org.fao.geonet.repository.SchematronRepository;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


// {
//    "schemaName":"iso19139.ca.HNAP",
//    "schemaFileName":"schematron-rules-multilingual.xsl",
//    "groupName":"*Generated*",

//    "action":"DELETE",

//    "type": "",
//    "value": "",
//    "uitype": "",
//    "uivalue": ""
// }
//
//  This is 1:1 with the schematron.json files
//   It also knows how to do the DELETE and ADD action (see below).
public class Schematron {

    enum Action {DELETE, ADD}

    String _schemaName;
    String _schemaFileName;
    String _groupName;

    String _type;
    String _value;
    String _uitype;
    String _uivalue;

    Action _action;


    private SchematronRepository _schematronRepository;

    private SchematronCriteriaGroupRepository _schematronCriteriaGroupRepository;


    private SchematronCriteriaRepository _schematronCriteriaRepository;


    //parses the JSONObject
    public Schematron(JSONObject obj, SchematronRepository _schematronRepository, SchematronCriteriaGroupRepository _schematronCriteriaGroupRepository, SchematronCriteriaRepository _schematronCriteriaRepository) throws JSONException {
        this._schemaName = (String) obj.get("schemaName");
        this._schemaFileName = (String) obj.get("schemaFileName");
        this._groupName = (String) obj.get("groupName");
        this._type = (String) obj.get("type");
        this._value = (String) obj.get("value");
        this._uitype = (String) obj.get("uitype");
        this._uivalue = (String) obj.get("uivalue");

        this._action = Action.valueOf((String) obj.get("action"));

        this._schematronCriteriaGroupRepository = _schematronCriteriaGroupRepository;
        this._schematronCriteriaRepository = _schematronCriteriaRepository;
        this._schematronRepository = _schematronRepository;
    }

    //1. finds the schematron Object ID via the schema and schematron file name
    //2. Finds the schematron groups via the Schematron ID (#1)
    //3. Finds the schematron group via its name
    private SchematronCriteriaGroup findCriteriaGroup() {
        org.fao.geonet.domain.Schematron stron = _schematronRepository.findOneByFileAndSchemaName(_schemaFileName, _schemaName);
        List<SchematronCriteriaGroup> groups = _schematronCriteriaGroupRepository.findAllById_SchematronId(stron.getId());
        Optional<SchematronCriteriaGroup> scg = groups.stream().filter(g -> g.getId().getName().equals(_groupName)).findFirst();
        if (scg.isPresent())
            return scg.get();
        return null;
    }

    //Finds the Criteria (if present) that corresponds to this object
    private SchematronCriteria findCriteria() {
        SchematronCriteriaGroup scg = findCriteriaGroup();
        if (scg != null) {
            // just look at _type and _value
            Optional<SchematronCriteria> crt = scg.getCriteria().stream().filter(c -> c.getType() == SchematronCriteriaType.valueOf(_type) && c.getValue().equals(_value)).findFirst();
            if (crt.isPresent()) {
                return crt.get();
            }
        }
        return null;
    }


    //deletes an existing schematron criteria that matches the values in this object
    private void deleteItem() {
        SchematronCriteria crt = findCriteria();
        if (crt != null) {
            _schematronCriteriaRepository.delete(crt.getId());
        }
    }

    //if there isn't a schematron criteria that maches the values in this object, then add it
    private void addItem() {
        SchematronCriteria crt = findCriteria();
        if (crt != null)
            return;  // already exists - nothing to do

        SchematronCriteriaGroup group = findCriteriaGroup();

        SchematronCriteria criteria = new SchematronCriteria();
        criteria.setType(SchematronCriteriaType.valueOf(_type));
        criteria.setValue(_value);
        criteria.setUiType(_uitype);
        criteria.setUiValue(_uivalue);

        group.addCriteria(criteria);

        _schematronCriteriaGroupRepository.saveAndFlush(group);
    }

    //process the _action for this object
    public void handle() throws IOException {
        switch (this._action) {
            case DELETE:
                deleteItem();
                break;
            case ADD:
                addItem();
                break;
        }

    }

}