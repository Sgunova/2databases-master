MaterializeUtils = {};

MaterializeUtils.showErrorModal = function (message) {
    var error = $('#error-modal');
    error.find("p").text(message);
    error.modal().modal('open');
};

MaterializeUtils.showConfirmationModal = function (text, complete) {
    var modal = $('#confirmation-modal');

    modal.find("p").text(text);
    modal.find("#confirm-btn").unbind("click.confirm");
    modal.find("#confirm-btn").bind("click.confirm", complete);

    modal.modal({
        dismissible: false
    }).modal('open');
};

MaterializeUtils.toast = function (message) {
    Materialize.toast(message, 1000);
};

MaterializeUtils.materializeUpdate = function () {
    this.selectInit();
    this.datePickerInit();
    this.sideHeader();
    this.fileWrapper();
    this.disableFields();
    Materialize.updateTextFields();
};

MaterializeUtils.selectInit = function () {
    $(".input-field:has('.select-wrapper')").css("height", "66px");
    $("select").material_select();
    var select = $(".select-wrapper");
    select.focusin(function () {
        $(this).parent().find("label").css("color", "#1e3685");
    });
    select.focusout(function () {
        $(this).parent().find("label").css("color", "#BDBDBD");
    });
    if (select.has("ul.multiple-select-dropdown")) {
        select.find("input").css("width", "98%");
        select.each(function () {
            var multiple = $(this).find("select");
            if (multiple.val() === null) {
                $(this).find("input").val("Выберите элементы из списка");
            }
            multiple.change(function () {
                if (multiple.val() === null) {
                    multiple.parent().find("input").val("Выберите элементы из списка");
                }
            })
        });
    }
};

MaterializeUtils.datePickerInit = function () {
    try {
        $(".date-picker").pickadate({
            format: "dd.mm.yyyy",
            monthsFull: ['января', 'февраля', 'марта', 'апреля', 'мая', 'июня', 'июля', 'августа', 'сентября', 'октября', 'ноября', 'декабря'],
            monthsShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
            weekdaysFull: ['воскресенье', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'],
            weekdaysShort: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
            weekdaysLetter: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
            today: 'Сегодня',
            clear: 'Очистить',
            close: '',
            firstDay: 1,
            formatSubmit: "dd.mm.yyyy",
            selectMonths: true,
            selectYears: 15,
            onSet: function (e) {
                if (e.select !== undefined) {
                    $(this).pickadate('close');
                }
            }
        });
    } catch (e) {
    }
};

MaterializeUtils.sideHeader = function () {
    $("#collapse-btn").sideNav();
    var sideHeader = $(".collapsible-header");
    if (sideHeader.hasClass("active")) {
        sideHeader.find("i").text("keyboard_arrow_up");
    } else {
        sideHeader.find("i").text("keyboard_arrow_down");
    }
    sideHeader.click(function () {
        if (!$(this).hasClass("active")) {
            $(this).find("i").text("keyboard_arrow_up");
        } else {
            $(this).find("i").text("keyboard_arrow_down");
        }
    });
};

MaterializeUtils.fileWrapper = function () {
    $(".file-path").click(function () {
        $(this).parents(".input-field").find("input[type=file]").click();
    })
};

MaterializeUtils.alignFieldsetHeight = function (nonAlignClass) {
    var fieldset = $("fieldset");
    var max = Math.max.apply(null, fieldset.not("." + nonAlignClass).map(function () {
        return $(this).height();
    }).get());
    if (max > 0) {
        fieldset.not("." + nonAlignClass).height(max);
    }
};

MaterializeUtils.disableFields = function () {
    $(".readonly-fields").find("input").prop("disabled", true);
};