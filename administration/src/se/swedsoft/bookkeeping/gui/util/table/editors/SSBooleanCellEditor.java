/*
 * @(#)SSIntegerCellEditor.java                v 1.0 2005-jul-25
 *
 * Time-stamp: <2005-jul-25 21:13:52 Hasse>
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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 */
public class SSBooleanCellEditor extends DefaultCellEditor {

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    // Our panel
    private JPanel iPanel;


    /**
     * Default constructor.
     */
    public SSBooleanCellEditor() {
        super(new JCheckBox());
        iPanel = new JPanel();

        JCheckBox checkBox = (JCheckBox)getComponent();
	    checkBox.setHorizontalAlignment(JCheckBox.CENTER);
    }


    /**
     * Implements the <code>TableCellEditor</code> interface.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Component c =  super.getTableCellEditorComponent(table, value, isSelected, row, column);

        if(value == null){
            if (isSelected) {
                iPanel.setForeground(table.getSelectionForeground());
                iPanel.setBackground(table.getSelectionBackground());
            } else {
                iPanel.setForeground(table.getForeground());
                iPanel.setBackground(table.getBackground());
            }

            iPanel.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));

            return iPanel;
        }

        return c;

    }


}
