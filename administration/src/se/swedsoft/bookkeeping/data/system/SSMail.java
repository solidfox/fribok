package se.swedsoft.bookkeeping.data.system;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSStandardText;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.util.SSMailMessage;
import se.swedsoft.bookkeeping.data.util.SSMailServer;
import se.swedsoft.bookkeeping.data.util.SymetricCrypter;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.util.SSUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.io.File;
import java.util.Properties;

/**
 * Utility methods for sending mail
 * 
 * 
 * User: Johan Gunnarsson
 * Date: 2007-mar-26
 * Time: 16:35:15
 */
public class SSMail {
    
    // Used to encrypt password
    public static SymetricCrypter crypter = new SymetricCrypter( new byte[] { 0x4f,0x53,-0x71,-0x28,0x0d,0x21,0x1c,-0x1c} );

    // Change this to get detailed debug info form JavaMail
    private static final boolean SHOULD_DEBUG_PRINT = false; 

    // This dir where to look for pdf to send as attachments. This is hardcoded thouhg out the
    // application, so dont change.
    private static final String PDF_FILE_DIR =  "pdftoemail" + File.separator;
    
    /**
     * Asks if the user really wants to send a mail, gets data from db, and calls 
     * doSendMail to send it
     * @param pTo
     * @param pSubject
     * @param pFileName
     * @return
     * @throws AddressException
     * @throws MessagingException
     */
    public static boolean sendMail(String pTo, String pSubject, String pFileName) 
        throws AddressException, MessagingException  {
        
        SSUtil.verifyNotNull("Arguments to sendMail can not be null", pTo, pSubject, pFileName);
        
        if (SSQueryDialog.showDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "mail.send", pTo) 
                != JOptionPane.OK_OPTION) {
            return false;
        }
        
        SSNewCompany company = SSDB.getInstance().getCurrentCompany();
        
        
        SSMailMessage message = new SSMailMessage(
                company.getEMail(), 
                pTo, 
                pSubject,
                company.getStandardText(SSStandardText.Email), 
                PDF_FILE_DIR + pFileName);
        
        // Send message
        MimeMessage mimeMessage = makeMessage(company.getMailServer(), message);
        Transport.send(mimeMessage);
        
        return true;
    }
    
    /**
     * Makes a MimeMessage ready to be send from the arguments
     * @param server
     * @param mail
     * @return
     * @throws MessagingException
     */
    public static MimeMessage makeMessage(SSMailServer server, SSMailMessage mail) 
        throws MessagingException {
        
        SSUtil.verifyNotNull("server" , server);
        SSUtil.verifyNotNull("Email message fields", 
                mail.getFrom(), mail.getTo(), mail.getSubject());
        
        Session session = makeSession(server);
        MimeMessage message = makeMime(mail, session);
        Multipart multipart = makeMultipart(mail);
        
        // Put parts in message
        message.setContent(multipart);
        
        return message;
    }

    private static MimeMessage makeMime(SSMailMessage mail, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session); 
        message.setFrom(new InternetAddress(mail.getFrom()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
        message.setSubject(mail.getSubject());
        return message;
    }

    /**
     * Makes a Multipart from the data in the argument. If getFileName returns non-null,
     * that file is send as an attachment.
     * @param mail
     * @return
     * @throws MessagingException
     */
    private static Multipart makeMultipart(SSMailMessage mail) throws MessagingException {
        // Create the multi-part
        Multipart multipart = new MimeMultipart();
        
        // Create part one
        BodyPart messageBodyPart = new MimeBodyPart();
        
        // Fill the message
        messageBodyPart.setText(SSUtil.convertNullToEmpty(mail.getBodyText()));
        
        // Add the first part
        multipart.addBodyPart(messageBodyPart);
        
        // Part two is attachment
        if (mail.getFileName() != null) {
        
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(mail.getFileName());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(mail.getFileName());
            
            // Add the second part
            multipart.addBodyPart(messageBodyPart);
        }
        
        return multipart;
    }

    /**
     * Makes a Session from the data in a SSMailServer
     * @param server
     * @return
     */
    private static Session makeSession(final SSMailServer server) {
        
        // Get system properties
        Properties props = new Properties();
        System.getProperties();
        
        // Setup mail server
        props.put("mail.smtp.host", server.getURI().getHost());
        props.put("mail.smtp.port", Integer.toString(server.getURI().getPort()));
        props.put("mail.smtp.auth", Boolean.toString(server.isAuth()));
        
        Authenticator auth = null;
        
        // Create Authenticator if it should be used
        if (server.isAuth()) {
            auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(server.getUsername(), SSMail.crypter.decrypt(server.getPassword()));
                }
            };
        }
        
        // Get session
        Session session = Session.getInstance(props, auth);
        
        session.setDebug(SHOULD_DEBUG_PRINT);
        
        return session;
    }

    /**
     * Throws an MailValidationException with an resource name that can be used to
     * get an error message from a resource file.
     * @param message
     * @param resourceName
     * @throws MailValidationException
     */
    public static void onError(String message, String resourceName) throws MailValidationException {
        throw new MailValidationException(message, resourceName);
    }

    /**
     * Checks if company and o has mail addresses and servers. If not, opens a error dialog
     * and returns false.
     * @param company
     * @param o can be either an SSSupplier or an SScustomer
     * @param resourceName
     * @return
     */
    private static boolean isOk(SSNewCompany company, Object o, String resourceName) {
        
        try {
            // Check company address and server
            if (company.getMailServer() == null) {
                onError("invalid mail server at current company", "mail.nosmtpserver"); 
            }
            
            if (SSUtil.isNullOrEmpty(company.getEMail())) {
                onError("invalid mail address at current company", "mail.nocompanyemailaddress");
            }

            // Check supplier or customer address
            if (o == null) {
                onError("Argument object is null", resourceName);
            }
                
            String s;
            
            if (o instanceof SSCustomer) {
                s = ((SSCustomer) o).getEMail();
            } else if ( o instanceof SSSupplier) {
                s = ((SSSupplier) o).getEMail();
            } else {
                throw new ClassCastException("o is of wrong type");
            }
                
            if (SSUtil.isNullOrEmpty(s)) {
                onError("No mail address",  resourceName);
            }
            
        } catch (MailValidationException e) {
            // Some nessesery field wasnt set, open an error dialog to notify the user
            new SSErrorDialog(SSMainFrame.getInstance(), e.getResourceName());
            return false;
        }

        return true;
    }
    
    /**
     * Checks if the current company and iCustomer has mail addresses and servers. If not, opens a error dialog
     * and returns false.
     * @param iCustomer
     * @return
     */
    public static boolean isOk(SSCustomer iCustomer) {
        return isOk(SSDB.getInstance().getCurrentCompany(), iCustomer, "mail.nocustomeremailaddress");
    }
  
    /**
     * Checks if the current company and iSupplier has mail addresses and servers. If not, opens a error dialog
     * and returns false.
     * @param iSupplier
     * @return
     */
    public static boolean isOk(SSSupplier iSupplier) {
        return isOk(SSDB.getInstance().getCurrentCompany(), iSupplier, "mail.nosupplieremailaddress");
    }
    
    private static class MailValidationException extends Exception
    {
        private final String resourceName;
        
        public MailValidationException(String message, String resourceName) {
            super(message);
            this.resourceName = resourceName;
        }
        
        public String getResourceName() {
            return resourceName;
        }
    }
}
