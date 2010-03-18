package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-sep-18
 * Time: 14:50:29
 */
public class SSInventory implements Serializable {

    private static final long serialVersionUID = 5324014159041899233L;

    public Integer iNumber;

    public Date iDate;

    public String iText;


    public List<SSInventoryRow> iRows;

    /**
     *
     */
    public SSInventory() {
        iNumber = 0;
        iDate   = new Date();
        iText   = "";
        iRows   = new LinkedList<SSInventoryRow>();

        doAutoIncrement();
    }

    /**
     * Copy constructor
     * 
     * @param iInventory
     */
    public SSInventory(SSInventory iInventory) {
        copyFrom(iInventory);
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     */
    public void doAutoIncrement(){
        iNumber = 1;

        List<SSInventory> iInventories = SSDB.getInstance().getInventories();
        for (SSInventory iInventory : iInventories) {
            if(iInventory.iNumber >= iNumber){
                iNumber = iInventory.iNumber + 1;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 
     * @param iInventory
     */
    public void copyFrom(SSInventory iInventory){
        iNumber = iInventory.iNumber;
        iDate   = iInventory.iDate;
        iText   = iInventory.iText;
        iRows   = new LinkedList<SSInventoryRow>();

        for (SSInventoryRow iRow : iInventory.iRows) {
            iRows.add( new SSInventoryRow(iRow) );
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public Integer getNumber() {
        return iNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(Integer iNumber) {
        this.iNumber = iNumber;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getText() {
        return iText;
    }

    /**
     *
     * @param iText
     */
    public void setText(String iText) {
        this.iText = iText;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSInventoryRow> getRows() {
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSInventoryRow> iRows) {
        this.iRows = iRows;
    }


    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Return the correction for the supplied product.
     *
     * @param iProduct
     * @return the correction
     */
    public Integer getChange(SSProduct iProduct){
        Integer iSum = 0;

        for (SSInventoryRow iRow : iRows) {
            if( iRow.hasProduct( iProduct) ){
                Integer iChange = iRow.getChange();

                if(iChange != null) iSum = iSum + iChange;
            }
        }
        return iSum;

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
        if (!(obj instanceof SSInventory)) {
            return false;
        }
        return iNumber.equals(((SSInventory) obj).iNumber);
    }


}
