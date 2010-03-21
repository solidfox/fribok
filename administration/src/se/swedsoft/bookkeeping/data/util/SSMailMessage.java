package se.swedsoft.bookkeeping.data.util;

/**
 * Contains the data needed to send a mail. Immutable.
 * 
 * @author jensli
 */
public class SSMailMessage
{
    private final String to;
    private final String subject;
    private final String fileName;
    private final String from;
    private final String bodyText;

    /**
     * @param from
     * @param to
     * @param subject
     * @param bodyText
     * @param fileName path to the attachment file, can be null
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.util.SSMailMessage");
        sb.append("{bodyText='").append(bodyText).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", from='").append(from).append('\'');
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", to='").append(to).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
