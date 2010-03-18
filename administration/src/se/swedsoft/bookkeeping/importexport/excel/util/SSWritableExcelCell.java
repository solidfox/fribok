package se.swedsoft.bookkeeping.importexport.excel.util;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Date: 2006-feb-14
 * Time: 11:57:23
 */
public class SSWritableExcelCell {

    private int iRow ;

    private int iColumn ;

    private WritableSheet iSheet ;

    /**
     *
     * @param pSheet
     * @param pRow
     * @param pColumn
     */
    public SSWritableExcelCell(WritableSheet pSheet, int pRow, int pColumn ){
        iSheet  = pSheet;
        iRow    = pRow;
        iColumn = pColumn;

    }

    /**
     *
     * @param pValue
     * @throws jxl.write.WriteException
     */
    public void setString(String pValue) throws WriteException {
        try{
            iSheet.addCell(  new Label(iColumn, iRow, pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param pValue
     * @throws jxl.write.WriteException
     */
    public void setInteger(Integer pValue) throws WriteException {
        try{
            iSheet.addCell(  new Number(iColumn, iRow, pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param pValue
     * @throws jxl.write.WriteException
     */
    public void setDouble(Double pValue) throws WriteException {
        try{
            iSheet.addCell(  new Number(iColumn, iRow, pValue) );
        } catch( RowsExceededException e){
            e.printStackTrace();
        }   
    }



    /**
     *
     * @return The column
     */
    public int getColumn() {
        return iColumn;
    }

    /**
     *
     * @return The row
     */
    public int getRow() {
        return iRow;
    }

}
