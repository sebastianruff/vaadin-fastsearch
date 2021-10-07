import { customElement, html, LitElement, property } from 'lit-element';
import { Candidate } from './vaadin-fastsearch';
import { repeat } from 'lit-html/directives/repeat';

@customElement('vaadin-fastsearch-results')
export class FastsearchResults extends LitElement {

  @property({ type: Array })
  $results: Array<Candidate> = [];

  render() {
    return html`
    ${repeat(this.$results, (result) => result.id, (result) => html`
  <div>${result.content}</div>
    `)}
`}
}
