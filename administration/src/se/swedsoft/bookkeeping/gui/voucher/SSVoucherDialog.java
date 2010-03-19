package se.swedsoft.bookkeeping.gui.voucher;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.SSVoucherTemplate;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.voucher.dialogs.SSAddAccountDialog;
import se.swedsoft.bookkeeping.gui.voucher.dialogs.SSCopyReversedVoucherDialog;
import se.swedsoft.bookkeeping.gui.voucher.panel.SSVoucherPanel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-nov-03
 * Time: 15:48:37
 */
public class SSVoucherDialog {
    private SSVoucherDialog() {
    }

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("voucherframe.new.title"));
        final SSVoucherPanel iPanel  = new SSVoucherPanel(iDialog);

      //iPanel.setModel( new SSVoucherRowTableModelOld( false, false ));
        iPanel.setVoucher( new SSVoucher(), false,false );
        iPanel.setMarkRowButtonVisible  (false);
        iPanel.setDeleteRowButtonVisible(true);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iPanel.getVoucher();

                if(!iPanel.getDate().isInCurrentAccountYear())
                {
                    new SSErrorDialog(new JFrame(), "voucherpanel.badyear");
                    iPanel.setVoucher(iVoucher,false,false);
                    return;
                }

                SSDB.getInstance().addVoucher(iVoucher,false);

                if(iPanel.isStoreAsTemplate()){
                    SSDB.getInstance().addVoucherTemplate( new SSVoucherTemplate(iVoucher));
                }

                if(pModel != null) pModel.fireTableDataChanged();

                if(iPanel.doReopen()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    iPanel.setVoucher(new SSVoucher(), false,false );
                    return;
                }
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });

        iPanel.addAddAccountAction(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SSAddAccountDialog.showDialog(iMainFrame);
                iPanel.updateAccounts();
            }
        });

        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "voucherframe.saveonclose") != JOptionPane.OK_OPTION ){
                    SSPostLock.removeLock(lockString);
                    return;
                }
                if(! iPanel.isValid()){
                    SSPostLock.removeLock(lockString);
                    return;
                }
                if(!iPanel.getDate().isInCurrentAccountYear())
                {
                    new SSErrorDialog(new JFrame(), "voucherpanel.badyear");
                    SSPostLock.removeLock(lockString);
                    return;
                }
                SSVoucher iVoucher = iPanel.getVoucher();

                SSDB.getInstance().addVoucher(iVoucher,false);

                if(pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }


    /**
     *
     * @param iMainFrame
     * @param iVoucher
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSVoucher iVoucher, final AbstractTableModel pModel) {
        final String lockString = "voucher"+iVoucher.getNumber()+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucherframe.voucheropen", iVoucher.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("voucherframe.edit.title"));
        final SSVoucherPanel iPanel  = new SSVoucherPanel(iDialog);

        //iPanel.setModel( new SSVoucherRowTableModelOld( true, false ));
        iPanel.setMarkRowButtonVisible  (true);
        iPanel.setDeleteRowButtonVisible(true);
        iPanel.setVoucher( new SSVoucher(iVoucher) , true,false );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iPanel.getVoucher ();

                if(!iPanel.getDate().isInCurrentAccountYear())
                {
                    new SSErrorDialog(new JFrame(), "voucherpanel.badyear");
                    iPanel.setVoucher(iVoucher,true,true);
                    return;
                }

                SSDB.getInstance().updateVoucher(iVoucher);

                if(iPanel.isStoreAsTemplate()){
                    SSDB.getInstance().addVoucherTemplate( new SSVoucherTemplate(iVoucher));
                }

                if(pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();

                if(iPanel.doReopen()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    newDialog(iMainFrame, pModel);
                }
            }
        });

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });

        iPanel.addAddAccountAction(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SSAddAccountDialog.showDialog(iMainFrame);
                iPanel.updateAccounts();
            }
        });

        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if( ! iPanel.isValid() || SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "voucherframe.saveonclose") != JOptionPane.OK_OPTION ) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                if(!iPanel.getDate().isInCurrentAccountYear()){
                    new SSErrorDialog(new JFrame(), "voucherpanel.badyear");
                    SSPostLock.removeLock(lockString);
                    return;
                }
                SSVoucher iVoucher = iPanel.getVoucher ();

                SSDB.getInstance().updateVoucher(iVoucher);
                SSPostLock.removeLock(lockString);

            }
        });
        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param iVoucher
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSVoucher iVoucher, final AbstractTableModel pModel) {
        final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "voucheriscreated");
            return;
        }
        final String lockString2 = "voucher"+iVoucher.getNumber()+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
        if (!SSPostLock.applyLock(lockString2)) {
            new SSErrorDialog(iMainFrame, "voucherframe.voucheropen", iVoucher.getNumber());
            SSPostLock.removeLock(lockString);
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("voucherframe.copy.title"));
        final SSVoucherPanel iPanel  = new SSVoucherPanel(iDialog);


        if(iVoucher.getCorrectedBy() != null){
            SSInformationDialog.showDialog(iMainFrame, "voucherframe.alreadyedited", iVoucher.getNumber());
            SSPostLock.removeLock(lockString);
            SSPostLock.removeLock(lockString2);
            return;
        }
        Boolean iCopyReverse = SSCopyReversedVoucherDialog.showDialog(iMainFrame, iVoucher);

        if(iCopyReverse == null) {
            SSPostLock.removeLock(lockString);
            SSPostLock.removeLock(lockString2);
            return;
        }

        SSVoucher iNew = new SSVoucher(iVoucher);
        iNew.doAutoIncrecement();
        if(iCopyReverse) {
            SSVoucherMath.copyRows(iVoucher, iNew, iCopyReverse);
        } else{
            iNew.setVoucherRows( new LinkedList<SSVoucherRow>());
        }

        iNew.setDate( SSVoucherMath.getNextVoucherDate() );
        iNew.setDescription(  String.format(SSBundle.getBundle().getString("voucherframe.correctsdescription"), iVoucher.getNumber(), iVoucher.getDescription() ));
        iNew.setCorrects(iVoucher);

        //iPanel.setModel( new SSVoucherRowTableModelOld( false, false ));
        iPanel.setMarkRowButtonVisible  (false);
        iPanel.setDeleteRowButtonVisible(true);
        iPanel.setVoucher( iNew, false,false );

        final SSVoucher iOriginal = iVoucher;

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iPanel.getVoucher();

                if(!iPanel.getDate().isInCurrentAccountYear())
                {
                    new SSErrorDialog(new JFrame(), "voucherpanel.badyear");
                    iPanel.setVoucher(iVoucher,false,false);
                    return;
                }

                SSDB.getInstance().addVoucher(iVoucher,false);

                if(iPanel.isStoreAsTemplate()){
                    SSDB.getInstance().addVoucherTemplate( new SSVoucherTemplate(iVoucher));
                }

                iOriginal.setCorrectedBy(iVoucher);

                SSDB.getInstance().updateVoucher(iOriginal);

                if(pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
                SSPostLock.removeLock(lockString2);
                iDialog.closeDialog();

                if(iPanel.doReopen()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    newDialog(iMainFrame, pModel);
                }
            }
        });

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                SSPostLock.removeLock(lockString2);
                iDialog.closeDialog();
            }
        });

        iPanel.addAddAccountAction(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SSAddAccountDialog.showDialog(iMainFrame);
                iPanel.updateAccounts();
            }
        });


        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if( ! iPanel.isValid() || SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "voucherframe.saveonclose") != JOptionPane.OK_OPTION ) {
                    SSPostLock.removeLock(lockString);
                    SSPostLock.removeLock(lockString2);
                    return;
                }

                if(!iPanel.getDate().isInCurrentAccountYear())
                {
                    new SSErrorDialog(new JFrame(), "voucherpanel.badyear");
                    SSPostLock.removeLock(lockString);
                    SSPostLock.removeLock(lockString2);
                    return;
                }
                SSVoucher iVoucher = iPanel.getVoucher();

                SSDB.getInstance().addVoucher(iVoucher,false);

                iOriginal.setCorrectedBy(iVoucher);
                SSDB.getInstance().updateVoucher(iVoucher);

                SSPostLock.removeLock(lockString);
                SSPostLock.removeLock(lockString2);
                if(pModel != null) pModel.fireTableDataChanged();
            }
        });
        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }
}



