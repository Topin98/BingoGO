var nombrePremio;

$(function () {

    $("#dialog-confirm").dialog({
        autoOpen: false,
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "No": function () {
                $(this).dialog("close");
            },
            "Sí": function () {
            	document.location.replace(`/premios/${nombrePremio}/canjear`);
            	$(this).dialog("close");
            	$(".loading").show();
            }
        }
    });

    $(".btnComprar").click(function(){
        nombrePremio = $(this).siblings("h2").text();
        $("#dialog-confirm").dialog("open");
    });
    
    $("#dialogoEditar").dialog({
        autoOpen: false,
        resizable: false,
        height: "auto",
        width: 470,
        modal: true
    });
    
    $(".btnEditar").click(function() {
        $("#dialogoEditar").dialog("open");
        
        //nuevo titulo del dialog
        $(".ui-dialog-title").text("Editar premio");
        
        //rellenamos los valores necesarios del form */
        $(".titulo").show(); //mostramos el form de la imagen
        $("#imagenPremioDialog").attr("src", `/premios/${$(this).siblings("h2").text()}/imagen`);
        $("#nombrePremioDialog").val($(this).siblings("h2").text());
        $("#desPremioDialog").val($(this).siblings("p").text());
        $("#precioPremioDialog").val($(this).siblings("button").data("precio"));
        $("#activoPremioDialog").val($(this).siblings(".premioActivo").val());
        $("#idPremioDialog").val($(this).siblings(".idPremio").val());
        
        $("#idPremioDialogImagen").val($(this).siblings(".idPremio").val());
    });
    
    $(".btnNuevoPremio").click(function() {
        $("#dialogoEditar").dialog("open");
        
        //nuevo titulo del dialog
        $(".ui-dialog-title").text("Nuevo premio");
        
        //limpiamos los inputs del form por si se edito anteriormente un premio
        $(".titulo").hide(); //ocultamos el form de la imagen
        
        $("#nombrePremioDialog").val("");
        $("#desPremioDialog").val("");
        $("#precioPremioDialog").val("");
        $("#activoPremioDialog").val("true");
        $("#idPremioDialog").val(0); //id cero para que no de error, cuando se inserta se pone el que toca automaticamente
    });
    
    $("input[type=file]").change(function(){
        
        if (this.files && this.files[0]) {
            var reader = new FileReader();
    
            reader.readAsDataURL(this.files[0]);

            reader.onload = function (e) {
                $("#imagenPremioDialog").attr("src", e.target.result);
            }
        }
    });
});