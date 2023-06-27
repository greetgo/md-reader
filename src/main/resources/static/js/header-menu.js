const searchLineMobileEl = document.getElementById("search-line-mobile");
const content = document.getElementById("content");
const breadcrumbs = document.getElementById("breadcrumbs");
const main = document.getElementById("main");
const headerMenu = document.getElementById("headerMenu");

let menuOpened = false;

function toggleMenuIcon() {
  menuOpened = !menuOpened;

  if (menuOpened) {
    headerMenu.style.fill = '#2f80ed';

    searchLineMobileEl.style.display = 'block';
    content.style.display = 'flex';
    breadcrumbs.style.top = '515px';
    main.style.marginTop = '554px';
  } else {
    headerMenu.style.fill = '#82898c';

    searchLineMobileEl.style.display = 'none';
    content.style.display = 'none';
    breadcrumbs.style.top = '62px';
    main.style.marginTop = '101px';
  }
}
