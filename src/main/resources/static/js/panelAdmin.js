$(function() {

    $(".titulo").click(function(){

        //ocultamos todos los enlaces que se van mostrando y ocultando
        $(".titulo > .opcion").css("display", "none");

        //si se expande la card los mostramos, si no los ocultamos
        if ($(this).hasClass("collapsed")) $(this).children(".opcion").css("display", "block");
        else $(this).children(".opcion").css("display", "none");
    });

    $("#dialogoCorreo").dialog({
        autoOpen: false,
        resizable: false,
        width: 400,
        height: 310,
        modal: true
    });

    $(".btnMandarCorreo").click(function(e){
    	
    	$("#correoDestino").val($(this).data("correo"));
    	
        $("#dialogoCorreo").dialog("open");

        //hace que no se oculte o muestre el accordion
        e.stopPropagation();
    });
    
    $(".aMandarCorreo").click(mostrarLoading);
    $(".formMandarCorreo").submit(function(){
    	$("#dialogoCorreo").dialog("close");
    	mostrarLoading();
    });
    
    var mensajeDialog = getUrlParameter("mensaje");
	if (mensajeDialog != null) {
		$("#dialogoMensaje").find("p").text(mensajeDialog);
		
		$("#dialogoMensaje").dialog({
	        resizable: false,
	        height: "auto",
	        width: 400,
	        modal: true
	    });
	}
    
});

function mostrarLoading(){
	$(".loading").show();
}

function getUrlParameter(sParam) {
	
    var sURLVariables = window.location.search.substring(1).split('&');

    for (let i = 0; i < sURLVariables.length; i++) {
        let sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}