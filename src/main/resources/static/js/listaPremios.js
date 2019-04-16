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

    $("#containerPremios > div").click(function(){
        nombrePremio = $(this).find("h2").text();
        $("#dialog-confirm").dialog("open");
    });
});