package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.apache.poi.ss.formula.functions.Irr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.models.Alumno;
import com.example.demo.models.Curso;
import com.example.demo.models.Ejercicio;
import com.example.demo.models.Respuesta;
import com.example.demo.repositories.RepositorioAlumno;
import com.example.demo.repositories.RepositorioEjercicios;
import com.example.demo.repositories.RepositorioRespuesta;
import com.example.demo.repositories.RepositorioUsuario;
import com.example.demo.services.ServicioUsuario;


@Controller
public class ControladorAlumno {

	@Autowired
	private ServicioUsuario usuarioService;
	@Autowired
	private RepositorioUsuario repoUsuarios;
	@Autowired
	private RepositorioEjercicios repoEjercicios;
	@Autowired
	private RepositorioRespuesta repoRespuestas;
	@Autowired
	private RepositorioAlumno repoAlumnos;
	
	@RequestMapping("/home")
	public String PaginaPrincipal(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("usuario", alumno);
		return "Pagina_Principal_Alumnos";
	}
	
	@RequestMapping("/asignarCurso")
	public String asignarProfesor(@RequestParam String codigo) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		if (usuarioService.asignarCurso(alumno, codigo)) {
			return "redirect:/home";
		}
		return "redirect:/home?error";
	}
	
	@RequestMapping("/perfil")
	public String verPerfilUsuario(@RequestParam String nombreUsuario, Model model) {
		Alumno alumno= (Alumno) repoUsuarios.findByNombreUsuario(nombreUsuario);
		
		model.addAttribute("usuario",alumno);
		model.addAttribute("controller","");
		return "Perfil_Usuario";
	}
	
	@RequestMapping("/ranking")
	public String montarRanking(Model model) {
		Alumno alumno= (Alumno) usuarioService.obtenerUsuarioActual();
		
		Curso c = alumno.getCurso();
		
		model.addAttribute("usuario",alumno);
		
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("mejores",repoAlumnos.findTop5ByCursoOrderByDineroDesc(c));
		if (!repoAlumnos.findTop5ByCursoOrderByDineroDesc(c).contains(alumno)) {
			model.addAttribute("enTop", "No"); 
		}
		return "Pagina_Ranking";
	}
	
	@RequestMapping("/DashboardAlumno")
	public String DashboardAlumno(Model model) {
		Alumno alumno= (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		return "Dashboard_Alumnos";
	}
	
	@RequestMapping("/cambioContrase??a")
	public String CambioContrase??Usuario(@RequestParam String contrase??aVieja, @RequestParam String contrase??aNueva1, @RequestParam String contrase??aNueva2, Model model) {
		Alumno alumno= (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		if(usuarioService.cambiarContrase??a(alumno, contrase??aVieja, contrase??aNueva1, contrase??aNueva2)) {
			return "redirect:/perfil?nombreUsuario="+alumno.getNombreUsuario()+"&exito";
		}
		return "redirect:/perfil?nombreUsuario="+alumno.getNombreUsuario()+"&error";
	}
	
	@RequestMapping("/bloqueOperacionesSimples")
	public String mostrarBloqueOperacionesSimples(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		return "Bloque_Operaciones_Simples";
	}
	
	@RequestMapping("/bloqueRentas")
	public String mostrarBloqueRentas(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		return "Bloque_Rentas";
	}
	
	@RequestMapping("/bloquePrestamos")
	public String mostrarBloquePrestamos(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		return "Bloque_Prestamos";
	}
	
	@RequestMapping("/listaDeObjetivos")
	public String mostrarListaDeObjetivos(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		return"Lista_Objetivos";
	}
	
	@RequestMapping("/objetivo1")
	public String mostrarObjetivo1(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		String e11,e12,e13;
		int p1 = 265000;
		int n1 = 30;
		double i1 = 2;
		double sol1 = 0.0;
		List<String> opciones1 = new ArrayList<>();
		
		e11 = "Acaba de conocer a la persona con la que va a compartir el resto de su vida. Esa persona es de Francia y como tal le gustar??a seguir viviendo en su pa??s natal, as?? que usted se pone manos a la obra y comienza a buscar una casa acorde para hacerla feliz.";
		e12 = "Tras varios meses de b??squeda encuentra una casa al sur de Francia, no muy lejos de los Alpes, valorada en 265.000???. A ambos les encanta la casa y sus vistas, pero no pueden hacer frente a tal cantidad de dinero y buscan una fuente de financiaci??n.";
		e13 = "Tras visitar varias entidades bancarias, una de ellas, Votre Banque d??argent, os ofrece el siguiente contrato hipotecario: un cr??dito por el importe total de la casa a pagar en 30 a??os a un tipo de inter??s efectivo anual del 2%. Dicho pr??stamo se amortizar??a aplicando el m??todo franc??s y con el pago de las cuotas mensualmente.";

		opciones1.add(Precision.round((p1*i1/100)/(1-Math.pow(1+i1/100, -n1)),2)+" ???/mes");
		i1 = Math.pow(1+i1/100, (double)1/12)-1;
		opciones1.add(Precision.round((p1*i1)/(1-Math.pow(1+i1, -n1)),2)+" ???/mes");
		n1 = n1*12;
		sol1 = Precision.round((p1*i1)/(1-Math.pow(1+i1, -n1)),2);
		opciones1.add(sol1+" ???/mes");
		opciones1.add(Precision.round((p1*i1)/(1-Math.pow(-i1, n1)),2)+" ???/mes");
		
		model.addAttribute("enunciado11", e11);
		model.addAttribute("enunciado12", e12);
		model.addAttribute("enunciado13", e13);
		Collections.shuffle(opciones1);
		model.addAttribute("opciones1",opciones1);
		model.addAttribute("ids1",sol1*43/34);
		
		//--------------------------------------------------------------\\
		
		String e21,e22,e23;
		int p2 = 199000;
		int n2 = 20;
		double i2 = 3.12;
		double sol2 = 0.0;
		List<String> opciones2 = new ArrayList<>();
		
		e21 = "Le acaban de despedir de su trabajo actual, pero tiene un CV muy demando en el mercado y pronto le salen nuevas ofertas.";
		e22 = "Una de estas ofertas te ha parecido muy interesante y podr??a servirte para un cambio de aires. Dicha oferta es de una empresa italiana, Consulttitoria S.A., y le ofrecen una vacante como m??nager financiero en su firma cobrando 3.210???/mes m??s otros grandes beneficios. Al ver dicha oferta decide aceptar y realizar la entrevista con la compa????a y al final acaba siendo contratado indefinidamente. Tras 4 a??os viviendo en Italia en una casa alquilada, decide dar el salto y comprarse su casa propia. ";
		e23 = "Tras buscar, encuentra un peque??o piso cerca de su lugar de trabajo a un precio asequible, por 199.000???. Como buen financiero que eres decides buscar financiaci??n. Y encuentras un banco, Soldi Facili, que le acaba ofreciendo el siguiente contrato: un pr??stamo por el valor del piso a pagar en 20 a??os, con pagos trimestrales, a un tipo de inter??s efectivo del 3,12% anual. Suponiendo que estas en Italia, se aplica el m??todo de amortizaci??n italiano.";

		opciones2.add(Precision.round((p2*i2/100)+(p2/n2),2) +" ???/trimestre");
		opciones2.add(Precision.round((p2*i2/100)+(p2/n2*4),2) +" ???/trimestre");
		i2 = Math.pow(1+i2/100, (double)1/4)-1;
		sol2 = Precision.round((p2*i2)+(p2/(n2*4)),2);
		opciones2.add(sol2 +" ???/trimestre");
		opciones2.add(Precision.round((p2*i2)+(p2/n2),2) +" ???/trimestre");
		
		model.addAttribute("enunciado21", e21);
		model.addAttribute("enunciado22", e22);
		model.addAttribute("enunciado23", e23);
		Collections.shuffle(opciones2);
		model.addAttribute("opciones2",opciones2);
		model.addAttribute("ids2",sol2*43/34);
		
		//--------------------------------------------------------------\\
		
		String e31,e32,e33;
		int p3 = 134500;
		int n3 = 5;
		double i3 = 5;
		double sol3 = 0.0;
		List<String> opciones3 = new ArrayList<>();
		
		e31 = "Te ha ido bastante bien en la vida desde que empezaste a trabajar en EE. UU., llegando a cumplir as?? tu sue??o americano.";
		e32 = "Ahora le llega el turno a tu hermano menor, justa acaba de encontrar una oportunidad de trabajo en EE. UU. y tu decides ayudarle en su estancia. La pega es que el chico tiene su nuevo empleo en la Costa Este y tu est??s situado en la Costa Oeste. Pero a??n as?? no lo consideras un problema, ya que tienes suficiente dinero para comprarle una casa tu hermano peque??o.";
		e33 = "El chico ha encontrado un apartamento a su medida por el modesto precio de 134.500???. A ti el dinero no es lo que m??s te preocupa, pero en vez de compr??rselo al contado decides financiarlo.\r\n"
				+ "De entre las propuestas de financiaci??n que hab??is encontrado y que m??s os ha interesado, es la siguiente: el banco CashNoMoney, decide financiaros el apartamento con un pr??stamo a devolver en 5 a??os al 5% efectivo anual, amortizado bajo el m??todo americano.";

		opciones3.add(Precision.round((p3*i3/100)+(p3/n3),2) +" ???");
		opciones3.add(Precision.round((p3*i3/100)+(p3/n3*4),2) +" ???");
		sol3 = Precision.round(((p3*i3/100)*n3)+(p3),2);
		opciones3.add(sol3 +" ???");
		opciones3.add(Precision.round((p3*i3/100)+(p3),2) +" ???");
		
		model.addAttribute("enunciado31", e31);
		model.addAttribute("enunciado32", e32);
		model.addAttribute("enunciado33", e33);
		Collections.shuffle(opciones3);
		model.addAttribute("opciones3",opciones3);
		model.addAttribute("ids3",sol3*43/34);
		

		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("tipoObjetivo", "Financiar Casa");
		
		return"Objetivos_1";
	}
	
	@RequestMapping("/objetivo2")
	public String mostrarObjetivo2(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		String e11,e12;
		int p1 = 100;
		int n1 = 20;
		double i1 = 2.7;
		double sol1 = 0.0;
		List<String> opciones1 = new ArrayList<>();
		
		e11 = "Con la crisis, te preocupa que cuando se jubile, la pensi??n no le alcance, debido a eso decides abrir una cuenta de ahorros.";
		e12 = "El Banco SANTARDE, acepta que realices dep??sitos al final de cada mes por valor de 100???, hasta que se jubile (en 20 a??os) aplic??ndole un tipo de inter??s compuesto del 2,7% anual.";

		opciones1.add(Precision.round((p1)*((Math.pow(1+i1/100, n1)-1)/(i1/100)),2)+" ???");
		i1 = Math.pow(1+i1/100, (double)1/12)-1;
		opciones1.add(Precision.round((p1)*((Math.pow(1+i1, n1)-1)/(i1)),2)+" ???");
		n1 = n1*12;
		sol1 = Precision.round((p1)*((Math.pow(1+i1, n1)-1)/(i1)),2);
		opciones1.add(sol1+" ???");
		opciones1.add(Precision.round((p1)*((Math.pow(1+i1, n1))/(i1)),2)+" ???");
		
		model.addAttribute("enunciado11", e11);
		model.addAttribute("enunciado12", e12);
		Collections.shuffle(opciones1);
		model.addAttribute("opciones1",opciones1);
		model.addAttribute("ids1",sol1*43/34);
		
		//--------------------------------------------------------------\\
		
		String e21,e22;
		int p2 = 200;
		int n2 = 18;
		double i2 = 1;
		double sol2 = 0.0;
		List<String> opciones2 = new ArrayList<>();
		
		e21 = "Con el nacimiento de su primer hijo, quiere garantizarle una educaci??n de excelencia, y has decido abrir una cuenta de ahorros donde ingresar?? al finalizar cada mes 200??? hasta que el chico entre a la universidad.";
		e22 = "Para ello, acude a su banco de confianza CasiBank, y le ofrecen capitalizar dichos dep??sitos al 1% efectivo anual.";

		opciones2.add(Precision.round((p2)*((Math.pow(1+i2/100, n2)-1)/(i2/100)),2)+" ???");
		i2 = Math.pow(1+i2/100, (double)1/12)-1;
		opciones2.add(Precision.round((p1)*((Math.pow(1+i2, n2)-1)/(i2)),2)+" ???");
		n2 = n2*12;
		sol2 = Precision.round((p2)*((Math.pow(1+i2, n2)-1)/(i2)),2);
		opciones2.add(sol2+" ???");
		opciones2.add(Precision.round((p2)*((Math.pow(1+i2, n2))/(i2)),2)+" ???");
		
		model.addAttribute("enunciado21", e21);
		model.addAttribute("enunciado22", e22);
		Collections.shuffle(opciones2);
		model.addAttribute("opciones2",opciones2);
		model.addAttribute("ids2",sol2*43/34);
		
		//--------------------------------------------------------------\\
		
		String e31,e32;
		int p3 = 5000;
		int n3 = 7;
		double i3 = 2;
		double sol3 = 0.0;
		List<String> opciones3 = new ArrayList<>();
		
		e31 = "Desea abrir una cuenta de ahorro para poder comprar un nuevo coche, para ello decides aportar al final de cada semestre 5.000???.";
		e32 = "Para ello acuerdas con el banco BEBEVA, realizar la operaci??n durante 7 a??os a un tipo del 2 % anual.";

		opciones3.add(Precision.round((p3)*((Math.pow(1+i3/100, n3)-1)/(i3/100)),2)+" ???");
		i3 = Math.pow(1+i3/100, (double)1/2)-1;
		opciones3.add(Precision.round((p3)*((Math.pow(1+i3, n3)-1)/(i3)),2)+" ???");
		n3 = n3*2;
		sol3 = Precision.round((p3)*((Math.pow(1+i3, n3)-1)/(i3)),2);
		opciones3.add(sol3+" ???");
		opciones3.add(Precision.round((p3)*((Math.pow(1+i3, n3))/(i3)),2)+" ???");
		
		model.addAttribute("enunciado31", e31);
		model.addAttribute("enunciado32", e32);
		Collections.shuffle(opciones3);
		model.addAttribute("opciones3",opciones3);
		model.addAttribute("ids3",sol3*43/34);
		

		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("tipoObjetivo", "Cuenta de Ahorros");
		
		
		return"Objetivos_2";
	}
	
	@RequestMapping("/objetivo3")
	public String mostrarObjetivo3(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("tipoObjetivo", "Letras del Tesoro");
		return"Objetivos_3";
	}
		
	@RequestMapping("/objetivo31")
	public String mostrarObjetivo3_1(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		String c1;
		int nc1 = 6;
		double solc1 = 0.0;
		List<String> opcionesc1 = new ArrayList<>();
		
		c1 = "Se suscribe una Letra del Tesoro de 1.000 ??? con vencimiento a 6 meses. El precio de compra es de 950 ???, con una comisi??n de 5 ???.";
		
		solc1 = Precision.round(((((double)1000/(double)945)-1)/0.5)*100,2);
		opcionesc1.add(solc1+" %");
		opcionesc1.add(Precision.round(((((double)1000/(double)945)-1)/(nc1))*100,2)+" %");
		opcionesc1.add(Precision.round(((((double)1000/(double)950)-1)/0.5)*100,2)+" %");
		opcionesc1.add(Precision.round(((((double)1000/(double)950)-1)/(nc1))*100,2)+" %");
		
		model.addAttribute("enunciadoc1", c1);
		Collections.shuffle(opcionesc1);
		model.addAttribute("opcionesc1",opcionesc1);
		model.addAttribute("idsc1",solc1*43/34);
		
		//--------------------------------------------------------------\\
		
		String c2;
		double solc2 = 0.0;
		List<String> opcionesc2 = new ArrayList<>();
		
		c2 = "Se suscribe una Letra del Tesoro de 1.000 ??? con vencimiento a 12 meses. El precio de compra es de 950 ???, con una comisi??n de 5 ???.";
		
		solc2 = Precision.round(((((double)1000/(double)945)-1)*100),2);
		opcionesc2.add(solc2+" %");
		opcionesc2.add(Precision.round(((((double)1000/(double)950)-1)*100),2)+" %");
		opcionesc2.add(Precision.round((((Math.pow(((double)1000/(double)945),12))-1)*100),2)+" %");
		opcionesc2.add(Precision.round((((Math.pow(((double)1000/(double)950),12))-1)*100),2)+" %");
		
		model.addAttribute("enunciadoc2", c2);
		Collections.shuffle(opcionesc2);
		model.addAttribute("opcionesc2",opcionesc2);
		model.addAttribute("idsc2",solc2*43/34);
		
		//--------------------------------------------------------------\\
		
		String c3;
		double solc3 = 0.0;
		List<String> opcionesc3 = new ArrayList<>();
		
		c3 = "Se suscribe una Letra del Tesoro de 1.000 ??? con vencimiento a 18 meses. El precio de compra es de 950 ???, con una comisi??n de 5 ???.";
		
		solc3 = Precision.round((((Math.pow(((double)1000/(double)945),0.6666666))-1)*100),2);
		opcionesc3.add(solc3+" %");
		opcionesc3.add(Precision.round(((((double)1000/(double)950)-1)*100),2)+" %");
		opcionesc3.add(Precision.round((((Math.pow(((double)1000/(double)945),1.5))-1)*100),2)+" %");
		opcionesc3.add(Precision.round((((Math.pow(((double)1000/(double)950),1.5))-1)*100),2)+" %");
		
		model.addAttribute("enunciadoc3", c3);
		Collections.shuffle(opcionesc3);
		model.addAttribute("opcionesc3",opcionesc3);
		model.addAttribute("idsc3",solc3*43/34);
		
		//--------------------------------------------------------------\\
		
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("tipoObjetivo", "Letras del Tesoro (Compra)");
		
		return"Objetivos_3_1";
	}
	
	@RequestMapping("/objetivo32")
	public String mostrarObjetivo3_2(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		String v1;
		int nv1 = 4;
		double solv1 = 0.0;
		List<String> opcionesv1 = new ArrayList<>();
		
		v1 = "Pasados 4 meses decide vender su Letra del Tesoro a otro inversor.\r\n"
				+ "Las condiciones de la operaci??n son de: 975??? el precio de venta y 3??? en concepto de comisiones.";
		
		solv1 = Precision.round(((((double)972/(double)945)-1)/(0.333333))*100,2);
		opcionesv1.add(solv1+" %");
		opcionesv1.add(Precision.round(((((double)975/(double)945)-1)/(nv1))*100,2)+" %");
		opcionesv1.add(Precision.round(((((double)972/(double)950)-1)/(0.333333))*100,2)+" %");
		opcionesv1.add(Precision.round(((((double)972/(double)950)-1)/(nv1))*100,2)+" %");
		
		model.addAttribute("enunciadov1", v1);
		Collections.shuffle(opcionesv1);
		model.addAttribute("opcionesv1",opcionesv1);
		model.addAttribute("idsv1",solv1*43/34);
		
		//--------------------------------------------------------------\\
		
		String v2;
		int nv2 = 5;
		double solv2 = 0.0;
		List<String> opcionesv2 = new ArrayList<>();
		
		v2 = "Pasados 5 meses decide vender su Letra del Tesoro a otro inversor.\r\n"
				+ "Las condiciones de la operaci??n son de: 965??? el precio de venta y 3??? en concepto de comisiones.";
		
		solv2 = Precision.round(((((double)962/(double)945)-1)/(0.4166666))*100,2);
		opcionesv2.add(solv2+" %");
		opcionesv2.add(Precision.round(((((double)965/(double)945)-1)/(nv2))*100,2)+" %");
		opcionesv2.add(Precision.round(((((double)962/(double)950)-1)/(0.4166666))*100,2)+" %");
		opcionesv2.add(Precision.round(((((double)962/(double)950)-1)/(nv2))*100,2)+" %");
		
		model.addAttribute("enunciadov2", v2);
		Collections.shuffle(opcionesv2);
		model.addAttribute("opcionesv2",opcionesv2);
		model.addAttribute("idsv2",solv2*43/34);
		
		//--------------------------------------------------------------\\
		
		String v3;
		int nv3 = 10;
		double solv3 = 0.0;
		List<String> opcionesv3 = new ArrayList<>();
		
		v3 = "Pasados 10 meses decide vender su Letra del Tesoro a otro inversor.\r\n"
				+ "Las condiciones de la operaci??n son de: 985??? el precio de venta y 6??? en concepto de comisiones.";
		
		solv3 = Precision.round(((((double)979/(double)945)-1)/(0.833333))*100,2);
		opcionesv3.add(solv3+" %");
		opcionesv3.add(Precision.round(((((double)985/(double)945)-1)/(nv3))*100,2)+" %");
		opcionesv3.add(Precision.round(((((double)985/(double)950)-1)/(0.833333))*100,2)+" %");
		opcionesv3.add(Precision.round(((((double)979/(double)950)-1)/(nv3))*100,2)+" %");
		
		model.addAttribute("enunciadov3", v3);
		Collections.shuffle(opcionesv3);
		model.addAttribute("opcionesv3",opcionesv3);
		model.addAttribute("idsv3",solv3*43/34);
		
		
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("tipoObjetivo", "Letras del Tesoro (Venta)");
		
		
		return"Objetivos_3_2";
	}
	
	@RequestMapping("/tantoDeInteresEquivalentes")
	public String ejerciciosTiposInteres(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*6);
		
		List<String> periodos = Arrays.asList("mensual","trimestral","cuatrimestral","semestral");
		List<Integer> m = Arrays.asList(12,4,3,2);
		String periodo = "";
		String enunciado = "";
		float tanto = (float)(1+Math.random()*9);
		double sol = 0.0;
		List<String> opciones = new ArrayList<>();
						
		switch (tipo) {
			//---------------------- SIMPLE: ANUAL >>>> MENSUAL ---------------------------
			case 0:
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				enunciado = "??Qu?? tanto "+periodo+" en capitalizaci??n SIMPLE equivale a un "+Precision.round(tanto,2)+" % anual?";
				for(int i = 0; i < 4; i++) {
					double im = (tanto/100)/m.get(i);
					opciones.add(Precision.round(im*100,2)+" %");
				}
				if (periodo=="mensual") {
					sol = (double) (((tanto/100)/12)*100);
				}
				else if(periodo=="trimestral") {
					sol = (double) (((tanto/100)/4)*100);
				}
				else if (periodo=="cuatrimestral") {
					sol = (double) (((tanto/100)/3)*100);
				}
				else {
					sol = (double) (((tanto/100)/2)*100);
				}
				break;
			//---------------------- SIMPLE: MENSUAL >>>> ANUAL ---------------------------
			case 1:
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				enunciado = "??Qu?? tanto anual en capitalizaci??n SIMPLE equivale a un "+Precision.round(tanto,2)+" % "+periodo+" ?";
				for(int j = 0; j < 4; j++) {
					double i = (tanto/100)*m.get(j);
					opciones.add(Precision.round(i*100,2)+" %");
				}
				if (periodo=="mensual") {
					sol = (double) (((tanto/100)*12)*100);
				}
				else if(periodo=="trimestral") {
					sol = (double) (((tanto/100)*4)*100);
				}
				else if (periodo=="cuatrimestral") {
					sol = (double) (((tanto/100)*3)*100);
				}
				else {
					sol = (double) (((tanto/100)*2)*100);
				}
				
				break;
			//---------------------- COMPUESTA: MENSUAL >>>> ANUAL ---------------------------
			case 2:
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				enunciado = "??Qu?? tanto anual en capitalizaci??n COMPUESTA equivale a un "+Precision.round(tanto,2)+" % "+periodo+" ?";
				for(int j = 0; j < 4; j++) {
					double i = Math.pow(1+tanto/100, m.get(j))-1;
					opciones.add(Precision.round(i*100,2)+" %");
				}
				if (periodo=="mensual") {
					sol = (double) (Math.pow(1+tanto/100, 12)-1)*100;
				}
				else if(periodo=="trimestral") {
					sol = (double) (Math.pow(1+tanto/100, 4)-1)*100;
				}
				else if (periodo=="cuatrimestral") {
					sol = (double) (Math.pow(1+tanto/100, 3)-1)*100;
				}
				else {
					sol = (double) (Math.pow(1+tanto/100, 2)-1)*100;
				}
				break;
			//---------------------- COMPUESTA: ANUAL >>>> MENSUAL ---------------------------
			case 3:
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				enunciado = "??Qu?? tanto "+periodo+" en capitalizaci??n COMPUESTA equivale a un "+Precision.round(tanto,2)+" % anual?";
				for(int i = 0; i < 4; i++) {
					double im = Math.pow(1+tanto/100, (double) 1/m.get(i))-1;
					opciones.add(Precision.round(im*100,2)+" %");
				}
				if (periodo=="mensual") {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/12)-1)*100;
				}
				else if(periodo=="trimestral") {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/4)-1)*100;
				}
				else if (periodo=="cuatrimestral") {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/3)-1)*100;
				}
				else {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/2)-1)*100;
				}
				break;
			//---------------------- EFECTIVO >>>> NOMINAL ---------------------------
			case 4:
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				enunciado = "??Qu?? tanto NOMINAL "+periodo+" equivale a un "+Precision.round(tanto,2)+" % anual?";
				for(int i = 1; i < 4; i++) {
					double im = Math.pow(1+tanto/100, 1/(double)(1+Math.random()*m.get(i)))-1;
					double jm = im*m.get(i);
					opciones.add(Precision.round(jm*100,2)+" %");
				}
				if (periodo=="mensual") {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/12)-1)*12*100;
				}
				else if(periodo=="trimestral") {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/4)-1)*4*100;
				}
				else if (periodo=="cuatrimestral") {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/3)-1)*3*100;
				}
				else {
					sol = (double) (Math.pow(1+tanto/100, (double) 1/2)-1)*2*100;
				}
				opciones.add(Precision.round(sol,2)+" %");
				break;
			//---------------------- NOMINAL >>>> EFECTIVO ---------------------------
			case 5:
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				enunciado = "??Qu?? tanto EFECTIVO anual equivale a un "+Precision.round(tanto,2)+" % NOMINAL "+periodo+" ?";
				for(int j = 1; j < 4; j++) {
					double im = (tanto/100)/m.get(j)*100; 
					double i = Math.pow(1+im/100, (double)(1+Math.random()*m.get(j)-1))-1;
					opciones.add(Precision.round(i*100,2)+" %");
				}
				if (periodo=="mensual") {
					double im = (tanto/100)/12*100;
					sol = (double) (Math.pow(1+im/100, 12)-1)*100;
				}
				else if(periodo=="trimestral") {
					double im = (tanto/100)/4*100;
					sol = (double) (Math.pow(1+im/100, 4)-1)*100;
				}
				else if (periodo=="cuatrimestral") {
					double im = (tanto/100)/3*100;
					sol = (double) (Math.pow(1+im/100, 3)-1)*100;
				}
				else {
					double im = (tanto/100)/2*100;
					sol = (double) (Math.pow(1+im/100, 2)-1)*100;
				}
				opciones.add(Precision.round(sol,2)+" %");
				break;
		}
		model.addAttribute("tipo",1.1);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Operaciones Simples");
		model.addAttribute("tema", "Tantos de Inter??s Equivalentes");
		model.addAttribute("atras", "bloqueOperacionesSimples");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/capitalizacionSimple")
	public String ejerciciosCapitalizacionSimple(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*4);
		
		List<String> periodos = Arrays.asList("meses","trimestres","cuatrimestres","semestres","a??os");
		List<String> periodos2 = Arrays.asList("mensual","trimestral","cuatrimestral","semestral","anual");
		List<Integer> m = new LinkedList<Integer>(Arrays.asList(12,4,3,2,1));
		double Cn,Co,i = 0;
		double n = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		String periodo = "";
		String enunciado = "";
		
		switch (tipo) {
		//------------------------- CALCULAR Cn -------------------------
		case 0:
			Co = (double)(1000+Math.random()*100000);
			i = (double)(1+Math.random()*9);
			n = (int)(2+Math.random()*50);
			periodo = periodos.get((int)(Math.random()*periodos.size()));
			
			enunciado = "Calcular el montante de "+(int)Co+"??? prestados al "+Precision.round(i,2)+"% anual en "+(int)n+" "+periodo+".";
			
			i = i/100;
			
			if (periodo=="meses") {
				Cn = Co*(1+i*(n/12));
				m.remove(m.get(0));
			}
			else if (periodo=="trimestres") {
				Cn = Co*(1+i*(n/4));
				m.remove(m.get(1));
			}
			else if (periodo=="cuatrimestres") {
				Cn = Co*(1+i*(n/3));
				m.remove(m.get(2));
			}
			else if (periodo=="semestres") {
				Cn = Co*(1+i*(n/2));
				m.remove(m.get(3));
			}
			else {
				Cn = Co*(1+i*n);
				m.remove(m.get(4));
			}
			opciones.add(Precision.round(Cn,2)+" ???");
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round(Co*(1+i*n/m.get(j)),2)+" ???");
			}
			sol = Precision.round(Cn,2);
			break;
		//------------------------- CALCULAR Co -------------------------
		case 1:
			Cn = (double)(1000+Math.random()*100000);
			i = (double)(1+Math.random()*9);
			n = (int)(2+Math.random()*50);
			periodo = periodos.get((int)(Math.random()*periodos.size()));
			
			enunciado = "??Qu?? capital se prest?? durante "+(int)n+" "+periodo+" al "+Precision.round(i,2)+"% anual, si la cantidad devuelta, incluidos los intereses, ascendi?? a "+Precision.round(Cn,2) +"????";
			
			i = i/100;
			
			if (periodo=="meses") {
				Co = Cn/(1+i*(n/12));
				m.remove(m.get(0));
			}
			else if (periodo=="trimestres") {
				Co = Cn/(1+i*(n/4));
				m.remove(m.get(1));
			}
			else if (periodo=="cuatrimestres") {
				Co = Cn/(1+i*(n/3));
				m.remove(m.get(2));
			}
			else if (periodo=="semestres") {
				Co = Cn/(1+i*(n/2));
				m.remove(m.get(3));
			}
			else {
				Co = Cn/(1+i*n);
				m.remove(m.get(4));
			}
			opciones.add(Precision.round(Co,2)+" ???");
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round(Cn/(1+i*n/m.get(j)),2)+" ???");
			}
			sol = Precision.round(Co,2);
			break;
		//------------------------- CALCULAR n -------------------------
		case 2:
			Co = (double)(1000+Math.random()*20000);
			Cn = (double)(20000+Math.random()*100000);
			i = (double)(1+Math.random()*9);
			
			enunciado = "Calcular la duraci??n de un pr??stamo de "+(int)Co+" ???, si al "+Precision.round(i,2)+"% anual se han reembolsado "+Precision.round(Cn,2)+" ???.";
			
			i = i/100;
			
			int periodoA??o = (int)(1+Math.random()*5);
			
			if (periodoA??o==1) {
				n = (((Cn/Co)-1)/i)*12;
				sol = Precision.round(n,2);
				opciones.add(Precision.round(n,2)+" meses");
				m.remove(m.get(0));
			}
			else if (periodoA??o==2) { 
				n = (((Cn/Co)-1)/i)*4;
				sol = Precision.round(n,2);
				opciones.add(Precision.round(n,2)+" trimestres");
				m.remove(m.get(1));
			}
			else if (periodoA??o==3) { 
				n = (((Cn/Co)-1)/i)*3;
				sol = Precision.round(n,2);
				opciones.add(Precision.round(n,2)+" cuatrimestres");
				m.remove(m.get(2));
			}
			else if (periodoA??o==4) { 
				n = (((Cn/Co)-1)/i)*2;
				sol = Precision.round(n,2);
				opciones.add(Precision.round(n,2)+" semestres");
				m.remove(m.get(3));
			}
			else { 
				n = (((Cn/Co)-1)/i);
				sol = Precision.round(n,2);
				opciones.add(Precision.round(n,2)+" a??os");
				m.remove(m.get(4));
			}
			
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round((((Cn/Co)-1)/i)*m.get(j),2)+" "+periodos.get(j));
			}
			break;
		//------------------------- CALCULAR i -------------------------
		case 3:
			Co = (double)(50000+Math.random()*110000);
			Cn = (double)(2000+Math.random()*50000);
			n = (int)(1+Math.random()*9);
			
			enunciado = "??A qu?? tipo de inter??s simple se prestaron "+(int)Co+" ???, si en "+(int)n+" a??os produjeron "+Precision.round(Cn,2)+" ??? de inter??s?";
			
			periodoA??o = (int)(1+Math.random()*5);
			
			i = ((((Cn+Co)/Co)-1)/n)*100;
			
			if (periodoA??o==1) { 
				i = i/12;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple mensual");
				m.remove(m.get(0));
			}
			else if (periodoA??o==2) { 
				i = i/4;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple trimestral");
				m.remove(m.get(1));
			}
			else if (periodoA??o==3) { 
				i = i/3;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple cuatrimestral");
				m.remove(m.get(2));
			}
			else if (periodoA??o==4) { 
				i = i/2;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple semestral");
				m.remove(m.get(3));
			}
			else { 
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple anual");
				m.remove(m.get(4));
			}
			
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round(((((Cn+Co)/Co)-1)/n)/m.get(j)*100,2)+" % simple "+periodos2.get(j));
			}
			break;
		}
		
		model.addAttribute("tipo",1.2);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Operaciones Simples");
		model.addAttribute("tema", "Capitalizaci??n Simple");
		model.addAttribute("atras", "bloqueOperacionesSimples");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/capitalizacionCompuesta")
	public String ejerciciosCapitalizacionCompuesta(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*4);
		
		List<String> periodos = Arrays.asList("meses","trimestres","cuatrimestres","semestres","a??os");
		List<String> periodos2 = Arrays.asList("mensual","trimestral","cuatrimestral","semestral","anual");
		List<Integer> m = new LinkedList<Integer>(Arrays.asList(12,4,3,2,1));
		double Cn,Co,i = 0;
		double n = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		String periodo = "";
		String enunciado = "";
		
		switch (tipo) {
		//------------------------- CALCULAR Cn -------------------------
		case 0:
			Co = (double)(1000+Math.random()*100000);
			i = (double)(1+Math.random()*9);
			n = (int)(2+Math.random()*50);
			periodo = periodos.get((int)(Math.random()*periodos.size()));
			
			enunciado = "Calcular el montante y los intereses de "+(int)Co+" ??? impuesto en capitalizaci??n compuesta al "+Precision.round(i,2)+"% anual durante "+(int)n+" "+periodo+".";
			
			i = i/100;
			
			if (periodo=="meses") {
				Cn = Co*Math.pow((1+i),n/12);
				m.remove(m.get(0));
			}
			else if (periodo=="trimestres") {
				Cn = Co*Math.pow((1+i),n/4);
				m.remove(m.get(1));
			}
			else if (periodo=="cuatrimestres") {
				Cn = Co*Math.pow((1+i),n/3);
				m.remove(m.get(2));
			}
			else if (periodo=="semestres") {
				Cn = Co*Math.pow((1+i),n/2);
				m.remove(m.get(3));
			}
			else {
				Cn = Co*Math.pow((1+i),n);
				m.remove(m.get(4));
			}
			opciones.add(Precision.round(Cn,2)+" ???");
			
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round(Co*Math.pow((1+i),n/m.get(j)),2)+" ???");
			}
			sol = Precision.round(Cn,2);
			break;
		//------------------------- CALCULAR Co -------------------------
		case 1:
			Cn = (double)(1000+Math.random()*100000);
			i = (double)(1+Math.random()*9);
			n = (int)(2+Math.random()*50);
			periodo = periodos.get((int)(Math.random()*periodos.size()));
			
			enunciado = "Calcular el capital que debe imponerse al "+Precision.round(i,2)+"% anual para disponer de "+Precision.round(Cn,2)+"??? a los "+(int)n+" "+periodo+".";
			
			i = i/100;
			
			if (periodo=="meses") {
				Co = Cn/Math.pow((1+i),n/12);
				m.remove(m.get(0));
			}
			else if (periodo=="trimestres") {
				Co = Cn/Math.pow((1+i),n/4);
				m.remove(m.get(1));
			}
			else if (periodo=="cuatrimestres") {
				Co = Cn/Math.pow((1+i),n/3);
				m.remove(m.get(2));
			}
			else if (periodo=="semestres") {
				Co = Cn/Math.pow((1+i),n/2);
				m.remove(m.get(3));
			}
			else {
				Co = Cn/Math.pow((1+i),n);
				m.remove(m.get(4));
			}
			opciones.add(Precision.round(Co,2)+" ???");
			
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round(Cn/Math.pow((1+i),n/m.get(j)),2)+" ???");
			}
			sol = Precision.round(Co,2);
			break;
		//------------------------- CALCULAR n -------------------------
		case 2:
			Co = (double)(1000+Math.random()*20000);
			Cn = (double)(20000+Math.random()*100000);
			i = (double)(1+Math.random()*9);
			
			enunciado = "??Cu??ntos a??os estuvo prestado un capital de "+(int)Co+" ???, si al "+Precision.round(i,2)+"% anual se transform?? en un montante de "+Precision.round(Cn,2)+" ????";
			
			i = i/100;
			
			int periodoA??o = (int)(1+Math.random()*5);
			
			if (periodoA??o==1) { 
				n = ((Math.log10(Cn)-Math.log10(Co))/Math.log10(1+i))*12;
				sol = Precision.round(n,2);
				opciones.add(sol+" meses");
				m.remove(m.get(0));
			}
			else if (periodoA??o==2) { 
				n = ((Math.log10(Cn)-Math.log10(Co))/Math.log10(1+i))*4;
				sol = Precision.round(n,2);
				opciones.add(sol+" trimestres");
				m.remove(m.get(1));
			}
			else if (periodoA??o==3) { 
				n = ((Math.log10(Cn)-Math.log10(Co))/Math.log10(1+i))*3;
				sol = Precision.round(n,2);
				opciones.add(sol+" cuatrimestres");
				m.remove(m.get(2));
			}
			else if (periodoA??o==4) { 
				n = ((Math.log10(Cn)-Math.log10(Co))/Math.log10(1+i))*2;
				sol = Precision.round(n,2);
				opciones.add(sol+" semestres");
				m.remove(m.get(3));
			}
			else { 
				n = ((Math.log10(Cn)-Math.log10(Co))/Math.log10(1+i));
				sol = Precision.round(n,2);
				opciones.add(sol+" a??os");
				m.remove(m.get(4));
			}
			
			Collections.reverse(m);
			for (int j=0; j<3; j++) {
				
				opciones.add(Precision.round(((Math.log10(Cn)-Math.log10(Co))/Math.log10(1+i))*m.get(j),2)+" "+periodos.get(j));
			}
			break;
		//------------------------- CALCULAR i -------------------------
		case 3:
			Co = (double)(1000+Math.random()*30000);
			Cn = (double)(30000+Math.random()*100000);
			n = (int)(1+Math.random()*9);
			
			enunciado = "Un capital de "+(int)Co+" ??? colocados durante "+(int)n+" a??os, se convierten en "+Precision.round(Cn,2)+" ???, ??a qu?? tanto por ciento se impuso?";
			
			periodoA??o = (int)(1+Math.random()*5);
			
			i = (Math.pow((Cn/Co),1/n)-1)*100;
			
			if (periodoA??o==1) { 
				i = i/12;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple mensual");
				m.remove(m.get(0));
			}
			else if (periodoA??o==2) { 
				i = i/4;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple trimestral");
				m.remove(m.get(1));
			}
			else if (periodoA??o==3) {
				i = i/3;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple cuatrimestral");
				m.remove(m.get(2));
			}
			else if (periodoA??o==4) { 
				i = i/2;
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple semestral");
				m.remove(m.get(3));
			}
			else { 
				sol = Precision.round(i,2);
				opciones.add(sol+" % simple anual");
				m.remove(m.get(4));
			}
			
			Collections.reverse(m);
			for (int j=0; j<3; j++) {
				opciones.add(Precision.round((Math.pow((Cn/Co),1/n)-1)/m.get(j)*100,2)+" % simple "+periodos2.get(j));
			}
			break;
		}
		
		model.addAttribute("tipo",1.3);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Operaciones Simples");
		model.addAttribute("tema", "Capitalizaci??n Compuesta");
		model.addAttribute("atras", "bloqueOperacionesSimples");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/descuentoSimple")
	public String ejerciciosDescuentoSimple(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*5);
		
		List<String> periodos = Arrays.asList("meses","trimestres","cuatrimestres","semestres","a??os");
		List<Integer> m = new LinkedList<Integer>(Arrays.asList(12,4,3,2,1));
		double Cn,Co,D,d = 0;
		double n = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		String periodo = "";
		String enunciado = "";
		
		switch (tipo) {
		//-------------------- DESCUENTO SIMPLE COMERCIAL (Co) ---------------------
			case 0:
				d = (double)(1+Math.random()*9);
				Cn = (double)(30000+Math.random()*100000);
				n = (int)(1+Math.random()*10);
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				
				enunciado = "Calcular el efectivo a percibir, en descuento comercial, al "+Precision.round(d,2)+"% simple anual de un cr??dito de "+(int)Cn+" ??? que vence dentro de "+(int)n+" "+periodo+".";
				
				d = d/100;
				
				if (periodo=="meses") {
					Co = Cn*(1-d*n/12);
					m.remove(m.get(0));
				}
				else if (periodo=="trimestres") {
					Co = Cn*(1-d*n/4);
					m.remove(m.get(1));
				}
				else if (periodo=="cuatrimestres") {
					Co = Cn*(1-d*n/3);
					m.remove(m.get(2));
				}
				else if (periodo=="semestres") {
					Co = Cn*(1-d*n/2);
					m.remove(m.get(3));
				}
				else {
					Co = Cn*(1-d*n);
					m.remove(m.get(4));
				}
				opciones.add(Precision.round(Cn-Co,2)+" ???");
				
				Collections.shuffle(m);
				for (int j=0; j<3; j++) {
					double co = Cn*(1-d*n/m.get(j));
					opciones.add(Precision.round(Cn-co,2)+" ???");
				}
				sol = Precision.round(Cn-Co,2);
				break;
		//-------------------- DESCUENTO SIMPLE COMERCIAL (Cn) ---------------------
			case 1:
				D = (double)(100+Math.random()*1000);
				d = (double)(1+Math.random()*9);
				n = (int)(2+Math.random()*10);
				periodo = periodos.get((int)(Math.random()*periodos.size()));

				enunciado = "Calcular el nominal de un letra de cambio que vence a los "+(int)n+" "+periodo+" y que al ser descontada comercialmente al "+Precision.round(d,2)+"% anual tuvo un descuento de "+Precision.round(D,2)+" ???.";
				
				d = d/100;
				
				if (periodo=="meses") {
					Cn = D/(d*n/12);
					m.remove(m.get(0));
				}
				else if (periodo=="trimestres") {
					Cn = D/(d*n/4);
					m.remove(m.get(1));
				}
				else if (periodo=="cuatrimestres") {
					Cn = D/(d*n/3);
					m.remove(m.get(2));
				}
				else if (periodo=="semestres") {
					Cn = D/(d*n/2);
					m.remove(m.get(3));
				}
				else {
					Cn = D/(d*n);
					m.remove(m.get(4));
				}
				opciones.add(Precision.round(Cn,2)+" ???");
				
				Collections.shuffle(m);
				for (int j=0; j<3; j++) {
					opciones.add(Precision.round(D/(d*n/m.get(j)),2)+" ???");
				}
				sol = Precision.round(Cn,2);
				
				break;
		//-------------------- DESCUENTO SIMPLE COMERCIAL (n) ---------------------
			case 2:
				D = (double)(1000+Math.random()*10000);
				Cn = (double)(20000+Math.random()*80000);
				d = (double)(1+Math.random()*9);
				
				enunciado = "Calcular el periodo de tiempo que se ha anticipado el pago de un cr??dito de "+(int)Cn+" ???, si descontado comercialmente al "+Precision.round(d,2)+"% anual se ha reducido en "+(int)D+"???.";
				
				d = d/100;
				
				int periodoA??o = (int)(1+Math.random()*5);
				
				if (periodoA??o==1) { 
					n = (D/(Cn*d))*12;
					sol = Precision.round(n,2);
					opciones.add(sol+" meses");
					m.remove(m.get(0));
				}
				else if (periodoA??o==2) { 
					n = (D/(Cn*d))*4;
					sol = Precision.round(n,2);
					opciones.add(sol+" trimestres");
					m.remove(m.get(1));
				}
				else if (periodoA??o==3) { 
					n = (D/(Cn*d))*3;
					sol = Precision.round(n,2);
					opciones.add(sol+" cuatrimestres");
					m.remove(m.get(2));
				}
				else if (periodoA??o==4) { 
					n = (D/(Cn*d))*2;
					sol = Precision.round(n,2);
					opciones.add(sol+" semestres");
					m.remove(m.get(3));
				}
				else { 
					n = (D/(Cn*d));
					sol = Precision.round(n,2);
					opciones.add(sol+" a??os");
					m.remove(m.get(4));
				}
				
				Collections.reverse(m);
				for (int j=0; j<3; j++) {
					opciones.add(Precision.round((D/(Cn*d))*m.get(j),2)+" "+periodos.get(j));
				}
				break;
		//-------------------- DESCUENTO SIMPLE COMERCIAL (d) ---------------------	
			case 3:
				double CoAux = (double)(1000+Math.random()*20000);
				Cn = (double)(30000+Math.random()*100000);
				Co = Cn - CoAux;
				n = (int)(1+Math.random()*9);
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				
				enunciado = "Calcular el tipo de descuento simple comercial que se aplic?? a un capital de "+(int)Cn+" ??? si al anticiparse su pago "+(int)n+" "+periodo+", se redujo a "+Precision.round(Co, 2)+" ???.";
				
				if (periodo=="meses") {
					d = ((Co/Cn)-1)/(-n/12);
					m.remove(m.get(0));
				}
				else if (periodo=="trimestres") {
					d = ((Co/Cn)-1)/(-n/4);
					m.remove(m.get(1));
				}
				else if (periodo=="cuatrimestres") {
					d = ((Co/Cn)-1)/(-n/3);
					m.remove(m.get(2));
				}
				else if (periodo=="semestres") {
					d = ((Co/Cn)-1)/(-n/2);
					m.remove(m.get(3));
				}
				else {
					d = ((Co/Cn)-1)/-n;
					m.remove(m.get(4));
				}
				sol = Precision.round(d, 2)*100;
				opciones.add(Precision.round(d, 2)*100+ " % anual");
				
				Collections.shuffle(m);
				for (int j=0; j<3; j++) {
					opciones.add(Precision.round(((Co/Cn)-1)/(-n/m.get(j))*100,2)+" % anual");
				}
				break;
			
		//-------------------- DESCUENTO SIMPLE RACIONAL (Co) ---------------------	
			case 4:
				Cn = (double)(1000+Math.random()*100000);
				d = (double)(1+Math.random()*9);
				n = (int)(2+Math.random()*50);
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				
				enunciado = "Calcular el efectivo que se obtiene descontando un pagar?? de nominal "+(int)Cn+" ???, a un plazo de "+(int)n+" "+periodo+", aplicando descuento simple racional con un tanto del "+Precision.round(d,2)+"% anual.";
				
				d = d/100;
				
				if (periodo=="meses") {
					Co = Cn/(1+d*(n/12));
					m.remove(m.get(0));
				}
				else if (periodo=="trimestres") {
					Co = Cn/(1+d*(n/4));
					m.remove(m.get(1));
				}
				else if (periodo=="cuatrimestres") {
					Co = Cn/(1+d*(n/3));
					m.remove(m.get(2));
				}
				else if (periodo=="semestres") {
					Co = Cn/(1+d*(n/2));
					m.remove(m.get(3));
				}
				else {
					Co = Cn/(1+d*n);
					m.remove(m.get(4));
				}
				opciones.add(Precision.round(Co,2)+" ???");
				Collections.shuffle(m);
				for (int j=0; j<3; j++) {
					opciones.add(Precision.round(Cn/(1+d*n/m.get(j)),2)+" ???");
				}
				sol = Precision.round(Co,2);
				break;
		}
		model.addAttribute("tipo",1.4);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Operaciones Simples");
		model.addAttribute("tema", "Descuento Simple");
		model.addAttribute("atras", "bloqueOperacionesSimples");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/descuentoCompuesto")
	public String ejerciciosDescuentoCompuesto(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*3);
		
		List<String> periodos = Arrays.asList("meses","trimestres","cuatrimestres","semestres","a??os");
		List<Integer> m = new LinkedList<Integer>(Arrays.asList(12,4,3,2,1));
		double Cn,Co,d,i = 0;
		double n = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		String periodo = "";
		String enunciado = "";
		switch (tipo) {
		//------------------- CALCULAR Co DESCUENTO COMPUESTO RACIONAL (con i)---------------------
			case 0:
				Cn = (double)(1000+Math.random()*100000);
				i = (double)(1+Math.random()*9);
				n = (int)(2+Math.random()*50);
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				
				enunciado = "Dado un montante de "+(int)Cn+" ??? calcular el capital inical, aplicando la ley de descuento compuesto racional, si la duraci??n ha sido de "+(int)n+" "+periodo+" y al tipo de interes del "+Precision.round(i,2)+" % anual.";
				
				i = i/100;
				
				if (periodo=="meses") {
					Co = Cn*Math.pow((1+i),-n/12);
					m.remove(m.get(0));
				}
				else if (periodo=="trimestres") {
					Co = Cn*Math.pow((1+i),-n/4);
					m.remove(m.get(1));
				}
				else if (periodo=="cuatrimestres") {
					Co = Cn*Math.pow((1+i),-n/3);
					m.remove(m.get(2));
				}
				else if (periodo=="semestres") {
					Co = Cn*Math.pow((1+i),-n/2);
					m.remove(m.get(3));
				}
				else {
					Co = Cn*Math.pow((1+i),-n);
					m.remove(m.get(4));
				}
				opciones.add(Precision.round(Co,2)+" ???");
				
				Collections.shuffle(m);
				for (int j=0; j<3; j++) {
					opciones.add(Precision.round(Cn*Math.pow((1+i),-n/m.get(j)),2)+" ???");
				}
				sol = Precision.round(Co,2);
				break;
			//------------------- CALCULAR Co DESCUENTO COMPUESTO RACIONAL (con d)---------------------
			case 1:
				Cn = (double)(1000+Math.random()*100000);
				d = (double)(1+Math.random()*9);
				n = (int)(2+Math.random()*50);
				periodo = periodos.get((int)(Math.random()*periodos.size()));
				
				enunciado = "Calcular el efectivo que se obtiene al descontar "+(int)Cn+" ??? durante "+(int)n+" "+periodo+", aplicando descuento compuesto, a una tasa de descuento del "+Precision.round(d,2)+" % anual.";
				
				d = d/100;
				
				if (periodo=="meses") {
					Co = Cn*Math.pow((1-d),n/12);
					m.remove(m.get(0));
				}
				else if (periodo=="trimestres") {
					Co = Cn*Math.pow((1-d),n/4);
					m.remove(m.get(1));
				}
				else if (periodo=="cuatrimestres") {
					Co = Cn*Math.pow((1-d),n/3);
					m.remove(m.get(2));
				}
				else if (periodo=="semestres") {
					Co = Cn*Math.pow((1-d),n/2);
					m.remove(m.get(3));
				}
				else {
					Co = Cn*Math.pow((1-d),n);
					m.remove(m.get(4));
				}
				opciones.add(Precision.round(Co,2)+" ???");
				
				Collections.shuffle(m);
				for (int j=0; j<3; j++) {
					opciones.add(Precision.round(Cn*Math.pow((1-d),n/m.get(j)),2)+" ???");
				}
				sol = Precision.round(Co,2);
				break;
			//------------------- EQUIVALENCIA TANTO INTERES Y TANTO DESCUENTO ---------------------
			case 2:
				d = (double)(1+Math.random()*9);
				
				enunciado = "Calcular el tanto de inter??s (i) equivalente a un tanto de descuento (d) del "+Precision.round(d, 2)+"% anual en compuesta.";
				
				d = d/100;
								
				sol = Precision.round((d/(1-d)*100), 2);
				
				opciones.add(sol+" % anual");
				opciones.add(Precision.round((d/(1+d))*100, 2) + " % anual");
				opciones.add(Precision.round(((1.3+d)*d)*100, 2) + " % anual");
				opciones.add(Precision.round(((1.6-d)*d)*100, 2) + " % anual");
				break;
		}
		model.addAttribute("tipo",1.5);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Operaciones Simples");
		model.addAttribute("tema", "Descuento Compuesto");
		model.addAttribute("atras", "bloqueOperacionesSimples");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/letrasTesoroLetrasCambio")
	public String ejerciciosLetras(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*4);
		
		List<String> periodos = Arrays.asList("meses","trimestres","cuatrimestres","semestres","a??os");
		List<Integer> m = new LinkedList<Integer>(Arrays.asList(12,4,3,2,1));
		double Cn,Co,d,i = 0;
		double n = 0;
		int comision = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		String periodo = "";
		String enunciado = "";
		switch (tipo) {
		//------------- LETRA CAMBIO -> CALCULAR EFECTIVO RECIBIDO -----------------
		case 0:
			Cn = (double)(1000+Math.random()*100000);
			d = (double)(1+Math.random()*15);
			n = (int)(2+Math.random()*10);
			comision = (int)(2+Math.random()*5);
			periodo = periodos.get((int)(Math.random()*periodos.size()));
			
			enunciado = "Un cliente se dispone a descontar una letra de cambio de "+Precision.round(Cn,2)+"??? que vence dentro de "+(int)n+" "+periodo+". "
					+ "Las condiciones del descuento propuestas por el banco, son: tipo de descuento simple (d) del "+(int)d+"%, comisi??n de "+comision+" ???."
					+ "Calcular el efectivo recibido por el cliente.";
			
			d = d/100;
			
			if (periodo=="meses") {
				Co = Cn-(Cn*d*n/12)-comision;
				m.remove(m.get(0));
			}
			else if (periodo=="trimestres") {
				Co = Cn-(Cn*d*n/4)-comision;
				m.remove(m.get(1));
			}
			else if (periodo=="cuatrimestres") {
				Co = Cn-(Cn*d*n/3)-comision;
				m.remove(m.get(2));
			}
			else if (periodo=="semestres") {
				Co = Cn-(Cn*d*n/2)-comision;
				m.remove(m.get(3));
			}
			else {
				Co = Cn-(Cn*d*n)-comision;
				m.remove(m.get(4));
			}
			opciones.add(Precision.round(Co,2)+" ???");
			
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				opciones.add(Precision.round(Cn-(Cn*d*n/m.get(j))-comision,2)+" ???");
			}
			sol = Precision.round(Co,2);
			
			break;
		//------------- LETRA CAMBIO -> CALCULAR COSTE EFECTIVO DTO. -----------------
		case 1:
			double CoAux = (double)(1000+Math.random()*20000);
			Cn = (double)(30000+Math.random()*100000);
			Co = Cn - CoAux;
			n = (int)(1+Math.random()*9);
			periodo = periodos.get((int)(Math.random()*periodos.size()));
			
			enunciado = "Calcule el coste efectivo de una letra de cambio, de capital de "+(int)Cn+" ??? si al anticiparse su pago "+(int)n+" "+periodo+", "
					+ "se redujo a "+Precision.round(Co, 2)+" ???.";
			
			if (periodo=="meses") {
				i = ((Cn/Co)-1)/(n/12);
				m.remove(m.get(0));
			}
			else if (periodo=="trimestres") {
				i = ((Cn/Co)-1)/(n/4);
				m.remove(m.get(1));
			}
			else if (periodo=="cuatrimestres") {
				i = ((Cn/Co)-1)/(n/3);
				m.remove(m.get(2));
			}
			else if (periodo=="semestres") {
				i = ((Cn/Co)-1)/(n/2);
				m.remove(m.get(3));
			}
			else {
				i = ((Cn/Co)-1)/(n);
				m.remove(m.get(4));
			}
			sol = Precision.round(i*100, 2);
			opciones.add(Precision.round(i*100, 2)+ " % anual");
			
			Collections.shuffle(m);
			for (int j=0; j<3; j++) {
				opciones.add(Precision.round((((Cn/Co)-1)/(n/m.get(j)))*100,2)+" % anual");
			}
			break;
		//------------- LETRA TESORO -> CALCULAR PRECIO ADQUISICION -----------------
		case 2:
			List<Integer> letrasN = new LinkedList<Integer>(Arrays.asList(6,12,18));
			n = letrasN.get((int)(Math.random()*letrasN.size()));
			i = (double)(1+Math.random()*15);
			
			enunciado = "Un inversor adquiri?? una letra del tesoro en el mercado primario de nominal 1.000??? y vencimiento "+(int)n +" meses. "
					+ "Determine el precio que pag?? si la rentabilidad ofrecidas es del "+ Precision.round(i, 2)+" %.";
			
			i = i/100;
			
			if(n==6) {
				sol = 1000/(Math.pow(1+i, 0.5));
				sol = Precision.round(sol, 2);
				
				Co = 1000/(Math.pow(1+i, 1));
				opciones.add(Precision.round(Co, 2)+ " ???");
				Co = 1000/(Math.pow(1+i, 1.5));
				opciones.add(Precision.round(Co, 2)+ " ???");
				Co = 1000/(Math.pow(1+i, 2));
				opciones.add(Precision.round(Co, 2)+ " ???");
			}
			else if(n==12) {
				sol = 1000/(Math.pow(1+i, 1));
				sol = Precision.round(sol, 2);
				
				Co = 1000/(Math.pow(1+i, 0.5));
				opciones.add(Precision.round(Co, 2)+ " ???");
				Co = 1000/(Math.pow(1+i, 1.5));
				opciones.add(Precision.round(Co, 2)+ " ???");
				Co = 1000/(Math.pow(1+i, 2));
				opciones.add(Precision.round(Co, 2)+ " ???");
			}
			else {
				sol = 1000/(Math.pow(1+i, 1.5));
				sol = Precision.round(sol, 2);
				
				Co = 1000/(Math.pow(1+i, 0.5));
				opciones.add(Precision.round(Co, 2)+ " ???");
				Co = 1000/(Math.pow(1+i, 1));
				opciones.add(Precision.round(Co, 2)+ " ???");
				Co = 1000/(Math.pow(1+i, 2));
				opciones.add(Precision.round(Co, 2)+ " ???");
			}
			
			opciones.add(sol+ " ???");
			
			break;
		//------------- LETRA TESORO -> CALCULAR RENTABILIDAD POR LA COMPRA -----------------
		case 3:
			Co = (double)(900+Math.random()*95);
			
			enunciado = "El precio en el mercado primario de una Letra del Tesoro de nominal 1.000 ??? y vencimiento a un a??o fue de "+Precision.round(Co, 2)+" ???. "
					+ "Calcule su rentabilidad efectiva.";
			
			i = (1000/Co)-1;
			sol = Precision.round(i*100, 2);
			opciones.add(Precision.round(i*100, 2)+ " % anual");
			
			opciones.add(Precision.round(((Math.pow((1000/Co), 0.75))-1)*100, 2)+ " % anual");
			opciones.add(Precision.round(((Math.pow((1000/Co),2))-1)*100, 2)+ " % anual");
			opciones.add(Precision.round(((Math.pow((1000/Co), 1.5))-1)*100, 2)+ " % anual");
			break;
		}
		model.addAttribute("tipo",1.6);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Operaciones Simples");
		model.addAttribute("tema", "Letras del Tesoro y Letras de Cambio");
		model.addAttribute("atras", "bloqueOperacionesSimples");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/rentas")
	public String ejerciciosRentas(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*4);
		
		List<String> periodos = Arrays.asList("mes","trimestre","cuatrimestre","semestre","a??o");
		String periodo = "";		
		int C,n = 0;
		String enunciado = "";
		double i = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		
		switch (tipo) {
		//--------------- VALOR FINAL PREPAGABLE -----------------------
		case 0:
			C = (int)(100+Math.random()*10000);
			n = (int)(12+Math.random()*5);
			int n1 = 0;
			int n2 = (int)(2+Math.random()*4);
			int n3 = (int)(2+Math.random()*5);
			n1 = n - (n2 + n3);
			double i1 = (double)(1+Math.random()*10);
			double i2 = (double)(1+Math.random()*10);
			double i3 = (double)(1+Math.random()*10);
			
			enunciado = "Calcule el valor final de una renta anual, prepagable, con "+n+" t??rminos de cuant??a constante de "+(int)C+" ???, "
					+ "si los tantos anuales de valoraci??n son el "+Precision.round(i1, 2)+"% anual para los primeros a??os, "
					+ "el "+Precision.round(i2, 2)+"% para los "+n2+" a??os siguientes y el "+Precision.round(i3, 2)+"% para los "+n3+" ??ltimos.";
			
			i1=i1/100;
			i2=i2/100;
			i3=i3/100;
			double tramo1 = (C*(((Math.pow(1+i1, n1))-1)/i1)*(1+i1))*(Math.pow(1+i2, n2))*(Math.pow(1+i3, n3));
			double tramo2 = (C*(((Math.pow(1+i2, n2))-1)/i2)*(1+i2))*(Math.pow(1+i3, n3));
			double tramo3 = C*(((Math.pow(1+i3, n3))-1)/i3)*(1+i3);
			
			sol = Precision.round((tramo1 +tramo2 +tramo3),2);
			opciones.add(sol+" ???");
			
			opciones.add(Precision.round(((C*(((Math.pow(1+i1, n1))-1)/i1)*(1+i1))+
										  (C*(((Math.pow(1+i2, n2))-1)/i1)*(1+i1))+
										  (C*(((Math.pow(1+i3, n3))-1)/i1)*(1+i1))),2)+" ???");
			
			opciones.add(Precision.round((((C*(((Math.pow(1+i1, n1))-1)/i1))*(Math.pow(1+i2, n2))*(Math.pow(1+i3, n3)))+
					  					  ((C*(((Math.pow(1+i2, n2))-1)/i2))*(Math.pow(1+i3, n3)))+
					  					   (C*(((Math.pow(1+i3, n3))-1)/i3))),2)+" ???"); //pospagable
			
			opciones.add(Precision.round(((C*(((Math.pow(1+i1, n1))-1)/i3))+
					  					  (C*(((Math.pow(1+i2, n2))-1)/i3))+
					  					  (C*(((Math.pow(1+i3, n3))-1)/i3))),2)+" ???");

			break;
		//--------------- VALOR FINAL POSPAGABLE -----------------------
		case 1:
			i = (double)(1+Math.random()*10);
			n = (int)(5+Math.random()*10);
			C = (int)(100+Math.random()*10000);
			periodo = periodos.get((int)(Math.random()*periodos.size()-1));
			
			enunciado = "Cierta persona ingresa al finalizar cada "+periodo+" de cada a??o "+C+" ???. La entidad financiera le abona intereses de"
			+" acuerdo con la ley de capitalizaci??n compuesta en un tanto anual efectivo del "+Precision.round(i, 2)+
			" %. Si mantiene los ingresos durante "+n+" a??os, ??qu?? capital habr?? conseguido reunir?";
			
			i = i/100;
			
			opciones.add(Precision.round((C*(((Math.pow(1+i, n))-1)/i)*(1+i)),2)+ " ???"); //(prepagable) opcion sin ajustar C al a??o
			
			double im = 0;
			if (periodo=="mes") {
				im = (Math.pow(1+i, (double) 1/12)-1);
				sol = (Precision.round((C*(((Math.pow(1+im, n*12))-1)/im)),2));
				C = C * 12;
				
			}
			else if (periodo=="trimestre") {
				im = (Math.pow(1+i, (double) 1/4)-1);
				sol = (Precision.round((C*(((Math.pow(1+im, n*4))-1)/im)),2));
				C = C * 4;
				
			}
			else if (periodo=="cuatrimestre") {
				im = (Math.pow(1+i, (double) 1/3)-1);
				sol = (Precision.round((C*(((Math.pow(1+im, n*3))-1)/im)),2));
				C = C * 3;
				
			}
			else if (periodo=="semestre") {
				im = (Math.pow(1+i, (double) 1/2)-1);
				sol = Precision.round((C*(((Math.pow(1+im, n*2))-1)/im)),2);
				C = C * 2;
			}

			opciones.add(sol+" ???");	
			opciones.add(Precision.round((C*(((Math.pow(1+im, n))-1)/im)),2)+ " ???");
			opciones.add(Precision.round((C*(((Math.pow(1+i, n))-1)/i)*(1+i)),2)+ " ???"); //prepagable
						
			break;
		//--------------- CALCULAR ANUALIDAD POSPAGABLE -----------------------
		case 2:
			i = (double)(1+Math.random()*10);
			n = (int)(5+Math.random()*20);
			C = (int)(10000+Math.random()*150000);
			
			enunciado = "Un ciudadano espa??ol desea constituir un capital de "+C+" ??? transcurridos "+n+" a??os. "
			+ "Para ello, ingresa una cuant??a constante al final de cada a??o en su entidad bancaria que capitaliza sus aportaciones al "+Precision.round(i,2)+" % anual. "
			+ "Calcule la anualidad constante que debe entregar.";
			
			i = i / 100;
			
			sol = Precision.round((C / (((Math.pow(1+i, n))-1)/i)),2);
			opciones.add(sol +" ???");
			
			opciones.add(Precision.round((C / ((((Math.pow(1+i, n))-1)/i)*(1+i))),2)+" ???"); //prepagable
			opciones.add(Precision.round((C / ((((Math.pow(1+i, n))-1)/(1+i)))),2)+" ???");
			opciones.add(Precision.round((C / ((((Math.pow(1+i, n))-1)/(1+i))*(1+i))),2)+" ???");
			
			break;
		//--------------- CALCULAR INTERES CONSTANTE -----------------------
		case 3:
			C = (int)(100+Math.random()*10000);
			n = (int)(12+Math.random()*5);
			n1 = 0;
			n2 = (int)(2+Math.random()*4);
			n3 = (int)(2+Math.random()*5);
			n1 = n - (n2 + n3);
			i1 = (double)(1+Math.random()*10);
			i2 = (double)(1+Math.random()*10);
			i3 = (double)(1+Math.random()*10);
			
			enunciado = "Dada una renta anual, prepagable, con "+n+" t??rminos de cuant??a constante de "+(int)C+" ???, "
					+ "si los tantos anuales de valoraci??n son el "+Precision.round(i1, 2)+"% anual para los primeros a??os, "
					+ "el "+Precision.round(i2, 2)+"% para los "+n2+" a??os siguientes y el "+Precision.round(i3, 2)+"% para los "+n3+" ??ltimos. "
					+"??Cu??l ser?? el tipo de inter??s constante para que el valor final de la renta sea el mismo?";
			
			i1=i1/100;
			i2=i2/100;
			i3=i3/100;
			tramo1 = (Math.pow(1+i1, n1));
			tramo2 = (Math.pow(1+i2, n2));
			tramo3 = (Math.pow(1+i3, n3));
			
			sol = Precision.round(((Math.pow((tramo1*tramo2*tramo3),(double)1/n))-1)*100,2);
			opciones.add(sol+" %");
			
			opciones.add(Precision.round(((Math.pow((tramo1+tramo2+tramo3),(double)1/n))-1)*100,2)+" %");
			opciones.add(Precision.round(((Math.pow((tramo1*tramo2*tramo3),(double)1/n1))-1)*100,2)+" %");
			opciones.add(Precision.round(((Math.pow((tramo1*tramo2*tramo3),(double)1/(n2+n3)))-1)*100,2)+" %");
			
			break;
		}
		
		model.addAttribute("tipo",2.1);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Rentas");
		model.addAttribute("tema", "Rentas postpagables y prepagables");
		model.addAttribute("atras", "bloqueRentas");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/operacionesFinancierasCompuestas")
	public String ejerciciosOperacionesFinancierasCompuestas(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*4);
		
		List<Integer> P = new ArrayList<>();
		List<Integer> CP = new ArrayList<>();
		int n;
		double i = 0;
		double sol = 0;
		String enunciado = "";
		List<String> opciones = new ArrayList<>();
		
		switch (tipo) {
		//------------------- RESERVA MATEMATICA >> SALDAR DEUDA -----------------------
		case 0:
			i = (int)(1+Math.random()*10);
			n = (int)(3+Math.random()*2);
			for (int j=0; j<=n; j++) {
				int c = 100*(int)(1+Math.random()*3);
				if(n==3) {
					if (j==n) {
						P.add(c);
					}else if(j==2) {
						P.add(0);
						CP.add(c);
					}else {
						CP.add(0);
						P.add(c);
					}
				}
				else {
					if (j==n) {
						P.add(c);
					}else if(j==2) {
						P.add(0);
						CP.add(c);
					}else {
						CP.add(0);
						P.add(c);
					}
				}
			}
			enunciado = "Dado el siguiente intercambio de capitales:";
			String enunciadoP = "PRESTACION =";
			for (int j=0;j<=n;j++) {
				enunciadoP = enunciadoP+" ("+P.get(j)+";"+j+")";
			}
			String enunciadoCP = "CONTRAPRESTACION =";
			for (int j=0;j<=n;j++) {
				if(j==n) {
					enunciadoCP = enunciadoCP+" (X;"+j+")";
				}else {
					enunciadoCP = enunciadoCP+" ("+CP.get(j)+";"+j+")";
				}
			}
			String enunciado2 = "Calcular la cuant??a ???X??? para saldar la operaci??n en el a??o "+n+", si el tanto efectivo es del "+(int)i+"% anual.";
			
			i = (double) i/100;
			if(n==3) {
				sol = Precision.round((P.get(0)*Math.pow((1+i),3))+(P.get(1)*Math.pow((1+i),2))+(P.get(3))-(CP.get(2)*(1+i)),2);
				opciones.add(Precision.round((P.get(1)*Math.pow((1+i),1))+(P.get(1)*Math.pow((1+i),2))+(P.get(3))-(CP.get(2)*(1+i)),2)+" ???");
				opciones.add(Precision.round((P.get(0)*Math.pow((1+i),3))+(P.get(2)*Math.pow((1+i),2))+(P.get(3))-(CP.get(2)*(1+i)),2)+" ???");
				opciones.add(Precision.round((P.get(2)*Math.pow((1+i),3))+(P.get(1)*Math.pow((1+i),2))+(P.get(3))+(CP.get(2)*(1+i)),2)+" ???");
			}
			else {
				sol = Precision.round((P.get(0)*Math.pow((1+i),4))+(P.get(1)*Math.pow((1+i),3))+(P.get(3)*(1+i))+(P.get(3))-(CP.get(2)*Math.pow((1+i),2)),2);
				opciones.add(Precision.round((P.get(0)*Math.pow((1+i),2))+(P.get(1)*Math.pow((1+i),2))+(P.get(3)*(1+i))+(P.get(3))-(CP.get(2)*Math.pow((1+i),3)),2)+" ???");
				opciones.add(Precision.round((P.get(2)*Math.pow((1+i),4))+(P.get(1)*Math.pow((1+i),1))+(P.get(3)*(1+i))+(P.get(3))+(CP.get(2)*Math.pow((1+i),2)),2)+" ???");
				opciones.add(Precision.round((P.get(0)*Math.pow((1+i),3))+(P.get(1)*Math.pow((1+i),3))+(P.get(3)*(1+i))+(P.get(3))-(CP.get(2)*Math.pow((1+i),1)),2)+" ???");
			}
			opciones.add(sol + " ???");
			model.addAttribute("enunciadoP", enunciadoP);
			model.addAttribute("enunciadoCP", enunciadoCP);
			model.addAttribute("enunciado2", enunciado2);
			
			break;
			//------------------- RESERVA MATEMATICA >> RESERVA EN t -----------------------
			case 1:
				List<Double> listaT = Arrays.asList(0.5 ,1.0 ,1.5, 2.0, 2.5, 3.0, 3.5);
				i = (int)(1+Math.random()*10);
				n = (int)(3+Math.random()*2);
				for (int j=0; j<=n; j++) {
					int c = 100*(int)(1+Math.random()*3);
					if(n==3) {
						if (j==n) {
							P.add(c);
						}else if(j==2) {
							P.add(0);
							CP.add(c);
						}else {
							CP.add(0);
							P.add(c);
						}
					}
					else {
						if (j==n) {
							P.add(c);
						}else if(j==2) {
							P.add(0);
							CP.add(c);
						}else {
							CP.add(0);
							P.add(c);
						}
					}
				}
				enunciado = "Dado el siguiente intercambio de capitales:";
				enunciadoP = "PRESTACION =";
				for (int j=0;j<=n;j++) {
					enunciadoP = enunciadoP+" ("+P.get(j)+";"+j+")";
				}
				enunciadoCP = "CONTRAPRESTACION =";
				for (int j=0;j<=n;j++) {
					if(j==n) {
						enunciadoCP = enunciadoCP+" (X;"+j+")";
					}else {
						enunciadoCP = enunciadoCP+" ("+CP.get(j)+";"+j+")";
					}
				}
				double t = 0;
				if(n==3) {
					t = listaT.get((int) (Math.random()*(listaT.size()-2)));
				}else {
					t = listaT.get((int) (Math.random()*listaT.size()));
				}
				if (t==1.0 || t==2.0 || t==3.0) {
					enunciado2 = "??Cu??l ser??a la reserva matem??tica en el momento t = "+(int)t+", si el tanto efectivo es del "+(int)i+"% anual?";
				}else {
					enunciado2 = "??Cu??l ser??a la reserva matem??tica en el momento t = "+t+", si el tanto efectivo es del "+(int)i+"% anual?";
				}

				i = (double) i/100;

				double contCP = 0, contP = 0;
				
				for(int j=0; j<=t; j++) {
					contCP = contCP + (CP.get(j)*Math.pow((1+i),(double)t-j));
					contP = contP + (P.get(j)*Math.pow((1+i),(double)t-j));
				}
				
				sol = Precision.round((contP - contCP),2);
				if(n==3) {
					opciones.add(Precision.round((P.get(1)*Math.pow((1+i),1))+(P.get(1)*Math.pow((1+i),2))+(P.get(3))-(CP.get(2)*(1+i)),2)+" ???");
					opciones.add(Precision.round((P.get(0)*Math.pow((1+i),3))+(P.get(2)*Math.pow((1+i),2))+(P.get(3))-(CP.get(2)*(1+i)),2)+" ???");
					opciones.add(Precision.round((P.get(2)*Math.pow((1+i),3))+(P.get(1)*Math.pow((1+i),2))+(P.get(3))+(CP.get(2)*(1+i)),2)+" ???");
				}
				else {
					opciones.add(Precision.round((P.get(0)*Math.pow((1+i),2))+(P.get(1)*Math.pow((1+i),2))+(P.get(3)*(1+i))+(P.get(3))-(CP.get(2)*Math.pow((1+i),3)),2)+" ???");
					opciones.add(Precision.round((P.get(2)*Math.pow((1+i),4))+(P.get(1)*Math.pow((1+i),1))+(P.get(3)*(1+i))+(P.get(3))+(CP.get(2)*Math.pow((1+i),2)),2)+" ???");
					opciones.add(Precision.round((P.get(0)*Math.pow((1+i),3))+(P.get(1)*Math.pow((1+i),3))+(P.get(3)*(1+i))+(P.get(3))-(CP.get(2)*Math.pow((1+i),1)),2)+" ???");
				}
				opciones.add(sol + " ???");
				model.addAttribute("enunciadoP", enunciadoP);
				model.addAttribute("enunciadoCP", enunciadoCP);
				model.addAttribute("enunciado2", enunciado2);
				
				break;
			//------------------- VAN -----------------------
			case 2:
				int desembolso = (int)(100000+Math.random()*5000000);
				int C = (int)(10000+Math.random()*100000);
				i = (1+Math.random()*15);
				n = (int)(3+Math.random()*10);
				List<String> periodos = Arrays.asList("mes","trimestre","cuatrimestre","semestre","a??o");
				String periodo = periodos.get((int)(Math.random()*periodos.size()));
				
				enunciado = "Aplicando una tasa del "+Precision.round(i, 2)+"% efectivo anual calcular el VAN de una operaci??n de inversi??n cuyo desembolso inicial es de "+desembolso+" ???"+
				"y cuya recuperaci??n dependen de un renta de "+n+" t??rminos de importe "+C+" ??? que se abonan al prinicpio de cada "+periodo+".";

				i = i/100;
				
				if(periodo.equals("mes")) {
					i = (double) (Math.pow(1+i, (double) 1/12)-1);
				}
				else if(periodo.equals("trimestre")) {
					i = (double) (Math.pow(1+i, (double) 1/4)-1);
				}
				else if(periodo.equals("cuatrimestre")) {
					i = (double) (Math.pow(1+i, (double) 1/3)-1);
				}
				else if(periodo.equals("semestre")) {
					i = (double) (Math.pow(1+i, (double) 1/2)-1);
				}
				sol = C*((1+i)*((1-(Math.pow(1+i, -n)))/i));
				
				opciones.add(Precision.round(sol, 2)+" ???");
				
				sol = -desembolso+sol;
				
				opciones.add(Precision.round(sol, 2)+" ???");
				opciones.add(Precision.round(sol*2, 2)+" ???");
				opciones.add(Precision.round(sol/2, 2)+" ???");
				
				break;
			//------------------- TIR -----------------------
			case 3:
				
				desembolso = (int)(10000+Math.random()*300000);
				n = (int)(3+Math.random()*5);
				double[] flujos = new double[n+1];
				flujos[0] = -desembolso;
				
				enunciado = "Calcular la TIR de una inversi??n con los siguientes flujos de caja. Desembolso inicial de "+desembolso+" ??? "+
						"y "+n+" recuperaciones anuales de importes";
				for(int j=1;j<=n;j++) {
					C = (int)(10000+Math.random()*150000);
					flujos[j]=C;
					if(j==n) {
						enunciado = enunciado + " "+C+" ???.";
					}
					else {
						enunciado = enunciado + " "+C+" ???,";
					}
				}
				
				sol = Precision.round(Irr.irr(flujos)*100, 2);
				
				opciones.add(sol+ " %");
				opciones.add(sol*2+ " %");
				opciones.add(sol/2+ " %");
				opciones.add(Precision.round(sol/2.5,2)+ " %");
				
				break;
		}
			
		
		model.addAttribute("tipo",2.2);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Rentas");
		model.addAttribute("tema", "Operaciones Financieras Compuestas");
		model.addAttribute("atras", "bloqueRentas");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/prestamos")
	public String ejerciciosPrestamos(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*5);
		
		List<String> periodos = Arrays.asList("mensual","trimestral","cuatrimestral","semestral");
		List<String> periodos2 = Arrays.asList("meses","trimestres","cuatrimestres","semestres");
		int Co,n = 0;
		String enunciado = "";
		double im,jm = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		
		switch (tipo) {
		//--------------- MENSUALIDAD A PAGAR ---------------
		case 0:
			Co = (int)(1+Math.random()*55)*10000;
			n = (int)(1+Math.random()*150);
			jm = (double)(1+Math.random()*10);
			
			enunciado = "Se concede un pr??stamo de "+Co+" ??? al "+Precision.round(jm, 2)+"% nominal (TIN), "
					+ "para ser amortizado mediante "+n+" mensualidades de cuant??a constante aplicando el m??todo franc??s."
					+" Calcule el t??rmino amortizativo del pr??stamo.";
			
			jm = jm/100;
			im = jm/12;
			
			sol = Precision.round(Co/((1-(Math.pow(1+im, -n)))/im),2);
			
			opciones.add(sol+" ???");
			
			opciones.add(Precision.round(Co/((1-(Math.pow(1+jm, -n)))/jm),2)+" ???");
			opciones.add(Precision.round(Co/((1+jm)*(1-(Math.pow(1+jm, -n)))/jm),2)+" ???");
			opciones.add(Precision.round(Co/((1+im)*(1-(Math.pow(1+im, -n)))/im),2)+" ???");
			break;
		//--------------- MENSUALIDAD A PAGAR CON CAMBIO DE INTERES ---------------
		case 1:
			Co = (int)(1+Math.random()*55)*10000;
			n = (int)(10+Math.random()*15);
			int nAux = (int)(1+Math.random()*10);
			jm = (double)(1+Math.random()*10);
			double jmAux = (double)(1+Math.random()*10);
			
			enunciado = "Se concede un pr??stamo de "+Co+" ???, para ser amortizado mediante el m??todo franc??s, al "+Precision.round(jm, 2)+"% nominal anual, pagos mensuales y duraci??n "+n+" a??os"
			+ ". Transcurridos "+ nAux+" a??os, el tipo de inter??s aplicado al pr??stamo var??a, y pasa a ser un "+Precision.round(jmAux, 2)+"% nominal anual. "
			+ "Si la duraci??n del pr??stamo no var??a, ??Cu??l ser?? la nueva mensualidad constante que deber?? abonar el deudor?";
			
			jm=jm/100;
			im=jm/12;
			jmAux=jmAux/100;
			double imAux = jmAux/12;
			n=n*12;
			nAux=nAux*12;

			double a = Co/((1-(Math.pow(1+im, -n)))/im);
			double CnAux = a*((1-Math.pow(1+im, -(n-nAux)))/im);
			sol = Precision.round(CnAux/((1-(Math.pow(1+imAux, -(n-nAux))))/imAux),2);
			
			opciones.add(sol+" ???");
			opciones.add(Precision.round(CnAux/((1+jm)*(1-(Math.pow(1+imAux, -(nAux))))/imAux),2)+" ???");
			opciones.add(Precision.round(CnAux/((1+jmAux)*(1-(Math.pow(1+imAux, -(n-nAux))))/imAux),2)+" ???");
			opciones.add(Precision.round(CnAux/((1+jm)*(1-(Math.pow(1+imAux, -(n-nAux))))/imAux),2)+" ???");
			
			break;
		//--------------- CAPITAL VIVO ---------------
		case 2:
			Co = (int)(1+Math.random()*55)*10000;
			n = (int)(10+Math.random()*15);
			nAux = (int)(1+Math.random()*10);
			jm = (double)(1+Math.random()*10);
			
			enunciado = "Se contrata un pr??stamo franc??s de "+Co+" ???, a "+n+" a??os, al "+Precision.representableDelta(jm, 2)+" % TIN, con pagos mensuales constantes. "
					+ "Calcular el capital vivo transcurridos "+nAux+" a??os.";

			jm=jm/100;
			im=jm/12;
			n=n*12;
			nAux=nAux*12;
			
			a = Co/((1-(Math.pow(1+im, -n)))/im);
			sol = Precision.round(a*((1-Math.pow(1+im, -(n-nAux)))/im),2);
			
			opciones.add(sol+" ???");
			opciones.add(Precision.round(a*((1+jm)*(1-Math.pow(1+im, -(n-nAux)))/im),2)+" ???");
			opciones.add(Precision.round(a*((1+im)*(1-Math.pow(1+im, -(n-nAux)))/im),2)+" ???");
			opciones.add(Precision.round(a*((1-Math.pow(1+im, -(nAux)))/im),2)+" ???");

			break;
		//--------------- CAPITAL INICIAL/NOMINAL ---------------
		case 3:
			Co = (int)(1+Math.random()*55)*100;
			n = (int)(1+Math.random()*5);
			nAux = (int)(2+Math.random()*9);
			int p = (int)(Math.random()*periodos.size());
			jm = (double)(1+Math.random()*10);
			
			enunciado = "Sea un pr??stamo tipo franc??s con t??rmino amortizativo "+periodos.get(p)+" y duraci??n "+n+" a??os, al que se aplica un tipo del "+Precision.round(jm, 2)+"% nominal anual."
			+ "La cuota de amortizaci??n tras "+nAux+" "+periodos2.get(p)+" es de "+Co+" ??? ??Cu??l es el Nominal del pr??stamo?";

			jm=jm/100;
			
			if (p==0) {
				im = jm/12;
				n = n*12;
			}else if(p==1) {
				im = jm/4;
				n = n*4;
			}else if (p==2) {
				im = jm/3;
				n = n*3;
			}else {
				im = jm/2;
				n = n*2;
			}
			
			double A1 = Co/(Math.pow((1+im),nAux-1));
			sol = Precision.round(A1*((Math.pow(1+im, n)-1)/im),2);
			
			opciones.add(sol +" ???");
			opciones.add(Precision.round((Co/(Math.pow((1+jm),nAux-1)))*((Math.pow(1+jm, n)-1)/jm),2)+" ???");
			opciones.add(Precision.round((Co/(Math.pow((1+jm),nAux)))*((Math.pow(1+jm, n)-1)/jm),2)+" ???");
			opciones.add(Precision.round((Co/(Math.pow((1+im),n-nAux)))*((Math.pow(1+im, n)-1)/im),2)+" ???");
			
			break;
		//--------------- INTERES EFECTIVO ---------------
		case 4:
			Co = (int)(1+Math.random()*55)*1000;
			int A = (int)(1+Math.random()*55)*100;
			n = (int)(15+Math.random()*10);
			periodos = Arrays.asList("3","4","6");
			p = (int)(Math.random()*periodos.size());
			
			enunciado = "Una sociedad obtiene un pr??stamo de "+Co+" ??? comprometi??ndose a pagar, transcurridos "+periodos.get(p)+" meses, "+A+" ??? e iguales importes cada "+periodos.get(p)+"meses "
			+ "hasta transcurridos "+n+" a??os desde que recibi?? el principal, momento en el cu??l paga como es habitual las "+A+" ??? m??s un importe de "+Co
			+" ??? saldando as?? pr??stamo. Determinar el tanto de inter??s efectivo anual que aplica el banco.";
			
			im = (double) A/Co;

			opciones.add(Precision.round(im*100,2)+" % efectivo anual"); 
			
			double i = 0;
			if(p==0) {
				opciones.add(Precision.round(im*4*100,2)+" % efectivo anual"); 
				opciones.add(Precision.round(im/4*100,2)+" % efectivo anual"); 
				i = (Math.pow((1+im),4)-1)*100;
			}else if(p==1) {
				opciones.add(Precision.round(im*3*100,2)+" % efectivo anual"); 
				opciones.add(Precision.round(im/3*100,2)+" % efectivo anual"); 
				i = (Math.pow((1+im),3)-1)*100;
			}else {
				opciones.add(Precision.round(im*2*100,2)+" % efectivo anual"); 
				opciones.add(Precision.round(im/2*100,2)+" % efectivo anual"); 
				i = (Math.pow((1+im),2)-1)*100;
			}
			
			sol = Precision.round(i, 2);
			opciones.add(sol+" % efectivo anual");
			
			break;
		}
		
		model.addAttribute("tipo",3.1);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Pr??stamos");
		model.addAttribute("tema", "Pr??stamos M??todo Franc??s");
		model.addAttribute("atras", "bloquePrestamos");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping("/leasing")
	public String ejerciciosLeasing(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		int tipo = (int)(Math.random()*2);
		
		List<String> periodos = Arrays.asList("mensuales","trimestrales","cuatrimestrales","semestrales");
		
		int Co,VR,n = 0;
		double a = 0;
		String enunciado = "";
		double im,jm = 0;
		double sol = 0;
		List<String> opciones = new ArrayList<>();
		
		switch (tipo) {
		//--------------- MENSUALIDAD A PAGAR LEASING POSPAGABLE ---------------
		case 0:
			Co = (int)(1+Math.random()*55)*10000;
			VR = (int)(1+Math.random()*55)*1000;
			n = (int)(3+Math.random()*8);
			jm = (double)(1+Math.random()*10);
			int p = (int)(Math.random()*periodos.size());
			
			enunciado = "Se contrata una operaci??n financiera de leasing sobre un principal de "+Co+" ??? a un plazo de "+n+" a??os, con pagos "+periodos.get(p)+" pospagables"
			+ " al "+Precision.round(jm, 2)+" % nominal anual. El valor residual es de "+VR+" ??? que se abonar??an a final de la operaci??n. Calcule el importe de la cuota a pagar.";
			
			jm=jm/100;
			
			opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1-Math.pow(1+jm, -n))/jm), 2)+ " ???");
			
			
			if(p==0) {
				im=jm/12;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1-Math.pow(1+im, -n))/im), 2)+ " ???"); 
				n=n*12;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1-Math.pow(1+jm, -n))/jm), 2)+ " ???"); 
			}else if(p==1) {
				im=jm/4;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1-Math.pow(1+im, -n))/im), 2)+ " ???"); 
				n=n*4;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1-Math.pow(1+jm, -n))/jm), 2)+ " ???");
			}else if(p==2) {
				im=jm/3;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1-Math.pow(1+im, -n))/im), 2)+ " ???");
				n=n*3;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1-Math.pow(1+jm, -n))/jm), 2)+ " ???");
			}
			else {
				im=jm/2;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1-Math.pow(1+im, -n))/im), 2)+ " ???");
				n=n*2;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1-Math.pow(1+jm, -n))/jm), 2)+ " ???");
			}
			a = (double)(Co-(VR*(Math.pow(1+im,-n))))/((1-Math.pow(1+im, -n))/im);
			sol = Precision.round(a, 2);
			
			opciones.add(sol+ " ???");
			break;
		//--------------- MENSUALIDAD A PAGAR LEASING PREPAGABLE ---------------			
		case 1:
			Co = (int)(1+Math.random()*55)*10000;
			VR = (int)(1+Math.random()*55)*1000;
			n = (int)(3+Math.random()*8);
			jm = (double)(1+Math.random()*10);
			p = (int)(Math.random()*periodos.size());
			
			enunciado = "Se contrata una operaci??n financiera de leasing sobre un principal de "+Co+" ??? a un plazo de "+n+" a??os, con pagos "+periodos.get(p)+" prepagables"
			+ " al "+Precision.round(jm, 2)+" % nominal anual. El valor residual es de "+VR+" ??? que se abonar??an a final de la operaci??n. Calcule el importe de la cuota a pagar.";
			
			jm=jm/100;
			
			opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1+jm)*((1-Math.pow(1+jm, -n))/jm)), 2)+ " ???");
			
			
			if(p==0) {
				im=jm/12;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1+im)*((1-Math.pow(1+im, -n))/im)), 2)+ " ???");
				n=n*12;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1+im)*((1-Math.pow(1+jm, -n))/jm)), 2)+ " ???");
			}else if(p==1) {
				im=jm/4;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1+im)*((1-Math.pow(1+im, -n))/im)), 2)+ " ???");
				n=n*4;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1+im)*((1-Math.pow(1+jm, -n))/jm)), 2)+ " ???");
			}else if(p==2) {
				im=jm/3;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/((1+im)*((1-Math.pow(1+im, -n))/im)), 2)+ " ???");
				n=n*3;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1+im)*((1-Math.pow(1+jm, -n))/jm)), 2)+ " ???");
			}
			else {
				im=jm/2;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+im,-n))))/(((1+im)*(1-Math.pow(1+im, -n))/im)), 2)+ " ???");
				n=n*2;
				opciones.add(Precision.round((Co-(VR*(Math.pow(1+jm,-n))))/((1+im)*((1-Math.pow(1+jm, -n))/jm)), 2)+ " ???");
			}
			a = (Co-(VR*(Math.pow(1+im,-n))))/((1+im)*((1-Math.pow(1+im, -n))/im));
			sol = Precision.round(a, 2);
			
			opciones.add(sol+ " ???");
			
			break;
		}
		model.addAttribute("tipo",3.2);
		model.addAttribute("enunciado",enunciado);
		Collections.shuffle(opciones);
		model.addAttribute("opciones",opciones);
		model.addAttribute("ids",sol*43/34);
		model.addAttribute("usuario", alumno);
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("bloque", "Pr??stamos");
		model.addAttribute("tema", "Leasing");
		model.addAttribute("atras", "bloquePrestamos");
		return "Pagina_Ejercicios";
	}
	
	@RequestMapping(value="/guardarRespuesta")
	@ResponseStatus(value = HttpStatus.OK)
	public void alamcenarRespuesta (@RequestBody List<String> respuestas) {
		
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		//GUARDAR EJERCICIO
		Ejercicio e = new Ejercicio();
		switch(respuestas.get(respuestas.size()-1)) {
		case "1.1":
			e.setBloque("Operaciones Simples");
			e.setTipo("Tantos Equivalentes");
			break;
		case "1.2":
			e.setBloque("Operaciones Simples");
			e.setTipo("Capitalizacion Simple");
			break;
		case "1.3":
			e.setBloque("Operaciones Simples");
			e.setTipo("Capitalizacion Compuesta");
			break;
		case "1.4":
			e.setBloque("Operaciones Simples");
			e.setTipo("Descuento Simple");
			break;
		case "1.5":
			e.setBloque("Operaciones Simples");
			e.setTipo("Descuento Compuesto");
			break;
		case "1.6":
			e.setBloque("Operaciones Simples");
			e.setTipo("Letras del Tesoro y Letras de Cambio");
			break;
		case "2.1":
			e.setBloque("Rentas");
			e.setTipo("Rentas postpagables y prepagables");
			break;
		case "2.2":
			e.setBloque("Rentas");
			e.setTipo("Operaciones Financieras Compuestas");
			break;
		case "3.1":
			e.setBloque("Pr??stamos");
			e.setTipo("Pr??stamos Franc??s");
			break;
		case "3.2":
			e.setBloque("Pr??stamos");
			e.setTipo("Leasing");
			break;
		}
		String[] enunciado = respuestas.get(respuestas.size()-2).split("(?<=\\G.{255})");
		e.setEnunciado1(enunciado[0]);
		if(enunciado.length>1) {
			e.setEnunciado2(enunciado[1]);
		}
		repoEjercicios.save(e);
		
		//GUARDAR RESPUESTA
		String respuestasAlumno=respuestas.get(0);
	    for (int i=1; i<respuestas.size()-3; i++) {
	    	respuestasAlumno=respuestasAlumno+"|"+respuestas.get(i);
	    }
	    Respuesta respuesta = new Respuesta();
	    respuesta.setEjercicio(e);
	    respuesta.setFechaRealizaci??n(new Date());
	    respuesta.setListaRespuestas(respuestasAlumno);
	    
	    alumno.getListaEjercicios().add(respuesta);
	    alumno.setDinero(alumno.getDinero()+Integer.parseInt(respuestas.get(respuestas.size()-3)));
	    
	    repoRespuestas.save(respuesta);
	    repoUsuarios.saveAndFlush(alumno);
	}
	
	@RequestMapping(value="/objetivoHecho")
	@ResponseStatus(value = HttpStatus.OK)
	public void objetivoRealizado (@RequestBody List<String> respuestas) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		
		if(respuestas.get(0).equals("1")) {
			alumno.setObj1(1);
		}
		else if (respuestas.get(0).equals("2")) {
			alumno.setObj2(1);
		}
		else if (respuestas.get(0).equals("3.1")) {
			alumno.setObj31(1);
		}
		else if (respuestas.get(0).equals("3.2")) {
			alumno.setObj32(1);
		}
		
		double dinero = Double.parseDouble(respuestas.get(1));
		alumno.setDinero(Precision.round(alumno.getDinero()+ dinero, 2));
		repoUsuarios.saveAndFlush(alumno);
	}
	
}
