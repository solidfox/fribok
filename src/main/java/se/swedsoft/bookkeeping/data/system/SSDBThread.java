package se.swedsoft.bookkeeping.data.system;

/**
 * Date: 2006-feb-24
 * Time: 17:13:25
 */
public class SSDBThread extends Thread {

    private static long iStorePeriod = 10;

    private SSDB         iDatabase;

    private SSSystemData iData;

    public SSDBThread(SSDB pDatabase, SSSystemData pData) {
        iData     = pData;
        iDatabase = pDatabase;
        setName("SSDBThread");
    }

    @Override
    public void run() {


                /*
                // Companydata changed
                if(iDatabase.iCompanyDataChanged) {
                    iDatabase.iCompanyDataChanged = false;

                    SSSystemCompany iCompanyData = iData.getCurrentCompany();

                    if(iCompanyData != null) {
                        iCompanyData.storeCompany();
                    }
                } else

                // Yeardata changed
                if(iDatabase.iYearDataChanged) {
                    iDatabase.iYearDataChanged = false;

                    SSSystemYear iYearData = iData.getCurrentYear();

                    if(iYearData != null) {
                        iYearData.storeYear();
                    }
                } else
                    */
                // Database changed
                /*if(iDatabase.changed ) {
                    iDatabase.changed = false;

                    iDatabase.SaveDatabase();
                                /*
                    if(!iDatabase.isReadonly()){
                        System.out.println(getClass().getName() + ": Saving database...");

                        SSSystemCompany iSystemCompany = iData.getCurrentCompany();
                        SSSystemYear    iSystemYear    = iData.getCurrentYear();


                        if(iSystemCompany != null) iSystemCompany.storeCompany();
                        if(iSystemYear    != null) iSystemYear   .storeYear();

                        iDatabase.storeDatabase();
                    }   */

                //}
                // Sleep


    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.system.SSDBThread");
        sb.append("{iData=").append(iData);
        sb.append(", iDatabase=").append(iDatabase);
        sb.append('}');
        return sb.toString();
    }
}
