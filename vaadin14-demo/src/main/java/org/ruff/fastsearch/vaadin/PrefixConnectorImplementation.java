package org.ruff.fastsearch.vaadin;

import org.ruff.fastsearch.PrefixConnector;

public class PrefixConnectorImplementation implements PrefixConnector {
    @Override
    public String getPrefix() {
        return "v";
    }

    @Override
    public void match(String match) {
        System.out.println("PrefixMatch: " + match);
    }
}