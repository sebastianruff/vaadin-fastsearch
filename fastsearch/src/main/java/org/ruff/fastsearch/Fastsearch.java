package org.ruff.fastsearch;

import java.util.function.Consumer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;

@Tag("vaadin-fastsearch")
@JsModule("./flexsearch.ts")
@JsModule("./vaadin-fastsearch.ts")
@CssImport("./vaadin-fastsearch.css")
@NpmPackage(value = "flexsearch", version = "0.7.31")
public class Fastsearch extends LitTemplate implements HasSize, Focusable<Fastsearch> {

        private static final long serialVersionUID = 2088300391648592896L;

        private SearchConnector connector;
        private transient JreJsonFactory jsonFactory;

        private PrefixConnector keywordConnector;

        private Candidate match;

        private Consumer<String> fallbackEnterConnector;

        public Fastsearch() {
                jsonFactory = new JreJsonFactory();
        }

        @Override
        protected void onAttach(AttachEvent attachEvent) {
                super.onAttach(attachEvent);
        }

        @ClientCallable
        private void clientMatch(String id) {
                match = connector.getCandidateSupplier().filter(
                                c -> c.getId() != null ? c.getId().equals(id) : id == String.valueOf(c.hashCode()))
                                .findFirst().get();
                if (match != null) {
                        connector.match(match);
                }
        }

        @ClientCallable
        private void enter(String id, String term) {
                if ("".equals(id)) {
                        if (term.startsWith(keywordConnector.getPrefix())) {
                                keywordConnector.match(term.replaceFirst(keywordConnector.getPrefix(), ""));
                        } else {
                                fallbackEnterConnector.accept(term);
                        }
                }
        }

        public void setFallbackEnterConnector(Consumer<String> fallbackEnterConnector) {
                this.fallbackEnterConnector = fallbackEnterConnector;
        }

        public void addClientCachedSearchConnector(SearchConnector connector) {
                this.connector = connector;
                JsonArray array = jsonFactory.createArray();
                connector.getCandidateSupplier().forEach(c -> {
                        JsonObject object = jsonFactory.createObject();
                        object.put(Candidate.ID, c.getId() != null ? c.getId() : c.hashCode());
                        object.put(Candidate.CONTENT, c.getContent());
                        object.put(Candidate.TAG, connector.getIndexName());
                        array.set(array.length(), object);
                });
                getElement().setPropertyJson("$candidates", array);
        }

        public void addPrefixConnector(PrefixConnector c) {
                this.keywordConnector = c;
                getElement().setProperty("$keyword", c.getPrefix());
        }
}