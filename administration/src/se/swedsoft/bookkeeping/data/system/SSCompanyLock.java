package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.data.SSNewCompany;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: Johan Gunnarsson
 * Date: 2007-jan-24
 * Time: 16:01:44
 * Denna klass används för att låsa öppnade företag, så att ett öppet företag
 * inte kan tas bort av en annan användare.
 */
public class SSCompanyLock {


    /**
     * Låser företag iCompany så att flera instanser av programmet inte kan editera det samtidigt.
     * @param iCompany - Det företag som ska låsas
     * @return
     */
    public static boolean applyLock(SSNewCompany iCompany) {
        if (iCompany == null) {
            return false;
        }
        if (!SSDB.getInstance().getLocking()) {
            return true;
        }

        try {
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("lockcompany");
            iOut.flush();
            iOut.println(iCompany.getId().toString());
            iOut.flush();
            String iReply = iIn.readLine();
            return iReply.equals("goahead");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Låser upp företaget iCompany. När detta är gjort är objektet öppet att editera igen
     * @param iCompany - Det objekt som ska låsas upp
     */
    public static void removeLock(SSNewCompany iCompany) {
        if(iCompany == null){
            return;
        }
        if (!SSDB.getInstance().getLocking()) {
            return;
        }
        PrintWriter iOut = SSDB.getInstance().getWriter();

        iOut.println("unlockcompany");
        iOut.flush();
        iOut.println(iCompany.getId().toString());
        iOut.flush();
    }

    /**
     * Kontrollerar om iCompany är låst. Läser från filen bookkeeping.db.companylock ända till
     * EOFException kastas.
     * @param iCompany - Det företag som ska kontrolleras
     * @return Företaget låst eller inte
     */
    public static boolean isLocked(SSNewCompany iCompany) {
        if(iCompany == null){
            return false;
        }
        if (!SSDB.getInstance().getLocking()) {
            return false;
        }
        try {
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("checkcompanylock");
            iOut.flush();
            iOut.println(iCompany.getId().toString());
            iOut.flush();
            String iReply = iIn.readLine();
            return iReply.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}

