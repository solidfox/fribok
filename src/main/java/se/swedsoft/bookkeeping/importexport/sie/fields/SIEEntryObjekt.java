package se.swedsoft.bookkeeping.importexport.sie.fields;


import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.types.SIEDimension;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.util.List;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.INT;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;


/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-23
 * Time: 12:25:13
 */
public class SIEEntryObjekt implements SIEEntry {

    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @return If anything was imported
     * @throws SSImportException
     *
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {

        // The standard says STRING, INT, STRING STING, but as we only supports integer values
        // for projects and resultunits we have to assume that the exporting program has
        // //exported as integer
        if (!iReader.hasFields(STRING, INT, INT, STRING)
                && !iReader.hasFields(STRING, INT, STRING, STRING)) {
            throw new SSImportException(
                    SSBundleString.getString("sieimport.fielderror", iReader.peekLine()));
        }

        int iNumber = iReader.nextInteger();

        SIEDimension iDimension = iImporter.getDimension(iNumber);

        if (iDimension == null) {
            System.out.println("No dimension:" + iNumber);
            return false;
        }

        String iNum = iReader.next();

        if (iNum == null) {
            new SSErrorDialog(SSMainFrame.getInstance(), "sieimport.stringnumber");
            return true;
        }
        // Resultatenhet, #DIM 1
        if (iDimension.getNumber() == 1) {
            SSNewResultUnit iResultUnit = new SSNewResultUnit();

            iResultUnit.setNumber(iNum);
            iResultUnit.setName(iReader.nextString());

            SSDB.getInstance().addResultUnit(iResultUnit);
        }

        // Projekt, #DIM 6
        if (iDimension.getNumber() == 6) {
            SSNewProject iProject = new SSNewProject();

            iProject.setNumber(iNum);
            iProject.setName(iReader.nextString());

            SSDB.getInstance().addProject(iProject);
        }

        return true;
    }

    /**
     * Exports the entry
     *
     * @param iExporter
     * @param iWriter
     * @return If anything was exported
     * @throws SSExportException
     *
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        List<SSNewResultUnit> iResultUnits = SSDB.getInstance().getResultUnits();
        List<SSNewProject   > iProjects = SSDB.getInstance().getProjects();

        // Resultatenhet, #DIM 1
        for (SSNewResultUnit iResultUnit: iResultUnits) {
            iWriter.append(SIELabel.SIE_OBJEKT);
            iWriter.append(1);
            iWriter.append(iResultUnit.getNumber());
            iWriter.append(iResultUnit.getName());
            iWriter.newLine();
        }

        // Projekt, #DIM 6
        for (SSNewProject iProject: iProjects) {
            iWriter.append(SIELabel.SIE_OBJEKT);
            iWriter.append(6);
            iWriter.append(iProject.getNumber());
            iWriter.append(iProject.getName());
            iWriter.newLine();
        }

        return !iResultUnits.isEmpty() || !iProjects.isEmpty();
    }
}
