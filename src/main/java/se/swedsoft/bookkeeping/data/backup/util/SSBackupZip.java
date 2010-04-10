package se.swedsoft.bookkeeping.data.backup.util;


import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * Date: 2006-mar-03
 * Time: 09:13:20
 */
public class SSBackupZip {
    private SSBackupZip() {}

    public static class ArchiveFile {

        private String name;

        private File   file;

        /**
         *
         */
        public ArchiveFile() {
            name = null;
            file = null;
        }

        public ArchiveFile(File pFile, String pName) {
            file = pFile;
            name = pName;
        }

        /**
         *
         * @param pFile
         */
        public ArchiveFile(File pFile) {
            file = pFile;
            name = pFile.getName();
        }

        public String toString() {
            return file.getAbsolutePath();
        }

        public File getFile() {
            return file;
        }

    }

    /**
     *
     * @param pFilename
     * @param iFiles
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void compressFiles(String pFilename, List<ArchiveFile> iFiles) throws FileNotFoundException, IOException {

        // Create the outputstream
        ZipOutputStream iZipOutputStream = new ZipOutputStream(
                new FileOutputStream(pFilename));

        // Set the compression ratio
        iZipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);

        byte[] buffer = new byte[18024];

        // Loop through the list of files, adding each to the zip file
        for (ArchiveFile iArchiveFile : iFiles) {

            // Associate a file input stream for the current file
            FileInputStream iFileInputStream = new FileInputStream(iArchiveFile.file);

            ZipEntry iEntry = new ZipEntry(iArchiveFile.name);

            // Add ZIP entry to output stream.
            iZipOutputStream.putNextEntry(iEntry);

            int iBytesRead;

            // Transfer bytes from the current file to the ZIP file
            while ((iBytesRead = iFileInputStream.read(buffer)) > 0) {
                iZipOutputStream.write(buffer, 0, iBytesRead);
            }

            // Close the current entry
            iZipOutputStream.closeEntry();

            // Close the current file input stream
            iFileInputStream.close();

        }
        // Close the ZipOutPutStream
        iZipOutputStream.close();

    }

    /**
     *
     * @param pFilename
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> getFiles(String pFilename) throws FileNotFoundException, IOException {
        ZipFile zipFile = new ZipFile(new File(pFilename), ZipFile.OPEN_READ);

        // Create an enumeration of the entries in the zip file
        Enumeration iEntries = zipFile.entries();

        List<String> iFiles = new LinkedList<String>();

        // Process each entry
        while (iEntries.hasMoreElements()) {
            // Grab a zip file entry
            ZipEntry iEntry = (ZipEntry) iEntries.nextElement();

            // Add the entry to the list
            iFiles.add(iEntry.getName());
        }
        return iFiles;
    }

    /**
     *
     * @param pFilename
     * @param iFiles
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void extractFiles(String pFilename, List<ArchiveFile> iFiles) throws FileNotFoundException, IOException {

        // Open Zip file for reading
        ZipFile iZipFile = new ZipFile(new File(pFilename), ZipFile.OPEN_READ);

        // Create an enumeration of the entries in the zip file
        Enumeration iEntries = iZipFile.entries();

        byte[] buffer = new byte[18024];

        // Process each entry
        while (iEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry iEntry = (ZipEntry) iEntries.nextElement();

            ArchiveFile iFile = null;

            for (ArchiveFile iCurrent: iFiles) {
                if (iEntry.getName().equals(iCurrent.name)) {
                    iFile = iCurrent;
                }
            }
            // Skip extracting if we don't want to extract this file
            if (iFile == null) {
                continue;
            }

            // grab file's parent directory structure
            File iParent = iFile.file.getParentFile();

            // create the parent directory structure if needed
            iParent.mkdirs();

            // extract file if not a directory
            if (!iEntry.isDirectory()) {
                BufferedInputStream iBufferedInputStream = new BufferedInputStream(
                        iZipFile.getInputStream(iEntry));

                BufferedOutputStream iBufferedOutputStream = new BufferedOutputStream(
                        new FileOutputStream(iFile.file));

                int iBytesRead;

                // Transfer bytes from the current file to the ZIP file
                while ((iBytesRead = iBufferedInputStream.read(buffer)) > 0) {
                    iBufferedOutputStream.write(buffer, 0, iBytesRead);
                }

                iBufferedOutputStream.flush();
                iBufferedOutputStream.close();
                iBufferedInputStream.close();
            }
        }
        iZipFile.close();
    }

    /**
     *
     * @param pFilename
     * @param iFile
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @return
     */
    public static boolean extractFile(String pFilename, ArchiveFile iFile) throws FileNotFoundException, IOException {

        // Open Zip file for reading
        ZipFile iZipFile = new ZipFile(new File(pFilename), ZipFile.OPEN_READ);

        // Create an enumeration of the entries in the zip file
        Enumeration iEntries = iZipFile.entries();

        byte[] buffer = new byte[18024];

        // Process each entry
        while (iEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry iEntry = (ZipEntry) iEntries.nextElement();

            // Skip extracting if we don't want to extract this file
            if (iEntry.getName().equals(iFile.name)) {

                // grab file's parent directory structure
                File iParent = iFile.file.getParentFile();

                // create the parent directory structure if needed
                iParent.mkdirs();

                // extract file if not a directory
                if (!iEntry.isDirectory()) {
                    BufferedInputStream iBufferedInputStream = new BufferedInputStream(
                            iZipFile.getInputStream(iEntry));

                    BufferedOutputStream iBufferedOutputStream = new BufferedOutputStream(
                            new FileOutputStream(iFile.file));

                    int iBytesRead;

                    // Transfer bytes from the current file to the ZIP file
                    while ((iBytesRead = iBufferedInputStream.read(buffer)) > 0) {
                        iBufferedOutputStream.write(buffer, 0, iBytesRead);
                    }

                    iBufferedOutputStream.flush();
                    iBufferedOutputStream.close();
                    iBufferedInputStream.close();
                }
                iZipFile.close();
                return true;
            }
        }
        iZipFile.close();
        return false;
    }
}

