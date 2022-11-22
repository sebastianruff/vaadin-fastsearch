package org.ruff.fastsearch.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.ruff.fastsearch.Candidate;
import org.ruff.fastsearch.SearchConnector;

public class SearchConnectorImplementation implements SearchConnector {

    @Override
    public void match(Candidate match) {
	System.out.println("SearchMatch: " + match.getContent());
    }

    @Override
    public Stream<Candidate> getCandidateSupplier() {
	List<Candidate> list = new ArrayList<>();
	for (Locale locale : Locale.getAvailableLocales()) {
	    Candidate e = new Candidate(locale.getCountry(), locale.getDisplayCountry());
	    list.add(e);
	}
	return list.stream();
    }

    @Override
    public String getIndexName() {
	return "nations";
    }
}