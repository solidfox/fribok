package se.swedsoft.bookkeeping.importexport.excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import se.swedsoft.bookkeeping.data.SSVoucherTemplate;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.importexport.dialog.SSImportReportDialog;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelCell;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static se.swedsoft.bookkeeping.data.SSVoucherTemplate.SSVoucherTemplateRow;

/**
 * Date: 2006-feb-13
 * Time: 16:43:09
 */
public class SSVoucherTemplateImporter {

    private File iFile;

    private Map<String, Integer> iColumns;

    /**
     *
     * @param iFile
     */
    public SSVoucherTemplateImporter(File iFile) {
        this.iFile = iFile;
        this.iColumns = new HashMap<String, Integer>();
    }


    /**
     *
     */
    public void Import()  throws IOException, SSImportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        List<SSVoucherTemplate> iVoucherTemplates = null;

        try{
            Workbook iWorkbook = Workbook.getWorkbook(iFile, iSettings);

            // Empty workbook, ie nothing to import
            if(iWorkbook.getNumberOfSheets() == 0){
                throw new SSImportException(SSBundle.getBundle(), "vouchertemplateframe.import.nosheets");
            }

            Sheet iSheet = iWorkbook.getSheet(0);

            iVoucherTemplates = importVouchers(new SSExcelSheet(iSheet));

            iWorkbook.close();

        }catch( BiffException e){
            throw new SSImportException( e.getLocalizedMessage() );
        }
        boolean iResult = showImportReport(iVoucherTemplates);
        if( iVoucherTemplates != null && iResult ){
            for (SSVoucherTemplate iVoucherTemplate : iVoucherTemplates) {
                if(!SSDB.getInstance().getVoucherTemplates().contains(iVoucherTemplate)){
                    SSDB.getInstance().addVoucherTemplate(iVoucherTemplate);
                }

            }
        }

    }



    /**
     *
     * @param iColumns
     */
    private void getColumnIndexes(SSExcelRow iColumns) {
        int iIndex = 0;

        this.iColumns.clear();
        for (SSExcelCell iColumn : iColumns.getCells()) {
            String iName = iColumn.getString();

            if(iName != null && iName.length() > 0) {
                if( iName.equalsIgnoreCase( SSVoucherTemplateExporter.BESKRIVNING    ) ) this.iColumns.put(SSVoucherTemplateExporter.BESKRIVNING     , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherTemplateExporter.KONTO          ) ) this.iColumns.put(SSVoucherTemplateExporter.KONTO           , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherTemplateExporter.DEBET          ) ) this.iColumns.put(SSVoucherTemplateExporter.DEBET           , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherTemplateExporter.KREDIT         ) ) this.iColumns.put(SSVoucherTemplateExporter.KREDIT          , iIndex); else
                 throw new SSImportException( "Ogiltigt kolumnnamn i importfilen: %s", iName);

            }
            iIndex++;
        }
    }

    /**
     *
     * @param pSheet
     */
    private List<SSVoucherTemplate> importVouchers(SSExcelSheet pSheet ){

        List<SSExcelRow> iRows = pSheet.getRows( );

        if( iRows.size() < 2 ) throw new SSImportException(SSBundle.getBundle(), "vouchertemplateframe.import.norows");

        getColumnIndexes(iRows.get(0));

        List<SSVoucherTemplate> iVoucherTemplates = new LinkedList<SSVoucherTemplate>();

        SSVoucherTemplate    iVoucherTemplate    = null;
        SSVoucherTemplateRow iVoucherTemplateRow = null;

        for(int row = 1; row < iRows.size(); row++){
            SSExcelRow iRow = iRows.get(row);
            // Skip empty rows
            if( iRow.empty() ) continue;

            List<SSExcelCell> iCells = iRow.getCells();


            // Get the cell
            for(int col = 0; col < iCells.size(); col++){
                SSExcelCell iCell = iCells.get(col);

                String iValue = iCell.getString();

                if(iValue == null || iValue.trim().length() == 0) continue;

                if( iColumns.containsKey(SSVoucherTemplateExporter.BESKRIVNING  ) && iColumns.get(SSVoucherTemplateExporter.BESKRIVNING       ) == col ) {
                    iVoucherTemplate = new SSVoucherTemplate();
                    iVoucherTemplate.setDescription(  iCell.getString() );

                    iVoucherTemplates.add(iVoucherTemplate);
                }

                if(iVoucherTemplate == null) continue;

                if( iColumns.containsKey(SSVoucherTemplateExporter.KONTO        ) && iColumns.get(SSVoucherTemplateExporter.KONTO        ) == col ) {
                    iVoucherTemplateRow = new SSVoucherTemplateRow();
                    iVoucherTemplateRow.setAccountNr ( iCell.getInteger() );

                    iVoucherTemplate.getRows().add(iVoucherTemplateRow);
                }

                if( iVoucherTemplateRow == null) continue;

                if( iColumns.containsKey(SSVoucherTemplateExporter.DEBET        ) && iColumns.get(SSVoucherTemplateExporter.DEBET        ) == col ) iVoucherTemplateRow.setDebet ( iCell.getBigDecimal() );
                if( iColumns.containsKey(SSVoucherTemplateExporter.KREDIT       ) && iColumns.get(SSVoucherTemplateExporter.KREDIT       ) == col ) iVoucherTemplateRow.setCredit( iCell.getBigDecimal() );
            }

        }
        return iVoucherTemplates;
    }


    /**
     *
     * @param iVoucherTemplates
     */
    private boolean showImportReport(List<SSVoucherTemplate> iVoucherTemplates){
        SSImportReportDialog iDialog = new SSImportReportDialog(SSMainFrame.getInstance(), SSBundle.getBundle().getString("vouchertemplateframe.import.report"));
        // Generate the import dialog
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        sb.append("Följande konteringsmallar kommer att importeras:<br>");

        sb.append("<ul>");
        for (SSVoucherTemplate iVoucherTemplate : iVoucherTemplates) {
            sb.append("<li>");
            sb.append(iVoucherTemplate);
            sb.append("</li>");
        }
        sb.append("</ul>");

        sb.append("Fortsätt med importeringen ?");
        sb.append("</html>");


        iDialog.setText( sb.toString() );
        iDialog.setSize( 640, 480);
        iDialog.setLocationRelativeTo(SSMainFrame.getInstance());

        return iDialog.showDialog() == JOptionPane.OK_OPTION;
    }




}
