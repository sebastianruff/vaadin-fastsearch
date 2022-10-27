package org.ruff.fastsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

        private static final Log LOG = LogFactory.getLog(Fastsearch.class);

        private static final long serialVersionUID = 2088300391648592896L;

        private SearchConnector searchConnector;
        private Consumer<String> fallbackEnterConnector;
        private List<PrefixConnector> prefixConnectors = new ArrayList<>();
        private List<RegexConnector> regexConnectors = new ArrayList<>();

        private transient JreJsonFactory jsonFactory;

        private Optional<Candidate> match;

        public Fastsearch() {
                jsonFactory = new JreJsonFactory();
        }

        @Override
        protected void onAttach(AttachEvent attachEvent) {
                super.onAttach(attachEvent);
        }

        @ClientCallable
        private void clientMatch(String id) {
                match = searchConnector.getCandidateSupplier().filter(
                                c -> c.getId() != null ? c.getId().equals(id) : id == String.valueOf(c.hashCode()))
                                .findFirst();
                if (match.isPresent()) {
                        searchConnector.match(match.get());
                }
        }

        @ClientCallable
        private void prefixMatch(String term) {
                prefixConnectors.stream().filter(prefixConnector -> term.startsWith(prefixConnector.getPrefix()))
                                .findFirst().ifPresent(prefixConnector -> {
                                        prefixConnector.match(term.replaceFirst(prefixConnector.getPrefix(), ""));
                                });
        }

        @ClientCallable
        private void regexMatch(String term) {
                regexConnectors.stream().filter(regexConnector -> term.matches(regexConnector.getRegEx()))
                                .findFirst().ifPresent(regexConnector -> {
                                        String termForMatching = term;
                                        if (regexConnector.ignoreSpaces()) {
                                                termForMatching = termForMatching.replaceAll("\\s", "");
                                        }
                                        for (String charToIgnore : regexConnector.ignoreCharsForMatching()) {
                                                termForMatching = StringUtils
                                                                .remove(termForMatching, charToIgnore);
                                        }
                                        regexConnector.match(termForMatching.toString());
                                });
        }

        @ClientCallable
        private void enter(String term) {
                fallbackEnterConnector.accept(term);
        }

        public void setFallbackEnterConnector(Consumer<String> fallbackEnterConnector) {
                this.fallbackEnterConnector = fallbackEnterConnector;
        }

        public void addClientCachedSearchConnector(SearchConnector searchConnector) {
                this.searchConnector = searchConnector;
                JsonArray array = jsonFactory.createArray();
                if (LOG.isDebugEnabled()) {
                        LOG.debug("SearchConnector Entries: ");
                }
                searchConnector.getCandidateSupplier().forEach(c -> {
                        JsonObject object = jsonFactory.createObject();
                        String id = c.getId() != null ? c.getId() : String.valueOf(c.hashCode());
                        object.put(Candidate.ID, id);
                        object.put(Candidate.CONTENT, c.getContent());
                        if (searchConnector.getIndexName() != null) {
                                object.put(Candidate.TAG, searchConnector.getIndexName());
                        }
                        array.set(array.length(), object);
                        if (LOG.isDebugEnabled()) {
                                LOG.debug(Candidate.ID + ": " + id + " " + Candidate.CONTENT + ": " + c.getContent()
                                                + " " + Candidate.TAG
                                                + ": " + searchConnector.getIndexName());
                        }
                });
                getElement().setPropertyJson("$candidates", array);
        }

        public void addPrefixConnector(PrefixConnector prefixConnector) {
                prefixConnectors.add(prefixConnector);
                JsonArray array = jsonFactory.createArray();
                prefixConnectors.forEach(c -> {
                        array.set(array.length(), c.getPrefix());
                });
                getElement().setPropertyJson("$prefixes", array);
        }

        public void addRegexConnectorConnector(RegexConnector regexConnector) {
                regexConnectors.add(regexConnector);
                JsonArray array = jsonFactory.createArray();
                regexConnectors.forEach(c -> {
                        array.set(array.length(), c.getRegEx());
                });
                getElement().setPropertyJson("$regexes", array);
        }

        public void addConnector(Connector connector) {
                if (connector instanceof SearchConnector) {
                        addClientCachedSearchConnector((SearchConnector) connector);
                } else if (connector instanceof PrefixConnector) {
                        addPrefixConnector((PrefixConnector) connector);
                } else if (connector instanceof RegexConnector) {
                        addRegexConnectorConnector((RegexConnector) connector);
                }
        }

}