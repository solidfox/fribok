package se.swedsoft.bookkeeping.gui.util.model;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 12:13:59
 */
public class SSTaxCodeTableModel extends SSTableModel<SSTaxCode> {

    private Map<SSTaxCode, BigDecimal> iValues;


    /**
     * Default constructor.
     */
    public SSTaxCodeTableModel() {
        super( SSTaxCode.values() );
        iValues = new HashMap<SSTaxCode, BigDecimal>();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if(iCompany != null){
            iValues.put(SSTaxCode.TAXRATE_0, new BigDecimal(0)       );
            iValues.put(SSTaxCode.TAXRATE_1, iCompany.getTaxRate1() );
            iValues.put(SSTaxCode.TAXRATE_2, iCompany.getTaxRate2() );
            iValues.put(SSTaxCode.TAXRATE_3, iCompany.getTaxRate3() );
        }
    }

    /**
     * Default constructor.
     * @param iValues
     */
    public SSTaxCodeTableModel(Map<SSTaxCode, BigDecimal> iValues) {
        super( SSTaxCode.values() );
        this.iValues = iValues;
    }




    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSTaxCode.class;
    }


    /**
     *
     * @return
     */
    public SSTableColumn<SSTaxCode> getValueColumn(){
        return new ValueColumn();
    }


    /**
     *
     * @return
     */
    public static SSTaxCodeTableModel getDropDownModel(){
        SSTaxCodeTableModel iModel = new SSTaxCodeTableModel();

        iModel.addColumn( iModel.getValueColumn() );
        iModel.sort();

        return iModel;
    }


    /**
     *
     * @param iValues
     * @return
     */
    public static SSTaxCodeTableModel getDropDownModel(Map<SSTaxCode, BigDecimal> iValues){
        SSTaxCodeTableModel iModel = new SSTaxCodeTableModel(iValues);

        iModel.addColumn( iModel.getValueColumn() );
        iModel.sort();

        return iModel;
    }


    /**
     *  Fakturanr
     */
    private class ValueColumn extends SSTableColumn<SSTaxCode> {

        public ValueColumn() {
            super("");
        }

        @Override
        public Object getValue(SSTaxCode iTaxCode) {
            BigDecimal iValue = iValues.get(iTaxCode);

            NumberFormat iFormat = new DecimalFormat("0");

            return iFormat.format(iValue) + '%';
        }

        @Override
        public void setValue(SSTaxCode iInvoice, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 250;
        }
    }





    /**
     *
     */
    public void sort(){
        Collections.sort(getObjects(), new Comparator<SSTaxCode>() {
            public int compare(SSTaxCode o1, SSTaxCode o2) {
                BigDecimal iValue1 = iValues.get(o1);
                BigDecimal iValue2 = iValues.get(o2);

                return (iValue1 == null || iValue2 == null) ? 0 : iValue1.compareTo(iValue2);
            }
        });
        fireTableDataChanged();
    }



}
