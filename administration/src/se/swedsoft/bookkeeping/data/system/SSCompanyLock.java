package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.data.SSNewCompany;

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
public class SSCompanyLock {


    /**
     * L�ser f�retag iCompany s� att flera instanser av programmet inte kan editera det samtidigt.
     * @param iCompany - Det f�retag som ska l�sas
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
     * L�ser upp f�retaget iCompany. N�r detta �r gjort �r objektet �ppet att editera igen
     * @param iCompany - Det objekt som ska l�sas upp
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
     * Kontrollerar om iCompany �r l�st. L�ser fr�n filen bookkeeping.db.companylock �nda till
     * EOFException kastas.
     * @param iCompany - Det f�retag som ska kontrolleras
     * @return F�retaget l�st eller inte
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

