//es necesario que los nombres no coincidan con los de otro js
var stompClientUtils = null;
var nombreUsuarioUtils;

$(function() {
	
	nombreUsuarioUtils = $("#nombreUsuario").val();
	
	//si no es null es que hay una sesion iniciada
	if (nombreUsuarioUtils != null) {
		
		conectar();
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
	
	//obtenemos el valor del parametro error
	var paramE = getUrlParameter("e");
    if (paramE){
		
    	$("body").append(`<div id="dialogoE" title="Error">
				    			<p>${decodeURIComponent(paramE).replace(/<|>|\"|'|`/g,"")}</p>
				   		  </div> `);
    	
    	$("#dialogoE").dialog({
	        resizable: false,
	        height: "auto",
	        width: 400,
	        modal: true
	    });
    }
    
	var paramExito = getUrlParameter("exito");
    if (paramExito){
    	
    	$("body").append(`<div id="dialogoE" title="Mensaje">
				    			<p>${decodeURIComponent(paramExito)}</p>
				   		  </div> `);
    	
    	$("#dialogoE").dialog({
	        resizable: false,
	        height: "auto",
	        width: 400,
	        modal: true
	    });
    }
    
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

function getUrlParameter(sParam) {
	
    var sURLVariables = window.location.search.substring(1).split('&');

    for (let i = 0; i < sURLVariables.length; i++) {
        let sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}