package org.ruff.fastsearch;

public interface PrefixConnector extends Connector {

        String getPrefix();

        void match(String query);

}