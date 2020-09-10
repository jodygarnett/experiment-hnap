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

import org.fao.geonet.domain.SettingDataType;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ThesaurusInitializationTest {

    @Test
    public void testParser() throws Exception {
        String fname = "src/test/testfiles/thesaurus.json";

        ThesaurusInitialization parser = new ThesaurusInitialization();
        List<ThesaurusItem> thesaurusItemsList = parser.parseFile(Paths.get(fname),Paths.get("webdir"),Paths.get("thesaurusdir"));
        assertEquals(1,thesaurusItemsList.size());

        assertEquals("webdir/initialization/settings/thesaurus/GC_Org_Names.rdf",thesaurusItemsList.get(0).getSource().toString());
        assertEquals(ThesaurusItem.ActionType.overwrite,thesaurusItemsList.get(0).getAction());
        assertEquals("thesaurusdir/external/thesauri/theme/GC_Org_Names.rdf",thesaurusItemsList.get(0).getDestination().toString());
    }
}
