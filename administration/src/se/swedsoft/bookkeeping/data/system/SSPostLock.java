package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import java.io.*;
import java.util.Vector;

/**
 * User: Johan Gunnarsson
 * Date: 2007-jan-24
 * Time: 16:01:44
 * Denna klass används för att låsa poster i databasen så att samma objekt
 * inte kan ändras samtidigt av olika instanser av programmet.
 */
public class SSPostLock {


    /**
     * Låser objekt O så att flera instanser av programmet inte kan editera det samtidigt.
     * @param O - Det objekt som ska låsas
     */
    public static boolean applyLock(Object O) {
        /*File iFile = new File(SSDBConfig.getDatabaseFile().getParent(), SSDBConfig.getDatabaseFile().getName() + ".postlock");
        ObjectOutputStream oos = null;
        try {
            oos = appendableObjectOutputStream(iFile);
            oos.writeObject(O);

        } catch (FileNotFoundException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(oos!=null)
                    oos.close();
            } catch (IOException e) {
                //Ingen felhantering då strömmarna ska stängas
            }
        }*/
        if (O == null) {
            return false;
        }
        if (!SSDB.getInstance().getLocking()) {
            return true;
        }
        try {
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("lockpost");
            iOut.flush();
            iOut.println(O);
            iOut.flush();
            String iReply = iIn.readLine();
            return iReply.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Låser upp objekt O. När detta är gjort är objektet öppet att editera igen
     * @param O - Det objekt som ska låsas upp
     */
    public static void removeLock(Object O) {
        /*File iFile = new File(SSDBConfig.getDatabaseFile().getParent(), SSDBConfig.getDatabaseFile().getName() + ".postlock");

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Vector<Object> iObjects = new Vector<Object>();
        if (!iFile.exists()) {
            return;
        }
        try {
            fis = new FileInputStream(iFile);
            ois = new ObjectInputStream(fis);

            while (true) {
                Object iObject = ois.readObject();
                if (!iObject.equals(O)) {
                    iObjects.addElement(iObject);
                }
            }

        } catch (IOException e) {

        } catch (ClassNotFoundException e){}
        finally{
            try {
                if(iObjects.size()>0)
                {
                    if(ois!=null)
                        ois.close();
                    if(fis!=null)
                        fis.close();
                    if(iFile.exists())
                        iFile.delete();
                    writeBack(iObjects);
                }
                else{
                    if(ois!=null)
                        ois.close();
                    if(fis!=null)
                        fis.close();
                    if(iFile.exists())
                        iFile.delete();
                }
            } catch (FileNotFoundException e){
            } catch (IOException e) {
                //Ingen felhantering då strömmarna ska stängas
            }
        }*/
        if(O == null){
            return;
        }
        if (!SSDB.getInstance().getLocking()) {
            return;
        }
        PrintWriter iOut = SSDB.getInstance().getWriter();

        iOut.println("unlockpost");
        iOut.flush();
        iOut.println(O);
        iOut.flush();
    }

    /**
     * Kontrollerar om O är låst. Läser från filen bookkeeping.db.postlock ända till
     * EOFException kastas.
     * @param O - Det objekt som ska kontrolleras
     * @return Objektet låst eller inte
     */
    public static boolean isLocked(Object O) {
        /*File iFile = new File(SSDBConfig.getDatabaseFile().getParent(), SSDBConfig.getDatabaseFile().getName() + ".postlock");
        if(iFile.exists())
            iFile = iFile.getAbsoluteFile();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            if (!iFile.exists()) {
                return false;
            }
            fis = new FileInputStream(iFile);
            ois = new ObjectInputStream(fis);
            while(true){
                Object iObject = ois.readObject();
                if (iObject.equals(O)) {
                    return true;
                }
            }


        } catch (FileNotFoundException e){
            SSFrameManager.getInstance().close();
            new SSErrorDialog( SSMainFrame.getInstance(), "connectionlost");
        } catch (IOException e) {
            return false;
        } catch (ClassNotFoundException e){
            return false;
        }
        finally{
            try {
                if(ois!=null)
                    ois.close();
                if(fis!=null)
                    fis.close();
            } catch (IOException e) {
                //Ingen felhantering då strömmarna ska stängas
            }
        }
        return false;*/
        if(O == null){
            return false;
        }
        if (!SSDB.getInstance().getLocking()) {
            return false;
        }
        try {
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("checkpostlock");
            iOut.flush();
            iOut.println(O);
            iOut.flush();
            String iReply = iIn.readLine();
            return iReply.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clearLocks() {
        /*File iFile = new File(SSDBConfig.getDatabaseFile().getParent(), SSDBConfig.getDatabaseFile().getName() + ".postlock");
        if (iFile.exists()) {
            iFile.delete();
        }*/
        PrintWriter iOut = SSDB.getInstance().getWriter();
        iOut.println("clearpostlocks");
        iOut.flush();
    }
}
