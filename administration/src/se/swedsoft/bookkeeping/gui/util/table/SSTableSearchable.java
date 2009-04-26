package se.swedsoft.bookkeeping.gui.util.table;

/*
 * @(#)SSTableSearchable.java                v 1.0 2005-aug-29
 *
 * Time-stamp: <2005-aug-29 14:03:10 Hasse>
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

public interface SSTableSearchable {

    /**
     * Returns the render string to be shown in the tables
     * 
     * @return The searchable string
     */
    public String toRenderString();

}
