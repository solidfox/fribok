package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;

import java.util.*;
import java.text.DateFormat;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSCustomerclaimPrinter extends SSPrinter {

    private Map<SSInvoice, BigDecimal> iSaldos;
    /**
     *
     */
    public SSCustomerclaimPrinter( Date iDate ) {
        this( iDate, SSDB.getInstance().getInvoices() );
    }

    /**
     *
     */
    public SSCustomerclaimPrinter( Date iDate , List<SSInvoice> iInvoices){
        super();

        iSaldos = SSInvoiceMath.getSaldo(iInvoices, iDate);

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("customerclaim.jrxml");
        setDetail      ("customerclaim.jrxml");
        setSummary     ("customerclaim.jrxml");

        addParameter("periodTitle", iBundle.getString("customerclaimreport.periodtitle") );
        addParameter("periodText" , iDate);
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        return iBundle.getString("customerclaimreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    protected SSDefaultTableModel getModel() {
        // Get all invoices
        List<SSInvoice> iInvoices = new LinkedList<SSInvoice>( iSaldos.keySet() );

        // Sort the invoices
        Collections.sort(iInvoices, new Comparator<SSInvoice>() {
            public int compare(SSInvoice o1, SSInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });


        SSDefaultTableModel<SSInvoice> iModel = new SSDefaultTableModel<SSInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSInvoice iInvoice = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iInvoice.getNumber();
                        break;
                    case 1:
                        value = iInvoice.getCustomerNr();
                        break;
                    case 2:
                        value = iInvoice.getCustomerName();
                        break;
                    case 3:
                        value = iInvoice.getDate()     == null ? null : iFormat.format( iInvoice.getDate() );
                        break;
                    case 4:
                        value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
                        break;
                    case 5:
                        value = SSInvoiceMath.getTotalSum(iInvoice);
                        break;
                    case 6:
                        value = iSaldos.get(iInvoice);
                        break;
                    case 7:
                        BigDecimal iSaldo = iSaldos.get(iInvoice);

                        value = SSInvoiceMath.convertToLocal(iInvoice, iSaldo );
                        break;


                }

                return value;
            }
        };

        iModel.addColumn("invoice.number");

        iModel.addColumn("customer.number");
        iModel.addColumn("customer.name");


        iModel.addColumn("invoice.date");
        iModel.addColumn("invoice.currency");
        iModel.addColumn("invoice.sum");
        iModel.addColumn("invoice.saldo");
        iModel.addColumn("invoice.localsaldo");

        iModel.setObjects(iInvoices);

        return iModel;
    }






}
