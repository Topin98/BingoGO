$(function() {

	let idPartida = $("#idPartida").val();
	
	//7 segundos
	//se pone asi para que no pete el setInterval
	let count = 700;

    let interval = setInterval(function(){

    	//si se acabo el tiempo
        if (count <= 0){
        	
        	clearInterval(interval);
        	
        	//forzamos a que entren en la partida con un carton normal
        	document.location.replace(`/partida/${idPartida}?tipo=false`);
        }

        //actualizamos el texto del elemento que indica el tiempo restante
        $("#countdown").text(count-- / 100); 
        
        //en los 3 ultimos segundos ponenos el texto en rojo y parpadeando
        if (count < 300) {
			$("#countdownContainer").css("color", "red");
			$("#countdownContainer").css("animation", "flickerAnimation 0.5s infinite");
		}

    }, 10);
    
    var paramError = window.location.search.substring(1).split('=');
    
    //si tiene mas de 1 es que hay errores
    //si tiene 1 es ""
    if (paramError.length != 1){
    	
    	//este mensaje se mostrara de forma bonita y tal
    	alert(decodeURIComponent(paramError[1]));
    	
    	//forzamos a que entren en la partida con un carton normal
    	document.location.replace(`/partida/${idPartida}?tipo=false`);
    }
    
});