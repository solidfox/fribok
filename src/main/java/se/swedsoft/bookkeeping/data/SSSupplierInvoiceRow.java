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
     * @param iSupplierInvoiceRow
     */
    public SSSupplierInvoiceRow(SSSupplierInvoiceRow iSupplierInvoiceRow) {
        iDescription         = iSupplierInvoiceRow.iDescription;
        iProductNr           = iSupplierInvoiceRow.iProductNr;
        iUnitprice           = iSupplierInvoiceRow.iUnitprice;
        iQuantity            = iSupplierInvoiceRow.iQuantity;
        iUnit                = iSupplierInvoiceRow.iUnit;
        iUnitFreight         = iSupplierInvoiceRow.iUnitFreight;
        iAccountNr           = iSupplierInvoiceRow.iAccountNr;
        //this.iProjectNr    = iSupplierInvoiceRow.iProjectNr;
        //this.iResultUnitNr = iSupplierInvoiceRow.iResultUnitNr;
        iProjectNumber       = iSupplierInvoiceRow.iProjectNumber;
        iResultUnitNumber    = iSupplierInvoiceRow.iResultUnitNumber;
        iProduct             = iSupplierInvoiceRow.iProduct;
        iAccount             = iSupplierInvoiceRow.iAccount;
        iProject             = iSupplierInvoiceRow.iProject;
        iResultUnit          = iSupplierInvoiceRow.iResultUnit;
    }

    /**
     *
     * @param iProduct
     */
    public SSSupplierInvoiceRow(SSProduct iProduct) {
        iProductNr    = iProduct.getNumber();
        iDescription  = iProduct.getDescription();
        iUnitprice    = iProduct.getSellingPrice();
        iUnit         = iProduct.getUnit();
        iAccountNr    = iProduct.getDefaultAccount(SSDefaultAccount.Sales);
        this.iProduct = iProduct;
        iQuantity     = null;
        iAccount      = null;
    }

    /**
     *
     * @param iRow
     */
    public SSSupplierInvoiceRow(SSPurchaseOrderRow iRow) {
        iDescription = iRow.getDescription();
        iProductNr   = iRow.getProductNr();
        iUnitprice   = iRow.getUnitPrice();
        iQuantity    = iRow.getQuantity();
        iUnit        = iRow.getUnit();
        iAccountNr   = iRow.getAccountNr();
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
        iAccount        = null;
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
        iResultUnitNumber = iResultUnitNr;
        iResultUnit       = null;
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
        iProjectNumber = iProjectNr;
        iProject       = null;
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
            iAccountNr = -1;
        } else {
            iAccountNr = iAccount.getNumber();
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
     * @param iProducts
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
        this.iProduct = iProduct;
        iProductNr    = iProduct == null ? null : iProduct.getNumber();

        if(iProduct != null){
            iDescription = iProduct.getDescription();
            iUnitprice   = iProduct.getPurchasePrice();
            iUnitFreight = iProduct.getUnitFreight();
            iUnit        = iProduct.getUnit();
            iAccountNr   = iProduct.getDefaultAccount(SSDefaultAccount.Purchases);
            iAccount     = null;
            iQuantity    = 1;
        }
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @param iResultUnits
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
        this.iResultUnit  = iResultUnit;
        iResultUnitNumber = iResultUnit == null ? null : iResultUnit.getNumber();
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @param iProjects
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
        this.iProject  = iProject;
        iProjectNumber = iProject == null ? null : iProject.getNumber();

    }

    ////////////////////////////////////////////////////


    /**
     * Returns {@code true} if the product is contained in the row
     *
     * @param iProduct
     * @return if the row contains the product
     */
    public boolean hasProduct(SSProduct iProduct){
        return iProductNr != null && iProductNr.equals( iProduct.getNumber() );
    }




    
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
