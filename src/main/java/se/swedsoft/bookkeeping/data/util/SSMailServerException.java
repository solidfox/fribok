package se.swedsoft.bookkeeping.data.util;


public class SSMailServerException extends Exception {
    private final String resourceName;
    public SSMailServerException(String s, String resourceName) {
        super(s);
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
