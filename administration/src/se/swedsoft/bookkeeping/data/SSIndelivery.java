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
public class SSIndelivery implements Serializable {

    private static final long serialVersionUID = -5537699296769492741L;

    public Integer iNumber;

    public Date iDate;

    public String iText;

    private List<SSIndeliveryRow> iRows;

    /**
     *
     */
    public SSIndelivery() {
        iDate = new Date();
        iText = null;
        iRows = new LinkedList<SSIndeliveryRow>();

        doAutoIncrement();
    }

    /**
     * Copy constructor
     * 
     * @param iIndelivery
     */
    public SSIndelivery(SSIndelivery iIndelivery) {
        copyFrom(iIndelivery);
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    public void doAutoIncrement(){
        iNumber = 1;

        List<SSIndelivery> iIndeliveries = SSDB.getInstance().getIndeliveries();

        for (SSIndelivery iIndelivery : iIndeliveries) {
            if(iIndelivery.getNumber() >= iNumber){
                iNumber = iIndelivery.getNumber() + 1;
            }
        }

    }

    /**
     *
     * @param iIndelivery
     */
    public void copyFrom(SSIndelivery iIndelivery){
        iNumber = iIndelivery.iNumber;
        iDate   = iIndelivery.iDate;
        iText   = iIndelivery.iText;
        iRows   = new LinkedList<SSIndeliveryRow>();

        for (SSIndeliveryRow iRow : iIndelivery.getRows()) {
            iRows.add( new SSIndeliveryRow(iRow) );
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
    public List<SSIndeliveryRow> getRows() {
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSIndeliveryRow> iRows) {
        this.iRows = iRows;
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
        if (!(obj instanceof SSIndelivery)) {
            return false;
        }
        return iNumber.equals(((SSIndelivery)obj).getNumber());
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Return the correction for the supplied product.
     *
     * @param iProduct
     * @return the correction
     */
    public Integer getChange(SSProduct iProduct){
        Integer iChange = 0;
        for (SSIndeliveryRow iRow : iRows) {
            if( iRow.hasProduct( iProduct) ){
                iChange = iChange + iRow.getChange();
            }

        }

        return iChange;

    }






}
