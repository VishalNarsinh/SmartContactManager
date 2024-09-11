let currentTheme = getTheme();
document.addEventListener("DOMContentLoaded", () => {
  const navbar = document.querySelector("#navbar");
  if (window.location.href.includes("user")) {
    navbar.classList.add("sm:pl-64");
  } else {
    navbar.classList.remove("sm:pl-64");
  }
  changeTheme();
});
function changeTheme() {
  changePageTheme(currentTheme, currentTheme);
  const changeThemeButton = document.querySelector("#change_theme_button");
  changeThemeButton.addEventListener("click", (event) => {
    let oldTheme = currentTheme;
    if (currentTheme === "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }
    changePageTheme(currentTheme, oldTheme);
  });
}

function changePageTheme(newTheme, oldTheme) {
  setTheme(newTheme);
  document.querySelector("html").classList.remove(oldTheme);
  document.querySelector("html").classList.add(newTheme);
  document
    .querySelector("#change_theme_button")
    .querySelector("span").textContent =
    newTheme === "light" ? "Dark" : "Light";
}

function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}
