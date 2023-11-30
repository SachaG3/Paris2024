let form=document.querySelector("form");
let mail=document.getElementById("mail");
let message=document.querySelector('.message');

form.addEventListener("submit", function (event) {
    event.preventDefault();
    let m=mail.value;
    if (!mail.validity.valid) {
        message.innerHTML = mail.validationMessage;
        message.className = "message error";
    } else {
        message.innerHTML=`'${m}' est valide !`;
        message.className = "message valid";
    }
});
mail.addEventListener("input", function (event) {
    if (mail.validity.valid) {
        message.innerHTML = "";
        message.className = "message";
    }
});