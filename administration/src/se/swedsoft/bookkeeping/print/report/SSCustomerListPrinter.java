package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSCustomer;
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
public class SSCustomerListPrinter extends SSPrinter {


    private List<SSCustomer> iCustomers;

    /**
     *
     */
    public SSCustomerListPrinter() {
        this( SSDB.getInstance().getCustomers() );
    }

    /**
     *
     * @param iCustomers
     */
    public SSCustomerListPrinter( List<SSCustomer> iCustomers){
        super();
        // Get all orders
        this.iCustomers = iCustomers;

        setPageHeader  ("header.jrxml");
        setColumnHeader("customerlist.jrxml");
        setDetail      ("customerlist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("customerlistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {



        SSDefaultTableModel<SSCustomer> iModel = new SSDefaultTableModel<SSCustomer>() {


            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSCustomer iCustomer = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iCustomer.getNumber();
                        break;
                    case 1:
                        value = iCustomer.getName();
                        break;
                    case 2:
                        value = iCustomer.getYourContactPerson();
                        break;
                    case 3:
                        value = iCustomer.getRegistrationNumber();
                        break;
                    case 4:
                        value = iCustomer.getPhone1();
                        break;
                    case 5:
                        value = iCustomer.getTelefax();
                        break;

                }

                return value;
            }
        };


        iModel.addColumn("customer.number");
        iModel.addColumn("customer.name");
        iModel.addColumn("customer.contact");
        iModel.addColumn("customer.registrationnumber");
        iModel.addColumn("customer.phone");
        iModel.addColumn("customer.telefax");


        Collections.sort(iCustomers, new Comparator<SSCustomer>() {
            public int compare(SSCustomer o1, SSCustomer o2) {
                return o1.getNumber().compareTo( o2.getNumber() );
            }
        });

        iModel.setObjects(iCustomers);


        return iModel;
    }






}
