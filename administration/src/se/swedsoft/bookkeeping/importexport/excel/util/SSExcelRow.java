package se.swedsoft.bookkeeping.importexport.excel.util;

import jxl.Sheet;
import jxl.Cell;

import java.util.List;
import java.util.LinkedList;

/**
 * Date: 2006-feb-14
 * Time: 11:36:34
 */
public class SSExcelRow {

    private int iRow;

    private Sheet iSheet ;

    public SSExcelRow(Sheet pSheet, int pRow){
        iSheet = pSheet;
        iRow   = pRow;
    }

    public List<SSExcelCell> getCells(){
         List<SSExcelCell> iList = new LinkedList<SSExcelCell>();

        int iColumn = 0;
        for(Cell iCell: iSheet.getRow(iRow)){
            iList.add( new SSExcelCell(iCell, iRow, iColumn) );
            iColumn++;
        }
        return iList;
    }


    public boolean empty(){
        Cell[] iRows = iSheet.getRow(iRow);
        if( iRows.length == 0) return true;

        for(Cell iCell : iRows){
            if( iCell.getContents() != null && iCell.getContents().length() > 0) return false;
        }
        return true;
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
     * @param pColumn
     * @return
     */
    public String getString(int pColumn){
        return iSheet.getCell(pColumn, iRow).getContents();
    }

    /**
     *
     * @param pColumn
     * @return
     */
    public Integer getInteger(int pColumn){
        try{
            return Integer.parseInt( getString(pColumn) );
        } catch(NumberFormatException e){
            return 0;
        }
    }
    /**
     *
     * @param pColumn
     * @return
     */
    public Double getDouble(int pColumn){
        try{
            return Double.parseDouble( getString(pColumn)  );
        } catch(NumberFormatException e){
            return 0.0;
        }
    }

}
