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

import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SchematronInitializationTest {


    @Test
    public void testParser() throws Exception {
        String fname = "src/test/testfiles/schematron.json";

        SchematronInitialization parser = new SchematronInitialization();
        List<Schematron> schematrons = parser.parseFile(Paths.get(fname));
        assertEquals(1,schematrons.size());

        Schematron s = schematrons.get(0);

        assertEquals("iso19139.ca.HNAP", s._schemaName);
        assertEquals("schematron-rules-multilingual.xsl", s._schemaFileName);
        assertEquals("*Generated*", s._groupName);
        assertEquals("ALWAYS_ACCEPT", s._type);
        assertEquals("_ignored_", s._value);
        assertEquals("abc", s._uitype);
        assertEquals("def", s._uivalue);

        assertEquals(Schematron.Action.DELETE, s._action);

    }

}
