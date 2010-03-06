package se.swedsoft.bookkeeping.data.util;

import se.swedsoft.bookkeeping.app.SSPath;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2006-mar-13
 * Time: 09:11:46
 */
public class SSConfig implements Serializable {

    /// Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // The config instance
    private static SSConfig cInstance;

    /**
     * Get the current instance
     * @return
     */
    public static SSConfig getInstance(){
        if (cInstance == null) {
            if (iFile.exists()) {
                loadConfig();
            }  else {
                newConfig();
            }
        }
        return cInstance;
    }

    // The settings file
    private static File iFile = new File(SSPath.get(SSPath.APP_BASE), "bookkeeping.config");

    private Map<String, Object> iSettings;


    /**
     *
     */
    private SSConfig(){
        iSettings                 = new HashMap<String, Object>();
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param pProperty
     * @return
     */
    public synchronized Object get(String pProperty) {
        return iSettings.get(pProperty);
    }


    /**
     *
      * @param pProperty
     * @param pDefault
     * @return
     */
    public synchronized Object get(String pProperty, Object pDefault) {
        Object iValue = iSettings.get(pProperty);

        return iValue != null ? iValue : pDefault;
    }
    /**
     *
     * @param pProperty
     * @param pValue
     */
    public synchronized void set(String pProperty, Object pValue) {
        iSettings.put(pProperty, pValue);
        storeConfig();
    }



    //////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new database
     */
    private synchronized static void newConfig()  {
        cInstance = new SSConfig();
    }

    /**
     * Loads the database
     *
     */
    private synchronized static void loadConfig()  {
        try {
            ObjectInputStream iObjectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(iFile)));

            try{
                cInstance = (SSConfig)iObjectInputStream.readObject();
            } finally{
                iObjectInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Store the database
     *
     */
    private synchronized static void storeConfig()  {
        try {
            ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(iFile)));

            iObjectOutputStream.writeObject(cInstance);
            iObjectOutputStream.flush();
            iObjectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
