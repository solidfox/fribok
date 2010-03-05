package se.swedsoft.bookkeeping.importexport.excel;

import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelSheet;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelCell;
import se.swedsoft.bookkeeping.importexport.dialog.SSImportReportDialog;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import java.io.File;
import java.io.IOException;
import java.util.*;

import jxl.WorkbookSettings;
import jxl.Workbook;
import jxl.Sheet;
import jxl.read.biff.BiffException;

import javax.swing.*;

/**
 * Date: 2006-feb-13
 * Time: 16:43:09
 */
public class SSVoucherImporter {

    private File iFile;

    private Map<String, Integer> iColumns;

    /**
     *
     * @param iFile
     */
    public SSVoucherImporter(File iFile) {
        this.iFile = iFile;
        this.iColumns = new HashMap<String, Integer>();
    }


    /**
     *
     */
    public void Import()  throws IOException, SSImportException {
        final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        List<SSVoucher> iVouchers = null;

        try{
            Workbook iWorkbook = Workbook.getWorkbook(iFile, iSettings);

            // Empty workbook, ie nothing to import
            if(iWorkbook.getNumberOfSheets() == 0){
                throw new SSImportException(SSBundle.getBundle(), "voucherframe.import.nosheets");
            }

            Sheet iSheet = iWorkbook.getSheet(0);

            iVouchers = importVouchers(new SSExcelSheet(iSheet));

            iWorkbook.close();

        }catch( BiffException e){
            throw new SSImportException( e.getLocalizedMessage() );
        }
        if (iVouchers != null && showImportReport(iVouchers)) {
            for (SSVoucher iVoucher : iVouchers) {
                if (!SSDB.getInstance().getVouchers().contains(iVoucher)) {
                    SSDB.getInstance().addVoucher(iVoucher,false);
                }
            }
        }
        SSPostLock.removeLock(lockString);

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
                if( iName.equalsIgnoreCase( SSVoucherExporter.NUMMER         ) ) this.iColumns.put(SSVoucherExporter.NUMMER          , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.BESKRIVNING    ) ) this.iColumns.put(SSVoucherExporter.BESKRIVNING     , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.DATUM          ) ) this.iColumns.put(SSVoucherExporter.DATUM           , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.KONTO          ) ) this.iColumns.put(SSVoucherExporter.KONTO           , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.DEBET          ) ) this.iColumns.put(SSVoucherExporter.DEBET           , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.KREDIT         ) ) this.iColumns.put(SSVoucherExporter.KREDIT          , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.PROJEKT        ) ) this.iColumns.put(SSVoucherExporter.PROJEKT         , iIndex); else
                if( iName.equalsIgnoreCase( SSVoucherExporter.RESULTATENHET  ) ) this.iColumns.put(SSVoucherExporter.RESULTATENHET   , iIndex); else
                 throw new SSImportException( "Ogiltigt kolumnnamn i importfilen: %s", iName);

            }
            iIndex++;
        }
    }

    /**
     *
     * @param pSheet
     */
    private List<SSVoucher> importVouchers(SSExcelSheet pSheet ){

        List<SSExcelRow> iRows = pSheet.getRows( );

        if( iRows.size() < 2 ) throw new SSImportException(SSBundle.getBundle(), "voucherframe.import.norows");

        getColumnIndexes(iRows.get(0));

        List<SSVoucher> iVouchers = new LinkedList<SSVoucher>();

        SSVoucher    iVoucher    = null;
        SSVoucherRow iVoucherRow = null;

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

                if( iColumns.containsKey(SSVoucherExporter.NUMMER       ) && iColumns.get(SSVoucherExporter.NUMMER       ) == col ) {
                    iVoucher = new SSVoucher();
                    iVoucher.setNumber(  iCell.getInteger() );

                    iVouchers.add(iVoucher);
                }

                if(iVoucher == null) continue;

                if( iColumns.containsKey(SSVoucherExporter.BESKRIVNING  ) && iColumns.get(SSVoucherExporter.BESKRIVNING  ) == col ) iVoucher.setDescription ( iValue );
                if( iColumns.containsKey(SSVoucherExporter.DATUM        ) && iColumns.get(SSVoucherExporter.DATUM        ) == col ) iVoucher.setDate        ( iCell.getDate() );

                if( iColumns.containsKey(SSVoucherExporter.KONTO        ) && iColumns.get(SSVoucherExporter.KONTO        ) == col ) {
                    iVoucherRow = new SSVoucherRow();
                    iVoucherRow.setAccountNr ( iCell.getInteger() );

                    iVoucher.getRows().add(iVoucherRow);
                }

                if( iVoucherRow == null) continue;

                if( iColumns.containsKey(SSVoucherExporter.DEBET        ) && iColumns.get(SSVoucherExporter.DEBET        ) == col ) iVoucherRow.setDebet ( iCell.getBigDecimal() );
                if( iColumns.containsKey(SSVoucherExporter.KREDIT       ) && iColumns.get(SSVoucherExporter.KREDIT       ) == col ) iVoucherRow.setCredit( iCell.getBigDecimal() );
                if( iColumns.containsKey(SSVoucherExporter.PROJEKT      ) && iColumns.get(SSVoucherExporter.PROJEKT      ) == col ) iVoucherRow.setProjectNr   ( iCell.getString() );
                if( iColumns.containsKey(SSVoucherExporter.RESULTATENHET) && iColumns.get(SSVoucherExporter.RESULTATENHET) == col ) iVoucherRow.setResultUnitNr( iCell.getString() );
            }

        }
        return iVouchers;
    }


    /**
     *
     * @param iVouchers
     */
    private boolean showImportReport(List<SSVoucher> iVouchers){
        SSImportReportDialog iDialog = new SSImportReportDialog(SSMainFrame.getInstance(), SSBundle.getBundle().getString("voucherframe.import.report"));
        // Generate the import dialog
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        sb.append("Följande verifikationer kommer att importeras:<br>");

        sb.append("<ul>");
        for (SSVoucher iVoucher : iVouchers) {
            sb.append("<li>");
            sb.append(iVoucher);
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
