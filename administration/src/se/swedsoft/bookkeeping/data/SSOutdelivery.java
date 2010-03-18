package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-sep-25
 * Time: 09:31:58
 */
public class SSOutdelivery implements Serializable {

    private static final long serialVersionUID = -5537699296769492741L;

    public Integer iNumber;

    public Date iDate;

    public String iText;

    private List<SSOutdeliveryRow> iRows;

    /**
     *
     */
    public SSOutdelivery() {
        iDate = new Date();
        iText = null;
        iRows = new LinkedList<SSOutdeliveryRow>();

        doAutoIncrement();
    }

    /**
     * Copy constructor
     * 
     * @param iOutdelivery
     */
    public SSOutdelivery(SSOutdelivery iOutdelivery) {
        copyFrom(iOutdelivery);
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    public void doAutoIncrement(){
        iNumber = 1;

        List<SSOutdelivery> iOutdeliveries = SSDB.getInstance().getOutdeliveries();

        for (SSOutdelivery iOutdelivery : iOutdeliveries) {
            if(iOutdelivery.getNumber() >= iNumber){
                iNumber = iOutdelivery.getNumber() + 1;
            }
        }

    }

    /**
     *
     * @param iOutdelivery
     */
    public void copyFrom(SSOutdelivery iOutdelivery){
        iNumber = iOutdelivery.iNumber;
        iDate   = iOutdelivery.iDate;
        iText   = iOutdelivery.iText;
        iRows   = new LinkedList<SSOutdeliveryRow>();

        for (SSOutdeliveryRow iRow : iOutdelivery.getRows()) {
            iRows.add( new SSOutdeliveryRow(iRow) );
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
    public List<SSOutdeliveryRow> getRows() {
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSOutdeliveryRow> iRows) {
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
        for (SSOutdeliveryRow iRow : iRows) {
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
        if (!(obj instanceof SSOutdelivery)) {
            return false;
        }
        return iNumber.equals(((SSOutdelivery)obj).getNumber());
    }



}
