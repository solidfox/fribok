package se.swedsoft.bookkeeping.importexport.excel.util;


import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Date: 2006-feb-14
 * Time: 11:36:34
 */
public class SSWritableExcelRow {

    private int iRow;

    private WritableSheet iSheet;

    /**
     *
     * @param pSheet
     * @param pRow
     */
    public SSWritableExcelRow(WritableSheet pSheet, int pRow) {
        iSheet = pSheet;
        iRow = pRow;
    }

    /**
     *
     * @param pCount
     * @return
     */
    public List<SSWritableExcelCell> getCells(int pCount) {
        List<SSWritableExcelCell> iList = new LinkedList<SSWritableExcelCell>();

        for (int iColumn = 0; iColumn < pCount; iColumn++) {
            iList.add(new SSWritableExcelCell(iSheet, iRow, iColumn));
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
     * @param iColumn
     * @param pValue
     * @throws WriteException
     */
    public void setString(int iColumn, String pValue) throws WriteException {
        try {
            iSheet.addCell(new Label(iColumn, iRow, pValue == null ? "" : pValue));
        } catch (RowsExceededException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iColumn
     * @param pValue
     * @param iCellFormat
     * @throws WriteException
     */
    public void setString(int iColumn, String pValue, CellFormat iCellFormat) throws WriteException {
        try {
            iSheet.addCell(
                    new Label(iColumn, iRow, pValue == null ? "" : pValue, iCellFormat));
        } catch (RowsExceededException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iColumn
     * @param pValue
     * @throws WriteException
     */
    public void setNumber(int iColumn, java.lang.Number pValue) throws WriteException {
        try {
            if (pValue == null) {
                iSheet.addCell(new Label(iColumn, iRow, ""));
            } else {
                iSheet.addCell(new Number(iColumn, iRow, pValue.doubleValue()));
            }
        } catch (RowsExceededException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param iColumn
     * @param pValue
     * @param iCellFormat
     * @throws WriteException
     */
    public void setNumber(int iColumn, java.lang.Number pValue, CellFormat iCellFormat) throws WriteException {
        try {
            if (pValue == null) {
                iSheet.addCell(new Label(iColumn, iRow, "", iCellFormat));
            } else {
                iSheet.addCell(
                        new Number(iColumn, iRow, pValue.doubleValue(), iCellFormat));
            }
        } catch (RowsExceededException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iColumn
     * @param pValue
     * @throws WriteException
     */
    public void setDate(int iColumn, Date pValue) throws WriteException {
        SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (pValue == null) {
                iSheet.addCell(new Label(iColumn, iRow, ""));
            } else {
                iSheet.addCell(new Label(iColumn, iRow, iFormat.format(pValue)));
            }

        } catch (RowsExceededException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iColumn
     * @param pValue
     * @param iCellFormat
     * @throws WriteException
     */
    public void setDate(int iColumn, Date pValue, CellFormat iCellFormat) throws WriteException {
        SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (pValue == null) {
                iSheet.addCell(new Label(iColumn, iRow, "", iCellFormat));
            } else {
                iSheet.addCell(
                        new Label(iColumn, iRow, iFormat.format(pValue), iCellFormat));
            }
        } catch (RowsExceededException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelRow");
        sb.append("{iRow=").append(iRow);
        sb.append(", iSheet=").append(iSheet);
        sb.append('}');
        return sb.toString();
    }
}
