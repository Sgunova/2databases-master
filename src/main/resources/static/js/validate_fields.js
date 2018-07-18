var emailErrorMessage = "Неверный формат email";
var phoneErrorMessage = "Неверный формат номера - +хххххххххххх";
var passwordLengthErrorMessage = "Минимальная длина пароля 8 символов";
var passwordEqualsErrorMessage = "Пароли не совпадают";

function validateFields(form) {
    var isAllowedSend = true;
    $.each(form.find(".required"), function () {
        if ($(this).val() === "") {
            isAllowedSend &= false;
            showInvalidMessageArg($(this).attr("name"), "Пожалуйста, заполните это поле");
        }
    });

    $.each(form.find(".email"), function () {
            if (!$(this).hasClass("required") && $(this).val().length > 0 && !isEmail($(this).val())) {
                isAllowedSend &= false;
                showInvalidMessageArg("email", emailErrorMessage);
            }

        }
    );
    $.each(form.find(".phone"), function () {
        if (!$(this).hasClass("required") && $(this).val().length > 0 && !isPhone($(this).val())) {
            isAllowedSend &= false;
            showInvalidMessageArg("phone", phoneErrorMessage);
        }
    });
    isAllowedSend &= checkPasswordValid(form);


    return isAllowedSend;
}

function checkPasswordValid(form) {
    var isValidPasswords = true;
    var passwordFields = form.find(".required[type='password']");
    if (passwordFields.length === 1) {
        return isValidPasswords;
    }
    $.each(passwordFields, function () {
        if (!isValidPassword($(this).val())) {
            isValidPasswords &= false;
            showInvalidMessageArg($(this).attr('id'), passwordLengthErrorMessage);
        }
    });
    if (isValidPasswords) {
        var password = form.find('[name="password"]').val();
        var confirmPassword = form.find('[name="confirmPassword"]').val();
        if (!isEqualsPassword(password, confirmPassword)) {
            isValidPasswords &= false;
            showInvalidMessageArg("confirmPassword", passwordEqualsErrorMessage);
        }
    }
    return isValidPasswords;
}

function showInvalidMessage(item) {
    showInvalidMessageArg(item.field, item.defaultMessage);
}

function showInvalidMessageArg(ifield, defaultMessage) {
    var field = $("[name=" + ifield + "]");
    field.addClass("invalid");
    field.siblings("label").attr('data-error', defaultMessage).addClass("active");
}

function isEmail(email) {
    var regex = /^.+@.+$/;
    return regex.test(email);
}

function isPhone(phone) {
    var regex = /^([+])+([0-9]{12})+$/;
    return regex.test(phone);
}

function isValidPassword(password) {
    return password.length >= 8;
}

function isEqualsPassword(password, confirmPassword) {
    return password == confirmPassword;
}
