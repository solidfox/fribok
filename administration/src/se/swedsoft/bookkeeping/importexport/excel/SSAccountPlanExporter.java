package se.swedsoft.bookkeeping.importexport.excel;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.excel.util.*;

import java.util.ResourceBundle;
import java.util.Iterator;
import java.util.Locale;
import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.WritableSheet;

/**
 * Date: 2006-feb-13
 * Time: 16:43:01
 */
public class SSAccountPlanExporter {

    private static ResourceBundle cBundle = SSBundle.getBundle();

    private static String cName  = cBundle.getString("importaccountplan.field_name");
    private static String cType  = cBundle.getString("importaccountplan.field_type");
    private static String cYear  = cBundle.getString("importaccountplan.field_year");
    private static String cStart = cBundle.getString("importaccountplan.field_start");


   private File iFile;


    public SSAccountPlanExporter(File iFile) {
        this.iFile = iFile;
    }

    /**
     *
     * @throws java.io.IOException
     * @throws se.swedsoft.bookkeeping.importexport.util.SSImportException
     */
    public void doExport(SSAccountPlan pAccountPlan) throws IOException, SSImportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        try{
            WritableWorkbook iWorkbook = Workbook.createWorkbook(iFile, iSettings);

            WritableSheet iSheet = iWorkbook.createSheet(pAccountPlan.getName(), 0);

            writeAccountPlan(new SSWritableExcelSheet(iSheet), pAccountPlan );

            iWorkbook.write();
            iWorkbook.close();

        }catch( WriteException e){
            throw new SSExportException( e.getLocalizedMessage() );
        }
    }

    /**
     *
     * @param pSheet
     * @param pAccountPlan
     */
    private void writeAccountPlan(SSWritableExcelSheet pSheet, SSAccountPlan pAccountPlan ) throws WriteException {
        int iRowStart = 6;

        Iterator<SSAccount> iAccounts = pAccountPlan.getAccounts().iterator();

        for(SSWritableExcelRow iRow : pSheet.getRows( pAccountPlan.getAccounts().size() + 6  ) ){

            // Name
            if( iRow.getRow() == 0) {
                iRow.setString(0, cName);
                iRow.setString(1, pAccountPlan.getName() );
                continue;
            }
            // Type
            if( iRow.getRow() == 1) {
                iRow.setString(0, cType);
                iRow.setString(1, pAccountPlan.getType().toString() );
                continue;
            }
            // Assessment year
            if( iRow.getRow() == 2 ){
                iRow.setString(0, cYear);
                iRow.setString(1, pAccountPlan.getAssessementYear() );
                continue;
            }
            // Account offset
            if( iRow.getRow() == 3 ){
                iRow.setString (0, cStart);
                iRow.setNumber(1, iRowStart );
                continue;
            }

            if(iRow.getRow() < iRowStart-1 ) continue;

            if( ! iAccounts.hasNext() ) continue;


            SSAccount iAccount = iAccounts.next();
            for(SSWritableExcelCell iCell : iRow.getCells(5) ){

                switch( iCell.getColumn() ){
                    case 0:
                        iCell.setInteger( iAccount.getNumber( ) );
                        break;
                    case 1:
                        iCell.setString( iAccount.getDescription( ) );
                        break;
                    case 2:
                        iCell.setString( iAccount.getVATCode( ) );
                        break;
                    case 3:
                        iCell.setString( iAccount.getSRUCode( ) );
                        break;
                    case 4:
                        iCell.setString(  iAccount.getReportCode( ) );
                        break;
                }


            }
        }

    }




}
