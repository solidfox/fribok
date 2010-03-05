package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;

import javax.swing.table.DefaultTableCellRenderer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSTaxCodeCellRenderer extends DefaultTableCellRenderer {

    private Map<SSTaxCode, BigDecimal> iValues;

    /**
     *
     */
    public SSTaxCodeCellRenderer() {
        super();
        iValues = new HashMap<SSTaxCode, BigDecimal>();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            setValue(SSTaxCode.TAXRATE_0, new BigDecimal(0)       );
            setValue(SSTaxCode.TAXRATE_1, iCompany.getTaxRate1() );
            setValue(SSTaxCode.TAXRATE_2, iCompany.getTaxRate2() );
            setValue(SSTaxCode.TAXRATE_3, iCompany.getTaxRate3() );
        }
        setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

    }

    /**
     *
     * @param value
     */
    @Override
    public void setValue(Object value) {
        NumberFormat iFormat = new DecimalFormat("0");

        if(value instanceof SSTaxCode){
            SSTaxCode iTaxCode = (SSTaxCode) value;

            BigDecimal iValue = iValues.get(iTaxCode);

            if(iValue != null){
                setText( iFormat.format(iValue) + "%" );
            } else {
                setText( "" );
            }
        }  else {
            setText( "" );
        }
    }

    /**
     *
     * @param iTaxCode
     * @param iValue
     */
    public void setValue(SSTaxCode iTaxCode, BigDecimal iValue){
        iValues.put(iTaxCode, iValue );
    }

}
