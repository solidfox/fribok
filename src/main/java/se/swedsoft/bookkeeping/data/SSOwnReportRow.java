package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportAccountRow;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Johan Gunnarsson
 * Date: 2007-nov-23
 * Time: 15:37:54
 */

public class SSOwnReportRow implements SSTableSearchable, Serializable {

    static final long serialVersionUID = 1L;
    
    private SSHeadingType iType;
    private String iHeading;

    List<SSOwnReportAccountRow> iAccounts;

    public SSOwnReportRow(){
        iType = null;
        iHeading = null;
        iAccounts = new LinkedList<SSOwnReportAccountRow>();
    }

    public SSOwnReportRow(SSOwnReportRow iRow){
        iType = iRow.iType;
        iHeading = iRow.iHeading;
        iAccounts = iRow.iAccounts;
    }

    public SSHeadingType getType() {
        return iType;
    }

    public void setType(SSHeadingType iType) {
        this.iType = iType;
    }

    public String getHeading() {
        return iHeading;
    }

    public void setHeading(String iHeading) {
        this.iHeading = iHeading;
    }

    public List<SSOwnReportAccountRow> getAccountRows(){
        return iAccounts;
    }

    public boolean equals(Object obj){
        if(obj == null) return false;
        if(iHeading == null && ((SSOwnReportRow)obj).iHeading == null) return true;

        if(obj instanceof SSOwnReportRow){
            return iHeading != null && iHeading.equals(((SSOwnReportRow)obj).iHeading);
        }
        return false;
    }

    public String toRenderString(){
        return iHeading;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSOwnReportRow");
        sb.append("{iAccounts=").append(iAccounts);
        sb.append(", iHeading='").append(iHeading).append('\'');
        sb.append(", iType=").append(iType);
        sb.append('}');
        return sb.toString();
    }
}
