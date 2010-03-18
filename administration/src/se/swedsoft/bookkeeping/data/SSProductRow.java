package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 09:26:41
 */
public class SSProductRow implements Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private String iProductNr;

    private String iDescription;

    private Integer iCount;



    private transient SSProduct iProduct;

    /**
     * Default constructor
     */
    public SSProductRow() {

    }

    /**
     * Copy constructor
     *
     * @param iProductRow
     */
    public SSProductRow(SSProductRow iProductRow) {
        iProductNr  = iProductRow.iProductNr;
        iCount      = iProductRow.iCount;
        iDescription= iProductRow.iDescription;
        iProduct    = iProductRow.iProduct;
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
    public void setProduct(String iProductNr) {
        this.iProductNr = iProductNr;
        iProduct        = null;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getDescription() {
        if(iDescription == null && iProduct != null){
            return iProduct.getDescription();
        } else {
            return iDescription;
        }
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
     public SSProduct getProduct() {
         return getProduct(SSDB.getInstance().getProducts());
     }

    /**
     *
     * @param iProducts
     * @return
     */
    public SSProduct getProduct(List<SSProduct> iProducts) {
        if(iProduct == null){
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

        if(iProduct != null){
            iCount       = 1;
            iDescription = iProduct.getDescription();

        }
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
     * A row is isValid of the product and count is set
     * @return
     */
    public boolean isValidRow() {
        return iProductNr != null && iCount != null;
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
        sb.append(iCount);

        return sb.toString();
    }

}


