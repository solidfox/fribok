package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEIterator;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.*;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.math.SSResultUnitMath;
import se.swedsoft.bookkeeping.calc.math.SSProjectMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.List;
import java.util.LinkedList;
import java.math.BigDecimal;


/**
 * Date: 2006-feb-24
 * Time: 09:04:20
 */
public class SIEEntryTransaktion extends SIEEntry {
    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @return If anything was imported
     * @throws SSImportException
     *
     */
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        throw new RuntimeException("Must be called within a voucher.");
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
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        throw new RuntimeException("Must be called within a voucher.");
    }


    /**
     *
     * @param iVoucherRow
     * @return
     */
    private  List<Object> getObjects(SSVoucherRow iVoucherRow){
        List<Object> iObjects = new LinkedList<Object>();

        SSNewResultUnit iResultUnit = iVoucherRow.getResultUnit();
        SSNewProject    iProject    = iVoucherRow.getProject();

        // Resultatenhet, #DIM 1
        if(iResultUnit != null){
            iObjects.add( 1 );
            iObjects.add( iResultUnit.getNumber() );
        }
        // Projekt, #DIM 6
        if(iProject != null){
            iObjects.add( 6 );
            iObjects.add( iProject.getNumber() );
        }
        return iObjects;
    }

    /**
     *
     * @param iVoucher
     * @param iImporter
     * @param iReader
     * @return
     * @throws SSImportException
     */
    public boolean importEntry(SSVoucher iVoucher, SSSIEImporter iImporter, SIEReader iReader,SSNewAccountingYear iCurrentYearData) throws SSImportException {
        //SSNewAccountingYear iCurrentYearData  = SSDB.getInstance().getCurrentYear();
                                              
        // #TRANS kontonr {objectlista} belopp [transdat] [transtext] [kvantitet]
        if(!iReader.hasFields(STRING, INT, ARRAY, FLOAT )) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }

        int          iAccountNumber = iReader.nextInteger();
        List<String> iObjects       = iReader.nextArray();
        BigDecimal   iAmmount       = iReader.nextBigDecimal();

        SSAccount    iAccount    = iCurrentYearData.getAccountPlan().getAccount(iAccountNumber);
        SSNewProject    iProject    = null;
        SSNewResultUnit iResultUnit = null;

        if(iAccount == null){
            throw new SSImportException(SSBundleString.getString("sieimport.missingaccount", Integer.toString(iAccountNumber) ) );
        }

        if(iObjects.size() % 2 != 0){
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }

        SIEIterator iObjectIterator = new SIEIterator(iObjects);

        while( iObjectIterator.hasNext() ){
            int iDimension = iObjectIterator.nextInteger();
            String iNum = iObjectIterator.next();
            if (iNum == null) {
                continue;
            }
            String iNumber = iNum;

            // Resultatenhet, #DIM 1
            if( iDimension == 1 ){
                iResultUnit =  SSResultUnitMath.getResultUnit(iNumber);

                //if(iResultUnit == null)
                //    throw new RuntimeException("Resultunit not defined: " + iNumber);
            }
            // Projekt, #DIM 6
            if( iDimension == 6 ){
                iProject =  SSProjectMath.getProject(SSDB.getInstance().getProjects(), iNumber);

                //if(iProject == null)
                //    throw new RuntimeException("Project not defined: " + iNumber);
            }

        }
        SSVoucherRow iVoucherRow = new SSVoucherRow();
        iVoucherRow.setAccount   (iAccount);
        iVoucherRow.setProject   (iProject);
        iVoucherRow.setResultUnit(iResultUnit);

        SSVoucherMath.setDebetMinusCredit(iVoucherRow, iAmmount );

        iVoucher.addVoucherRow(iVoucherRow);

        return true;
    }


    /**
     *
     * @param iVoucher
     * @param iExporter
     * @param iWriter
     * @return
     * @throws SSExportException
     */
    public boolean exportEntry(SSVoucher iVoucher, SSSIEExporter iExporter, SIEWriter iWriter) throws SSExportException {

        boolean iHasData = false;
        // #TRANS kontonr {objectlista} belopp [transdat] [transtext] [kvantitet]
        for(SSVoucherRow iVoucherRow: iVoucher.getRows() ){
            SSAccount    iAccount    = iVoucherRow.getAccount();

            if( !iVoucherRow.isValid() || iVoucherRow.isCrossed()) continue;

            iWriter.append( SIELabel.SIE_TRANS);
            iWriter.append( iAccount.getNumber());
            iWriter.append( getObjects(iVoucherRow) );
            iWriter.append( SSVoucherMath.getDebetMinusCredit(iVoucherRow) );
            iWriter.newLine();
            iHasData = true;
        }


        return iHasData;
    }

}
