$(function() {
	
	//tooltips con el nombre de usuario para cada foto
	$('[data-toggle="tooltip"]').tooltip();
	
	//dialogo para reportar un jugador
    $("#dialogoReport").dialog({
    	autoOpen: false,
        resizable: false,
        height: "auto",
        width: 400,
        height: 230,
        modal: true
    });
    
    
    $("#btnReportar").click(function(){
    	
    	$("#dialogoReport").dialog("open");
    	
    });
});