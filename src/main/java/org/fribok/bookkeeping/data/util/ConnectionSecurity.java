package org.fribok.bookkeeping.data.util;

public enum ConnectionSecurity {
    NONE (0, ""),
    SSL_TLS (1, "SSL/TLS"),
    STARTTLS (2, "STARTTLS");
    
    private int index;
    private String name;
    ConnectionSecurity(int index, String name) { 
	this.index = index;
	this.name = name;
    }

    public int getIndex() {
	return index;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return getName();
    }
}
