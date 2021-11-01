package se.swedsoft.bookkeeping.data.system;


import org.fribok.bookkeeping.app.Path;
import se.swedsoft.bookkeeping.data.SSNewCompany;

import java.io.*;
import java.util.Collection;
import java.util.Vector;


/**
 * Johan Gunnarsson
 * Date: 2007-jan-26
 * Time: 11:40:47
 */
public class SSCompanyConfig {
    private SSCompanyConfig() {}

    public static void saveLastOpenCompany(SSSystemCompany iLastCompany) {
        File iFile = new File(Path.get(Path.APP_BASE), "lastcompanyopen.config");

        if (iFile.exists()) {
            iFile.delete();
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(iFile);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(iLastCompany);
            if (iLastCompany.getCurrentYear() != null) {
                oos.writeObject(iLastCompany.getCurrentYear());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SSSystemCompany openLastOpenCompany() {
        File iFile = new File(Path.get(Path.APP_BASE), "lastcompanyopen.config");

        if (!iFile.exists()) {
            return null;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        SSSystemCompany iSystemCompany = null;

        try {
            fis = new FileInputStream(iFile);
            ois = new ObjectInputStream(fis);

            iSystemCompany = (SSSystemCompany) ois.readObject();
            SSSystemYear iSystemYear = (SSSystemYear) ois.readObject();

            if (iSystemCompany != null && iSystemYear != null) {
                for (SSSystemYear iCurrent : iSystemCompany.getYears()) {
                    if (iCurrent.getId().equals(iSystemYear.getId())) {
                        iCurrent.setCurrent(true);
                    }
                }
            }

        } catch (IOException e) {
            iFile.delete();
            return iSystemCompany;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return iSystemCompany;
    }

    public static void saveCompanySetting(SSNewCompany iCompany) {
        if (iCompany == null) {
            return;
        }

        File iFile = new File(Path.get(Path.APP_BASE), "companysettings.config");

        Collection<Object> iVector = new Vector<Object>();

        iVector.add(iCompany);
        // iVector.add(iCompany.getCurrentYear());
        ObjectInputStream ois = null;
        FileInputStream fis = null;

        try {
            if (!iFile.exists()) {
                return;
            }

            fis = new FileInputStream(iFile);
            ois = new ObjectInputStream(fis);
            while (true) {
                SSSystemCompany iSystemCompany = (SSSystemCompany) ois.readObject();
                SSSystemYear iSystemYear = (SSSystemYear) ois.readObject();

                if (iSystemCompany != null) {
                    if (!iSystemCompany.getId().equals(iCompany.getId())) {
                        iVector.add(iSystemCompany);
                        iVector.add(iSystemYear);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (iFile.exists()) {
                    iFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(iFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                for (Object iObject : iVector) {
                    oos.writeObject(iObject);
                }
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SSSystemCompany openCompanySetting(SSSystemCompany iCompany) {
        File iFile = new File(Path.get(Path.APP_BASE), "companysettings.config");

        if (!iFile.exists()) {
            return null;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        SSSystemCompany iSystemCompany = null;

        try {
            fis = new FileInputStream(iFile);
            ois = new ObjectInputStream(fis);

            SSSystemYear iSystemYear = null;

            while (true) {
                iSystemCompany = (SSSystemCompany) ois.readObject();
                iSystemYear = (SSSystemYear) ois.readObject();
                if (iSystemCompany != null) {
                    if (iSystemCompany.getId().equals(iCompany.getId())) {
                        if (iSystemYear != null) {
                            for (SSSystemYear iCurrent : iSystemCompany.getYears()) {
                                if (iCurrent.getId().equals(iSystemYear.getId())) {
                                    iCurrent.setCurrent(true);
                                }
                            }
                        }
                        return iSystemCompany;
                    }
                }
            }

        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return iSystemCompany;
    }

    private static ObjectOutputStream appendableObjectOutputStream(File f) throws IOException {
        FileOutputStream fos = new FileOutputStream(f, true);
        boolean append = f.exists() && f.length() > 0;

        if (append) {
            return new ObjectOutputStream(fos) {
                @SuppressWarnings({ "RedundantThrowsDeclaration"})
                @Override
                protected void writeStreamHeader() throws IOException {}
            };
        } else {
            return new ObjectOutputStream(fos);
        }
    }

    public static void deleteFiles() {
        File iFile1 = new File(Path.get(Path.APP_BASE), "lastcompanyopen.config");
        File iFile2 = new File(Path.get(Path.APP_BASE), "companysettings.config");

        if (iFile1.exists()) {
            iFile1.delete();
        }

        if (iFile2.exists()) {
            iFile2.delete();
        }

    }
}
