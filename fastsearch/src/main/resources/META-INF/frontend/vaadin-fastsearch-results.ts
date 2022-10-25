import { customElement, html, LitElement, property } from 'lit-element';
import { Candidate } from './vaadin-fastsearch';
import { repeat } from 'lit-html/directives/repeat';
import { OverlayElement } from '@vaadin/vaadin-overlay/src/vaadin-overlay';
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
    ${repeat(this.$results, (result) => result.id, (result) => html`
  <div>${result.content}</div>
    `)}
`}
}
