package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;

import java.io.*;
import java.net.SocketException;

/**
 * Date: 2006-feb-28
 * Time: 16:37:12
 */
public class SSDBUtils {
    private static Integer iLocalRevisionNumber = 1;

    private static void copyFile(File in, File out) throws Exception {
        try{
            FileInputStream fis  = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);

            byte[] buf = new byte[1024];
            int i = 0;
            while((i=fis.read(buf))!=-1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a backup for the file with the given file name.
     * Changes the name of the file.
     *
     * @param iFile The name to change.
     */
    public static void backup(File iFile) {
        File iBackupFile = getBackupFile(iFile);

        if (iFile.exists() && !iBackupFile.exists()) {
            try {
                copyFile(iFile, iBackupFile);
            } catch (Exception e) {
                System.out.println("Failed to copy " + iFile + " to " + iBackupFile);
            }
        }
    }

    /**
     * Removes the backup file for the given file name.
     *
     * @param iFile The file name of the file to remove the backup for.
     */
    public static void removeBackup(File iFile) {
        File iBackupFile = getBackupFile( iFile);

        if (iBackupFile.exists()) {
            iBackupFile.delete();
        }
    }

    /**
     * Restores the backup file for the given file name.
     *
     * @param iFile The file name of the file to restore the backup for.
     * @return
     */
    public static boolean restoreBackup(File iFile) {
        File iBackupFile = getBackupFile(iFile);
        if (iBackupFile.exists()) {
            if(iFile.exists()) {
                iFile.delete();
            }
            return iBackupFile.renameTo(iFile);
        }
        return false;
    }

    public static Integer getRevisionNumber() {
        try {
            if (!SSDB.getInstance().getLocking()) {
                return iLocalRevisionNumber;
            }
            PrintWriter iOut = SSDB.getInstance().getWriter();
            BufferedReader iIn = SSDB.getInstance().getReader();

            iOut.println("getrevisionnumber");
            iOut.flush();
            String iAnswer = iIn.readLine();
            return Integer.parseInt(iAnswer);

        } catch (SocketException e) {
            new SSErrorDialog(SSMainFrame.getInstance(), "connectionlostrestart");
            System.exit(0);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setRevisionNumber(Integer iNumber) {
        if (!SSDB.getInstance().getLocking()) {
            iLocalRevisionNumber = iNumber;
            return;
        }
        PrintWriter iOut = SSDB.getInstance().getWriter();

        iOut.println("setrevisionnumber");
        iOut.flush();
        iOut.println(iNumber.toString());
        iOut.flush();
    }


    /**
     * Creates a backup name for the given file name. <P>
     *
     * @param iFile The file name to append a backup appendix to.
     *
     * @return A modified file name to use for backups.
     */
    private static File getBackupFile(File iFile) {
        return new File(iFile.getParent(), iFile.getName() + ".backup");
    }


    /**
     * Deletes a file if the file exists
     *
     * @param pFilename
     * @return if the file was deleted
     *
     */
    public static boolean deleteFile(String pFilename) {
        File iFile = new File(pFilename);

        if(iFile.exists()){
            return iFile.delete();
        }
        return false;
    }

    /**
     * Creates a backup for the file with the given file name.
     * Changes the name of the file.
     *
     * @param pFileName The name to change.
     */
    public static void backup(String pFileName) {
        File iFile       = new File(pFileName);
        File iBackupFile = getBackupFile(iFile);

        if (iFile.exists()) {
            if(iBackupFile.exists()) iBackupFile.delete();

            iFile.renameTo(iBackupFile);
        }
    }

    /**
     * Removes the backup file for the given file name.
     *
     * @param pFileName The file name of the file to remove the backup for.
     */
    public static void removeBackup(String pFileName) {
        File iBackupFile = getBackupFile( new File(pFileName));

        if (iBackupFile.exists()) {
            iBackupFile.delete();
        }
    }

    /**
     * Restores the backup file for the given file name.
     *
     * @param pFileName The file name of the file to remove the backup for.
     */
    public static void restoreBackup(String pFileName) {
        File iFile       = new File(pFileName);
        File iBackupFile = getBackupFile(iFile);

        if (iBackupFile.exists()) {
            if(iFile.exists()) iFile.delete();

            iBackupFile.renameTo(iFile);
        }
    }


    /**
     *
     * @param iFile
     * @param iObject
     * @throws IOException
     */
    public static void SaveToFile(File iFile, Object iObject) throws IOException {
        FileOutputStream fos  = null;
        BufferedOutputStream iBufferedOutputStream = null;
        ObjectOutputStream iObjectOutputStream = null;
        try {
            fos  = new FileOutputStream(iFile);
            iBufferedOutputStream = new BufferedOutputStream(fos);
            iObjectOutputStream = new ObjectOutputStream(iBufferedOutputStream);

            iObjectOutputStream.writeObject(iObject);
            iObjectOutputStream.flush();
            iObjectOutputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e1) {
            if(fos != null)
                fos.close();
            if(iBufferedOutputStream != null)
                iBufferedOutputStream.close();
            if (iObjectOutputStream != null)
                iObjectOutputStream.close();

            throw new IOException(e1.getMessage());
        }
    }

    /**
     *
     * @param iFile
     * @return
     * @throws IOException
     */
    public static Object LoadFromFile(File iFile)  throws IOException {
        FileInputStream fis = null;
        BufferedInputStream iBufferedInputStream = null;
        ObjectInputStream iObjectInputStream = null;
        try {
            Object iObject = null;
            fis = new FileInputStream(iFile);
            iBufferedInputStream = new BufferedInputStream(fis);
            iObjectInputStream = new SSObjectInputStream(iBufferedInputStream);
            try {
                iObject = iObjectInputStream.readObject();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            iObjectInputStream.close();

            return iObject;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            if(fis != null)
                fis.close();
            if(iBufferedInputStream != null)
                iBufferedInputStream.close();
            if (iObjectInputStream != null)
                iObjectInputStream.close();

            throw new IOException(e1.getMessage());
        }

        return null;

    }




    /**
     * Loads the company object from the disk
     *
     * @param pFilename
     * @return The company
     *
     */
    public static SSNewCompany loadCompany(String pFilename) {
        try {
            BufferedInputStream iBufferedInputStream = new BufferedInputStream(new FileInputStream( pFilename ));

            ObjectInputStream iObjectInputStream = new SSObjectInputStream(iBufferedInputStream);

            return (SSNewCompany)iObjectInputStream.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Stores the SSNewCompany object from disk
     *
     * @param pFilename
     * @param pCompany
     *
     * @throws IOException
     */
    public static void storeCompany(String pFilename, SSNewCompany pCompany) throws IOException {
        BufferedOutputStream iBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pFilename));

        ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(iBufferedOutputStream);

        iObjectOutputStream.writeObject(pCompany);
        iObjectOutputStream.flush();
        iObjectOutputStream.close();

    }


    /**
     * Loads the year object from the disk
     *
     * @param pFilename
     * @return The year
     *
     */
    public static SSNewAccountingYear loadYear(String pFilename) {
        try {
            BufferedInputStream iBufferedInputStream = new BufferedInputStream(new FileInputStream( pFilename ));

            ObjectInputStream iObjectInputStream = new ObjectInputStream(iBufferedInputStream);

            SSNewAccountingYear iYear = (SSNewAccountingYear)iObjectInputStream.readObject();

            if(iYear == null){
                return new SSNewAccountingYear();
            } else {
                return iYear;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Stores the year object from disk
     *
     * @param pFilename
     * @param pYear
     *
     * @throws IOException
     */
    public static void storeYear(String pFilename, SSNewAccountingYear pYear) throws IOException {
        BufferedOutputStream iBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pFilename));

        ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(iBufferedOutputStream);

        iObjectOutputStream.writeObject(pYear);
        iObjectOutputStream.flush();
        iObjectOutputStream.close();

    }


    /**
     *
     */
    private static class SSObjectInputStream extends ObjectInputStream{

        /**
         *
         * @param in
         * @throws IOException
         */
        public SSObjectInputStream(InputStream in) throws IOException {
            super(in);
        }


        /**
         * This function is to make the database forward compatible
         *
         */
        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

            // This is the old SSStandardText class
            if(desc.getSerialVersionUID() == -673623580724335542L){
                return null;
            }

            return super.resolveClass(desc);
        }


    }


}
