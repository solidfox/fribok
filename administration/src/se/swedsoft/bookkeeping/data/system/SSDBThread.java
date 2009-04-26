package se.swedsoft.bookkeeping.data.system;

/**
 * Date: 2006-feb-24
 * Time: 17:13:25
 */
public class SSDBThread extends Thread {

    private static long iStorePeriod = 10;

    private SSDB         iDatabase;

    private SSSystemData iData;




    /**
     * Allocates a new <code>Thread</code> object. This constructor has
     * the same effect as <code>Thread(null, null,</code>
     * <i>gname</i><code>)</code>, where <b><i>gname</i></b> is
     * a newly generated name. Automatically generated names are of the
     * form <code>"Thread-"+</code><i>n</i>, where <i>n</i> is an integer.
     *
     * @see Thread#Thread(ThreadGroup,
     *      Runnable, String)
     */
    public SSDBThread(SSDB pDatabase, SSSystemData pData) {
        super();
        iData     = pData;
        iDatabase = pDatabase;
        setName("SSDBThread");
    }


    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p/>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see Thread#start()
     * @see Thread#stop()
     * @see Thread#Thread(ThreadGroup,
     *      Runnable, String)
     * @see Runnable#run()
     */
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

}
