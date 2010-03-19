package se.swedsoft.bookkeeping.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Johan Gunnarsson
 * Date: 2007-nov-22
 * Time: 16:47:12
 */
public class SSOwnReport implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer iId;

    private String iName;

    private List<SSOwnReportRow> iHeadings;

    private String iProjectNr;

    private String iResultUnitNr;

    public SSOwnReport(){
        iId = -1;
        iName = "";
        iHeadings = new LinkedList<SSOwnReportRow>();
        iProjectNr = null;
        iResultUnitNr = null;
    }

    /**
     *
     * @return iNumber - Nummret för rapporten
     */
    public Integer getId(){
        return iId;
    }

    public void setId(Integer iId) {
        this.iId = iId;
    }

    /**
     *
     * @return iName - Namnet på rapporten
     */
    public String getName() {
        return iName;
    }

    public void setName(String iName) {
        this.iName = iName;
    }

    /**
     *
     * @return iHeadings - Rubriker samt dess typ
     */
    public List<SSOwnReportRow> getHeadings() {
        return iHeadings;
    }

    public void setHeadings(List<SSOwnReportRow> iHeadings) {
        this.iHeadings = iHeadings;
    }

    /**
     *
     * @return iProjectNumber - Projekt för rapporten
     */
    public String getProjectNr() {
        return iProjectNr;
    }

    public void setProjectNr(String iProjectNumber) {
        iProjectNr = iProjectNumber;
    }

    /**
     *
     * @return iResultUnitNumber - Resultatenhet för rapporten
     */
    public String getResultUnitNr() {
        return iResultUnitNr;
    }

    public void setResultUnitNr(String iResultUnitNr) {
        this.iResultUnitNr = iResultUnitNr;
    }

    /**
     *
     * @param obj - The object to compare with
     * @return Whether obj is equal to this object
     */

    public boolean equals(Object obj) {
        if(obj == null) return false;

        if(obj instanceof SSOwnReport){
            SSOwnReport iOwnReport = (SSOwnReport) obj;
            if(iOwnReport.iId.equals(iId)) return true;
        }
        return false;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSOwnReport");
        sb.append("{iHeadings=").append(iHeadings);
        sb.append(", iId=").append(iId);
        sb.append(", iName='").append(iName).append('\'');
        sb.append(", iProjectNr='").append(iProjectNr).append('\'');
        sb.append(", iResultUnitNr='").append(iResultUnitNr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
