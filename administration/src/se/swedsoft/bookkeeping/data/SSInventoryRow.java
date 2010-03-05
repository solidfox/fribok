package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-sep-27
 * Time: 15:02:20
 */
public class SSInventoryRow implements Serializable {

    private static final long serialVersionUID = 6027445105394812104L;

    private String iProductNr;
    // Lagerantal
    private Integer iQuantity;

    // Skillnad mot inventerat antal
    private Integer iChange;

    private transient SSProduct iProduct;


    /**
     *
     */
    public SSInventoryRow() {
        this.iChange = 0;
        this.iQuantity = 0;
        this.iProductNr = null;
        this.iProduct = null;
    }

    /**
     * 
     * @param iInventoryRow
     */
    public SSInventoryRow(SSInventoryRow iInventoryRow) {
        copyFrom(iInventoryRow);
    }

    /**
     *
     * @param iProduct
     * @param iChange
     */
    public SSInventoryRow(SSProduct iProduct, int iChange) {
        this.iChange    = iChange;
        this.iProduct   = iProduct;
        this.iProductNr = iProduct == null ? null : iProduct.getNumber();

    }

    /////////////////////////////////////////////////////////////////////////////


    public void copyFrom(SSInventoryRow iInventoryRow) {
        this.iProductNr = iInventoryRow.iProductNr;
        this.iChange    = iInventoryRow.iChange;
        this.iQuantity  = iInventoryRow.iQuantity;
        this.iProduct   = iInventoryRow.iProduct;
    }

    /////////////////////////////////////////////////////////////////////////////

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
    /////////////////////////////////////////////////////////////////////////////

    /**
     *  Lagerantal
     *
     * @return
     */
    public Integer getStockQuantity() {
        return iQuantity;
    }

    /**
     * Lagerantal
     *
     * @param iQuantity
     */
    public void setStockQuantity(Integer iQuantity) {
        this.iQuantity = iQuantity;
    }

    /////////////////////////////////////////////////////////////////////////////

    /**
     * Inventerat antal
     *
     * @return
     */
    public Integer getInventoryQuantity() {
        if(iQuantity == null || iChange == null) return null;

        return iQuantity + iChange;
    }

    /**
     * Inventerat antal
     *
     * @param iValue
     */
    public void setInventoryQuantity(Integer iValue) {
        if(iQuantity == null || iValue == null) return;

        this.iChange = iValue - iQuantity;
    }

    /////////////////////////////////////////////////////////////////////////////


    /**
     * Skillnad
     *
     * @return
     */
    public Integer getChange() {
        return iChange;
    }

    /**
     * Skillnad
     *
     * @param iChange
     */
    public void setChange(Integer iChange) {
        this.iChange = iChange;
    }

    /////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSProduct getProduct() {
        return getProduct(SSDB.getInstance().getProducts());
    }

    /**
     *
     * @return
     */
    public SSProduct getProduct(List<SSProduct> iProducts) {
        if(iProduct == null && iProductNr != null){
            for (SSProduct iCurrent : iProducts) {
                if(iProductNr.equals( iCurrent.getNumber() )){
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
        this.iProduct   = iProduct;
        this.iProductNr = iProduct == null ? null : iProduct.getNumber();
    }

    /////////////////////////////////////////////////////////////////////////////

    /**
     * Returns true if the product is the same as in this row
     *
     * @param iProduct
     * @return
     */
    public boolean hasProduct(SSProduct iProduct) {
        return iProductNr != null && iProductNr.equals( iProduct.getNumber() );
    }
}
