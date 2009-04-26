package se.swedsoft.bookkeeping.data.common;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-28
 * Time: 11:12:04
 */
public enum SSTaxCode implements SSTableSearchable {
    TAXRATE_0("0"),
    TAXRATE_1("1"),
    TAXRATE_2("2"),
    TAXRATE_3("3");

    private String iName;

    /**
     *
     * @param iName
     */
    private SSTaxCode(String iName) {
        this.iName = iName;
    }


    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            NumberFormat iFormat =  NumberFormat.getNumberInstance();

            BigDecimal iValue = null;

            if( this == TAXRATE_0 ) iValue = new BigDecimal(0);
            if( this == TAXRATE_1 ) iValue = iCompany.getTaxRate1();
            if( this == TAXRATE_2 ) iValue = iCompany.getTaxRate2();
            if( this == TAXRATE_3 ) iValue = iCompany.getTaxRate3();

            if(iValue != null)  return iFormat.format(iValue) + "%";
        }

        return iName;
    }



    /**
     *
     * @param iValue
     * @return
     */
    public static SSTaxCode decode(String iValue){
        // Append a % if not
        if(!iValue.endsWith("%") ) iValue = iValue + "%";

        if(iValue.equals( TAXRATE_1.toRenderString() ) ) return TAXRATE_1;
        if(iValue.equals( TAXRATE_2.toRenderString() ) ) return TAXRATE_2;
        if(iValue.equals( TAXRATE_3.toRenderString() ) ) return TAXRATE_3;

        return TAXRATE_0;
    }

    public static SSTaxCode decode2(String iValue){
        // Append a % if not
        if(!iValue.endsWith("%") ) iValue = iValue + "%";

        if(iValue.equals( TAXRATE_0.toRenderString() ) ) return TAXRATE_0;
        if(iValue.equals( TAXRATE_2.toRenderString() ) ) return TAXRATE_2;
        if(iValue.equals( TAXRATE_3.toRenderString() ) ) return TAXRATE_3;

        return TAXRATE_1;
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    public String toString() {
        return toRenderString();
    }
}
