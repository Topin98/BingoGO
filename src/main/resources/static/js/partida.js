var stompClient = null; //socket para los numeros de la tombola
var stompClientChat = null; //socket para los mensajes del chat
var stompClientPosiciones = null; //socket para los numeros restantes del carton de cada jugador
var idPartida; //id de la partida que estamos
var numerosTombola = []; //array de numeros que va soltando la tombola
var odometer; //diseño de los numeros que salen de la tombola

$(function() {
	
	idPartida = $("#idPartida").val();
	
	//nos conectamos al socket
	connect();
	
	//formulario con el input del chat
	var inpMensaje = $("#frmEnviarMensaje > input");
	$("#frmEnviarMensaje").submit(function(){
		
		let mensaje = inpMensaje.val().trim();
		
		if (mensaje) {
			stompClientChat.send(`/app/partida/${idPartida}/chat/enviarMensaje`, {}, mensaje);
			inpMensaje.val("");
		}
		
		return false;
	});
	
	//controlador de los botones de los numeros
	$(".containerCarton > button.num").click(function(){
		
		if (numerosTombola.includes(parseInt($(this).text()))){
			$(this).attr("data-marcado", true)
				.addClass("crossed")
				.css("background-color", "#C0C0C0")
				.css("color", "black")
				.off(); //quita el onclick del boton
			
			//indicamos cuantos numeros nos quedan por tachar
			stompClientPosiciones.send(`/app/partida/${idPartida}/accionesCarton`,
										{},
										JSON.stringify({tipo: 0, numerosRestantes: $(".containerCarton > button[data-marcado!=true]").length}));
		}
	});
	
	$("#btnLinea").click(function(){
		
		//primera linea
		if (Array.from($(".containerCarton > button:lt(9)")).filter(x => filtrar(x)).length == 9
				
				//segunda linea
				|| Array.from($(".containerCarton > button:nth-child(n+10):nth-child(-n+18)")).filter(x => filtrar(x)).length == 9
				
				//tercera linea
				|| Array.from($(".containerCarton > button:nth-child(n+19)")).filter(x => filtrar(x)).length == 9){
			
			//indicamos cuantos numeros nos quedan por tachar
			stompClientPosiciones.send(`/app/partida/${idPartida}/accionesCarton`,
										{},
										JSON.stringify({tipo: 1, numerosTombola: numerosTombola}));
			
			$(this).off();
		}
		
	});
	
	$("#btnBingo").click(function(){
		
		//aqui no hace falta aplicar el filtro, se puede poner directamente en el selector
		if ($(".containerCarton > button[data-marcado=true]").length == 27){
			
			//indicamos cuantos numeros nos quedan por tachar
			stompClientPosiciones.send(`/app/partida/${idPartida}/accionesCarton`,
										{},
										JSON.stringify({tipo: 2, numerosTombola: numerosTombola}));
			
			$(this).off();
		}
	});
	
});

//filtra los botones que ya esten marcados (tambien cuentan los que no tenian numeros)
function filtrar(x){
	return $(x).attr("data-marcado") == "true";
}

function connect() {
	
	//socket para recibir los numeros de la tombola
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    
    //socket para recibir los numeros de la tombola
    var socketPosiciones = new SockJS("/ws");
    stompClientPosiciones = Stomp.over(socketPosiciones);
    stompClientPosiciones.connect({}, onConnectedPosiciones, onError);
    
    //socket para cuando mandamos o recibimos un mensaje en el chat
    var socketChat = new SockJS("/ws");
    stompClientChat = Stomp.over(socketChat);
    stompClientChat.connect({}, onConnectedChat, onError);
}

function onConnected() {
    
	//nos suscribimos a la tombola de la partida
    stompClient.subscribe(`/partida/${idPartida}/tombola`, onNewNumber);
}

function onError(error) {
	console.log("Error " + error);
}

function onNewNumber(payload){
	
	try{
		//si podemos parsearlo a JSON es que llega el array de numeros
		numerosTombola = JSON.parse(payload.body);
		
		//el ultimo numero es el que va a mostrar la tombola
		$("#odometer").html(numerosTombola[numerosTombola.length - 1]);
		
		//filtramos los numeros que no se han puesto ya en el historial
		let listaFiltrada = numerosTombola.filter(x => $("#" + x).length == 0);
		
		//los añadimos al div contenedor
		//con foreach por si acaso se unio a la pantalla de la partida un poco tarde
		listaFiltrada.forEach(numero => {
			
			//añadimos el numero al div contenedor
            $("#historialNumeros").append(`<div id="${numero}">${numero}</div>`);
		});
		
		//desplazamos el scroll a la derecha del todo
        $("#historialNumeros").scrollLeft($("#historialNumeros")[0].scrollWidth);
		
		//si no es que ya acabo la partida
	} catch (error){
		$("#odometer").html(0);
		$("#numeros").text("Partida terminada");
		$("#aVolverSala").show();
	}
	
}

function onConnectedChat() {
    
	//nos suscribimos a la sala
	stompClientChat.subscribe(`/partida/${idPartida}/chat`, onNewMessage);
	
}

function onNewMessage(payload){
	
	let respuesta = JSON.parse(payload.body);
	
	if (respuesta.mensaje){
		
		//el perfil del usuario se abriria en una pestaña nueva al clicar sobre el nombre
		$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombreUsuario}/" target="_blank">${respuesta.nombreUsuario}</a><span>: ${respuesta.mensaje}</span></div>`);
		
		//desplazamos el scroll del chat abajo del todo
	    $("#containerMensajes").scrollTop($("#containerMensajes")[0].scrollHeight);
	}
	
}

function onConnectedPosiciones(){
	
	//nos suscribimos a la escucha de los numeros restantes de cada jugador
	stompClientPosiciones.subscribe(`/partida/${idPartida}`, actualizarPosiciones);
	
	//indicamos cuantos numeros tiene nuestro carton
	stompClientPosiciones.send(`/app/partida/${idPartida}/accionesCarton`,
			{},
			JSON.stringify({numerosRestantes: $(".containerCarton > button[data-marcado!=true]").length,
							tipo: 0}));	
}

function actualizarPosiciones(payload){
	
	let respuesta = JSON.parse(payload.body);
	
	switch (respuesta.tipo){
		//alguien acerto un numero
		case 0: $("#" + respuesta.nombreUsuario)
				.text(`${respuesta.nombreUsuario}`)
				.siblings("span").text(`Numeros restantes: ${respuesta.numerosRestantes}`);
			break;
		//alguien hizo linea
		case 1: 
			if (respuesta.mensaje) {
				$("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombreUsuario}/" target="_blank">${respuesta.nombreUsuario}</a><span> ${respuesta.mensaje}</span></div>`);
			}
			break;
		//alguien hizo bingo
		case 2:
			if (respuesta.mensaje) $("#containerMensajes").append(`<div><a href="/perfil/${respuesta.nombreUsuario}/" target="_blank">${respuesta.nombreUsuario}</a><span> ${respuesta.mensaje}</span></div>`);
				
			//al que hizo bingo se le muestra el enlace de volver a la lista de salas
			if ($("#nombreUsuario").val() == respuesta.nombreUsuario){
				$("#aVolverSalas").show();
			}
			
			break;
	}
	
	//desplazamos el scroll del chat abajo del todo
    $("#containerMensajes").scrollTop($("#containerMensajes")[0].scrollHeight);
	
	
}
