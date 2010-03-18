package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * User: Andreas Lago
 * Date: 2006-jun-15
 * Time: 10:42:37
 *
 * Rad för inköpsorder
 */
public class SSPurchaseOrderRow implements Serializable {

    private static final long serialVersionUID = 4891634413753480921L;

    // Produktnummer
    private String iProductNr;
    // Beskrivning
    private String iDescription;
    // Leverantörens artikel nummer
    private String iSupplierArticleNr;
    // Inköpspris
    private BigDecimal iUnitprice;
    // Antal
    private Integer iQuantity;
    // Enhet
    private SSUnit iUnit;

    // Konto
    private Integer iAccountNr;

    // Transient product
    private transient SSProduct iProduct;

    ////////////////////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSPurchaseOrderRow() {
    }

    /**
     * Copy constructor
     *
     * @param iPurchaseOrderRow
     */
    public SSPurchaseOrderRow(SSPurchaseOrderRow iPurchaseOrderRow) {
        iProductNr         = iPurchaseOrderRow.iProductNr;
        iDescription       = iPurchaseOrderRow.iDescription;
        iSupplierArticleNr = iPurchaseOrderRow.iSupplierArticleNr;
        iUnitprice         = iPurchaseOrderRow.iUnitprice;
        iQuantity          = iPurchaseOrderRow.iQuantity;
        iUnit              = iPurchaseOrderRow.iUnit;
        iProduct           = iPurchaseOrderRow.iProduct;
        iAccountNr         = iPurchaseOrderRow.iAccountNr;
    }

    ////////////////////////////////////////////////////////////////////

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
        iProduct        = null;
    }

    ////////////////////////////////////////////////////////////////////

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
        if(getProduct() != null){
            String iProductDescription = iProduct.getDescription(iLocale);

            if(iProductDescription != null){
                return iProductDescription;
            }
        }
        return iDescription;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getSupplierArticleNr() {
        return iSupplierArticleNr;
    }

    /**
     *
     * @param iSupplierArticleNr
     */
    public void setSupplierArticleNr(String iSupplierArticleNr) {
        this.iSupplierArticleNr = iSupplierArticleNr;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getUnitPrice() {
        return iUnitprice;
    }

    /**
     *
     * @param iUnitprice
     */
    public void setUnitPrice(BigDecimal iUnitprice) {
        this.iUnitprice = iUnitprice;
    }

    ////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSProduct getProduct() {
        return getProduct(SSDB.getInstance().getProducts());
    }
    /**
     *
     * @param iProducts
     * @return
     */
    public SSProduct getProduct(List<SSProduct> iProducts) {
        if(iProduct == null && iProductNr != null){
            for (SSProduct iCurrent : iProducts) {
                if(iCurrent.getNumber().equals(iProductNr)){
                    iProduct = iCurrent;
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

        if( iProduct != null){
            iDescription       = iProduct.getDescription();
            iUnitprice         = iProduct.getPurchasePrice();
            iUnit              = iProduct.getUnit();
            iSupplierArticleNr = iProduct.getSupplierProductNr();
            iAccountNr         = iProduct.getDefaultAccount(SSDefaultAccount.Purchases);
            iQuantity          = 1;
        }
    }

    ////////////////////////////////////////////////////////////////////

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
    }

    ////////////////////////////////////////////////////////////////////

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
     * Retuns the sum of this row
     *
     * @return the sum
     */
    public BigDecimal getSum(){
        // If either of the unitprice or count is null we cant have a sum
        if(iUnitprice == null || iQuantity == null) return null;


        return iUnitprice.multiply(new BigDecimal(iQuantity));
    }


}
