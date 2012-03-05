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
 * Fribok is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Fribok.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.swedsoft.bookkeeping.calc;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.*;
import se.swedsoft.bookkeeping.calc.SSOCRNumber;
import se.swedsoft.bookkeeping.data.SSInvoice;
import static org.junit.Assert.*;


/**
 * Tests for SSOCRNumber
 *
 * @author Stefan Kangas
 * @version $Rev$, $Date$
 */
public class SSOCRNumberTest {
    private SSInvoice invoice;

    @Before
    public void setUp() {
        invoice = new SSInvoice();
    }

    @After
    public void tearDown() {}

    /* Generate and return ocr for default test object
     */
    private String ocr() {
        return SSOCRNumber.getOCRNumber(invoice);
    }

    @Test
    public void OCRNumberIsTheSameForOneNumber() {
        invoice.setNumber(512);
        String one = ocr();
        String two = ocr();

        assertEquals(one, two);
    }

    @Test
    public void OCRChangesWhenInvoiceNumberChanges() {
        invoice.setNumber(256);
        String one = ocr();

        invoice.setNumber(512);
        String two = ocr();

        assertTrue(!one.equals(two));
    }

    @Test
    public void OCRContainsInvoiceNumber() {
        int num = 65536;

        invoice.setNumber(num);
        Pattern pattern = Pattern.compile(Integer.toString(num));
        Matcher matcher = pattern.matcher(ocr());

        assertTrue(matcher.find());
    }

    /* This is here to ensure we don't accidentally change the behavior of the
     * OCR generation.  Every time it changes, these values should be updated as
     * well.
     */
    @Test
    public void DontChangeBehaviorWithoutChangingMe() {
        int num = 65536;

        invoice.setNumber(num);
        assertEquals("6553671", ocr());
    }
}
