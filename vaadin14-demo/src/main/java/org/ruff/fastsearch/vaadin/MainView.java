package org.ruff.fastsearch.vaadin;

import org.ruff.fastsearch.Fastsearch;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

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

	fastsearch.addPrefixConnector(new PrefixConnectorImplementation("v"));
	fastsearch.addPrefixConnector(new PrefixConnectorImplementation("a"));

	fastsearch.setFallbackEnterConnector(value -> System.out.println("Fallback Connector: " + value));
	add(new Label(
		"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."));
    }
}
