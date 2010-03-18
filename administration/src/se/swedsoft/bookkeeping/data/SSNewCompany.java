package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.util.SSAutoIncrement;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.util.SSMailServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roger Björnstedt
 */
public class SSNewCompany implements Serializable {


    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private Integer iId;
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

    private SSUnit iStandardUnit;

    private SSPaymentTerm iPaymentTerm;

    private SSDeliveryTerm iDeliveryTerm;

    private SSDeliveryWay iDeliveryWay;

    private boolean iRoundingOff;

    private Integer iVatPeriod;
    
    private SSMailServer iMailServer;

    // Autoräknare
    private SSAutoIncrement iAutoIncrement;

    //////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public SSNewCompany() {
        iId                = 0;
        iAddress           = new SSAddress();
        iDeliveryAddress   = new SSAddress();
        iStandardTexts     = new HashMap<SSStandardText, String>();
        iDefaultAccounts   = new HashMap<SSDefaultAccount, Integer>();

        iAutoIncrement    = new SSAutoIncrement();
        iRoundingOff   = false;
        iVatPeriod = 1;

    }

    public SSNewCompany(SSCompany iOldCompany) {
        iName = iOldCompany.getName();
        iPhone = iOldCompany.getPhone();
        iPhone2 = iOldCompany.getPhone2();
        iTelefax = iOldCompany.getTelefax();
        iResidence = iOldCompany.getResidence();
        iWebAddress = iOldCompany.getHomepage();
        iSMTPAddress = iOldCompany.getSMTP();
        iEMail = iOldCompany.getEMail();
        iContactPerson = iOldCompany.getContactPerson();
        iTaxRegistered = iOldCompany.getTaxRegistered();
        iCorporateID = iOldCompany.getCorporateID();
        iLogotype = iOldCompany.getLogotype();
        iBank = iOldCompany.getBank();
        iVATNumber = iOldCompany.getVATNumber();
        iBankAccountNumber = iOldCompany.getBankGiroNumber();
        iPlusAccountNumber = iOldCompany.getPlusGiroNumber();
        iIBAN = iOldCompany.getIBAN();
        iSwift = iOldCompany.getBIC();
        iCurrency = iOldCompany.getCurrency();
        iDelayintrest = iOldCompany.getDelayInterest();
        iReminderfee = iOldCompany.getReminderfee();
        iEstimatedDelivery = iOldCompany.getEstimatedDelivery();
        iTaxrate1 = iOldCompany.getTaxRate1();
        iTaxrate2 = iOldCompany.getTaxRate2();
        iTaxrate3 = iOldCompany.getTaxRate3();
        iWeightUnit = iOldCompany.getWeightUnit();
        iVolumeUnit = iOldCompany.getVolumeUnit();
        iAddress = iOldCompany.getAddress();
        iDeliveryAddress = iOldCompany.getDeliveryAddress();
        iStandardTexts = iOldCompany.getStandardTexts();
        iDefaultAccounts = iOldCompany.getDefaultAccounts();
        iStandardUnit = iOldCompany.getStandardUnit();
        iPaymentTerm = iOldCompany.getPaymentTerm();
        iDeliveryTerm = iOldCompany.getDeliveryTerm();
        iDeliveryWay = iOldCompany.getDeliveryWay();
        iAutoIncrement = iOldCompany.getAutoIncrement();
        iVatPeriod = 1;
    }
    ////////////////////////////////////////////////////////////////////////////////////

    public Integer getId() {
        return iId;
    }

    public void setId(Integer pId) {
        iId = pId;
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

    public SSMailServer getMailServer() {
        return iMailServer;
    }
    
    public void setMailServer( SSMailServer server ) {
        iMailServer = server;
    }
    
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
     * @param iStandardtext
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

    public boolean isRoundingOff() {
        return iRoundingOff;
    }

    public void setRoundingOff(boolean iRoundingOff) {
        this.iRoundingOff = iRoundingOff;
    }

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
     * @param iDefaultAccount
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

    public Integer getVatPeriod(){
        return iVatPeriod;
    }

    public void setVatPeriod(Integer iVatPeriod){
        this.iVatPeriod = iVatPeriod;
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

    public Double getTenderValueForMonth(SSMonth iMonth) {
        Double sum = 0.0;
        for (SSTender iTender: SSDB.getInstance().getTenders()) {
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

    public Double getOrderValueForMonth(SSMonth iMonth) {
        Double sum = 0.0;
        List<SSOrder> iOrders = SSDB.getInstance().getOrders();
        for (SSOrder iOrder : iOrders) {
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

    public Double getInvoiceValueForMonth(SSMonth iMonth) {
        Double suminvoices = 0.0;
        for (SSInvoice iInvoice:SSDB.getInstance().getInvoices()) {
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
        for (SSCreditInvoice iCreditInvoice:SSDB.getInstance().getCreditInvoices()) {
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


    public Double getPurchaseOrderValueForMonth(SSMonth iMonth) {
        Double sum = 0.0;
        for (SSPurchaseOrder iPurchaseOrder : SSDB.getInstance().getPurchaseOrders()) {
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


    public Double getSupplierInvoiceValueForMonth(SSMonth iMonth) {
        Double sumSupplierInvoices = 0.0;
        for (SSSupplierInvoice iSupplierInvoice:SSDB.getInstance().getSupplierInvoices()) {
            Date iSupplierInvoiceDate = iSupplierInvoice.getDate();
            if (iMonth.isDateInMonth(iSupplierInvoiceDate)) {
                if(SSSupplierInvoiceMath.getNetSum(iSupplierInvoice)!=null){
                    sumSupplierInvoices += SSSupplierInvoiceMath.getNetSum(iSupplierInvoice).doubleValue()*iSupplierInvoice.getCurrencyRate().doubleValue();
                }
            }
        }
        Double sumSupplierCreditInvoices = 0.0;
        for (SSSupplierCreditInvoice iSupplierCreditInvoice:SSDB.getInstance().getSupplierCreditInvoices()) {
            Date iSupplierCreditInvoiceDate = iSupplierCreditInvoice.getDate();
            if (iMonth.isDateInMonth(iSupplierCreditInvoiceDate)) {
                if(SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice)!=null){
                    sumSupplierCreditInvoices += SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice).doubleValue()*iSupplierCreditInvoice.getCurrencyRate().doubleValue();
                }

            }
        }

        return sumSupplierInvoices - sumSupplierCreditInvoices;
    }


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
        if(obj == null) return false;

        if (!(obj instanceof SSNewCompany)) {
            return false;
        }
        if(iId == null) return false;

        return iId.equals(((SSNewCompany)obj).getId());
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
