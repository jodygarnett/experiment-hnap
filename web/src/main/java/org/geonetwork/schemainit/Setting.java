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

import org.json.JSONException;
import org.json.JSONObject;
import org.fao.geonet.domain.SettingDataType;
import org.fao.geonet.repository.SettingRepository;

// handles the details of a Setting
// {
//    "type":"STRING",   // STRING, INT, or BOOLEAN
//    "key":"/system/abc",
//    "position":123,
//    "value":"..."
// }
public class Setting {

    String key;
    SettingDataType type;
    String value;
    int position;
    SettingRepository _settingRepository;

    //parses the JSON rep to a set of values
     public Setting(JSONObject obj,SettingRepository _settingRepository) throws JSONException {
         this._settingRepository=_settingRepository;
         this.key = (String) obj.get("key");
         this.type = SettingDataType.valueOf( (String) obj.get("type") );
         this.value =  (String) obj.get("value");
         this.position =   (Integer) obj.get("position");
     }

     // convert the values to a GN domain Setting
     public org.fao.geonet.domain.Setting asSetting() {
         org.fao.geonet.domain.Setting setting = new org.fao.geonet.domain.Setting();
         setting.setDataType(type);
         setting.setName(key);
         setting.setPosition(position);
         setting.setValue(value);
         return setting;
     }

     //adds a setting to the DB
     public void addSetting() {
        if (!settingExist()) {
            _settingRepository.save(asSetting());
        }
    }
    // updates a setting
    //   if not exists in DB --> add
    //   if exists in DB --> update its values and save
    public void updateSetting() {
        if (!settingExist()) {
            addSetting();
            return;
        }

        org.fao.geonet.domain.Setting setting = _settingRepository.findOne(key);
        setting.setValue(value);
        _settingRepository.save(setting);

    }
    //see if the setting exists (based on setting name)
    public boolean settingExist() {
         if (_settingRepository == null)
             return false;
        return  !(_settingRepository.findOne(key) == null);
    }

}
