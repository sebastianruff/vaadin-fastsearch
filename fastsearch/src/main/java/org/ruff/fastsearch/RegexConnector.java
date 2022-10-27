package org.ruff.fastsearch;

import java.util.Collections;
import java.util.List;

public interface RegexConnector extends Connector {

        String getRegEx();

        void match(String query);

        String matchText(String query);

        default List<String> ignoreCharsForMatching() {
                return Collections.emptyList();
        }

        default boolean ignoreSpaces() {
                return false;
        }
}