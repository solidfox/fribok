/*
 * @(#)SSNewAccountingYearFrame.java                v 1.0 2005-jul-27
 *
 * Time-stamp: <2005-jul-27 21:58:39 Hasse>
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
package se.swedsoft.bookkeeping.gui.accountingyear;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.accountingyear.panel.SSStartingAmountPanel;
import se.swedsoft.bookkeeping.gui.util.*;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.frame.*;
import se.swedsoft.bookkeeping.calc.SSBalanceCalculator;
import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.print.report.SSStartingAmountPrinter;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.*;
import java.math.BigDecimal;


/**
 * Date: 2006-feb-15
 * Time: 11:17:40
 */
public class SSStartingAmountFrame extends SSDefaultTableFrame {


    private static SSStartingAmountFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSStartingAmountFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewAccountingYearFrame
     */
    public static SSStartingAmountFrame getInstance(){
        return cInstance;
    }


    private SSStartingAmountPanel iStartingAmountPanel;

    private SSNewAccountingYear      iAccountingYear;

    /**
     * Constructor
     * @param pMainFrame
     * @param width
     * @param height
     */
    private SSStartingAmountFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("startingammountframe.title"), width, height);

        iAccountingYear = SSDB.getInstance().getCurrentYear();


        iStartingAmountPanel.setInBalance( iAccountingYear.getInBalance(),  SSAccountMath.getBalanceAccounts( iAccountingYear) );
        addCloseListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cInstance != null ){
                    SSQueryDialog iDialog = new SSQueryDialog( getMainFrame(), SSBundle.getBundle(),  "startingammountpanel.saveonclose" );
                    int iResponce = iDialog.getResponce();
                    if(iResponce != JOptionPane.YES_OPTION) {
                        SSPostLock.removeLock("startingamount"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId());
                        return;
                    }
                    iAccountingYear.setInBalance( iStartingAmountPanel.getInBalance()  );
                    SSDB.getInstance().updateAccountingYear(iAccountingYear);
                    SSPostLock.removeLock("startingamount"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId());
                }
            }
        });
    }


    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        SSButton iButton;

        // Save
        // ***************************
        iButton = new SSButton("ICON_SAVEITEM", "startingammountframe.savebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                iAccountingYear.setInBalance(iStartingAmountPanel.getInBalance());
                SSDB.getInstance().updateAccountingYear(iAccountingYear);
                SSPostLock.removeLock("startingamount"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId());
                cInstance = null;
                setVisible(false);
            }
        });
        toolBar.add(iButton);

        // Cancel
        // ***************************
        iButton = new SSButton("ICON_CANCELITEM", "startingammountframe.cancelbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock("startingamount"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId());
                cInstance = null;
                setVisible(false);
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();
            /*
        // Import
        // ***************************
        iButton = new SSButton("ICON_IMPORT", "startingammountframe.importbutton", false, new ActionListener(){
            public void actionPerformed(ActionEvent e) {

            }
        });
        toolBar.add(iButton);

        // Export
        // ***************************
        iButton = new SSButton("ICON_EXPORT", "startingammountframe.exportbutton", false, new ActionListener(){
            public void actionPerformed(ActionEvent e) {

            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();
               */

        // Import from balance budget
        // ***************************
        iButton = new SSButton("ICON_REDO", "startingammountframe.importbalancebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                importFromLastYearBalanceReport();
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Print
        // ***************************
        iButton = new SSButton("ICON_PRINT", "startingammountframe.printbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                printStartingAmmounts();
            }
        });
        toolBar.add(iButton);


        return toolBar;
    }


    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    public JComponent getMainContent() {
        iStartingAmountPanel = new SSStartingAmountPanel();
        JPanel iPanel = new JPanel();
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iStartingAmountPanel.getPanel() , BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));
        return iPanel;
    }

    /**
     * This method should return the status bar content, if any. <P>
     *
     * @return The content for the status bar or null if none is wanted.
     */
    public JComponent getStatusBar() {
        return null;
    }


    /**
     * Indicates whether this frame is a company data related frame. <P>
     *
     * @return A boolean value.
     */
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame. <P>
     *
     * @return A boolean value.
     */
    public boolean isYearDataFrame() {
        return true;
    }

    /**
     *
     */
    private void printStartingAmmounts() {
        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

                Date iFrom = iAccountingYear.getFrom();
                Date iTo = iAccountingYear.getTo();
                SSStartingAmountPrinter iPrinter = new SSStartingAmountPrinter(iStartingAmountPanel.getInBalance(), iFrom, iTo);

                iPrinter.preview( getMainFrame() );
            }
        });
    }


    /**
     *
     */
    private void importFromLastYearBalanceReport(){
        SSNewAccountingYear iPreviousYear = SSDB.getInstance().getPreviousYear();

        // If nothing selected, return
        if(iPreviousYear == null){
            new SSErrorDialog( getMainFrame(), "startingammountpanel.nopreviousyear" );
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog( getMainFrame(), SSBundle.getBundle(),  "startingammountpanel.importbalance" );
        int iResponce = iDialog.getResponce();
        if(iResponce != JOptionPane.YES_OPTION) {
            return;
        }
        //SSBalanceCalculator iCalculator = new SSBalanceCalculator(iPreviousYear);

       // iCalculator.calculate();

        Map<SSAccount, BigDecimal > iOutBalance = SSBalanceCalculator.getOutBalance(iPreviousYear);

           //     iCalculator.getOutSaldo();

        iStartingAmountPanel.setInBalance(iOutBalance );
    }

    public void actionPerformed(ActionEvent e)
    {
        iStartingAmountPanel=null;
        iAccountingYear=null;
        SSStartingAmountFrame.cInstance=null;
    }
}




