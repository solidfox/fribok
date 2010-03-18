package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Johan Gunnarsson
 * Date: 2007-mars-22
 * Time: 10:43:47
 */
public class SSAutoDist implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Kontonummer
    private Integer iAccountNumber;

    // Beskrivning
    private String iDescription;

    // Belopp
    private BigDecimal iAmount;

    // Automatfördelningsrader
    private List<SSAutoDistRow> iRows;

    //Transient referens till kontot
    private transient SSAccount iAccount;

    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSAutoDist() {
        iRows           = new LinkedList<SSAutoDistRow>();
        iAmount         = new BigDecimal(0.0);
    }

    /**
     *
     * @param iAccount
     */
    public SSAutoDist(SSAccount iAccount) {
        iRows           = new LinkedList<SSAutoDistRow>();
        iAmount         = new BigDecimal(0.0);
        this.iAccount   = iAccount;
        iAccountNumber  = iAccount.getNumber();
    }


    /**
     * Copy constructor
     *
     * @param iAutoDist
     */
    public SSAutoDist(SSAutoDist iAutoDist) {
        copyFrom(iAutoDist);
    }


    /**
     * Clone constructor
     *
     * @param iAutoDist
     * @param iAccountNumber
     */
    public SSAutoDist(SSAutoDist iAutoDist, Integer iAccountNumber) {
        copyFrom(iAutoDist);

        this.iAccountNumber = iAccountNumber;
    }
    ////////////////////////////////////////////////////

    /**
     *
     * @param iAutoDist
     */
    public void copyFrom(SSAutoDist iAutoDist) {
        iAccountNumber = iAutoDist.iAccountNumber;
        iAccount       = iAutoDist.iAccount;
        iDescription   = iAutoDist.iDescription;
        iAmount        = iAutoDist.iAmount;
        iRows          = new LinkedList<SSAutoDistRow>();

        // Copy all rows
        for(SSAutoDistRow iRow : iAutoDist.iRows){
            iRows.add( new SSAutoDistRow(iRow) );
        }
    }



    ////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public Integer getNumber() {
        return iAccountNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setAccountNumber(Integer iNumber) {
        iAccountNumber = iNumber;
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
     * @param iText
     */
    public void setDescrition(String iText) {
        iDescription = iText;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public List<SSAutoDistRow> getRows() {
        if(iRows == null) iRows = new LinkedList<SSAutoDistRow>();
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSAutoDistRow> iRows) {
        this.iRows = iRows;
    }

    ////////////////////////////////////////////////////

    public BigDecimal getAmount() {
        return iAmount;
    }



    public void setAmount(BigDecimal iAmount) {
        this.iAmount = iAmount;
    }

    ///////////////////////////////////////////////////

    public SSAccount getAccount() {
        return getAccount(SSDB.getInstance().getAccounts());
    }

    public SSAccount getAccount(List<SSAccount> iAccounts ) {
        if(iAccount == null && iAccountNumber != null){
            for (SSAccount iCurrent : iAccounts) {
                if(iAccountNumber.equals(iCurrent.getNumber() )){
                    iAccount = iCurrent;
                    break;
                }
            }
        }
        return iAccount;
    }

    public void setAccount(SSAccount iAccount) {
        if(iAccount == null) return;
        this.iAccount = iAccount;
        iAccountNumber = iAccount.getNumber();
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
        sb.append(iAccountNumber);
        sb.append(", ");
        sb.append(iDescription);
        return sb.toString();
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iAccountNumber == null ? null : iAccountNumber.toString();
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
        if (!(obj instanceof SSAutoDist)) {
            return false;
        }
        return iAccountNumber.equals(((SSAutoDist)obj).getNumber());
    }
}

