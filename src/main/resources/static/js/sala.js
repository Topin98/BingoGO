var stompClient = null;
var stompClientChat = null;
var idSala;
var nombreUsuario;
var jugadores = [];

$(function() {
	
	idSala = $("#idSala").val();
	nombreUsuario = $("#nombreUsuario").val();
	
	//Connect to WebSocket Server.
	connect();
	
	//como los divs van ir apareciendo, se declara asi el evento on click para los div hijos de listaJugadores
	//(asi nos ahorramos poner los eventos en la funcion onNewUser cada vez que se une un usuario)
	$("#listaJugadores").on("click", "div", function() {
		
		//obtenemos el jugador clicado
		let jugador = jugadores.find(x => x.nombre == $(this).text());

		//se actualiza el layout
		$("#infoJugador").show();
		$("#nombreJugador").text(jugador.nombre);
		$("#puntuacionJugador").text(jugador.puntuacionTotal);
		$("#imagenJugador").attr("src", `/perfil/${jugador.nombre}/imagen`);
		
		//si el usuario clicado no somos nosotros igual lo podemos mostrar
		//si el usuario no es propietario de la sala no pasa nada porque no va estar ni en el dom
		jugador.nombre == nombreUsuario ? $("#btnExpulsarJugador").hide() : $("#btnExpulsarJugador").show();
		
	});
	
	//formulario con el input del chat
	var inpMensaje = $("#frmEnviarMensaje > input");
	$("#frmEnviarMensaje").submit(function(){
		
		let mensaje = inpMensaje.val().trim();
		
		if (mensaje) {
			stompClientChat.send(`/app/salas/sala/${idSala}/chat/enviarMensaje`, {}, mensaje);
			inpMensaje.val("");
		}
		
		return false;
	});
	
	//boton de empezar partida (se pone asi el evento por si abandona el usuario propietario no tener que volver a declararlo)
	$("#opciones").on("click", "#btnEmpezarPartida", function(){
		stompClient.send(`/app/salas/sala/${idSala}/empezarPartida`);
	});
	
	//boton de expulsar jugador (se pone asi el evento por si abandona el usuario propietario no tener que volver a declararlo)
	$("#infoJugador").on("click", "#btnExpulsarJugador", function(){
		stompClient.send(`/app/salas/sala/${idSala}/expulsarJugador`, {}, $(this).siblings("#nombreJugador").text());
	});
	
	$("#btnAbandonarSala").click(function(){
		//nos desconectamos de la sala (nos iremos cuando llegue un mensaje al chat indicando que se han reflejado los cambios en el servidor)
		stompClient.disconnect();
	});
	
});


function connect() {
	
	//socket para cuando un jugador se une o abandona la sala
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    
    //socket para cuando mandamos o recibimos un mensaje en el chat
    var socketChat = new SockJS("/ws");
    stompClientChat = Stomp.over(socketChat);
    stompClientChat.connect({}, onConnectedChat, onError);
}

function onConnected() {
    
	//nos suscribimos a la sala
    stompClient.subscribe(`/salas/sala/${idSala}`, onNewUser);
 
    //indicamos nuestra llegada al servidor
    stompClient.send(`/app/salas/sala/${idSala}/newUser`);
}

function onError(error) {
	console.log("Error " + error);
}

function onNewUser(payload){
	
	//respuesta que contiene tipo, mensaje y usuario
	let respuesta = JSON.parse(payload.body);
	
	//comprobamos que haya mensaje para que no salga en el chat "undefined"
	//por si acaso alguien ejecuta algo de js desde la consola, como por ejemplo intenta empezar la partida una persona que no es el propietario de la sala
	if (respuesta.mensaje){
		
		//ponemos el mensaje en el chat
		//el perfil del usuario se abriria en una pestaña nueva al clicar sobre el nombre
		$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombreUsuario}/" target="_blank">${respuesta.nombreUsuario}</a> <span>${respuesta.mensaje}</span></div>`);
		
		//desplazamos el scroll del chat abajo del todo
	    $("#containerMensajes").scrollTop($("#containerMensajes")[0].scrollHeight);
	}
	
	switch(respuesta.tipo){
		case 0: nuevoJugador(respuesta);
			break;
		case 1: seFueJugador(respuesta);
			break;
		case 2: expulsarJugador(respuesta);
			break;
		case 3: empezarPartida(respuesta);
			break
		
	}
	
}

//si alguien se unio a la sala
function nuevoJugador(respuesta){
	
	$("#listaJugadores").append(`<div id="${respuesta.nombreUsuario}">${respuesta.nombreUsuario}</div>`);
	
	//obtenemos la sala
	let sala = JSON.parse(respuesta.sala);
	jugadores = sala.lUsuarios;
}

//si alguien abandono la sala
function seFueJugador(respuesta){
	
	//eliminamos el usuario del array
	//cogemos la posicion y lo borramos (con 1 se borra solo a el mismo que es lo que queremos)
	//se puede quitar el .find y poner usuario directamente pero tengo la sensacion que daria problemas lmao
	jugadores.splice(jugadores.indexOf(jugadores.find(x => x.nombre == respuesta.nombreUsuario)), 1);
	
	//eliminamos el div que contiene su nombre
	$("#" + respuesta.nombreUsuario).remove();
	
	//si el que lo abandono fue el dueño de la sala
	if (respuesta.nuevoPropietarioNombre){
		
		//mostramos mensaje de que x es el nuevo dueño de la sala
		$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nuevoPropietarioNombre}/" target="_blank">${respuesta.nuevoPropietarioNombre}</a><span> ${respuesta.nuevoPropietarioMensaje}</span></div>`);
		
		//si este cliente es el dueño de la sala
		if (respuesta.nuevoPropietarioNombre == nombreUsuario){
			
			//le ponemos el boton de empezar partida y el boton de abandonar sala que ocupe la mitad
			$("#opciones").append(`<button id="btnEmpezarPartida" class="btn btn-primary">Empezar partida</button>`);
			$("#btnAbandonarSala").toggleClass("full");
			
			//le ponemos el boton de expulsar usuario y ocultamos el panel por si se tenia marcado a el
			$("#infoJugador").append(`<button id="btnExpulsarJugador" class="btn btn-primary">Expulsar jugador</button>`)
				.hide();
		}
	}
}

function empezarPartida(respuesta){
	
	//si no se produjeron errores
	if (!respuesta.error){
		//replace lo que hace es que al ir para atras en el navegador en vez de ir a la sala otra vez
		//se vaya a la lista de salas
		document.location.replace(`/partida/${respuesta.idPartida}/guardarPartidaSession`);
		
	} else {
		$("#containerMensajes").append(`<div>${respuesta.error}</div>`);
	}
	
}

function expulsarJugador(respuesta){
	
	//mostramos mensaje de que x expulso a x
	$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombre}/" target="_blank">${respuesta.nombreUsuario}</a><span> ${respuesta.mensajeExpulsar}</span><a href="/perfil/${respuesta.nombreUsuarioExpulsado}/" target="_blank"> ${respuesta.nombreUsuarioExpulsado}</a></div>`);
	
	//si este cliente es el expulsado
	if (respuesta.nombreUsuarioExpulsado == nombreUsuario){
		
		//alert("Has sido expulsado de la sala");
		
		//lo echamos de la sala
		//(desonectar del socket en vez de href para que ejecute todo el proceso)
		stompClient.disconnect();
	}
	
	//si el cliente es el usuario propietario
	if (respuesta.nombreUsuario == nombreUsuario){
		//le ocultamos el div de info de jugador para que no pueda spamear el boton de echar
		$("#infoJugador").hide();
	}
}

function onConnectedChat() {
    
	//nos suscribimos a la sala
	stompClientChat.subscribe(`/salas/sala/${idSala}/chat`, onNewMessage);
	
}

function onNewMessage(payload){
	
	let respuesta = JSON.parse(payload.body);
	
	//si tiene un mensaje y no viene de un evento de abandonar la sala
	if (respuesta.mensaje && !respuesta.irse){
			
		//el perfil del usuario se abriria en una pestaña nueva al clicar sobre el nombre
		$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombreUsuario}/" target="_blank">${respuesta.nombreUsuario}</a><span>: ${respuesta.mensaje}</span></div>`);
		
		//desplazamos el scroll del chat abajo del todo
	    $("#containerMensajes").scrollTop($("#containerMensajes")[0].scrollHeight);
	}
	
	//si tiene la propiedad irse es que o se fue un jugador o le expulsaron
	if (respuesta.irse){
		//si este cliente es el que abandono
		if (respuesta.nombreUsuario == nombreUsuario){
			//lo mandamos a la lista de salas
			document.location.replace(`/salas`);
		}
	}
}