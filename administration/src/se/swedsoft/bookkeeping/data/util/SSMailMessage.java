package se.swedsoft.bookkeeping.data.util;

/**
 * Contains the data needed to send a mail. Immutable.
 * 
 * @author jensli
 */
public class SSMailMessage
{
    private final String to, subject, fileName, from, bodyText;

    /**
     * @param from
     * @param to
     * @param subject
     * @param bodyText
     * @param fileName path to the attatchment file, can be null
     */
    public SSMailMessage(String from, String to, String subject, String bodyText, String fileName) {
        
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.bodyText = bodyText;
        this.fileName = fileName;
    }

    public String getTo() {
        return to;
    }

    public String getBodyText() {
        return bodyText;
    }

    public String getSubject() {
        return subject;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFrom() {
        return from;
    }
}