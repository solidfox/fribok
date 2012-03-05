/*
 * Copyright © 2009 Stefan Kangas <stefankangas@gmail.com>
 *
 * This file is part of Fribok.
 *
 * Fribok is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Fribok distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Fribok.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.swedsoft.bookkeeping.calc.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.*;
import se.swedsoft.bookkeeping.calc.util.SSAutoIncrement;
import static org.junit.Assert.*;


/**
 * Tests for SSAutoIncrement
 *
 * @author Stefan Kangas
 * @version $Rev$, $Date$
 */
public class SSAutoIncrementTest {

    private SSAutoIncrement ainc;
    String key;
    int value;

    @org.junit.Before
    public void setUp() {
        ainc = new SSAutoIncrement();
        key = "KEY";
        value = 3141593;
    }

    @org.junit.After
    public void tearDown() {}

    @org.junit.Test
    public void GetNumberIsZero() {
        assertEquals(ainc.getNumber(key), 0);
    }

    @org.junit.Test
    public void IncrementAndGet() {
        ainc.doAutoIncrement(key);
        assertEquals(ainc.getNumber(key), 1);
    }

    @org.junit.Test
    public void SetAndGet() {
        ainc.setNumber(key, value);
        assertEquals(ainc.getNumber(key), value);
    }

    @org.junit.Test
    public void SetIncrementAndGet() {
        ainc.setNumber(key, value);
        ainc.doAutoIncrement(key);
        assertEquals(ainc.getNumber(key), value + 1);
    }

    @org.junit.Test
    public void IncrementSetAndGet() {
        ainc.doAutoIncrement(key);
        ainc.setNumber(key, value);
        assertEquals(ainc.getNumber(key), value);
    }

    @org.junit.Test
    public void SetAndGetThreeDifferent() {
        ainc.setNumber(key + "a", value + 1);
        ainc.setNumber(key + "b", value + 2);
        ainc.setNumber(key + "c", value + 3);
        assertEquals(ainc.getNumber(key + "a"), value + 1);
        assertEquals(ainc.getNumber(key + "b"), value + 2);
        assertEquals(ainc.getNumber(key + "c"), value + 3);
    }

    @org.junit.Test
    public void StringValueMatchesWhatWePutIn() {
        ainc.setNumber(key + "a", value + 1);
        ainc.setNumber(key + "b", value + 2);
        ainc.setNumber(key + "c", value + 3);
        String valueString = String.valueOf(value);
        Pattern pattern = Pattern.compile(
                key + "[a-c]" + " " + valueString.substring(0, valueString.length() - 1));

        String result = ainc.toString();
        Matcher matcher = pattern.matcher(result);

        assertTrue(matcher.find());
        assertTrue(matcher.find());
        assertTrue(matcher.find());
    }

    @org.junit.Ignore
    public void WhatAboutNegativeNumbers() {// TODO: Decide what to do if passed negative numbers and test it
    }
}
