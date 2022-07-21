const signUpBtn = document.getElementById("signUp");
const signInBtn = document.getElementById("signIn");
const firstForm = document.getElementById("frmSignIn");
const secondForm = document.getElementById("frmSignUp");
const container = document.querySelector(".container");

signUpBtn.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});

signInBtn.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});

firstForm.addEventListener("submit", (e) => e.preventDefault());
secondForm.addEventListener("submit", (e) => e.preventDefault());