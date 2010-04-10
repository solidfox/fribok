package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-sep-27
 * Time: 15:02:20
 */
public class SSOutdeliveryRow implements Serializable {

    private static final long serialVersionUID = -9210187383331088946L;

    private String iProductNr;

    private Integer iChange;

    private transient SSProduct iProduct;

    /**
     *
     */
    public SSOutdeliveryRow() {}

    /**
     *
     * @param iOutdeliveryRow
     */
    public SSOutdeliveryRow(SSOutdeliveryRow iOutdeliveryRow) {
        copyFrom(iOutdeliveryRow);
    }

    /**
     *
     * @param iProduct
     * @param iChange
     */
    public SSOutdeliveryRow(SSProduct iProduct, int iChange) {
        this.iChange = iChange;
        this.iProduct = iProduct;
        iProductNr = iProduct == null ? null : iProduct.getNumber();

    }

    // ///////////////////////////////////////////////////////////////////////////

    public void copyFrom(SSOutdeliveryRow iOutdeliveryRow) {
        iProductNr = iOutdeliveryRow.iProductNr;
        iChange = iOutdeliveryRow.iChange;
        iProduct = iOutdeliveryRow.iProduct;
    }

    // ///////////////////////////////////////////////////////////////////////////

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

    // ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getChange() {
        return iChange;
    }

    /**
     *
     * @param iChange
     */
    public void setChange(Integer iChange) {
        this.iChange = iChange;
    }

    // ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSProduct getProduct() {
        if (iProduct == null && iProductNr != null) {
            List<SSProduct> iProducts = SSDB.getInstance().getProducts();

            for (SSProduct iCurrent : iProducts) {
                if (iProductNr.equals(iCurrent.getNumber())) {
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
        iProductNr = iProduct == null ? null : iProduct.getNumber();
    }

    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns true if the product is the same as in this row
     *
     * @param iProduct
     * @return
     */
    public boolean hasProduct(SSProduct iProduct) {
        return iProductNr != null && iProductNr.equals(iProduct.getNumber());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.data.SSOutdeliveryRow");
        sb.append("{iChange=").append(iChange);
        sb.append(", iProduct=").append(iProduct);
        sb.append(", iProductNr='").append(iProductNr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
