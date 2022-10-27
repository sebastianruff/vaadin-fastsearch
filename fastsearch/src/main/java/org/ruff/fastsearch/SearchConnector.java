package org.ruff.fastsearch;

import java.util.stream.Stream;

public interface SearchConnector extends Connector {

        Stream<Candidate> getCandidateSupplier();

        void match(Candidate match);

        String getIndexName();
}