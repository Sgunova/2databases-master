$(document).on("ready", function () {

    var datePicker = $(".date-picker");

    datePicker.focusin(function () {
        $(this).parent().parent().parent().find("legend").css("color", "#1e3685");
    });
    datePicker.focusout(function () {
        $(this).parent().parent().parent().find("legend").css("color", "#BDBDBD");
    });

    $("#submit-btn").click(function () {
        $(this).closest('form').submit();
    });

    var pages = $(".pagination").find("li");
    if (pages.size() < 6) {
        $(".pagination-area").hide();
    }
    pages.each(function (index) {
        switch (index) {
            case 0:
                $(this).find("span[aria-hidden]").html("<i class='material-icons'>first_page</i>");
                $(this).find("a").css("padding", "0");
                break;
            case 1:
                $(this).find("span[aria-hidden]").html("<i class='material-icons'>chevron_left</i>");
                $(this).find("a").css("padding", "0");
                break;
            case pages.size() - 2:
                $(this).find("span[aria-hidden]").html("<i class='material-icons'>chevron_right</i>");
                $(this).find("a").css("padding", "0");
                break;
            case pages.size() - 1:
                $(this).find("span[aria-hidden]").html("<i class='material-icons'>last_page</i>");
                $(this).find("a").css("padding", "0");
                break;
            default:
                $(this).find(".sr-only").remove();
                if ($(this).hasClass("active")) {
                    $(this).html("<a>" + $(this).find("span").text() + "</a>")
                }

        }
    })
});

function reset() {
    $("input").each(function (index, elem) {
        $(elem).val("");
        MaterializeUtils.datePickerInit();
    });
    $("select").each(function (index, elem) {
        $(elem).val(null);
        MaterializeUtils.selectInit();
    });
}
