package se.swedsoft.bookkeeping.gui.status;


import se.swedsoft.bookkeeping.app.Version;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;


/**
 * User: Andreas Lago
 * Date: 2006-mar-22
 * Time: 11:02:56
 */
public class SSMainStatusBar {

    private JLabel iNameLabel;

    private JLabel iCompanyLabel;

    private JLabel iYearLabel;

    private JLabel iReadonlyLabel;

    private JLabel iMemLabel;

    public SSMainStatusBar() {
        iNameLabel = new JLabel();
        iCompanyLabel = new JLabel();
        iYearLabel = new JLabel();
        iReadonlyLabel = new JLabel();
        iMemLabel = new JLabel();
        iNameLabel.setText(String.format("%s %s", Version.APP_TITLE, Version.APP_VERSION));

        setCompanyText(/* SSDB.getInstance().getCurrentCompany()*/null);
        setYearText(/* SSDB.getInstance().getCurrentYear()*/null);

        SSDB.getInstance().addPropertyChangeListener("COMPANY",
                new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                SSNewCompany iCompany = (SSNewCompany) evt.getNewValue();

                setCompanyText(iCompany);

            }
        });
        SSDB.getInstance().addPropertyChangeListener("YEAR",
                new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                SSNewAccountingYear iAccountingYear = (SSNewAccountingYear) evt.getNewValue();

                setYearText(iAccountingYear);
            }
        });

        /* SSMemoryWarning.setPercentageUsageThreshold(0.80);

         SSMemoryWarning mws = new SSMemoryWarning();
         mws.addListener(new SSMemoryWarning.Listener() {
         public void memoryUsageLow(final long usedMemory,final long maxMemory) {
         //                File iFile = new File(SSDBConfig.getDatabaseFile().getParent()+File.separator+"automatisk_sakerhetskopia.zip");
         //SSBackup iBackup = SSBackupFactory.createBackup(iFile.getAbsolutePath());
         //SSBackupDatabase.getInstance().getBackups().add(iBackup);
         SSBackupDatabase.getInstance().notifyUpdated();
         Thread blink = new Thread(new Runnable(){
         public void run() {
         while (true) {

         iMemLabel.setText("<html><b>Minnesanvändning hög. Starta om programmet!</b></html>");

         iMemLabel.setForeground(Color.RED);
         iMemLabel.setVisible(true);
         try {
         Thread.sleep(1000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         iMemLabel.setVisible(false);
         try {
         Thread.sleep(1000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         }
         }
         });
         blink.start();

         /*iMemLabel.setText("Minnesanvändning hög. Starta om programmet!");
         iMemLabel.setForeground(Color.RED);
         SSMemoryWarning.setPercentageUsageThreshold(0.95);
         double percentageUsed = ((double) usedMemory) / maxMemory;
         if (percentageUsed > 0.95) {
         iMemLabel.setText("MINNESNIVÅ KRITISK! STARTA OM PROGRAMMET ANNARS KAN DATABASEN GÅ FÖRLORAD!");
         } */
        // }
        // });
    }

    /**
     *
     * @param iCompany
     */
    private void setCompanyText(SSNewCompany iCompany) {

        if (iCompany == null) {
            iCompanyLabel.setText(
                    SSBundle.getBundle().getString("mainframe.status.nocompany"));
            iCompanyLabel.setForeground(Color.RED);
        } else {
            iCompanyLabel.setText(iCompany.getName());
            iCompanyLabel.setForeground(Color.BLACK);
        }
    }

    /**
     *
     * @param iAccountingYear
     */
    private void setYearText(SSNewAccountingYear iAccountingYear) {
        if (iAccountingYear == null) {
            iYearLabel.setText(SSBundle.getBundle().getString("mainframe.status.noyear"));
            iYearLabel.setForeground(Color.RED);
        } else {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            StringBuilder year = new StringBuilder();

            year.append(df.format(iAccountingYear.getFrom()));
            year.append(' ');
            year.append(SSBundle.getBundle().getString("date.separator"));
            year.append(' ');
            year.append(df.format(iAccountingYear.getTo()));
            iYearLabel.setText(year.toString());
            iYearLabel.setForeground(Color.BLACK);
        }
    }

    public JLabel getNameLabel() {
        return iNameLabel;
    }

    public JLabel getCompanyLabel() {
        return iCompanyLabel;
    }

    public JLabel getYearLabel() {
        return iYearLabel;
    }

    public JLabel getReadonlyLabel() {
        return iReadonlyLabel;
    }

    public JLabel getMemLabel() {
        return iMemLabel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.status.SSMainStatusBar");
        sb.append("{iCompanyLabel=").append(iCompanyLabel);
        sb.append(", iMemLabel=").append(iMemLabel);
        sb.append(", iNameLabel=").append(iNameLabel);
        sb.append(", iReadonlyLabel=").append(iReadonlyLabel);
        sb.append(", iYearLabel=").append(iYearLabel);
        sb.append('}');
        return sb.toString();
    }
}
