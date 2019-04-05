//es necesario que los nombres no coincidan con los de otro js
var stompClientUtils = null;
var nombreUsuarioUtils;

$(function() {
	
	nombreUsuarioUtils = $("#nombreUsuario").val();
	
	//si no es null es que hay una sesion iniciada
	if (nombreUsuarioUtils != null) {
		
		conectar();
		
		$("#relleno").click(function(){
			stompClientUtils.send("/app/usuario/Dani", {},
					JSON.stringify({tipo: 0, mensaje: "hola"}));
		});
	}
	
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
});

function conectar() {
    
    var socketUtils = new SockJS("/ws");
    stompClientUtils = Stomp.over(socketUtils);
 
    stompClientUtils.connect({}, conectado, fallado);
}

function conectado() {
    
	stompClientUtils.subscribe(`/usuario/${nombreUsuarioUtils}`, onNewNotification);
    
}

function fallado(error) {
    console.log("Error " + error);
}

function onNewNotification(payload){
	
	let respuesta = JSON.parse(payload.body);
	
	//creamos la notificacion
	$("body").append(`<div class="notificacion">
        <div class="nombreNoti">${respuesta.nombreUsuario}</div>
        <div class="mensajeNoti">${respuesta.mensaje.substring(0, 30)} <a href="${respuesta.url}" class="btn btn-primary">Ver</a></div>
    </div>`);
	
	//la mostramos con la animacion
	$(".notificacion").animate({"right":"0"}, "slow");
	
	//a los 4 segundos
	setTimeout(function(){
		
		//la escondemos y cuando haya terminado de esconderse la eliminamos del dom
        $(".notificacion").animate({"right":"-500"}, "slow", function(){
        	$(this).remove();
        });
    }, 4000);
	
}