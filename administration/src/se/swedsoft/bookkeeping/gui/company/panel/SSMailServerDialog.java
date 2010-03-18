package se.swedsoft.bookkeeping.gui.company.panel;

import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.data.util.SSMailServer;
import se.swedsoft.bookkeeping.data.util.SSMailServerException;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;




/**
 * A dialog where the user can enter info about a mail server used to send mail.
 * <p/>
 * When opened it takes a SSMailServer and sets the fields of the dialog, and
 * returns a new SSMailServer with the new information.
 * <p/>
 * Opened from SSCompanyPageAdditional.
 *
 * @author jensli
 *
 * $Id$
 *
 */
public class SSMailServerDialog extends SSDialog {

    private JPanel contentPane;
    private JTextField addressText;
    private JLabel addressLabel;
    private JCheckBox authCheckbox;
    private JLabel userNameLabel;
    private JTextField usernameText;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JLabel portLabel;
    private JTextField portField;
    private SSButtonPanel iButtonPanel;

    private JDialog iParent;

    private SSMailServer iMailServer;

    private boolean shouldSave;

    public SSMailServerDialog(JDialog iParent) {

        super(iParent, SSBundle.getBundle().getString("companypanel.basic.server_dialog_title"));

        this.iParent = iParent;

        setLocationRelativeTo(iParent);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(iButtonPanel.getOkButton());

        iButtonPanel.getOkButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        iButtonPanel.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        authCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                onNewAuthState(e.getStateChange() == ItemEvent.SELECTED);
            }
        });


        this.pack();
    }

    /**
     * Sets the text of the fields in the dialog from the data in server.
     * @param server
     */
    private void loadFieldsFromServer(SSMailServer server) {

        addressText.setText(server.getURI().getHost());
        authCheckbox.setSelected(server.isAuth());
        usernameText.setText(server.getUsername());
        passwordField.setText(SSMail.crypter.decrypt(server.getPassword()));
        portField.setText(Integer.toString(server.getURI().getPort()));

        onNewAuthState(server.isAuth());
    }

    /**
     * Reads the fields of the dialog and constructs a SSMailServer from
     * that, throwing if there was a format error in the fields.
     * @throws se.swedsoft.bookkeeping.data.util.SSMailServerException
     * @return
     */
    private SSMailServer getServerFromFields() throws SSMailServerException {


        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            throw new SSMailServerException("parse error for portField", "mailserver.number_error");
        }


        SSMailServer s = SSMailServer.makeIfValid(
                "NONAME",
                addressText.getText(),
                port,
                authCheckbox.isSelected(),
                usernameText.getText(),
                SSMail.crypter.encrypt(String.valueOf(passwordField.getPassword())));


        return s;
    }

    /**
     * The method for opening the dialog. The data from server will be
     * copied to the text fields of the dialog, a SSMailServer constructed
     * from the edited fields will bve returned.
     * @param server
     * @return
     */
    public SSMailServer showServerQuery(SSMailServer server) {

        if (server != null) {
            loadFieldsFromServer(server);
        }

        iMailServer = server;

        setVisible();

        return iMailServer;
    }

    /**
     * Disables/enables the authorisation components
     * @param isEnabled
     */
    private void onNewAuthState(boolean isEnabled) {
        passwordField.setEnabled(isEnabled);
        passwordLabel.setEnabled(isEnabled);
        usernameText.setEnabled(isEnabled);
        userNameLabel.setEnabled(isEnabled);
    }


    /**
     * Called when the Ok button is clicked. Reads the fields of the dialog, constructs a
     * SSMailServer from them and sets iMailServer with that. Then close the dialog.
     * <p/>
     * If there was an error in the fields, open a dialog to inform about that.
     */
    private void onOK() {

        boolean shouldDiscard = true;

        try {
            iMailServer = getServerFromFields();
        } catch (SSMailServerException exc) {
            shouldDiscard = queryShouldDiscard(SSBundle.getBundle().getString(exc.getResourceName()));
        }

        // Close the dialog, else leave dialog open and let user
        // fix the data
        if (shouldDiscard) {
            closeDialog(JOptionPane.OK_OPTION);
        }

    }

    /**
     * Opends a dialog asking if the user wants to discard the faulty info.
     * @param message
     * @return
     */
    private boolean queryShouldDiscard(String message) {

        int res = JOptionPane.showConfirmDialog(this,
                message + "\n\n" +
                        SSBundle.getBundle().getString("companypanel.basic.server_error_message"),
                SSBundle.getBundle().getString("companypanel.basic.server_error_title"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

        return res == JOptionPane.OK_OPTION;
    }

    /**
     * Closes the dialog without saving
     */
    private void onCancel() {
        closeDialog(JOptionPane.CANCEL_OPTION);
    }

}
