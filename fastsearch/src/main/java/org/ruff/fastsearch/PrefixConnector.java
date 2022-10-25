package org.ruff.fastsearch;

public interface PrefixConnector {

    String getPrefix();

    void match(String match);

}