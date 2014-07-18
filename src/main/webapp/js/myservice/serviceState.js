function changeState(id, name){
	$("#state").val(id);
	$("#stateName").val(name);
	
	$("#serviceState").hide("fast");
	query();
}
function closState(){
	$("#serviceState").hide('fast');
}

function displayState(){
	$("#stateBtn").focus();
	$("#serviceState").toggle("fast");
}