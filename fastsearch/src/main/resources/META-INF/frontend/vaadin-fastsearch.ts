// vaadin-fastsearch.ts
import { customElement, html, LitElement, property, query } from 'lit-element';
import '@vaadin/vaadin-text-field';
import { TextFieldElement } from "@vaadin/vaadin-text-field";
import './flexsearch';
import flexsearch from "flexsearch"
import './vaadin-fastsearch-results';
import type { FastsearchResults } from './vaadin-fastsearch-results';
import { Candidate } from './candidate';


@customElement('vaadin-fastsearch')
export class Fastsearch extends LitElement {

    $server?: FastsearchServerInterface;

    @property({ type: Array })
    $candidates: Array<Candidate> = [];

    @property({ type: Array })
    $results: Array<Candidate> = [];

    @property({ type: Array })
    $prefixes: Array<string> = [];

    @property({ type: Array })
    $regexes: Array<string> = [];

    @property({ type: String })
    $keywordFormatted: string = "";

    @property({ type: Boolean })
    $opened: boolean = false;
    /* 
        @query("#text-field")
        private textfield?: TextFieldElement;
    */
    @query("#result-overlay")
    $resultOverlay?: FastsearchResults;

    /*     @property({ attribute: false })
        $resultsMap: Map<any, FastsearchResults> = new Map(); */

    render() {
        return html`
            <vaadin-text-field id="text-field" @keyup="${this.keyup}"></vaadin-text-field>
            <vaadin-fastsearch-results id="result-overlay" opened="{{opened}}" theme$="[[theme]]">
            </vaadin-fastsearch-results>
            <div>
                <p>${this.$keywordFormatted}</p>
            </div>
`}

    $index = new flexsearch.Document({
        document: {
            id: "id",
            index: ["content"],
            tag: "tag"
        },
        tokenize: 'full'
    });

    updated(changedProperties: any) {
        if (changedProperties.has('$candidates')) {
            this.$candidates?.forEach(candidate => {
                this.$index.add(candidate.id, candidate, false);
            });
        } else if (changedProperties.has('$opened')) {
            console.debug("opened:  " + this.$opened);
        }
    }



    keyup(e: KeyboardEvent) {
        const term: string = (<TextFieldElement>e.currentTarget).value;
        console.debug("Typing: " + term);
        this.search(term)
        if (e.key === 'Enter') {
            if (this.isPrefixAction(term)) {
                this.$server?.prefixMatch(term);
            } else if (this.isRegexAction(term)) {
                this.$server?.regexMatch(term);
            } else if (this.$results.length > 0) {
                this.$server?.clientMatch(this.$results[0].id);
            } else if (term.length > 0) {
                this.$server?.enter(term);
            }
        }
    }
    isRegexAction(term: string) {
        let regexMatch = this.$regexes.find(pattern => term.match(pattern));
        if (regexMatch != null) {
            this.$keywordFormatted = "Suche nach " + term;
            return true;
        }
        return false;
    }

    isPrefixAction(term: string) {
        let prefixMatch = this.$prefixes.find(prefix => term.startsWith(prefix));
        if (prefixMatch != null) {
            this.$keywordFormatted = prefixMatch + term;
            return true;
        }
        return false;
    }

    search(term: string) {
        this.$results = [];
        let finds = this.$index.search(term, 10, { bool: "or", suggest: true, enrich: true, tags: ["nations"] }, null);
        finds.forEach((find: any): void => {
            find.result.forEach((resultEntry: any): void => {
                const candidate = this.$candidates?.find(e => e.id == resultEntry);
                if (candidate != null) {
                    console.debug("Result: " + candidate?.id + " - " + candidate?.content);
                    this.$results.indexOf(candidate) === -1 ? this.$results.push(candidate) : console.debug("Candidate " + candidate + " already found.");
                }
            });
        });
    }

    /* 	@eventOptions({})
        _openedChange(opened: boolean) {
            console.debug("_openedChange: " + opened);
            if (opened) {
                this._setOverlayPosition();
            }
        }
	
        _setOverlayPosition() {
            if (this.textfield && this.resultOverlay) {
                const inputRect = this.textfield.getBoundingClientRect();
                this.resultOverlay.style.left = inputRect.left + 'px';
                this.resultOverlay.style.top = inputRect.bottom + window.pageYOffset + 'px';
                this.resultOverlay.updateStyles({ '--vcf-autosuggest-options-width': inputRect.width + 'px' });
            }
        } */
}

interface FastsearchServerInterface {
    enter(term: string): void;
    prefixMatch(term: string): void;
    regexMatch(term: string): void;
    clientMatch(id: string): void;
}