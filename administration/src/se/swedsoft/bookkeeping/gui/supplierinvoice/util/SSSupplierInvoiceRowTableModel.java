package se.swedsoft.bookkeeping.gui.supplierinvoice.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSSupplierInvoiceRowTableModel extends SSEditableTableModel<SSSupplierInvoiceRow> {
    /**
     *
     * @return
     */
    @Override
    public SSSupplierInvoiceRow newObject() {
        return new SSSupplierInvoiceRow();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSSupplierInvoiceRow.class;
    }


    /**
     * Product column
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_PRODUCT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.1")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            SSProduct iProduct = iObject.getProduct(SSDB.getInstance().getProducts());

            return iProduct != null ? iProduct : iObject.getProductNr();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            if(iValue instanceof SSProduct){
                iObject.setProduct((SSProduct)iValue);
                if (iObject.getProduct() != null && iObject.getProduct().getResultUnitNr() != null) {
                    iObject.setResultUnit(iObject.getProduct().getResultUnit(iObject.getProduct().getResultUnitNr()));
                }
                if (iObject.getProduct() != null && iObject.getProduct().getProjectNr() != null) {
                    iObject.setProject(iObject.getProduct().getProject(iObject.getProduct().getProjectNr()));
                }

            } else {
                iObject.setProductNr((String)iValue);

                SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

                if(iObject.getUnit()    == null) iObject.setUnit   ( iCompany.getStandardUnit() );

                if (iObject.getProduct() != null && iObject.getProduct().getResultUnitNr() != null) {
                    iObject.setResultUnit(iObject.getProduct().getResultUnit(iObject.getProduct().getResultUnitNr()));
                }
                if (iObject.getProduct() != null && iObject.getProduct().getProjectNr() != null) {
                    iObject.setProject(iObject.getProduct().getProject(iObject.getProduct().getProjectNr()));
                }

            }
        }

        @Override
        public Class getColumnClass() {
            return SSProduct.class;
        }

        @Override
        public int getDefaultWidth() {
            return 150;
        }
    };

    /**
     * Description column
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_DESCRIPTION = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.2")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getDescription();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setDescription((String)iValue);

            SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

            if(iObject.getUnit()    == null) iObject.setUnit   ( iCompany.getStandardUnit() );
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 150;
        }
    };


    /**
     * Unit price
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_UNITPRICE = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.3")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getUnitprice();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setUnitprice((BigDecimal)iValue);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     * Unit freight
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_UNITFREIGHT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.4")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getUnitFreight();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setUnitFreight((BigDecimal)iValue);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Quantity
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_QUANTITY = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.5")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getQuantity();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setQuantity((Integer)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 60;
        }
    };

    /**
     * Quantity
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_UNIT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.6")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getUnit();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setUnit((SSUnit)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSUnit.class;
        }

        @Override
        public int getDefaultWidth() {
            return 40;
        }
    };

    /**
     * Sum
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_SUM = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.7")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getSum();
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {

        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Account
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_ACCOUNT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.8")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getAccount( SSDB.getInstance().getAccounts() );
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setAccount((SSAccount)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSAccount.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Account
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_RESULTUNIT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.9")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getResultUnit( SSDB.getInstance().getResultUnits() );
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setResultUnit((SSNewResultUnit)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSNewResultUnit.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Project
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_PROJECT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.10")) {
        @Override
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getProject( SSDB.getInstance().getProjects() );
        }

        @Override
        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setProject((SSNewProject)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSNewProject.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };
}
