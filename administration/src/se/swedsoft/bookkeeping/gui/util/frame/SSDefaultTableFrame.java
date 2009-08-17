/*
 * @(#)SSDefaultTableFrame.java                v 1.0 2005-jul-07
 *
 * Time-stamp: <2005-jul-07 08:58:48 Hasse>
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
package se.swedsoft.bookkeeping.gui.util.frame;

import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class should be the default class for internal frames in this application. <P>
 * Use this rather than the base class SSInternalFrame to enable global changes
 * to the application GUI.
 *
 * @author Roger Björnstedt
 */
public abstract class SSDefaultTableFrame extends SSInternalFrame {

    /**
     * Constructor
     *
     * @param pMainFrame
     * @param pTitle
     * @param pWidth
     * @param pHeight
     */
    public SSDefaultTableFrame(SSMainFrame pMainFrame, String pTitle, int pWidth, int pHeight) {
        super(pMainFrame, pTitle, pWidth, pHeight);

        setLayout(new BorderLayout());

        // Make sure to get the main content first in order to make sure
        // that the content is created before we create the additional
        // components.
        JComponent iMainContent = getMainContent();

        // Get the toolbar
        JToolBar iToolBar = getToolBar();
        if (iToolBar != null) {
            iToolBar.setFloatable(false);

            add(iToolBar, BorderLayout.NORTH);
        }

        // Get the status bar
        JComponent iStatusBar = getStatusBar();
        if (iStatusBar != null) {
            add(iStatusBar, BorderLayout.SOUTH);
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(iMainContent, BorderLayout.CENTER);

    }






    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    public abstract JToolBar getToolBar();


    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    public abstract JComponent getMainContent();


    /**
     * This method should return the status bar content, if any. <P>
     *
     * @return The content for the status bar or null if none is wanted.
     */
    public abstract JComponent getStatusBar();
}
