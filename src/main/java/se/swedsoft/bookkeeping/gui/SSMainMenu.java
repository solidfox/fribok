package se.swedsoft.bookkeeping.gui;

import se.swedsoft.bookkeeping.app.Path;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.backup.SSBackupDatabase;
import se.swedsoft.bookkeeping.data.system.*;
import se.swedsoft.bookkeeping.gui.about.dialog.SSAboutDialog;
import se.swedsoft.bookkeeping.gui.accountingyear.SSAccountingYearFrame;
import se.swedsoft.bookkeeping.gui.accountingyear.SSStartingAmountFrame;
import se.swedsoft.bookkeeping.gui.accountplans.SSAccountPlanDialog;
import se.swedsoft.bookkeeping.gui.accountplans.SSAccountPlanFrame;
import se.swedsoft.bookkeeping.gui.autodist.SSAutoDistFrame;
import se.swedsoft.bookkeeping.gui.backup.SSBackupDialog;
import se.swedsoft.bookkeeping.gui.backup.SSBackupFrame;
import se.swedsoft.bookkeeping.gui.budget.SSBudgetFrame;
import se.swedsoft.bookkeeping.gui.company.SSCompanyDialog;
import se.swedsoft.bookkeeping.gui.company.SSCompanyFrame;
import se.swedsoft.bookkeeping.gui.creditinvoice.SSCreditInvoiceFrame;
import se.swedsoft.bookkeeping.gui.customer.SSCustomerFrame;
import se.swedsoft.bookkeeping.gui.help.SSHelpFrame;
import se.swedsoft.bookkeeping.gui.indelivery.SSIndeliveryFrame;
import se.swedsoft.bookkeeping.gui.inpayment.SSInpaymentFrame;
import se.swedsoft.bookkeeping.gui.inventory.SSInventoryFrame;
import se.swedsoft.bookkeeping.gui.invoice.SSInvoiceFrame;
import se.swedsoft.bookkeeping.gui.order.SSOrderFrame;
import se.swedsoft.bookkeeping.gui.outdelivery.SSOutdeliveryFrame;
import se.swedsoft.bookkeeping.gui.outpayment.SSOutpaymentFrame;
import se.swedsoft.bookkeeping.gui.ownreport.SSOwnReportFrame;
import se.swedsoft.bookkeeping.gui.periodicinvoice.SSPeriodicInvoiceFrame;
import se.swedsoft.bookkeeping.gui.product.SSProductFrame;
import se.swedsoft.bookkeeping.gui.project.SSProjectFrame;
import se.swedsoft.bookkeeping.gui.purchaseorder.SSPurchaseOrderFrame;
import se.swedsoft.bookkeeping.gui.purchasesuggestion.dialog.SSPurchaseSuggestionDialog;
import se.swedsoft.bookkeeping.gui.resultunit.SSResultUnitFrame;
import se.swedsoft.bookkeeping.gui.sie.dialog.SSExportSIEDialog;
import se.swedsoft.bookkeeping.gui.supplier.SSSupplierFrame;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.SSSupplierCreditInvoiceFrame;
import se.swedsoft.bookkeeping.gui.supplierinvoice.SSSupplierInvoiceFrame;
import se.swedsoft.bookkeeping.gui.tender.SSTenderFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.*;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSBackupFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSSIEFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterALL;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterBGMAX;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterTXT;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.frame.SSInternalFrame;
import se.swedsoft.bookkeeping.gui.util.menu.SSMenuLoader;
import se.swedsoft.bookkeeping.gui.voucher.SSVoucherFrame;
import se.swedsoft.bookkeeping.gui.vouchertemplate.SSVoucherTemplateFrame;
import se.swedsoft.bookkeeping.importexport.bgmax.SSBgMaxImporter;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxFile;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEType;
import se.swedsoft.bookkeeping.importexport.supplierpayments.SSSupplierPaymentImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.print.SSReportFactory;
import se.swedsoft.bookkeeping.print.dialog.SSInventoryBasisDialog;
import se.swedsoft.bookkeeping.print.report.SSAccountPlanPrinter;
import se.swedsoft.bookkeeping.print.report.SSInventoryBasisPrinter;
import se.swedsoft.bookkeeping.print.report.SSStartingAmountPrinter;
import se.swedsoft.bookkeeping.util.BrowserLaunch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @version $Id$
 */
public class SSMainMenu {

    private static final String MENU_RES = "/MainMenu.xml";

    private static final ResourceBundle bundle = SSBundle.getBundle();

    private SSMainFrame iMainFrame;

    private SSMenuLoader iMenuLoader;

    /**
     * Default constructor.
     * @param pMainFrame
     */
    public SSMainMenu(SSMainFrame pMainFrame) {
        iMainFrame  = pMainFrame;
        iMenuLoader = new SSMenuLoader();

        InputStream stream = getClass().getResourceAsStream(MENU_RES);
        iMenuLoader.loadMenus(stream);

        iMenuLoader.setEnabled("Company", /*SSBookkeeping.iCompany.getData()  != null*/false);
        iMenuLoader.setEnabled("Year"   , /*SSBookkeeping.iCompany.getCurrentYear()     != null*/false);

        loadActions();

        // Called when the year is changed
        SSDB.getInstance().addPropertyChangeListener("YEAR"   , new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iMenuLoader.setEnabled("Year" , evt.getNewValue() != null);
            }
        });

        // Called when the company is changed
        SSDB.getInstance().addPropertyChangeListener("COMPANY", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getNewValue() == null){
                    iMenuLoader.setEnabled("Company", false);
                    iMenuLoader.setEnabled("Year"   , false);
                } else {
                    iMenuLoader.setEnabled("Company", true);
                }
            }
        });

    }

    /**
     *
     * @return
     */
    public JMenuBar getMenuBar(){
        return iMenuLoader.getMenuBar("MainMenu");
    }

    /**
     *
     */
    private void loadActions(){
        loadFileActions();

        loadRegisterActions();

        loadInvoiceActions();

        bgcmenuActions();

        addStockActions();

        loadBookkeepingActions();

        loadPrintBookkeepingActions();

        loadHelpActions();

        loadWindowActions();
    }

    /**
     * Add actions to the File menu
     */
    private void loadFileActions(){
        // Open company
        // *****************************
        iMenuLoader.addActionListener("filemenu.companies", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCompanyFrame.showFrame(iMainFrame, 500, 300);
            }
        });

        // Company settings
        // *****************************
        iMenuLoader.addActionListener("filemenu.company.settings", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCompanyDialog.editCurrentDialog(iMainFrame, SSDB.getInstance().getCurrentCompany(), null);
            }
        });

        // Account plans
        // *****************************
        iMenuLoader.addActionListener("filemenu.accountplans", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlanFrame.showFrame(iMainFrame, 640, 480);
            }
        });

        // SIE Import
        // *****************************
        iMenuLoader.addActionListener("filemenu.import.sie", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();
                SSPostLock.applyLock("sieimport"+iCurrentCompany.getId());
                SSCompanyLock.removeLock(iCurrentCompany);
                if (SSCompanyLock.isLocked(iCurrentCompany)) {
                    SSCompanyLock.applyLock(iCurrentCompany);
                    SSPostLock.removeLock("sieimport"+iCurrentCompany.getId());
                    new SSErrorDialog( iMainFrame, "companyframe.siecompanyopen");
                    return;
                }
                SSCompanyLock.applyLock(iCurrentCompany);
                SSFileChooser iFileChooser = SSSIEFileChooser.getInstance();
                int iResponce = iFileChooser.showOpenDialog(iMainFrame);
                boolean iShowDialog = false;
                if(iResponce == JFileChooser.APPROVE_OPTION ){
                    iShowDialog = true;
                    SSSIEImporter iImporter = new SSSIEImporter(iFileChooser.getSelectedFile());
                    try {
                        iImporter.doImport(   );

                    } catch (SSImportException ex) {
                        SSPostLock.removeLock("sieimport"+iCurrentCompany.getId());
                        iShowDialog = false;
                        new SSErrorDialog(iMainFrame, "importexceptiondialog", ex.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        iShowDialog = false;
                        SSPostLock.removeLock("sieimport"+iCurrentCompany.getId());
                    }
                }
                SSPostLock.removeLock("sieimport"+iCurrentCompany.getId());
                if(iShowDialog)
                    new SSInformationDialog(iMainFrame, "sieimport.importdone");
            }
        });

        // Voucher import
        // *****************************
        iMenuLoader.addActionListener("filemenu.import.vouchers", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( iMainFrame, "voucheriscreated");
                    return;
                }
                SSSIEFileChooser iFileChooser = SSSIEFileChooser.getInstance();
                if (iFileChooser.showOpenDialog(iMainFrame) == JFileChooser.APPROVE_OPTION) {
                    SSSIEImporter iImporter = new SSSIEImporter(iFileChooser.getSelectedFile());

                    try {
                        iImporter.doImportVouchers();
                    } catch (SSImportException ex) {
                        SSPostLock.removeLock(lockString);
                        new SSErrorDialog(iMainFrame, "importexceptiondialog", ex.getMessage());
                    }
                    if (SSVoucherFrame.getInstance() != null) {
                        SSVoucherFrame.getInstance().getModel().fireTableDataChanged();
                    }
                    SSPostLock.removeLock(lockString);
                } else {
                    SSPostLock.removeLock(lockString);
                }

            }
        });

        // SIE export
        // *****************************
        iMenuLoader.addActionListener("filemenu.export.sie", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSExportSIEDialog.showDialog(iMainFrame);
            }
        });

        // Voucher export
        // *****************************
        iMenuLoader.addActionListener("filemenu.export.vouchers", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSIEFileChooser iFileChooser = SSSIEFileChooser.getInstance();

                iFileChooser.setDefaultFileName();

                if(iFileChooser.showSaveDialog(iMainFrame) == JFileChooser.APPROVE_OPTION ){
                    try {
                        SSSIEExporter iExporter = new SSSIEExporter(SIEType.SIE_4I, "Format SIE 4I");
                        iExporter.exportSIE(iFileChooser.getSelectedFile() );
                    } catch (SSExportException ex ) {
                        new SSErrorDialog(iMainFrame, "exportexceptiondialog", ex.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Backup all
        // *****************************
        iMenuLoader.addActionListener("filemenu.backup.all", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createBackupDialog();
            }
        });

        // Restore backup
        // *****************************
        iMenuLoader.addActionListener("filemenu.backup.restore", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (SSDB.getInstance().getLocking()) {
                    new SSInformationDialog(iMainFrame, "backupframe.runningonserver");
                    return;
                }
                SSBackupFrame.showFrame(iMainFrame, 600, 400);
            }
        });

        // Network settings
        // *****************************
        iMenuLoader.addActionListener("filemenu.network.settings.db", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String iIpAddress = SSInputDialog.showDialog();
                if(iIpAddress == null) return;
                try {
                    if (iIpAddress.length() == 0) {
                        throw new IOException("Could not connect to server on address null");
                    }
                    SSDB.getInstance().openSocket(iIpAddress);
                } catch (IOException ex){
                    SSQueryDialog iDialog = new SSQueryDialog(SSMainFrame.getInstance(),"noserverfound");

                    if (SSDBConfig.getClientkey() != null && SSDBConfig.getClientkey().length() != 0) {
                        SSDB.getInstance().removeClient();
                    }
                    if (iDialog.getResponce() == JOptionPane.YES_OPTION) {
                        if(SSDB.getInstance().getLocking()) {
                            SSCompanyLock.removeLock(SSDB.getInstance().getCurrentCompany());
                            SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
                        }
                        SSDB.getInstance().setLocking(true);
                        SSDBConfig.setServerAddress(null);
                        SSDB.getInstance().setCurrentCompany(null);
                        SSDB.getInstance().setCurrentYear(null);
                        SSFrameManager.getInstance().close();
                        SSDB.getInstance().loadLocalDatabase();
                        SSCompanyFrame.showFrame(iMainFrame, 500, 300);
                    }
                    return;
                }
                if(SSDB.getInstance().getLocking()){
                    SSCompanyLock.removeLock(SSDB.getInstance().getCurrentCompany());
                    SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
                }

                SSDBConfig.setServerAddress(iIpAddress);
                SSDB.getInstance().setCurrentCompany(null);
                SSDB.getInstance().setCurrentYear(null);
                SSFrameManager.getInstance().close();
                SSDB.getInstance().loadSelectedDatabase(iIpAddress);
                SSCompanyFrame.showFrame(iMainFrame, 500, 300);
            }
        });

        // Exit
        // *****************************
        iMenuLoader.addActionListener("filemenu.exit", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iMainFrame.saveSizeAndLocation();
                System.exit(0);
            }
        });

    }

    /**
     * Add actions to register menu
     */
    private void loadRegisterActions() {

        // Select accountingyear
        // *****************************
        iMenuLoader.addActionListener("registermenu.accountingyear", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountingYearFrame.showFrame(iMainFrame, 500, 300);
            }
        });

        // Change accountplan for this year
        // *****************************
        iMenuLoader.addActionListener("registermenu.accountplan", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Open warningdialog if no yeardata
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }
                SSAccountPlan iAccountPlan = yearData.getAccountPlan();

                SSAccountPlanDialog.editCurrentDialog( iMainFrame, iAccountPlan, null);
            }
        });

        // Projects
        // *****************************
        iMenuLoader.addActionListener("registermenu.project", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSProjectFrame.showFrame(iMainFrame, 400, 300);
            }
        });

        // Resultatenhet
        // *****************************
        iMenuLoader.addActionListener("registermenu.resultunit", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSResultUnitFrame.showFrame(iMainFrame, 400, 300);
            }
        });

        // Producter
        // *****************************
        iMenuLoader.addActionListener("registermenu.product", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSProductFrame.showFrame(iMainFrame, 640, 480);
            }
        });
        // Customer
        // *****************************
        iMenuLoader.addActionListener("registermenu.customer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCustomerFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Supplier
        // *****************************
        iMenuLoader.addActionListener("registermenu.supplier", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        iMenuLoader.addActionListener("registermenu.vouchertemplates", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //SSVoucherTemplateTableFrame voucherTemplateFrame = new SSVoucherTemplateTableFrame(iMainFrame, 400, 300);
                //voucherTemplateFrame.setVisible(true);

                SSVoucherTemplateFrame.showFrame(iMainFrame);

            }
        });

        // Automatfördelning
        iMenuLoader.addActionListener("registermenu.autodist", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAutoDistFrame.showFrame(iMainFrame, 488, 360);
            }
        });
    }

    /**
     *
     */
    private void loadInvoiceActions() {

        // Inbetalningar
        // *****************************
        iMenuLoader.addActionListener("salemenu.inpayment", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInpaymentFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Utbetalningar
        // *****************************
        iMenuLoader.addActionListener("salemenu.outpayment", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpaymentFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Kreditfakturor
        // *****************************
        iMenuLoader.addActionListener("salemenu.creditinvoice", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoiceFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Periodfaktura
        // *****************************
        iMenuLoader.addActionListener("salemenu.periodicinvoice", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoiceFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Offerter
        // *****************************
        iMenuLoader.addActionListener("salemenu.tender", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSTenderFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Order
        // *****************************
        iMenuLoader.addActionListener("salemenu.order", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOrderFrame.showFrame(iMainFrame, 800, 600);
            }
        });
        // Faktura
        // *****************************
        iMenuLoader.addActionListener("salemenu.invoice", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoiceFrame.showFrame(iMainFrame, 880, 600);
            }
        });
        // Inköpsorder
        // *****************************
        iMenuLoader.addActionListener("salemenu.purchaseorder", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrderFrame.showFrame(iMainFrame, 880, 600);
            }
        });

        // Leverantörsfaktura
        // *****************************
        iMenuLoader.addActionListener("salemenu.supplierinvoice", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoiceFrame.showFrame(iMainFrame, 880, 600);
            }
        });

        // Leverantörskreditfaktura
        // *****************************
        iMenuLoader.addActionListener("salemenu.suppliercreditinvoice", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoiceFrame.showFrame(iMainFrame, 880, 600);
            }
        });

    }

    /**
     *
     */
    private void bgcmenuActions() {
        // Importera från bgmax
        // *****************************
        iMenuLoader.addActionListener("bgcmenu.bgmax", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSFileChooser iFileChooser = new SSFileChooser(  );

                SSFilterBGMAX iFilter = new SSFilterBGMAX();

                iFileChooser.addChoosableFileFilter( iFilter );
                iFileChooser.addChoosableFileFilter( new SSFilterTXT() );
                iFileChooser.addChoosableFileFilter( new SSFilterALL() );
                iFileChooser.setFileFilter(  iFilter );

                if( iFileChooser.showOpenDialog(iMainFrame) != SSFileChooser.APPROVE_OPTION) return;
                BgMaxFile iBgMaxFile;
                try {
                    iBgMaxFile = SSBgMaxImporter.Import(iFileChooser.getSelectedFile());
                } catch (SSImportException ex) {
                    new SSErrorDialog(iMainFrame, "importexceptiondialog", ex.getMessage());
                    return;
                }

                if(iBgMaxFile == null) {
                    return;
                }
                List<SSInpayment> iInpayments = SSBgMaxImporter.getInpayments(iMainFrame, iBgMaxFile);

                if(iInpayments == null) {
                    return;
                }
                StringBuilder iNumbers = new StringBuilder();
                for (SSInpayment iInpayment : iInpayments) {
                    SSDB.getInstance().addInpayment(iInpayment);

                    if(iNumbers.length() > 0) iNumbers.append(", ");

                    iNumbers.append( iInpayment.getNumber() );
                }
                SSInformationDialog.showDialog(iMainFrame,"bgmaximport.success",iNumbers.toString());

            }
        });

        // Importera från lbin fil
        // *****************************
        iMenuLoader.addActionListener("bgcmenu.lbin", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSFileChooser iFileChooser = new SSFileChooser( );

                SSFilterBGMAX iFilter = new SSFilterBGMAX();

                iFileChooser.addChoosableFileFilter( iFilter );
                iFileChooser.addChoosableFileFilter( new SSFilterTXT() );
                iFileChooser.addChoosableFileFilter( new SSFilterALL() );
                iFileChooser.setFileFilter(  iFilter );

                if( iFileChooser.showOpenDialog(iMainFrame) != SSFileChooser.APPROVE_OPTION) return;
                //  List<SSOutpayment> iOutpayments = SSSupplierPaymentImporter.Import(iFileChooser.getSelectedFile());

                List<SSOutpayment> iOutpayments ;
                try {
                    iOutpayments = SSSupplierPaymentImporter.Import(iFileChooser.getSelectedFile());
                } catch (SSImportException ex) {
                    new SSErrorDialog(iMainFrame, "importexceptiondialog", ex.getMessage());
                    return;
                }
                StringBuilder iNumbers = new StringBuilder();
                for (SSOutpayment iOutpayment : iOutpayments) {
                    SSDB.getInstance().addOutpayment(iOutpayment);

                    if(iNumbers.length() > 0) iNumbers.append(", ");

                    iNumbers.append( iOutpayment.getNumber() );
                }
                new SSInformationDialog(iMainFrame, "supplierpaymentimport.importcomplete", iNumbers.toString() );
            }

        });

    }

    /**
     *
     */
    private void addStockActions() {

        // Inleverans
        // *****************************
        iMenuLoader.addActionListener("stockmenu.indelivery", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSIndeliveryFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Utleverans
        // *****************************
        iMenuLoader.addActionListener("stockmenu.outdelivery", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutdeliveryFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Inköpsförslag
        // *****************************
        iMenuLoader.addActionListener("stockmenu.purchasesuggestion", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPurchaseSuggestionDialog.showDialog(iMainFrame);
            }
        });

        // Inventering
        // *****************************
        iMenuLoader.addActionListener("stockmenu.inventory", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInventoryFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Inventeringsunderlag
        // *****************************
        iMenuLoader.addActionListener("stockmenu.inventorybasisreport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInventoryBasisDialog iDialog = new SSInventoryBasisDialog(iMainFrame);

                iDialog.setLocationRelativeTo(iMainFrame);
                if(iDialog.showDialog() != JOptionPane.OK_OPTION) return;
                final Date    iDate         = iDialog.getDate();
                final boolean iDateSelected = iDialog.isDateSelected();
                SSProgressDialog.runProgress(iMainFrame, new Runnable(){
                    public void run() {
                        SSInventoryBasisPrinter iPrinter;
                        if(iDateSelected){
                            iPrinter = new SSInventoryBasisPrinter(iDate);
                        } else {
                            iPrinter = new SSInventoryBasisPrinter();
                        }
                        iPrinter.preview( iMainFrame );
                    }
                });
            }
        });

        // Lagerredovisning
        // *****************************
        iMenuLoader.addActionListener("stockmenu.stockaccountreport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.StockAccount(iMainFrame);
            }
        });

        // Lagervärde
        // *****************************
        iMenuLoader.addActionListener("stockmenu.stockvaluereport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.StockValue(iMainFrame);
            }
        });

    }

    /**
     *
     */
    private void loadBookkeepingActions() {

        // Ingående balans
        // *****************************
        iMenuLoader.addActionListener("bookkeepingmenu.startingamount", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String lockString = "startingamount"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( iMainFrame, "startingamountopen");
                    return;
                }
                SSStartingAmountFrame.showFrame(iMainFrame, 500, 400);
            }
        });

        // Vouchers
        // *****************************
        iMenuLoader.addActionListener("bookkeepingmenu.vouchers", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucherFrame.showFrame(iMainFrame, 800, 600);
            }
        });

        // Budget
        // *****************************
        iMenuLoader.addActionListener("bookkeepingmenu.budget", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String lockString = "budget"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( iMainFrame,  "budgetopen");
                    return;
                }
                SSBudgetFrame.showFrame(iMainFrame, 800, 600);
            }
        });

    }

    /**
     *
     */
    private void loadPrintBookkeepingActions() {

        // Vouchers
        // *****************************
        iMenuLoader.addActionListener("reportmenu.vouchers", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.buildVoucherReport(iMainFrame, bundle, yearData);
            }
        });

        // Starting ammounts
        // *****************************
        iMenuLoader.addActionListener("reportmenu.startingamounts", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();
                // Check so the yeardata isn't null
                if (iAccountingYear == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSProgressDialog.runProgress(iMainFrame, new Runnable(){
                    public void run() {
                        SSStartingAmountPrinter iPrinter = new SSStartingAmountPrinter(iAccountingYear);
                        iPrinter.preview( iMainFrame );
                    }
                });
            }
        });

        // Account plan
        // *****************************
        iMenuLoader.addActionListener("reportmenu.accountplan", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }
                SSProgressDialog.runProgress(iMainFrame, new Runnable(){
                    public void run() {
                        SSAccountPlanPrinter iPrinter = new SSAccountPlanPrinter();

                        iPrinter.preview( iMainFrame );
                    }
                });

            }
        });

        // Mainbook
        // *****************************
        iMenuLoader.addActionListener("reportmenu.mainbook", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }
                SSReportFactory.MainbookReport(iMainFrame, yearData);
            }
        });

        // Result report
        // *****************************
        iMenuLoader.addActionListener("reportmenu.resultreport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }
                SSNewAccountingYear previousYearData = SSDB.getInstance().getPreviousYear();

                SSReportFactory.buildResultReport(iMainFrame, bundle, yearData, previousYearData);
            }
        });

        // Project result
        // *****************************
        iMenuLoader.addActionListener("reportmenu.projectresult", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (iAccountingYear == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }
                SSReportFactory.ProjectResultReport(iMainFrame, bundle, iAccountingYear);
            }
        });

        // Resultunit result
        // *****************************
        iMenuLoader.addActionListener("reportmenu.resultunitresult", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (iAccountingYear == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.ResultUnitResultReport(iMainFrame, bundle, iAccountingYear);
            }
        });

        // Budget report
        // *****************************
        iMenuLoader.addActionListener("reportmenu.budget", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.buildBudgetReport(iMainFrame, bundle, yearData);
            }
        });

        // Balance report
        // *****************************
        iMenuLoader.addActionListener("reportmenu.balancereport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.buildBalanceReport(iMainFrame, bundle, yearData);
            }
        });

        // Momsrapport
        // *****************************
        /*iMenuLoader.addActionListener("reportmenu.vatreport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( iMainFrame, "voucheriscreated");
                    return;
                }

                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSPostLock.removeLock(lockString);
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.buildVATReport(iMainFrame, bundle, yearData);
            }
        });*/

        // Momsrapport 2007
        // *****************************
        /* iMenuLoader.addActionListener("reportmenu.vatreport2007", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( iMainFrame, "voucheriscreated");
                    return;
                }
                SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (iAccountingYear == null) {
                    SSPostLock.removeLock(lockString);
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.VATReport2007(iMainFrame, bundle, iAccountingYear);
            }
        }); */

        // Momsrapport 2015
        // *****************************
        iMenuLoader.addActionListener("reportmenu.vatreport2015", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( iMainFrame, "voucheriscreated");
                    return;
                }
                SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (iAccountingYear == null) {
                    SSPostLock.removeLock(lockString);
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.VATReport2015(iMainFrame, bundle, iAccountingYear);
            }
        });

        // Räkenskapsschema
        // *****************************
        iMenuLoader.addActionListener("reportmenu.accountdiagram", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.buildAccountDiagramReport(iMainFrame, bundle, yearData);
            }
        });

        // Förenklat årsbokslut
        // *****************************
        iMenuLoader.addActionListener("reportmenu.simplestatement", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }

                SSReportFactory.SimplestatementReport(iMainFrame);
            }
        });

        iMenuLoader.addActionListener("reportmenu.ownreports", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();
                // Check so the yeardata isn't null
                if (yearData == null) {
                    SSNewAccountingYear.openWarningDialogNoYearData(iMainFrame);
                    return;
                }
                SSOwnReportFrame.showFrame(iMainFrame,800,600);
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////

        // Orderlista
        // *****************************
        iMenuLoader.addActionListener("reportmenu.orderlist", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.OrderListReport(iMainFrame);
            }
        });

        // Kundreskontra
        // *****************************
        iMenuLoader.addActionListener("reportmenu.accountsrecievable", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.AccountsRecievableReport(iMainFrame);
            }
        });

        // Kundfodran
        // *****************************
        iMenuLoader.addActionListener("reportmenu.customerclaim", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.CustomerClaimReport(iMainFrame);
            }
        });

        // EU-Kvartalsredovisning
        // *****************************
        iMenuLoader.addActionListener("reportmenu.quarterreport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.QuarterReport(iMainFrame);
            }
        });

        // Faktura journal
        // *****************************
        iMenuLoader.addActionListener("journalmenu.invoicejornal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.InvoiceJournal(iMainFrame);
            }
        });

        // Kreditfaktura journal
        // *****************************
        iMenuLoader.addActionListener("journalmenu.creditinvoicejornal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.CreditInvoiceJournal(iMainFrame);
            }
        });

        // Inbetalningsjournal
        // *****************************
        iMenuLoader.addActionListener("journalmenu.inpaymentjournal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.InpaymentJournal(iMainFrame);
            }
        });

        // leverantörsfaktura journal
        // *****************************
        iMenuLoader.addActionListener("journalmenu.supplierinvoicejornal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.SupplierInvoiceJournal(iMainFrame);
            }
        });

        // Leverantörskreditfaktura journal
        // *****************************
        iMenuLoader.addActionListener("journalmenu.suppliercreditinvoicejornal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.SupplierCreditInvoiceJournal(iMainFrame);
            }
        });

        // Utbetalningsjournal
        // *****************************
        iMenuLoader.addActionListener("journalmenu.outpaymentjournal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.OutpaymentJournal(iMainFrame);
            }
        });

        // Leverantörsreskontra
        // *****************************
        iMenuLoader.addActionListener("reportmenu.accountspayable", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.AccountsPayableReport(iMainFrame);
            }
        });

        // Leverantörsskuld
        // *****************************
        iMenuLoader.addActionListener("reportmenu.supplierdept", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.SupplierDebtReport(iMainFrame);
            }
        });

        // Försäljningsrapport
        // *****************************
        iMenuLoader.addActionListener("reportmenu.salereport", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.SaleReport(iMainFrame);
            }
        });

        // Försäljningsvärden
        // *****************************
        iMenuLoader.addActionListener("reportmenu.salevalues",new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();

                SSReportFactory.Salevalues(iMainFrame, bundle, yearData);
            }
        });

        // Inköpsvärden
        // *****************************
        iMenuLoader.addActionListener("reportmenu.purchasevalues",new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SSNewAccountingYear yearData = SSDB.getInstance().getCurrentYear();
                SSReportFactory.Purchasevalues(iMainFrame, bundle, yearData);
            }
        });

    }

    /**
     *
     */
    private void loadHelpActions() {
        // Hjälp
        // *****************************
        iMenuLoader.addActionListener("helpmenu.help",new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSHelpFrame.showFrame(iMainFrame, 980, 720);

            }

        });

        // Online support
        // *****************************
        iMenuLoader.addActionListener("helpmenu.support", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url =  SSBundle.getBundle().getString("application.url.support") ;
                BrowserLaunch.openURL( url );
            }
        });

        // Online updates
        // *****************************
        iMenuLoader.addActionListener("helpmenu.updates", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url =  SSBundle.getBundle().getString("application.url.updates") ;
                BrowserLaunch.openURL( url );
            }
        });

        // Ta bort postlås
        // ***********************
        iMenuLoader.addActionListener("helpmenu.clearlocks", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSConfirmDialog iDialog = new SSConfirmDialog("helpmenu.clearlocks.warning");
                if(iDialog.openDialog(iMainFrame)==JOptionPane.OK_OPTION)
                    SSPostLock.clearLocks();
            }
        });

        iMenuLoader.addActionListener("helpmenu.compress", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSConfirmDialog iDialog = new SSConfirmDialog("helpmenu.compress.warning");
                if(iDialog.openDialog(iMainFrame)==JOptionPane.OK_OPTION){

                    SSDB.getInstance().shutdownCompact();
                    System.exit(0);
                }

            }
        });

        iMenuLoader.addActionListener("helpmenu.cleartransactions", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSClearTransactionsDialog iDialog = new SSClearTransactionsDialog(iMainFrame);
                iDialog.setLocationRelativeTo(iMainFrame);

                if(iDialog.showDialog() != JOptionPane.OK_OPTION) return;

                final Date iDate = SSDateMath.ceil(iDialog.getDate());
                SSConfirmDialog iConfirmDialog;
                if (!SSDB.getInstance().getLocking() /*&& !SSVersion.app_title.contains("JFS Fakturering")*/) {
                    iConfirmDialog = new SSConfirmDialog("helpmenu.cleartransactions.warning",new SimpleDateFormat("yyyy-MM-dd").format(iDate));
                }
                // // Fakturering
                // else if(!SSDB.getInstance().getLocking()){
                //     iConfirmDialog = new SSConfirmDialog("helpmenu.cleartransactions.wfl",new SimpleDateFormat("yyyy-MM-dd").format(iDate));
                // }
                // else if(SSVersion.app_title.contains("JFS Fakturering")){
                //     iConfirmDialog = new SSConfirmDialog("helpmenu.cleartransactions.wfs",new SimpleDateFormat("yyyy-MM-dd").format(iDate));
                // }
                else{
                    iConfirmDialog = new SSConfirmDialog("helpmenu.cleartransactions.was",new SimpleDateFormat("yyyy-MM-dd").format(iDate));
                }
                if(iConfirmDialog.openDialog(iMainFrame)!=JOptionPane.OK_OPTION) return;

                if(!SSDB.getInstance().getLocking()){
                    if (!createBackupDialog())
                        return;
                }
                else{
                    SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();
                    SSCompanyLock.removeLock(iCurrentCompany);
                    if (SSCompanyLock.isLocked(iCurrentCompany)) {
                        SSCompanyLock.applyLock(iCurrentCompany);
                        new SSErrorDialog( iMainFrame, "companyframe.companyopen");
                        return;
                    }
                    SSCompanyLock.applyLock(iCurrentCompany);
                }

                SSFrameManager.getInstance().close();
                SSDB.getInstance().dropTriggers();

                SSInitDialog.runProgress(SSMainFrame.getInstance(),"Rensar transaktioner...", new Runnable(){
                    public void run() {
                        SSStock iStock = new SSStock(true);
                        Map<String, Integer> iStockStatusStart = new HashMap<String, Integer>();
                        for(SSProduct iProduct : SSDB.getInstance().getProducts()){
                            if(iProduct.isStockProduct())
                                iStockStatusStart.put(iProduct.getNumber(), iStock.getQuantity(iProduct));
                        }

                        Map<Integer, BigDecimal> iSaldoMap = SSInvoiceMath.getSaldos(iDate);

                        for(SSInpayment iInpayment : SSDB.getInstance().getInpayments()){
                            if(iInpayment.getDate().before(iDate)){
                                List<SSInpaymentRow> iSavedRows = new LinkedList<SSInpaymentRow>();
                                for(SSInpaymentRow iRow : iInpayment.getRows()){
                                    if(iRow.getInvoiceNr() != null && iSaldoMap.containsKey(iRow.getInvoiceNr())){
                                        BigDecimal iSaldo = iSaldoMap.get(iRow.getInvoiceNr());
                                        if(iSaldo.signum() != 0) iSavedRows.add(iRow);
                                    }
                                }
                                iInpayment.setRows(iSavedRows);

                                if(iInpayment.getRows().isEmpty())
                                    SSDB.getInstance().deleteInpayment(iInpayment);
                                else
                                    SSDB.getInstance().updateInpayment(iInpayment);
                            }
                        }

                        for(SSTender iTender : SSDB.getInstance().getTenders()){
                            if(iTender.getDate().before(iDate)) SSDB.getInstance().deleteTender(iTender);
                        }

                        for(SSOrder iOrder : SSDB.getInstance().getOrders()){
                            if(iOrder.getDate().before(iDate)) SSDB.getInstance().deleteOrder(iOrder);
                        }

                        for(SSInvoice iInvoice : SSDB.getInstance().getInvoices()){
                            if(iInvoice.getDate().before(iDate) && iSaldoMap.containsKey(iInvoice.getNumber())){
                                BigDecimal iSaldo = iSaldoMap.get(iInvoice.getNumber());
                                if(iSaldo.signum() == 0) SSDB.getInstance().deleteInvoice(iInvoice);
                            }
                            else if(iInvoice.getDate().before(iDate)){
                                SSDB.getInstance().deleteInvoice(iInvoice);
                            }
                        }

                        for(SSCreditInvoice iCreditInvoice : SSDB.getInstance().getCreditInvoices()){
                            if(iCreditInvoice.getDate().before(iDate) && iSaldoMap.containsKey(iCreditInvoice.getCreditingNr())){
                                BigDecimal iSaldo = iSaldoMap.get(iCreditInvoice.getCreditingNr());
                                if(iSaldo.signum() == 0) SSDB.getInstance().deleteCreditInvoice(iCreditInvoice);
                            }
                            else if(iCreditInvoice.getDate().before(iDate)){
                                SSDB.getInstance().deleteCreditInvoice(iCreditInvoice);
                            }
                        }

                        for(SSPeriodicInvoice iPeriodicInvoice : SSDB.getInstance().getPeriodicInvoices()){
                            if(iPeriodicInvoice.getDate().before(iDate) && iPeriodicInvoice.getNextDate() == null)
                                SSDB.getInstance().deletePeriodicInvoice(iPeriodicInvoice);
                        }

                        Map<Integer, BigDecimal> iPurchaseSaldoMap = SSSupplierInvoiceMath.getSaldos(iDate);

                        for(SSOutpayment iOutpayment : SSDB.getInstance().getOutpayments()){
                            if(iOutpayment.getDate().before(iDate)){
                                List<SSOutpaymentRow> iSavedRows = new LinkedList<SSOutpaymentRow>();
                                for(SSOutpaymentRow iRow : iOutpayment.getRows()){
                                    if(iRow.getInvoiceNr() != null && iPurchaseSaldoMap.containsKey(iRow.getInvoiceNr())){
                                        BigDecimal iSaldo = iPurchaseSaldoMap.get(iRow.getInvoiceNr());
                                        if(iSaldo.signum() != 0) iSavedRows.add(iRow);
                                    }
                                }
                                iOutpayment.setRows(iSavedRows);

                                if(iOutpayment.getRows().isEmpty())
                                    SSDB.getInstance().deleteOutpayment(iOutpayment);
                                else
                                    SSDB.getInstance().updateOutpayment(iOutpayment);
                            }
                        }

                        for(SSPurchaseOrder iPurchaseOrder : SSDB.getInstance().getPurchaseOrders()){
                            if(iPurchaseOrder.getDate().before(iDate)) SSDB.getInstance().deletePurchaseOrder(iPurchaseOrder);
                        }

                        for(SSSupplierInvoice iSupplierInvoice : SSDB.getInstance().getSupplierInvoices()){
                            if(iSupplierInvoice.getDate().before(iDate) && iPurchaseSaldoMap.containsKey(iSupplierInvoice.getNumber())){
                                BigDecimal iSaldo = iPurchaseSaldoMap.get(iSupplierInvoice.getNumber());
                                if(iSaldo.signum() == 0) SSDB.getInstance().deleteSupplierInvoice(iSupplierInvoice);
                            }
                            else if(iSupplierInvoice.getDate().before(iDate)){
                                SSDB.getInstance().deleteSupplierInvoice(iSupplierInvoice);
                            }
                        }

                        for(SSSupplierCreditInvoice iSupplierCreditInvoice : SSDB.getInstance().getSupplierCreditInvoices()){
                            if(iSupplierCreditInvoice.getDate().before(iDate) && iSaldoMap.containsKey(iSupplierCreditInvoice.getCreditingNr())){
                                BigDecimal iSaldo = iSaldoMap.get(iSupplierCreditInvoice.getCreditingNr());
                                if(iSaldo.signum() == 0) SSDB.getInstance().deleteSupplierCreditInvoice(iSupplierCreditInvoice);
                            }
                            else if(iSupplierCreditInvoice.getDate().before(iDate)){
                                SSDB.getInstance().deleteSupplierCreditInvoice(iSupplierCreditInvoice);
                            }
                        }

                        for(SSIndelivery iIndelivery : SSDB.getInstance().getIndeliveries()){
                            if(iIndelivery.getDate().before(iDate)){
                                SSDB.getInstance().deleteIndelivery(iIndelivery);
                            }
                        }

                        for(SSOutdelivery iOutdelivery : SSDB.getInstance().getOutdeliveries()){
                            if(iOutdelivery.getDate().before(iDate)){
                                SSDB.getInstance().deleteOutdelivery(iOutdelivery);
                            }
                        }

                        for(SSInventory iInventory : SSDB.getInstance().getInventories()){
                            if(iInventory.getDate().before(iDate)){
                                SSDB.getInstance().deleteInventory(iInventory);
                            }
                        }

                        SSDB.getInstance().clearLists();
                        SSDB.getInstance().init(false);

                        iStock = new SSStock(true);
                        SSInventory iInventoryDone = new SSInventory();
                        for(SSProduct iProduct : SSDB.getInstance().getProducts()){
                            SSInventoryRow iRow = new SSInventoryRow();
                            iRow.setProduct(iProduct);
                            iRow.setStockQuantity(iStock.getQuantity(iProduct));
                            if(iStockStatusStart.containsKey(iProduct.getNumber())) {
                                iRow.setInventoryQuantity(iStockStatusStart.get(iProduct.getNumber()));
                                iInventoryDone.getRows().add(iRow);
                            }
                        }
                        iInventoryDone.setText("Lagerjustering vid transaktionsrensning");
                        SSDB.getInstance().addInventory(iInventoryDone);

                        SSDB.getInstance().shutdownCompact();
                        System.exit(0);
                    }
                });
            }
        });

        // About
        // *****************************
        iMenuLoader.addActionListener("helpmenu.about", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAboutDialog.showDialog(iMainFrame);
            }
        });

    }
    private List<JMenuItem> iWindowItems;

    /**
     * Window actions
     */
    private void loadWindowActions(){
        iWindowItems = new LinkedList<JMenuItem>();

        SSFrameManager.getInstance().addFrameListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenu iMenu = iMenuLoader.getMenu("Window");

                if(iMenu == null) return;

                // Remove all old items
                for(JMenuItem iMenuItem : iWindowItems){
                    iMenu.remove(iMenuItem);
                }
                iWindowItems.clear();

                int iCounter = 0;
                for(final SSInternalFrame iFrame : SSFrameManager.getInstance().getFrames()){
                    JMenuItem iMenuItem = new JMenuItem();
                    iMenuItem.setText(Integer.toString(iCounter) + ": " +  iFrame.getTitle() );
                    iMenuItem.setMnemonic( (char)('0' + iCounter));

                    iMenuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            iFrame.deIconize();
                        }
                    });
                    iMenu.add(iMenuItem);

                    iWindowItems.add(iMenuItem);

                    iCounter++;

                }

            }
        });

        // Cascade...
        // *****************************
        iMenuLoader.addActionListener("windowmenu.cascade", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SSFrameManager.getInstance().cascade();
                }
            });

        // Close...
        // *****************************
        iMenuLoader.addActionListener("windowmenu.close", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSFrameManager.getInstance().close();
            }
        });

    }

    /**
     * Set up and create a BackupDialog
     * @return true on successful backup
     */
    private boolean createBackupDialog() {
        boolean result = false;
        if (SSDB.getInstance().getLocking()) {
            new SSInformationDialog(iMainFrame, "backupframe.runningonserver");
        }
        else {
            JFileChooser fc             = SSBackupFileChooser.getInstance();
            SSBackupDatabase db         = SSBackupDatabase.getInstance();
            SSBackupDialog backupDialog = new SSBackupDialog(iMainFrame, fc, db);
            SSBackupFrame.hideFrame();
            result = backupDialog.show();
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.SSMainMenu");
        sb.append("{iMainFrame=").append(iMainFrame);
        sb.append(", iMenuLoader=").append(iMenuLoader);
        sb.append(", iWindowItems=").append(iWindowItems);
        sb.append('}');
        return sb.toString();
    }
}
