$(function() {

    $("#formRegistro").submit(function (e) {
    	
        let pw = $("#pw");
        let pw2 = $("#pw2");
        
        if (pw.val() != pw2.val()) {

        	pw.val("");
        	pw2.val("");
        	
            //muestra el mensaje de que las contraseñas no coinciden
        	$("#errorPws").show();
            
            //oculta el div que contiene los mensajes procedentes del controlador
            $(".mensajeController").hide();
            
            return false;
        }
    });
    
    $("input[type=file]").change(function(){
        
        if (this.files && this.files[0]) {
            var reader = new FileReader();
    
            reader.readAsDataURL(this.files[0]);

            reader.onload = function (e) {
                $("#imagenPerfil").attr("src", e.target.result);
            }
        }
    });
});