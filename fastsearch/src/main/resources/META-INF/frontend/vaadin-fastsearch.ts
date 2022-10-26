// vaadin-fastsearch.ts
import { customElement, html, LitElement, property } from 'lit-element';
import '@vaadin/vaadin-text-field';
import { TextFieldElement } from '@vaadin/vaadin-text-field';
import flexsearch from "flexsearch"
import { repeat } from 'lit-html/directives/repeat';

@customElement('vaadin-fastsearch')
export class Fastsearch extends LitElement {

	$server?: FastsearchServerInterface;

	@property({ type: Array })
	$candidates: Array<Candidate> = [];

	@property({ type: Array })
	$results: Array<Candidate> = [];

	@property({ type: Array })
	$prefixes: Array<string> = [];

	@property({ type: String })
	$keywordFormatted: string = "";

	/* 	@property({ attribute: false })
		$resultsMap: Map<any, FastsearchResults> = new Map(); */



	$index = new flexsearch.Document({
		document: {
			id: "id",
			index: ["content"],
			tag: "tag",
		},
		tokenize: 'full'
	});

	updated(changedProperties: any) {
		if (changedProperties.has('$candidates')) {
			this.$candidates?.forEach(candidate => {
				this.$index.add(candidate.id, candidate, false);
			});
		}
	}

	render() {
		return html`
			<vaadin-text-field @keyup="${this.keyup}"></vaadin-text-field>
			<div> 	
			<p>${this.$keywordFormatted}</p>
		</div>
		<vaadin-fastsearch-results id="resultOverlay" opened="{{opened}}" theme$="[[theme]]"> 		
		${repeat(this.$results, (result) => result.id, (result) => html`
			<p>${result.content}</p>
			${console.debug("Result rendered: " + result.content)}
    `)}	
		</vaadin-fastsearch-results>

`}

	keyup(e: KeyboardEvent) {
		const term: string = (<TextFieldElement>e.currentTarget).value;
		console.debug("Typing: " + term);
		this.search(term)
		if (e.key === 'Enter') {
			if (this.isPrefixAction(term)) {
				this.$server?.prefixMatch(term);
			} else if (this.$results.length > 0) {
				this.$server?.clientMatch(this.$results[0].id);
			} else if (term.length > 0) {
				this.$server?.enter(term);
			}
		}
	}

	isPrefixAction(term: string) {
		let prefixMatch = this.$prefixes.find(value => term.startsWith(value));
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
}

export interface Candidate {
	id: any;
	content: string;
	tag: string;
}

interface FastsearchServerInterface {
	enter(term: string): void;
	prefixMatch(term: string): void;
	clientMatch(id: string): void;
}