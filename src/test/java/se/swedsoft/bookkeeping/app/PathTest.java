/*
 * Copyright © 2010 Stefan Kangas <skangas@skangas.se>
 *
 * This file is part of Fribok.
 *
 * Fribok is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Fribok is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Fribok.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.swedsoft.bookkeeping.app;


import org.junit.Assert;
import org.junit.Test;
import se.swedsoft.bookkeeping.app.Path;


/**
 * Tests for Path
 *
 * @author Stefan Kangas
 * @version $Id$
 */
public class PathTest {
    @Test
    public void ValuesNotNull() {
        for (Path path : Path.values()) {
            Assert.assertNotNull(Path.get(path));
        }
    }
}
