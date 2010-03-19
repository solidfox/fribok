package se.swedsoft.bookkeeping.importexport.excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelCell;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Date: 2006-feb-13
 * Time: 16:43:09
 */
public class SSAccountPlanImporter {

    private static ResourceBundle cBundle = SSBundle.getBundle();

    private static String cName  = cBundle.getString("importaccountplan.field_name");
    private static String cType  = cBundle.getString("importaccountplan.field_type");
    private static String cYear  = cBundle.getString("importaccountplan.field_year");
    private static String cStart = cBundle.getString("importaccountplan.field_start");

    private File iFile;

    /**
     *
     * @param iFile
     */
    public SSAccountPlanImporter(File iFile) {
        this.iFile = iFile;
    }


    /**
     *
     * @throws IOException
     * @throws SSImportException
     */
    public void doImport() throws IOException, SSImportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        SSAccountPlan iAccountPlan = new SSAccountPlan();

        try{
            Workbook iWorkbook = Workbook.getWorkbook(iFile, iSettings);

            // Empty workbook, ie nothing to import
            if(iWorkbook.getNumberOfSheets() == 0){
                throw new SSImportException(cBundle, "importaccountplan.nosheets");
            }

            Sheet iSheet = iWorkbook.getSheet(0);

            readAccountPlan(new SSExcelSheet(iSheet), iAccountPlan );

            iWorkbook.close();

        }catch( BiffException e){
            throw new SSImportException( e.getLocalizedMessage() );
        }
        for (SSAccountPlan pAccountPlan : SSDB.getInstance().getAccountPlans()) {
            if (iAccountPlan.getName().equals(pAccountPlan.getName())){
                new SSErrorDialog(SSMainFrame.getInstance(), "accountplanframe.duplicate", iAccountPlan.getName());
                return;
            }
        }
        // Store the account plan.
        SSDB.getInstance().addAccountPlan(iAccountPlan);
    }

    /**
     *
     * @param pSheet
     * @param pAccountPlan
     */
    private void readAccountPlan(SSExcelSheet pSheet, SSAccountPlan pAccountPlan ){
        int iRowStart = Integer.MAX_VALUE;


        for(SSExcelRow iRow : pSheet.getRows() ){

            // Skip empty rows
            if( iRow.empty() ) continue;

            String c = iRow.getString(0);

            // Name
            if(c.startsWith( cName )) {
                pAccountPlan.setName( iRow.getString(1) );
                continue;
            }
            // Type
            if(c.startsWith( cType )) {
                pAccountPlan.setType( iRow.getString(1) );
                continue;
            }
            // Assessment year
            if(c.startsWith( cYear )){
                pAccountPlan.setAssessementYear( iRow.getString(1) );
                continue;
            }
            // Account offset
            if(c.startsWith( cStart )){
                iRowStart = iRow.getInteger(1);
                continue;
            }

            if(iRow.getRow() < iRowStart-1 ) continue;


            SSAccount iAccount = new SSAccount();
            for(SSExcelCell iCell : iRow.getCells() ){

                switch( iCell.getColumn() ){
                    case 0:
                        iAccount.setNumber( iCell.getInteger() );
                        break;
                    case 1:
                        iAccount.setDescription( iCell.getString() );
                        break;
                    case 2:
                        iAccount.setVATCode( iCell.getString() );
                        break;
                    case 3:
                        iAccount.setSRUCode( iCell.getString() );
                        break;
                    case 4:
                        iAccount.setReportCode( iCell.getString() );
                        break;
                }


            }
            pAccountPlan.addAccount(iAccount);

        }

        if(pAccountPlan.getAccounts().isEmpty()){
            throw new SSImportException(cBundle, "importaccountplan.fileempty");
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.excel.SSAccountPlanImporter");
        sb.append("{iFile=").append(iFile);
        sb.append('}');
        return sb.toString();
    }
}
