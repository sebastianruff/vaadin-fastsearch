package org.ruff.fastsearch.vaadin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.ruff.fastsearch.Fastsearch;

/**
 * The main view contains a button and a click listener.
 */
@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 7096008159730845939L;

	public MainView() {
		Fastsearch fastsearch = new Fastsearch();
		add(fastsearch);

		fastsearch.addClientCachedSearchConnector(new SearchConnectorImplementation());

		fastsearch.addPrefixConnector(new PrefixConnectorImplementation());

		fastsearch.setFallbackEnterConnector(value -> System.out.println("Fallback Connector: " + value));
	}
}
