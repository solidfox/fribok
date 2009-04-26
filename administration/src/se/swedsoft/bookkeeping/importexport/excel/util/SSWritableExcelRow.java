package se.swedsoft.bookkeeping.importexport.excel.util;

import jxl.Sheet;
import jxl.Cell;
import jxl.format.CellFormat;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Date: 2006-feb-14
 * Time: 11:36:34
 */
public class SSWritableExcelRow {

    private int iRow;

    private WritableSheet iSheet ;

    /**
     * 
     * @param pSheet
     * @param pRow
     */
    public SSWritableExcelRow(WritableSheet pSheet, int pRow){
        iSheet = pSheet;
        iRow   = pRow;
    }

    /**
     *
     * @param pCount
     * @return
     */
    public List<SSWritableExcelCell> getCells(int pCount){
        List<SSWritableExcelCell> iList = new LinkedList<SSWritableExcelCell>();

        for(int iColumn = 0; iColumn < pCount; iColumn++){
            iList.add( new SSWritableExcelCell(iSheet, iRow, iColumn) );
        }
        return iList;
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return iRow;
    }


    /**
     *
     */
    public void setString(int iColumn, String pValue) throws WriteException {
        try{
            iSheet.addCell(  new Label(iColumn, iRow, pValue == null ? "" : pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void setString(int iColumn, String pValue, CellFormat iCellFormat) throws WriteException {
        try{
            iSheet.addCell(  new Label(iColumn, iRow, pValue == null ? "" : pValue, iCellFormat) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }



    /**
     *
     * @param iColumn
     * @param pValue
     */
    public void setNumber(int iColumn, java.lang.Number pValue) throws WriteException  {
        try{
            if(pValue == null){
                iSheet.addCell( new Label(iColumn, iRow,  "" ) );
            }   else {
                iSheet.addCell( new Number(iColumn, iRow,  pValue.doubleValue() ) );
            }
        } catch( RowsExceededException e){
            e.printStackTrace();
        }

    }
    /**
     *
     */
    public void setNumber(int iColumn, java.lang.Number pValue, CellFormat iCellFormat) throws WriteException {
        try{
            if(pValue == null){
                iSheet.addCell( new Label(iColumn, iRow,  "", iCellFormat ) );
            }   else {
                iSheet.addCell(  new Number(iColumn, iRow, pValue.doubleValue() , iCellFormat) );
            }
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }
    /**
     *
     */
    public void setDate(int iColumn, Date pValue) throws WriteException {
        SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            if(pValue == null){
                iSheet.addCell( new Label(iColumn, iRow,  "" ) );
            }   else {
                iSheet.addCell(  new Label(iColumn, iRow, iFormat.format(pValue)) );
            }

        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void setDate(int iColumn, Date pValue, CellFormat iCellFormat) throws WriteException {
        SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            if(pValue == null){
                iSheet.addCell( new Label(iColumn, iRow,  "", iCellFormat ) );
            }   else {
                iSheet.addCell(  new Label(iColumn, iRow, iFormat.format(pValue), iCellFormat) );
            }
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }



}
