$(document).ready(function(){
    $("#login-form").ajaxForm({
        beforeSubmit: function (arr, $form, options) {
            return validateFields($form) != 0;
        },
        url: $(this).attr("action"),
        method: "POST",
        success: function (response) {
            CallbackUtil.redirect("");
        },
        error: function (error) {
            if (error.status === 401) {
                MaterializeUtils.toast("Неверный логин или пароль");
            } else {
                MaterializeUtils.showErrorModal("Ошибка при попытке авторизации");
            }
        }
    });
});