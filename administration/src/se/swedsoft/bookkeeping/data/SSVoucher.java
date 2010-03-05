/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class SSVoucher implements Serializable, Cloneable, SSTableSearchable  {

    /**
     * Constant for serialization versioning.
     */
    static final long serialVersionUID = 1L;


    private int iNumber;

    private Date iDate;

    private String iDescription;

    private SSVoucher iCorrects;

    private SSVoucher iCorrectedBy;

    private List<SSVoucherRow> iVoucherRows;


    /**
     * Default constructor.
     */
    public SSVoucher() {
        iDate        = SSVoucherMath.getNextVoucherDate();
        iVoucherRows = new ArrayList<SSVoucherRow>();
        doAutoIncrecement();
    }

    public SSVoucher(Integer iNumber) {
        iDate = SSVoucherMath.getNextVoucherDate();
        iVoucherRows = new ArrayList<SSVoucherRow>();
        this.iNumber = iNumber;
    }

    /**
     * Copy constructor.
     *
     * @param voucher The voucher to copy.
     */
    public SSVoucher(SSVoucher voucher) {
        copyFrom(voucher);
    }

    /**
     *
     * @param pVoucher
     */
    public void copyFrom(SSVoucher pVoucher) {
        iNumber      = pVoucher.iNumber;
        iDate        = pVoucher.iDate;
        iDescription = pVoucher.iDescription;
        iCorrects    = pVoucher.iCorrects;
        iCorrectedBy = pVoucher.iCorrectedBy;
        iVoucherRows = new LinkedList<SSVoucherRow>();

        for (SSVoucherRow iVoucherRow : pVoucher.iVoucherRows) {
            iVoucherRows.add(new SSVoucherRow(iVoucherRow));
        }

    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public int getNumber() {
        return iNumber;
    }

    /**
     *
     * @param number
     */
    public void setNumber(int number) {
        iNumber = number;
    }

    /**
     * Sets the number of this voucher as the maxinum mumber + 1
     */
    public void doAutoIncrecement() {
            int iNumber = SSVoucherMath.getMaxNumber();
            setNumber(iNumber + 1);
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        iDate = date;
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
     * @param description
     */
    public void setDescription(String description) {
        iDescription = description;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSVoucher getCorrects() {
        return iCorrects;
    }

    /**
     *
     * @param corrects
     */
    public void setCorrects(SSVoucher corrects) {
        iCorrects = corrects;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSVoucher getCorrectedBy() {
        return iCorrectedBy;
    }

    /**
     *
     * @param correctedBy
     */
    public void setCorrectedBy(SSVoucher correctedBy) {
        iCorrectedBy = correctedBy;
    }

    ////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSVoucherRow> getRows() {
        return iVoucherRows;
    }

    /**
     *
     * @param pVoucherRow
     */
    public void addVoucherRow(SSVoucherRow pVoucherRow) {
        iVoucherRows.add(pVoucherRow);
    }


    /**
     *
     * @param iAccount
     * @param iDebet
     * @param iCredit
     */
    public void addVoucherRow(SSAccount iAccount, BigDecimal iDebet, BigDecimal iCredit) {
        SSVoucherRow iVoucherRow = new SSVoucherRow();

        iVoucherRow.setAccount( iAccount );
        iVoucherRow.setDebet  ( iDebet   );
        iVoucherRow.setCredit ( iCredit  );

        iVoucherRows.add(iVoucherRow);
    }

    /**
     *
     * @param iAccount
     * @param iValue
     */
    public void addVoucherRow(SSAccount iAccount, BigDecimal iValue) {
        // Dont add a empty row
        if(iValue == null || iValue.signum() == 0) return;

        SSVoucherRow iVoucherRow = new SSVoucherRow();

        iVoucherRow.setAccount( iAccount );

        // Add the rounding
        if( iValue.signum() > 0){
            iVoucherRow.setDebet  ( iValue      );
            iVoucherRow.setCredit ( null);
        } else {
            iVoucherRow.setDebet  ( null   );
            iVoucherRow.setCredit ( iValue.abs()  );
        }

        iVoucherRows.add(iVoucherRow);
    }


    /**
     *
     * @param row
     * @return
     */
    public boolean removeVoucherRow(SSVoucherRow row) {
        return iVoucherRows.remove(row);
    }

    /**
     *
     * @param rows
     */
    public void setVoucherRows(List<SSVoucherRow> rows) {
        iVoucherRows = rows;
    }

    ////////////////////////////////////////////////////////////////////


    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if(obj instanceof SSVoucher){
            SSVoucher iVoucher = (SSVoucher) obj;

            return iNumber == iVoucher.iNumber;
        }
        return super.equals(obj);
    }


    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        StringBuilder sb = new StringBuilder();

        sb.append( iNumber );
        sb.append( ", " );
        sb.append( iDescription);
        sb.append( ", " );
        sb.append( iFormat.format(iDate) ); /*
        sb.append( ", " );
        sb.append( iVoucherRows.size() );
        sb.append( " rows.{\n" );
        for (SSVoucherRow row : iVoucherRows) {
            sb.append("  " );
            sb.append(row );
            sb.append("\n");
        }
        sb.append( "}\n" );   */
        return sb.toString();
    }


    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return Integer.toString(iNumber);
    }





    /**
     * Creates a new voucher with the number as the lastest voucher number + 1
     * @return The voucher

    public static SSVoucher newVoucher(){
        SSVoucher        iVoucher  = new SSVoucher();
        SSVoucher        iPrevious = SSVoucherMath.getPreviousVoucher();
        SSNewAccountingYear iYear     = SSDB.getInstance().getCurrentYear();

        Date iDate = iPrevious != null ? iPrevious.getDate() : (iYear != null) ? iYear.getFrom() :  new Date();

        iVoucher.doAutoIncrecement();
        iVoucher.setDate  (iDate      );

        return iVoucher;
    }

   */

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        //iDate=null;
        //iDescription=null;
        //iCorrects=null;
        //iCorrectedBy=null;
        //iVoucherRows.removeAll(iVoucherRows);
    }
}
