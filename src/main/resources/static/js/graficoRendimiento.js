
let miGraficaRendt;
mostrarDatos();

$("input:checkbox").on('change', mostrarDatos);

function mostrarDatos() {
	let curso = document.getElementById("curso").innerText;
	if ($("#bloqueOpSimples").is(":checked")) {
		bloque_1_Rendt(curso);
		return;
	}
	if ($("#bloqueRentas").is(":checked")) {
		bloque_2_Rendt(curso);
		return;
	}
	if ($("#bloquePrestamos").is(":checked")) {
		bloque_3_Rendt(curso);
		return;
	}
	else{
			
	}
}

function  bloque_1_Rendt(curso){
	var urlBloque1 = '/profesor/getDatosRendimientoBloque1/'+ encodeURI(curso);
	$.getJSON(urlBloque1, function(dataB1){
		if ($("#bloqueRentas").is(":checked")) {
			datosB2(dataB1);
			return;	
		}
		if ($("#bloquePrestamos").is(":checked")) {
			datosB3(dataB1);
			return;	
		}
		function datosB2(dataB1){
			var urlBloque2 = '/profesor/getDatosRendimientoBloque2/'+ encodeURI(curso);
			$.getJSON(urlBloque2, function(dataB2){
				if ($("#bloquePrestamos").is(":checked")) {
					datosB3(dataB1,dataB2);
					return;	
				}
				function datosB3(){
					var urlBloque3 = '/profesor/getDatosRendimientoBloque3/'+ encodeURI(curso);
					$.getJSON(urlBloque3, function(dataB3){
						dibujarR(["Bloque Op. Simples","Bloque Rentas","Bloque Préstamos"],[dataB1,dataB2,dataB3]);
					});
				}
				
				dibujarR(["Bloque Op. Simples","Bloque Rentas"],[dataB1,dataB2]);
			});
		}
		function datosB3(dataB1){
			var urlBloque3 = '/profesor/getDatosRendimientoBloque3/'+ encodeURI(curso);
			$.getJSON(urlBloque3, function(dataB3){
				dibujarR(["Bloque Op. Simples","Bloque Préstamos"],[dataB1,dataB3]);
			});
		}
		var urlBloque1_tipos = '/profesor/getDatosRendimientoBloque1_tipos/'+ encodeURI(curso);
		$.getJSON(urlBloque1_tipos, function(dataB1){
			dibujarR(["Tantos Equivalentes","Capitalizacion Simple","Capitalizacion Compuesta","Descuento Simple","Descuento Compuesto","Letras del Tesoro y Letras de Cambio"],dataB1);
		});
	});
}
function  bloque_2_Rendt(curso){
	var urlBloque2 = '/profesor/getDatosRendimientoBloque2/'+ encodeURI(curso);
	$.getJSON(urlBloque2, function(dataB2){
		if ($("#bloquePrestamos").is(":checked")) {
			datosB3(dataB2);
			return;	
		}
		function datosB3(dataB2){
			var urlBloque3 = '/profesor/getDatosRendimientoBloque3/'+ encodeURI(curso);
			$.getJSON(urlBloque3, function(dataB3){
				
				dibujarR(["Bloque Rentas","Bloque Préstamos"],[dataB2,dataB3]);
			});
		}
		var urlBloque2_tipos = '/profesor/getDatosRendimientoBloque2_tipos/'+ encodeURI(curso);
		$.getJSON(urlBloque2_tipos, function(dataB2){
			dibujarR(["Rentas postpagables y prepagables","Operaciones Financieras Compuestas"],dataB2);
		});
	});
}
function  bloque_3_Rendt(curso){
	var urlBloque3_tipos = '/profesor/getDatosRendimientoBloque3_tipos/'+ encodeURI(curso);
	$.getJSON(urlBloque3_tipos, function(dataB3){
		
		dibujarR(["Préstamos Francés","Leasing"],dataB3);
	});
}


function dibujarR(x,y){
	if (miGraficaRendt) {
		miGraficaRendt.destroy();
	}
	var xValues = x;
	var yValues = y;
	var barColors = ["#00508C","#267CBF","#72ACD8"];
	miGraficaRendt = new Chart("grafico5", {
	  type: "doughnut",
	  data: {
	    labels: xValues,
	    datasets: [{
			backgroundColor: barColors,
			data: yValues
	    }]
	  },
	  options: {
		responsive: true,
	    maintainAspectRatio: false,
		plugins: {
		    legend:{
		    	display: true,
				position: "right"
		    }
		}
	  }
	});
}