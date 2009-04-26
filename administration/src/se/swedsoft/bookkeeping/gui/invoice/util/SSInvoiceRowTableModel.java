package se.swedsoft.bookkeeping.gui.invoice.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.*;
import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSInvoiceRowTableModel extends SSEditableTableModel<SSSaleRow> {

    private static SSCustomer iCustomer;

    /**
     *
     * @return
     */
    public SSSaleRow newObject() {
        return  new SSSaleRow();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSSaleRow.class;
    }


    public void setCustomer(SSCustomer pCustomer) {
        iCustomer = pCustomer;
    }


    /**
     * Product column
     */
    public static SSTableColumn<SSSaleRow> COLUMN_PRODUCT = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.1")) {
        public Object getValue(SSSaleRow iObject) {
            SSProduct iProduct = iObject.getProduct(SSDB.getInstance().getProducts());

            return iProduct != null ? iProduct : iObject.getProductNr();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            if(iValue instanceof SSProduct){
                iObject.setProduct((SSProduct)iValue);
                if (iCustomer != null && iCustomer.getDiscount() != null) {
                    iObject.setDiscount(iCustomer.getDiscount().doubleValue() == (new BigDecimal(0.0)).doubleValue() ? null : iCustomer.getDiscount());
                }
                if (iObject.getProduct() != null && iObject.getProduct().getResultUnitNr() != null) {
                    iObject.setResultUnit(iObject.getProduct().getResultUnit(iObject.getProduct().getResultUnitNr()));
                }
                if (iObject.getProduct() != null && iObject.getProduct().getProjectNr() != null) {
                    iObject.setProject(iObject.getProduct().getProject(iObject.getProduct().getProjectNr()));
                }
            } else {
                iObject.setProductNr((String)iValue);

                SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

                if(iObject.getTaxCode() == null) iObject.setTaxCode( SSTaxCode.TAXRATE_1 );
                if(iObject.getUnit()    == null) iObject.setUnit   ( iCompany.getStandardUnit() );

                if (iCustomer != null && iCustomer.getDiscount() != null) {
                    iObject.setDiscount(iCustomer.getDiscount().doubleValue() == (new BigDecimal(0.0)).doubleValue() ? null : iCustomer.getDiscount());
                }
                if (iObject.getProduct() != null && iObject.getProduct().getResultUnit() != null) {
                    iObject.setResultUnit(iObject.getProduct().getResultUnit());
                }
                if (iObject.getProduct() != null && iObject.getProduct().getProject() != null) {
                    iObject.setProject(iObject.getProduct().getProject());
                }


            }
        }

        public Class getColumnClass() {
            return SSProduct.class;
        }

        public int getDefaultWidth() {
            return 150;
        }
    };

    /**
     * Description column
     */
    public static SSTableColumn<SSSaleRow> COLUMN_DESCRIPTION = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.2")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getDescription();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setDescription((String)iValue);

            SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

            if(iObject.getTaxCode() == null) iObject.setTaxCode ( SSTaxCode.TAXRATE_1 );
            if(iObject.getUnit()    == null) iObject.setUnit    ( iCompany.getStandardUnit() );
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 150;
        }
    };


    /**
     * Unit price
     */
    public static SSTableColumn<SSSaleRow> COLUMN_UNITPRICE = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.3")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getUnitprice();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setUnitprice((BigDecimal)iValue);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };





    /**
     * Quantity
     */
    public static SSTableColumn<SSSaleRow> COLUMN_QUANTITY = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.4")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getQuantity();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setQuantity((Integer)iValue);
        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 60;
        }
    };

    /**
     * Quantity
     */
    public static SSTableColumn<SSSaleRow> COLUMN_UNIT = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.5")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getUnit();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setUnit((SSUnit)iValue);
        }

        public Class getColumnClass() {
            return SSUnit.class;
        }

        public int getDefaultWidth() {
            return 40;
        }
    };

    /**
     * Sum
     */
    public static SSTableColumn<SSSaleRow> COLUMN_DISCOUNT = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.6")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getDiscount();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setDiscount((BigDecimal)iValue);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     * Sum
     */
    public static SSTableColumn<SSSaleRow> COLUMN_SUM = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.7")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getSum();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {

        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Moms
     */
    public static SSTableColumn<SSSaleRow> COLUMN_TAX = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.8")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getTaxCode();
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setTaxCode((SSTaxCode)iValue);
        }

        public Class getColumnClass() {
            return SSTaxCode.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     * Account
     */
    public static SSTableColumn<SSSaleRow> COLUMN_ACCOUNT = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.9")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getAccount( SSDB.getInstance().getAccounts() );
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            if(iValue instanceof SSAccount){
                iObject.setAccount((SSAccount)iValue);
            }

        }

        public Class getColumnClass() {
            return SSAccount.class;
        }

        public int getDefaultWidth() {
            return 60;
        }
    };

    /**
     * Account
     */
    public static SSTableColumn<SSSaleRow> COLUMN_RESULTUNIT = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.10")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getResultUnit( SSDB.getInstance().getResultUnits() );
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setResultUnit((SSNewResultUnit)iValue);
        }

        public Class getColumnClass() {
            return SSNewResultUnit.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Project
     */
    public static SSTableColumn<SSSaleRow> COLUMN_PROJECT = new SSTableColumn<SSSaleRow>(SSBundle.getBundle().getString("salerowtable.column.11")) {
        public Object getValue(SSSaleRow iObject) {
            return iObject.getProject( SSDB.getInstance().getProjects() );
        }

        public void setValue(SSSaleRow iObject, Object iValue) {
            iObject.setProject((SSNewProject)iValue);
        }

        public Class getColumnClass() {
            return SSNewProject.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };




}
