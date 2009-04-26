package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.SSBookkeeping;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.data.SSStandardText;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSSupplier;

import java.util.Properties;
import java.io.File;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.swing.*;

/**
 * User: Johan Gunnarsson
 * Date: 2007-mar-26
 * Time: 16:35:15
 */
public class SSMail {

    public static boolean sendMail(String pTo, String pSubject, String pFileName) throws Exception {

        if( SSQueryDialog.showDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "mail.send",pTo) != JOptionPane.OK_OPTION) {
            return false;
        }
        String iSMTP = SSDB.getInstance().getCurrentCompany().getSMTP();
        String iFrom = SSDB.getInstance().getCurrentCompany().getEMail();


        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
        props.put("mail.smtp.host", iSMTP);

        // Get session
        Session session = Session.getDefaultInstance(props, null);

        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(iFrom));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(pTo));
        message.setSubject(pSubject);

        // Create the multi-part
        Multipart multipart = new MimeMultipart();

        // Create part one
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        String iMessage = SSDB.getInstance().getCurrentCompany().getStandardText(SSStandardText.Email);
        messageBodyPart.setText(iMessage == null ? "" : iMessage);

        // Add the first part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource("pdftoemail" + File.separator + pFileName);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(pFileName);

        // Add the second part
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        message.setContent(multipart);

        // Send message
        Transport.send(message);
        return true;
    }

    public static boolean isOk(SSCustomer iCustomer) {

        if (SSDB.getInstance().getCurrentCompany().getSMTP() == null || SSDB.getInstance().getCurrentCompany().getSMTP().length() == 0) {
            new SSErrorDialog(SSMainFrame.getInstance(), "mail.nosmtpserver");
            return false;
        }

        if (SSDB.getInstance().getCurrentCompany().getEMail() == null || SSDB.getInstance().getCurrentCompany().getEMail().length() == 0) {
            new SSErrorDialog(SSMainFrame.getInstance(), "mail.nocompanyemailaddress");
            return false;
        }

        if (iCustomer != null) {
            if (iCustomer.getEMail() == null || iCustomer.getEMail().length()==0) {
                new SSErrorDialog(SSMainFrame.getInstance(), "mail.nocustomeremailaddress");
                return false;
            }
        } else {
            new SSErrorDialog(SSMainFrame.getInstance(), "mail.nocustomeremailaddress");
            return false;
        }
        return true;
    }

    public static boolean isOk(SSSupplier iSupplier) {

        if (SSDB.getInstance().getCurrentCompany().getSMTP() == null || SSDB.getInstance().getCurrentCompany().getSMTP().length() == 0) {
            new SSErrorDialog(SSMainFrame.getInstance(), "mail.nosmtpserver");
            return false;
        }

        if (SSDB.getInstance().getCurrentCompany().getEMail() == null || SSDB.getInstance().getCurrentCompany().getEMail().length() == 0) {
            new SSErrorDialog(SSMainFrame.getInstance(), "mail.nocompanyemailaddress");
            return false;
        }

        if (iSupplier != null) {
            if (iSupplier.getEMail() == null || iSupplier.getEMail().length()==0) {
                new SSErrorDialog(SSMainFrame.getInstance(), "mail.nosupplieremailaddress");
                return false;
            }
        } else {
            new SSErrorDialog(SSMainFrame.getInstance(), "mail.nosupplieremailaddress");
            return false;
        }
        return true;
    }
}
