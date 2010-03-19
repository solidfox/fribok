/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.print.util;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Trade Extensions specific imports

// Java specific imports

/**
 * @author Roger Björnstedt
 */
public class SSDefaultJasperDataSource implements JRDataSource {

    //--------------- Constants -------------------------------------

    //--------------- Class variables -------------------------------

    //--------------- Instance variables ----------------------------

    /** The table model with the data to print. */
    private SSDefaultTableModel iModel;

    /** */
    private int iRow;

    /** A map from column names to column indices. */
    private Map<String, Integer> iColumnIndices;

    //--------------- Constructors ----------------------------------

    /**
     * Constructor. <P>
     *
     * @param model The table model to use for generating the report.
     */
    public SSDefaultJasperDataSource(SSDefaultTableModel model) {
        iModel = model;
        iRow = -1;

        List<String> columns = iModel.getColumnNames();
        iColumnIndices = new HashMap<String, Integer>(columns.size() << 1);
        for (int i = 0, size = columns.size(); i < size; i++) {
            iColumnIndices.put(columns.get(i), i);
        }
    }

    //--------------- Methods ---------------------------------------

    /**
     *
     */
    public void reset() {
        iRow = -1;
    }


    /**
     *
     * @return
     *
     * @throws JRException
     */
    public boolean next() throws JRException {
        return ++iRow < iModel.getRowCount();
    }

    /**
     *
     * @param jrField
     *
     * @return
     *
     * @throws JRException
     */
    public Object getFieldValue(JRField jrField) throws JRException {

        Integer column = iColumnIndices.get(jrField.getName());

        return column != null ? iModel.getValueAt(iRow, column) : null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource");
        sb.append("{iColumnIndices=").append(iColumnIndices);
        sb.append(", iModel=").append(iModel);
        sb.append(", iRow=").append(iRow);
        sb.append('}');
        return sb.toString();
    }
} // End of class SSDefaultJasperDataSource
