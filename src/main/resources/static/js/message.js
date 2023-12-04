let form = document.getElementById("registrationForm");
let mail = document.getElementById("mail");
let message = document.querySelector('.message');

form.querySelectorAll(".form-control").forEach(input => {
    input.addEventListener("input", function () {
        if (input.checkValidity()) {
            input.classList.remove('is-invalid');
            input.classList.add('is-valid');
        } else {
            input.classList.remove('is-valid');
            input.classList.add('is-invalid');
        }
    });
});

form.addEventListener("submit", function (event) {
    let isValidForm = form.checkValidity();
    let password = document.getElementById("password");
    let confirmPassword = document.getElementById("confirmPassword");
    let passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\W]).{9,}$/;

    if (!isValidForm) {
        event.preventDefault();
        event.stopPropagation();
        form.classList.add('was-validated');
    } else if (!passwordPattern.test(password.value)) {
        event.preventDefault();
        password.classList.add('is-invalid');
    } else if (password.value !== confirmPassword.value) {
        event.preventDefault();
        confirmPassword.classList.add('is-invalid');
    } else {
    }
});
document.getElementById('togglePassword').addEventListener('click', function (e) {
    // Basculer l'icône de l'œil
    e.target.classList.toggle('fa-eye');
    e.target.classList.toggle('fa-eye-slash');

    // Basculer le type de champ de mot de passe
    const password = document.getElementById('password');
    if (password.type === 'password') {
        password.type = 'text';
    } else {
        password.type = 'password';
    }
});

