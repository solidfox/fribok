package se.swedsoft.bookkeeping.gui.supplierinvoice.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.math.BigDecimal;
import java.util.LinkedList;


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
    public SSSupplierInvoiceRow newObject() {
        return new SSSupplierInvoiceRow();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSSupplierInvoiceRow.class;
    }


    /**
     * Product column
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_PRODUCT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.1")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            SSProduct iProduct = iObject.getProduct(SSDB.getInstance().getProducts());

            return iProduct != null ? iProduct : iObject.getProductNr();
        }

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
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_DESCRIPTION = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.2")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getDescription();
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setDescription((String)iValue);

            SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

            if(iObject.getUnit()    == null) iObject.setUnit   ( iCompany.getStandardUnit() );
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
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_UNITPRICE = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.3")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getUnitprice();
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
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
     * Unit freight
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_UNITFREIGHT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.4")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getUnitFreight();
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setUnitFreight((BigDecimal)iValue);
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
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_QUANTITY = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.5")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getQuantity();
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_UNIT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.6")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getUnit();
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_SUM = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.7")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getSum();
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {

        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Account
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_ACCOUNT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.8")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getAccount( SSDB.getInstance().getAccounts() );
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
            iObject.setAccount((SSAccount)iValue);
        }

        public Class getColumnClass() {
            return SSAccount.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Account
     */
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_RESULTUNIT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.9")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getResultUnit( SSDB.getInstance().getResultUnits() );
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoiceRow> COLUMN_PROJECT = new SSTableColumn<SSSupplierInvoiceRow>(SSBundle.getBundle().getString("supplierinvoicerowtable.column.10")) {
        public Object getValue(SSSupplierInvoiceRow iObject) {
            return iObject.getProject( SSDB.getInstance().getProjects() );
        }

        public void setValue(SSSupplierInvoiceRow iObject, Object iValue) {
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
