package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSupplierdebtPrinter extends SSPrinter {

    private Map<SSSupplierInvoice, BigDecimal> iSaldos;
    /**
     *
     * @param iDate
     */
    public SSSupplierdebtPrinter( Date iDate ) {
        this( iDate, SSDB.getInstance().getSupplierInvoices() );
    }

    /**
     *
     * @param iDate
     * @param iInvoices
     */
    public SSSupplierdebtPrinter( Date iDate , List<SSSupplierInvoice> iInvoices){
        iSaldos = SSSupplierInvoiceMath.getSaldo(iInvoices, iDate);

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("supplierdebt.jrxml");
        setDetail      ("supplierdebt.jrxml");
        setSummary     ("supplierdebt.jrxml");

        addParameter("periodTitle", iBundle.getString("supplierdebtreport.periodtitle") );
        addParameter("periodText" , iDate);
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return iBundle.getString("supplierdebtreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        // Get all invoices
        List<SSSupplierInvoice> iInvoices = new LinkedList<SSSupplierInvoice>( iSaldos.keySet() );

        // Sort the invoices
        Collections.sort(iInvoices, new Comparator<SSSupplierInvoice>() {
            public int compare(SSSupplierInvoice o1, SSSupplierInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });


        SSDefaultTableModel<SSSupplierInvoice> iModel = new SSDefaultTableModel<SSSupplierInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSSupplierInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSSupplierInvoice iInvoice = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iInvoice.getNumber();
                        break;
                    case 1:
                        value = iInvoice.getSupplierNr();
                        break;
                    case 2:
                        value = iInvoice.getSupplierName();
                        break;
                    case 3:
                        value = iInvoice.getDate()     == null ? null : iFormat.format( iInvoice.getDate() );
                        break;
                    case 4:
                        value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
                        break;
                    case 5:
                        value = SSSupplierInvoiceMath.getTotalSum(iInvoice);
                        break;
                    case 6:
                        value = iSaldos.get(iInvoice);
                        break;
                    case 7:
                        BigDecimal iSaldo = iSaldos.get(iInvoice);

                        value = SSSupplierInvoiceMath.convertToLocal(iInvoice, iSaldo );
                        break;


                }

                return value;
            }
        };

        iModel.addColumn("supplierinvoice.number");

        iModel.addColumn("supplier.number");
        iModel.addColumn("supplier.name");


        iModel.addColumn("supplierinvoice.date");
        iModel.addColumn("supplierinvoice.currency");
        iModel.addColumn("supplierinvoice.sum");
        iModel.addColumn("supplierinvoice.saldo");
        iModel.addColumn("supplierinvoice.localsaldo");

        iModel.setObjects(iInvoices);

        return iModel;
    }






}
