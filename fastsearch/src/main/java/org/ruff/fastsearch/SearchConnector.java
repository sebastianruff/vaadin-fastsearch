package org.ruff.fastsearch;

import java.util.stream.Stream;

public interface SearchConnector {

    Stream<Candidate> getCandidateSupplier();

    void match(Candidate match);

    String getIndexName();
}