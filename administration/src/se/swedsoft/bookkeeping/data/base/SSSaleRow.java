package se.swedsoft.bookkeeping.data.base;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 15:55:59
 */
public class SSSaleRow implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    private static final long serialVersionUID = 1L;

    // Productnr
    private String iProductNr;
    // Beskrivning
    private String iDescription;
    // Enhetspris
    private BigDecimal iUnitprice;
    // Antal
    private Integer iCount;
    // Enhet
    private SSUnit iUnit;
    // Rabatt
    private BigDecimal iDiscount;
    // Momskod
    private SSTaxCode iTaxCode;
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
    public SSSaleRow() {
    }

    /**
     * Copy constructor
     *
     * @param
     */
    public SSSaleRow(SSSaleRow iTenderRow) {
        this.iDescription  = iTenderRow.iDescription;
        this.iProductNr    = iTenderRow.iProductNr;
        this.iUnitprice    = iTenderRow.iUnitprice;
        this.iCount        = iTenderRow.iCount;
        this.iUnit         = iTenderRow.iUnit;
        this.iDiscount     = iTenderRow.iDiscount;
        this.iTaxCode      = iTenderRow.iTaxCode ;
        this.iProduct      = iTenderRow.iProduct;
        this.iAccountNr    = iTenderRow.iAccountNr;
        //this.iProjectNr    = iTenderRow.iProjectNr;
        this.iProjectNumber = iTenderRow.iProjectNumber;
        //this.iResultUnitNr = iTenderRow.iResultUnitNr;
        this.iResultUnitNumber = iTenderRow.iResultUnitNumber;
        this.iAccount      = iTenderRow.iAccount;
        this.iProject      = iTenderRow.iProject;
        this.iResultUnit   = iTenderRow.iResultUnit;
    }

    /**
     *
     * @param iProduct
     */
    public SSSaleRow(SSProduct iProduct) {
        this.iProductNr   = iProduct.getNumber();
        this.iDescription = iProduct.getDescription();
        this.iUnitprice   = iProduct.getSellingPrice();
        this.iUnit        = iProduct.getUnit();
        this.iTaxCode     = iProduct.getTaxCode();
        this.iAccountNr   = iProduct.getDefaultAccount(SSDefaultAccount.Sales);
        this.iProduct     = iProduct;
        this.iDiscount    = null;
        this.iCount       = null;
        this.iAccount     = null;
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
        this.iProduct   = null;
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
     * @return
     */
    public String getDescription(Locale iLocale) {
        if(getProduct() != null){
            String iProductDescription = iProduct.getDescription(iLocale);

            if(iProductDescription != null){
                return iProductDescription;
            }
        }
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
    public Integer getQuantity() {
        return iCount;
    }

    /**
     *
     * @param iCount
     */
    public void setQuantity(Integer iCount) {
        this.iCount = iCount;
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
    public BigDecimal getDiscount() {
        return iDiscount;
    }

    /**
     *
     * @param iDiscount
     */
    public void setDiscount(BigDecimal iDiscount) {
        this.iDiscount = iDiscount;
    }

    /**
     *
     * @return the normalized discount
     */
    public BigDecimal getNormalizedDiscount() {
        return iDiscount == null ? new BigDecimal(0.0) : iDiscount.scaleByPowerOfTen(-2);

    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSTaxCode getTaxCode() {
        return iTaxCode;
    }

    /**
     *
     * @param iTaxCode
     */
    public void setTaxCode(SSTaxCode iTaxCode) {
        this.iTaxCode = iTaxCode;
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
        if(iProduct == null && iProductNr != null){
            for(SSProduct iCurrent: iProducts){
                if(iProductNr.equals(iCurrent.getNumber())){
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
        this.iProductNr   = iProduct.getNumber();
        this.iDescription = iProduct.getDescription();
        this.iUnitprice   = iProduct.getSellingPrice();
        this.iUnit        = iProduct.getUnit();
        this.iTaxCode     = iProduct.getTaxCode();
        this.iAccountNr   = iProduct.getDefaultAccount(SSDefaultAccount.Sales);
        this.iCount       = 1;
        this.iAccount     = null;
        this.iProduct     = iProduct;
    }

    public void setProductOnly(SSProduct iProduct) {
        this.iProductNr   = iProduct.getNumber();
        this.iDescription = iProduct.getDescription();
        this.iUnitprice   = iProduct.getSellingPrice();
        this.iUnit        = iProduct.getUnit();
        this.iTaxCode     = iProduct.getTaxCode();
        this.iAccountNr   = iProduct.getDefaultAccount(SSDefaultAccount.Sales);
        this.iAccount     = null;
        this.iProduct     = iProduct;
        this.iProject = iProduct.getProject();
        this.iProjectNumber = iProduct.getProjectNr();
        this.iResultUnit = iProduct.getResultUnit();
        this.iResultUnitNumber = iProduct.getResultUnitNr();
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
        sb.append(iCount);
        sb.append(" ");
        sb.append(iUnit);
        sb.append(", ");
        sb.append(iDiscount);
        sb.append(", ");
        sb.append(iTaxCode);
        sb.append(" (");
        sb.append(iProduct);
        sb.append(")");
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
     * Returns <code>true</code> if the product is contained in the row
     *
     * @param iProduct
     * @return if the row contains the product
     */
    public boolean hasProduct(SSProduct iProduct){
        return iProductNr != null && iProductNr.equals( iProduct.getNumber() );
    }



    /**
     * Calculate the sum of the row as (Count * Unitprice) * (1 / iDiscount)
     * 
     * @return the sum of the row
     */
    public BigDecimal getSum() {
        // If either of the unitprice or count is null we cant have a sum
        if(iUnitprice == null || iCount == null) return null;

        // Calculate the sum of the products
        BigDecimal iSum = iUnitprice.multiply( new BigDecimal(iCount) );

        // No discount
        if(iDiscount == null) return iSum;

        // Subtract the discount from the sum
        return iSum.subtract( iSum.multiply( getNormalizedDiscount() )  );
    }

}
