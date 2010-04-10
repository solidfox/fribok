package se.swedsoft.bookkeeping.gui.invoice.util;

import se.swedsoft.bookkeeping.calc.math.SSInpaymentMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.gui.util.table.editors.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSInterestInvoiceTableModel extends SSDefaultTableModel<SSInvoice> {

    private Map<SSInvoice, InterestAction> iActions;

    /**
     * Constructor.
     *
     * @param iInvoices The data for the table model.
     */
    public SSInterestInvoiceTableModel(List<SSInvoice> iInvoices) {
        super(iInvoices);

        iActions   = new HashMap<SSInvoice, InterestAction>();

        for (SSInvoice iInvoice : iInvoices) {
            iActions .put(iInvoice, InterestAction.INVOICE);
        }

        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.1"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.2"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.3"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.4"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.5"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.6"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.7"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.8"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.9"));
        addColumn(SSBundle.getBundle().getString("interestinvoicetable.column.10"));
    }

    @Override
    public Class getType() {
        return SSInvoice.class;
    }

    /**
     * Returns the value for the cell at {@code columnIndex} and
     * {@code rowIndex}.
     *
     * interestinvoicetable.column.1 = Fakturanr
     * interestinvoicetable.column.2 = Kundnummer
     * interestinvoicetable.column.3 = Kundnamn
     * interestinvoicetable.column.4 = Förfallodatum
     * interestinvoicetable.column.5 = Slutbetald
     * interestinvoicetable.column.6 = Valuta
     * interestinvoicetable.column.7 = Fakturabelopp
     * interestinvoicetable.column.8 = Ränta
     * interestinvoicetable.column.9 = Räntasumma
     * interestinvoicetable.column.10= Åtgärd
     *
     * @param    rowIndex    the row whose value is to be queried
     * @param    columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSInvoice iInvoice = getObject(rowIndex);

        Object value = null;
        switch(columnIndex){
            case 0:
                value = iInvoice.getNumber();
                break;
            case 1:
                value = iInvoice.getCustomerNr();
                break;
            case 2:
                value = iInvoice.getCustomerName();
                break;
            case 3:
                value = iInvoice.getDueDate();
                break;
            case 4:
                value = SSInpaymentMath.getLastInpaymentForInvoice(iInvoice);
                break;
            case 5:
                value =  SSInvoiceMath.getInterestSaldo(iInvoice);
                break;
            case 6:
                value =  iInvoice.getCurrency();
                break;
            case 7:
                value =  iInvoice.getDelayInterest();
                break;
            case 8:
                value = SSInvoiceMath.getInterestSum(iInvoice, SSInvoiceMath.getInterestSaldo(iInvoice), SSInvoiceMath.getNumDelayedDays(iInvoice) );
                break;
            case 9:
                value = iActions.get(iInvoice);
                break;
        }


        return value;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return SSInvoice.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Date.class;
            case 4:
                return Date.class;
            case 5:
                return BigDecimal.class;
            case 6:
                return SSCurrency.class;
            case 7:
                return BigDecimal.class;
            case 8:
                return BigDecimal.class;
            case 9:
                return InterestAction.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 9;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SSInvoice iInvoice = getObject(rowIndex);

        switch(columnIndex){
            case 9:
                iActions.put(iInvoice, (InterestAction)aValue);
                break;
        }
        fireTableDataChanged();
    }

    /**
     * Get the interest invoices depending on the user selections.
     *
     * Sets the "interest invoiced" flag on the invoices to true.
     *
     * @param iDescription
     * @param iAccount
     * @return the interest invoices
     */
    public List<SSInvoice> getInterestInvoices(String iDescription, SSAccount iAccount){

        List<SSInvoice> iInvoices = new LinkedList<SSInvoice>();

        for (SSInvoice iInvoice : getObjects()) {

            if(SSPostLock.isLocked("invoice"+iInvoice.getNumber()+ SSDB.getInstance().getCurrentCompany().getId())){
                new SSErrorDialog(new JFrame(), "interestinvoice.invoiceopen", iInvoice.getNumber(), iInvoice.getNumber());
                iInvoice.setInterestInvoiced(false);
                continue;
            }

            InterestAction iAction   = iActions  .get(iInvoice);

            // Don't do anything with the sales
            if(iAction == InterestAction.SKIP      ) continue;

            // If we selected INVOICE or OBLITERATE, set the flag to true
            iInvoice.setInterestInvoiced(true);

            SSDB.getInstance().updateInvoice(iInvoice);
            // Obliterate the sales, no need to create a new invoiec
            if(iAction == InterestAction.OBLITERATE) continue;

            // Create the interest sales
            SSInvoice iInterestInvoice = new SSInvoice(iInvoice);

            iInterestInvoice.setEntered(false);

            iInterestInvoice.setPrinted(false);

            iInterestInvoice.setNumRemainders(0);

            iInterestInvoice.setDate(new Date() );

            iInterestInvoice.setEuSaleCommodity(false);

            iInterestInvoice.setEuSaleYhirdPartCommodity(false);

            iInterestInvoice.setDueDate();

            iInterestInvoice.setInterestInvoiced(false);
                                        /*
            if(iInterestInvoice.getPaymentTerm() != null){
                int iDays = iInterestInvoice.getPaymentTerm().decodeValue();

                Calendar iCalendar = Calendar.getInstance();

                iCalendar.add(Calendar.DAY_OF_MONTH, iDays);

                iInterestInvoice.setDueDate( iCalendar.getTime() );
            } else {
                iInterestInvoice.setDueDate( new Date() );

            }     */

            SSSaleRow iRow = new SSSaleRow();

            iRow.setDescription( iDescription        );
            iRow.setAccount    ( iAccount            );
            iRow.setTaxCode    ( SSTaxCode.TAXRATE_0 );
            iRow.setQuantity   ( 1                   );
            iRow.setUnitprice  ( SSInvoiceMath.getInterestSum(iInvoice, SSInvoiceMath.getInterestSaldo(iInvoice), SSInvoiceMath.getNumDelayedDays(iInvoice) ) );

            // Set the rows to the tax row
            iInterestInvoice.setRows( iRow );
            // Add the interest sales to the list of invoices
            iInvoices.add(iInterestInvoice);
        }

        return iInvoices;
    }



    /**
     *
     * @param iTable
     */
    public static void setupTable(SSTable iTable){
        iTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        iTable.getColumnModel().getColumn( 0).setPreferredWidth(70);
        iTable.getColumnModel().getColumn( 1).setPreferredWidth(120);
        iTable.getColumnModel().getColumn( 2).setPreferredWidth(120);
        iTable.getColumnModel().getColumn( 3).setPreferredWidth(90);
        iTable.getColumnModel().getColumn( 4).setPreferredWidth(90);
        iTable.getColumnModel().getColumn( 5).setPreferredWidth(80);
        iTable.getColumnModel().getColumn( 6).setPreferredWidth(50);
        iTable.getColumnModel().getColumn( 7).setPreferredWidth(60);
        iTable.getColumnModel().getColumn( 8).setPreferredWidth(80);
        iTable.getColumnModel().getColumn( 9).setPreferredWidth(90);

        iTable.getColumnModel().getColumn(7).setCellRenderer( new SSPercentCellRenderer(2)  );


        iTable.setDefaultRenderer(Date.class, new SSDateCellRenderer() );

        iTable.setDefaultRenderer(SSCurrency.class, new SSCurrencyCellRenderer() );

        iTable.setDefaultEditor  (BigDecimal.class, new SSBigDecimalCellEditor(2));
        iTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2));

        iTable.setDefaultEditor  (InterestAction.class, new InterestActionCellEditor());
        iTable.setDefaultRenderer(InterestAction.class, new InterestActionCellRenderer());

        // iTable.getColumnModel().getColumn( 8).setCellEditor     (new SSBigDecimalCellEditor  (5));
        //  iTable.getColumnModel().getColumn( 8).setCellRenderer   (new SSBigDecimalCellRenderer(5));
    }











    /**
     *
     */
    private enum InterestAction implements SSTableSearchable {
        SKIP      ("interestinvoice.action.1"),
        INVOICE   ("interestinvoice.action.2"),
        OBLITERATE("interestinvoice.action.3");

        private String iBundleName;

        /**
         *
         * @param iBundleName
         */
        InterestAction(String iBundleName) {
            this.iBundleName = iBundleName;
        }

        /**
         *
         * @return
         */
        public String getDescription(){
            return SSBundle.getBundle().getString(iBundleName);
        }

        /**
         * Returns the render string to be shown in the tables
         *
         * @return The searchable string
         */
        public String toRenderString() {
            return getDescription();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.gui.invoice.util.SSInterestInvoiceTableModel.InterestAction");
            sb.append("{iBundleName='").append(iBundleName).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    private static class InterestActionCellEditor extends DefaultCellEditor {

        /**
         * Constructs a {@code DefaultCellEditor} that uses a text field.
         *
         */
        public InterestActionCellEditor() {
            super(new JComboBox() );

            JComboBox iComboBox = (JComboBox)getComponent();

            iComboBox.setModel( new DefaultComboBoxModel( InterestAction.values() ) );
            iComboBox.setRenderer(new DefaultListCellRenderer(){
                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    if(value instanceof InterestAction){
                        InterestAction iAction = (InterestAction) value;

                        setText( iAction.getDescription() );
                    } else {
                        setText("");
                    }

                    return this;
                }
            });
        }
    }

    private static class InterestActionCellRenderer extends DefaultTableCellRenderer {

        @Override
        public void setValue(Object value) {
            if(value instanceof InterestAction){
                InterestAction iAction = (InterestAction) value;

                setText(iAction.getDescription());
            }  else {
                setText( "" );
            }
        }


    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.invoice.util.SSInterestInvoiceTableModel");
        sb.append("{iActions=").append(iActions);
        sb.append('}');
        return sb.toString();
    }
}


