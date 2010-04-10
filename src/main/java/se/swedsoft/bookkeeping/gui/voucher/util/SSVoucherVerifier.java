package se.swedsoft.bookkeeping.gui.voucher.util;


import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Tests if a voucher is isValid
 * Date: 2006-feb-06
 * Time: 09:27:58
 */
public class SSVoucherVerifier  implements PropertyChangeListener, TableModelListener, ActionListener {

    private static ResourceBundle bundle = SSBundle.getBundle();

    public interface OnUpdate {
        void update(boolean valid, String Error);
    }

    private SSVoucher iVoucher;

    private List<JComponent> iComponents;

    private boolean iValid;

    private String iError;

    private OnUpdate iOnUpdate;

    /**
     *
     * @param pComponents
     */
    public SSVoucherVerifier(JComponent... pComponents) {
        iVoucher = null;
        iComponents = new LinkedList<JComponent>();
        iError = null;
        iOnUpdate = null;
        iValid = false;

        iComponents.addAll(Arrays.asList(pComponents));
    }

    /**
     *
     * @param pVoucher
     * @param pComponents
     */
    public SSVoucherVerifier(SSVoucher pVoucher, JComponent... pComponents) {
        iVoucher = pVoucher;
        iComponents = new LinkedList<JComponent>();
        iError = null;
        iOnUpdate = null;

        iComponents.addAll(Arrays.asList(pComponents));
    }

    /**
     *
     * @param pVoucher
     */
    public void setVoucher(SSVoucher pVoucher) {
        iVoucher = pVoucher;
    }

    /**
     *
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        update();
    }

    /**
     *
     * @param evt
     */
    public void propertyChange(PropertyChangeEvent evt) {
        update();
    }

    /**
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        update();
    }

    /**
     *
     * @return If the voucher is isValid
     */
    private boolean validate() {
        iError = null;

        String description = iVoucher.getDescription();

        if (description == null || description.length() == 0) {
            // Verifikationen saknar beskrivning.
            iError = bundle.getString("voucherframe.error.1");
            return false;
        }

        if (iVoucher.getRows().isEmpty()) {
            // Verifikationen saknar rader.
            iError = bundle.getString("voucherframe.error.2");
            return false;
        }

        for (SSVoucherRow iRow : iVoucher.getRows()) {

            if (iRow.isCrossed()) {
                if (iRow.getEditedSignature() == null) {
                    // Tillagd rad saknar signatur.
                    iError = bundle.getString("voucherframe.error.7");
                    return false;
                }
                continue;
            }

            if (iRow.getAccount() == null) {
                // Raden saknar konto.
                iError = bundle.getString("voucherframe.error.3");
                return false;
            }

            if (iRow.getDebet() == null && iRow.getCredit() == null/* || (iRow.getDebet() != null && iRow.getDebet().signum() == 0) || (iRow.getCredit() != null && iRow.getCredit().signum() == 0)*/) {
                // Raden saknar debit eller credit.
                iError = bundle.getString("voucherframe.error.4");
                return false;
            }

            if (iRow.getAccount().isProjectRequired() && iRow.getProject() == null) {
                iError = bundle.getString("voucherframe.error.9");
                return false;
            }

            if (iRow.getAccount().isResultUnitRequired() && iRow.getResultUnit() == null) {
                iError = bundle.getString("voucherframe.error.10");
                return false;
            }

            if (iRow.isAdded() && iRow.getEditedSignature() == null) {
                // Tillagd rad saknar signatur.
                iError = bundle.getString("voucherframe.error.5");
                return false;
            }
        }
        BigDecimal iDebetSum = SSVoucherMath.getDebetSum(iVoucher);
        BigDecimal iCreditSum = SSVoucherMath.getCreditSum(iVoucher);

        if (iDebetSum.subtract(iCreditSum).setScale(2, RoundingMode.HALF_UP).signum() != 0) {
            // Verifikationens differans är inte noll.
            iError = bundle.getString("voucherframe.error.6");
            return false;
        }

        if (iDebetSum.setScale(2, RoundingMode.HALF_UP).signum() == 0) {
            // Omslutningen är noll
            for (SSVoucherRow iRow : iVoucher.getRows()) {
                if (!iRow.isCrossed()) {
                    iError = bundle.getString("voucherframe.error.8");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     *
     */
    public void update() {
        if (iVoucher == null) {
            return;
        }

        iValid = validate();

        if (iOnUpdate != null) {
            iOnUpdate.update(iValid, iError);
        }

        setComponentsEnabled(iValid);
    }

    /**
     *
     * @param enabled
     */
    private void setComponentsEnabled(boolean enabled) {
        for (JComponent iComponent: iComponents) {
            iComponent.setEnabled(enabled);
        }

    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        return iValid;
    }

    /**
     *
     * @return
     */
    public String getError() {
        return iError;
    }

    /**
     *
     * @param pOnUpdate
     */
    public void setOnUpdate(OnUpdate pOnUpdate) {
        iOnUpdate = pOnUpdate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherVerifier");
        sb.append("{iComponents=").append(iComponents);
        sb.append(", iError='").append(iError).append('\'');
        sb.append(", iOnUpdate=").append(iOnUpdate);
        sb.append(", iValid=").append(iValid);
        sb.append(", iVoucher=").append(iVoucher);
        sb.append('}');
        return sb.toString();
    }
}
