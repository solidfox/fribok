package se.swedsoft.bookkeeping.print;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import se.swedsoft.bookkeeping.app.Path;
import se.swedsoft.bookkeeping.calc.SSOCRNumber;
import se.swedsoft.bookkeeping.calc.math.*;
import se.swedsoft.bookkeeping.calc.util.SSAutoIncrement;
import se.swedsoft.bookkeeping.calc.util.SSVATUtil;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.*;
import se.swedsoft.bookkeeping.gui.voucher.SSVoucherFrame;
import se.swedsoft.bookkeeping.print.dialog.*;
import se.swedsoft.bookkeeping.print.report.*;
import se.swedsoft.bookkeeping.print.report.journals.*;
import se.swedsoft.bookkeeping.print.report.sales.*;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.util.*;
import java.util.List;


/**
 * $Id$
 *
 */
public class SSReportFactory {
    private static final File PDF_FILE_DIR = new File(Path.get(Path.APP_DATA), "pdftoemail");
    private SSReportFactory() {}

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param pYearData
     */
    public static void buildVoucherReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear pYearData) {
        final SSVoucherListDialog iDialog = new SSVoucherListDialog(iMainFrame);

        iDialog.setDateFrom(pYearData.getFrom());
        iDialog.setDateTo(pYearData.getTo());
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                List<SSVoucher> iVouchers = iDialog.getElementsToPrint();

                DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

                SSVoucherListPrinter iPrinter = new SSVoucherListPrinter(iVouchers);

                if (iDialog.isDateSelected()) {
                    Date iDateFrom = iDialog.getDateFrom();
                    Date iDateTo = iDialog.getDateTo();

                    iPrinter.addParameter("periodTitle",
                            String.format(
                            SSBundle.getBundle().getString("voucherlistreport.period.date"),
                            iFormat.format(iDateFrom), iFormat.format(iDateTo)));
                    iPrinter.addParameter("periodText", " ");
                }

                if (iDialog.isNumberSelected()) {
                    Integer iNumberFrom = iDialog.getNumberFrom();
                    Integer iNumberTo = iDialog.getNumberTo();

                    iPrinter.addParameter("periodTitle",
                            String.format(
                            SSBundle.getBundle().getString(
                                    "voucherlistreport.period.number"),
                                    iNumberFrom,
                                    iNumberTo));
                    iPrinter.addParameter("periodText", " ");
                }

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param pYearData
     */
    public static void MainbookReport(final SSMainFrame iMainFrame, final SSNewAccountingYear pYearData) {
        final SSMainBookDialog iDialog = new SSMainBookDialog(iMainFrame);

        iDialog.setDateFrom(pYearData.getFrom());
        iDialog.setDateTo(pYearData.getTo());

        iDialog.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();

                final Date       lDateFrom = iDialog.getDateFrom();
                final Date       lDateTo = iDialog.getDateTo();
                final SSAccount  lAccountFrom = iDialog.getAccountFrom();
                final SSAccount  lAccountTo = iDialog.getAccountTo();

                final SSNewProject    iProject = iDialog.getProject();
                final SSNewResultUnit iResultUnit = iDialog.getResultUnit();
                final boolean      isProjectSelected = iDialog.isProjectSelected();
                final boolean      isResultUnitSelected = iDialog.isResultUnitSelected();

                SSProgressDialog.runProgress(iMainFrame,
                        new Runnable() {
                    public void run() {
                        SSMainBookPrinter iPrinter = new SSMainBookPrinter(pYearData,
                                lAccountFrom, lAccountTo, lDateFrom, lDateTo, iProject,
                                iResultUnit);

                        if (isProjectSelected) {}
                        if (isResultUnitSelected) {}

                        iPrinter.preview(iMainFrame);
                    }
                });

            }
        });
        iDialog.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();

    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param yearData
     * @param lastYearData
     */
    public static void buildResultReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear yearData, final SSNewAccountingYear lastYearData) {
        Date from = yearData.getFrom();
        Date to = yearData.getTo();

        final SSDialog iDialog = new SSDialog(iMainFrame,
                bundle.getString("resultreport.perioddialog.title"));
        final SSResultPrinterSetupPanel iPanel = new SSResultPrinterSetupPanel();

        iPanel.setFrom(from);
        iPanel.setTo(to);
        iPanel.setPrintBudget(false);
        iPanel.setPrintLastyear(false);
        iPanel.setPrintLastyearEnabled(lastYearData != null);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Date    lFrom = iPanel.getFrom();
                final Date    lTo = iPanel.getTo();
                final boolean lPrintBudget = iPanel.getPrintBudget();
                final boolean lPrintLastyear = iPanel.getPrintLastyear();

                iDialog.closeDialog();

                SSProgressDialog.runProgress(iMainFrame,
                        new Runnable() {
                    public void run() {
                        SSResultPrinter iPrinter = new SSResultPrinter(lFrom, lTo,
                                lPrintBudget, lPrintLastyear);

                        iPrinter.preview(iMainFrame);
                    }
                });

            }
        });
        iPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });

	iDialog.getRootPane().setDefaultButton(iPanel.getButtonPanel().getOkButton());

        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    public static void buildOwnReport(final SSMainFrame iMainFrame, final SSOwnReport iOwnReport) {
        SSPeriodSelectionDialog iDateDialog = new SSPeriodSelectionDialog(iMainFrame,
                "Välj period");
        SSNewAccountingYear iYear = SSDB.getInstance().getCurrentYear();

        iDateDialog.setFrom(iYear.getFrom());
        iDateDialog.setTo(iYear.getTo());
        iDateDialog.setLocationRelativeTo(iMainFrame);

        if (iDateDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iFrom = iDateDialog.getFrom();
        final Date iTo = iDateDialog.getTo();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSOwnReportPrinter iPrinter = new SSOwnReportPrinter(iFrom, iTo,
                        iOwnReport);

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void ProjectResultReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, SSNewAccountingYear iAccountingYear) {
        final SSProjectResultSetupDialog iDialog = new SSProjectResultSetupDialog(
                iMainFrame, bundle.getString("resultreport.perioddialog.title"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date      iFrom = iDialog.getFrom();
        final Date      iTo = iDialog.getTo();
        final SSNewProject iProject = iDialog.getProject();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSProjectResultPrinter iPrinter = new SSProjectResultPrinter(iFrom, iTo,
                        iProject);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void ResultUnitResultReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, SSNewAccountingYear iAccountingYear) {

        final SSResultUnitResultSetupDialog iDialog = new SSResultUnitResultSetupDialog(
                iMainFrame, bundle.getString("resultreport.perioddialog.title"));

        iDialog.setFrom(iAccountingYear.getFrom());
        iDialog.setTo(iAccountingYear.getTo());
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date         iFrom = iDialog.getFrom();
        final Date         iTo = iDialog.getTo();
        final SSNewResultUnit iResultUnit = iDialog.getSelectedResultUnit();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSResultUnitResultPrinter iPrinter = new SSResultUnitResultPrinter(iFrom,
                        iTo, iResultUnit);

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void buildBalanceReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                bundle.getString("balancereport.perioddialog.title"));

        iDialog.setFrom(iAccountingYear.getFrom());
        iDialog.setTo(iAccountingYear.getTo());
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSBalancePrinter iPrinter = new SSBalancePrinter(iFrom, iTo);

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void buildBudgetReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                bundle.getString("budgetreport.perioddialog.title"));

        iDialog.setFrom(iAccountingYear.getFrom());
        iDialog.setTo(iAccountingYear.getTo());
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSBudgetPrinter iPrinter = new SSBudgetPrinter(iFrom, iTo);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void buildVATReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFrom = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iTo = iCalendar.getTime();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                bundle.getString("vatreport.perioddialog.title"));

        iDialog.setFrom(iFrom);
        iDialog.setTo(iTo);
        iDialog.setLocationRelativeTo(iMainFrame);
        int iResponce = iDialog.showDialog();

        if (iResponce != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }

        final Date localFrom = iDialog.getFrom();
        final Date localTo = iDialog.getTo();

        // Get the active accounts
        List<SSAccount> iAccounts = iAccountingYear.getAccounts();

        // If more then one account is marked with R1, R2 or A throw a warning message
        if (SSAccountMath.getNumAccountsByVatCode(iAccounts, "R1") > 1
                || SSAccountMath.getNumAccountsByVatCode(iAccounts, "R2") > 1
                || SSAccountMath.getNumAccountsByVatCode(iAccounts, "A") > 1) {
            SSPostLock.removeLock(lockString);
            new SSErrorDialog(iMainFrame, "vatbasis.dialog.morethenoneaccount");
            return;
        }
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        // Get the R1, R2 and A accounts
        final SSAccount accountR1 = SSAccountMath.getAccountWithVATCode(iAccounts, "R1",
                iAccountingYear.getAccountPlan().getAccount(1650));
        final SSAccount accountR2 = SSAccountMath.getAccountWithVATCode(iAccounts, "R2",
                iAccountingYear.getAccountPlan().getAccount(2650));
        final SSAccount accountA = SSAccountMath.getAccountWithVATCode(iAccounts, "A",
                iAccountingYear.getAccountPlan().getAccount(3740));

        String voucherName = String.format(bundle.getString("vatbasis.vouchername"),
                format.format(localFrom), format.format(localTo));

        final SSVoucher iVoucher = SSVATUtil.generateVATVoucher(voucherName, localFrom,
                localTo, accountR1, accountR2, accountA);

        // This runs the report generations with a progress iDialog
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {

                SSVATReportPrinter  iPrinter1 = new SSVATReportPrinter(iAccountingYear,
                        localFrom, localTo);
                SSVATControlPrinter iPrinter2 = new SSVATControlPrinter(iAccountingYear,
                        localFrom, localTo);
                SSVoucherPrinter iPrinter3 = new SSVoucherPrinter(iVoucher,
                        bundle.getString("vatbasisreport.title"), accountR1, accountR2,
                        accountA);

                SSMultiPrinter mPrinter = new SSMultiPrinter();

                mPrinter.addReport(iPrinter1);
                mPrinter.addReport(iPrinter2);
                mPrinter.addReport(iPrinter3);

                mPrinter.preview(iMainFrame,
                        new InternalFrameAdapter() {

                    /**
                     * Invoked when an internal frame has been closed.
                     */
                    @Override
                    public void internalFrameClosed(InternalFrameEvent e) {
                        // Ask the user if he wants to generate a vatVoucher
                        dialogVATVoucher(iMainFrame, iVoucher, iAccountingYear, localFrom,
                                localTo);

                        // For some reason this event get called over and over, this is a "hack" to avoid it
                        e.getInternalFrame().removeInternalFrameListener(this);
                    }
                });

            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void VATReport2007(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();
        SSVATReportDialog iDialog = new SSVATReportDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);
        int iResponce = iDialog.showDialog();

        if (iResponce != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        // Get the R1, R2 and A accounts
        final SSAccount iAccountR1 = iDialog.getAccountR1();
        final SSAccount iAccountR2 = iDialog.getAccountR2();
        final SSAccount iAccountA = iDialog.getAccountA();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                SSVATReport2007Printer   iPrinter1 = new SSVATReport2007Printer(
                        iAccountingYear, iFrom, iTo);
                SSVATControl2007Printer  iPrinter2 = new SSVATControl2007Printer(
                        iAccountingYear, iFrom, iTo);

                final SSVoucher iVoucher = iPrinter2.getVoucher(iAccountR1, iAccountR2,
                        iAccountA);

                SSVoucherPrinter  iPrinter3 = new SSVoucherPrinter(iVoucher,
                        bundle.getString("vatbasisreport.title"), iAccountR1, iAccountR2,
                        iAccountA);

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);
                iPrinter.addReport(iPrinter3);

                iPrinter.preview(iMainFrame,
                        new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);
                        SSQueryDialog iDialog = new SSQueryDialog(iMainFrame,
                                SSBundle.getBundle(), "vatcontrol2007.voucherdialog",
                                iFormat.format(iFrom), iFormat.format(iTo),
                                iVoucher.getNumber());

                        int iResponce = iDialog.getResponce();

                        if (iResponce != JOptionPane.OK_OPTION) {
                            SSPostLock.removeLock(lockString);
                            return;
                        }
                        SSDB.getInstance().addVoucher(iVoucher, false);

                        if (SSVoucherFrame.getInstance() != null) {
                            SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                        }
                        SSPostLock.removeLock(lockString);
                    }
                });
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void VATReport2015(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();
        SSVATReportDialog iDialog = new SSVATReportDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);
        int iResponce = iDialog.showDialog();

        if (iResponce != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        // Get the R1, R2 and A accounts
        final SSAccount iAccountR1 = iDialog.getAccountR1();
        final SSAccount iAccountR2 = iDialog.getAccountR2();
        final SSAccount iAccountA = iDialog.getAccountA();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                SSVATReport2015Printer   iPrinter1 = new SSVATReport2015Printer(
                        iAccountingYear, iFrom, iTo);
                SSVATControl2015Printer  iPrinter2 = new SSVATControl2015Printer(
                        iAccountingYear, iFrom, iTo);

                final SSVoucher iVoucher = iPrinter2.getVoucher(iAccountR1, iAccountR2,
                        iAccountA);

                SSVoucherPrinter  iPrinter3 = new SSVoucherPrinter(iVoucher,
                        bundle.getString("vatbasisreport.title"), iAccountR1, iAccountR2,
                        iAccountA);

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);
                iPrinter.addReport(iPrinter3);

                iPrinter.preview(iMainFrame,
                        new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);
                        SSQueryDialog iDialog = new SSQueryDialog(iMainFrame,
                                SSBundle.getBundle(), "vatcontrol2015.voucherdialog",
                                iFormat.format(iFrom), iFormat.format(iTo),
                                iVoucher.getNumber());

                        int iResponce = iDialog.getResponce();

                        if (iResponce != JOptionPane.OK_OPTION) {
                            SSPostLock.removeLock(lockString);
                            return;
                        }
                        SSDB.getInstance().addVoucher(iVoucher, false);

                        if (SSVoucherFrame.getInstance() != null) {
                            SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                        }
                        SSPostLock.removeLock(lockString);
                    }
                });
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void SimplestatementReport(final SSMainFrame iMainFrame) {
        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("simplestatement.dialog.title"));

        iDialog.setFrom(SSDB.getInstance().getCurrentYear().getFrom());
        iDialog.setTo(SSDB.getInstance().getCurrentYear().getTo());
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.YES_NO_OPTION) {
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSSimpleStatementPrinter iPrinter = new SSSimpleStatementPrinter(iFrom,
                        iTo);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param pVoucher
     * @param pAccountingYear
     * @param pFrom
     * @param pTo
     */
    public static void dialogVATVoucher(final SSMainFrame iMainFrame, final SSVoucher pVoucher, final SSNewAccountingYear pAccountingYear, final Date pFrom, final Date pTo) {
        String lockString = "voucher" + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        // Manually construct an input popup
        SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                "vatbasis.dialog", format.format(pFrom), format.format(pTo),
                pVoucher.getNumber());
        int iResponce = iDialog.getResponce();

        if (iResponce != JOptionPane.YES_NO_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        SSDB.getInstance().addVoucher(pVoucher, false);

        if (SSVoucherFrame.getInstance() != null) {
            SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
        }
        SSPostLock.removeLock(lockString);
    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param yearData
     */
    public static void buildAccountDiagramReport(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear yearData) {
        List<SSAccount> iAccounts = SSDB.getInstance().getAccounts();

        List<SSAccount> iAccountsWithoutSRUCode = SSAccountMath.getAccountsWithoutSRUCode(
                iAccounts);

        if (!iAccountsWithoutSRUCode.isEmpty()) {
            new SSWarningDialog(iMainFrame, "accountdiagramreport.dialogmissingSRUCode");
        }

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSAccountdiagramPrinter iPrinter = new SSAccountdiagramPrinter();

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     */
    public static void OrderListReport(final SSMainFrame iMainFrame) {
        SSOrderListDialog iDialog = new SSOrderListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSOrder> iOrders = iDialog.getOrdersToPrint();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSOrderListPrinter iPrinter = new SSOrderListPrinter(iOrders);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void InvoiceListReport(final SSMainFrame iMainFrame) {
        SSInvoiceListDialog iDialog = new SSInvoiceListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSInvoice> iInvoices = iDialog.getInvoicesToPrint();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSInvoiceListPrinter iPrinter = new SSInvoiceListPrinter(iInvoices);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void CreditInvoiceListReport(final SSMainFrame iMainFrame) {
        SSCreditInvoiceListDialog iDialog = new SSCreditInvoiceListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSCreditInvoice> iInvoices = iDialog.getInvoicesToPrint();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSCreditInvoiceListPrinter iPrinter = new SSCreditInvoiceListPrinter(
                        iInvoices);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void TenderListReport(final SSMainFrame iMainFrame) {
        SSTenderListDialog iDialog = new SSTenderListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSTender> iTenders = iDialog.getTendersToPrint();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSTenderListPrinter iPrinter = new SSTenderListPrinter(iTenders);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void PurchaseOrderListReport(final SSMainFrame iMainFrame) {
        SSPurchaseOrderListDialog iDialog = new SSPurchaseOrderListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSPurchaseOrder> iOrders = iDialog.getElementsToPrint();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSPurchaseOrderListPrinter iPrinter = new SSPurchaseOrderListPrinter(
                        iOrders);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void SupplierInvoiceListReport(final SSMainFrame iMainFrame) {
        SSSupplierInvoiceListDialog iDialog = new SSSupplierInvoiceListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSSupplierInvoice> iInvoices = iDialog.getElementsToPrint();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSSupplierInvoiceListPrinter iPrinter = new SSSupplierInvoiceListPrinter(
                        iInvoices);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void SupplierCreditInvoiceListReport(final SSMainFrame iMainFrame) {
        SSSupplierCreditInvoiceListDialog iDialog = new SSSupplierCreditInvoiceListDialog(
                iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSSupplierCreditInvoice> iInvoices = iDialog.getElementsToPrint();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSSupplierCreditInvoiceListPrinter iPrinter = new SSSupplierCreditInvoiceListPrinter(
                        iInvoices);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void InventoryList(final SSMainFrame iMainFrame) {
        final SSInventoryListDialog iDialog = new SSInventoryListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final List<SSInventory> iInventories = iDialog.getElementsToPrint();
        final boolean           isDateSelected = iDialog.isDateSelected();
        final boolean           isProductSelected = iDialog.isProductSelected();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSInventoryListPrinter iPrinter = new SSInventoryListPrinter(iInventories);

                if (isDateSelected) {
                    iPrinter.addParameter("dateFrom", iDialog.getDateFrom());
                    iPrinter.addParameter("dateTo", iDialog.getDateTo());
                }
                if (isProductSelected) {
                    SSProduct iProduct = iDialog.getProduct();

                    iPrinter.addParameter("periodTitle",
                            SSBundle.getBundle().getString(
                            "inventorylistreport.producttitle"));
                    iPrinter.addParameter("periodText",
                            iProduct == null ? null : iProduct.getNumber());
                }

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void IndeliveryList(final SSMainFrame iMainFrame) {
        final SSIndeliveryListDialog iDialog = new SSIndeliveryListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final List<SSIndelivery> iIndeliveries = iDialog.getElementsToPrint();
        final boolean            isDateSelected = iDialog.isDateSelected();
        final boolean            isProductSelected = iDialog.isProductSelected();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSIndeliveryListPrinter iPrinter = new SSIndeliveryListPrinter(
                        iIndeliveries);

                if (isDateSelected) {
                    iPrinter.addParameter("dateFrom", iDialog.getDateFrom());
                    iPrinter.addParameter("dateTo", iDialog.getDateTo());
                }
                if (isProductSelected) {
                    SSProduct iProduct = iDialog.getProduct();

                    iPrinter.addParameter("periodTitle",
                            SSBundle.getBundle().getString(
                            "indeliverylistreport.producttitle"));
                    iPrinter.addParameter("periodText",
                            iProduct == null ? null : iProduct.getNumber());
                }
                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void OutdeliveryList(final SSMainFrame iMainFrame) {
        final SSOutdeliveryListDialog iDialog = new SSOutdeliveryListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final List<SSOutdelivery> iOutdeliveries = iDialog.getElementsToPrint();
        final boolean             isDateSelected = iDialog.isDateSelected();
        final boolean             isProductSelected = iDialog.isProductSelected();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSOutdeliveryListPrinter iPrinter = new SSOutdeliveryListPrinter(
                        iOutdeliveries);

                if (isDateSelected) {
                    iPrinter.addParameter("dateFrom", iDialog.getDateFrom());
                    iPrinter.addParameter("dateTo", iDialog.getDateTo());
                }
                if (isProductSelected) {
                    SSProduct iProduct = iDialog.getProduct();

                    iPrinter.addParameter("periodTitle",
                            SSBundle.getBundle().getString(
                            "indeliverylistreport.producttitle"));
                    iPrinter.addParameter("periodText",
                            iProduct == null ? null : iProduct.getNumber());
                }
                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void InpaymentList(final SSMainFrame iMainFrame) {
        final SSInpaymentListDialog iDialog = new SSInpaymentListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSInpayment> iInpayments = iDialog.getElementsToPrint();
        final boolean           isDateSelected = iDialog.isDateSelected();
        final boolean           isInvoiceSelected = iDialog.isInvoiceSelected();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSInpaymentListPrinter iPrinter = new SSInpaymentListPrinter(iInpayments);

                if (isDateSelected) {
                    iPrinter.addParameter("dateFrom", iDialog.getDateFrom());
                    iPrinter.addParameter("dateTo", iDialog.getDateTo());
                }
                if (isInvoiceSelected) {
                    SSInvoice iInvoice = iDialog.getInvoice();

                    iPrinter.addParameter("periodTitle",
                            SSBundle.getBundle().getString(
                            "inpaymentlistreport.invoicetitle"));
                    iPrinter.addParameter("periodText",
                            iInvoice == null ? null : iInvoice.getNumber().toString());
                }

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void OutpaymentList(final SSMainFrame iMainFrame) {
        final SSOutpaymentListDialog iDialog = new SSOutpaymentListDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final List<SSOutpayment> iInpayments = iDialog.getElementsToPrint();
        final boolean           isDateSelected = iDialog.isDateSelected();
        final boolean           isInvoiceSelected = iDialog.isInvoiceSelected();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSOutpaymentListPrinter iPrinter = new SSOutpaymentListPrinter(iInpayments);

                if (isDateSelected) {
                    iPrinter.addParameter("dateFrom", iDialog.getDateFrom());
                    iPrinter.addParameter("dateTo", iDialog.getDateTo());
                }
                if (isInvoiceSelected) {
                    SSSupplierInvoice iInvoice = iDialog.getInvoice();

                    iPrinter.addParameter("periodTitle",
                            SSBundle.getBundle().getString(
                            "outpaymentlistreport.invoicetitle"));
                    iPrinter.addParameter("periodText",
                            iInvoice == null ? null : iInvoice.getNumber().toString());
                }

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void StockValue(final SSMainFrame iMainFrame) {
        SSStockValueDialog iDialog = new SSStockValueDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iDate = iDialog.getDate();
        final boolean isDateSelected = iDialog.isDateSelected();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSStockValuePrinter iPrinter;

                if (isDateSelected) {
                    iPrinter = new SSStockValuePrinter(iDate);
                } else {
                    iPrinter = new SSStockValuePrinter();
                }

                if (isDateSelected) {}

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void StockAccount(final SSMainFrame iMainFrame) {

        SSStockAccountDialog iDialog = new SSStockAccountDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final Date iDate = iDialog.getDate();
        final boolean iDateSelected = iDialog.isDateSelected();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {

                SSStockAccountPrinter iPrinter;

                if (iDateSelected) {
                    iPrinter = new SSStockAccountPrinter(iDate);
                } else {
                    iPrinter = new SSStockAccountPrinter();
                }
                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void AccountsRecievableReport(final SSMainFrame iMainFrame) {
        SSAccountsrecievableDialog iDialog = new SSAccountsrecievableDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final Date iDate = iDialog.getDate();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSAccountsRecievablePrinter iPrinter = new SSAccountsRecievablePrinter(
                        iDate);

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     */
    public static void CustomerClaimReport(final SSMainFrame iMainFrame) {
        SSCustomerclaimDialog iDialog = new SSCustomerclaimDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iDate = iDialog.getDate();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSCustomerclaimPrinter iPrinter = new SSCustomerclaimPrinter(
                        SSDateMath.ceil(iDate));

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     */
    public static void AccountsPayableReport(final SSMainFrame iMainFrame) {
        SSAccountsPayableDialog iDialog = new SSAccountsPayableDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iDate = iDialog.getDate();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSAccountsPayablePrinter iPrinter = new SSAccountsPayablePrinter(iDate);

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     */
    public static void SupplierDebtReport(final SSMainFrame iMainFrame) {
        SSSupplierdebtDialog iDialog = new SSSupplierdebtDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final Date iDate = iDialog.getDate();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSSupplierdebtPrinter iPrinter = new SSSupplierdebtPrinter(iDate);

                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     * Försäljningsrapport
     *
     * @param iMainFrame
     */
    public static void SaleReport(final SSMainFrame iMainFrame) {
        SSSaleReportDialog iDialog = new SSSaleReportDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }
        final Date                            iFrom = iDialog.getFrom();
        final Date                            iTo = iDialog.getTo();
        final SSSaleReportPrinter.SortingMode iSortingMode = iDialog.getSortingMode();
        final boolean                         iAscending = iDialog.getAscending();

        SSProgressDialog.runProgress(iMainFrame,
                SSBundle.getBundle().getString("salereport.title"),
                new Runnable() {
            public void run() {
                SSSaleReportPrinter iPrinter = new SSSaleReportPrinter(iFrom, iTo,
                        iSortingMode, iAscending);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void Salevalues(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                bundle.getString("salevalues.perioddialog.title"));

        if (iAccountingYear != null) {
            iDialog.setFrom(iAccountingYear.getFrom());
            iDialog.setTo(iAccountingYear.getTo());
        } else {
            Calendar iCal = Calendar.getInstance();

            iDialog.setFrom(iCal.getTime());
            iCal.add(Calendar.MONTH, 1);
            iDialog.setTo(iCal.getTime());
        }
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSSalevaluesPrinter iPrinter = new SSSalevaluesPrinter(iFrom, iTo);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param bundle
     * @param iAccountingYear
     */
    public static void Purchasevalues(final SSMainFrame iMainFrame, final ResourceBundle bundle, final SSNewAccountingYear iAccountingYear) {
        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                bundle.getString("purchasevalues.perioddialog.title"));

        if (iAccountingYear != null) {
            iDialog.setFrom(iAccountingYear.getFrom());
            iDialog.setTo(iAccountingYear.getTo());
        } else {
            Calendar iCal = Calendar.getInstance();

            iDialog.setFrom(iCal.getTime());
            iCal.add(Calendar.MONTH, 1);
            iDialog.setTo(iCal.getTime());
        }
        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSPurchasevaluePrinter iPrinter = new SSPurchasevaluePrinter(iFrom, iTo);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iInvoices
     */
    public static void InvoiceReport(final SSMainFrame iMainFrame, final List<SSInvoice> iInvoices) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.invoice"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        for (SSInvoice iInvoice : iInvoices) {
            iInvoice.setPrinted();
            SSDB.getInstance().updateInvoice(iInvoice);
        }
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSInvoice iInvoice : iInvoices) {
                    SSInvoicePrinter iInvoicePrinter = new SSInvoicePrinter(iInvoice,
                            iLanguage);

                    iPrinter.addReport(iInvoicePrinter);
                }
                iPrinter.preview(iMainFrame);

            }
        });
    }

    public static void EmailInvoiceReport(final SSMainFrame iMainFrame, final SSInvoice iInvoice) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.invoice"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        iInvoice.setPrinted();
        SSDB.getInstance().updateInvoice(iInvoice);
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();
                SSInvoicePrinter iInvoicePrinter = new SSInvoicePrinter(iInvoice,
                        iLanguage);

                iPrinter.addReport(iInvoicePrinter);

                iPrinter.generateReport();
                iPrinter.getPrinter();
                String iFileName = "faktura.pdf";
		if (!PDF_FILE_DIR.exists()) {
		    PDF_FILE_DIR.mkdirs();
		}

                try {
                    JasperExportManager.exportReportToPdfFile(iPrinter.getPrinter(),
                            new File(PDF_FILE_DIR, iFileName).getPath());
                } catch (JRException e) {
                    e.printStackTrace();
                }
                String iSubject = "Faktura " + iInvoice.getNumber() + " från "
                        + SSDB.getInstance().getCurrentCompany().getName();

                try {
                    if (!SSMail.sendMail(iInvoice.getCustomer().getEMail(), iSubject,
                            iFileName)) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new SSErrorDialog(SSMainFrame.getInstance(), "mail.somethingwrong");
                    return;
                }
                SSInformationDialog.showDialog(SSMainFrame.getInstance(), "mail.success");
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iInvoices
     */
    public static void OCRInvoiceReport(final SSMainFrame iMainFrame, final List<SSInvoice> iInvoices) {
        SSOCRInvoiceDialog iDialog = new SSOCRInvoiceDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.ocrinvoice"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale  iLanguage = iDialog.getLanguage();
        final boolean iShowBackground = iDialog.doShowBackground();

        for (SSInvoice iInvoice : iInvoices) {
            iInvoice.setPrinted();
            String iOCRNumber = SSOCRNumber.getOCRNumber(iInvoice);

            iInvoice.setOCRNumber(iOCRNumber);
            SSDB.getInstance().updateInvoice(iInvoice);
        }

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSInvoice iInvoice : iInvoices) {
                    SSOCRInvoicePrinter iInvoicePrinter = new SSOCRInvoicePrinter(iInvoice,
                            iLanguage, iShowBackground);

                    iPrinter.addReport(iInvoicePrinter);
                }
                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iCreditInvoices
     */
    public static void CreditInvoiceReport(final SSMainFrame iMainFrame, final List<SSCreditInvoice> iCreditInvoices) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.creditinvoice"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            iCreditInvoice.setPrinted();
            SSDB.getInstance().updateCreditInvoice(iCreditInvoice);
        }

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
                    SSCreditinvoicePrinter iCreditinvoicePrinter = new SSCreditinvoicePrinter(
                            iCreditInvoice, iLanguage);

                    iPrinter.addReport(iCreditinvoicePrinter);
                }
                iPrinter.preview(iMainFrame);

            }
        });
    }

    public static void EmailCreditInvoiceReport(final SSMainFrame iMainFrame, final SSCreditInvoice iCreditInvoice) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.creditinvoice"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        iCreditInvoice.setPrinted();
        SSDB.getInstance().updateCreditInvoice(iCreditInvoice);
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();
                SSCreditinvoicePrinter iCreditInvoicePrinter = new SSCreditinvoicePrinter(
                        iCreditInvoice, iLanguage);

                iPrinter.addReport(iCreditInvoicePrinter);

                iPrinter.generateReport();
                iPrinter.getPrinter();
                String iFileName = "kreditfaktura.pdf";
		if (!PDF_FILE_DIR.exists()) {
		    PDF_FILE_DIR.mkdirs();
		}
                try {
                    JasperExportManager.exportReportToPdfFile(iPrinter.getPrinter(),
                            new File(PDF_FILE_DIR, iFileName).getPath());
                } catch (JRException e) {
                    e.printStackTrace();
                }
                String iSubject = "Kreditfaktura " + iCreditInvoice.getNumber()
                        + " från " + SSDB.getInstance().getCurrentCompany().getName();

                try {
                    if (!SSMail.sendMail(iCreditInvoice.getCustomer().getEMail(), iSubject,
                            iFileName)) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new SSErrorDialog(SSMainFrame.getInstance(), "mail.somethingwrong");
                    return;
                }
                SSInformationDialog.showDialog(SSMainFrame.getInstance(), "mail.success");
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iOrders
     */
    public static void OrderReport(final SSMainFrame iMainFrame, final List<SSOrder> iOrders) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.order"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        for (SSOrder iOrder : iOrders) {
            iOrder.setPrinted();
            SSDB.getInstance().updateOrder(iOrder);
        }

        SSProgressDialog.runProgress(iMainFrame, new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSOrder iOrder : iOrders) {
                    SSOrderPrinter iOrderPrinter = new SSOrderPrinter(iOrder, iLanguage);

                    iPrinter.addReport(iOrderPrinter);
                }
                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iOrder
     */
    public static void EmailOrderReport(final SSMainFrame iMainFrame, final SSOrder iOrder) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.order"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        iOrder.setPrinted();
        SSDB.getInstance().updateOrder(iOrder);
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();
                SSOrderPrinter iOrderPrinter = new SSOrderPrinter(iOrder, iLanguage);

                iPrinter.addReport(iOrderPrinter);

                iPrinter.generateReport();
                iPrinter.getPrinter();
                String iFileName = "order.pdf";
		if (!PDF_FILE_DIR.exists()) {
		    PDF_FILE_DIR.mkdirs();
		}

                try {
                    JasperExportManager.exportReportToPdfFile(iPrinter.getPrinter(),
                            new File(PDF_FILE_DIR, iFileName).getPath());
                } catch (JRException e) {
                    e.printStackTrace();
                }
                String iSubject = "Order " + iOrder.getNumber() + " från "
                        + SSDB.getInstance().getCurrentCompany().getName();

                try {
                    if (iOrder.getCustomer() != null) {
                        if (!SSMail.sendMail(iOrder.getCustomer().getEMail(), iSubject,
                                iFileName)) {
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new SSErrorDialog(SSMainFrame.getInstance(), "mail.somethingwrong");
                    return;
                }
                SSInformationDialog.showDialog(SSMainFrame.getInstance(), "mail.success");
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iTenders
     */
    public static void TenderReport(final SSMainFrame iMainFrame, final List<SSTender> iTenders) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.tender"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        for (SSTender iTender : iTenders) {
            iTender.setPrinted();
            SSDB.getInstance().updateTender(iTender);
        }
        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSTender iTender : iTenders) {
                    SSTenderPrinter iTenderPrinter = new SSTenderPrinter(iTender,
                            iLanguage);

                    iPrinter.addReport(iTenderPrinter);
                }
                iPrinter.preview(iMainFrame);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iTender
     */
    public static void EmailTenderReport(final SSMainFrame iMainFrame, final SSTender iTender) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.tender"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        iTender.setPrinted();
        SSDB.getInstance().updateTender(iTender);

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();
                SSTenderPrinter iTenderPrinter = new SSTenderPrinter(iTender, iLanguage);

                iPrinter.addReport(iTenderPrinter);

                iPrinter.generateReport();
                iPrinter.getPrinter();
                String iFileName = "offert.pdf";
		if (!PDF_FILE_DIR.exists()) {
		    PDF_FILE_DIR.mkdirs();
		}

                try {
                    JasperExportManager.exportReportToPdfFile(iPrinter.getPrinter(),
                            new File(PDF_FILE_DIR, iFileName).getPath());
                } catch (JRException e) {
                    e.printStackTrace();
                }
                String iSubject = "Offert " + iTender.getNumber() + " från "
                        + SSDB.getInstance().getCurrentCompany().getName();

                try {
                    if (iTender.getCustomer() != null) {
                        if (!SSMail.sendMail(iTender.getCustomer().getEMail(), iSubject,
                                iFileName)) {
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new SSErrorDialog(SSMainFrame.getInstance(), "mail.somethingwrong");
                    return;
                }
                SSInformationDialog.showDialog(SSMainFrame.getInstance(), "mail.success");
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iOrders
     */
    public static void PickingslipReport(final SSMainFrame iMainFrame, final List<SSOrder> iOrders) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.pickingslip"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSOrder iOrder : iOrders) {
                    SSPickingslipPrinter iPickingslipPrinter = new SSPickingslipPrinter(
                            iOrder, iLanguage);

                    iPrinter.addReport(iPickingslipPrinter);
                }
                iPrinter.preview(iMainFrame);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param iOrders
     */
    public static void DeliverynoteReport(final SSMainFrame iMainFrame, final List<SSOrder> iOrders) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.deliverynote"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSOrder iOrder : iOrders) {
                    SSDeliverynotePrinter iDeliverynotePrinter = new SSDeliverynotePrinter(
                            iOrder, iLanguage);

                    iPrinter.addReport(iDeliverynotePrinter);
                }
                iPrinter.preview(iMainFrame);

            }
        });

    }

    /**
     *
     * @param iMainFrame
     * @param iPurchaseOrders
     */
    public static void PurchaseOrderReport(final SSMainFrame iMainFrame, final  List<SSPurchaseOrder> iPurchaseOrders) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.purchaseorder"));

        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        for (SSPurchaseOrder iPurchaseOrder : iPurchaseOrders) {
            iPurchaseOrder.setPrinted();
            SSDB.getInstance().updatePurchaseOrder(iPurchaseOrder);
        }
        final Locale iLanguage = iDialog.getLanguage();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSPurchaseOrder iPurchaseOrder : iPurchaseOrders) {
                    SSPurchaseOrderPrinter iPurchaseOrderPrinter = new SSPurchaseOrderPrinter(
                            iPurchaseOrder, iLanguage);

                    iPrinter.addReport(iPurchaseOrderPrinter);
                }
                iPrinter.preview(iMainFrame);

            }
        });
    }

    public static void EmailPurchaseOrderReport(final SSMainFrame iMainFrame, final SSPurchaseOrder iPurchaseOrder) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.purchaseorder"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        iPurchaseOrder.setPrinted();
        SSDB.getInstance().updatePurchaseOrder(iPurchaseOrder);

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();
                SSPurchaseOrderPrinter iPurchaseOrderPrinter = new SSPurchaseOrderPrinter(
                        iPurchaseOrder, iLanguage);

                iPrinter.addReport(iPurchaseOrderPrinter);

                iPrinter.generateReport();
                iPrinter.getPrinter();
                String iFileName = "inkopsorder.pdf";
		if (!PDF_FILE_DIR.exists()) {
		    PDF_FILE_DIR.mkdirs();
		}

                try {
                    JasperExportManager.exportReportToPdfFile(iPrinter.getPrinter(),
                            new File(PDF_FILE_DIR, iFileName).getPath());
                } catch (JRException e) {
                    e.printStackTrace();
                }
                String iSubject = "Inköpsorder " + iPurchaseOrder.getNumber() + " från "
                        + SSDB.getInstance().getCurrentCompany().getName();

                try {
                    if (!SSMail.sendMail(iPurchaseOrder.getSupplier().getEMail(), iSubject,
                            iFileName)) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new SSErrorDialog(SSMainFrame.getInstance(), "mail.somethingwrong");
                    return;
                }
                SSInformationDialog.showDialog(SSMainFrame.getInstance(), "mail.success");
            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iPurchaseOrders
     */
    public static void InquiryReport(final SSMainFrame iMainFrame, final  List<SSPurchaseOrder> iPurchaseOrders) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.inquiry"));

        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();

                for (SSPurchaseOrder iPurchaseOrder : iPurchaseOrders) {
                    SSInquiryPrinter iInquiryPrinter = new SSInquiryPrinter(iPurchaseOrder,
                            iLanguage);

                    iPrinter.addReport(iInquiryPrinter);
                }
                iPrinter.preview(iMainFrame);

            }
        });
    }

    public static void EmailInquiryReport(final SSMainFrame iMainFrame, final  SSPurchaseOrder iPurchaseOrder) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.inquiry"));

        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSMultiPrinter iPrinter = new SSMultiPrinter();
                SSInquiryPrinter iInquiryPrinter = new SSInquiryPrinter(iPurchaseOrder,
                        iLanguage);

                iPrinter.addReport(iInquiryPrinter);
                iPrinter.generateReport();
                iPrinter.getPrinter();
                String iFileName = "forfragan.pdf";
		if (!PDF_FILE_DIR.exists()) {
		    PDF_FILE_DIR.mkdirs();
		}

                try {
                    JasperExportManager.exportReportToPdfFile(iPrinter.getPrinter(),
                            new File(PDF_FILE_DIR, iFileName).getPath());
                } catch (JRException e) {
                    e.printStackTrace();
                }
                String iSubject = "Förfrågan från "
                        + SSDB.getInstance().getCurrentCompany().getName();

                try {
                    if (!SSMail.sendMail(iPurchaseOrder.getSupplier().getEMail(), iSubject,
                            iFileName)) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new SSErrorDialog(SSMainFrame.getInstance(), "mail.somethingwrong");
                    return;
                }
                SSInformationDialog.showDialog(SSMainFrame.getInstance(), "mail.success");

            }
        });
    }

    /**
     *
     * @param iMainFrame
     * @param iInvoices
     */
    public static void ReminderReport(final SSMainFrame iMainFrame, final List<SSInvoice> iInvoices) {
        SSLanguageDialog iDialog = new SSLanguageDialog(iMainFrame,
                SSBundle.getBundle().getString("report.title.reminder"));

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Locale iLanguage = iDialog.getLanguage();

        final InternalFrameAdapter iRegisterAdapter = new InternalFrameAdapter() {

            /**
             * Invoked when an internal frame has been closed.
             */
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(),
                        "invoiceframe.registerremainder")
                        != JOptionPane.OK_OPTION) {
                    return;
                }
                for (SSInvoice iInvoice : iInvoices) {
                    iInvoice.setNumRemainders(iInvoice.getNumReminders() + 1);
                    SSDB.getInstance().updateInvoice(iInvoice);
                }
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                Map<SSCustomer, List<SSInvoice>> iInvoicesPerCustomer = new HashMap<SSCustomer, List<SSInvoice>>();

                for (SSInvoice iInvoice : iInvoices) {
                    SSCustomer iCustomer = iInvoice.getCustomer();

                    List<SSInvoice> iInvoicesForCustomer = iInvoicesPerCustomer.get(
                            iCustomer);

                    if (iInvoicesForCustomer == null) {
                        iInvoicesForCustomer = new LinkedList<SSInvoice>();

                        iInvoicesPerCustomer.put(iCustomer, iInvoicesForCustomer);
                    }
                    iInvoicesForCustomer.add(iInvoice);
                }
                // Create the multi report
                SSMultiPrinter iMultiPrinter = new SSMultiPrinter();

                // Get the invoices for the customer
                for (Map.Entry<SSCustomer, List<SSInvoice>> ssCustomerListEntry : iInvoicesPerCustomer.entrySet()) {
                    List<SSInvoice> iInvoicesForCustomer = ssCustomerListEntry.getValue();

                    SSReminderPrinter iPrinter = new SSReminderPrinter(
                            iInvoicesForCustomer, ssCustomerListEntry.getKey(), iLanguage);

                    iMultiPrinter.addReport(iPrinter);
                }
                iMultiPrinter.preview(iMainFrame, iRegisterAdapter);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void QuarterReport(final SSMainFrame iMainFrame) {
        SSQuarterReportDialog iDialog = new SSQuarterReportDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iDate = iDialog.getDate();
        final Date iEndDate = iDialog.getEndDate();

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSQuarterReportPrinter iPrinter = new SSQuarterReportPrinter(
                        Locale.getDefault(), iDate, iEndDate);

                iPrinter.preview(iMainFrame);
            }
        });
    }

    // Journals

    /**
     *
     * @param iMainFrame
     */
    public static void InvoiceJournal(final SSMainFrame iMainFrame) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        SSAutoIncrement iAutoIncrement = SSDB.getInstance().getCurrentCompany().getAutoIncrement();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("invoicejournal.dialog.title"));
        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFirstDayOfMonth = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iLastDayOfMonth = iCalendar.getTime();

        iDialog.setFrom(iFirstDayOfMonth);
        iDialog.setTo(iLastDayOfMonth);

        iDialog.setLocationRelativeTo(iMainFrame);
        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final List<SSInvoice> iFiltered = new LinkedList<SSInvoice>();

        for (SSInvoice iInvoice : iInvoices) {
            if (!iInvoice.isEntered() && SSInvoiceMath.inPeriod(iInvoice, iFrom, iTo)) {
                iFiltered.add(iInvoice);
            }
        }
        if (iFiltered.isEmpty()) {
            SSPostLock.removeLock(lockString);
            new SSInformationDialog(iMainFrame, "invoicejournal.dialog.norows");
            return;
        }

        final Integer iNumber = iAutoIncrement.getNumber("invoicejournal") + 1;

        SSVoucher iVoucher = new SSVoucher();

        iVoucher.setDescription(
                String.format(
                        SSBundle.getBundle().getString(
                                "invoicejournal.voucher.description"),
                                iNumber));
        iVoucher.setDate(iTo);

        for (SSInvoice iInvoice : iFiltered) {
            SSVoucher iCurrent = iInvoice.generateVoucher();

            for (SSVoucherRow iRow : iCurrent.getRows()) {
                iVoucher.addVoucherRow(new SSVoucherRow(iRow));
            }
        }
        final SSVoucher iVoucher1 = SSVoucherMath.compress(iVoucher);

        final ActionListener iCloseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                        "invoicejournal.dialog.register", iNumber, iVoucher1.getNumber());

                if (iDialog.getResponce() != JOptionPane.YES_NO_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                // Mark all invoices as entered
                for (SSInvoice iInvoice : iFiltered) {
                    iInvoice.setEntered();
                    SSDB.getInstance().updateInvoice(iInvoice);
                }
                // Auto increment the invoice journal counter.
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                iCurrentCompany.getAutoIncrement().doAutoIncrement("invoicejournal");

                SSDB.getInstance().updateCompany(iCurrentCompany);
                // Add the voucher to the database.
                SSDB.getInstance().addVoucher(iVoucher1, false);

                if (SSVoucherFrame.getInstance() != null) {
                    SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                }
                SSPostLock.removeLock(lockString);
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSInvoicejournalPrinter iPrinter1 = new SSInvoicejournalPrinter(iFiltered,
                        iNumber, iTo);

                SSVoucherPrinter iPrinter2 = new SSVoucherPrinter(iVoucher1,
                        iPrinter1.getTitle());

                SSMultiPrinter iPrinter = new SSMultiPrinter();

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);

                iPrinter.preview(iMainFrame, iCloseListener);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void CreditInvoiceJournal(final SSMainFrame iMainFrame) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        SSAutoIncrement iAutoIncrement = SSDB.getInstance().getCurrentCompany().getAutoIncrement();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("creditinvoicejournal.dialog.title"));

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFirstDayOfMonth = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iLastDayOfMonth = iCalendar.getTime();

        iDialog.setFrom(iFirstDayOfMonth);
        iDialog.setTo(iLastDayOfMonth);

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final List<SSCreditInvoice> iFiltered = new LinkedList<SSCreditInvoice>();

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if (!iCreditInvoice.isEntered()
                    && SSInvoiceMath.inPeriod(iCreditInvoice, iFrom, iTo)) {
                iFiltered.add(iCreditInvoice);
            }
        }
        if (iFiltered.isEmpty()) {
            SSPostLock.removeLock(lockString);
            new SSInformationDialog(iMainFrame, "creditinvoicejournal.dialog.norows");
            return;
        }

        final Integer iNumber = iAutoIncrement.getNumber("creditinvoicejournal") + 1;

        SSVoucher iVoucher = new SSVoucher();

        iVoucher.setDescription(
                String.format(
                        SSBundle.getBundle().getString(
                                "creditinvoicejournal.voucher.description"),
                                iNumber));
        iVoucher.setDate(iTo);

        for (SSCreditInvoice iCreditInvoice : iFiltered) {
            SSVoucher iCurrent = iCreditInvoice.generateVoucher();

            for (SSVoucherRow iRow : iCurrent.getRows()) {
                iVoucher.addVoucherRow(new SSVoucherRow(iRow));
            }
        }
        final SSVoucher iVoucher1 = SSVoucherMath.compress(iVoucher);

        final ActionListener iCloseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                        "creditinvoicejournal.dialog.register", iNumber,
                        iVoucher1.getNumber());

                if (iDialog.getResponce() != JOptionPane.YES_NO_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                // Mark all invoices as entered
                for (SSCreditInvoice iCreditInvoice : iFiltered) {
                    iCreditInvoice.setEntered();
                    SSDB.getInstance().updateCreditInvoice(iCreditInvoice);
                }
                // Auto increment the invoice journal counter.
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                iCurrentCompany.getAutoIncrement().doAutoIncrement("creditinvoicejournal");
                SSDB.getInstance().updateCompany(iCurrentCompany);

                // Add the voucher to the database.
                SSDB.getInstance().addVoucher(iVoucher1, false);

                if (SSVoucherFrame.getInstance() != null) {
                    SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                }
                SSPostLock.removeLock(lockString);
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSCreditinvoicejournalPrinter iPrinter1 = new SSCreditinvoicejournalPrinter(
                        iFiltered, iNumber, iTo);

                SSVoucherPrinter            iPrinter2 = new SSVoucherPrinter(iVoucher1,
                        iPrinter1.getTitle());

                SSMultiPrinter iPrinter = new SSMultiPrinter();

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);

                iPrinter.preview(iMainFrame, iCloseListener);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     */
    public static void InpaymentJournal(final SSMainFrame iMainFrame) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        SSAutoIncrement iAutoIncrement = SSDB.getInstance().getCurrentCompany().getAutoIncrement();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("inpaymentjournal.dialog.title"));

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFirstDayOfMonth = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iLastDayOfMonth = iCalendar.getTime();

        iDialog.setFrom(iFirstDayOfMonth);
        iDialog.setTo(iLastDayOfMonth);

        /* iDialog.setFrom( SSDB.getInstance().getCurrentYear().getFrom() );
         iDialog.setTo  ( SSDB.getInstance().getCurrentYear().getTo()   );*/

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSInpayment> iInpayments = SSDB.getInstance().getInpayments();

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final List<SSInpayment> iFiltered = new LinkedList<SSInpayment>();

        for (SSInpayment iInpayment : iInpayments) {
            if (!iInpayment.isEntered()
                    && SSInpaymentMath.inPeriod(iInpayment, iFrom, iTo)) {
                iFiltered.add(iInpayment);
            }
        }
        if (iFiltered.isEmpty()) {
            SSPostLock.removeLock(lockString);
            new SSInformationDialog(iMainFrame, "inpaymentjournal.dialog.norows");
            return;
        }

        final Integer iNumber = iAutoIncrement.getNumber("inpaymentjournal") + 1;

        SSVoucher iVoucher = new SSVoucher();

        iVoucher.setDescription(
                String.format(
                        SSBundle.getBundle().getString(
                                "inpaymentjournal.voucher.description"),
                                iNumber));
        iVoucher.setDate(iTo);

        for (SSInpayment iInpayment : iFiltered) {
            SSVoucher iCurrent = iInpayment.generateVoucher();

            for (SSVoucherRow iRow : iCurrent.getRows()) {
                iVoucher.addVoucherRow(new SSVoucherRow(iRow));
            }
        }
        final SSVoucher iVoucher1 = SSVoucherMath.compress(iVoucher);

        final ActionListener iCloseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                        "inpaymentjournal.dialog.register", iNumber, iVoucher1.getNumber());

                if (iDialog.getResponce() != JOptionPane.YES_NO_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                // Mark all invoices as entered
                for (SSInpayment iInpayment : iFiltered) {
                    iInpayment.setEntered();
                    SSDB.getInstance().updateInpayment(iInpayment);
                }
                // Auto increment the invoice journal counter.
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                iCurrentCompany.getAutoIncrement().doAutoIncrement("inpaymentjournal");
                SSDB.getInstance().updateCompany(iCurrentCompany);

                // Add the voucher to the database.
                SSDB.getInstance().addVoucher(iVoucher1, false);

                if (SSVoucherFrame.getInstance() != null) {
                    SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                }
                SSPostLock.removeLock(lockString);
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSInpaymentjournalPrinter iPrinter1 = new SSInpaymentjournalPrinter(
                        iFiltered, iNumber, iTo);

                SSVoucherPrinter            iPrinter2 = new SSVoucherPrinter(iVoucher1,
                        iPrinter1.getTitle());

                SSMultiPrinter iPrinter = new SSMultiPrinter();

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);

                iPrinter.preview(iMainFrame, iCloseListener);
            }
        });

    }

    /**
     *
     * @param iMainFrame
     */
    public static void SupplierInvoiceJournal(final SSMainFrame iMainFrame) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }

        SSAutoIncrement iAutoIncrement = SSDB.getInstance().getCurrentCompany().getAutoIncrement();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("supplierinvoicejournal.dialog.title"));

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFirstDayOfMonth = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iLastDayOfMonth = iCalendar.getTime();

        iDialog.setFrom(iFirstDayOfMonth);
        iDialog.setTo(iLastDayOfMonth);

        iDialog.setLocationRelativeTo(iMainFrame);

        int iResponce = iDialog.showDialog();

        if (iResponce != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSSupplierInvoice> iInvoices = SSDB.getInstance().getSupplierInvoices();

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>();

        for (SSSupplierInvoice iInvoice : iInvoices) {
            if (!iInvoice.isEntered()
                    && SSSupplierInvoiceMath.inPeriod(iInvoice, iFrom, iTo)) {
                iFiltered.add(iInvoice);
            }
        }
        if (iFiltered.isEmpty()) {
            SSPostLock.removeLock(lockString);
            new SSInformationDialog(iMainFrame, "supplierinvoicejournal.dialog.norows");
            return;
        }

        final Integer iNumber = iAutoIncrement.getNumber("supplierinvoicejournal") + 1;

        SSVoucher iVoucher = new SSVoucher();

        iVoucher.setDescription(
                String.format(
                        SSBundle.getBundle().getString(
                                "supplierinvoicejournal.voucher.description"),
                                iNumber));
        iVoucher.setDate(iTo);

        for (SSSupplierInvoice iInvoice : iFiltered) {
            SSVoucher iCurrent = iInvoice.generateVoucher();

            for (SSVoucherRow iRow : iCurrent.getRows()) {
                iVoucher.addVoucherRow(new SSVoucherRow(iRow));
            }
        }
        final SSVoucher iVoucher1 = SSVoucherMath.compress(iVoucher);

        final ActionListener iCloseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                        "supplierinvoicejournal.dialog.register", iNumber,
                        iVoucher1.getNumber());
                int iResponce = iDialog.getResponce();

                if (iResponce != JOptionPane.YES_NO_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                // Mark all invoices as entered
                for (SSSupplierInvoice iInvoice : iFiltered) {
                    iInvoice.setEntered();
                    SSDB.getInstance().updateSupplierInvoice(iInvoice);
                }
                // Auto increment the invoice journal counter.
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                iCurrentCompany.getAutoIncrement().doAutoIncrement(
                        "supplierinvoicejournal");
                SSDB.getInstance().updateCompany(iCurrentCompany);
                // Add the voucher to the database.
                SSDB.getInstance().addVoucher(iVoucher1, false);

                if (SSVoucherFrame.getInstance() != null) {
                    SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                }
                SSPostLock.removeLock(lockString);
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSSupplierInvoicejournalPrinter iPrinter1 = new SSSupplierInvoicejournalPrinter(
                        iFiltered, iNumber, iTo);

                SSVoucherPrinter     iPrinter2 = new SSVoucherPrinter(iVoucher1,
                        iPrinter1.getTitle());

                SSMultiPrinter iPrinter = new SSMultiPrinter();

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);

                iPrinter.preview(iMainFrame, iCloseListener);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void SupplierCreditInvoiceJournal(final SSMainFrame iMainFrame) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        SSAutoIncrement iAutoIncrement = SSDB.getInstance().getCurrentCompany().getAutoIncrement();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("suppliercreditinvoicejournal.dialog.title"));

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFirstDayOfMonth = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iLastDayOfMonth = iCalendar.getTime();

        iDialog.setFrom(iFirstDayOfMonth);
        iDialog.setTo(iLastDayOfMonth);

        iDialog.setLocationRelativeTo(iMainFrame);

        int iResponce = iDialog.showDialog();

        if (iResponce != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSSupplierCreditInvoice> iInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final List<SSSupplierCreditInvoice> iFiltered = new LinkedList<SSSupplierCreditInvoice>();

        for (SSSupplierCreditInvoice iInvoice : iInvoices) {
            if (!iInvoice.isEntered()
                    && SSSupplierCreditInvoiceMath.inPeriod(iInvoice, iFrom, iTo)) {
                iFiltered.add(iInvoice);
            }
        }
        if (iFiltered.isEmpty()) {
            SSPostLock.removeLock(lockString);
            new SSInformationDialog(iMainFrame,
                    "suppliercreditinvoicejournal.dialog.norows");
            return;
        }

        final Integer iNumber = iAutoIncrement.getNumber("suppliercreditinvoicejournal")
                + 1;

        SSVoucher iVoucher = new SSVoucher();

        iVoucher.setDescription(
                String.format(
                        SSBundle.getBundle().getString(
                                "suppliercreditinvoicejournal.voucher.description"),
                                iNumber));
        iVoucher.setDate(iTo);

        for (SSSupplierCreditInvoice iInvoice : iFiltered) {
            SSVoucher iCurrent = iInvoice.generateVoucher();

            for (SSVoucherRow iRow : iCurrent.getRows()) {
                iVoucher.addVoucherRow(new SSVoucherRow(iRow));
            }
        }
        final SSVoucher iVoucher1 = SSVoucherMath.compress(iVoucher);

        final ActionListener iCloseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                        "suppliercreditinvoicejournal.dialog.register", iNumber,
                        iVoucher1.getNumber());
                int iResponce = iDialog.getResponce();

                if (iResponce != JOptionPane.YES_NO_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                // Mark all invoices as entered
                for (SSSupplierCreditInvoice iInvoice : iFiltered) {
                    iInvoice.setEntered();
                    SSDB.getInstance().updateSupplierCreditInvoice(iInvoice);
                }
                // Auto increment the invoice journal counter.
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                iCurrentCompany.getAutoIncrement().doAutoIncrement(
                        "suppliercreditinvoicejournal");
                SSDB.getInstance().updateCompany(iCurrentCompany);
                // Add the voucher to the database.
                SSDB.getInstance().addVoucher(iVoucher1, false);

                if (SSVoucherFrame.getInstance() != null) {
                    SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                }

                SSPostLock.removeLock(lockString);
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSSuppliercreditinvoicejournalPrinter iPrinter1 = new SSSuppliercreditinvoicejournalPrinter(
                        iFiltered, iNumber, iTo);

                SSVoucherPrinter     iPrinter2 = new SSVoucherPrinter(iVoucher1,
                        iPrinter1.getTitle());

                SSMultiPrinter iPrinter = new SSMultiPrinter();

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);

                iPrinter.preview(iMainFrame, iCloseListener);
            }
        });
    }

    /**
     *
     * @param iMainFrame
     */
    public static void OutpaymentJournal(final SSMainFrame iMainFrame) {
        final String lockString = "voucher"
                + SSDB.getInstance().getCurrentCompany().getId()
                + SSDB.getInstance().getCurrentYear().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        SSAutoIncrement iAutoIncrement = SSDB.getInstance().getCurrentCompany().getAutoIncrement();

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(iMainFrame,
                SSBundle.getBundle().getString("outpaymentjournal.dialog.title"));

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.MONTH, -1);

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date iFirstDayOfMonth = iCalendar.getTime();

        iCalendar.set(Calendar.DAY_OF_MONTH,
                iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date iLastDayOfMonth = iCalendar.getTime();

        iDialog.setFrom(iFirstDayOfMonth);
        iDialog.setTo(iLastDayOfMonth);

        iDialog.setLocationRelativeTo(iMainFrame);
        int iResponce = iDialog.showDialog();

        if (iResponce != JOptionPane.OK_OPTION) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final List<SSOutpayment> iFiltered = new LinkedList<SSOutpayment>();

        for (SSOutpayment iOutpayment : iOutpayments) {
            if (!iOutpayment.isEntered()
                    && SSOutpaymentMath.inPeriod(iOutpayment, iFrom, iTo)) {
                iFiltered.add(iOutpayment);
            }
        }
        if (iFiltered.isEmpty()) {
            SSPostLock.removeLock(lockString);
            new SSInformationDialog(iMainFrame, "outpaymentjournal.dialog.norows");
            return;
        }

        final Integer iNumber = iAutoIncrement.getNumber("outpaymentjournal") + 1;

        SSVoucher iVoucher = new SSVoucher();

        iVoucher.setDescription(
                String.format(
                        SSBundle.getBundle().getString(
                                "outpaymentjournal.voucher.description"),
                                iNumber));
        iVoucher.setDate(iTo);

        for (SSOutpayment iOutpayment : iFiltered) {
            SSVoucher iCurrent = iOutpayment.generateVoucher();

            for (SSVoucherRow iRow : iCurrent.getRows()) {
                iVoucher.addVoucherRow(new SSVoucherRow(iRow));
            }
        }
        final SSVoucher iVoucher1 = SSVoucherMath.compress(iVoucher);

        final ActionListener iCloseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSQueryDialog iDialog = new SSQueryDialog(iMainFrame, SSBundle.getBundle(),
                        "outpaymentjournal.dialog.register", iNumber,
                        iVoucher1.getNumber());
                int iResponce = iDialog.getResponce();

                if (iResponce != JOptionPane.YES_NO_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                // Mark all outpayments as entered
                for (SSOutpayment iOutpayment : iFiltered) {
                    iOutpayment.setEntered();
                    SSDB.getInstance().updateOutpayment(iOutpayment);
                }
                // Auto increment the invoice journal counter.
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                iCurrentCompany.getAutoIncrement().doAutoIncrement("outpaymentjournal");
                SSDB.getInstance().updateCompany(iCurrentCompany);
                // Add the voucher to the database.
                SSDB.getInstance().addVoucher(iVoucher1, false);

                if (SSVoucherFrame.getInstance() != null) {
                    SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                }
                SSPostLock.removeLock(lockString);
            }
        };

        SSProgressDialog.runProgress(iMainFrame,
                new Runnable() {
            public void run() {
                SSOutpaymentjournalPrinter iPrinter1 = new SSOutpaymentjournalPrinter(
                        iFiltered, iNumber, iTo);

                SSVoucherPrinter            iPrinter2 = new SSVoucherPrinter(iVoucher1,
                        iPrinter1.getTitle());

                SSMultiPrinter iPrinter = new SSMultiPrinter();

                iPrinter.addReport(iPrinter1);
                iPrinter.addReport(iPrinter2);

                iPrinter.preview(iMainFrame, iCloseListener);
            }
        });

    }

}

