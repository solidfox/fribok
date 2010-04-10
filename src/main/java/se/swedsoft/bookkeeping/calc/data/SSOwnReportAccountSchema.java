package se.swedsoft.bookkeeping.calc.data;


import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSOwnReport;
import se.swedsoft.bookkeeping.data.SSOwnReportRow;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportAccountRow;

import java.util.LinkedList;
import java.util.List;


/**
 * User: Johan Gunnarsson
 * Date: 2007-nov-29
 * Time: 08:48:12
 */
public class SSOwnReportAccountSchema {

    private List<SSOwnReportAccountGroup> iResultGroups;

    /**
     *
     */
    private SSOwnReportAccountSchema() {
        iResultGroups = new LinkedList<SSOwnReportAccountGroup>();
    }

    /**
     * Returns the result groups
     * @return
     */
    public List<SSOwnReportAccountGroup> getResultGroups() {
        return iResultGroups;
    }

    public static SSOwnReportAccountSchema getSchema(SSOwnReport iOwnReport) {

        SSOwnReportAccountSchema iSchema = new SSOwnReportAccountSchema();

        SSOwnReportAccountGroup iHeading1Group = new SSOwnReportAccountGroup();

        Integer iId1 = 0;
        Integer iId2 = 0;
        Integer iId3 = 0;

        for (SSOwnReportRow iOwnReportRow : iOwnReport.getHeadings()) {
            if (iOwnReportRow.getType() == SSHeadingType.HEADING1) {
                iId1++;
                // iId2 = 0;
                iHeading1Group = new SSOwnReportAccountGroup();
                iHeading1Group.setId(iId1);
                iHeading1Group.setName(iOwnReportRow.getHeading());
                iHeading1Group.setAccounts(null);
                iSchema.iResultGroups.add(iHeading1Group);
            } else if (iOwnReportRow.getType() == SSHeadingType.HEADING2) {
                iId2++;
                SSOwnReportAccountGroup iHeading2Group = new SSOwnReportAccountGroup();

                iHeading2Group.setId(iId2);
                iHeading2Group.setName(iOwnReportRow.getHeading());
                List<SSAccount> iAccounts = new LinkedList<SSAccount>();

                for (SSOwnReportAccountRow iAccountRow : iOwnReportRow.getAccountRows()) {
                    if (iAccountRow.getAccount() != null) {
                        iAccounts.add(iAccountRow.getAccount());
                    }
                }
                iHeading2Group.setAccounts(iAccounts);
                iHeading2Group.setSumTitle("Summa " + iOwnReportRow.getHeading());
                iHeading1Group.addAccountGroup(iHeading2Group);
            } else if (iOwnReportRow.getType() == SSHeadingType.HEADING3) {
                SSOwnReportAccountGroup iHeading3Group = new SSOwnReportAccountGroup();

                iId3 = iId2;
                iHeading3Group.setId(iId3);
                iHeading3Group.setName(iOwnReportRow.getHeading());
                iHeading3Group.setAccounts(null);
                iHeading3Group.setGroups(null);
                iHeading1Group.getGroups().add(iHeading3Group);
            }
        }
        return iSchema;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ResultGroups: {\n");
        for (SSOwnReportAccountGroup iLevelOne : iResultGroups) {
            sb.append(iLevelOne);
        }
        sb.append("}\n");

        return sb.toString();
    }
}
