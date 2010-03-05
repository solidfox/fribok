package se.swedsoft.bookkeeping.importexport.excel.util;

import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.util.List;
import java.util.LinkedList;

/**
 * Date: 2006-feb-14
 * Time: 11:30:33
 */
public class SSWritableExcelSheet {


    private WritableSheet iSheet ;

    
    /**
     *
     * @param pSheet
     */
    public SSWritableExcelSheet(WritableSheet pSheet){
        iSheet = pSheet;

    }

    
    /**
     *
     * @return
     */
    public List<SSWritableExcelRow> getRows(int pCount){
        List<SSWritableExcelRow> iList = new LinkedList<SSWritableExcelRow>();

        for(int iRow = 0; iRow < pCount; iRow++){
            iList.add( new SSWritableExcelRow(iSheet, iRow) );
        }
        return iList;
    }


      /**
     *
     */
    public void setString(int iRow, int iColumn, String pValue) throws WriteException {
      
        try{
            iSheet.addCell(  new Label(iColumn, iRow, pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void setInteger(int iRow, int iColumn, Integer pValue) throws WriteException {
        try{
            iSheet.addCell(  new jxl.write.Number(iColumn, iRow, pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void setDouble(int iRow, int iColumn, Double pValue) throws WriteException {
        try{
            iSheet.addCell(  new Number(iColumn, iRow, pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return
     */
    public WritableSheet getSheet() {
        return iSheet;
    }

    /**
     *
     * @param iSheet
     */
    public void setSheet(WritableSheet iSheet) {
        this.iSheet = iSheet;
    }


}
