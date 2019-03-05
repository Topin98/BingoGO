//este archivo lo va a llamar index.html, login.html y registro.html
$(function() {

    $(".mostrarLogin, .cerrar").click(mostrarLogin);

    $("#formRegistro").submit(function (e) {
    	
        let pw = $("#pw");
        let pw2 = $("#pw2");
        
        if (pw.val() != pw2.val()) {

        	pw.val("");
        	pw2.val("");
        	
            //muestra el mensaje de que las contraseñas no coinciden
        	$("#errorPws").show();
            
            //oculta el div que contiene los mensajes de errores procedentes del controlador
            $(".erroresRegistro").hide();
            
            return false;
        }
    });
});

function mostrarLogin() {
    $(".mostrarLogin").css("background-color", "#448AFF");

    $("#divLogin").slideToggle("slow", function () {

        if ($("#divLogin").css("display") != "block") {
            $(".mostrarLogin").css("background-color", "rgba(0, 0, 0, 0)");
        }
    });
}