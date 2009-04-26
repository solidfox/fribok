/*
 * @(#)SSBigDecimalCellRenderer.java                v 1.0 2005-sep-24
 *
 * Time-stamp: <2005-sep-24 11:48:24 Hasse>
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
import java.text.NumberFormat;
import java.math.BigDecimal;


/**
 */
public class SSBigDecimalCellRenderer extends DefaultTableCellRenderer {

    /**
     *
     */
    private int iFractionDigits;

    private boolean iShowNullValues;

    /**
     *
     * @param pFractionDigits
     */
    public SSBigDecimalCellRenderer(int pFractionDigits) {
        this(pFractionDigits, false);
   }
    /**
     *
     * @param pFractionDigits
     * @param pShowNullValues
     */
    public SSBigDecimalCellRenderer(int pFractionDigits, boolean pShowNullValues) {
        super();
        iFractionDigits = pFractionDigits;
        iShowNullValues = pShowNullValues;

        setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
    }

    /**
     *
     * @param value
     */
    protected void setValue(Object value) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(iFractionDigits);
        format.setMaximumFractionDigits(iFractionDigits);
        format.setGroupingUsed(true);

        if(value != null){
            setText( format.format(value) );
        } else{

            if(iShowNullValues){
                setText(format.format(0.00));
            } else {
                setText("");
            }
        }


    }
}
