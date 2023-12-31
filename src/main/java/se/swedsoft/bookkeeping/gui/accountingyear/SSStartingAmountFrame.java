/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.accountingyear;


import se.swedsoft.bookkeeping.calc.SSBalanceCalculator;
import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountingyear.panel.SSStartingAmountPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.print.report.SSStartingAmountPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


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
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight) {
        if (cInstance == null || cInstance.isClosed()) {
            cInstance = new SSStartingAmountFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewAccountingYearFrame
     */
    public static SSStartingAmountFrame getInstance() {
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
        super(pMainFrame, SSBundle.getBundle().getString("startingammountframe.title"),
                width, height);

        iAccountingYear = SSDB.getInstance().getCurrentYear();

        iStartingAmountPanel.setInBalance(iAccountingYear.getInBalance(),
                SSAccountMath.getBalanceAccounts(iAccountingYear));
        addCloseListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cInstance != null) {
                    SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(),
                            SSBundle.getBundle(), "startingammountpanel.saveonclose");
                    int iResponce = iDialog.getResponce();

                    if (iResponce != JOptionPane.YES_OPTION) {
                        SSPostLock.removeLock(
                                "startingamount"
                                        + SSDB.getInstance().getCurrentCompany().getId()
                                        + SSDB.getInstance().getCurrentYear().getId());
                        return;
                    }
                    iAccountingYear.setInBalance(iStartingAmountPanel.getInBalance());
                    SSDB.getInstance().updateAccountingYear(iAccountingYear);
                    SSPostLock.removeLock(
                            "startingamount"
                                    + SSDB.getInstance().getCurrentCompany().getId()
                                    + SSDB.getInstance().getCurrentYear().getId());
                }
            }
        });
    }

    /**
     * This method should return a toolbar if the sub-class wants one.
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    @Override
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        // Save
        // ***************************
        SSButton iButton = new SSButton("ICON_SAVEITEM", "startingammountframe.savebutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iAccountingYear.setInBalance(iStartingAmountPanel.getInBalance());
                SSDB.getInstance().updateAccountingYear(iAccountingYear);
                SSPostLock.removeLock(
                        "startingamount" + SSDB.getInstance().getCurrentCompany().getId()
                        + SSDB.getInstance().getCurrentYear().getId());
                cInstance = null;
                setVisible(false);
            }
        });

        toolBar.add(iButton);

        // Cancel
        // ***************************
        iButton = new SSButton("ICON_CANCELITEM", "startingammountframe.cancelbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(
                        "startingamount" + SSDB.getInstance().getCurrentCompany().getId()
                        + SSDB.getInstance().getCurrentYear().getId());
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
        iButton = new SSButton("ICON_REDO", "startingammountframe.importbalancebutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importFromLastYearBalanceReport();
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Print
        // ***************************
        iButton = new SSButton("ICON_PRINT", "startingammountframe.printbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printStartingAmmounts();
            }
        });
        toolBar.add(iButton);

        return toolBar;
    }

    /**
     * This method should return the main content for the frame.
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    @Override
    public JComponent getMainContent() {
        iStartingAmountPanel = new SSStartingAmountPanel();
        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(iStartingAmountPanel.getPanel(), BorderLayout.CENTER);
        iPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return iPanel;
    }

    /**
     * This method should return the status bar content, if any.
     *
     * @return The content for the status bar or null if none is wanted.
     */
    @Override
    public JComponent getStatusBar() {
        return null;
    }

    /**
     * Indicates whether this frame is a company data related frame.
     *
     * @return A boolean value.
     */
    @Override
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame.
     *
     * @return A boolean value.
     */
    @Override
    public boolean isYearDataFrame() {
        return true;
    }

    /**
     *
     */
    private void printStartingAmmounts() {
        SSProgressDialog.runProgress(getMainFrame(),
                new Runnable() {
            public void run() {
                SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

                Date iFrom = iAccountingYear.getFrom();
                Date iTo = iAccountingYear.getTo();
                SSStartingAmountPrinter iPrinter = new SSStartingAmountPrinter(
                        iStartingAmountPanel.getInBalance(), iFrom, iTo);

                iPrinter.preview(getMainFrame());
            }
        });
    }

    /**
     *
     */
    private void importFromLastYearBalanceReport() {
        SSNewAccountingYear iPreviousYear = SSDB.getInstance().getPreviousYear();

        // If nothing selected, return
        if (iPreviousYear == null) {
            new SSErrorDialog(getMainFrame(), "startingammountpanel.nopreviousyear");
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), SSBundle.getBundle(),
                "startingammountpanel.importbalance");
        int iResponce = iDialog.getResponce();

        if (iResponce != JOptionPane.YES_OPTION) {
            return;
        }
        // SSBalanceCalculator iCalculator = new SSBalanceCalculator(iPreviousYear);

        // iCalculator.calculate();

        Map<SSAccount, BigDecimal > iOutBalance = SSBalanceCalculator.getOutBalance(
                iPreviousYear);

        // iCalculator.getOutSaldo();

        iStartingAmountPanel.setInBalance(iOutBalance);
    }

    public void actionPerformed(ActionEvent e) {
        iStartingAmountPanel = null;
        iAccountingYear = null;
        cInstance = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.accountingyear.SSStartingAmountFrame");
        sb.append("{iAccountingYear=").append(iAccountingYear);
        sb.append(", iStartingAmountPanel=").append(iStartingAmountPanel);
        sb.append('}');
        return sb.toString();
    }
}

