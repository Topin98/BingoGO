$(function() {

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