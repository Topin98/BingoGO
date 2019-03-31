$(function() {

    $(".mostrarLogin, .cerrar").click(mostrarLogin);
});

function mostrarLogin() {
    $(".mostrarLogin").css("background-color", "#448AFF");

    $("#divLogin").slideToggle("slow", function () {

        if ($("#divLogin").css("display") != "block") {
            $(".mostrarLogin").css("background-color", "rgba(0, 0, 0, 0)");
        }
    });
}