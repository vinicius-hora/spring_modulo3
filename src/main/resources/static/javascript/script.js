/**
 * 
 */
function validarCampos(){
	
	numero = document.getElementById('numero').value;
	tipo = document.getElementById('tipo').value;
	
	if (numero == null || numero === ""){
		
		alert('Número deve ser informado');
		
		return false;
	}
	else if(tipo === "" || tipo == null){
		
		alert('tipo deve ser informado');
		return false;
	}
	
	return true;
}