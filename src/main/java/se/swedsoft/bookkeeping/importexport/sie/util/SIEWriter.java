package se.swedsoft.bookkeeping.importexport.sie.util;


import se.swedsoft.bookkeeping.data.SSMonth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Date: 2006-feb-22
 * Time: 11:53:59
 */
public class SIEWriter {

    private static DateFormat iFormat = new SimpleDateFormat("yyyyMMdd");

    private StringBuilder iStringBuilder;

    private List<String> iLines;

    /**
     *
     */
    public SIEWriter() {
        iStringBuilder = new StringBuilder();
        iLines = new LinkedList<String>();
    }

    /**
     * Adds the contents of the stringbuilder to the writer and reset the stringbuilder.
     */
    public void newLine() {
        iLines.add(iStringBuilder.toString());

        iStringBuilder = new StringBuilder();
    }

    /**
     * Adds a new line to the writer and reset the stringbuilder
     * @param iLine
     */
    public void newLine(String iLine) {
        iLines.add(iLine);

        iStringBuilder = new StringBuilder();
    }

    /**
     *
     * @param pValue
     */
    public void append(String pValue) {
        if (pValue != null && pValue.contains(" ")) {
            iStringBuilder.append('\"');
            iStringBuilder.append(pValue);
            iStringBuilder.append("\" ");
        } else {
            iStringBuilder.append(pValue);
            iStringBuilder.append(' ');
        }
    }

    /**
     *
     * @param pValue
     */
    public void append(Object pValue) {
        iStringBuilder.append(pValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValues
     */
    public void append(Object... pValues) {
        iStringBuilder.append('{');
        for (Object iObject: pValues) {
            if (iObject != null) {
                append(iObject);
            }
        }
        iStringBuilder.append("} ");
    }

    /**
     *
     * @param pValues
     */
    public void append(List<Object> pValues) {
        iStringBuilder.append('{');
        for (Object iObject: pValues) {
            if (iObject != null) {
                append(iObject);
            }
        }
        iStringBuilder.append("} ");
    }

    /**
     *
     * @param pValue
     */
    public void append(SSMonth pValue) {
        DateFormat iFormat = new SimpleDateFormat("yyyyMM");

        String iValue;

        if (pValue != null) {
            iValue = iFormat.format(pValue.getDate());
        } else {
            iValue = "000000";
        }
        iStringBuilder.append(iValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(SIELabel pValue) {
        iStringBuilder.append(pValue.getName());
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(Date pValue) {
        String iValue;

        if (pValue != null) {
            iValue = iFormat.format(pValue).replace("-", "");
        } else {
            iValue = "00000000";
        }
        iStringBuilder.append(iValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(Integer pValue) {
        iStringBuilder.append(pValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(Float pValue) {
        if (pValue != null) {
            append(pValue.doubleValue());
        } else {
            append(0);
        }
    }

    /**
     *
     * @param pValue
     */
    public void append(Double pValue) {
        NumberFormat iFormat = NumberFormat.getNumberInstance();

        iFormat.setMinimumFractionDigits(2);
        iFormat.setMaximumFractionDigits(2);
        iFormat.setGroupingUsed(false);

        String iValue = iFormat.format(pValue);

        iValue = iValue.replace(",", ".");

        iStringBuilder.append(iValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(boolean pValue) {
        iStringBuilder.append(pValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(BigInteger pValue) {
        iStringBuilder.append(pValue);
        iStringBuilder.append(' ');
    }

    /**
     *
     * @param pValue
     */
    public void append(BigDecimal pValue) {
        append(pValue.doubleValue());
    }

    /**
     *
     * @return
     */
    public String getLine() {
        return iStringBuilder.toString().trim();
    }

    /**
     *
     * @return
     */
    public List<String> getLines() {
        // List<String> lines = new LinkedList<String>(iLines);

        // lines.add(iStringBuilder.toString());

        return iLines;
    }

    public String toString() {
        return iStringBuilder.toString().trim();
    }

}
