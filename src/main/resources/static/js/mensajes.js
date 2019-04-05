var stompClient = null;
var nombreUsuario; //usuario cliente
var nombreUsuarioMensaje; //usario al que se le va mandar un mensaje

$(function() {
	
	nombreUsuario = $("#nombreUsuario").val();
	
	connect();
	
	/* esto se pone aqui porque la conexion con el socket de utils no es necesaria en esta pantalla */
	//click en el li del nombre y foto de perfil
	$(".infoUsuario").click(function(){
		
	    //si se estan mostrando las opciones las ocultamos
	    if ($("#divInfoUsuario").css("display") == "grid") {
	
	        $(".infoUsuario").css("background-color", "rgba(0, 0, 0, 0)");
	        $("#divInfoUsuario").hide();
	
	        //si no es que las opciones estan ocultas, las mostramos 
	    } else {
	        $("#divInfoUsuario").css("display", "grid");
	        $(".infoUsuario").css("background-color", "#448AFF");
	    }
	});
	
	//input para buscar contactos
	$("#buscador").keyup(function () {
		
		//si no esta vacio
		if ($(this).val() != ""){
			
			//ocultamos los que no coincidan con la busqueda
			$(`#contactos > div:not(.${$(this).val().toUpperCase()}`).hide();
		      
			//mostramos los que si coincidan con la busqueda
	        $(`#contactos > div[class*=${$(this).val().toUpperCase()}]`).show();
	        
	        //si no es que esta vacio
		} else {
			//mostramos todos los contactos
			$("#contactos > div").show();
		}
    });
	
	//click en un contacto
	$("#contactos > div").click(function(){
		
		//ocultamos los chats
		$(".der > div").hide();
		
		//quitamos el color de fondo de los contactos
		$("#contactos > div").css("background-color", "");
		
		//nombre del usuario en mayusculas (con espacios)
		nombreUsuarioMensaje = $(this).attr("class");
		
		//buscamos el chat del contacto y lo mostramos
		//(tanta movida es por si tiene espacios el nombre)
		let query = "." + $(this).attr("class").split(' ').join(".");
		$(query).show();
		
		//ponemos el contacto marcado con el color de fondo
		$("#contactos > div" + query).css("background-color", "rgb(121, 211, 241)");
		
		//desplazamos el chat hacia abajo
		$(".der").scrollTop($(".der")[0].scrollHeight);
		
	});
	 
	$("form").submit(function(){

		//obtenemos el input del formulario que se muestra
		let inpMensaje = $(this).find("input");
		
		//proteccion xss simple
		let mensaje = inpMensaje.val().trim().replace(/<|>|\"|'|`/g,"");
		
		if (mensaje) {
		
			//mandamos el mensaje al server
			stompClient.send(`/app/mensajes/${nombreUsuarioMensaje}/enviarMensaje`, {}, mensaje);
			inpMensaje.val("");
			
			//añadimos el mensaje al chat
			$(`.der > div.${nombreUsuarioMensaje} > div:last-of-type`).after(`<div class="enviado">${mensaje}</div>`);
			
			//desplazamos el chat hacia abajo
			$(".der").scrollTop($(".der")[0].scrollHeight);
		}
		
		return false;
	});
});

function connect() {
    
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
 
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    
	//(necesario el toUpperCase porque se coge desde el attr class que esta en mayusculas)
    stompClient.subscribe(`/mensajes/${nombreUsuario.toUpperCase()}`, onNewMessage);
    
}

function onError(error) {
    console.log("Error " + error);
}

function onNewMessage(payload){
	
	let respuesta = JSON.parse(payload.body);
	
	if (respuesta.mensaje){
		
		//añadimos el mensaje al div correspondiente
		$(`.der > div.${respuesta.nombreUsuario.toUpperCase().split(' ').join(".")} > div:last-of-type`).after(`<div class="recibido">${respuesta.mensaje}</div>`);
		
		//bajamos el scroll abajo del todo
		$(".der").scrollTop($(".der")[0].scrollHeight);
		
	}
}