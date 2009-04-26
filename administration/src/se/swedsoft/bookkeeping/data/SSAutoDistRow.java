package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * User: Johan Gunnarsson
 * Date: 2007-mar-22
 * Time: 10:52:13
 */
public class SSAutoDistRow implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Kontonummer
    private Integer iAccountNr;

    //Kontobeskrivning
    private String iDescription;

    //Procent
    private BigDecimal iPercentage;

    // Debet
    private BigDecimal iDebet;

    // Kredit
    private BigDecimal iCredit;

    // Projekt
    private String iProjectNr;

    // Resultatenhet
    private String iResultUnitNr;

    // Transient referens till kontot
    private transient SSAccount iAccount;

    // Transient referens till projektet
    private transient SSNewProject iProject;

    // Transient referens till kontot
    private transient SSNewResultUnit iResultUnit;




    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSAutoDistRow() {
    }

    /**
     * Default constructor
     */
    public SSAutoDistRow(SSAccount iAccount) {
        this.iAccount           = iAccount;
        this.iAccountNr         = iAccount.getNumber();
        this.iPercentage        = new BigDecimal(0.0);
        this.iDebet             = new BigDecimal(0.0);
        this.iCredit            = new BigDecimal(0.0);
        this.iProject           = null;
        this.iResultUnit        = null;
    }

    /**
     * Copy constructor
     *
     * @param iRow
     */
    public SSAutoDistRow(SSAutoDistRow iRow) {
        copyFrom(iRow);
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @param iAutoDistRow
     */
    public void copyFrom(SSAutoDistRow iAutoDistRow) {
        this.iAccountNr         = iAutoDistRow.iAccountNr;
        this.iDescription       = iAutoDistRow.iDescription;
        this.iPercentage        = iAutoDistRow.iPercentage;
        this.iDebet             = iAutoDistRow.iDebet;
        this.iCredit            = iAutoDistRow.iCredit;
        this.iProjectNr         = iAutoDistRow.iProjectNr;
        this.iResultUnitNr      = iAutoDistRow.iResultUnitNr;

        this.iAccount           = iAutoDistRow.iAccount;
        this.iProject           = iAutoDistRow.iProject;
        this.iResultUnit        = iAutoDistRow.iResultUnit;
    }

    ////////////////////////////////////////////////////

    public Integer getAccountNr() {
        return iAccountNr;
    }

    public void setAccountNr(Integer iAccountNr) {
        this.iAccountNr = iAccountNr;
    }

    ////////////////////////////////////////////////////

    public String getDescription() {
        return iDescription;
    }

    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
    }

    ////////////////////////////////////////////////////

    public BigDecimal getPercentage() {
        return iPercentage;
    }

    public void setPercentage(BigDecimal iPercentage) {
        this.iPercentage = iPercentage;
    }

    ////////////////////////////////////////////////////

    public BigDecimal getDebet() {
        return iDebet;
    }

    public void setDebet(BigDecimal iDebet) {
        this.iDebet = iDebet;
    }

    ////////////////////////////////////////////////////

    public BigDecimal getCredit() {
        return iCredit;
    }

    public void setCredit(BigDecimal iCredit) {
        this.iCredit = iCredit;
    }

    ////////////////////////////////////////////////////

    public String getProjectNr() {
        return iProjectNr;
    }

    public void setProjectNr(String iProjectNr) {
        this.iProjectNr = iProjectNr;
    }

    ////////////////////////////////////////////////////

    public String getResultUnitNr() {
        return iResultUnitNr;
    }

    public void setResultUnitNr(String iResultUnitNr) {
        this.iResultUnitNr = iResultUnitNr;
    }

    ////////////////////////////////////////////////////

    public SSAccount getAccount() {
        return getAccount(SSDB.getInstance().getAccounts());
    }

    public void setAccount(SSAccount iAccount) {
        this.iAccount = iAccount;
        this.iAccountNr = iAccount.getNumber();
    }

    /**
     *
     * @return
     */
    public SSAccount getAccount(List<SSAccount> iAccounts ) {
        if(iAccount == null && iAccountNr != null){
            for (SSAccount iCurrent : iAccounts) {
                if(iAccountNr.equals(iCurrent.getNumber() )){
                    iAccount = iCurrent;
                    break;
                }
            }
        }
        return iAccount;
    }

    ////////////////////////////////////////////////////

    public SSNewProject getProject() {
        return getProject( SSDB.getInstance().getProjects() );
    }

    public SSNewProject getProject(List<SSNewProject> iProjects) {
        if(iProject == null && iProjectNr != null){
            for (SSNewProject iCurrent : iProjects) {
                if(iProjectNr.equals(iCurrent.getNumber() )){
                    iProject = iCurrent;
                    break;
                }
            }
        }
        return iProject;
    }

    public void setProject(SSNewProject iProject) {
        this.iProject   = iProject;
        this.iProjectNr = iProject == null ? null : iProject.getNumber();
    }

    ////////////////////////////////////////////////////

    public SSNewResultUnit getResultUnit() {
        return getResultUnit(SSDB.getInstance().getResultUnits());

    }
    /**
     *
     * @return
     */
    public SSNewResultUnit getResultUnit(List<SSNewResultUnit> iResultUnits) {
        if(iResultUnit == null && iResultUnitNr != null){
            for (SSNewResultUnit iCurrent : iResultUnits) {
                if(iResultUnitNr.equals(iCurrent.getNumber() )){
                    iResultUnit = iCurrent;
                    break;
                }
            }
        }
        return iResultUnit;
    }

    public void setResultUnit(SSNewResultUnit iResultUnit) {
        this.iResultUnit   = iResultUnit;
        this.iResultUnitNr = iResultUnit == null ? null : iResultUnit.getNumber();
    }

    ////////////////////////////////////////////////////
    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return "" + iAccountNr;
    }
}
