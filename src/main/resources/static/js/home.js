//este archivo lo va a llamar home.html, login.html y registro.html
window.onload = function () {

    $(".mostrarLogin, .cerrar").click(mostrarLogin);

    $("#formRegistro").submit(function (e) {
    	
        let pw = $("#pw");
        let pw2 = $("#pw2");
        
        if (pw.val() != pw2.val()) {

        	pw.val("");
        	pw2.val("");
        	
            $("#errorPws").css("display", "block");
            
            return false;
        }
    });
}

function mostrarLogin() {
    $(".mostrarLogin").css("background-color", "#448AFF");

    $("#divLogin").slideToggle("slow", function () {

        if ($("#divLogin").css("display") != "block") {
            $(".mostrarLogin").css("background-color", "rgba(0, 0, 0, 0)");
        }
    });
}