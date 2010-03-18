package se.swedsoft.bookkeeping.data.backup;

import se.swedsoft.bookkeeping.data.backup.util.SSBackupType;

import java.io.*;
import java.util.Date;

/**
 * Date: 2006-mar-03
 * Time: 09:03:45
 */
public class SSBackup implements Serializable {

    static final long serialVersionUID = 1L;

    // The filename of the backup
    private String      iFilename;

    // The date of the backup
    private Date        iDate;

    // The type of the backup
    private SSBackupType iType;




    /**
     *
     * @param pType
     */
    public SSBackup(SSBackupType pType){
        iType = pType;
    }

    /**
     *
     * @return the filename
     */
    public String getFilename() {
        return iFilename;
    }

    /**
     *
     * @param iFilename
     */
    public void setFilename(String iFilename) {
        this.iFilename = iFilename;
    }

    /////////////////////////////////////////////////////////////////////

    /**
     *
     * @return the backupdate
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;
    }

    /////////////////////////////////////////////////////////////////////

    /**
     *
     * @return the type
     */
    public SSBackupType getType() {
        return iType;
    }

    /**
     *
     * @param iType
     */
    public void setType(SSBackupType iType) {
        this.iType = iType;
    }

    /////////////////////////////////////////////////////////////////////





    /////////////////////////////////////////////////////////////////////

    /**
     * Removes the backup from disk
     */
    public void delete(){
        File iFile = new File(iFilename);
        if(iFile.exists()){
            iFile.delete();
        }
    }

    /**
     *
     * @return if the backup exists
     */
    public boolean exists(){
        File iFile = new File(iFilename);
        return iFile.exists();
    }



    /**
     *
     * @param iFile
     * @return the backup
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static SSBackup loadBackup(File iFile) throws IOException, ClassNotFoundException {
        SSBackup iBackup = null;
        ObjectInputStream iObjectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(iFile)));

        try{
            iBackup          = (SSBackup)iObjectInputStream.readObject();
        } finally{
            iObjectInputStream.close();
        }

        return iBackup;
    }

    /**
     *
     * @param iFile
     * @param iBackup
     * @throws IOException
     */
    public static void storeBackup(File iFile, SSBackup iBackup) throws IOException {
        ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(iFile)));

        iObjectOutputStream.writeObject(iBackup);
        iObjectOutputStream.flush();
        iObjectOutputStream.close();
    }


    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(iFilename);
        sb.append(", ");
        sb.append(iDate);
        sb.append(", ");
        sb.append(iType);

        return super.toString();
    }



}




