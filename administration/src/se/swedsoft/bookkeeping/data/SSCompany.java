package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.calc.util.SSAutoIncrement;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSPurchaseOrderMath;

import javax.imageio.ImageIO;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.rmi.server.UID;
import java.awt.*;

/**
 * @author Roger Björnstedt
 */
public class SSCompany implements Serializable {


    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private UID iId;

    // Grunduppgifter
    private String      iName;
    // Telefon
    private String      iPhone;

    private String      iPhone2;

    private String      iTelefax;

    private String      iResidence;

    private String      iWebAddress;

    private String      iSMTPAddress;

    private String      iEMail;

    private String      iContactPerson;
    // Innehar F-slattebevis
    private boolean     iTaxRegistered;
    // org nr / corporateid
    private String      iCorporateID;
    // Logo url
    private String      iLogotype;

    private String      iBank;
    // Momsregistreringsnummer (vat nr)
    private String      iVATNumber;
    // Bankgiro
    private String      iBankAccountNumber;
    // Plusgiro
    private String      iPlusAccountNumber;

    private String      iIBAN;

    private String      iSwift;

    private SSCurrency    iCurrency;
    // Fördröjningsränta
    private BigDecimal    iDelayintrest;
    // Påminnelseavgift
    private BigDecimal    iReminderfee;
    // Beräknad leveranstid
    private String        iEstimatedDelivery;

    // Momssatser
    private BigDecimal iTaxrate1;

    private BigDecimal iTaxrate2;

    private BigDecimal iTaxrate3;

    // Viktenhet
    private String iWeightUnit;

    //Volymenhet
    private String iVolumeUnit;

    // Address
    private SSAddress      iAddress;
    // Leveransadress
    private SSAddress      iDeliveryAddress;

    // Standardtexter
    private Map<SSStandardText, String> iStandardTexts;
    // Standardkonton
    private Map<SSDefaultAccount, Integer> iDefaultAccounts;

    // Valutor
    private List<SSCurrency> iCurrencies;
    // Enheter
    private List<SSUnit> iUnits;
    //  Leveranssätt
    private List<SSDeliveryWay> iDeliveryWays;
    // Leveransvilkor
    private List<SSDeliveryTerm> iDeliveryTerms;
    // Betalningsvilkor
    private List<SSPaymentTerm> iPaymentTerms;
    // Standard enhet
    private SSUnit iStandardUnit;

    private SSPaymentTerm iPaymentTerm;

    private SSDeliveryTerm iDeliveryTerm;

    private SSDeliveryWay iDeliveryWay;



    // Projects
    private List<SSProject>         iProjects;
    // Resultatenheter
    private List<SSResultUnit>      iResultUnits;
    // Verifikationsmallar
    private List<SSVoucherTemplate> iVoucherTemplates;
    // Produkter
    private List<SSProduct>         iProducts;
    // Kunder
    private List<SSCustomer>        iCustomers;
    // Leverantörer
    private List<SSSupplier>        iSuppliers;
    //Automatfördelningar
    private List<SSAutoDist>        iAutoDists;

    // Offerter
    private List<SSTender>          iTenders;
    // Orders
    private List<SSOrder>           iOrders;
    // Kundfakturor
    private List<SSInvoice>         iInvoices;
    // Inbetalningar
    private List<SSInpayment>       iInpayments;
    // Utbetalningar
    private List<SSOutpayment> iOutpayments;
    // Kreditfakturor
    private List<SSCreditInvoice>   iCreditInvoices;
    // Periodfakturor
    private List<SSPeriodicInvoice> iPeriodicInvoices;

    // Inköpsordrar
    private List<SSPurchaseOrder> iPurchaseOrders;
    // Leverantörsfakturor
    private List<SSSupplierInvoice> iSupplierInvoices;
    // Leverantörskreditfakturor
    private List<SSSupplierCreditInvoice> iSupplierCreditinvoices;
    // Inventeringar
    private List<SSInventory> iInventories;
    // Inleveranser
    private List<SSIndelivery> iIndeliveries;
    // Utleveranser
    private List<SSOutdelivery> iOutdeliveries;


    // Autoräknare
    private SSAutoIncrement iAutoIncrement;

    //////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public SSCompany() {
        iId                = new UID();
        iAddress           = new SSAddress();
        iDeliveryAddress   = new SSAddress();
        iStandardTexts     = new HashMap<SSStandardText, String>();
        iDefaultAccounts   = new HashMap<SSDefaultAccount, Integer>();

        iProjects          = new LinkedList<SSProject>();
        iVoucherTemplates  = new LinkedList<SSVoucherTemplate>();
        iResultUnits       = new LinkedList<SSResultUnit>();

        iProducts          = new LinkedList<SSProduct>();
        iCustomers         = new LinkedList<SSCustomer>();
        iSuppliers         = new LinkedList<SSSupplier>();
        iAutoDists         = new LinkedList<SSAutoDist>();
        iTenders           = new LinkedList<SSTender>();
        iOrders            = new LinkedList<SSOrder>();
        iInvoices          = new LinkedList<SSInvoice>();
        iInpayments        = new LinkedList<SSInpayment>();
        iCreditInvoices    = new LinkedList<SSCreditInvoice>();

        iAutoIncrement    = new SSAutoIncrement();

    }


    ////////////////////////////////////////////////////////////////////////////////////

    public UID getId() {
        return iId;
    }

    /**
     *
     * @return
     */
    public SSAddress getAddress() {
        return iAddress;
    }

    /**
     *
     * @param pAddress
     */
    public void setAddress(SSAddress pAddress) {
        iAddress = pAddress;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSAddress getDeliveryAddress() {
        return iDeliveryAddress;
    }

    /**
     *
     * @param pDeliveryAddress
     */
    public void setDeliveryAddress(SSAddress pDeliveryAddress) {
        iDeliveryAddress = pDeliveryAddress;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Map<SSStandardText, String> getStandardTexts() {
        if( iStandardTexts == null) iStandardTexts = new HashMap<SSStandardText, String>();
        return iStandardTexts;
    }


    /**
     *
     * @return
     */
    public String getStandardText(SSStandardText iStandardtext) {
        if( iStandardTexts != null) {
            return iStandardTexts.get(iStandardtext);
        } else {
            return null;
        }

    }

    /**
     *
     * @param pStandardTexts
     */
    public void setStandardTexts(Map<SSStandardText, String> pStandardTexts) {
        iStandardTexts = pStandardTexts;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Map<SSDefaultAccount, Integer> getDefaultAccounts() {
        if( iDefaultAccounts == null) iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        return iDefaultAccounts;
    }

    /**
     *
     * @return
     */
    public Integer getDefaultAccount(SSDefaultAccount iDefaultAccount) {
        if( iDefaultAccounts != null && iDefaultAccounts.containsKey(iDefaultAccount) ) {
            return iDefaultAccounts.get(iDefaultAccount);
        } else {
            return iDefaultAccount.getDefaultAccountNumber();
        }
    }
    /**
     *
     * @param iAccountPlan
     * @param iDefaultAccount
     * @return
     */
    public SSAccount getDefaultAccount(SSAccountPlan iAccountPlan, SSDefaultAccount iDefaultAccount){
        Integer iAccountNumber = iDefaultAccounts.get(iDefaultAccount);

        if(iAccountNumber == null) return null;

        return iAccountPlan.getAccount(iAccountNumber);
    }


    /**
     *
     * @param iDefaultAccounts
     */
    public void setDefaultAccounts(Map<SSDefaultAccount, Integer> iDefaultAccounts) {
        this.iDefaultAccounts = iDefaultAccounts;
    }


    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        iName = name;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getPhone() {
        return iPhone;
    }

    /**
     *
     * @param pPhone
     */
    public void setPhone(String pPhone) {
        iPhone = pPhone;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getPhone2() {
        return iPhone2;
    }

    /**
     *
     * @param pPhone2
     */
    public void setPhone2(String pPhone2) {
        iPhone2 = pPhone2;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getTelefax() {
        return iTelefax;
    }

    /**
     *
     * @param pTelefax
     */
    public void setTelefax(String pTelefax) {
        iTelefax = pTelefax;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getResidence() {
        return iResidence;
    }

    /**
     *
     * @param residence
     */
    public void setResidence(String residence) {
        iResidence = residence;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getEMail() {
        return iEMail;
    }

    /**
     *
     * @param pEMail
     */
    public void setEMail(String pEMail) {
        iEMail = pEMail;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getHomepage() {
        return iWebAddress;
    }

    /**
     *
     * @param pWebAddress
     */
    public void setHomepage(String pWebAddress) {
        iWebAddress = pWebAddress;
    }

    /////////////////////////////////////////////////////

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getSMTP() {
        return iSMTPAddress;
    }

    /**
     *
     * @param pSMTPAddress
     */
    public void setSMTP(String pSMTPAddress) {
        iSMTPAddress = pSMTPAddress;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getContactPerson() {
        return iContactPerson;
    }

    /**
     *
     * @param pContactPerson
     */
    public void setContactPerson(String pContactPerson) {
        iContactPerson = pContactPerson;
    }


    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getTaxRegistered() {
        return iTaxRegistered;
    }

    /**
     *
     * @param pTaxRegistered
     */
    public void setTaxRegistered(boolean pTaxRegistered) {
        iTaxRegistered = pTaxRegistered;
    }


    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getCorporateID() {
        return iCorporateID;
    }

    /**
     *
     * @param iCorporateID
     */
    public void setCorporateID(String iCorporateID) {
        this.iCorporateID = iCorporateID;
    }


    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getLogotype() {
        return iLogotype;
    }

    /**
     *
     * @param pLogotype
     */
    public void setLogotype(String pLogotype) {
        iLogotype = pLogotype;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getVATNumber() {
        return iVATNumber;
    }

    /**
     *
     * @param pVATNumber
     */
    public void setVATNumber(String pVATNumber) {
        iVATNumber = pVATNumber;
    }


    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getBank() {
        return iBank;
    }

    /**
     *
     * @param pBank
     */
    public void setBank(String pBank) {
        iBank = pBank;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getBankGiroNumber() {
        return iBankAccountNumber;
    }

    /**
     *
     * @param bankAccountNumber
     */
    public void setBankGiroNumber(String bankAccountNumber) {
        iBankAccountNumber = bankAccountNumber;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getPlusGiroNumber() {
        return iPlusAccountNumber;
    }

    /**
     *
     * @param plusAccountNumber
     */
    public void setPlusGiroNumber(String plusAccountNumber) {
        iPlusAccountNumber = plusAccountNumber;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getIBAN() {
        return iIBAN;
    }

    /**
     *
     * @param IBAN
     */
    public void setIBAN(String IBAN) {
        iIBAN = IBAN;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getBIC() {
        return iSwift;
    }

    /**
     *
     * @param swift
     */
    public void setBIC(String swift) {
        iSwift = swift;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getVolumeUnit() {
        if(iVolumeUnit == null) iVolumeUnit = "m3";

        return iVolumeUnit;
    }

    /**
     *
     * @param iVolumeUnit
     */
    public void setVolumeUnit(String iVolumeUnit) {
        this.iVolumeUnit = iVolumeUnit;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getWeightUnit() {
        if(iWeightUnit == null) iWeightUnit = "kg";

        return iWeightUnit;
    }

    /**
     *
     * @param iWeightUnit
     */
    public void setWeightUnit(String iWeightUnit) {
        this.iWeightUnit = iWeightUnit;
    }
    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSCurrency getCurrency() {
        return iCurrency;
    }

    /**
     *
     * @param iCurrency
     */
    public void setCurrency(SSCurrency iCurrency) {
        this.iCurrency = iCurrency;
    }
    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSUnit getStandardUnit() {
        return iStandardUnit;
    }

    /**
     *
     * @param iStandardUnit
     */
    public void setStandardUnit(SSUnit iStandardUnit) {
        this.iStandardUnit = iStandardUnit;
    }



    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getDelayInterest() {
        return iDelayintrest;
    }

    /**
     *
     * @param iDelayintrest
     */
    public void setDelayInterest(BigDecimal iDelayintrest) {
        this.iDelayintrest = iDelayintrest;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getReminderfee() {
        return iReminderfee;
    }

    /**
     *
     * @param iReminderfee
     */
    public void setReminderfee(BigDecimal iReminderfee) {
        this.iReminderfee = iReminderfee;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate1() {
        if(iTaxrate1 == null) iTaxrate1 = new BigDecimal(25.0);
        return iTaxrate1;
    }

    /**
     *
     * @param iTaxrate1
     */
    public void setTaxrate1(BigDecimal iTaxrate1) {
        this.iTaxrate1 = iTaxrate1;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate2() {
        if(iTaxrate2 == null) iTaxrate2 = new BigDecimal(12.0);
        return iTaxrate2;
    }

    /**
     *
     * @param iTaxrate2
     */
    public void setTaxrate2(BigDecimal iTaxrate2) {
        this.iTaxrate2 = iTaxrate2;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate3() {
        if(iTaxrate3 == null) iTaxrate3 = new BigDecimal(6.0);
        return iTaxrate3;
    }

    /**
     *
     * @param iTaxrate3
     */
    public void setTaxrate3(BigDecimal iTaxrate3) {
        this.iTaxrate3 = iTaxrate3;
    }

    /////////////////////////////////////////////////////

    public SSDeliveryWay getDeliveryWay() {
        return iDeliveryWay;
    }

    public void setDeliveryWay(SSDeliveryWay iDeliveryWay) {
        this.iDeliveryWay = iDeliveryWay;
    }

    public SSDeliveryTerm getDeliveryTerm() {
        return iDeliveryTerm;
    }

    public void setDeliveryTerm(SSDeliveryTerm iDeliveryTerm) {
        this.iDeliveryTerm = iDeliveryTerm;
    }

    public SSPaymentTerm getPaymentTerm() {
        return iPaymentTerm;
    }

    public void setPaymentTerm(SSPaymentTerm iPaymentTerm) {
        this.iPaymentTerm = iPaymentTerm;
    }

    /**
     *
     * @param iTaxCode
     * @return
     */
    public BigDecimal getTaxRate(SSTaxCode iTaxCode) {
        if(iTaxCode == SSTaxCode.TAXRATE_1) return  iTaxrate1;
        if(iTaxCode == SSTaxCode.TAXRATE_2) return  iTaxrate2;
        if(iTaxCode == SSTaxCode.TAXRATE_3) return  iTaxrate3;
        return null;
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getEstimatedDelivery() {
        return iEstimatedDelivery;
    }

    /**
     *
     * @param iEstimatedDelivery
     */
    public void setEstimatedDelivery(String iEstimatedDelivery) {
        this.iEstimatedDelivery = iEstimatedDelivery;
    }


    /////////////////////////////////////////////////////

    /**
     * Returns the currencies
     *
     * @return list of currencies
     */
    public List<SSCurrency> getCurrencies() {
        if(iCurrencies == null) iCurrencies = SSCurrency.getDefaultCurrencies();

        return iCurrencies;
    }

    public void updateCurrency(SSCurrency pCurrency) {
        for (int i = 0; i<iCurrencies.size();i++) {
            SSCurrency iCurrency = iCurrencies.get(i);
            if (iCurrency.getName().equals(pCurrency.getName())) {
                iCurrencies.remove(i);
                iCurrencies.add(i,pCurrency);
            }
        }
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSUnit> getUnits() {
        if(iUnits == null) iUnits = SSUnit.getDefaultUnits();

        return iUnits;
    }

    public void updateUnit(SSUnit pUnit) {
        for (int i = 0; i<iCurrencies.size();i++) {
            SSUnit iUnit = iUnits.get(i);
            if (iUnit.getName().equals(pUnit.getName())) {
                iUnits.remove(i);
                iUnits.add(i,pUnit);
            }
        }
    }

    /////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSDeliveryWay> getDeliveryWays() {
        if(iDeliveryWays == null)  iDeliveryWays = SSDeliveryWay.getDefaultDeliveryWays();

        return iDeliveryWays;
    }

    public void updateDeliveryWay(SSDeliveryWay pDeliveryWay) {
        for (int i = 0; i<iCurrencies.size();i++) {
            SSDeliveryWay iDeliveryWay = iDeliveryWays.get(i);
            if (iDeliveryWay.getName().equals(pDeliveryWay.getName())) {
                iDeliveryWays.remove(i);
                iDeliveryWays.add(i,pDeliveryWay);
            }
        }
    }

    /**
     *
     * @return
     */
    public List<SSDeliveryTerm> getDeliveryTerms() {
        if(iDeliveryTerms == null) iDeliveryTerms = SSDeliveryTerm.getDefaultDeliveryTerms();

        return iDeliveryTerms;
    }

    public void updateDeliveryTerm(SSDeliveryTerm pDeliveryTerm) {
        for (int i = 0; i<iCurrencies.size();i++) {
            SSDeliveryTerm iDeliveryTerm = iDeliveryTerms.get(i);
            if (iDeliveryTerm.getName().equals(pDeliveryTerm.getName())) {
                iDeliveryTerms.remove(i);
                iDeliveryTerms.add(i,pDeliveryTerm);
            }
        }
    }
    /**
     *
     * @return
     */
    public List<SSPaymentTerm> getPaymentTerms() {
        if(iPaymentTerms == null) iPaymentTerms  = SSPaymentTerm.getDefaultPaymentTerms();
        return iPaymentTerms;
    }

    public void updatePaymentTerm(SSPaymentTerm pPaymentTerm) {
        for (int i = 0; i<iCurrencies.size();i++) {
            SSPaymentTerm iPaymentTerm = iPaymentTerms.get(i);
            if (iPaymentTerm.getName().equals(pPaymentTerm.getName())) {
                iPaymentTerms.remove(i);
                iPaymentTerms.add(i,pPaymentTerm);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSProject> getProjects() {
        return iProjects;
    }

    /**
     *
     * @param iProjects
     */
    public void setProjects(List<SSProject> iProjects) {
        this.iProjects = iProjects;
    }

    public void updateProject(SSProject pProject) {
        for (int i = 0; i<iProjects.size();i++) {
            SSProject iProject = iProjects.get(i);
            if (iProject.getNumber()==pProject.getNumber()) {
                iProjects.remove(i);
                iProjects.add(i,pProject);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSResultUnit> getResultUnits() {
        return iResultUnits;
    }

    /**
     *
     * @param iResultUnits
     */
    public void setResultUnits(List<SSResultUnit> iResultUnits) {
        this.iResultUnits = iResultUnits;
    }

    public void updateResultUnit(SSResultUnit pResultUnit) {
        for (int i = 0; i<iResultUnits.size();i++) {
            SSResultUnit iResultUnit = iResultUnits.get(i);
            if (iResultUnit.getNumber()==pResultUnit.getNumber()) {
                iResultUnits.remove(i);
                iResultUnits.add(i,pResultUnit);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSVoucherTemplate> getVoucherTemplates() {
        return iVoucherTemplates;
    }

    /**
     *
     * @param iVoucherTemplates
     */
    public void setVoucherTemplates(List<SSVoucherTemplate> iVoucherTemplates) {
        this.iVoucherTemplates = iVoucherTemplates;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSProduct> getProducts() {
        if(iProducts == null) iProducts = new LinkedList<SSProduct>();

        return iProducts;
    }

    /**
     *
     * @param iProducts
     */
    public void setProducts(List<SSProduct> iProducts) {
        this.iProducts = iProducts;
    }

    public void updateProduct(SSProduct pProduct) {
        for (int i = 0; i<iProducts.size();i++) {
            SSProduct iProduct = iProducts.get(i);
            if (iProduct.getNumber().equals(pProduct.getNumber())) {
                iProducts.remove(i);
                iProducts.add(i,pProduct);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSCustomer> getCustomers() {
        if(iCustomers == null) iCustomers = new LinkedList<SSCustomer>();
        return iCustomers;
    }

    /**
     *
     * @param iCustomers
     */
    public void setCustomers(List<SSCustomer> iCustomers) {
        this.iCustomers = iCustomers;
    }

    public void updateCustomer(SSCustomer pCustomer) {
        for (int i = 0; i<iCustomers.size();i++) {
            SSCustomer iCustomer = iCustomers.get(i);
            if (iCustomer.getNumber().equals(pCustomer.getNumber())) {
                iCustomers.remove(i);
                iCustomers.add(i,pCustomer);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSSupplier> getSuppliers() {
        if(iSuppliers == null) iSuppliers = new LinkedList<SSSupplier>();
        return iSuppliers;
    }

    /**
     *
     * @param iSuppliers
     */
    public void setSuppliers(List<SSSupplier> iSuppliers) {
        this.iSuppliers = iSuppliers;
    }

    public void updateSupplier(SSSupplier pSupplier) {
        for (int i = 0; i<iSuppliers.size();i++) {
            SSSupplier iSupplier = iSuppliers.get(i);
            if (iSupplier.getNumber().equals(pSupplier.getNumber())) {
                iSuppliers.remove(i);
                iSuppliers.add(i,pSupplier);
            }
        }
    }

    //////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSAutoDist> getAutoDists() {
        if(iAutoDists == null) iAutoDists = new LinkedList<SSAutoDist>();
        return iAutoDists;
    }

    /**
     *
     * @param iAutoDists
     */
    public void setAutoDists(List<SSAutoDist> iAutoDists) {
        this.iAutoDists = iAutoDists;
    }

    public void updateAutoDist(SSAutoDist pAutoDist) {
        for (int i = 0; i<iAutoDists.size();i++) {
            SSAutoDist iAutoDist = iAutoDists.get(i);
            if (iAutoDist.getNumber().equals(pAutoDist.getNumber())) {
                iAutoDists.remove(i);
                iAutoDists.add(i,pAutoDist);
            }
        }
    }

    //////////////////////////////////////////////////////

    /**
     * Returns the tenders for this company
     *
     * @return the tenders
     */
    public List<SSTender> getTenders() {
        if( iTenders == null) iTenders = new LinkedList<SSTender>();
        return iTenders;
    }

    public void updateTender(SSTender pTender) {
        for (int i = 0; i<iTenders.size();i++) {
            SSTender iTender = iTenders.get(i);
            if (iTender.getNumber().equals(pTender.getNumber())) {
                iTenders.remove(i);
                iTenders.add(i,pTender);
            }
        }
    }

    /**
     * Sets the tenders for this company
     *
     * @param iTenders
     */
    public void setTenders(List<SSTender> iTenders) {
        this.iTenders = iTenders;
    }

    public Double getTenderValueForMonth(SSMonth iMonth) {
        Double sum = 0.0;
        for (SSTender iTender:iTenders) {
            Date iTenderDate = iTender.getDate();
            if (iMonth.isDateInMonth(iTenderDate)) {
                for (SSSaleRow iRow : iTender.getRows()) {
                    if(iRow.getSum() != null){
                        sum+=(iRow.getSum().doubleValue())*iTender.getCurrencyRate().doubleValue();
                    }
                }

            }
        }
        return sum;
    }

    //////////////////////////////////////////////////////



    /**
     * Returns the orders for this company
     *
     * @return the orders
     */
    public List<SSOrder> getOrders() {
        if( iOrders == null) iOrders = new LinkedList<SSOrder>();
        return iOrders;
    }

    /**
     * Sets the orders for this company
     *
     * @param iOrders
     */
    public void setOrders(List<SSOrder> iOrders) {
        this.iOrders = iOrders;
    }

    public void updateOrder(SSOrder pOrder) {
        for (int i = 0; i<iOrders.size();i++) {
            SSOrder iOrder = iOrders.get(i);
            if (iOrder.getNumber().equals(pOrder.getNumber())) {
                //pOrder.setInvoice(iOrder.getInvoice());
                //pOrder.setPurchaseOrder(iOrder.getPurchaseOrder());
                iOrders.remove(i);
                iOrders.add(i,pOrder);
            }
        }
    }

    public Double getOrderValueForMonth(SSMonth iMonth) {
        Double sum = 0.0;
        for (SSOrder iOrder:iOrders) {
            Date iOrderDate = iOrder.getDate();
            if (iMonth.isDateInMonth(iOrderDate)) {
                for (SSSaleRow iRow : iOrder.getRows()) {
                    if(iRow.getSum() != null){
                        sum+=(iRow.getSum().doubleValue())*iOrder.getCurrencyRate().doubleValue();
                    }
                }
            }
        }
        return sum;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSInvoice> getInvoices() {
        if( iInvoices == null) iInvoices = new LinkedList<SSInvoice>();
        return iInvoices;
    }

    /**
     *
     * @param iInvoices
     */
    public void setInvoices(List<SSInvoice> iInvoices) {
        this.iInvoices = iInvoices;
    }

    public void updateInvoice(SSInvoice pInvoice) {
        for (int i = 0; i<iInvoices.size();i++) {
            SSInvoice iInvoice = iInvoices.get(i);
            if (iInvoice.getNumber().equals(pInvoice.getNumber())) {
                iInvoices.remove(i);
                iInvoices.add(i,pInvoice);
            }
        }
    }

    public Double getInvoiceValueForMonth(SSMonth iMonth) {
        Double suminvoices = 0.0;
        for (SSInvoice iInvoice:iInvoices) {
            Date iInvoiceDate = iInvoice.getDate();
            if (iMonth.isDateInMonth(iInvoiceDate)) {
                for (SSSaleRow iRow : iInvoice.getRows()) {
                    if(iRow.getSum() != null){
                        suminvoices+=(iRow.getSum().doubleValue())*iInvoice.getCurrencyRate().doubleValue();
                    }
                }

            }
        }
        Double sumcreditinvoices = 0.0;
        for (SSCreditInvoice iCreditInvoice:iCreditInvoices) {
            Date iCreditInvoiceDate = iCreditInvoice.getDate();
            if (iMonth.isDateInMonth(iCreditInvoiceDate)) {
                for (SSSaleRow iRow : iCreditInvoice.getRows()) {
                    if(iRow.getSum() != null){
                        sumcreditinvoices+=(iRow.getSum().doubleValue())*iCreditInvoice.getCurrencyRate().doubleValue();
                    }
                }

            }
        }

        return suminvoices-sumcreditinvoices;
    }
    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSInpayment> getInpayments() {
        if( iInpayments == null) iInpayments = new LinkedList<SSInpayment>();
        return iInpayments;
    }

    /**
     *
     * @param iInpayments
     */
    public void setInpayments(List<SSInpayment> iInpayments) {
        this.iInpayments = iInpayments;
    }

    public void updateInpayment(SSInpayment pInpayment) {
        for (int i = 0; i<iInpayments.size();i++) {
            SSInpayment iInpayment = iInpayments.get(i);
            if (iInpayment.getNumber().equals(pInpayment.getNumber())) {
                iInpayments.remove(i);
                iInpayments.add(i,pInpayment);
            }
        }
    }

    /**
     *
     * @return
     */
    public List<SSOutpayment> getOutpayments() {
        if( iOutpayments == null) iOutpayments = new LinkedList<SSOutpayment>();
        return iOutpayments;
    }

    /**
     *
     * @param iOutpayments
     */
    public void setOutpayments(List<SSOutpayment> iOutpayments) {
        this.iOutpayments = iOutpayments;
    }

    public void updateOutpayment(SSOutpayment pOutpayment) {
        for (int i = 0; i<iOutpayments.size();i++) {
            SSOutpayment iOutpayment = iOutpayments.get(i);
            if (iOutpayment.getNumber().equals(pOutpayment.getNumber())) {
                iOutpayments.remove(i);
                iOutpayments.add(i,pOutpayment);
            }
        }
    }


    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSCreditInvoice> getCreditInvoices() {
        if( iCreditInvoices == null) iCreditInvoices = new LinkedList<SSCreditInvoice>();
        return iCreditInvoices;
    }

    /**
     *
     * @param iCreditInvoices
     */
    public void setCreditInvoices(List<SSCreditInvoice> iCreditInvoices) {
        this.iCreditInvoices = iCreditInvoices;
    }

    public void updateCreditInvoice(SSCreditInvoice pCreditInvoice) {
        for (int i = 0; i<iCreditInvoices.size();i++) {
            SSCreditInvoice iCreditInvoice = iCreditInvoices.get(i);
            if (iCreditInvoice.getNumber().equals(pCreditInvoice.getNumber())) {
                iCreditInvoices.remove(i);
                iCreditInvoices.add(i,pCreditInvoice);
            }
        }
    }

    //////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public List<SSPeriodicInvoice> getPeriodicInvoices() {
        if( iPeriodicInvoices == null) iPeriodicInvoices = new LinkedList<SSPeriodicInvoice>();
        return iPeriodicInvoices;
    }

    /**
     *
     * @param iPeriodicInvoices
     */
    public void setPeriodicInvoices(List<SSPeriodicInvoice> iPeriodicInvoices) {
        this.iPeriodicInvoices = iPeriodicInvoices;
    }

    public void updatePeriodicInvoice(SSPeriodicInvoice pPeriodicInvoice) {
        for (int i = 0; i<iPeriodicInvoices.size();i++) {
            SSPeriodicInvoice iPeriodicInvoice = iPeriodicInvoices.get(i);
            if (iPeriodicInvoice.getNumber().equals(pPeriodicInvoice.getNumber())) {
                iPeriodicInvoices.remove(i);
                iPeriodicInvoices.add(i,pPeriodicInvoice);
            }
        }
    }


    //////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSPurchaseOrder> getPurchaseOrders() {
        if( iPurchaseOrders == null) iPurchaseOrders = new LinkedList<SSPurchaseOrder>();

        return iPurchaseOrders;
    }


    /**
     *
     * @param iPurchaseOrders
     */
    public void setPurchaseOrders(List<SSPurchaseOrder> iPurchaseOrders) {
        this.iPurchaseOrders = iPurchaseOrders;
    }

    public void updatePurchaseOrder(SSPurchaseOrder pPurchaseOrder) {
        for (int i = 0; i<iPurchaseOrders.size();i++) {
            SSPurchaseOrder iPurchaseOrder = iPurchaseOrders.get(i);
            if (iPurchaseOrder.getNumber().equals(pPurchaseOrder.getNumber())) {
                iPurchaseOrders.remove(i);
                iPurchaseOrders.add(i,pPurchaseOrder);
            }
        }
    }

    public Double getPurchaseOrderValueForMonth(SSMonth iMonth) {
        Double sum = 0.0;
        for (SSPurchaseOrder iPurchaseOrder:iPurchaseOrders) {
            Date iPurchaseOrderDate = iPurchaseOrder.getDate();
            if (iMonth.isDateInMonth(iPurchaseOrderDate)) {
                if(iPurchaseOrder.getSum()!=null){
                    sum+= iPurchaseOrder.getSum().doubleValue()*iPurchaseOrder.getCurrencyRate().doubleValue();
                }
            }
        }
        return sum;
    }
    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSSupplierInvoice> getSupplierInvoices() {
        if( iSupplierInvoices == null) iSupplierInvoices = new LinkedList<SSSupplierInvoice>();

        return iSupplierInvoices;
    }

    /**
     *
     * @param iSupplierInvoices
     */
    public void setSupplierInvoices(List<SSSupplierInvoice> iSupplierInvoices) {
        this.iSupplierInvoices = iSupplierInvoices;
    }

    public void updateSupplierInvoice(SSSupplierInvoice pSupplierInvoice) {
        for (int i = 0; i<iSupplierInvoices.size();i++) {
            SSSupplierInvoice iSupplierInvoice = iSupplierInvoices.get(i);
            if (iSupplierInvoice.getNumber().equals(pSupplierInvoice.getNumber())) {
                iSupplierInvoices.remove(i);
                iSupplierInvoices.add(i,pSupplierInvoice);
            }
        }
    }

    public Double getSupplierInvoiceValueForMonth(SSMonth iMonth) {
        Double sumSupplierInvoices = 0.0;
        for (SSSupplierInvoice iSupplierInvoice:iSupplierInvoices) {
            Date iSupplierInvoiceDate = iSupplierInvoice.getDate();
            if (iMonth.isDateInMonth(iSupplierInvoiceDate)) {
                if(SSSupplierInvoiceMath.getNetSum(iSupplierInvoice)!=null){
                    sumSupplierInvoices += SSSupplierInvoiceMath.getNetSum(iSupplierInvoice).doubleValue()*iSupplierInvoice.getCurrencyRate().doubleValue();
                }
            }
        }
        Double sumSupplierCreditInvoices = 0.0;
        for (SSSupplierCreditInvoice iSupplierCreditInvoice:iSupplierCreditinvoices) {
            Date iSupplierCreditInvoiceDate = iSupplierCreditInvoice.getDate();
            if (iMonth.isDateInMonth(iSupplierCreditInvoiceDate)) {
                if(SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice)!=null){
                    sumSupplierCreditInvoices += SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice).doubleValue()*iSupplierCreditInvoice.getCurrencyRate().doubleValue();
                }

            }
        }

        return sumSupplierInvoices - sumSupplierCreditInvoices;
    }


    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSSupplierCreditInvoice> getSupplierCreditinvoices() {
        if( iSupplierCreditinvoices == null) iSupplierCreditinvoices = new LinkedList<SSSupplierCreditInvoice>();

        return iSupplierCreditinvoices;
    }

    /**
     *
     * @param iSupplierCreditinvoices
     */
    public void setSupplierCreditinvoices(List<SSSupplierCreditInvoice> iSupplierCreditinvoices) {
        this.iSupplierCreditinvoices = iSupplierCreditinvoices;
    }

    public void updateSupplierCreditInvoice(SSSupplierCreditInvoice pSupplierCreditInvoice) {
        for (int i = 0; i<iSupplierCreditinvoices.size();i++) {
            SSSupplierCreditInvoice iSupplierCreditInvoice = iSupplierCreditinvoices.get(i);
            if (iSupplierCreditInvoice.getNumber().equals(pSupplierCreditInvoice.getNumber())) {
                iSupplierCreditinvoices.remove(i);
                iSupplierCreditinvoices.add(i,pSupplierCreditInvoice);
            }
        }
    }


    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSInventory> getInventories() {
        if( iInventories == null) iInventories = new LinkedList<SSInventory>();

        return iInventories;
    }


    /**
     *
     * @param iInventories
     */
    public void setInventories(List<SSInventory> iInventories) {
        this.iInventories = iInventories;
    }

    public void updateInventory(SSInventory pInventory) {
        for (int i = 0; i<iInventories.size();i++) {
            SSInventory iInventory = iInventories.get(i);
            if (iInventory.getNumber().equals(pInventory.getNumber())) {
                iInventories.remove(i);
                iInventories.add(i,pInventory);
            }
        }
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSIndelivery> getIndeliveries() {
        if( iIndeliveries == null) iIndeliveries = new LinkedList<SSIndelivery>();

        return iIndeliveries;
    }


    /**
     *
     * @param iIndeliveries
     */
    public void setIndeliveries(List<SSIndelivery> iIndeliveries) {
        this.iIndeliveries = iIndeliveries;
    }

    public void updateIndelivery(SSIndelivery pIndelivery) {
        for (int i = 0; i<iIndeliveries.size();i++) {
            SSIndelivery iIndelivery = iIndeliveries.get(i);
            if (iIndelivery.getNumber().equals(pIndelivery.getNumber())) {
                iIndeliveries.remove(i);
                iIndeliveries.add(i,pIndelivery);
            }
        }
    }
    //////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSOutdelivery> getOutdeliveries() {
        if( iOutdeliveries == null) iOutdeliveries = new LinkedList<SSOutdelivery>();

        return iOutdeliveries;
    }


    /**
     *
     * @param iOutdeliveries
     */
    public void setOutdeliveries(List<SSOutdelivery> iOutdeliveries) {
        this.iOutdeliveries = iOutdeliveries;
    }

    public void updateOutdelivery(SSOutdelivery pOutdelivery) {
        for (int i = 0; i<iOutdeliveries.size();i++) {
            SSOutdelivery iOutdelivery = iOutdeliveries.get(i);
            if (iOutdelivery.getNumber().equals(pOutdelivery.getNumber())) {
                iOutdeliveries.remove(i);
                iOutdeliveries.add(i,pOutdelivery);
            }
        }
    }
    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSAutoIncrement getAutoIncrement() {
        if(iAutoIncrement == null){
            iAutoIncrement = new SSAutoIncrement();
        }
        return iAutoIncrement;
    }

    /**
     *
     * @return
     */
    public Image getLogoImage(){
        // The logotype is null
        if(iLogotype == null) return null;

        // Create the file
        File iFile = new File(iLogotype);

        // The file doesn't exists, return null
        if( ! iFile.exists() ) return null;

        Image iImage = null;
        try {
            iImage = ImageIO.read(iFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iImage;

    }

    //////////////////////////////////////////////////////





    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * <p/>
     *
     * @return a hash code value for this object.
     *
     */
    public int hashCode() {
        return iId.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     *
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof SSCompany)) {
            return false;
        }
        return iId.equals(((SSCompany)obj).getId());
    }


    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(iName);

        return sb.toString();
    }


}
