package se.swedsoft.bookkeeping.print.dialog;


import se.swedsoft.bookkeeping.calc.math.SSIndeliveryMath;
import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.data.SSIndelivery;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.product.util.SSProductTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-apr-19
 * Time: 14:28:34
 */
public class SSIndeliveryListDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JCheckBox iCheckDate;
    private JCheckBox iCheckProduct;

    private SSTableComboBox<SSProduct> iProduct;
    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    /**
     *
     * @param iMainFrame
     */
    public SSIndeliveryListDialog(SSMainFrame iMainFrame) {
        super(iMainFrame,
                SSBundle.getBundle().getString("indeliverylistreport.dialog.title"));

        setPanel(iPanel);

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION, true);
            }
        });
        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION, true);
            }
        });

        iProduct.setModel(
                SSProductTableModel.getDropDownModel(SSProductMath.getNormalProducts()));
        iProduct.setSearchColumns(0);
        iProduct.setSelected(iProduct.getFirst());

        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iProduct.setEnabled(iCheckProduct.isSelected());

                iFromDate.setEnabled(iCheckDate.isSelected());
                iToDate.setEnabled(iCheckDate.isSelected());
            }
        };

        iCheckDate.addChangeListener(iChangeListener);
        iCheckProduct.addChangeListener(iChangeListener);

        iChangeListener.stateChanged(null);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     * Returns the invoices to print depending on the user selections
     *
     * @return
     */
    public List<SSIndelivery> getElementsToPrint() {
        List<SSIndelivery> iIndeliveries = SSDB.getInstance().getIndeliveries();

        // Filter by a product
        if (iCheckProduct.isSelected() && iProduct.getSelected() != null) {
            final SSProduct iProduct = this.iProduct.getSelected();

            iIndeliveries = SSFilterFactory.doFilter(iIndeliveries,
                    new SSFilter<SSIndelivery>() {
                public boolean applyFilter(SSIndelivery iIndelivery) {
                    return  SSIndeliveryMath.hasProduct(iIndelivery, iProduct);
                }
            });

        }
        // Filter by date
        if (iCheckDate.isSelected()) {
            final Date iDateFrom = iFromDate.getDate();
            final Date iDateTo = iToDate.getDate();

            iIndeliveries = SSFilterFactory.doFilter(iIndeliveries,
                    new SSFilter<SSIndelivery>() {
                public boolean applyFilter(SSIndelivery iIndelivery) {
                    return SSIndeliveryMath.inPeriod(iIndelivery, iDateFrom, iDateTo);
                }
            });
        }

        return iIndeliveries;
    }

    /**
     *
     * @return
     */
    public boolean isDateSelected() {
        return iCheckDate.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isProductSelected() {
        return iCheckProduct.isSelected();
    }

    /**
     *
     * @return
     */
    public Date getDateFrom() {
        return iFromDate.getDate();
    }

    /**
     *
     * @return
     */
    public Date getDateTo() {
        return iToDate.getDate();
    }

    /**
     *
     * @return
     */
    public SSProduct getProduct() {
        return iProduct.getSelected();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.dialog.SSIndeliveryListDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCheckDate=").append(iCheckDate);
        sb.append(", iCheckProduct=").append(iCheckProduct);
        sb.append(", iFromDate=").append(iFromDate);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iProduct=").append(iProduct);
        sb.append(", iToDate=").append(iToDate);
        sb.append('}');
        return sb.toString();
    }
}
