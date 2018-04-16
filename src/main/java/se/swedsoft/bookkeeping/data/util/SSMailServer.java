package se.swedsoft.bookkeeping.data.util;

import org.fribok.bookkeeping.data.util.ConnectionSecurity;
import se.swedsoft.bookkeeping.util.SSUtil;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Class to store data about a mail server. Immutable.
 *
 * $Id$
 * 
 * @author jensli
 */
public class SSMailServer implements Serializable {
    private final String name;
    private final String username;
    private final String password;

    private final URI address;

    private final boolean isAuth; // Should authorization be used when connecting to the mail server?
    private final ConnectionSecurity connectionSecurity; // Should SSL or STARTTLS be used

    /**
     * Creates a new SSMailServer if arguments are valid, else throws MailServerException
     * with resourceName set to something that can identify the error so that an appropriate
     * error message can be displayed.
     * @param name
     * @param host
     * @param port
     * @param isAuth
     * @param connectionSecurity
     * @param username
     * @param password
     * @return
     * @throws SSMailServerException
     */
    public static SSMailServer makeIfValid(String name, String host,
            int port, boolean isAuth, ConnectionSecurity connectionSecurity,
	    String username, String password)
        throws SSMailServerException {

        if (!SSUtil.isInRage(port, 1, 65563)) {
            onError("mailserver.port_error");
        }

        URI tempAddress = null;

        try {
            tempAddress = new URI(null, null, host, port, null, null, null);
        } catch (URISyntaxException e) {
            onError("mailserver.format_error");
        }

        if (!isValidAddress(tempAddress)) {
            onError("mailserver.parts_error");
        }

        return new SSMailServer(name, tempAddress, isAuth, connectionSecurity, username, password);
    }

    public SSMailServer(String name, URI address, boolean isAuth, ConnectionSecurity connectionSecurity, String username, String password) {

        SSUtil.verifyNotNull("argument to SSMailServer constructor", name, address);

        if (isAuth) {
            SSUtil.verifyNotNull("argument to SSMailServer constructor", username,
                    password);
        }

        this.name = name;
        this.isAuth = isAuth;
        this.connectionSecurity = connectionSecurity;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public static void onError(String msg) throws SSMailServerException {
        throw new SSMailServerException("Error when creating SSMailServer", msg);
    }

    public static boolean isValidAddress(URI a) {

        return a.getHost() != null && !a.isAbsolute() && isNullOrEmpty(a.getPath())
                && isNullOrEmpty(a.getQuery()) && isNullOrEmpty(a.getFragment());
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private static void verifyNotNull(Object o, String msg) {
        if (o == null) {
            throw new NullPointerException(msg + " must not be null");
        }
    }

    private String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public boolean isSSL() {
        return connectionSecurity == ConnectionSecurity.SSL_TLS;
    }

    public boolean isStartTLS() {
        return connectionSecurity == ConnectionSecurity.STARTTLS;
    }

    public boolean isNoConnectionSecurity() {
        return !isSSL() && !isStartTLS();
    }
    
    public ConnectionSecurity getConnectionSecurity() {
	return connectionSecurity;
    }

    public URI getURI() {
        return address;
    }

    static final long serialVersionUID = 2L;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.data.util.SSMailServer");
        sb.append("{address=").append(address);
        sb.append(", isAuth=").append(isAuth);
        sb.append(", connectionSecurity=").append(connectionSecurity);
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
