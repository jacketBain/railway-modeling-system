var form = document.getElementById('loginForm');
var login = document.getElementById('inputLogin');
var password = document.getElementById('inputPassword');
var password2 = document.getElementById('inputPassword2');

var loginError = document.getElementById('loginError');
var passwordError = document.getElementById('passwordError');
var password2Error = document.getElementById('password2Error');

function showError(field){
    switch (field) {
        case "login":
            loginError.innerHTML = "<div id=\"warning\" class=\"alert alert-danger alert-dismissible mt-2 mb-2\" role=\"alert\">\n" +
                "\t<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
                "\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                "\t</button>\n" +
                "\tДлина логина: 3-30 символов.\n" +
                "\tДопустимые символы:\r\n" +
                "\t1. Латинские буквы, цифры, дефисы, знаки подчеркивания.\n" +
                "\t2. Первый символ буква.\n" +
                "</div>";
            break;
        case "password":
            passwordError.innerHTML = "<div id=\"warning\" class=\"alert alert-danger alert-dismissible mt-2 mb-2\" role=\"alert\">\n" +
                "\t<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
                "\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                "\t</button>\n" +
                "\tМинимальная длина: 8 символов.\n" +
                "\tДопустимые символы:\n" +
                "\tЛатинсике буквы, цифры, _ и - .\n" +
                "</div>";
            break;
        case "password2":
            password2Error.innerHTML = "<div id=\"warning\" class=\"alert alert-danger alert-dismissible mt-2 mb-2\" role=\"alert\">\n" +
                "\t<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
                "\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                "\t</button>\n" +
                "\tПароли не совпадают.\n" +
                "</div>";
            break;
        case "occupied":
            loginError.innerHTML = "<div id=\"warning\" class=\"alert alert-danger alert-dismissible mt-2 mb-2\" role=\"alert\">\n" +
                "\t<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
                "\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                "\t</button>\n" +
                "\tЛогин" + login.value + " уже занят." +
                "</div>";
            break;
    }
}

function loginIsFree(){
    $.ajax({
        url: "/signup/users",
        data: { login: login.value},
        dataType: "json",
        success: function (data) {
            if(data.status === "SUCCESS")
                loginError.innerHTML = "";
            else
                loginError.innerHTML = "<div id=\"warning\" class=\"alert alert-danger alert-dismissible mt-2 mb-2\" role=\"alert\">\n" +
                    "\t<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
                    "\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                    "\t</button>\n" +
                    "\tПользователь с таким именем уже существует.\n" +
                    "</div>"
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}


login.addEventListener("input", function () {
    if(login.validity.valid) {
        if (login.value === "")
            loginError.innerHTML = "";
        else {
            loginIsFree();
        }
    }
    else{
        if(login.value === "")
            loginError.innerHTML = "";
        else
        showError("login");
    }

}, false);

password.addEventListener("input", function () {
    if(password.validity.valid || password.value === ""){
        passwordError.innerHTML = "";
    }
    else{
        showError("password");
    }
}, false);

password2.addEventListener("input", function () {
    if(password.value !== password2.value){
        showError("password2");
    }
    else {
        password2Error.innerHTML = "";
    }
}, false);

form.addEventListener("submit", function (event) {
    if(!login.validity.valid || !password.validity.valid || password.value !== password2.value){
        event.preventDefault();
    }
    else{
        event.preventDefault();

        $.ajax({
            type : "post",
            url : '/signup',
            data : { 'username' : login.value, 'password' : password.value },
            response: 'text',
            success : function() {
                $.post(
                    "/j_spring_security_check",
                    {'username' : login.value, 'password' : password.value},
                    function () {
                        window.location.replace("/home");
                    }
                )
            }
        });
    }
});

