package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

import java.io.*;

/**
 * User: Johan Gunnarsson
 * Date: 2007-jan-24
 * Time: 16:01:44
 * Denna klass används för att låsa öppnade företag, så att ett öppet företag
 * inte kan tas bort av en annan användare.
 */
public class SSYearLock {


    /**
     * Låser systemår iYear så att flera instanser av programmet inte kan editera det samtidigt.
     * @param iYear - Det år som ska låsas
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
     * Låser upp året iYear. När detta är gjort är objektet öppet att editera igen
     * @param iYear - Det objekt som ska låsas upp
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
     * Kontrollerar om iYear är låst.
     * @param iYear - Det företag som ska kontrolleras
     * @return Företaget låst eller inte
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


