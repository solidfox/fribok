package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-jun-12
 * Time: 14:51:21
 */
public class SSSupplierInvoiceRow  implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    private static final long serialVersionUID = 1L;

    // Productnr
    private String iProductNr;
    // Beskrivning
    private String iDescription;
    // Enhetspris
    private BigDecimal iUnitprice;
    // Antal
    private Integer iQuantity;
    // Enhet
    private SSUnit iUnit;
    // Frakt / Enhet
    private BigDecimal iUnitFreight;



    // Konto
    private Integer iAccountNr;
    // Projekt
    private String iProjectNumber;
    private Integer iProjectNr;
    // Resultatenhet
    private String iResultUnitNumber;
    private Integer iResultUnitNr;



    // Transient reference to the product
    private transient SSProduct iProduct;
    // Transient reference to the account
    private transient SSAccount iAccount;
    // Transient reference to the Projekt
    private transient SSNewProject iProject;
    // Transient reference to the result init
    private transient SSNewResultUnit iResultUnit;


    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSSupplierInvoiceRow() {
    }

    /**
     * Copy constructor
     *
     * @param
     */
    public SSSupplierInvoiceRow(SSSupplierInvoiceRow iSupplierInvoiceRow) {
        this.iDescription  = iSupplierInvoiceRow.iDescription;
        this.iProductNr    = iSupplierInvoiceRow.iProductNr;
        this.iUnitprice    = iSupplierInvoiceRow.iUnitprice;
        this.iQuantity     = iSupplierInvoiceRow.iQuantity;
        this.iUnit         = iSupplierInvoiceRow.iUnit;
        this.iUnitFreight  = iSupplierInvoiceRow.iUnitFreight;
        this.iAccountNr    = iSupplierInvoiceRow.iAccountNr;
        //this.iProjectNr    = iSupplierInvoiceRow.iProjectNr;
        //this.iResultUnitNr = iSupplierInvoiceRow.iResultUnitNr;
        this.iProjectNumber = iSupplierInvoiceRow.iProjectNumber;
        this.iResultUnitNumber = iSupplierInvoiceRow.iResultUnitNumber;
        this.iProduct      = iSupplierInvoiceRow.iProduct;
        this.iAccount      = iSupplierInvoiceRow.iAccount;
        this.iProject      = iSupplierInvoiceRow.iProject;
        this.iResultUnit   = iSupplierInvoiceRow.iResultUnit;
    }

    /**
     *
     * @param iProduct
     */
    public SSSupplierInvoiceRow(SSProduct iProduct) {
        this.iProductNr   = iProduct.getNumber();
        this.iDescription = iProduct.getDescription();
        this.iUnitprice   = iProduct.getSellingPrice();
        this.iUnit        = iProduct.getUnit();
        this.iAccountNr   = iProduct.getDefaultAccount(SSDefaultAccount.Sales);
        this.iProduct     = iProduct;
        this.iQuantity    = null;
        this.iAccount     = null;
    }

    /**
     *
     * @param iRow
     */
    public SSSupplierInvoiceRow(SSPurchaseOrderRow iRow) {
        this.iDescription     = iRow.getDescription();
        this.iProductNr    = iRow.getProductNr();
        this.iUnitprice    = iRow.getUnitPrice();
        this.iQuantity     = iRow.getQuantity();
        this.iUnit         = iRow.getUnit();
        this.iAccountNr    = iRow.getAccountNr();
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getProductNr() {
        return iProductNr;
    }

    /**
     *
     * @param iProductNr
     */
    public void setProductNr(String iProductNr) {
        this.iProductNr = iProductNr;
    }

    ////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getUnitprice() {
        return iUnitprice;
    }

    /**
     *
     * @param iUnitprice
     */
    public void setUnitprice(BigDecimal iUnitprice) {
        this.iUnitprice = iUnitprice;
    }

    ////////////////////////////////////////////////////

    /**
     * 
     * @return
     */
    public BigDecimal getUnitFreight() {
        return iUnitFreight;
    }

    /**
     *
     * @param iUnitFreight
     */
    public void setUnitFreight(BigDecimal iUnitFreight) {
        this.iUnitFreight = iUnitFreight;
    }
    ////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public Integer getQuantity() {
        return iQuantity;
    }

    /**
     *
     * @param iQuantity
     */
    public void setQuantity(Integer iQuantity) {
        this.iQuantity = iQuantity;
    }

    ////////////////////////////////////////////////////

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



    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getAccountNr() {
        return iAccountNr;
    }

    /**
     *
     * @param iAccountNr
     */
    public void setAccountNr(Integer iAccountNr) {
        this.iAccountNr = iAccountNr;
        this.iAccount   = null;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getResultUnitNr() {
        return iResultUnitNumber;
    }

    /**
     *
     * @param iResultUnitNr
     */
    public void setResultUnitNr(String iResultUnitNr) {
        this.iResultUnitNumber = iResultUnitNr;
        this.iResultUnit   = null;
    }

    public void fixResultUnitAndProject() {
        if (iResultUnitNr != null && iResultUnitNumber == null) {
            iResultUnitNumber = iResultUnitNr.toString();
        }
        if (iProjectNr != null && iProjectNumber == null) {
            iProjectNumber = iProjectNr.toString();
        }
    }
    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getProjectNr() {
        return iProjectNumber;
    }

    /**
     *
     * @param iProjectNr
     */
    public void setProjectNr(String iProjectNr) {
        this.iProjectNumber = iProjectNr;
        this.iProject   = null;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @param iAccounts
     * @return
     */
    public SSAccount getAccount(List<SSAccount> iAccounts) {
        if(iAccount == null && iAccountNr != null){
            for(SSAccount iCurrent: iAccounts){
                if(iAccountNr.equals( iCurrent.getNumber() )){
                    iAccount = iCurrent;
                }
            }
        }
        return iAccount;
    }

    /**
     *
     * @param iAccount
     */
    public void setAccount(SSAccount iAccount) {
        this.iAccount   = iAccount;

        if (iAccount == null) {
            this.iAccountNr = -1;
        } else {
            this.iAccountNr = iAccount.getNumber();
        }
    }

    ////////////////////////////////////////////////////

    /**
     * Get the product for this row
     *
     * @return
     */
    public SSProduct getProduct() {
        return getProduct(SSDB.getInstance().getProducts());
    }

    /**
     * Gets the product for this row
     *
     * @return
     */
    public SSProduct getProduct(List<SSProduct> iProducts) {
        if(iProduct == null){
            for(SSProduct iCurrent: iProducts){
                String iNumber = iCurrent.getNumber();

                if(iNumber != null && iNumber.equals(iProductNr)){
                    iProduct = iCurrent;
                    break;
                }
            }
        }
        return iProduct;
    }

    /**
     *
     * @param iProduct
     */
    public void setProduct(SSProduct iProduct) {
        this.iProduct     = iProduct;
        this.iProductNr   = iProduct == null ? null : iProduct.getNumber();

        if(iProduct != null){
            this.iDescription = iProduct.getDescription();
            this.iUnitprice   = iProduct.getPurchasePrice();
            this.iUnitFreight = iProduct.getUnitFreight();
            this.iUnit        = iProduct.getUnit();
            this.iAccountNr   = iProduct.getDefaultAccount(SSDefaultAccount.Purchases);
            this.iAccount     = null;
            this.iQuantity    = 1;
        }
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSNewResultUnit getResultUnit(List<SSNewResultUnit> iResultUnits) {
        if(iResultUnit == null){
            for(SSNewResultUnit iCurrent: iResultUnits){
                String iNumber = iCurrent.getNumber();

                if(iNumber != null && iNumber.equals(iResultUnitNumber)){
                    iResultUnit = iCurrent;
                    break;
                }
            }
        }
        return iResultUnit;
    }

    /**
     *
     * @param iResultUnit
     */
    public void setResultUnit(SSNewResultUnit iResultUnit) {
        this.iResultUnit   = iResultUnit;
        this.iResultUnitNumber = iResultUnit == null ? null : iResultUnit.getNumber();
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSNewProject getProject(List<SSNewProject> iProjects) {
        if(iProject == null){
            for(SSNewProject iCurrent: iProjects){
                String iNumber = iCurrent.getNumber();

                if(iNumber != null && iNumber.equals(iProjectNumber)){
                    iProject = iCurrent;
                    break;
                }
            }
        }
        return iProject;
    }

    /**
     *
     * @param iProject
     */
    public void setProject(SSNewProject iProject) {
        this.iProject   = iProject;
        this.iProjectNumber = iProject == null ? null : iProject.getNumber();

    }

    ////////////////////////////////////////////////////


    /**
     * Returns <code>true</code> if the product is contained in the row
     *
     * @param iProduct
     * @return if the row contains the product
     */
    public boolean hasProduct(SSProduct iProduct){
        return iProductNr != null && iProductNr.equals( iProduct.getNumber() );
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
        sb.append(iProductNr);
        sb.append(", ");
        sb.append(iDescription);
        sb.append(", ");
        sb.append(iUnitprice);
        sb.append(", ");
        sb.append(iQuantity);
        sb.append(' ');
        sb.append(iUnit);
        sb.append(" (");
        sb.append(iProduct);
        sb.append(')');
        return sb.toString();
    }
    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iProductNr;
    }



    /**
     * Calculate the sum of the row as (Count * Unitprice) * (1 / iDiscount)
     *
     * @return the sum of the row
     */
    public BigDecimal getSum() {
        // If either of the unitprice or count is null we cant have a sum
        if(iUnitprice == null || iQuantity == null) return null;

        // Calculate the sum of the products
        return iUnitprice.multiply( new BigDecimal(iQuantity) );
    }

}
