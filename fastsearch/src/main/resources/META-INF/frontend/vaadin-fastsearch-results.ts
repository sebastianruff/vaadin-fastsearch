import { customElement, html, property } from 'lit-element';
import { Candidate } from "./candidate";
import { repeat } from 'lit-html/directives/repeat';
import '@vaadin/vaadin-overlay';
import { OverlayElement } from '@vaadin/vaadin-overlay';
import { registerStyles, css } from '@vaadin/vaadin-themable-mixin/register-styles.js';

registerStyles(
  'vaadin-fastsearch-results',
  css`
     :host {
       align-items: flex-start;
       justify-content: flex-start;
       right: auto;
       position: absolute;
       bottom: auto;
       background: #fff;
     }
 
     [part='overlay'] {
       background-color: var(--lumo-base-color);
       background-image: linear-gradient(var(--lumo-tint-5pct), var(--lumo-tint-5pct));
       border-radius: var(--lumo-border-radius);
       box-shadow: 0 0 0 1px var(--lumo-shade-5pct), var(--lumo-box-shadow-m);
     }
   `
);

@customElement('vaadin-fastsearch-results')
export class FastsearchResults extends OverlayElement {

  @property({ type: Array })
  $results: Array<Candidate> = [];

  render() {
    return html`
            <vaadin-fastsearch-results id="result-overlay" opened="{{opened}}" theme$="[[theme]]">
              ${repeat(this.$results, (result: any) => result.id, (result: any) => html`
              <p>${result.content}</p>
              ${console.debug("FastsearchResults rendered: " + result.content)}
              `)}
            </vaadin-fastsearch-results>
`}
}
