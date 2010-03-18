package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSupplierListPrinter extends SSPrinter {


    private List<SSSupplier> iSuppliers;

    /**
     *
     */
    public SSSupplierListPrinter() {
        this( SSDB.getInstance().getSuppliers() );
    }

    /**
     *
     * @param iSuppliers
     */
    public SSSupplierListPrinter( List<SSSupplier> iSuppliers){
        super();
        // Get all orders
        this.iSuppliers = iSuppliers;

        setPageHeader  ("header.jrxml");
        setColumnHeader("supplierlist.jrxml");
        setDetail      ("supplierlist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("supplierlistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {



        SSDefaultTableModel<SSSupplier> iModel = new SSDefaultTableModel<SSSupplier>() {


            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSSupplier iSupplier = getObject(rowIndex);


                switch (columnIndex) {
                    case 0  :
                        value = iSupplier.getNumber();
                        break;
                    case 1:
                        value = iSupplier.getName();
                        break;
                    case 2:
                        value = iSupplier.getPhone1();
                        break;
                    case 3:
                        value = iSupplier.getOurCustomerNr();
                        break;
                    case 4:
                        value = iSupplier.getYourContact();
                        break;


                }

                return value;
            }
        };


        iModel.addColumn("supplier.number");
        iModel.addColumn("supplier.name");
        iModel.addColumn("supplier.phone");
        iModel.addColumn("supplier.ourcustomernumber");
        iModel.addColumn("supplier.contact");


        Collections.sort(iSuppliers, new Comparator<SSSupplier>() {
            public int compare(SSSupplier o1, SSSupplier o2) {
                return o1.getNumber().compareTo( o2.getNumber() );
            }
        });

        iModel.setObjects(iSuppliers);


        return iModel;
    }






}
