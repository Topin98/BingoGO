var stompClient = null;
var stompClientChat = null;
var idSala;
var jugadores = [];

$(function() {
	
	idSala = $("#idSala").val();
	
	//Connect to WebSocket Server.
	connect();
	
	//como los divs van ir apareciendo, se declara asi el evento on click para los div hijos de listaJugadores
	//(asi nos ahorramos poner los eventos en la funcion onNewUser cada vez que se une un usuario)
	$("#listaJugadores").on("click", "div", function() {
		let jugador = jugadores.find(x => x.nombre == $(this).text());

		$("#nombreJugador").text(jugador.nombre);
		$("#puntuacionJugador").text(jugador.puntuacionTotal);
		$("#imagenJugador").attr("src", `/perfil/${jugador.nombre}/imagen`);
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
	
	//boton de empezar partida
	$("#btnEmpezarPartida").click(function(){
		stompClient.send(`/app/salas/sala/${idSala}/empezarPartida`);
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
	if (respuesta.mensaje){
		//ponemos el mensaje en el chat
		$("#containerMensajes").append(`<div>${respuesta.mensaje}</div>`);
	}
	
	switch(respuesta.tipo){
		case 0: nuevoJugador(respuesta);
			break;
		case 1: seFueJugador(respuesta);
			break;
		case 2: empezarPartida(respuesta);
			break
	}
	
}

//si alguien se unio a la sala
function nuevoJugador(respuesta){
	
	//hay que hacer un segundo parse a la propiedad usuario del json para convertirlo en json tambien
	let usuario = JSON.parse(respuesta.usuario);
	
	$("#listaJugadores").append(`<div id="${usuario.nombre}">${usuario.nombre}</div>`);
	
	//obtenemos la sala
	let sala = JSON.parse(respuesta.sala);
	jugadores = sala.lUsuarios;
}

//si alguien abandono la sala
function seFueJugador(respuesta){
	
	//hay que hacer un segundo parse a la propiedad usuario del json para convertirlo en json tambien
	let usuario = JSON.parse(respuesta.usuario);
	
	//eliminamos el usuario del array
	//cogemos la posicion y lo borramos (con 1 se borra solo a el mismo que es lo que queremos)
	//se puede quitar el .find y poner usuario directamente pero tengo la sensacion que daria problemas lmao
	jugadores.splice(jugadores.indexOf(jugadores.find(x => x.nombre == usuario.nombre)), 1);
	
	//eliminamos el div que contiene su nombre
	$("#" + usuario.nombre).remove();
}

function empezarPartida(respuesta){
	
	//replace lo que hace es que al ir para atras en el navegador en vez de ir a la sala otra vez
	//se vaya a la lista de salas
	document.location.replace(`/partida/${respuesta.idPartida}/guardarPartidaSession`);
}

function onConnectedChat() {
    
	//nos suscribimos a la sala
	stompClientChat.subscribe(`/salas/sala/${idSala}/chat`, onNewMessage);
	
}

function onNewMessage(payload){
	
	$("#containerMensajes").append(`<div>${payload.body}</div>`);
}