CallbackUtil = {};

CallbackUtil.errorResponse = function(error) {
    if (error.status === 403) {
        MaterializeUtils.showErrorModal("У вас нет доступа для совершения данной операции");
    }
    if (error.status === 500) {
        MaterializeUtils.showErrorModal(error.responseJSON);
    }
    if (error.status === 413) {
        MaterializeUtils.showErrorModal("Отправленный запрос превышает допустимый размер");
    }
};

CallbackUtil.emptySuccessSave = function() {
    MaterializeUtils.toast("Данные успешно сохранены");
};

CallbackUtil.redirect = function (url) {
    window.location = contextRoot + url;
};

CallbackUtil.reload = function () {
    window.location.reload();
};