/*
 * @(#)SSDateCellRenderer.java                v 1.0 2005-okt-16
 *
 * Time-stamp: <2005-okt-16 12:56:15 Hasse>
 *
 * Copyright (c) Trade Extensions TradeExt AB, Sweden.
 * www.tradeextensions.com, info@tradeextensions.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Trade Extensions ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Trade Extensions.
 */
package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;

// Trade Extensions specific imports

// Java specific imports

/**
 * This class implements a cell renderer that renders a Date using a DateFormat
 * with format DateFormat.SHORT. This will only display year-month-day. <P>
 *
 * @author Roger Björnstedt
 */
public class SSDateCellRenderer extends DefaultTableCellRenderer {


    // The formatter to use.
    private DateFormat iFormat;


    /**
     * Default constructor.
     */
    public SSDateCellRenderer() {
        super();
          setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
    }


    /**
     * Sets the value for the cell.
     *
     * @param value The value to format.
     */
    public void setValue(Object value) {
        if (iFormat == null) {
            iFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        }
        setText((value == null) ? "" : iFormat.format(value));
    }

}
