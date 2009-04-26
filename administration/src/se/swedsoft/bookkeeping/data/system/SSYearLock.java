package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

import java.io.*;
import java.util.Vector;
import java.rmi.server.UID;

/**
 * User: Johan Gunnarsson
 * Date: 2007-jan-24
 * Time: 16:01:44
 * Denna klass anv�nds f�r att l�sa �ppnade f�retag, s� att ett �ppet f�retag
 * inte kan tas bort av en annan anv�ndare.
 */
public class SSYearLock {


    /**
     * L�ser system�r iYear s� att flera instanser av programmet inte kan editera det samtidigt.
     * @param iYear - Det �r som ska l�sas
     */
    public static boolean applyLock(SSNewAccountingYear iYear) {
        if (iYear == null) {
            return false;
        }
        if (!SSDB.getInstance().getLocking()) {
            return true;
        }
        try {
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("lockyear");
            iOut.flush();
            iOut.println(iYear.getId().toString());
            iOut.flush();
            String iReply = iIn.readLine();
            return iReply.equals("goahead");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * L�ser upp �ret iYear. N�r detta �r gjort �r objektet �ppet att editera igen
     * @param iYear - Det objekt som ska l�sas upp
     */
    public static void removeLock(SSNewAccountingYear iYear) {
        if(iYear == null){
            return;
        }
        if (!SSDB.getInstance().getLocking()) {
            return;
        }
        PrintWriter iOut = SSDB.getInstance().getWriter();

        iOut.println("unlockyear");
        iOut.flush();
        iOut.println(iYear.getId().toString());
        iOut.flush();
    }

    /**
     * Kontrollerar om iYear �r l�st.
     * @param iYear - Det f�retag som ska kontrolleras
     * @return F�retaget l�st eller inte
     */
    public static boolean isLocked(SSNewAccountingYear iYear) {

        if(iYear == null){
            return false;
        }
        if (!SSDB.getInstance().getLocking()) {
            return false;
        }
        try {
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("checkyearlock");
            iOut.flush();
            iOut.println(iYear.getId().toString());
            iOut.flush();
            String iReply = iIn.readLine();
            return iReply.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}


