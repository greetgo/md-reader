class MdRef extends HTMLElement {

  constructor() {
    super();
    this.addEventListener('click', this.onClick.bind(this));

    console.log("88WiMWTccD :: constructor", this);
  }

  onClick(event) {
    console.log("n75Ru7to9y :: onClick event = ", event);
    let reference = this.getAttribute("href");
    if (reference) {
      location.href = reference;
    }
  }

}

window.customElements.define("md-ref", MdRef);
console.log("X1YkGuZFxJ :: window.customElements.define")
