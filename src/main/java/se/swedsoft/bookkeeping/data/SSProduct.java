package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


/**
 * User: Andreas Lago
 * Date: 2006-mar-20
 * Time: 14:45:08
 */
public class SSProduct implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // produkt nr
    private String iNumber;
    // beskrivning
    private String iDescription;
    // á-pris
    private BigDecimal iUnitprice;
    // Momskod
    private SSTaxCode iTaxCode;
    // Lagerplats
    private String iWarehouseLocation;
    // Beställningspunkt
    private Integer iOrderpoint;
    // Beställningsantal
    private Integer iOrdercount;
    // Inköpspris
    private BigDecimal iPurchasePrice;
    // Lagerpris
    private BigDecimal iStockPrice;
    // Enhetsfrakt
    private BigDecimal iFreight;
    // Muvudleverantör
    private String iSupplierNr;
    // Leverantörens artikel nr
    private String iSupplierProductNr;
    // Utgått
    private boolean iExpired;
    // Lagervara
    private boolean iStockGoods;
    // Enhet
    private SSUnit iUnit;
    // Vikt kg
    private BigDecimal iWeight;
    // Volum m^3
    private BigDecimal iVolume;
    // Försäljningskonto och Inköpskonto
    private Map<SSDefaultAccount, Integer> iDefaultAccounts;

    private Integer iProjectNr;
    private String iProjectNumber;

    private Integer iResultUnitNr;
    private String iResultUnitNumber;
    // Ingående produkter
    private List<SSProductRow> iProductRows;

    private transient SSSupplier iSupplier;

    private transient SSNewProject iProject;

    private transient SSNewResultUnit iResultUnit;

    private Map<Locale, String> iDescriptions;

    // //////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSProduct() {
        iProductRows = new LinkedList<SSProductRow>();
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        iUnitprice = new BigDecimal(0);
        iPurchasePrice = new BigDecimal(0);
        iStockPrice = new BigDecimal(0);
        iFreight = new BigDecimal(0);
        iTaxCode = SSTaxCode.TAXRATE_1;
        iStockGoods = true;

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if (iCompany != null) {
            iUnit = iCompany.getStandardUnit();

            iDefaultAccounts.put(SSDefaultAccount.Sales,
                    iCompany.getDefaultAccount(SSDefaultAccount.Sales));
            iDefaultAccounts.put(SSDefaultAccount.Purchases,
                    iCompany.getDefaultAccount(SSDefaultAccount.Purchases));
        }
    }

    /**
     * Copy constructor
     *
     * @param iProduct
     */
    public SSProduct(SSProduct iProduct) {
        copyFrom(iProduct);
    }

    /**
     *
     * @param iProduct
     */
    public void copyFrom(SSProduct iProduct) {
        iNumber = iProduct.iNumber;
        iDescription = iProduct.iDescription;
        iUnitprice = iProduct.iUnitprice;
        iTaxCode = iProduct.iTaxCode;
        iWarehouseLocation = iProduct.iWarehouseLocation;
        iOrderpoint = iProduct.iOrderpoint;
        iOrdercount = iProduct.iOrdercount;
        iPurchasePrice = iProduct.iPurchasePrice;
        iStockPrice = iProduct.iStockPrice;
        iFreight = iProduct.iFreight;
        iSupplierNr = iProduct.iSupplierNr;
        iSupplierProductNr = iProduct.iSupplierProductNr;
        iExpired = iProduct.iExpired;
        iStockGoods = iProduct.iStockGoods;
        iUnit = iProduct.iUnit;
        iWeight = iProduct.iWeight;
        iVolume = iProduct.iVolume;
        iSupplier = iProduct.iSupplier;

        iDescriptions = new HashMap<Locale, String>();
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        iProductRows = new LinkedList<SSProductRow>();

        iDefaultAccounts.put(SSDefaultAccount.Sales,
                iProduct.iDefaultAccounts.get(SSDefaultAccount.Sales));
        iDefaultAccounts.put(SSDefaultAccount.Purchases,
                iProduct.iDefaultAccounts.get(SSDefaultAccount.Purchases));

        iProject = iProduct.iProject;
        iProjectNumber = iProduct.iProjectNumber;

        iResultUnit = iProduct.iResultUnit;
        iResultUnitNumber = iProduct.iResultUnitNumber;

        for (SSProductRow iProductRow: iProduct.iProductRows) {
            iProductRows.add(new SSProductRow(iProductRow));
        }
        iDescriptions.putAll(iProduct.getDescriptions());
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getNumber() {
        return iNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(String iNumber) {
        this.iNumber = iNumber;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     *
     * @param iDescription
     */
    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
    }

    /**
     *
     * @param iLocale
     * @return
     */
    public String getDescription(Locale iLocale) {
        if (iDescriptions == null) {
            iDescriptions = new HashMap<Locale, String>();
        }

        String iDescription = iDescriptions.get(iLocale);

        if (iDescription == null || iDescription.trim().length() == 0) {
            return null;
        } else {
            return iDescription;
        }
    }

    /**
     *
     * @param iLocale
     * @param iDescription
     */
    public void setDescription(Locale iLocale, String iDescription) {
        if (iDescriptions == null) {
            iDescriptions = new HashMap<Locale, String>();
        }

        iDescriptions.put(iLocale, iDescription);
    }

    // //////////////////////////////////////////////////

    public String getResultUnitNr() {
        return iResultUnitNumber;
    }

    public void setResultUnitNr(String iResultUnitNr) {
        iResultUnitNumber = iResultUnitNr;
    }

    public String getProjectNr() {
        return iProjectNumber;
    }

    public void setProjectNr(String iProjectNr) {
        iProjectNumber = iProjectNr;
    }

    public SSNewProject getProject() {
        return iProject;
    }

    public SSNewProject getProject(String iNumber) {
        for (SSNewProject pProject : SSDB.getInstance().getProjects()) {
            if (pProject.getNumber().equals(iNumber)) {
                return pProject;
            }
        }
        return null;
    }

    public void setProject(SSNewProject iProject) {
        this.iProject = iProject;
        iProjectNumber = iProject == null ? null : iProject.getNumber();
    }

    public SSNewResultUnit getResultUnit() {
        return iResultUnit;
    }

    public SSNewResultUnit getResultUnit(String iNumber) {
        for (SSNewResultUnit pResultUnit : SSDB.getInstance().getResultUnits()) {
            if (pResultUnit.getNumber().equals(iNumber)) {
                return pResultUnit;
            }
        }
        return null;
    }

    public void setResultUnit(SSNewResultUnit iResultUnit) {
        this.iResultUnit = iResultUnit;
        iResultUnitNumber = iResultUnit == null ? null : iResultUnit.getNumber();
    }

    public void fixResultUnitAndProject() {
        if (iResultUnitNr != null && iResultUnitNumber == null) {
            iResultUnitNumber = iResultUnitNr.toString();
        }
        if (iProjectNr != null && iProjectNumber == null) {
            iProjectNumber = iProjectNr.toString();
        }
    }

    /**
     *
     * @return
     */
    public Map<Locale, String> getDescriptions() {
        if (iDescriptions == null) {
            iDescriptions = new HashMap<Locale, String>();
        }

        return iDescriptions;
    }

    /**
     *
     * @param iDescriptions
     */
    public void setDescriptions(Map<Locale, String> iDescriptions) {
        this.iDescriptions = iDescriptions;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getSellingPrice() {
        iUnitprice = iUnitprice.setScale(2, BigDecimal.ROUND_HALF_UP);
        return iUnitprice;
    }

    /**
     *
     * @param iUnitprice
     */
    public void setSellingPrice(BigDecimal iUnitprice) {
        this.iUnitprice = iUnitprice;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSTaxCode getTaxCode() {
        return iTaxCode;
    }

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if (iTaxCode != null && iCompany != null) {
            return iCompany.getTaxRate(iTaxCode);
        }
        return null;
    }

    /**
     *
     * @param iTaxCode
     */
    public void setTaxCode(SSTaxCode iTaxCode) {
        this.iTaxCode = iTaxCode;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getWarehouseLocation() {
        return iWarehouseLocation;
    }

    /**
     *
     * @param iWarehouseLocation
     */
    public void setWarehouseLocation(String iWarehouseLocation) {
        this.iWarehouseLocation = iWarehouseLocation;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getOrderpoint() {
        return iOrderpoint;
    }

    /**
     *
     * @param iOrderpoint
     */
    public void setOrderpoint(Integer iOrderpoint) {
        this.iOrderpoint = iOrderpoint;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getOrdercount() {
        return iOrdercount;
    }

    /**
     *
     * @param iOrdercount
     */
    public void setOrdercount(Integer iOrdercount) {
        this.iOrdercount = iOrdercount;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getPurchasePrice() {
        return iPurchasePrice;
    }

    /**
     *
     * @param iPurchasePrice
     */
    public void setPurchasePrice(BigDecimal iPurchasePrice) {
        this.iPurchasePrice = iPurchasePrice;
    }

    // //////////////////////////////////////////////////

    public BigDecimal getStockPrice() {
        return iStockPrice;
    }

    public void setStockPrice(BigDecimal iStockPrice) {
        this.iStockPrice = iStockPrice;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getUnitFreight() {
        return iFreight;
    }

    /**
     *
     * @param iFreight
     */
    public void setUnitFreight(BigDecimal iFreight) {
        this.iFreight = iFreight;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getSupplierNr() {
        return iSupplierNr;
    }

    /**
     *
     * @param iSupplierNr
     */
    public void setSupplierNr(String iSupplierNr) {
        this.iSupplierNr = iSupplierNr;
        iSupplier = null;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @param iSuppliers
     * @return
     */
    public SSSupplier getSupplier(List<SSSupplier> iSuppliers) {
        if (iSupplier == null) {
            for (SSSupplier iCurrent : iSuppliers) {
                if (iCurrent.getNumber().equals(iSupplierNr)) {
                    iSupplier = iCurrent;
                }
            }
        }
        return iSupplier;
    }

    /**
     *
     * @param iSupplier
     */
    public void setSupplier(SSSupplier iSupplier) {
        this.iSupplier = iSupplier;
        iSupplierNr = iSupplier == null ? null : iSupplier.getNumber();
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getSupplierProductNr() {
        return iSupplierProductNr;
    }

    /**
     *
     * @param iSupplierProductNr
     */
    public void setSupplierProductNr(String iSupplierProductNr) {
        this.iSupplierProductNr = iSupplierProductNr;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isExpired() {
        return iExpired;
    }

    /**
     *
     * @param iExpired
     */
    public void setExpired(boolean iExpired) {
        this.iExpired = iExpired;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isStockProduct() {
        return iStockGoods;
    }

    /**
     *
     * @param iStockGoods
     */
    public void setStockProduct(boolean iStockGoods) {
        this.iStockGoods = iStockGoods;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSUnit getUnit() {
        return iUnit;
    }

    /**
     *
     * @param iUnit
     */
    public void setUnit(SSUnit iUnit) {
        this.iUnit = iUnit;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getWeight() {
        if (iWeight == null) {
            iWeight = new BigDecimal(0);
        }
        return iWeight;
    }

    /**
     *
     * @param iWeight
     */
    public void setWeight(BigDecimal iWeight) {
        this.iWeight = iWeight;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getVolume() {
        if (iVolume == null) {
            iVolume = new BigDecimal(0);
        }

        return iVolume;
    }

    /**
     *
     * @param iVolume
     */
    public void setVolume(BigDecimal iVolume) {
        this.iVolume = iVolume;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @param iDefaultAccount
     * @return
     */
    public Integer getDefaultAccount(SSDefaultAccount iDefaultAccount) {
        if (iDefaultAccounts == null) {
            iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        }

        if (iDefaultAccounts.containsKey(iDefaultAccount)) {
            return iDefaultAccounts.get(iDefaultAccount);
        } else {
            return iDefaultAccount.getDefaultAccountNumber();
        }
    }

    /**
     *
     * @param iDefaultAccount
     * @param iDefault
     * @return
     */
    public Integer getDefaultAccount(SSDefaultAccount iDefaultAccount, Integer iDefault) {
        if (iDefaultAccounts == null) {
            iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        }

        if (iDefaultAccounts.containsKey(iDefaultAccount)) {
            return iDefaultAccounts.get(iDefaultAccount);
        } else {
            return iDefault;
        }
    }

    /**
     *
     * @param iDefaultAccount
     * @param iAccount
     */
    public void setDefaultAccount(SSDefaultAccount iDefaultAccount, Integer iAccount) {
        if (iDefaultAccounts == null) {
            iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        }
        iDefaultAccounts.put(iDefaultAccount, iAccount);
    }

    /**
     *
     * @param iDefaultAccount
     * @param iAccount
     */
    public void setDefaultAccount(SSDefaultAccount iDefaultAccount, SSAccount iAccount) {
        if (iAccount != null) {
            setDefaultAccount(iDefaultAccount, iAccount.getNumber());
        }
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSProductRow> getParcelRows() {
        if (iProductRows == null) {
            iProductRows = new LinkedList<SSProductRow>();
        }

        return iProductRows;
    }

    /**
     *
     * @param iParcelRows
     */
    public void setParcelRows(List<SSProductRow> iParcelRows) {
        iProductRows = iParcelRows;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isParcel() {
        return !getParcelRows().isEmpty();
    }

    // //////////////////////////////////////////////////

    public boolean equals(Object obj) {

        if (iNumber == null) {
            return false;
        }

        if (obj instanceof SSProduct) {
            SSProduct iProduct = (SSProduct) obj;

            return iNumber.equals(iProduct.iNumber);
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(iNumber);
        sb.append(", ");
        sb.append(iDescription);

        return sb.toString();
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iNumber;
    }

    public BigDecimal getProductRevenueForMonth(SSMonth iMonth) {
        Double iInvoiceSum = 0.0;
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        for (SSInvoice iInvoice : iInvoices) {
            if (iMonth.isDateInMonth(iInvoice.getDate())) {
                for (SSSaleRow iRow : iInvoice.getRows()) {
                    if (iRow.getProductNr() != null) {
                        if (iRow.getProductNr().equals(iNumber) && iRow.getSum() != null) {
                            iInvoiceSum += iRow.getSum().doubleValue()
                                    * iInvoice.getCurrencyRate().doubleValue();
                        }
                    }
                }
            }
        }

        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();
        Double iCreditInvoiceSum = 0.0;

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if (iMonth.isDateInMonth(iCreditInvoice.getDate())) {
                for (SSSaleRow iRow : iCreditInvoice.getRows()) {
                    if (iRow.getProductNr() != null) {
                        if (iRow.getProductNr().equals(iNumber) && iRow.getSum() != null) {
                            iCreditInvoiceSum += iRow.getSum().doubleValue()
                                    * iCreditInvoice.getCurrencyRate().doubleValue();
                        }
                    }
                }
            }
        }
        return new BigDecimal(iInvoiceSum - iCreditInvoiceSum);
    }
}
