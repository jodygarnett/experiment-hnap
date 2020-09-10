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

import org.fao.geonet.domain.UiSetting;
import org.fao.geonet.repository.UiSettingsRepository;
import org.json.JSONException;
import org.json.JSONObject;



// {
//    "id":"canada",
//    "configuration":"{...}"
 // }

public class UISetting {
    UiSettingsRepository  _UiSettingsRepository;
    String id;
    String configuration;

    //parses the JSON object into its values
    public UISetting(JSONObject obj, UiSettingsRepository  _UiSettingsRepository) throws JSONException {
        this._UiSettingsRepository=_UiSettingsRepository;
        this.id = (String) obj.get("id");
        this.configuration = (String) obj.get("configuration");;
    }

    //converts the values to a GN domain UiSetting
    public org.fao.geonet.domain.UiSetting asUiSetting() {
        org.fao.geonet.domain.UiSetting uiSetting = new org.fao.geonet.domain.UiSetting();
        uiSetting.setConfiguration(configuration);
        uiSetting.setId(id);
        return uiSetting;
    }

    //adds a UiSetting to the DB
    public void addSetting() {
        if (!settingExist()) {
            _UiSettingsRepository.save(asUiSetting());
        }
    }

    // updates a uiSetting
    //   if not exists in DB --> add
    //   if exists in DB --> update its value and save
    public void updateSetting() {
        if (!settingExist()) {
            addSetting();
            return;
        }

        UiSetting UiSetting = _UiSettingsRepository.findOne(id);
        UiSetting.setConfiguration(configuration);
        _UiSettingsRepository.save(UiSetting);

    }

    //see if the uiSetting exists (based on settings ui name)
    public boolean settingExist() {
        if (_UiSettingsRepository == null)
            return false;
        return  !(_UiSettingsRepository.findOne(id) == null);
    }


}
