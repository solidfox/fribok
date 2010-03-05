package se.swedsoft.bookkeeping.importexport.excel.util;

import jxl.Cell;
import jxl.NumberCell;
import jxl.DateCell;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

/**
 * Date: 2006-feb-14
 * Time: 11:57:23
 */
public class SSExcelCell {

    private int iRow ;

    private int iColumn ;

    private Cell iCell ;

    public SSExcelCell(Cell pCell, int pRow, int pColumn ){
        iCell   = pCell;
        iRow    = pRow;
        iColumn = pColumn;

    }
    /**
     *
     * @return
     */
    public String getString(){
        return iCell.getContents();
    }

    /**
     *
     * @return
     */
    public Integer getInteger(){
        if( iCell instanceof NumberCell){
            NumberCell iNumber = (NumberCell) iCell;

            return Math.round( (float)iNumber.getValue() );
        }
        try{
            return Integer.parseInt( iCell.getContents()  );
        } catch(NumberFormatException e){
            return 0;
        }
    }
    /**
     *
     * @return
     */
    public Double getDouble(){
        if( iCell instanceof NumberCell){
            NumberCell iNumber = (NumberCell) iCell;

            return iNumber.getValue();
        }

        try{
            return Double.parseDouble( iCell.getContents()  );
        } catch(NumberFormatException e){
            return 0.0;
        }
    }



    /**
     *
     * @return
     */
    public Date getDate(){
        if( iCell instanceof DateCell  ){
            DateCell   iDateCell   = (DateCell  ) iCell;

            return iDateCell.getDate();
        }
        SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            return iFormat.parse( iCell.getContents()  );
        } catch(Exception e){
            return new Date();
        }
    }
    /**
     *
     * @return
     */
    public BigDecimal getBigDecimal(){
        if( iCell instanceof NumberCell){
            NumberCell iNumber = (NumberCell) iCell;

            return new BigDecimal(iNumber.getValue());
        }
        try{
            return new BigDecimal( iCell.getContents()  );
        } catch(NumberFormatException e){
            return null;
        }
    }




    /**
     *
     * @return
     */
    public int getColumn() {
        return iColumn;
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return iRow;
    }

}
