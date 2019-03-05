var stompClient = null;
var idSala;

$(function() {
	
	//Connect to WebSocket Server.
	connect();
	
	idSala = $("#idSala").val();
	
});


function connect() {
      
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
 
    stompClient.connect({}, onConnected, onError);
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
	
	let respuesta = JSON.parse(payload.body);
	
	//ponemos el mensaje en el chat
	$("#chat").append(`<div>${respuesta.mensaje}</div>`);
	
	//si alguien se unio
	if (respuesta.tipo == 0){
		$("#listaJugadores").append(`<div id="${respuesta.nombreUsuario}">${respuesta.nombreUsuario}</div>`)
		
		//si no es que se fue
	} else {
		$("#" + respuesta.nombreUsuario).remove();
	}
}