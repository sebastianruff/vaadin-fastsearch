package org.ruff.fastsearch.vaadin;

import org.ruff.fastsearch.PrefixConnector;

public class PrefixConnectorImplementation implements PrefixConnector {

    private String prefix;

    public PrefixConnectorImplementation(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void match(String match) {
        System.out.println("PrefixMatch for " + prefix + ": " + match);
    }
}