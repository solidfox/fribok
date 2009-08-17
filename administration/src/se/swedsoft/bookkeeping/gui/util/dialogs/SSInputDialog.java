package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.util.components.SSImagePanel;

import javax.swing.*;
import java.awt.event.*;

public class SSInputDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField iIpAddressTextField;
    private SSImagePanel iImage;
    private static String iAddress;


    public SSInputDialog() {
        setLocation(400,300);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Välj server");
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        iAddress = iIpAddressTextField.getText();
        dispose();
    }

    private void onCancel() {
        iAddress = null;
        dispose();
    }

    public static String showDialog() {
        SSInputDialog dialog = new SSInputDialog();
        dialog.pack();
        dialog.setVisible(true);
        return iAddress;
    }
}
