package se.swedsoft.bookkeeping.importexport.excel.util;

import jxl.Sheet;
import jxl.Cell;
import jxl.write.WritableSheet;

import java.util.List;
import java.util.LinkedList;

import se.swedsoft.bookkeeping.data.SSAccount;

/**
 * Date: 2006-feb-14
 * Time: 11:30:33
 */
public class SSExcelSheet {


    private Sheet iSheet ;


    /**
     *
     * @param pSheet
     */
    public SSExcelSheet(Sheet pSheet){
        iSheet = pSheet;
    }



    /**
     *
     * @return
     */
    public List<SSExcelRow> getRows(){
        List<SSExcelRow> iList = new LinkedList<SSExcelRow>();

        for(int i = 0; i < iSheet.getRows(); i++){
            iList.add( new SSExcelRow(iSheet, i) );
        }
        return iList;
    }





}
