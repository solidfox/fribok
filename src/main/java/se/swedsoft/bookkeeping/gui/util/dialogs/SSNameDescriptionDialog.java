package se.swedsoft.bookkeeping.gui.util.dialogs;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * User: Andreas Lago
 * Date: 2006-mar-23
 * Time: 14:25:51
 */
public class SSNameDescriptionDialog extends SSDialog {

    private JPanel iPanel;

    private JButton iOkButton;

    private JButton iCancelButton;

    private JTextField iDescription;

    private JTextField iName;

    private int iModalResult;

    /**
     * @param iFrame
     * @param title
     */
    public SSNameDescriptionDialog(JFrame iFrame, String title) {
        super(iFrame, title);
        add(iPanel, BorderLayout.CENTER);

        iModalResult = JOptionPane.CANCEL_OPTION;
        iOkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iModalResult = JOptionPane.OK_OPTION;

                closeDialog();
            }
        });

        iCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iModalResult = JOptionPane.CANCEL_OPTION;
                closeDialog();
            }
        });
        pack();
        setLocationRelativeTo(iFrame);
    }

    /**
     * @param iDialog
     * @param title
     */
    public SSNameDescriptionDialog(JDialog iDialog, String title) {
        super(iDialog, title);
        add(iPanel, BorderLayout.CENTER);

        iModalResult = JOptionPane.CANCEL_OPTION;
        iOkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iModalResult = JOptionPane.OK_OPTION;

                closeDialog();
            }
        });

        iCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iModalResult = JOptionPane.CANCEL_OPTION;
                closeDialog();
            }
        });
        pack();
        setLocationRelativeTo(iDialog);
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return iName.getText();
    }

    /**
     *
     * @param iName
     */
    @Override
    public void setName(String iName) {
        this.iName.setText(iName);
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return iDescription.getText();
    }

    /**
     *
     * @param iDescription
     */
    public void setDescription(String iDescription) {
        this.iDescription.setText(iDescription);
    }

    /**
     *
     * @return
     */
    @Override
    public int showDialog() {
        setVisible(true);
        return iModalResult;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.dialogs.SSNameDescriptionDialog");
        sb.append("{iCancelButton=").append(iCancelButton);
        sb.append(", iDescription=").append(iDescription);
        sb.append(", iModalResult=").append(iModalResult);
        sb.append(", iName=").append(iName);
        sb.append(", iOkButton=").append(iOkButton);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
