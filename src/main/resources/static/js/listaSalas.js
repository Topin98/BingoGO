var stompClient = null;

$(function() {
	
	//conectar al chat de la pantalla lista de salas
	connect();
	
	//tooltips con el nombre de usuario para cada foto
	$('[data-toggle="tooltip"]').tooltip();
	
	/* dialogo para crear una sala */
    $("#dialogo").dialog({
    	autoOpen: false,
        resizable: false,
        height: "auto",
        width: 500,
        height: 330,
        modal: true
    });
	
	$("#btnCrearSala").click(function(){
		
		//si crea una sala publica y le da para atras le pediria pw con el si marcado
		if ($("#rb1").is(":checked")){
			
			$("input[type=password]").hide();
			
			//quitamos el atributo required porque aunque este oculto no dejaria hacer el submit
			$("input[type=password]").removeAttr("required");
			
		} else {
			
			$("input[type=password]").show();
			
			$("input[type=password]").prop("required", true);
		}
		
		$("#dialogo").dialog("open");
	});

	$("input[type=radio]").change(function() {
		
		//si si es publica
		if (this.id == "rb1"){
			$("input[type=password]").hide();
			
			//quitamos el atributo required porque aunque este oculto no dejaria hacer el submit
			$("input[type=password]").removeAttr("required");
			
			//si no es que es privada
		} else {
			
			$("input[type=password]").show();
			
			$("input[type=password]").prop("required", true);
			
		}
	});
	
	/* dialogo para pedir la contraseña de una sala */
    $("#dialogoPw").dialog({
    	autoOpen: false,
        resizable: false,
        height: "auto",
        width: 500,
        height: 200,
        modal: true
    });
    
    $(".grid-container:has(i.fa-lock)").click(function(){
		
    	$("input[name=idSala]").val($(this).attr("id"));
    	$("#dialogoPw").dialog("open");
    	
    	return false;
	});
    
	//formulario con el input del chat
	var inpMensaje = $("#frmEnviarMensaje > input");
	$("#frmEnviarMensaje").submit(function(){
		
		let mensaje = inpMensaje.val().trim();
		
		if (mensaje) {
			stompClient.send("/app/salas/chat/enviarMensaje", {}, mensaje);
			inpMensaje.val("");
		}
		
		return false;
	});
	
	/* comprobamos si la pagina se ha cargado a partir de que se ha intentado crear una sala con un nombre demasiado larga */
	
	//obtenemos el valor del parametro error
	var error = getUrlParameter("error");
	
	//si hay error mostramos el dialogo con el error
	if (error != null){
		//convertimos el texto a texto plano
		$("#errorNuevaSala").text(error);
		$("#dialogo").dialog("open");
	}
	
});

function getUrlParameter(sParam) {
	
    var sURLVariables = window.location.search.substring(1).split('&');

    for (let i = 0; i < sURLVariables.length; i++) {
        let sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}

function connect() {
    
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
 
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    
    stompClient.subscribe("/salas/chat", onNewMessage);
    
}

function onError(error) {
    console.log("Error " + error);
}

function onNewMessage(payload){
	
	let respuesta = JSON.parse(payload.body);
	
	if (respuesta.mensaje){
		
		$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombreUsuario}/">${respuesta.nombreUsuario}</a><span>: ${respuesta.mensaje}</span></div>`);
		
		//desplazamos el scroll del chat abajo del todo
	    $("#containerMensajes").scrollTop($("#containerMensajes")[0].scrollHeight);
	}
	
}

