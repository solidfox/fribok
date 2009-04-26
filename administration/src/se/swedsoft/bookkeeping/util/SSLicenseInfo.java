package se.swedsoft.bookkeeping.util;

import se.swedsoft.bookkeeping.data.util.SSFileSystem;
import se.swedsoft.bookkeeping.license.SSLicenseType;

import java.util.Date;
import java.io.*;
import java.text.DateFormat;

/**
 * Date: 2006-mar-16
 * Time: 11:56:20
 */
public class SSLicenseInfo implements Serializable {

    /// Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private String iName;

    private String iCompany;

    private SSLicenseType iType;

    private Boolean iPrivate;

    private Date iCreated;

    private Date iExpires;


    /**
     *
     * @param pType
     * @param pName
     */
    public SSLicenseInfo(SSLicenseType pType, String pName) {
        iType    = pType;
        iName    = pName;
        iCreated = new Date();
    }


    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param pName
     */
    public void setName(String pName) {
        iName = pName;
    }

    ////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getCompany() {
        return iCompany;
    }

    /**
     *
     * @param PCompany
     */
    public void setCompany(String PCompany) {
        iCompany = PCompany;
    }

    ////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSLicenseType getType() {
        return iType;
    }

    /**
     *
     * @param PType
     */
    public void setType(SSLicenseType PType) {
        iType = PType;
    }
    ////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Boolean getPrivate() {
        return iPrivate;
    }

    /**
     *
     * @param pPrivate
     */
    public void setPrivate(Boolean pPrivate) {
        iPrivate = pPrivate;
    }

    ////////////////////////////////////////////////


    public Date getExpires() {
        return iExpires;
    }

    /**
     *
     * @param PExpires
     */
    public void setExpires(Date PExpires) {
        iExpires = PExpires;
    }

    ////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getCreated() {
        if(iCreated == null) iCreated = new Date();
        return iCreated;
    }

    /**
     *
     * @param iCreated
     */
    public void setCreated(Date iCreated) {
        this.iCreated = iCreated;
    }

    ////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public boolean expired(){
        Date iNow = new Date();

        return (iType == SSLicenseType.Evaluation) && (iNow.getTime() > iExpires.getTime() );
    }


    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        StringBuilder sb = new StringBuilder();

        sb.append("Licenced to ");
        sb.append(iName);
        sb.append(", ");
        sb.append(iCompany);

        if(iCreated != null){
            sb.append(", ");
            sb.append( iFormat.format(iCreated) );
        }

        if(iType == SSLicenseType.Evaluation){
            sb.append(" (expires at ");
            sb.append(  iFormat.format(iExpires) );
            sb.append(")");
        }


        return sb.toString();
    }

    ////////////////////////////////////////////////

    private static String iFilename = SSFileSystem.getApplicationDirectory() + "jfs.license";


    // The currrent license
    private static SSLicenseInfo cInstance;

    /**
     * Get the current license
     * @return
     */
    public static SSLicenseInfo getLicense(){

        if(cInstance == null){
            File iFile = new File(iFilename);

            if(iFile.exists()){
                loadLicense();
            }  else {
               return null;
            }
        }
        return cInstance;
    }

    /**
     * Sets a new license key
     *
     * @param iLicense
     */
    public static void setLicense(SSLicenseInfo iLicense){
        cInstance = iLicense;

        if(cInstance != null){
           storeLicense();
        }

    }




    /**
     * Loads the database
     *
     */
    private static void loadLicense()  {
        try {
            ObjectInputStream iObjectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(iFilename)));

            try{
                cInstance = (SSLicenseInfo)iObjectInputStream.readObject();
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
    private static void storeLicense()  {
        try {
            ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(iFilename)));

            iObjectOutputStream.writeObject(cInstance);
            iObjectOutputStream.flush();
            iObjectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


