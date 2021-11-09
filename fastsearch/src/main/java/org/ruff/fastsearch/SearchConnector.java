package org.ruff.fastsearch;

import java.util.stream.Stream;

public interface SearchConnector {

	public Stream<Candidate> getCandidateSupplier();

	public void match(Candidate match);

	public String getIndexName();
}
