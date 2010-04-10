package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
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

    // Kontobeskrivning
    private String iDescription;

    // Procent
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

    // //////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSAutoDistRow() {}

    /**
     * Default constructor
     * @param iAccount
     */
    public SSAutoDistRow(SSAccount iAccount) {
        this.iAccount = iAccount;
        iAccountNr = iAccount.getNumber();
        iPercentage = new BigDecimal(0);
        iDebet = new BigDecimal(0);
        iCredit = new BigDecimal(0);
        iProject = null;
        iResultUnit = null;
    }

    /**
     * Copy constructor
     *
     * @param iRow
     */
    public SSAutoDistRow(SSAutoDistRow iRow) {
        copyFrom(iRow);
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @param iAutoDistRow
     */
    public void copyFrom(SSAutoDistRow iAutoDistRow) {
        iAccountNr = iAutoDistRow.iAccountNr;
        iDescription = iAutoDistRow.iDescription;
        iPercentage = iAutoDistRow.iPercentage;
        iDebet = iAutoDistRow.iDebet;
        iCredit = iAutoDistRow.iCredit;
        iProjectNr = iAutoDistRow.iProjectNr;
        iResultUnitNr = iAutoDistRow.iResultUnitNr;

        iAccount = iAutoDistRow.iAccount;
        iProject = iAutoDistRow.iProject;
        iResultUnit = iAutoDistRow.iResultUnit;
    }

    // //////////////////////////////////////////////////

    public Integer getAccountNr() {
        return iAccountNr;
    }

    public void setAccountNr(Integer iAccountNr) {
        this.iAccountNr = iAccountNr;
    }

    // //////////////////////////////////////////////////

    public String getDescription() {
        return iDescription;
    }

    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
    }

    // //////////////////////////////////////////////////

    public BigDecimal getPercentage() {
        return iPercentage;
    }

    public void setPercentage(BigDecimal iPercentage) {
        this.iPercentage = iPercentage;
    }

    // //////////////////////////////////////////////////

    public BigDecimal getDebet() {
        return iDebet;
    }

    public void setDebet(BigDecimal iDebet) {
        this.iDebet = iDebet;
    }

    // //////////////////////////////////////////////////

    public BigDecimal getCredit() {
        return iCredit;
    }

    public void setCredit(BigDecimal iCredit) {
        this.iCredit = iCredit;
    }

    // //////////////////////////////////////////////////

    public String getProjectNr() {
        return iProjectNr;
    }

    public void setProjectNr(String iProjectNr) {
        this.iProjectNr = iProjectNr;
    }

    // //////////////////////////////////////////////////

    public String getResultUnitNr() {
        return iResultUnitNr;
    }

    public void setResultUnitNr(String iResultUnitNr) {
        this.iResultUnitNr = iResultUnitNr;
    }

    // //////////////////////////////////////////////////

    public SSAccount getAccount() {
        return getAccount(SSDB.getInstance().getAccounts());
    }

    public void setAccount(SSAccount iAccount) {
        this.iAccount = iAccount;
        iAccountNr = iAccount.getNumber();
    }

    /**
     *
     * @param iAccounts
     * @return
     */
    public SSAccount getAccount(List<SSAccount> iAccounts) {
        if (iAccount == null && iAccountNr != null) {
            for (SSAccount iCurrent : iAccounts) {
                if (iAccountNr.equals(iCurrent.getNumber())) {
                    iAccount = iCurrent;
                    break;
                }
            }
        }
        return iAccount;
    }

    // //////////////////////////////////////////////////

    public SSNewProject getProject() {
        return getProject(SSDB.getInstance().getProjects());
    }

    public SSNewProject getProject(List<SSNewProject> iProjects) {
        if (iProject == null && iProjectNr != null) {
            for (SSNewProject iCurrent : iProjects) {
                if (iProjectNr.equals(iCurrent.getNumber())) {
                    iProject = iCurrent;
                    break;
                }
            }
        }
        return iProject;
    }

    public void setProject(SSNewProject iProject) {
        this.iProject = iProject;
        iProjectNr = iProject == null ? null : iProject.getNumber();
    }

    // //////////////////////////////////////////////////

    public SSNewResultUnit getResultUnit() {
        return getResultUnit(SSDB.getInstance().getResultUnits());

    }

    /**
     *
     * @param iResultUnits
     * @return
     */
    public SSNewResultUnit getResultUnit(List<SSNewResultUnit> iResultUnits) {
        if (iResultUnit == null && iResultUnitNr != null) {
            for (SSNewResultUnit iCurrent : iResultUnits) {
                if (iResultUnitNr.equals(iCurrent.getNumber())) {
                    iResultUnit = iCurrent;
                    break;
                }
            }
        }
        return iResultUnit;
    }

    public void setResultUnit(SSNewResultUnit iResultUnit) {
        this.iResultUnit = iResultUnit;
        iResultUnitNr = iResultUnit == null ? null : iResultUnit.getNumber();
    }

    // //////////////////////////////////////////////////
    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return String.valueOf(iAccountNr);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.data.SSAutoDistRow");
        sb.append("{iAccount=").append(iAccount);
        sb.append(", iAccountNr=").append(iAccountNr);
        sb.append(", iCredit=").append(iCredit);
        sb.append(", iDebet=").append(iDebet);
        sb.append(", iDescription='").append(iDescription).append('\'');
        sb.append(", iPercentage=").append(iPercentage);
        sb.append(", iProject=").append(iProject);
        sb.append(", iProjectNr='").append(iProjectNr).append('\'');
        sb.append(", iResultUnit=").append(iResultUnit);
        sb.append(", iResultUnitNr='").append(iResultUnitNr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
