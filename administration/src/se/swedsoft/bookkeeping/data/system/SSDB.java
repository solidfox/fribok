package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.SSTriggerHandler;
import se.swedsoft.bookkeeping.app.SSPath;
import se.swedsoft.bookkeeping.calc.math.*;
import se.swedsoft.bookkeeping.calc.util.SSAutoIncrement;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.backup.util.SSBackupZip;
import se.swedsoft.bookkeeping.data.backup.util.SSBackupZip.ArchiveFile;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.autodist.SSAutoDistFrame;
import se.swedsoft.bookkeeping.gui.creditinvoice.SSCreditInvoiceFrame;
import se.swedsoft.bookkeeping.gui.customer.SSCustomerFrame;
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
import se.swedsoft.bookkeeping.gui.resultunit.SSResultUnitFrame;
import se.swedsoft.bookkeeping.gui.supplier.SSSupplierFrame;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.SSSupplierCreditInvoiceFrame;
import se.swedsoft.bookkeeping.gui.supplierinvoice.SSSupplierInvoiceFrame;
import se.swedsoft.bookkeeping.gui.tender.SSTenderFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInitDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.voucher.SSVoucherFrame;
import se.swedsoft.bookkeeping.gui.vouchertemplate.SSVoucherTemplateFrame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.server.UID;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 
 * $Id$
 */
public class SSDB {

    // The instance of the database
    private static SSDB cInstance;

    private SSNewCompany iCurrentCompany;

    private SSNewAccountingYear iCurrentYear;

    List<SSProduct> iProducts;
    List<SSCustomer> iCustomers;
    List<SSSupplier> iSuppliers;
    List<SSAutoDist> iAutoDists;

    List<SSInpayment> iInpayments;
    List<SSTender> iTenders;
    List<SSOrder> iOrders;
    List<SSInvoice> iInvoices;
    List<SSCreditInvoice> iCreditInvoices;
    List<SSPeriodicInvoice> iPeriodicInvoices;

    List<SSOutpayment> iOutpayments;
    List<SSPurchaseOrder> iPurchaseOrders;
    List<SSSupplierInvoice> iSupplierInvoices;
    List<SSSupplierCreditInvoice> iSupplierCreditInvoices;

    List<SSInventory> iInventories;
    List<SSIndelivery> iIndeliveries;
    List<SSOutdelivery> iOutdeliveries;

    List<SSVoucher> iVouchers;
    List<SSOwnReport> iOwnReports;
    /**
     * Returns the instance of the database
     *
     * @return the database
     */
    public static SSDB getInstance(){
        if(cInstance == null){
            cInstance = new SSDB();
        }
        return cInstance;
    }

    public static final Object iSyncObject = new Object();

    Connection iConnection;

    private Socket iSocket;
    private BufferedReader iIn;
    private PrintWriter iOut;

    private boolean iLocking;

    // Listeners
    private Map<String, List<PropertyChangeListener>> iListenerMap;

    private SSDB(){
        iListenerMap = new HashMap<String, List<PropertyChangeListener>>();
    }

    public boolean getLocking() {
        return iLocking;
    }

    /**
     *
     * @param pConnection
     *
     * @throws SQLException
     */
    public void startupLocal(Connection pConnection) throws SQLException {
        iConnection = pConnection;
        iConnection.setAutoCommit(false);
        iLocking = false;

        createNewTables();
        //dropTriggers();
        createLocalTriggers();
        //Läs in företaget och året som senast var öppet.
        Integer iLastCompany = SSDBConfig.getCompanyId();
        Integer iLastYear = SSDBConfig.getYearId();

        ResultSet iResultSet;
        PreparedStatement iStatement;
        if(iLastCompany != null){
            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_company WHERE id=?");
            iStatement.setObject(1, iLastCompany);
            iResultSet = iStatement.executeQuery();
            if(iResultSet.next()) {
                SSNewCompany iCompany = (SSNewCompany) iResultSet.getObject("company");
                setCurrentCompany(iCompany);
            }
            iResultSet.close();
            iStatement.close();
        }

        if(iLastYear != null && iCurrentCompany != null){
            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear WHERE id=?");
            iStatement.setObject(1, iLastYear);
            iResultSet = iStatement.executeQuery();

            if(iResultSet.next()) {
                SSNewAccountingYear iYear = (SSNewAccountingYear) iResultSet.getObject("accountingyear");
                setCurrentYear(iYear);
            }
            iResultSet.close();
            iStatement.close();
        }
    }

    public void startupRemote(Connection pConnection, String iServerAddress) throws SQLException {
        iConnection = pConnection;
        iConnection.setAutoCommit(false);
        createNewTables();
        //dropTriggers();
        createServerTriggers();
        PreparedStatement iStatement;

        String iKey = SSDBConfig.getClientkey();
        try {
            if(iKey != null && iKey.length() != 0){
                iStatement = iConnection.prepareStatement("INSERT INTO tbl_license VALUES(?)");
                iStatement.setObject(1,iKey);
                iStatement.executeUpdate();
                iStatement.close();
            }
        } catch (SQLException e) {
            new SSErrorDialog(SSMainFrame.getInstance(),"database.duplicateclient");
            System.exit(0);
        }

        try {
            openSocket(iServerAddress);
            iLocking = true;
            SSTriggerHandler iTriggerHandler = new SSTriggerHandler();
            iTriggerHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
            //Kunde inte ansluta.
            //Borde inte kunna komma hit då det borde kommit SQLEXCEPTION före!!
        }
        iLocking = true;
        //Läs in företaget och året som senast var öppet.
        Integer iLastCompany = SSDBConfig.getCompanyId();
        Integer iLastYear = SSDBConfig.getYearId();

        ResultSet iResultSet;
        if(iLastCompany != null){
            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_company WHERE id=?");
            iStatement.setObject(1, iLastCompany);
            iResultSet = iStatement.executeQuery();

            if(iResultSet.next()) {
                SSNewCompany iCompany = (SSNewCompany) iResultSet.getObject("company");
                setCurrentCompany(iCompany);
                SSCompanyLock.applyLock(iCompany);
            }
            iResultSet.close();
            iStatement.close();
        }

        if(iLastYear != null && iCurrentCompany != null){
            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear WHERE id=?");
            iStatement.setObject(1, iLastYear);
            iResultSet = iStatement.executeQuery();

            if(iResultSet.next()) {
                SSNewAccountingYear iYear = (SSNewAccountingYear) iResultSet.getObject("accountingyear");
                setCurrentYear(iYear);
                SSYearLock.applyLock(iYear);
            }
            iResultSet.close();
            iStatement.close();
        }
    }

    public void init(boolean iShowDialog) {
        if(iCurrentCompany == null) return;

        if(iShowDialog){
            SSInitDialog.runProgress(SSMainFrame.getInstance(),"Läser in data", new Runnable(){
                public void run() {
                    getProducts();
                    getCustomers();
                    getSuppliers();
                    getAutoDists();

                    getInpayments();
                    getTenders();
                    getOrders();
                    getInvoices();
                    getCreditInvoices();
                    getPeriodicInvoices();

                    getOutpayments();
                    getPurchaseOrders();
                    getSupplierInvoices();
                    getSupplierCreditInvoices();

                    getInventories();
                    getIndeliveries();
                    getOutdeliveries();

                    getOwnReports();

                    SSInvoiceMath.iSaldoMap = null;
                    SSInvoiceMath.calculateSaldos();
                    SSCustomerMath.iInvoicesForCustomers = null;
                    SSCustomerMath.getInvoicesForCustomers();
                    SSSupplierInvoiceMath.iSaldoMap = null;
                    SSSupplierInvoiceMath.calculateSaldos();
                    SSSupplierMath.iInvoicesForSuppliers = null;
                    SSSupplierMath.getInvoicesForSuppliers();
                    //SSOrderMath.setInvoiceForOrders();
                    initYear(false);
                }
            });
        }
        else{
            getProducts();
            getCustomers();
            getSuppliers();
            getAutoDists();

            getInpayments();
            getTenders();
            getOrders();
            getInvoices();
            getCreditInvoices();
            getPeriodicInvoices();

            getOutpayments();
            getPurchaseOrders();
            getSupplierInvoices();
            getSupplierCreditInvoices();

            getInventories();
            getIndeliveries();
            getOutdeliveries();

            getOwnReports();

            SSInvoiceMath.iSaldoMap = null;
            SSInvoiceMath.calculateSaldos();
            SSCustomerMath.iInvoicesForCustomers = null;
            SSCustomerMath.getInvoicesForCustomers();
            SSSupplierInvoiceMath.iSaldoMap = null;
            SSSupplierInvoiceMath.calculateSaldos();
            SSSupplierMath.iInvoicesForSuppliers = null;
            SSSupplierMath.getInvoicesForSuppliers();
            //SSOrderMath.setInvoiceForOrders();
            initYear(false);
        }

    }

    public void initYear(boolean iShowLoadingDialog) {
        if(iCurrentYear == null) return;

        iVouchers = null;
        getCurrentYear();

        if (iShowLoadingDialog) {
            SSInitDialog.runProgress(SSMainFrame.getInstance(), "Läser in data", new Runnable() {
                public void run() {
                    getVouchers();
                }
            });
        } else {
            getVouchers();
        }


    }
    public void shutdown() {
        if (!iLocking) {
            try {
                if(!iConnection.isClosed()){
                    Statement iStatement = iConnection.createStatement();
                    iStatement.executeQuery("SHUTDOWN");
                    iStatement.close();
                    iConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            SSCompanyLock.removeLock(iCurrentCompany);
            SSYearLock.removeLock(iCurrentYear);
        }
    }

    public void shutdownCompact() {
        if (!iLocking) {
            try {
                Statement iStatement = iConnection.createStatement();
                iStatement.executeQuery("SHUTDOWN COMPACT");
                iStatement.close();
                iConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            SSCompanyLock.removeLock(iCurrentCompany);
            SSYearLock.removeLock(iCurrentYear);
        }
    }

    public void openSocket(String iIpAddress) throws IOException {
        Socket iOldSocket = iSocket;
        PrintWriter iOldWriter = iOut;
        BufferedReader iOldReader  = iIn;
        iSocket = new Socket(iIpAddress, 2222);
        iOut = new PrintWriter(new OutputStreamWriter(iSocket.getOutputStream()));
        iIn = new BufferedReader(new InputStreamReader(iSocket.getInputStream()));

        if(iOldSocket != null)
        {
            iOldWriter.println("disconnect");
            iOldWriter.flush();
            iOldWriter.close();
            iOldReader.close();
            iOldSocket.close();
        }
    }

    public void setLocking(boolean iLock) {
        iLocking = iLock;
    }

    public void loadSelectedDatabase(String iAddress) {
        if (!iLocking && iConnection != null) {
            try {
                Statement iStatement = iConnection.createStatement();
                iStatement.executeQuery("SHUTDOWN");
                iStatement.close();
                iConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try { iConnection.rollback(); } catch (SQLException ignored) {}
                SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
            }
        }

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        try {
            iConnection = DriverManager.getConnection("jdbc:hsqldb:hsql://" + iAddress + "/JFSDB", "sa", "");
            iLocking = true;
            iConnection.setAutoCommit(false);

            String iKey = SSDBConfig.getClientkey();
            try {
                if(iKey != null && iKey.length() != 0){
                    PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_license VALUES(?)");
                    iStatement.setObject(1,iKey);
                    iStatement.executeUpdate();
                    iConnection.commit();
                    iStatement.close();
                }
            } catch (SQLException e) {
                new SSErrorDialog(SSMainFrame.getInstance(),"database.duplicateclient");
                System.exit(0);
            }


            createNewTables();
            dropTriggers();
            createServerTriggers();
            SSTriggerHandler iTriggerHandler = new SSTriggerHandler();
            iTriggerHandler.start();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void loadLocalDatabase() {
        try {
            if (iConnection != null) {
                iConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        try {
            iConnection = DriverManager.getConnection("jdbc:hsqldb:file:db" + File.separator + "JFSDB", "sa", "");
            iConnection.setAutoCommit(false);
            iLocking = false;
            createNewTables();
            dropTriggers();
            createLocalTriggers();
            if (iOut != null) {
                iOut.println("disconnect");
                iOut.flush();
                iOut.close();
            }
            if (iIn != null)
                iIn.close();
            if (iSocket != null)
                iSocket.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void removeClient() {
        if(!iLocking || iConnection == null) return;

        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_license WHERE licensekey=?");
            iStatement.setObject(1, SSDBConfig.getClientkey());
            iStatement.executeUpdate();
            iStatement.close();
            iConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void restart() {

    }

    public void delete()  {
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SHUTDOWN");
            iStatement.executeUpdate();
            iStatement.close();
            iConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        File iPropFile = new File("db"+File.separator+"JFSDB.properties");
        File iScriptFile = new File("db"+File.separator+"JFSDB.script");
        File iDataFile = new File("db"+File.separator+"JFSDB.data");
        File iBackupFile = new File("db"+File.separator+"JFSDB.backup");
        File iLogFile = new File("db"+File.separator+"JFSDB.log");
        if (iPropFile.exists())
            iPropFile.delete();
        if (iScriptFile.exists())
            iScriptFile.delete();
        if (iDataFile.exists())
            iDataFile.delete();
        if (iBackupFile.exists())
            iBackupFile.delete();
        if (iLogFile.exists())
            iLogFile.delete();
    }

    public void clear()  {

    }

    public BufferedReader getReader() {
        return iIn;
    }

    public PrintWriter getWriter() {
        return iOut;
    }

    public Socket getSocket() {
        return iSocket;
    }

    public boolean LockDatabase() {
        if (!iLocking) {
            return true;
        }
        if (iOut == null || iIn == null || iSocket == null) {
            return false;
        }
        try {
            iOut.println("lockdatabase");
            iOut.flush();

            String iReply = iIn.readLine();
            if (iReply.equals("goahead")) {
                return true;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     *
     *
     */
    public void UnlockDatabase() {
        if (!iLocking) {
            return;
        }
        if (iOut == null || iIn == null || iSocket == null) {
            return;
        }
        iOut.println("unlockdatabase");
        iOut.flush();
    }

    public void clearLists(){
        iProducts = null;
        iCustomers = null;
        iSuppliers = null;
        iAutoDists = null;
        iInpayments = null;
        iTenders = null;
        iOrders = null;
        iInvoices = null;
        iCreditInvoices = null;
        iPeriodicInvoices = null;
        iOutpayments = null;
        iPurchaseOrders = null;
        iSupplierInvoices = null;
        iSupplierCreditInvoices = null;
        iInventories = null;
        iIndeliveries = null;
        iOutdeliveries = null;
        iOwnReports = null;
    }
    
    public void setCurrentCompany(SSNewCompany iCompany){
        iCurrentCompany = getCompany(iCompany);
        iProducts = null;
        iCustomers = null;
        iSuppliers = null;
        iAutoDists = null;
        iInpayments = null;
        iTenders = null;
        iOrders = null;
        iInvoices = null;
        iCreditInvoices = null;
        iPeriodicInvoices = null;
        iOutpayments = null;
        iPurchaseOrders = null;
        iSupplierInvoices = null;
        iSupplierCreditInvoices = null;
        iInventories = null;
        iIndeliveries = null;
        iOutdeliveries = null;
        iOwnReports = null;
        notifyListeners("COMPANY", iCurrentCompany, null);
    }

    public SSNewCompany getCurrentCompany(){
        iCurrentCompany = getCompany(iCurrentCompany);
        return iCurrentCompany;
    }


    public void setCurrentYear(SSNewAccountingYear iYear){
        iCurrentYear = iYear;
        iVouchers = null;
        notifyListeners("YEAR", iCurrentYear, null);
    }

    public SSNewAccountingYear getCurrentYear() {
        return getAccountingYear(iCurrentYear);
    }

    public List<SSNewCompany> getCompanies() {
        List<SSNewCompany> iCompanies = null;
        try {
            iCompanies = new LinkedList<SSNewCompany>();

            if(iConnection == null || iConnection.isClosed()) return iCompanies;

            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_company");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iCompanies.add((SSNewCompany) iResultSet.getObject("company"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iCompanies;
    }

    public SSNewCompany getCompany(SSNewCompany pCompany) {
        try {
            if(pCompany == null || iConnection.isClosed()) return null;

            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_company WHERE id=?");
            iStatement.setObject(1,pCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSNewCompany iCompany = (SSNewCompany) iResultSet.getObject("company");
                iResultSet.close();
                iStatement.close();
                return iCompany;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addCompany(SSNewCompany iCompany) {
        if(iCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_company VALUES(NULL,?)");
            iStatement.setObject(1, iCompany);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_company");
            ResultSet iResultSet = iStatement.executeQuery();
            Integer iId = -1;
            while(iResultSet.next()){
                if(iResultSet.isLast())
                    iId = iResultSet.getInt("id");
            }
            iResultSet.close();
            iStatement.close();
            iCompany.setId(iId);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iStatement = iConnection.prepareStatement("UPDATE tbl_company SET company=? WHERE id=?");
            iStatement.setObject(1, iCompany);
            iStatement.setObject(2, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateCompany(SSNewCompany iCompany) {
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_company SET company=? WHERE id=?");
            iStatement.setObject(1, iCompany);
            iStatement.setObject(2, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            notifyListeners("COMPANY",iCompany, null);

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteCompany(SSNewCompany iCompany){
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_project WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_resultunit WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_product WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_customer WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_supplier WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_vouchertemplate WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_autodist WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_inpayment WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_tender WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_order WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_invoice WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_creditinvoice WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_periodicinvoice WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_outpayment WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_purchaseorder WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_supplierinvoice WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_suppliercreditinvoice WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_inventory WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_indelivery WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_outdelivery WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement(  "DELETE FROM tbl_ownreport WHERE companyid=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            for (SSNewAccountingYear iYear : getYearsForCompany(iCompany)) {
                deleteAccountingYear(iYear);
            }

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_company WHERE id=?");
            iStatement.setObject(1, iCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public List<SSNewAccountingYear> getYears() {
        List<SSNewAccountingYear> iYears = new LinkedList<SSNewAccountingYear>();
        if(iCurrentCompany != null){
            try {
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear WHERE companyid=?");
                iStatement.setObject(1,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();

                while (iResultSet.next()) {
                    iYears.add((SSNewAccountingYear) iResultSet.getObject("accountingyear"));
                }
                iResultSet.close();
                iStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try { iConnection.rollback(); } catch (SQLException ignored) {}
                SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
            }
        }
        return iYears;
    }

    public List<SSNewAccountingYear> getYearsForCompany(SSNewCompany iCompany) {
        List<SSNewAccountingYear> iYears = new LinkedList<SSNewAccountingYear>();
        if(iCompany != null){
            try {
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear WHERE companyid=?");
                iStatement.setObject(1,iCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();

                while (iResultSet.next()) {
                    iYears.add((SSNewAccountingYear) iResultSet.getObject("accountingyear"));
                }
                iResultSet.close();
                iStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try { iConnection.rollback(); } catch (SQLException ignored) {}
                SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
            }
        }
        return iYears;
    }
    public SSNewAccountingYear getAccountingYear(SSNewAccountingYear pAccountingYear){
        try {
            if(pAccountingYear == null) return null;

            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear WHERE id=?");
            iStatement.setObject(1,pAccountingYear.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSNewAccountingYear iAccountingYear = (SSNewAccountingYear) iResultSet.getObject("accountingyear");
                iResultSet.close();
                iStatement.close();
                return iAccountingYear;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addAccountingYear(SSNewAccountingYear iAccountingYear) {
        if(iAccountingYear == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_accountingyear VALUES(NULL,?,?)");
            iStatement.setObject(1, iAccountingYear);
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear");
            ResultSet iResultSet = iStatement.executeQuery();
            Integer iId = -1;
            while(iResultSet.next()){
                if(iResultSet.isLast())
                    iId = iResultSet.getInt("id");
            }
            iAccountingYear.setId(iId);
            iStatement.close();

            iStatement = iConnection.prepareStatement("UPDATE tbl_accountingyear SET accountingyear=? WHERE id=?");
            iStatement.setObject(1, iAccountingYear);
            iStatement.setObject(2, iAccountingYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();

            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateAccountingYear(SSNewAccountingYear iAccountingYear) {
        if(iAccountingYear == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_accountingyear SET accountingyear=? WHERE id=?");
            iStatement.setObject(1, iAccountingYear);
            iStatement.setObject(2, iAccountingYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            if (iAccountingYear.equals(iCurrentYear)) {
                iCurrentYear = iAccountingYear;
                notifyListeners("YEAR",iAccountingYear, null);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteAccountingYear(SSNewAccountingYear iAccountingYear){
        if(iAccountingYear == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_voucher WHERE yearid=?");
            iStatement.setObject(1, iAccountingYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("DELETE FROM tbl_accountingyear WHERE id=?");
            iStatement.setObject(1, iAccountingYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public SSNewAccountingYear getPreviousYear(){
        iCurrentYear = getCurrentYear();
        if(iCurrentYear == null) return null;
        List<SSNewAccountingYear> iYears = getYears();

        Date iFirstDayOfCurrent = iCurrentYear.getFrom();

        //Hämta sista dagen i föregående år
        Calendar iCalendar = Calendar.getInstance();
        iCalendar.setTime(iFirstDayOfCurrent);
        iCalendar.add(Calendar.DATE, -1);
        for(SSNewAccountingYear iAccountingYear : iYears){
            Date iLastDayOfYear = iAccountingYear.getTo();
            Calendar iNewCalendar = Calendar.getInstance();
            iNewCalendar.setTime(iLastDayOfYear);
            if (iCalendar.get(Calendar.YEAR) == iNewCalendar.get(Calendar.YEAR) && iCalendar.get(Calendar.MONTH) == iNewCalendar.get(Calendar.MONTH) && iCalendar.get(Calendar.DATE) == iNewCalendar.get(Calendar.DATE))
            {
                return iAccountingYear;
            }
        }
        return null;
    }

    public SSNewAccountingYear getLastYear(){
        List<SSNewAccountingYear> iYears = new LinkedList<SSNewAccountingYear>();
        if(iCurrentCompany != null){
            try {
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountingyear WHERE companyid=?");
                iStatement.setObject(1,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();

                while (iResultSet.next()) {
                    iYears.add((SSNewAccountingYear) iResultSet.getObject("accountingyear"));
                }
                iResultSet.close();
                iStatement.close();

                Date iLastDate = null;
                SSNewAccountingYear iAccountingYear = null;
                for (SSNewAccountingYear iYear : iYears) {
                    if (iLastDate == null) {
                        iLastDate = iYear.getTo();
                        iAccountingYear = iYear;
                    }
                    Date iTo = iYear.getTo();
                    if (iTo.after(iLastDate)) {
                        iLastDate = iTo;
                        iAccountingYear = iYear;
                    }
                }
                return iAccountingYear;
            } catch (SQLException e) {
                e.printStackTrace();
                try { iConnection.rollback(); } catch (SQLException ignored) {}
                SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
            }
        }
        return null;
    }


    /**
     *
     * Adds a property listerner to the database, the avaiable properties is:
     *   IO      : I/O event
     *   COMPANY : Changed active company
     *   YEAR    : Changed active year
     *
     * @param pProperty
     * @param pPropertyChangeListener
     */
    public void addPropertyChangeListener(String pProperty, PropertyChangeListener pPropertyChangeListener) {
        List<PropertyChangeListener> iPropertyChangeListeners = iListenerMap.get(pProperty);

        if (iPropertyChangeListeners == null) {
            iPropertyChangeListeners = new LinkedList<PropertyChangeListener>();

            iListenerMap.put(pProperty, iPropertyChangeListeners );
        }

        iPropertyChangeListeners.add(pPropertyChangeListener);
    }

    /**
     *
     * @param pProperty
     * @param pNewValue
     * @param pOldValue
     */
    public void notifyListeners(String pProperty, Object pNewValue, Object pOldValue) {

        List<PropertyChangeListener> iPropertyChangeListeners = iListenerMap.get(pProperty);

        if (iPropertyChangeListeners == null) return;

        PropertyChangeEvent iPropertyChangeEvent = new PropertyChangeEvent(this, pProperty, pOldValue, pNewValue);

        for (PropertyChangeListener iPropertyChangeListener : iPropertyChangeListeners) {
            iPropertyChangeListener.propertyChange(iPropertyChangeEvent);
        }
    }

    public SSAutoIncrement getAutoIncrement() {
        return null;
    }

    public List<SSVoucher> getVouchers() {
        if (iVouchers != null) {
            return iVouchers;
        }
        iVouchers = new LinkedList<SSVoucher>();
        if(iCurrentYear == null) return iVouchers;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_voucher WHERE yearid=? AND id>?");
                iStatement.setObject(1, iCurrentYear.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iVouchers.add((SSVoucher) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iVouchers;
    }

    public List<SSVoucher> getVouchers(SSNewAccountingYear iAccountingYear) {
        List<SSVoucher> iVoucherList = new LinkedList<SSVoucher>();
        if(iAccountingYear == null) return iVoucherList;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_voucher WHERE yearid=? AND id>?");
                iStatement.setObject(1, iAccountingYear.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iVoucherList.add((SSVoucher) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iVoucherList;
    }

    public SSVoucher getVoucher(SSVoucher pVoucher){
        if(pVoucher == null || iCurrentYear == null) return null;

        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_voucher WHERE number=? AND yearid=?");
            iStatement.setObject(1,pVoucher.getNumber());
            iStatement.setObject(2,iCurrentYear.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSVoucher iVoucher = (SSVoucher) iResultSet.getObject(3);
                iStatement.close();
                return iVoucher;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSVoucher> getVouchers(List<SSVoucher> pVouchers){
        if(pVouchers == null || iCurrentYear == null) return null;
        List<SSVoucher> iVouchers = new LinkedList<SSVoucher>();
        try {
            for(SSVoucher iVoucher : pVouchers){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_voucher WHERE number=? AND yearid=?");
                iStatement.setObject(1,iVoucher.getNumber());
                iStatement.setObject(2,iCurrentYear.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iVouchers.add((SSVoucher) iResultSet.getObject("voucher"));
                }
                iStatement.close();
            }

            return iVouchers;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addVoucher(SSVoucher iVoucher, boolean iHasNumber) {
        if(iVoucher == null || iCurrentYear == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement;
            if(!iHasNumber){
                iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_voucher WHERE yearid=?");
                iStatement.setObject(1, iCurrentYear.getId());
                ResultSet iResultSet = iStatement.executeQuery();

                if (iResultSet.next()) {
                    Integer iNumber = iResultSet.getInt("maxnum");
                    iVoucher.setNumber(iNumber+1);
                } else {
                    iVoucher.setNumber(1);
                }
                iResultSet.close();
                iStatement.close();
            }

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_voucher VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iVoucher.getNumber());
            iStatement.setObject(2, iVoucher);
            iStatement.setObject(3, iCurrentYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public Integer getLastVoucherNumber() {
        if(iCurrentYear == null) return 0;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_voucher WHERE yearid=?");
            iStatement.setObject(1, iCurrentYear.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iNumber = 0;
            if (iResultSet.next()) {
                iNumber = iResultSet.getInt("maxnum");
            }
            iResultSet.close();
            iStatement.close();

            return iNumber;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return 0;
    }

    public void updateVoucher(SSVoucher iVoucher) {
        if(iVoucher == null || iCurrentYear == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_voucher SET voucher=? WHERE number=? AND yearid=?");
            iStatement.setObject(1, iVoucher);
            iStatement.setObject(2, iVoucher.getNumber());
            iStatement.setObject(3, iCurrentYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteVoucher(SSVoucher iVoucher) {
        if(iVoucher == null || iCurrentYear == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_voucher WHERE number=? AND yearid=?");
            iStatement.setObject(1, iVoucher.getNumber());
            iStatement.setObject(2, iCurrentYear.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public List<SSVoucherTemplate> getVoucherTemplates() {
        List<SSVoucherTemplate> iVoucherTemplates = new LinkedList<SSVoucherTemplate>();
        if(iCurrentCompany == null) return iVoucherTemplates;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_vouchertemplate WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();
            int i = 0;
            while (iResultSet.next()) {
                iVoucherTemplates.add((SSVoucherTemplate) iResultSet.getObject(2));
                i++;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iVoucherTemplates;
    }

    public List<SSVoucherTemplate> getVoucherTemplates(List<SSVoucherTemplate> pVoucherTemplates){
        if(pVoucherTemplates == null) return null;
        List<SSVoucherTemplate> iVoucherTemplates = new LinkedList<SSVoucherTemplate>();
        if(iCurrentCompany == null) return iVoucherTemplates;
        try {
            for(SSVoucherTemplate iVoucherTemplate : pVoucherTemplates){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_vouchertemplate WHERE name=? AND companyid=?");
                iStatement.setObject(1,iVoucherTemplate.getDescription());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iVoucherTemplates.add((SSVoucherTemplate) iResultSet.getObject(2));
                }
                iStatement.close();
            }

            return iVoucherTemplates;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addVoucherTemplate(SSVoucherTemplate iVoucherTemplate) {
        if(iVoucherTemplate == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_vouchertemplate VALUES(?,?,?)");
            iStatement.setObject(1, iVoucherTemplate.getDescription());
            iStatement.setObject(2, iVoucherTemplate);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteVoucherTemplate(SSVoucherTemplate iVoucherTemplate) {
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_vouchertemplate WHERE name=? AND companyid=?");
            iStatement.setObject(1, iVoucherTemplate.getDescription());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public List<SSAccount> getAccounts() {
        return iCurrentYear == null ? new LinkedList<SSAccount>() : iCurrentYear.getAccounts();
    }

    /**
     * Retuns the account plan for the current year
     *
     * @return the acoount plan for the current year
     */
    public SSAccountPlan getCurrentAccountPlan() {

        if(iCurrentYear != null) {
            return iCurrentYear.getAccountPlan();
        }
        return new SSAccountPlan("Default");
    }


    public List<SSAccountPlan> getAccountPlans() {
        List<SSAccountPlan> iAccountPlans = new LinkedList<SSAccountPlan>();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountplan");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iAccountPlans.add((SSAccountPlan) iResultSet.getObject("accountplan"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iAccountPlans;
    }

    public SSAccountPlan getAccountPlan(SSAccountPlan pAccountPlan){
        if(pAccountPlan == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountplan WHERE id=?");
            iStatement.setObject(1,pAccountPlan.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSAccountPlan iAccountPlan = (SSAccountPlan) iResultSet.getObject("accountplan");
                iStatement.close();
                return iAccountPlan;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addAccountPlan(SSAccountPlan iAccountPlan) {
        if(iAccountPlan == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_accountplan VALUES(NULL,?)");
            iStatement.setObject(1, iAccountPlan);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_accountplan");
            ResultSet iResultSet = iStatement.executeQuery();
            Integer iId = -1;
            while(iResultSet.next()){
                if(iResultSet.isLast())
                    iId = iResultSet.getInt("id");
            }
            iAccountPlan.setId(iId);
            iStatement.close();

            iStatement = iConnection.prepareStatement("UPDATE tbl_accountplan SET accountplan=? WHERE id=?");
            iStatement.setObject(1, iAccountPlan);
            iStatement.setObject(2, iAccountPlan.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateAccountPlan(SSAccountPlan iAccountPlan) {
        if(iAccountPlan == null) return;

        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_accountplan SET accountplan=? WHERE id=?");
            iStatement.setObject(1, iAccountPlan);
            iStatement.setObject(2, iAccountPlan.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteAccountPlan(SSAccountPlan iAccountPlan) {
        if(iAccountPlan == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_accountplan WHERE id=?");
            iStatement.setObject(1, iAccountPlan.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


    public List<SSUnit> getUnits() {
        List<SSUnit> iUnits = new LinkedList<SSUnit>();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_unit");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iUnits.add((SSUnit) iResultSet.getObject("unit"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iUnits;
    }

    public void addUnit(SSUnit iUnit) {
        if(iUnit == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_unit VALUES(?,?)");
            iStatement.setObject(1, iUnit.getName());
            iStatement.setObject(2, iUnit);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateUnit(SSUnit iUnit) {
        if(iUnit == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_unit SET unit=? WHERE name=?");
            iStatement.setObject(1, iUnit);
            iStatement.setObject(2, iUnit.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteUnit(SSUnit iUnit) {
        if(iUnit == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_unit WHERE name=?");
            iStatement.setObject(1, iUnit.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a List of the current curriencies
     *
     * @return A List of curriencies.
     */
    public List<SSCurrency> getCurrencies() {
        List<SSCurrency> iCurrencies = new LinkedList<SSCurrency>();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_currency");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iCurrencies.add((SSCurrency) iResultSet.getObject("currency"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iCurrencies;
    }

    public SSCurrency getCurrency(SSCurrency iCurrency) {
        SSCurrency iUpdatedCurrency = new SSCurrency();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_currency WHERE code=?");
            iStatement.setObject(1, iCurrency.getName());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                iUpdatedCurrency = (SSCurrency) iResultSet.getObject("currency");
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iUpdatedCurrency;
    }

    public void addCurrency(SSCurrency iCurrency) {
        if(iCurrency == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_currency VALUES(?,?)");
            iStatement.setObject(1, iCurrency.getName());
            iStatement.setObject(2, iCurrency);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateCurrency(SSCurrency iCurrency) {
        if(iCurrency == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_currency SET currency=? WHERE code=?");
            iStatement.setObject(1, iCurrency);
            iStatement.setObject(2, iCurrency.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteCurrency(SSCurrency iCurrency) {
        if(iCurrency == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_currency WHERE code=?");
            iStatement.setObject(1, iCurrency.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the delivery ways
     *
     * @return a list of deliveryways
     */
    public List<SSDeliveryWay> getDeliveryWays() {
        List<SSDeliveryWay> iDeliveryWays = new LinkedList<SSDeliveryWay>();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_deliveryway");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iDeliveryWays.add((SSDeliveryWay) iResultSet.getObject("deliveryway"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iDeliveryWays;
    }

    public void addDeliveryWay(SSDeliveryWay iDeliveryWay) {
        if(iDeliveryWay == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_deliveryway VALUES(?,?)");
            iStatement.setObject(1, iDeliveryWay.getName());
            iStatement.setObject(2, iDeliveryWay);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateDeliveryWay(SSDeliveryWay iDeliveryWay) {
        if(iDeliveryWay == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_deliveryway SET deliveryway=? WHERE name=?");
            iStatement.setObject(1, iDeliveryWay);
            iStatement.setObject(2, iDeliveryWay.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteDeliveryWay(SSDeliveryWay iDeliveryWay) {
        if(iDeliveryWay == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_deliveryway WHERE name=?");
            iStatement.setObject(1, iDeliveryWay.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retuns a list of delivery terms.
     *
     * @return a list of delivery terms
     */
    public List<SSDeliveryTerm> getDeliveryTerms() {
        List<SSDeliveryTerm> iDeliveryTerms = new LinkedList<SSDeliveryTerm>();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_deliveryterm");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iDeliveryTerms.add((SSDeliveryTerm) iResultSet.getObject("deliveryterm"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iDeliveryTerms;
    }

    public void addDeliveryTerm(SSDeliveryTerm iDeliveryTerm) {
        if(iDeliveryTerm == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_deliveryterm VALUES(?,?)");
            iStatement.setObject(1, iDeliveryTerm.getName());
            iStatement.setObject(2, iDeliveryTerm);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateDeliveryTerm(SSDeliveryTerm iDeliveryTerm) {
        if(iDeliveryTerm == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_deliveryterm SET deliveryterm=? WHERE name=?");
            iStatement.setObject(1, iDeliveryTerm);
            iStatement.setObject(2, iDeliveryTerm.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteDeliveryTerm(SSDeliveryTerm iDeliveryTerm) {
        if(iDeliveryTerm == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_deliveryterm WHERE name=?");
            iStatement.setObject(1, iDeliveryTerm.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     *  Returns the payment terms
     *
     * @return a list of payment terms
     */
    public List<SSPaymentTerm> getPaymentTerms() {
        List<SSPaymentTerm> iPaymentTerms = new LinkedList<SSPaymentTerm>();
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_paymentterm");
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iPaymentTerms.add((SSPaymentTerm) iResultSet.getObject("paymentterm"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iPaymentTerms;
    }

    public void addPaymentTerm(SSPaymentTerm iPaymentTerm) {
        if(iPaymentTerm == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_paymentterm VALUES(?,?)");
            iStatement.setObject(1, iPaymentTerm.getName());
            iStatement.setObject(2, iPaymentTerm);
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updatePaymentTerm(SSPaymentTerm iPaymentTerm) {
        if(iPaymentTerm == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_paymentterm SET paymentterm=? WHERE name=?");
            iStatement.setObject(1, iPaymentTerm);
            iStatement.setObject(2, iPaymentTerm.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deletePaymentTerm(SSPaymentTerm iPaymentTerm) {
        if(iPaymentTerm == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_paymentterm WHERE name=?");
            iStatement.setObject(1, iPaymentTerm.getName());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    public List<SSNewResultUnit> getResultUnits() {
        List<SSNewResultUnit> iResultUnits = new LinkedList<SSNewResultUnit>();
        if(iCurrentCompany == null) return iResultUnits;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_resultunit WHERE companyid=?");
            iStatement.setObject(1,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iResultUnits.add((SSNewResultUnit) iResultSet.getObject("resultunit"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iResultUnits;
    }

    public SSNewResultUnit getResultUnit(SSNewResultUnit pResultUnit){
        if(pResultUnit == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_resultunit WHERE number=? AND companyid=?");
            iStatement.setObject(1,pResultUnit.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSNewResultUnit iResultUnit = (SSNewResultUnit) iResultSet.getObject("resultunit");
                iStatement.close();
                return iResultUnit;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public SSNewResultUnit getResultUnit(String pResultUnitNumber){
        if(pResultUnitNumber == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_resultunit WHERE number=? AND companyid=?");
            iStatement.setObject(1,pResultUnitNumber);
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSNewResultUnit iResultUnit = (SSNewResultUnit) iResultSet.getObject("resultunit");
                iStatement.close();
                return iResultUnit;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSNewResultUnit> getResultUnits(List<SSNewResultUnit> pResultUnits){
        if(pResultUnits == null) return null;
        List<SSNewResultUnit> iResultUnits = new LinkedList<SSNewResultUnit>();
        if(iCurrentCompany == null) return iResultUnits;
        try {
            for(SSNewResultUnit iResultUnit : pResultUnits){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_resultunit WHERE number=? AND companyid=?");
                iStatement.setObject(1,iResultUnit.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iResultUnits.add((SSNewResultUnit) iResultSet.getObject("resultunit"));
                }
                iStatement.close();
            }

            return iResultUnits;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addResultUnit(SSNewResultUnit iResultUnit) {
        if(iResultUnit == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_resultunit VALUES(?,?,?)");
            iStatement.setObject(1, iResultUnit.getNumber());
            iStatement.setObject(2, iResultUnit);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateResultUnit(SSNewResultUnit iResultUnit) {
        if(iResultUnit == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_resultunit SET resultunit=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iResultUnit);
            iStatement.setObject(2, iResultUnit.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteResultUnit(SSNewResultUnit iResultUnit) {
        if(iResultUnit == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_resultunit WHERE number=? AND companyid=?");
            iStatement.setObject(1, iResultUnit.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    public List<SSNewProject> getProjects() {
        List<SSNewProject> iProjects = new LinkedList<SSNewProject>();
        if(iCurrentCompany == null) return iProjects;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_project WHERE companyid=?");
            iStatement.setObject(1,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            while (iResultSet.next()) {
                iProjects.add((SSNewProject) iResultSet.getObject("project"));
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iProjects;
    }

    public SSNewProject getProject(SSNewProject pProject){
        if(pProject == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_project WHERE number=? AND companyid=?");
            iStatement.setObject(1,pProject.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSNewProject iProject = (SSNewProject) iResultSet.getObject("project");
                iStatement.close();
                return iProject;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public SSNewProject getProject(String pProjectNumber){
        if(pProjectNumber == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_project WHERE number=? AND companyid=?");
            iStatement.setObject(1,pProjectNumber);
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSNewProject iProject = (SSNewProject) iResultSet.getObject("project");
                iStatement.close();
                return iProject;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSNewProject> getProjects(List<SSNewProject> pProjects){
        if(pProjects == null) return null;
        List<SSNewProject> iProjects = new LinkedList<SSNewProject>();
        if(iCurrentCompany == null) return iProjects;
        try {
            for(SSNewProject iProject : pProjects){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_project WHERE number=? AND companyid=?");
                iStatement.setObject(1,iProject.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iProjects.add((SSNewProject) iResultSet.getObject("project"));
                }
                iStatement.close();
            }

            return iProjects;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addProject(SSNewProject iProject) {
        if(iProject == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_project VALUES(?,?,?)");
            iStatement.setObject(1, iProject.getNumber());
            iStatement.setObject(2, iProject);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateProject(SSNewProject iProject) {
        if(iProject == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_project SET project=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iProject);
            iStatement.setObject(2, iProject.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteProject(SSNewProject iProject) {
        if(iProject == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_project WHERE number=? AND companyid=?");
            iStatement.setObject(1, iProject.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    public synchronized void triggerAction(String iTriggerName, String iTableName, String iNumber) {
        /** Körs då en trigger triggas i databasen. De flesta triggers uppdaterar listan som
         *  som motsvarar objekten triggen körts på. Projekt, Resultatenhet och konteringsmallar får
         *  behandlas något annorlunda då dessa inte lästs in i minnet vid uppstart.
         */

        try {
            /**
             *  REGISTER
             */
            if (iTriggerName.contains("PROJECT")) {
                if (SSProjectFrame.getInstance() != null) SSProjectFrame.getInstance().updateFrame();
            } else if (iTriggerName.contains("RESULTUNIT")) {
                if (SSResultUnitFrame.getInstance() != null) SSResultUnitFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWPRODUCT") && iProducts != null) {
                SSProduct iProduct = new SSProduct();
                iProduct.setNumber(iNumber);
                iProduct = getProduct(iProduct);

                iProducts.add(iProduct);
                iProduct = null;
                if (SSProductFrame.getInstance() != null) SSProductFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("EDITPRODUCT") && iProducts != null) {
                SSProduct iProduct = new SSProduct();
                iProduct.setNumber(iNumber);
                iProduct = getProduct(iProduct);
                int iIndex = iProducts.lastIndexOf(iProduct);
                if(iIndex == -1) return;
                iProducts.remove(iIndex);
                iProducts.add(iIndex, iProduct);
                iProduct = null;
                if (SSProductFrame.getInstance() != null) SSProductFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEPRODUCT") && iProducts != null) {
                SSProduct iProduct = new SSProduct();
                iProduct.setNumber(iNumber);
                iProducts.remove(iProduct);
                iProduct = null;
                if (SSProductFrame.getInstance() != null) SSProductFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWCUSTOMER") && iCustomers != null) {
                SSCustomer iCustomer = new SSCustomer();
                iCustomer.setNumber(iNumber);
                iCustomer = getCustomer(iCustomer);
                iCustomers.add(iCustomer);
                SSCustomerMath.iInvoicesForCustomers.put(iCustomer.getNumber(), new LinkedList<SSInvoice>());
                iCustomer = null;
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("EDITCUSTOMER") && iCustomers != null) {
                SSCustomer iCustomer = new SSCustomer();
                iCustomer.setNumber(iNumber);
                iCustomer = getCustomer(iCustomer);
                int iIndex = iCustomers.lastIndexOf(iCustomer);
                if(iIndex == -1) return;
                iCustomers.remove(iIndex);
                iCustomers.add(iIndex, iCustomer);
                iCustomer = null;
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETECUSTOMER") && iCustomers != null) {
                SSCustomer iCustomer = new SSCustomer();
                iCustomer.setNumber(iNumber);
                iCustomers.remove(iCustomer);
                iCustomer = null;
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWSUPPLIER") && iSuppliers != null) {
                SSSupplier iSupplier = new SSSupplier();
                iSupplier.setNumber(iNumber);
                iSupplier = getSupplier(iSupplier);
                iSuppliers.add(iSupplier);
                SSSupplierMath.iInvoicesForSuppliers.put(iSupplier.getNumber(), new LinkedList<SSSupplierInvoice>());
                iSupplier = null;
                if (SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("EDITSUPPLIER") && iSuppliers != null) {
                SSSupplier iSupplier = new SSSupplier();
                iSupplier.setNumber(iNumber);
                iSupplier = getSupplier(iSupplier);
                int iIndex = iSuppliers.lastIndexOf(iSupplier);
                if(iIndex == -1) return;
                iSuppliers.remove(iIndex);
                iSuppliers.add(iIndex, iSupplier);
                iSupplier = null;
                if (SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETESUPPLIER") && iSuppliers != null) {
                SSSupplier iSupplier = new SSSupplier();
                iSupplier.setNumber(iNumber);
                iSuppliers.remove(iSupplier);
                iSupplier = null;
                if (SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
            } else if (iTriggerName.contains("VOUCHERTEMPLATE")) {
                if (SSVoucherTemplateFrame.getInstance() != null) SSVoucherTemplateFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWAUTODIST") && iAutoDists != null) {
                Integer iAccount = Integer.parseInt(iNumber);
                SSAutoDist iAutoDist = new SSAutoDist();
                iAutoDist.setAccountNumber(iAccount);
                iAutoDist = getAutoDist(iAutoDist);
                iAutoDists.add(iAutoDist);
                iAutoDist = null;
                if (SSAutoDistFrame.getInstance() != null) SSAutoDistFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("EDITAUTODIST") && iAutoDists != null) {
                Integer iAccount = Integer.parseInt(iNumber);
                SSAutoDist iAutoDist = new SSAutoDist();
                iAutoDist.setAccountNumber(iAccount);
                iAutoDist = getAutoDist(iAutoDist);
                int iIndex = iAutoDists.lastIndexOf(iAutoDist);
                if(iIndex == -1) return;
                iAutoDists.remove(iIndex);
                iAutoDists.add(iIndex, iAutoDist);
                iAutoDist = null;
                if (SSAutoDistFrame.getInstance() != null) SSAutoDistFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEAUTODIST") && iAutoDists != null) {
                Integer iAccount = Integer.parseInt(iNumber);
                SSAutoDist iAutoDist = new SSAutoDist();
                iAutoDist.setAccountNumber(iAccount);
                iAutoDists.remove(iAutoDist);
                iAutoDist = null;
                if (SSAutoDistFrame.getInstance() != null) SSAutoDistFrame.getInstance().updateFrame();
            }
            /**
             * FÖRSÄLJNING
             */
            else if (iTriggerName.equals("NEWINPAYMENT") && iInpayments != null) {
                SSInpayment iInpayment = new SSInpayment();
                iInpayment.setNumber(Integer.parseInt(iNumber));
                iInpayment = getInpayment(iInpayment);
                if (!iInpayments.contains(iInpayment)) {
                    iInpayments.add(iInpayment);
                }
                for(SSInpaymentRow iRow : iInpayment.getRows()){
                    if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                        if(SSInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                            SSInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).subtract(iRow.getValue()));
                        }
                    }
                }
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                if (SSInpaymentFrame.getInstance() != null) SSInpaymentFrame.getInstance().updateFrame();
                iInpayment = null;
            } else if (iTriggerName.equals("EDITINPAYMENT") && iInpayments != null) {
                SSInpayment iInpayment = new SSInpayment();
                iInpayment.setNumber(Integer.parseInt(iNumber));
                iInpayment = getInpayment(iInpayment);
                int iIndex = iInpayments.lastIndexOf(iInpayment);
                if(iIndex == -1) return;
                SSInpayment iOldInpayment = iInpayments.get(iIndex);
                for(SSInpaymentRow iRow : iOldInpayment.getRows()){
                    if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                        if(SSInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                            SSInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).add(iRow.getValue()));
                        }
                    }
                }
                iInpayments.remove(iIndex);

                iInpayments.add(iIndex, iInpayment);
                for(SSInpaymentRow iRow : iInpayment.getRows()){
                    if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                        if(SSInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                            SSInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).subtract(iRow.getValue()));
                        }
                    }
                }
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                iInpayment = null;
                if (SSInpaymentFrame.getInstance() != null) SSInpaymentFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEINPAYMENT") && iInpayments != null) {
                SSInpayment iInpayment = new SSInpayment();
                iInpayment.setNumber(Integer.parseInt(iNumber));
                iInpayments.remove(iInpayment);

                iInpayment = null;
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                if (SSInpaymentFrame.getInstance() != null) SSInpaymentFrame.getInstance().updateFrame();

            } else if (iTriggerName.equals("NEWTENDER") && iTenders != null) {
                SSTender iTender = new SSTender();
                iTender.setNumber(Integer.parseInt(iNumber));
                iTender = getTender(iTender);
                if (!iTenders.contains(iTender)) {
                    iTenders.add(iTender);
                }
                if (SSTenderFrame.getInstance() != null) SSTenderFrame.getInstance().updateFrame();
                iTender = null;
            } else if (iTriggerName.equals("EDITTENDER") && iTenders != null) {
                SSTender iTender = new SSTender();
                iTender.setNumber(Integer.parseInt(iNumber));
                iTender = getTender(iTender);
                int iIndex = iTenders.lastIndexOf(iTender);
                if(iIndex == -1) return;
                iTenders.remove(iIndex);
                iTenders.add(iIndex, iTender);
                iTender = null;
                if (SSTenderFrame.getInstance() != null) SSTenderFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETETENDER") && iTenders != null) {
                SSTender iTender = new SSTender();
                iTender.setNumber(Integer.parseInt(iNumber));
                iTenders.remove(iTender);
                iTender = null;
                if (SSTenderFrame.getInstance() != null) SSTenderFrame.getInstance().updateFrame();

            } else if (iTriggerName.equals("NEWORDER") && iOrders != null) {
                SSOrder iOrder = new SSOrder();
                iOrder.setNumber(Integer.parseInt(iNumber));
                iOrder = getOrder(iOrder);
                if (!iOrders.contains(iOrder)) {
                    iOrders.add(iOrder);
                }
                if (SSOrderFrame.getInstance() != null) SSOrderFrame.getInstance().updateFrame();
                iOrder = null;
            } else if (iTriggerName.equals("EDITORDER") && iOrders != null) {
                SSOrder iOrder = new SSOrder();
                iOrder.setNumber(Integer.parseInt(iNumber));
                iOrder = getOrder(iOrder);
                int iIndex = iOrders.lastIndexOf(iOrder);
                if(iIndex == -1) return;
                iOrders.remove(iIndex);
                iOrders.add(iIndex, iOrder);
                iOrder = null;
                if (SSOrderFrame.getInstance() != null) SSOrderFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEORDER") && iOrders != null) {
                SSOrder iOrder = new SSOrder();
                iOrder.setNumber(Integer.parseInt(iNumber));
                iOrders.remove(iOrder);
                iOrder = null;
                if (SSOrderFrame.getInstance() != null) SSOrderFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWINVOICE") && iInvoices!= null) {
                SSInvoice iInvoice = new SSInvoice();
                iInvoice.setNumber(Integer.parseInt(iNumber));
                iInvoice = getInvoice(iInvoice);
                if (!iInvoices.contains(iInvoice)) {
                    iInvoices.add(iInvoice);
                }
                SSInvoiceMath.iSaldoMap.put(iInvoice.getNumber(),SSInvoiceMath.getSaldo(iInvoice));
                if(SSCustomerMath.iInvoicesForCustomers.containsKey(iInvoice.getCustomerNr())){
                    SSCustomerMath.iInvoicesForCustomers.get(iInvoice.getCustomerNr()).add(iInvoice);
                }
                else {
                    List<SSInvoice> iNumbers = new LinkedList<SSInvoice>();
                    iNumbers.add(iInvoice);
                    SSCustomerMath.iInvoicesForCustomers.put(iInvoice.getCustomerNr(),iNumbers);
                }
                if (SSOrderFrame.getInstance() != null) SSOrderFrame.getInstance().updateFrame();
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                iInvoice = null;
            } else if (iTriggerName.equals("EDITINVOICE") && iInvoices!= null) {
                SSInvoice iInvoice = new SSInvoice();
                iInvoice.setNumber(Integer.parseInt(iNumber));
                iInvoice = getInvoice(iInvoice);
                int iIndex = iInvoices.lastIndexOf(iInvoice);
                if(iIndex == -1) return;
                iInvoices.remove(iIndex);
                iInvoices.add(iIndex, iInvoice);
                SSInvoiceMath.iSaldoMap.put(iInvoice.getNumber(),SSInvoiceMath.getSaldo(iInvoice));
                iIndex = SSCustomerMath.iInvoicesForCustomers.get(iInvoice.getCustomerNr()).indexOf(iInvoice);
                if(iIndex != -1){
                        SSCustomerMath.iInvoicesForCustomers.get(iInvoice.getCustomerNr()).remove(iIndex);
                        SSCustomerMath.iInvoicesForCustomers.get(iInvoice.getCustomerNr()).add(iIndex, iInvoice);
                }
                iInvoice = null;
                if (SSOrderFrame.getInstance() != null) SSOrderFrame.getInstance().updateFrame();
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEINVOICE") && iInvoices!= null) {
                SSInvoice iInvoice = new SSInvoice();
                iInvoice.setNumber(Integer.parseInt(iNumber));
                iInvoices.remove(iInvoice);
                SSInvoiceMath.iSaldoMap.remove(iInvoice.getNumber());
                iInvoice = null;
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWCREDITINVOICE") && iCreditInvoices!= null) {
                SSCreditInvoice iCreditInvoice = new SSCreditInvoice();
                iCreditInvoice.setNumber(Integer.parseInt(iNumber));
                iCreditInvoice = getCreditInvoice(iCreditInvoice);
                if (!iCreditInvoices.contains(iCreditInvoice)) {
                    iCreditInvoices.add(iCreditInvoice);
                }

                if(SSInvoiceMath.iSaldoMap.containsKey(iCreditInvoice.getCreditingNr())){
                    SSInvoiceMath.iSaldoMap.put(iCreditInvoice.getCreditingNr(),SSInvoiceMath.iSaldoMap.get(iCreditInvoice.getCreditingNr()).subtract(SSCreditInvoiceMath.getTotalSum(iCreditInvoice)));
                }
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                if (SSCreditInvoiceFrame.getInstance() != null) SSCreditInvoiceFrame.getInstance().updateFrame();
                iCreditInvoice = null;
            } else if (iTriggerName.equals("EDITCREDITINVOICE") && iCreditInvoices!= null) {
                SSCreditInvoice iCreditInvoice = new SSCreditInvoice();
                iCreditInvoice.setNumber(Integer.parseInt(iNumber));
                iCreditInvoice = getCreditInvoice(iCreditInvoice);
                int iIndex = iCreditInvoices.lastIndexOf(iCreditInvoice);
                if(iIndex == -1) return;
                SSCreditInvoice iOldCreditInvoice = iCreditInvoices.get(iIndex);
                if(SSInvoiceMath.iSaldoMap.containsKey(iOldCreditInvoice.getCreditingNr())){
                    SSInvoiceMath.iSaldoMap.put(iOldCreditInvoice.getCreditingNr(),SSInvoiceMath.iSaldoMap.get(iOldCreditInvoice.getCreditingNr()).add(SSCreditInvoiceMath.getTotalSum(iOldCreditInvoice)));
                }
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                iCreditInvoices.remove(iIndex);
                iCreditInvoices.add(iIndex, iCreditInvoice);
                if(SSInvoiceMath.iSaldoMap.containsKey(iCreditInvoice.getCreditingNr())){
                    SSInvoiceMath.iSaldoMap.put(iCreditInvoice.getCreditingNr(),SSInvoiceMath.iSaldoMap.get(iCreditInvoice.getCreditingNr()).subtract(SSCreditInvoiceMath.getTotalSum(iCreditInvoice)));
                }
                if (SSInvoiceFrame.getInstance() != null) SSInvoiceFrame.getInstance().updateFrame();
                iCreditInvoice = null;
                if (SSCreditInvoiceFrame.getInstance() != null) SSCreditInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETECREDITINVOICE") && iCreditInvoices!= null) {
                SSCreditInvoice iCreditInvoice = new SSCreditInvoice();
                iCreditInvoice.setNumber(Integer.parseInt(iNumber));
                iCreditInvoices.remove(iCreditInvoice);
                iCreditInvoice = null;
                if (SSCustomerFrame.getInstance() != null) SSCustomerFrame.getInstance().updateFrame();
                if (SSCreditInvoiceFrame.getInstance() != null) SSCreditInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWPERIODICINVOICE") && iPeriodicInvoices!= null) {
                SSPeriodicInvoice iPeriodicInvoice = new SSPeriodicInvoice();
                iPeriodicInvoice.setNumber(Integer.parseInt(iNumber));
                iPeriodicInvoice = getPeriodicInvoice(iPeriodicInvoice);
                if (!iPeriodicInvoices.contains(iPeriodicInvoice)) {
                    iPeriodicInvoices.add(iPeriodicInvoice);
                }
                if (SSPeriodicInvoiceFrame.getInstance() != null) SSPeriodicInvoiceFrame.getInstance().updateFrame();
                iPeriodicInvoice = null;
            } else if (iTriggerName.equals("EDITPERIODICINVOICE") && iPeriodicInvoices!= null) {
                SSPeriodicInvoice iPeriodicInvoice = new SSPeriodicInvoice();
                iPeriodicInvoice.setNumber(Integer.parseInt(iNumber));
                iPeriodicInvoice = getPeriodicInvoice(iPeriodicInvoice);
                int iIndex = iPeriodicInvoices.lastIndexOf(iPeriodicInvoice);
                if(iIndex == -1) return;
                iPeriodicInvoices.remove(iIndex);
                iPeriodicInvoices.add(iIndex, iPeriodicInvoice);
                iPeriodicInvoice = null;
                if (SSPeriodicInvoiceFrame.getInstance() != null) SSPeriodicInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEPERIODICINVOICE") && iPeriodicInvoices!= null) {
                SSPeriodicInvoice iPeriodicInvoice = new SSPeriodicInvoice();
                iPeriodicInvoice.setNumber(Integer.parseInt(iNumber));
                iPeriodicInvoices.remove(iPeriodicInvoice);
                iPeriodicInvoice = null;
                if (SSPeriodicInvoiceFrame.getInstance() != null) SSPeriodicInvoiceFrame.getInstance().updateFrame();
            }
            /**
             * INKÖP
             */
            else if (iTriggerName.equals("NEWOUTPAYMENT") && iOutpayments!= null) {
                SSOutpayment iOutpayment = new SSOutpayment();
                iOutpayment.setNumber(Integer.parseInt(iNumber));
                iOutpayment = getOutpayment(iOutpayment);
                if (!iOutpayments.contains(iOutpayment)) {
                    iOutpayments.add(iOutpayment);
                }
                for(SSOutpaymentRow iRow : iOutpayment.getRows()){
                    if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                        if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                            SSSupplierInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSSupplierInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).subtract(iRow.getValue()));
                        }
                    }
                }
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                if (SSOutpaymentFrame.getInstance() != null) SSOutpaymentFrame.getInstance().updateFrame();
                iOutpayment = null;
            } else if (iTriggerName.equals("EDITOUTPAYMENT") && iOutpayments!= null) {
                SSOutpayment iOutpayment = new SSOutpayment();
                iOutpayment.setNumber(Integer.parseInt(iNumber));
                iOutpayment = getOutpayment(iOutpayment);
                int iIndex = iOutpayments.lastIndexOf(iOutpayment);
                if(iIndex == -1) return;
                SSOutpayment iOldOutpayment = iOutpayments.get(iIndex);
                for(SSOutpaymentRow iRow : iOldOutpayment.getRows()){
                    if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                        if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                            SSSupplierInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSSupplierInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).add(iRow.getValue()));
                        }
                    }
                }
                iOutpayments.remove(iIndex);
                iOutpayments.add(iIndex, iOutpayment);
                for(SSOutpaymentRow iRow : iOutpayment.getRows()){
                    if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                        if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                            SSSupplierInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSSupplierInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).subtract(iRow.getValue()));
                        }
                    }
                }
                iOutpayment = null;
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                if (SSOutpaymentFrame.getInstance() != null) SSOutpaymentFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEOUTPAYMENT") && iOutpayments!= null) {
                SSOutpayment iOutpayment = new SSOutpayment();
                iOutpayment.setNumber(Integer.parseInt(iNumber));
                iOutpayments.remove(iOutpayment);
                iOutpayment = null;
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                if (SSOutpaymentFrame.getInstance() != null) SSOutpaymentFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWPURCHASEORDER") && iPurchaseOrders!= null) {
                SSPurchaseOrder iPurchaseOrder = new SSPurchaseOrder();
                iPurchaseOrder.setNumber(Integer.parseInt(iNumber));
                iPurchaseOrder = getPurchaseOrder(iPurchaseOrder);
                if (!iPurchaseOrders.contains(iPurchaseOrder)) {
                    iPurchaseOrders.add(iPurchaseOrder);
                }
                if (SSOrderFrame.getInstance() != null ) SSOrderFrame.getInstance().updateFrame();
                if (SSPurchaseOrderFrame.getInstance() != null) SSPurchaseOrderFrame.getInstance().updateFrame();
                iPurchaseOrder = null;
            } else if (iTriggerName.equals("EDITPURCHASEORDER") && iPurchaseOrders!= null) {
                SSPurchaseOrder iPurchaseOrder = new SSPurchaseOrder();
                iPurchaseOrder.setNumber(Integer.parseInt(iNumber));
                iPurchaseOrder = getPurchaseOrder(iPurchaseOrder);
                int iIndex = iPurchaseOrders.lastIndexOf(iPurchaseOrder);
                if(iIndex == -1) return;
                iPurchaseOrders.remove(iIndex);
                iPurchaseOrders.add(iIndex, iPurchaseOrder);
                iPurchaseOrder = null;
                if (SSPurchaseOrderFrame.getInstance() != null) SSPurchaseOrderFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEPURCHASEORDER") && iPurchaseOrders!= null) {
                SSPurchaseOrder iPurchaseOrder = new SSPurchaseOrder();
                iPurchaseOrder.setNumber(Integer.parseInt(iNumber));
                iPurchaseOrders.remove(iPurchaseOrder);
                iPurchaseOrder = null;
                if (SSOrderFrame.getInstance() != null ) SSOrderFrame.getInstance().updateFrame();
                if (SSPurchaseOrderFrame.getInstance() != null) SSPurchaseOrderFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWSUPPLIERINVOICE") && iSupplierInvoices!= null) {
                SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice();
                iSupplierInvoice.setNumber(Integer.parseInt(iNumber));
                iSupplierInvoice = getSupplierInvoice(iSupplierInvoice);
                if (!iSupplierInvoices.contains(iSupplierInvoice)) {
                    iSupplierInvoices.add(iSupplierInvoice);
                }
                SSSupplierInvoiceMath.iSaldoMap.put(iSupplierInvoice.getNumber(),SSSupplierInvoiceMath.getSaldo(iSupplierInvoice));
                if(SSSupplierMath.iInvoicesForSuppliers.containsKey(iSupplierInvoice.getSupplierNr())){
                    SSSupplierMath.iInvoicesForSuppliers.get(iSupplierInvoice.getSupplierNr()).add(iSupplierInvoice);
                }
                else {
                    List<SSSupplierInvoice> iNumbers = new LinkedList<SSSupplierInvoice>();
                    iNumbers.add(iSupplierInvoice);
                    SSSupplierMath.iInvoicesForSuppliers.put(iSupplierInvoice.getSupplierNr(),iNumbers);
                }
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                iSupplierInvoice = null;
            } else if (iTriggerName.equals("EDITSUPPLIERINVOICE") && iSupplierInvoices!= null) {
                SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice();
                iSupplierInvoice.setNumber(Integer.parseInt(iNumber));
                iSupplierInvoice = getSupplierInvoice(iSupplierInvoice);
                int iIndex = iSupplierInvoices.lastIndexOf(iSupplierInvoice);
                if(iIndex == -1) return;
                iSupplierInvoices.remove(iIndex);
                iSupplierInvoices.add(iIndex, iSupplierInvoice);
                SSSupplierInvoiceMath.iSaldoMap.put(iSupplierInvoice.getNumber(),SSSupplierInvoiceMath.getSaldo(iSupplierInvoice));
                iIndex = SSSupplierMath.iInvoicesForSuppliers.get(iSupplierInvoice.getSupplierNr()).indexOf(iSupplierInvoice);
                if(iIndex != -1){
                        SSSupplierMath.iInvoicesForSuppliers.get(iSupplierInvoice.getSupplierNr()).remove(iIndex);
                        SSSupplierMath.iInvoicesForSuppliers.get(iSupplierInvoice.getSupplierNr()).add(iIndex, iSupplierInvoice);
                }
                iSupplierInvoice = null;
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETESUPPLIERINVOICE") && iSupplierInvoices!= null) {
                SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice();
                iSupplierInvoice.setNumber(Integer.parseInt(iNumber));
                iSupplierInvoices.remove(iSupplierInvoice);
                SSSupplierInvoiceMath.iSaldoMap.remove(iSupplierInvoice.getNumber());
                iSupplierInvoice = null;
                if (SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWSUPPLIERCREDITINVOICE") && iSupplierCreditInvoices!= null) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = new SSSupplierCreditInvoice();
                iSupplierCreditInvoice.setNumber(Integer.parseInt(iNumber));
                iSupplierCreditInvoice = getSupplierCreditInvoice(iSupplierCreditInvoice);
                if (!iSupplierCreditInvoices.contains(iSupplierCreditInvoice)) {
                    iSupplierCreditInvoices.add(iSupplierCreditInvoice);
                }
                if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iSupplierCreditInvoice.getCreditingNr())){
                    SSSupplierInvoiceMath.iSaldoMap.put(iSupplierCreditInvoice.getCreditingNr(),SSSupplierInvoiceMath.iSaldoMap.get(iSupplierCreditInvoice.getCreditingNr()).subtract(SSSupplierInvoiceMath.getTotalSum(iSupplierCreditInvoice)));
                }
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                if (SSSupplierCreditInvoiceFrame.getInstance() != null) SSSupplierCreditInvoiceFrame.getInstance().updateFrame();
                iSupplierCreditInvoice = null;
            } else if (iTriggerName.equals("EDITSUPPLIERCREDITINVOICE") && iSupplierCreditInvoices!= null) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = new SSSupplierCreditInvoice();
                iSupplierCreditInvoice.setNumber(Integer.parseInt(iNumber));
                iSupplierCreditInvoice = getSupplierCreditInvoice(iSupplierCreditInvoice);
                int iIndex = iSupplierCreditInvoices.lastIndexOf(iSupplierCreditInvoice);
                if(iIndex == -1) return;
                SSSupplierCreditInvoice iOldSupplierCreditInvoice = iSupplierCreditInvoices.get(iIndex);
                if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iOldSupplierCreditInvoice.getCreditingNr())){
                    SSSupplierInvoiceMath.iSaldoMap.put(iOldSupplierCreditInvoice.getCreditingNr(),SSSupplierInvoiceMath.iSaldoMap.get(iOldSupplierCreditInvoice.getCreditingNr()).add(SSSupplierInvoiceMath.getTotalSum(iOldSupplierCreditInvoice)));
                }
                iSupplierCreditInvoices.remove(iIndex);
                iSupplierCreditInvoices.add(iIndex, iSupplierCreditInvoice);
                if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iSupplierCreditInvoice.getCreditingNr())){
                    SSSupplierInvoiceMath.iSaldoMap.put(iSupplierCreditInvoice.getCreditingNr(),SSSupplierInvoiceMath.iSaldoMap.get(iSupplierCreditInvoice.getCreditingNr()).subtract(SSSupplierInvoiceMath.getTotalSum(iSupplierCreditInvoice)));
                }
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                iSupplierCreditInvoice = null;
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierCreditInvoiceFrame.getInstance() != null) SSSupplierCreditInvoiceFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETESUPPLIERCREDITINVOICE") && iSupplierCreditInvoices!= null) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = new SSSupplierCreditInvoice();
                iSupplierCreditInvoice.setNumber(Integer.parseInt(iNumber));
                iSupplierCreditInvoices.remove(iSupplierCreditInvoice);
                iSupplierCreditInvoice = null;
                if(SSSupplierFrame.getInstance() != null) SSSupplierFrame.getInstance().updateFrame();
                if (SSSupplierInvoiceFrame.getInstance() != null) SSSupplierInvoiceFrame.getInstance().updateFrame();
                if (SSSupplierCreditInvoiceFrame.getInstance() != null) SSSupplierCreditInvoiceFrame.getInstance().updateFrame();
            }
            /**
             * LAGER
             */
            else if (iTriggerName.equals("NEWINVENTORY") && iInventories!= null) {
                SSInventory iInventory = new SSInventory();
                iInventory.setNumber(Integer.parseInt(iNumber));
                iInventory = getInventory(iInventory);
                if (!iInventories.contains(iInventory)) {
                    iInventories.add(iInventory);
                }
                if (SSInventoryFrame.getInstance() != null) SSInventoryFrame.getInstance().updateFrame();
                iInventory = null;
            } else if (iTriggerName.equals("EDITINVENTORY") && iInventories!= null) {
                SSInventory iInventory = new SSInventory();
                iInventory.setNumber(Integer.parseInt(iNumber));
                iInventory = getInventory(iInventory);
                int iIndex = iInventories.lastIndexOf(iInventory);
                if(iIndex == -1) return;
                iInventories.remove(iIndex);
                iInventories.add(iIndex, iInventory);
                iInventory = null;
                if (SSInventoryFrame.getInstance() != null) SSInventoryFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEINVENTORY") && iInventories!= null) {
                SSInventory iInventory = new SSInventory();
                iInventory.setNumber(Integer.parseInt(iNumber));
                iInventories.remove(iInventory);
                iInventory = null;
                if (SSInventoryFrame.getInstance() != null) SSInventoryFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWINDELIVERY") && iIndeliveries!= null) {
                SSIndelivery iIndelivery = new SSIndelivery();
                iIndelivery.setNumber(Integer.parseInt(iNumber));
                iIndelivery = getIndelivery(iIndelivery);
                if (!iIndeliveries.contains(iIndelivery)) {
                    iIndeliveries.add(iIndelivery);
                }
                if (SSIndeliveryFrame.getInstance() != null) SSIndeliveryFrame.getInstance().updateFrame();
                iIndelivery = null;
            } else if (iTriggerName.equals("EDITINDELIVERY") && iIndeliveries!= null) {
                SSIndelivery iIndelivery = new SSIndelivery();
                iIndelivery.setNumber(Integer.parseInt(iNumber));
                iIndelivery = getIndelivery(iIndelivery);
                int iIndex = iIndeliveries.lastIndexOf(iIndelivery);
                if(iIndex == -1) return;
                iIndeliveries.remove(iIndex);
                iIndeliveries.add(iIndex, iIndelivery);
                iIndelivery = null;
                if (SSIndeliveryFrame.getInstance() != null) SSIndeliveryFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEINDELIVERY") && iIndeliveries!= null) {
                SSIndelivery iIndelivery = new SSIndelivery();
                iIndelivery.setNumber(Integer.parseInt(iNumber));
                iIndeliveries.remove(iIndelivery);
                iIndelivery = null;
                if (SSIndeliveryFrame.getInstance() != null) SSIndeliveryFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWOUTDELIVERY") && iOutdeliveries!= null) {
                SSOutdelivery iOutdelivery = new SSOutdelivery();
                iOutdelivery.setNumber(Integer.parseInt(iNumber));
                iOutdelivery = getOutdelivery(iOutdelivery);
                if (!iOutdeliveries.contains(iOutdelivery)) {
                    iOutdeliveries.add(iOutdelivery);
                }
                if (SSOutdeliveryFrame.getInstance() != null) SSOutdeliveryFrame.getInstance().updateFrame();
                iOutdelivery = null;
            } else if (iTriggerName.equals("EDITOUTDELIVERY") && iOutdeliveries!= null) {
                SSOutdelivery iOutdelivery = new SSOutdelivery();
                iOutdelivery.setNumber(Integer.parseInt(iNumber));
                iOutdelivery = getOutdelivery(iOutdelivery);
                int iIndex = iOutdeliveries.lastIndexOf(iOutdelivery);
                if(iIndex == -1) return;
                iOutdeliveries.remove(iIndex);
                iOutdeliveries.add(iIndex, iOutdelivery);
                iOutdelivery = null;
                if (SSOutdeliveryFrame.getInstance() != null) SSOutdeliveryFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEOUTDELIVERY") && iOutdeliveries!= null) {
                SSOutdelivery iOutdelivery = new SSOutdelivery();
                iOutdelivery.setNumber(Integer.parseInt(iNumber));
                iOutdeliveries.remove(iOutdelivery);
                iOutdelivery = null;
                if (SSOutdeliveryFrame.getInstance() != null) SSOutdeliveryFrame.getInstance().updateFrame();
            }
            /**
             * BOKFÖRING
             */
            else if (iTriggerName.equals("NEWVOUCHER") && iVouchers!= null) {
                SSVoucher iVoucher = new SSVoucher(Integer.parseInt(iNumber));
                iVoucher = getVoucher(iVoucher);
                if (!iVouchers.contains(iVoucher)) {
                    iVouchers.add(iVoucher);
                }
                if (SSVoucherFrame.getInstance() != null) SSVoucherFrame.getInstance().updateFrame();
                iVoucher = null;
            } else if (iTriggerName.equals("EDITVOUCHER") && iVouchers!= null) {
                SSVoucher iVoucher = new SSVoucher(Integer.parseInt(iNumber));
                iVoucher = getVoucher(iVoucher);
                int iIndex = iVouchers.lastIndexOf(iVoucher);
                if(iIndex == -1) return;
                iVouchers.remove(iIndex);
                iVouchers.add(iIndex, iVoucher);
                iVoucher = null;
                if (SSVoucherFrame.getInstance() != null) SSVoucherFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEVOUCHER") && iVouchers!= null) {
                SSVoucher iVoucher = new SSVoucher(Integer.parseInt(iNumber));
                iVouchers.remove(iVoucher);
                iVoucher = null;
                if (SSVoucherFrame.getInstance() != null) SSVoucherFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("NEWOWNREPORT") && iOwnReports!= null) {
                SSOwnReport iOwnReport = new SSOwnReport();
                iOwnReport.setId(Integer.parseInt(iNumber));
                iOwnReport = getOwnReport(iOwnReport);
                if (!iOwnReports.contains(iOwnReport) && iOwnReport.getId() != -1) {
                    iOwnReports.add(iOwnReport);
                }
                if (SSOwnReportFrame.getInstance() != null) SSOwnReportFrame.getInstance().updateFrame();
                iOwnReport = null;
            } else if (iTriggerName.equals("EDITOWNREPORT") && iOwnReports!= null) {
                SSOwnReport iOwnReport = new SSOwnReport();
                iOwnReport.setId(Integer.parseInt(iNumber));
                iOwnReport = getOwnReport(iOwnReport);
                int iIndex = iOwnReports.lastIndexOf(iOwnReport);
                if(iIndex != -1){
                    iOwnReports.remove(iIndex);
                    iOwnReports.add(iIndex, iOwnReport);
                }
                else{
                    iOwnReports.add(iOwnReport);
                }
                iOwnReport = null;
                if (SSOwnReportFrame.getInstance() != null) SSOwnReportFrame.getInstance().updateFrame();
            } else if (iTriggerName.equals("DELETEOWNREPORT") && iOwnReports!= null) {
                SSOwnReport iOwnReport = new SSOwnReport();
                iOwnReport.setId(Integer.parseInt(iNumber));
                iOwnReports.remove(iOwnReport);
                iOwnReport = null;
                if (SSOwnReportFrame.getInstance() != null) SSOwnReportFrame.getInstance().updateFrame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<SSProduct> getProducts() {
        if (iProducts != null) {
            return iProducts;
        }
        iProducts = new LinkedList<SSProduct>();

        if(iCurrentCompany == null) return iProducts;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_product WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);
                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iProducts.add((SSProduct) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iProducts;
    }

    public SSProduct getProduct(SSProduct pProduct){
        if(pProduct == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_product WHERE number=? AND companyid=?");
            iStatement.setObject(1,pProduct.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSProduct iProduct = (SSProduct) iResultSet.getObject(3);
                iStatement.close();
                return iProduct;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public SSProduct getProduct(String iProductNumber){
        if(iProductNumber == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_product WHERE LOWER(number)=LOWER('" + iProductNumber + "') AND companyid=?");
            iStatement.setObject(1,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSProduct iProduct = (SSProduct) iResultSet.getObject(3);
                iStatement.close();
                return iProduct;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSProduct> getProducts(List<SSProduct> pProducts){
        if(pProducts == null) return null;
        List<SSProduct> iProducts = new LinkedList<SSProduct>();
        if (this.iProducts != null) {
            for (SSProduct iProduct : pProducts) {
                if (this.iProducts.contains(iProduct)) {
                    iProducts.add(iProduct);
                }
            }
            return iProducts;
        }
        if(iCurrentCompany == null) return iProducts;
        try {
            for(SSProduct iProduct : pProducts){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_product WHERE number=? AND companyid=?");
                iStatement.setObject(1,iProduct.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iProducts.add((SSProduct) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iProducts;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addProduct(SSProduct iProduct) {
        if(iProduct == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_product VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iProduct.getNumber());
            iStatement.setObject(2, iProduct);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateProduct(SSProduct iProduct) {
        if(iProduct == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_product SET product=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iProduct);
            iStatement.setObject(2, iProduct.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteProduct(SSProduct iProduct) {
        if(iProduct == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_product WHERE number=? AND companyid=?");
            iStatement.setObject(1, iProduct.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Returns the customers for the current company.
     *
     * @return  A List of customers or an empty list.
     */
        public List<SSCustomer> getCustomers() {
        if (iCustomers != null) {
            return iCustomers;
        }
        iCustomers = new LinkedList<SSCustomer>();
        if(iCurrentCompany == null) return iCustomers;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_customer WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);
                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iCustomers.add((SSCustomer) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iCustomers;
    }

    public SSCustomer getCustomer(SSCustomer pCustomer){
        if(pCustomer == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_customer WHERE number=? AND companyid=?");
            iStatement.setObject(1,pCustomer.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSCustomer iCustomer = (SSCustomer) iResultSet.getObject(3);
                iStatement.close();
                return iCustomer;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public SSCustomer getCustomer(String iCustomerNumber){
        if(iCustomerNumber == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_customer WHERE LOWER(number)=LOWER('" + iCustomerNumber + "') AND companyid=?");
            iStatement.setObject(1,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSCustomer iCustomer = (SSCustomer) iResultSet.getObject(3);
                iStatement.close();
                return iCustomer;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSCustomer> getCustomers(List<SSCustomer> pCustomers){
        if(pCustomers == null) return null;
        List<SSCustomer> iCustomers = new LinkedList<SSCustomer>();
        if (this.iCustomers != null) {
            for (SSCustomer iCustomer : pCustomers) {
                if (this.iCustomers.contains(iCustomer)) {
                    iCustomers.add(iCustomer);
                }
            }
            return iCustomers;
        }
        if(iCurrentCompany == null) return iCustomers;
        try {
            for(SSCustomer iCustomer : pCustomers){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_customer WHERE number=? AND companyid=?");
                iStatement.setObject(1,iCustomer.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iCustomers.add((SSCustomer) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iCustomers;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addCustomer(SSCustomer iCustomer) {
        if(iCustomer == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_customer VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iCustomer.getNumber());
            iStatement.setObject(2, iCustomer);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateCustomer(SSCustomer iCustomer) {
        if(iCustomer == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_customer SET customer=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iCustomer);
            iStatement.setObject(2, iCustomer.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteCustomer(SSCustomer iCustomer) {
        if(iCustomer == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_customer WHERE number=? AND companyid=?");
            iStatement.setObject(1, iCustomer.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the suppliers for the current company.
     *
     * @return  A List of suppliers or an empty list.
     */
    public List<SSSupplier> getSuppliers() {
        if (iSuppliers != null) {
            return iSuppliers;
        }
        iSuppliers = new LinkedList<SSSupplier>();
        if(iCurrentCompany == null) return iSuppliers;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_supplier WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);
                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iSuppliers.add((SSSupplier) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iSuppliers;
    }

    public SSSupplier getSupplier(SSSupplier pSupplier){
        if(pSupplier == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_supplier WHERE number=? AND companyid=?");
            iStatement.setObject(1,pSupplier.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSSupplier iSupplier = (SSSupplier) iResultSet.getObject(3);
                iStatement.close();
                return iSupplier;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSSupplier> getSuppliers(List<SSSupplier> pSuppliers){
        if(pSuppliers == null) return null;
        List<SSSupplier> iSuppliers = new LinkedList<SSSupplier>();
        if (this.iSuppliers != null) {
            for (SSSupplier iSupplier : pSuppliers) {
                if (this.iSuppliers.contains(iSupplier)) {
                    iSuppliers.add(iSupplier);
                }
            }
            return iSuppliers;
        }
        if(iCurrentCompany == null) return iSuppliers;
        try {
            for(SSSupplier iSupplier : pSuppliers){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_supplier WHERE number=? AND companyid=?");
                iStatement.setObject(1,iSupplier.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iSuppliers.add((SSSupplier) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iSuppliers;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addSupplier(SSSupplier iSupplier) {
        if(iSupplier == null ||iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_supplier VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iSupplier.getNumber());
            iStatement.setObject(2, iSupplier);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateSupplier(SSSupplier iSupplier) {
        if(iSupplier == null ||iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_supplier SET supplier=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iSupplier);
            iStatement.setObject(2, iSupplier.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteSupplier(SSSupplier iSupplier) {
        if(iSupplier == null ||iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_supplier WHERE number=? AND companyid=?");
            iStatement.setObject(1, iSupplier.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the autodistributions for the current company.
     *
     * @return  A List of autodists or an empty list.
     */
    public List<SSAutoDist> getAutoDists() {
        if (iAutoDists != null) {
            return iAutoDists;
        }
        iAutoDists = new LinkedList<SSAutoDist>();
        if(iCurrentCompany == null) return iAutoDists;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_autodist WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);
                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iAutoDists.add((SSAutoDist) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iAutoDists;
    }

    public SSAutoDist getAutoDist(SSAutoDist pAutoDist){
        if(pAutoDist == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_autodist WHERE number=? AND companyid=?");
            iStatement.setObject(1,pAutoDist.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSAutoDist iAutoDist = (SSAutoDist) iResultSet.getObject(3);
                iStatement.close();
                return iAutoDist;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSAutoDist> getAutoDists(List<SSAutoDist> pAutoDists){
        if(pAutoDists == null) return null;
        List<SSAutoDist> iAutoDists = new LinkedList<SSAutoDist>();
        if (this.iAutoDists != null) {
            for (SSAutoDist iAutoDist : pAutoDists) {
                if (this.iAutoDists.contains(iAutoDist)) {
                    iAutoDists.add(iAutoDist);
                }
            }
            return iAutoDists;
        }
        if(iCurrentCompany == null) return iAutoDists;
        try {
            for(SSAutoDist iAutoDist : pAutoDists){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_autodist WHERE number=? AND companyid=?");
                iStatement.setObject(1,iAutoDist.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iAutoDists.add((SSAutoDist) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iAutoDists;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addAutoDist(SSAutoDist iAutoDist) {
        if(iAutoDist == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_autodist VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iAutoDist.getNumber());
            iStatement.setObject(2, iAutoDist);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateAutoDist(SSAutoDist iAutoDist, SSAutoDist iOriginal) {
        if(iAutoDist == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_autodist SET autodist=?, number=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iAutoDist);
            iStatement.setObject(2, iAutoDist.getNumber());
            iStatement.setObject(3, iOriginal.getNumber());
            iStatement.setObject(4, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteAutoDist(SSAutoDist iAutoDist) {
        if(iAutoDist == null || iCurrentCompany == null) return;
        if(iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_autodist WHERE number=? AND companyid=?");
            iStatement.setObject(1, iAutoDist.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the tenders in the current company.
     *
     * @return  A List of tenders or an empty list.
     */
    public List<SSTender> getTenders() {
        if (iTenders != null) {
            return iTenders;
        }
        iTenders = new LinkedList<SSTender>();
        if(iCurrentCompany == null) return iTenders;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_tender WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iTenders.add((SSTender) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iTenders;
    }

    public SSTender getTender(SSTender pTender){
        if(pTender == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_tender WHERE number=? AND companyid=?");
            iStatement.setObject(1,pTender.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSTender iTender = (SSTender) iResultSet.getObject(3);
                iStatement.close();
                return iTender;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSTender> getTenders(List<SSTender> pTenders){
        if(pTenders == null) return null;
        List<SSTender> iTenders = new LinkedList<SSTender>();
        if (this.iTenders != null) {
            for (SSTender iTender : pTenders) {
                if (this.iTenders.contains(iTender)) {
                    iTenders.add(iTender);
                }
            }
            return iTenders;
        }
        if(iCurrentCompany == null) return iTenders;

        try {
            for(SSTender iTender : pTenders){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_tender WHERE number=? AND companyid=?");
                iStatement.setObject(1,iTender.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iTenders.add((SSTender) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iTenders;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addTender(SSTender iTender) {
        if(iTender == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_tender WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("tender");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iTender.setNumber(iNumber+1);
                } else {
                    iTender.setNumber(iCompanyNumber+1);
                }
            } else {
                iTender.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_tender VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iTender.getNumber());
            iStatement.setObject(2, iTender);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateTender(SSTender iTender) {
        if(iTender == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_tender SET tender=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iTender);
            iStatement.setObject(2, iTender.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteTender(SSTender iTender) {
        if(iTender == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_tender WHERE number=? AND companyid=?");
            iStatement.setObject(1, iTender.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////


    public List<SSOrder> getOrders() {
        if (iOrders != null) {
            return iOrders;
        }
        iOrders = new LinkedList<SSOrder>();
        if(iCurrentCompany == null) return iOrders;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_order WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iOrders.add((SSOrder) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iOrders;
    }

    public SSOrder getOrder(SSOrder pOrder){
        if(pOrder == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_order WHERE number=? AND companyid=?");
            iStatement.setObject(1,pOrder.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSOrder iOrder = (SSOrder) iResultSet.getObject(3);
                iStatement.close();
                return iOrder;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSOrder> getOrders(List<SSOrder> pOrders){
        if(pOrders == null) return null;
        List<SSOrder> iOrders = new LinkedList<SSOrder>();
        if (this.iOrders != null) {
            for (SSOrder iOrder : pOrders) {
                if (this.iOrders.contains(iOrder)) {
                    iOrders.add(iOrder);
                }
            }
            return iOrders;
        }
        if(iCurrentCompany == null) return iOrders;
        try {
            for(SSOrder iOrder : pOrders){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_order WHERE number=? AND companyid=?");
                iStatement.setObject(1,iOrder.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iOrders.add((SSOrder) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iOrders;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addOrder(SSOrder iOrder) {
        if(iOrder == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_order WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("order");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iOrder.setNumber(iNumber+1);
                } else {
                    iOrder.setNumber(iCompanyNumber+1);
                }
            } else {
                iOrder.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_order VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iOrder.getNumber());
            iStatement.setObject(2, iOrder);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateOrder(SSOrder iOrder) {
        if(iOrder == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_order SET iorder=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iOrder);
            iStatement.setObject(2, iOrder.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteOrder(SSOrder iOrder) {
        if(iOrder == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_order WHERE number=? AND companyid=?");
            iStatement.setObject(1, iOrder.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Returns the invoices in the current company.
     *
     * @return  A List of invoices or an empty list.
     */
    public List<SSInvoice> getInvoices() {
        if (iInvoices != null) {
            return iInvoices;
        }
        iInvoices = new LinkedList<SSInvoice>();
        if(iCurrentCompany == null) return iInvoices;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_invoice WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);
                iResultSet = iStatement.executeQuery();

                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iInvoices.add((SSInvoice) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iInvoices;
    }

    public SSInvoice getInvoice(SSInvoice pInvoice){
        if(pInvoice == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_invoice WHERE number=? AND companyid=?");
            iStatement.setObject(1,pInvoice.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSInvoice iInvoice = (SSInvoice) iResultSet.getObject(3);
                iStatement.close();
                return iInvoice;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSInvoice> getInvoices(List<SSInvoice> pInvoices){
        if(pInvoices == null) return null;
        List<SSInvoice> iInvoices = new LinkedList<SSInvoice>();
        if (this.iInvoices != null) {
            for (SSInvoice iInvoice : pInvoices) {
                if (this.iInvoices.contains(iInvoice)) {
                    iInvoices.add(iInvoice);
                }
            }
            return iInvoices;
        }
        if( iCurrentCompany == null) return iInvoices;
        try {
            for(SSInvoice iInvoice : pInvoices){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_invoice WHERE number=? AND companyid=?");
                iStatement.setObject(1,iInvoice.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iInvoices.add((SSInvoice) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iInvoices;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addInvoice(SSInvoice iInvoice) {
        if(iInvoice == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_invoice WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("invoice");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iInvoice.setNumber(iNumber+1);
                } else {
                    iInvoice.setNumber(iCompanyNumber+1);
                }
            } else {
                iInvoice.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_invoice VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iInvoice.getNumber());
            iStatement.setObject(2, iInvoice);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateInvoice(SSInvoice iInvoice) {
        if(iInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_invoice SET invoice=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iInvoice);
            iStatement.setObject(2, iInvoice.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteInvoice(SSInvoice iInvoice) {
        if(iInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_invoice WHERE number=? AND companyid=?");
            iStatement.setObject(1, iInvoice.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Returns the inpayments in the current company.
     *
     * @return  A List of invoices or an empty list.
     */
    public List<SSInpayment> getInpayments() {
        if (iInpayments != null) {
            return iInpayments;
        }
        iInpayments = new LinkedList<SSInpayment>();
        if(iCurrentCompany == null) return iInpayments;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_inpayment WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iInpayments.add((SSInpayment) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iInpayments;
    }

    public SSInpayment getInpayment(SSInpayment pInpayment){
        if(pInpayment == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_inpayment WHERE number=? AND companyid=?");
            iStatement.setObject(1,pInpayment.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSInpayment iInpayment = (SSInpayment) iResultSet.getObject(3);
                iStatement.close();
                return iInpayment;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addInpayment(SSInpayment iInpayment) {
        if(iInpayment == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_inpayment WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("inpayment");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iInpayment.setNumber(iNumber+1);
                } else {
                    iInpayment.setNumber(iCompanyNumber+1);
                }
            } else {
                iInpayment.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_inpayment VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iInpayment.getNumber());
            iStatement.setObject(2, iInpayment);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateInpayment(SSInpayment iInpayment) {
        if(iInpayment == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_inpayment SET inpayment=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iInpayment);
            iStatement.setObject(2, iInpayment.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteInpayment(SSInpayment iInpayment) {
        if(iInpayment == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_inpayment WHERE number=? AND companyid=?");
            iStatement.setObject(1, iInpayment.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


    /**
     * Returns the outpayments in the current company.
     *
     * @return  A List of outpayments or an empty list.
     */
    public List<SSOutpayment> getOutpayments() {
        if (iOutpayments != null) {
            return iOutpayments;
        }
        iOutpayments = new LinkedList<SSOutpayment>();
        if(iCurrentCompany == null) return iOutpayments;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_outpayment WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iOutpayments.add((SSOutpayment) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iOutpayments;
    }

    public SSOutpayment getOutpayment(SSOutpayment pOutpayment){
        if(pOutpayment == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_outpayment WHERE number=? AND companyid=?");
            iStatement.setObject(1,pOutpayment.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSOutpayment iOutpayment = (SSOutpayment) iResultSet.getObject(3);
                iStatement.close();
                return iOutpayment;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addOutpayment(SSOutpayment iOutpayment) {
        if(iOutpayment == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_outpayment WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("outpayment");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iOutpayment.setNumber(iNumber+1);
                } else {
                    iOutpayment.setNumber(iCompanyNumber+1);
                }
            } else {
                iOutpayment.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_outpayment VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iOutpayment.getNumber());
            iStatement.setObject(2, iOutpayment);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateOutpayment(SSOutpayment iOutpayment) {
        if(iOutpayment == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_outpayment SET outpayment=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iOutpayment);
            iStatement.setObject(2, iOutpayment.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteOutpayment(SSOutpayment iOutpayment) {
        if(iOutpayment == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_outpayment WHERE number=? AND companyid=?");
            iStatement.setObject(1, iOutpayment.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the credit invoices in the current company.
     *
     * @return  A List of invoices or an empty list.
     */
    public List<SSCreditInvoice> getCreditInvoices() {
        if (iCreditInvoices != null) {
            return iCreditInvoices;
        }
        iCreditInvoices = new LinkedList<SSCreditInvoice>();
        if(iCurrentCompany == null) return iCreditInvoices;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_creditinvoice WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iCreditInvoices.add((SSCreditInvoice) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iCreditInvoices;
    }

    public SSCreditInvoice getCreditInvoice(SSCreditInvoice pCreditInvoice){
        if(pCreditInvoice == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_creditinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1,pCreditInvoice.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSCreditInvoice iCreditInvoice = (SSCreditInvoice) iResultSet.getObject(3);
                iStatement.close();
                return iCreditInvoice;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSCreditInvoice> getCreditInvoices(List<SSCreditInvoice> pCreditInvoices){
        if(pCreditInvoices == null) return null;
        List<SSCreditInvoice> iCreditInvoices = new LinkedList<SSCreditInvoice>();
        if (this.iCreditInvoices != null) {
            for (SSCreditInvoice iCreditInvoice : pCreditInvoices) {
                if (this.iCreditInvoices.contains(iCreditInvoice)) {
                    iCreditInvoices.add(iCreditInvoice);
                }
            }
            return iCreditInvoices;
        }
        if(iCurrentCompany == null) return iCreditInvoices;
        try {
            for(SSCreditInvoice iCreditInvoice : pCreditInvoices){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_creditinvoice WHERE number=? AND companyid=?");
                iStatement.setObject(1,iCreditInvoice.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iCreditInvoices.add((SSCreditInvoice) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iCreditInvoices;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addCreditInvoice(SSCreditInvoice iCreditInvoice) {
        if(iCreditInvoice == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_creditinvoice WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("creditinvoice");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iCreditInvoice.setNumber(iNumber+1);
                } else {
                    iCreditInvoice.setNumber(iCompanyNumber+1);
                }
            } else {
                iCreditInvoice.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_creditinvoice VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iCreditInvoice.getNumber());
            iStatement.setObject(2, iCreditInvoice);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateCreditInvoice(SSCreditInvoice iCreditInvoice) {
        if(iCreditInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_creditinvoice SET creditinvoice=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iCreditInvoice);
            iStatement.setObject(2, iCreditInvoice.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteCreditInvoice(SSCreditInvoice iCreditInvoice) {
        if(iCreditInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_creditinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1, iCreditInvoice.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }



////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Returns the periodic invoices in the current company.
     *
     * @return  A List of periodic invoices or an empty list.
     */
    public List<SSPeriodicInvoice> getPeriodicInvoices() {
        if (iPeriodicInvoices != null) {
            return iPeriodicInvoices;
        }
        iPeriodicInvoices = new LinkedList<SSPeriodicInvoice>();
        if(iCurrentCompany == null) return iPeriodicInvoices;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_periodicinvoice WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iPeriodicInvoices.add((SSPeriodicInvoice) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iPeriodicInvoices;
    }

    public SSPeriodicInvoice getPeriodicInvoice(SSPeriodicInvoice pPeriodicInvoice){
        if(pPeriodicInvoice == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_periodicinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1,pPeriodicInvoice.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSPeriodicInvoice iPeriodicInvoice = (SSPeriodicInvoice) iResultSet.getObject(3);
                iStatement.close();
                return iPeriodicInvoice;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addPeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        if(iPeriodicInvoice == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_periodicinvoice WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("periodicinvoice");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iPeriodicInvoice.setNumber(iNumber+1);
                } else {
                    iPeriodicInvoice.setNumber(iCompanyNumber+1);
                }
            } else {
                iPeriodicInvoice.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_periodicinvoice VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iPeriodicInvoice.getNumber());
            iStatement.setObject(2, iPeriodicInvoice);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updatePeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        if(iPeriodicInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_periodicinvoice SET periodicinvoice=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iPeriodicInvoice);
            iStatement.setObject(2, iPeriodicInvoice.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deletePeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        if(iPeriodicInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_periodicinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1, iPeriodicInvoice.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Returns the purchase orders in the current company.
     *
     * @return  A List of orders or an empty list.
     */
    public List<SSPurchaseOrder> getPurchaseOrders() {
        if (iPurchaseOrders != null) {
            return iPurchaseOrders;
        }
        iPurchaseOrders = new LinkedList<SSPurchaseOrder>();
        if(iCurrentCompany == null) return iPurchaseOrders;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_purchaseorder WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iPurchaseOrders.add((SSPurchaseOrder) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iPurchaseOrders;
    }

    public SSPurchaseOrder getPurchaseOrder(SSPurchaseOrder pPurchaseOrder){
        if(pPurchaseOrder == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_purchaseorder WHERE number=? AND companyid=?");
            iStatement.setObject(1,pPurchaseOrder.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSPurchaseOrder iPurchaseOrder = (SSPurchaseOrder) iResultSet.getObject(3);
                iStatement.close();
                return iPurchaseOrder;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSPurchaseOrder> getPurchaseOrders(List<SSPurchaseOrder> pPurchaseOrders){
        if(pPurchaseOrders == null) return null;
        List<SSPurchaseOrder> iPurchaseOrders = new LinkedList<SSPurchaseOrder>();
        if (this.iPurchaseOrders != null) {
            for (SSPurchaseOrder iPurchaseOrder : pPurchaseOrders) {
                if (this.iPurchaseOrders.contains(iPurchaseOrder)) {
                    iPurchaseOrders.add(iPurchaseOrder);
                }
            }
            return iPurchaseOrders;
        }
        if(iCurrentCompany == null) return iPurchaseOrders;
        try {
            for(SSPurchaseOrder iPurchaseOrder : pPurchaseOrders){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_purchaseorder WHERE number=? AND companyid=?");
                iStatement.setObject(1,iPurchaseOrder.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iPurchaseOrders.add((SSPurchaseOrder) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iPurchaseOrders;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addPurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        if(iPurchaseOrder == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_purchaseorder WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("purchaseorder");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iPurchaseOrder.setNumber(iNumber+1);
                } else {
                    iPurchaseOrder.setNumber(iCompanyNumber+1);
                }
            } else {
                iPurchaseOrder.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_purchaseorder VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iPurchaseOrder.getNumber());
            iStatement.setObject(2, iPurchaseOrder);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updatePurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        if(iPurchaseOrder == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_purchaseorder SET purchaseorder=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iPurchaseOrder);
            iStatement.setObject(2, iPurchaseOrder.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deletePurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        if(iPurchaseOrder == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_purchaseorder WHERE number=? AND companyid=?");
            iStatement.setObject(1, iPurchaseOrder.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the supplier invoices in the current company.
     *
     * @return  A List of invoices or an empty list.
     */
    public List<SSSupplierInvoice> getSupplierInvoices() {
        if (iSupplierInvoices != null) {
            return iSupplierInvoices;
        }
        iSupplierInvoices = new LinkedList<SSSupplierInvoice>();
        if(iCurrentCompany == null) return iSupplierInvoices;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_supplierinvoice WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iSupplierInvoices.add((SSSupplierInvoice) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iSupplierInvoices;
    }

    public SSSupplierInvoice getSupplierInvoice(SSSupplierInvoice pSupplierInvoice){
        if(pSupplierInvoice == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_supplierinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1,pSupplierInvoice.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSSupplierInvoice iSupplierInvoice = (SSSupplierInvoice) iResultSet.getObject(3);
                iStatement.close();
                return iSupplierInvoice;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSSupplierInvoice> getSupplierInvoices(List<SSSupplierInvoice> pSupplierInvoices){
        if(pSupplierInvoices == null) return null;
        List<SSSupplierInvoice> iSupplierInvoices = new LinkedList<SSSupplierInvoice>();
        if (this.iSupplierInvoices != null) {
            for (SSSupplierInvoice iSupplierInvoice : pSupplierInvoices) {
                if (this.iSupplierInvoices.contains(iSupplierInvoice)) {
                    iSupplierInvoices.add(iSupplierInvoice);
                }
            }
            return iSupplierInvoices;
        }
        if(iCurrentCompany == null) return iSupplierInvoices;
        try {
            for(SSSupplierInvoice iSupplierInvoice : pSupplierInvoices){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_supplierinvoice WHERE number=? AND companyid=?");
                iStatement.setObject(1,iSupplierInvoice.getNumber());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iSupplierInvoices.add((SSSupplierInvoice) iResultSet.getObject(3));
                }
                iStatement.close();
            }

            return iSupplierInvoices;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addSupplierInvoice(SSSupplierInvoice iSupplierInvoice) {
        if(iSupplierInvoice == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_supplierinvoice WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("supplierinvoice");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iSupplierInvoice.setNumber(iNumber+1);
                } else {
                    iSupplierInvoice.setNumber(iCompanyNumber+1);
                }
            } else {
                iSupplierInvoice.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_supplierinvoice VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iSupplierInvoice.getNumber());
            iStatement.setObject(2, iSupplierInvoice);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateSupplierInvoice(SSSupplierInvoice iSupplierInvoice) {
        if(iSupplierInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_supplierinvoice SET supplierinvoice=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iSupplierInvoice);
            iStatement.setObject(2, iSupplierInvoice.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteSupplierInvoice(SSSupplierInvoice iSupplierInvoice) {
        if(iSupplierInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_supplierinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1, iSupplierInvoice.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the credit invoices in the current company.
     *
     * @return  A List of invoices or an empty list.
     */
    public List<SSSupplierCreditInvoice> getSupplierCreditInvoices() {
        if (iSupplierCreditInvoices != null) {
            return iSupplierCreditInvoices;
        }
        iSupplierCreditInvoices = new LinkedList<SSSupplierCreditInvoice>();
        if(iCurrentCompany == null) return iSupplierCreditInvoices;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_suppliercreditinvoice WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iSupplierCreditInvoices.add((SSSupplierCreditInvoice) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iSupplierCreditInvoices;
    }

    public SSSupplierCreditInvoice getSupplierCreditInvoice(SSSupplierCreditInvoice pSupplierCreditInvoice){
        if(pSupplierCreditInvoice == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_suppliercreditinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1,pSupplierCreditInvoice.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = (SSSupplierCreditInvoice) iResultSet.getObject(3);
                iStatement.close();
                return iSupplierCreditInvoice;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addSupplierCreditInvoice(SSSupplierCreditInvoice iSupplierCreditInvoice) {
        if(iSupplierCreditInvoice == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_suppliercreditinvoice WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("suppliercreditinvoice");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iSupplierCreditInvoice.setNumber(iNumber+1);
                } else {
                    iSupplierCreditInvoice.setNumber(iCompanyNumber+1);
                }
            } else {
                iSupplierCreditInvoice.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_suppliercreditinvoice VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iSupplierCreditInvoice.getNumber());
            iStatement.setObject(2, iSupplierCreditInvoice);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateSupplierCreditInvoice(SSSupplierCreditInvoice iSupplierCreditInvoice) {
        if(iSupplierCreditInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_suppliercreditinvoice SET suppliercreditinvoice=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iSupplierCreditInvoice);
            iStatement.setObject(2, iSupplierCreditInvoice.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteSupplierCreditInvoice(SSSupplierCreditInvoice iSupplierCreditInvoice) {
        if(iSupplierCreditInvoice == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_suppliercreditinvoice WHERE number=? AND companyid=?");
            iStatement.setObject(1, iSupplierCreditInvoice.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSInventory> getInventories() {
        if (iInventories != null) {
            return iInventories;
        }
        iInventories = new LinkedList<SSInventory>();
        if(iCurrentCompany == null) return iInventories;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_inventory WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iInventories.add((SSInventory) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iInventories;
    }

    public SSInventory getInventory(SSInventory pInventory){
        if(pInventory == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_inventory WHERE number=? AND companyid=?");
            iStatement.setObject(1,pInventory.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSInventory iInventory = (SSInventory) iResultSet.getObject(3);
                iStatement.close();
                return iInventory;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addInventory(SSInventory iInventory) {
        if(iInventory == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_inventory WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("inventory");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iInventory.setNumber(iNumber+1);
                } else {
                    iInventory.setNumber(iCompanyNumber+1);
                }
            } else {
                iInventory.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_inventory VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iInventory.getNumber());
            iStatement.setObject(2, iInventory);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateInventory(SSInventory iInventory) {
        if(iInventory == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_inventory SET inventory=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iInventory);
            iStatement.setObject(2, iInventory.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteInventory(SSInventory iInventory) {
        if(iInventory == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_inventory WHERE number=? AND companyid=?");
            iStatement.setObject(1, iInventory.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSIndelivery> getIndeliveries() {
        if (iIndeliveries != null) {
            return iIndeliveries;
        }
        iIndeliveries = new LinkedList<SSIndelivery>();
        if(iCurrentCompany == null) return iIndeliveries;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_indelivery WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iIndeliveries.add((SSIndelivery) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iIndeliveries;
    }

    public SSIndelivery getIndelivery(SSIndelivery pIndelivery){
        if(pIndelivery == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_indelivery WHERE number=? AND companyid=?");
            iStatement.setObject(1,pIndelivery.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSIndelivery iIndelivery = (SSIndelivery) iResultSet.getObject(3);
                iStatement.close();
                return iIndelivery;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addIndelivery(SSIndelivery iIndelivery) {
        if(iIndelivery == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_indelivery WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("indelivery");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iIndelivery.setNumber(iNumber+1);
                } else {
                    iIndelivery.setNumber(iCompanyNumber+1);
                }
            } else {
                iIndelivery.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_indelivery VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iIndelivery.getNumber());
            iStatement.setObject(2, iIndelivery);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateIndelivery(SSIndelivery iIndelivery) {
        if(iIndelivery == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_indelivery SET indelivery=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iIndelivery);
            iStatement.setObject(2, iIndelivery.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteIndelivery(SSIndelivery iIndelivery) {
        if(iIndelivery == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_indelivery WHERE number=? AND companyid=?");
            iStatement.setObject(1, iIndelivery.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSOutdelivery> getOutdeliveries() {
        if (iOutdeliveries != null) {
            return iOutdeliveries;
        }
        iOutdeliveries = new LinkedList<SSOutdelivery>();
        if(iCurrentCompany == null) return iOutdeliveries;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_outdelivery WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);

                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iOutdeliveries.add((SSOutdelivery) iResultSet.getObject(3));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iOutdeliveries;
    }

    public SSOutdelivery getOutdelivery(SSOutdelivery pOutdelivery){
        if(pOutdelivery == null || iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_outdelivery WHERE number=? AND companyid=?");
            iStatement.setObject(1,pOutdelivery.getNumber());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSOutdelivery iOutdelivery = (SSOutdelivery) iResultSet.getObject(3);
                iStatement.close();
                return iOutdelivery;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addOutdelivery(SSOutdelivery iOutdelivery) {
        if(iOutdelivery == null || iCurrentCompany == null) return;
        try {
            LockDatabase();
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT MAX(number) AS maxnum FROM tbl_outdelivery WHERE companyid=?");
            iStatement.setObject(1, iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            Integer iCompanyNumber = getCurrentCompany().getAutoIncrement().getNumber("outdelivery");

            if (iResultSet.next()) {
                Integer iNumber = iResultSet.getInt("maxnum");
                if (iNumber > iCompanyNumber) {
                    iOutdelivery.setNumber(iNumber+1);
                } else {
                    iOutdelivery.setNumber(iCompanyNumber+1);
                }
            } else {
                iOutdelivery.setNumber(iCompanyNumber+1);
            }
            iResultSet.close();
            iStatement.close();

            iStatement = iConnection.prepareStatement("INSERT INTO tbl_outdelivery VALUES(NULL,?,?,?)");
            iStatement.setObject(1, iOutdelivery.getNumber());
            iStatement.setObject(2, iOutdelivery);
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
            UnlockDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            UnlockDatabase();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateOutdelivery(SSOutdelivery iOutdelivery) {
        if(iOutdelivery == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_outdelivery SET outdelivery=? WHERE number=? AND companyid=?");
            iStatement.setObject(1, iOutdelivery);
            iStatement.setObject(2, iOutdelivery.getNumber());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteOutdelivery(SSOutdelivery iOutdelivery) {
        if(iOutdelivery == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_outdelivery WHERE number=? AND companyid=?");
            iStatement.setObject(1, iOutdelivery.getNumber());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    public List<SSOwnReport> getOwnReports() {
        if (iOwnReports != null) {
            return iOwnReports;
        }
        iOwnReports = new LinkedList<SSOwnReport>();

        if(iCurrentCompany == null) return iOwnReports;
        try {
            Integer iMax = -1;
            ResultSet iResultSet;
            PreparedStatement iStatement;
            while (true) {
                iStatement = iConnection.prepareStatement("SELECT * FROM tbl_ownreport WHERE companyid=? AND id>?");
                iStatement.setObject(1, iCurrentCompany.getId());
                iStatement.setObject(2, iMax);
                iStatement.setMaxRows(1024);
                iResultSet = iStatement.executeQuery();
                int i = 0;
                while (iResultSet.next()) {
                    iMax = iResultSet.getInt(1);
                    iOwnReports.add((SSOwnReport) iResultSet.getObject(2));
                    i++;
                }
                if (i != 1024)
                    break;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return iOwnReports;
    }

    public SSOwnReport getOwnReport(SSOwnReport pOwnReport){
        if(pOwnReport == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_ownreport WHERE id=? AND companyid=?");
            iStatement.setObject(1,pOwnReport.getId());
            iStatement.setObject(2,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSOwnReport iOwnReport = (SSOwnReport) iResultSet.getObject(2);
                iStatement.close();
                return iOwnReport;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public SSOwnReport getOwnReport(Integer iOwnReportNumber){
        if(iOwnReportNumber == null) return null;
        if(iCurrentCompany == null) return null;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_ownreport WHERE id=" + iOwnReportNumber + " AND companyid=?");
            iStatement.setObject(1,iCurrentCompany.getId());
            ResultSet iResultSet = iStatement.executeQuery();

            if (iResultSet.next()) {
                SSOwnReport iOwnReport = (SSOwnReport) iResultSet.getObject(2);
                iStatement.close();
                return iOwnReport;
            }
            iResultSet.close();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public List<SSOwnReport> getOwnReports(List<SSOwnReport> pOwnReports){
        if(pOwnReports == null) return null;
        List<SSOwnReport> iOwnReports = new LinkedList<SSOwnReport>();
        if (this.iOwnReports != null) {
            for (SSOwnReport iOwnReport : pOwnReports) {
                if (this.iOwnReports.contains(iOwnReport)) {
                    iOwnReports.add(iOwnReport);
                }
            }
            return iOwnReports;
        }
        if(iCurrentCompany == null) return iOwnReports;
        try {
            for(SSOwnReport iOwnReport : pOwnReports){
                PreparedStatement iStatement = iConnection.prepareStatement("SELECT * FROM tbl_ownreport WHERE id=? AND companyid=?");
                iStatement.setObject(1,iOwnReport.getId());
                iStatement.setObject(2,iCurrentCompany.getId());
                ResultSet iResultSet = iStatement.executeQuery();
                if(iResultSet.next()) {
                    iOwnReports.add((SSOwnReport) iResultSet.getObject(2));
                }
                iStatement.close();
            }

            return iOwnReports;
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
        return null;
    }

    public void addOwnReport(SSOwnReport iOwnReport) {
        if(iOwnReport == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("INSERT INTO tbl_ownreport VALUES(NULL,?,?)");
            iStatement.setObject(1, iOwnReport);
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            iStatement = iConnection.prepareStatement("SELECT * FROM tbl_ownreport");
            ResultSet iResultSet = iStatement.executeQuery();
            Integer iId = -1;
            while(iResultSet.next()){
                if(iResultSet.isLast())
                    iId = iResultSet.getInt("id");
            }
            iResultSet.close();
            iStatement.close();
            iOwnReport.setId(iId);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iStatement = iConnection.prepareStatement("UPDATE tbl_ownreport SET ownreport=? WHERE id=?");
            iStatement.setObject(1, iOwnReport);
            iStatement.setObject(2, iOwnReport.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void updateOwnReport(SSOwnReport iOwnReport) {
        if(iOwnReport == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("UPDATE tbl_ownreport SET ownreport=? WHERE id=? AND companyid=?");
            iStatement.setObject(1, iOwnReport);
            iStatement.setObject(2, iOwnReport.getId());
            iStatement.setObject(3, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    public void deleteOwnReport(SSOwnReport iOwnReport) {
        if(iOwnReport == null || iCurrentCompany == null) return;
        try {
            PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_ownreport WHERE id=? AND companyid=?");
            iStatement.setObject(1, iOwnReport.getId());
            iStatement.setObject(2, iCurrentCompany.getId());
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try { iConnection.rollback(); } catch (SQLException ignored) {}
            SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////


    public void createServerTriggers() {

        try {
            PreparedStatement iStatement = iConnection.prepareStatement(
                "CREATE TRIGGER NEWPROJECT  AFTER INSERT ON tbl_project FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITPROJECT  AFTER UPDATE ON tbl_project FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEPROJECT  AFTER DELETE ON tbl_project FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWRESULTUNIT  AFTER INSERT ON tbl_resultunit FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITRESULTUNIT  AFTER UPDATE ON tbl_resultunit FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETERESULTUNIT  AFTER DELETE ON tbl_resultunit FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWPRODUCT  AFTER INSERT ON tbl_product FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITPRODUCT  AFTER UPDATE ON tbl_product FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEPRODUCT  AFTER DELETE ON tbl_product FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWCUSTOMER  AFTER INSERT ON tbl_customer FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITCUSTOMER  AFTER UPDATE ON tbl_customer FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETECUSTOMER  AFTER DELETE ON tbl_customer FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWSUPPLIER  AFTER INSERT ON tbl_supplier FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITSUPPLIER  AFTER UPDATE ON tbl_supplier FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETESUPPLIER  AFTER DELETE ON tbl_supplier FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWVOUCHERTEMPLATE  AFTER INSERT ON tbl_vouchertemplate FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEVOUCHERTEMPLATE  AFTER DELETE ON tbl_vouchertemplate FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWAUTODIST  AFTER INSERT ON tbl_autodist FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITAUTODIST  AFTER UPDATE ON tbl_autodist FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEAUTODIST  AFTER DELETE ON tbl_autodist FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWINPAYMENT  AFTER INSERT ON tbl_inpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITINPAYMENT  AFTER UPDATE ON tbl_inpayment FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEINPAYMENT  AFTER DELETE ON tbl_inpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWTENDER  AFTER INSERT ON tbl_tender FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITTENDER  AFTER UPDATE ON tbl_tender FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETETENDER  AFTER DELETE ON tbl_tender FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWORDER  AFTER INSERT ON tbl_order FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITORDER  AFTER UPDATE ON tbl_order FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEORDER  AFTER DELETE ON tbl_order FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWINVOICE  AFTER INSERT ON tbl_invoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITINVOICE  AFTER UPDATE ON tbl_invoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEINVOICE  AFTER DELETE ON tbl_invoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWCREDITINVOICE  AFTER INSERT ON tbl_creditinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITCREDITINVOICE  AFTER UPDATE ON tbl_creditinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETECREDITINVOICE  AFTER DELETE ON tbl_creditinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWPERIODICINVOICE  AFTER INSERT ON tbl_periodicinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITPERIODICINVOICE  AFTER UPDATE ON tbl_periodicinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEPERIODICINVOICE  AFTER DELETE ON tbl_periodicinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWOUTPAYMENT  AFTER INSERT ON tbl_outpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITOUTPAYMENT  AFTER UPDATE ON tbl_outpayment FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEOUTPAYMENT  AFTER DELETE ON tbl_outpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWPURCHASEORDER  AFTER INSERT ON tbl_purchaseorder FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITPURCHASEORDER  AFTER UPDATE ON tbl_purchaseorder FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEPURCHASEORDER  AFTER DELETE ON tbl_purchaseorder FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWSUPPLIERINVOICE  AFTER INSERT ON tbl_supplierinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITSUPPLIERINVOICE  AFTER UPDATE ON tbl_supplierinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETESUPPLIERINVOICE  AFTER DELETE ON tbl_supplierinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWSUPPLIERCREDITINVOICE  AFTER INSERT ON tbl_suppliercreditinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITSUPPLIERCREDITINVOICE  AFTER UPDATE ON tbl_suppliercreditinvoice FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETESUPPLIERCREDITINVOICE  AFTER DELETE ON tbl_suppliercreditinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWINVENTORY  AFTER INSERT ON tbl_inventory FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITINVENTORY  AFTER UPDATE ON tbl_inventory FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEINVENTORY  AFTER DELETE ON tbl_inventory FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWINDELIVERY  AFTER INSERT ON tbl_indelivery FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITINDELIVERY  AFTER UPDATE ON tbl_indelivery FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEINDELIVERY  AFTER DELETE ON tbl_indelivery FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWOUTDELIVERY  AFTER INSERT ON tbl_outdelivery FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITOUTDELIVERY  AFTER UPDATE ON tbl_outdelivery FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEOUTDELIVERY  AFTER DELETE ON tbl_outdelivery FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWVOUCHER  AFTER INSERT ON tbl_voucher FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITVOUCHER  AFTER UPDATE ON tbl_voucher FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEVOUCHER  AFTER DELETE ON tbl_voucher FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER NEWOWNREPORT  AFTER INSERT ON tbl_ownreport FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER EDITOWNREPORT  AFTER UPDATE ON tbl_ownreport FOR EACH ROW CALL \"se.swedsoft.SSServer\";"+
                "CREATE TRIGGER DELETEOWNREPORT  AFTER DELETE ON tbl_ownreport FOR EACH ROW CALL \"se.swedsoft.SSServer\";");

            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            //System.out.println("Triggers fanns redan vi remote tilläggning");
            //e.printStackTrace();
        }
    }

    public void createLocalTriggers() {

        try {
            PreparedStatement iStatement = iConnection.prepareStatement(
                "CREATE TRIGGER NEWPROJECT  AFTER INSERT ON tbl_project FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITPROJECT  AFTER UPDATE ON tbl_project FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEPROJECT  AFTER DELETE ON tbl_project FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWRESULTUNIT  AFTER INSERT ON tbl_resultunit FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITRESULTUNIT  AFTER UPDATE ON tbl_resultunit FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETERESULTUNIT  AFTER DELETE ON tbl_resultunit FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWPRODUCT  AFTER INSERT ON tbl_product FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITPRODUCT  AFTER UPDATE ON tbl_product FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEPRODUCT  AFTER DELETE ON tbl_product FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWCUSTOMER  AFTER INSERT ON tbl_customer FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITCUSTOMER  AFTER UPDATE ON tbl_customer FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETECUSTOMER  AFTER DELETE ON tbl_customer FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWSUPPLIER  AFTER INSERT ON tbl_supplier FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITSUPPLIER  AFTER UPDATE ON tbl_supplier FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETESUPPLIER  AFTER DELETE ON tbl_supplier FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWVOUCHERTEMPLATE  AFTER INSERT ON tbl_vouchertemplate FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEVOUCHERTEMPLATE  AFTER DELETE ON tbl_vouchertemplate FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWAUTODIST  AFTER INSERT ON tbl_autodist FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITAUTODIST  AFTER UPDATE ON tbl_autodist FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEAUTODIST  AFTER DELETE ON tbl_autodist FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWINPAYMENT  AFTER INSERT ON tbl_inpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITINPAYMENT  AFTER UPDATE ON tbl_inpayment FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEINPAYMENT  AFTER DELETE ON tbl_inpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWTENDER  AFTER INSERT ON tbl_tender FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITTENDER  AFTER UPDATE ON tbl_tender FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETETENDER  AFTER DELETE ON tbl_tender FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWORDER  AFTER INSERT ON tbl_order FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITORDER  AFTER UPDATE ON tbl_order FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEORDER  AFTER DELETE ON tbl_order FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWINVOICE  AFTER INSERT ON tbl_invoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITINVOICE  AFTER UPDATE ON tbl_invoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEINVOICE  AFTER DELETE ON tbl_invoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWCREDITINVOICE  AFTER INSERT ON tbl_creditinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITCREDITINVOICE  AFTER UPDATE ON tbl_creditinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETECREDITINVOICE  AFTER DELETE ON tbl_creditinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWPERIODICINVOICE  AFTER INSERT ON tbl_periodicinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITPERIODICINVOICE  AFTER UPDATE ON tbl_periodicinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEPERIODICINVOICE  AFTER DELETE ON tbl_periodicinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWOUTPAYMENT  AFTER INSERT ON tbl_outpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITOUTPAYMENT  AFTER UPDATE ON tbl_outpayment FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEOUTPAYMENT  AFTER DELETE ON tbl_outpayment FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWPURCHASEORDER  AFTER INSERT ON tbl_purchaseorder FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITPURCHASEORDER  AFTER UPDATE ON tbl_purchaseorder FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEPURCHASEORDER  AFTER DELETE ON tbl_purchaseorder FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWSUPPLIERINVOICE  AFTER INSERT ON tbl_supplierinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITSUPPLIERINVOICE  AFTER UPDATE ON tbl_supplierinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETESUPPLIERINVOICE  AFTER DELETE ON tbl_supplierinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWSUPPLIERCREDITINVOICE  AFTER INSERT ON tbl_suppliercreditinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITSUPPLIERCREDITINVOICE  AFTER UPDATE ON tbl_suppliercreditinvoice FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETESUPPLIERCREDITINVOICE  AFTER DELETE ON tbl_suppliercreditinvoice FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWINVENTORY  AFTER INSERT ON tbl_inventory FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITINVENTORY  AFTER UPDATE ON tbl_inventory FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEINVENTORY  AFTER DELETE ON tbl_inventory FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWINDELIVERY  AFTER INSERT ON tbl_indelivery FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITINDELIVERY  AFTER UPDATE ON tbl_indelivery FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEINDELIVERY  AFTER DELETE ON tbl_indelivery FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWOUTDELIVERY  AFTER INSERT ON tbl_outdelivery FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITOUTDELIVERY  AFTER UPDATE ON tbl_outdelivery FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEOUTDELIVERY  AFTER DELETE ON tbl_outdelivery FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWVOUCHER  AFTER INSERT ON tbl_voucher FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITVOUCHER  AFTER UPDATE ON tbl_voucher FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEVOUCHER  AFTER DELETE ON tbl_voucher FOR EACH ROW QUEUE 10000 CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER NEWOWNREPORT  AFTER INSERT ON tbl_ownreport FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER EDITOWNREPORT  AFTER UPDATE ON tbl_ownreport FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";"+
                "CREATE TRIGGER DELETEOWNREPORT  AFTER DELETE ON tbl_ownreport FOR EACH ROW CALL \"se.swedsoft.bookkeeping.SSTriggerHandler\";");
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            //System.out.println("Triggers fanns redan vi lokal tilläggning");
            //e.printStackTrace();
        }
    }

    public void createTriggers() {
        if (iLocking) {
            createServerTriggers();
        } else {
            createLocalTriggers();
        }
    }
    public void dropTriggers() {

        try {
            PreparedStatement iStatement = iConnection.prepareStatement(
                "DROP TRIGGER NEWPROJECT;"+
                "DROP TRIGGER EDITPROJECT;"+
                "DROP TRIGGER DELETEPROJECT;"+
                "DROP TRIGGER NEWRESULTUNIT;"+
                "DROP TRIGGER EDITRESULTUNIT;"+
                "DROP TRIGGER DELETERESULTUNIT;"+
                "DROP TRIGGER NEWPRODUCT;"+
                "DROP TRIGGER EDITPRODUCT;"+
                "DROP TRIGGER DELETEPRODUCT;"+
                "DROP TRIGGER NEWCUSTOMER;"+
                "DROP TRIGGER EDITCUSTOMER;"+
                "DROP TRIGGER DELETECUSTOMER;"+
                "DROP TRIGGER NEWSUPPLIER;"+
                "DROP TRIGGER EDITSUPPLIER;"+
                "DROP TRIGGER DELETESUPPLIER;"+
                "DROP TRIGGER NEWVOUCHERTEMPLATE;"+
                "DROP TRIGGER DELETEVOUCHERTEMPLATE;"+
                "DROP TRIGGER NEWAUTODIST;"+
                "DROP TRIGGER EDITAUTODIST;"+
                "DROP TRIGGER DELETEAUTODIST;"+
                "DROP TRIGGER NEWINPAYMENT;"+
                "DROP TRIGGER EDITINPAYMENT;"+
                "DROP TRIGGER DELETEINPAYMENT;"+
                "DROP TRIGGER NEWTENDER;"+
                "DROP TRIGGER EDITTENDER;"+
                "DROP TRIGGER DELETETENDER;"+
                "DROP TRIGGER NEWORDER;"+
                "DROP TRIGGER EDITORDER;"+
                "DROP TRIGGER DELETEORDER;"+
                "DROP TRIGGER NEWINVOICE;"+
                "DROP TRIGGER EDITINVOICE;"+
                "DROP TRIGGER DELETEINVOICE;"+
                "DROP TRIGGER NEWCREDITINVOICE;"+
                "DROP TRIGGER EDITCREDITINVOICE;"+
                "DROP TRIGGER DELETECREDITINVOICE;"+
                "DROP TRIGGER NEWPERIODICINVOICE;"+
                "DROP TRIGGER EDITPERIODICINVOICE;"+
                "DROP TRIGGER DELETEPERIODICINVOICE;"+
                "DROP TRIGGER NEWOUTPAYMENT;"+
                "DROP TRIGGER EDITOUTPAYMENT;"+
                "DROP TRIGGER DELETEOUTPAYMENT;"+
                "DROP TRIGGER NEWPURCHASEORDER;"+
                "DROP TRIGGER EDITPURCHASEORDER;"+
                "DROP TRIGGER DELETEPURCHASEORDER;"+
                "DROP TRIGGER NEWSUPPLIERINVOICE;"+
                "DROP TRIGGER EDITSUPPLIERINVOICE;"+
                "DROP TRIGGER DELETESUPPLIERINVOICE;"+
                "DROP TRIGGER NEWSUPPLIERCREDITINVOICE;"+
                "DROP TRIGGER EDITSUPPLIERCREDITINVOICE;"+
                "DROP TRIGGER DELETESUPPLIERCREDITINVOICE;"+
                "DROP TRIGGER NEWINVENTORY;"+
                "DROP TRIGGER EDITINVENTORY;"+
                "DROP TRIGGER DELETEINVENTORY;"+
                "DROP TRIGGER NEWINDELIVERY;"+
                "DROP TRIGGER EDITINDELIVERY;"+
                "DROP TRIGGER DELETEINDELIVERY;"+
                "DROP TRIGGER NEWOUTDELIVERY;"+
                "DROP TRIGGER EDITOUTDELIVERY;"+
                "DROP TRIGGER DELETEOUTDELIVERY;"+
                "DROP TRIGGER NEWVOUCHER;"+
                "DROP TRIGGER EDITVOUCHER;"+
                "DROP TRIGGER DELETEVOUCHER;"+
                "DROP TRIGGER NEWOWNREPORT;"+
                "DROP TRIGGER EDITOWNREPORT;"+
                "DROP TRIGGER DELETEOWNREPORT;");
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

        } catch (SQLException e) {
            //System.out.println("Triggers fanns inte vid borttagning");
        }
    }

    /**
     * Funktion för att läsa in den gamla databasen till HSQL-databasen. Finns filen
     * bookkeeper.db samt tillhörande .data filer läses dessa in i hsql-databasen och
     * zippas ner.
     */
    public void readOldDatabase() {
        final File iFile = new File(SSPath.get(SSPath.APP_BASE), "db/bookkeeper.db");
        if(!iFile.exists()) return;

        SSInitDialog.runProgress(SSMainFrame.getInstance(),"Konverterar databasen", new Runnable(){
            public void run() {
                //dropTriggers();
                SSDBUtils.backup(iFile);
                SSSystemData iData = null;
                try {
                    iData = (SSSystemData) SSDBUtils.LoadFromFile(iFile);
                    SSDBUtils.removeBackup(iFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    SSDBUtils.restoreBackup(iFile);
                }

                if(iData == null) return;
                List<ArchiveFile> iFiles = new LinkedList<ArchiveFile>();
                iFiles.add(new ArchiveFile( iFile) );

                for (SSSystemCompany iSystemCompany : iData.getSystemCompanies()) {
                    File iCompanyFile = getFile( iSystemCompany.getId() );
                    LoadCompany(iCompanyFile);
                    iFiles.add(new ArchiveFile(iCompanyFile));
                    for (SSSystemYear iSystemYear : iSystemCompany.getYears()) {
                        File iYearFile = getFile( iSystemYear.getId());
                        LoadYear(iYearFile);
                        iFiles.add(new ArchiveFile(iYearFile));
                        setCurrentYear(null);
                    }
                    setCurrentCompany(null);
                }
                if (!iData.getAccountPlans().isEmpty()) {
                    try {
                        PreparedStatement iStatement = iConnection.prepareStatement("DELETE FROM tbl_accountplan");
                        iStatement.executeUpdate();
                        iStatement.close();
                        iConnection.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        try { iConnection.rollback(); } catch (SQLException ignored) {}
                        SSErrorDialog.showDialog(SSMainFrame.getInstance(),"SQL Error", e.getMessage());
                    }
                }

                for (SSAccountPlan iAccountPlan : iData.getAccountPlans()) {
                    addAccountPlan(iAccountPlan);
                }

                try {
                    SSBackupZip.compressFiles(SSPath.get(SSPath.APP_BASE) + "/db/databas_v1.zip", iFiles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (ArchiveFile iArchiveFile : iFiles) {
                    if (iArchiveFile.getFile().exists()) {
                        iArchiveFile.getFile().delete();
                    }
                }
                if (iLocking) {
                    createServerTriggers();
                } else {
                    createLocalTriggers();
                }
                SSFrameManager.getInstance().close();
            }
        });
    }

    private void LoadCompany(File iFile) {

        SSDBUtils.backup(iFile);
        try {
            SSCompany iCompany = (SSCompany)SSDBUtils.LoadFromFile(iFile);

            SSDBUtils.removeBackup(iFile);

            SSNewCompany iNewCompany = new SSNewCompany(iCompany);
            addCompany(iNewCompany);
            setCurrentCompany(getCompany(iNewCompany));

            for (SSCurrency iCurrency : iCompany.getCurrencies()) {
                if (!getCurrencies().contains(iCurrency)) {
                    addCurrency(iCurrency);
                }
            }

            for (SSUnit iUnit : iCompany.getUnits()) {
                if (!getUnits().contains(iUnit)) {
                    addUnit(iUnit);
                }
            }

            for (SSDeliveryWay iDeliveryWay : iCompany.getDeliveryWays()) {
                if (!getDeliveryWays().contains(iDeliveryWay)) {
                    addDeliveryWay(iDeliveryWay);
                }
            }

            for (SSDeliveryTerm iDeliveryTerm : iCompany.getDeliveryTerms()) {
                if (!getDeliveryTerms().contains(iDeliveryTerm)) {
                    addDeliveryTerm(iDeliveryTerm);
                }
            }

            for (SSPaymentTerm iPaymentTerm : iCompany.getPaymentTerms()) {
                if (!getPaymentTerms().contains(iPaymentTerm)) {
                    addPaymentTerm(iPaymentTerm);
                }
            }

            for (SSProject iOldProject : iCompany.getProjects()) {
                SSNewProject iProject = new SSNewProject(iOldProject);
                addProject(iProject);
            }
            for (SSResultUnit iOldResultUnit : iCompany.getResultUnits()) {
                SSNewResultUnit iResultUnit = new SSNewResultUnit(iOldResultUnit);
                addResultUnit(iResultUnit);
            }
            for (SSProduct iProduct : iCompany.getProducts()) {
                iProduct.fixResultUnitAndProject();
                addProduct(iProduct);
            }
            for (SSCustomer iCustomer : iCompany.getCustomers()) {
                addCustomer(iCustomer);
            }
            for (SSSupplier iSupplier : iCompany.getSuppliers()) {
                addSupplier(iSupplier);
            }
            for (SSAutoDist iAutoDist : iCompany.getAutoDists()) {
                addAutoDist(iAutoDist);
            }
            for (SSVoucherTemplate iVoucherTemplate : iCompany.getVoucherTemplates()) {
                addVoucherTemplate(iVoucherTemplate);
            }
            for (SSInpayment iInpayment : iCompany.getInpayments()) {
                addInpayment(iInpayment);
            }
            for (SSTender iTender : iCompany.getTenders()) {
                addTender(iTender);
            }
            for (SSOrder iOrder : iCompany.getOrders()) {
                addOrder(iOrder);
            }
            for (SSInvoice iInvoice : iCompany.getInvoices()) {
                for (SSSaleRow iRow : iInvoice.getRows()) {
                    iRow.fixResultUnitAndProject();
                }
                addInvoice(iInvoice);
            }
            for (SSCreditInvoice iCreditInvoice : iCompany.getCreditInvoices()) {
                for (SSSaleRow iRow : iCreditInvoice.getRows()) {
                    iRow.fixResultUnitAndProject();
                }
                addCreditInvoice(iCreditInvoice);
            }
            for (SSPeriodicInvoice iPeriodicInvoice : iCompany.getPeriodicInvoices()) {
                for (SSSaleRow iRow : iPeriodicInvoice.getTemplate().getRows()) {
                    iRow.fixResultUnitAndProject();
                }
                addPeriodicInvoice(iPeriodicInvoice);
            }
            for (SSOutpayment iOutpayment : iCompany.getOutpayments()) {
                addOutpayment(iOutpayment);
            }
            for (SSPurchaseOrder iPurchaseOrder : iCompany.getPurchaseOrders()) {
                addPurchaseOrder( iPurchaseOrder);
            }
            for (SSSupplierInvoice iSupplierInvoice : iCompany.getSupplierInvoices()) {
                for (SSSupplierInvoiceRow iRow : iSupplierInvoice.getRows()) {
                    iRow.fixResultUnitAndProject();
                }
                addSupplierInvoice(iSupplierInvoice);
            }
            for (SSSupplierCreditInvoice iSupplierCreditInvoice : iCompany.getSupplierCreditinvoices()) {
                for (SSSupplierInvoiceRow iRow : iSupplierCreditInvoice.getRows()) {
                    iRow.fixResultUnitAndProject();
                }
                addSupplierCreditInvoice(iSupplierCreditInvoice);
            }
            for (SSIndelivery iIndelivery : iCompany.getIndeliveries()) {
                addIndelivery(iIndelivery);
            }
            for (SSOutdelivery iOutdelivery : iCompany.getOutdeliveries()) {
                addOutdelivery(iOutdelivery);
            }
            for (SSInventory iInventory : iCompany.getInventories()) {
                addInventory(iInventory);
            }
        } catch (IOException e) {
            e.printStackTrace();
            SSDBUtils.restoreBackup(iFile);
        }
    }
    /**
     *
     * @param iFile
     */
    private void LoadYear(File iFile) {

        SSDBUtils.backup(iFile);
        try {
            SSAccountingYear iAccountingYear = (SSAccountingYear)SSDBUtils.LoadFromFile(iFile);
            SSNewAccountingYear iNewAccountingYear = new SSNewAccountingYear(iAccountingYear);
            addAccountingYear(iNewAccountingYear);
            setCurrentYear(getAccountingYear(iNewAccountingYear));

            for (SSVoucher iVoucher : iAccountingYear.getVouchers()) {
                for (SSVoucherRow iRow : iVoucher.getRows()) {
                    iRow.fixResultUnitAndProject();
                }
                addVoucher(iVoucher, true);
            }

            SSDBUtils.removeBackup(iFile);
        } catch (FileNotFoundException e){
            e.printStackTrace();
            SSDBUtils.restoreBackup(iFile);
        } catch (IOException e) {
            e.printStackTrace();
            SSDBUtils.restoreBackup(iFile);
        }
    }

    public File getFile(UID pIdentifier) {
        String iFileName =  pIdentifier.toString();

        iFileName = iFileName.replace(":", ".");
        iFileName = iFileName.replace("-", ".");

        return new File(SSPath.get(SSPath.APP_BASE), "db/" + iFileName + ".data");
    }

    public void createNewTables(){
        try {
            if(iConnection == null || iConnection.isClosed()) return;

            PreparedStatement iStatement = iConnection.prepareStatement("CREATE CACHED TABLE tbl_ownreport(id INTEGER IDENTITY, ownreport OBJECT, companyid INTEGER, FOREIGN KEY(companyid) REFERENCES tbl_company(id));");
            iStatement.executeUpdate();
            iConnection.commit();
            iStatement.close();

            dropTriggers();
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.system.SSDB");
        sb.append("{iAutoDists=").append(iAutoDists);
        sb.append(", iConnection=").append(iConnection);
        sb.append(", iCreditInvoices=").append(iCreditInvoices);
        sb.append(", iCurrentCompany=").append(iCurrentCompany);
        sb.append(", iCurrentYear=").append(iCurrentYear);
        sb.append(", iCustomers=").append(iCustomers);
        sb.append(", iIn=").append(iIn);
        sb.append(", iIndeliveries=").append(iIndeliveries);
        sb.append(", iInpayments=").append(iInpayments);
        sb.append(", iInventories=").append(iInventories);
        sb.append(", iInvoices=").append(iInvoices);
        sb.append(", iListenerMap=").append(iListenerMap);
        sb.append(", iLocking=").append(iLocking);
        sb.append(", iOrders=").append(iOrders);
        sb.append(", iOut=").append(iOut);
        sb.append(", iOutdeliveries=").append(iOutdeliveries);
        sb.append(", iOutpayments=").append(iOutpayments);
        sb.append(", iOwnReports=").append(iOwnReports);
        sb.append(", iPeriodicInvoices=").append(iPeriodicInvoices);
        sb.append(", iProducts=").append(iProducts);
        sb.append(", iPurchaseOrders=").append(iPurchaseOrders);
        sb.append(", iSocket=").append(iSocket);
        sb.append(", iSupplierCreditInvoices=").append(iSupplierCreditInvoices);
        sb.append(", iSupplierInvoices=").append(iSupplierInvoices);
        sb.append(", iSuppliers=").append(iSuppliers);
        sb.append(", iTenders=").append(iTenders);
        sb.append(", iVouchers=").append(iVouchers);
        sb.append('}');
        return sb.toString();
    }
}
