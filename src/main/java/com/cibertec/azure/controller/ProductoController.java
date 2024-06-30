package com.cibertec.azure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.azure.models.entity.Producto;
import com.cibertec.azure.models.service.IProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@GetMapping("/verTodos")
	public String mostrarProducto(Model model) {
		System.out.println("Listado de Productos");

		List<Producto> listadoProductos = productoService.listarTodos();

		model.addAttribute("subtitulo", "Lista de Productos");
		model.addAttribute("productos", listadoProductos);

		return "/productos/reports/listarProducto";
	}
	
	
	@GetMapping("/create")
	public String crear(Model model) {
		Producto producto = new Producto();
		model.addAttribute("titulo", "Crea un nuevo Producto");
		model.addAttribute("subtitulo", "Formulario: Nuevo Producto");
		model.addAttribute("producto", producto);
		
		return "/productos/reports/frmCrearProducto";
	}
	
	
	@PostMapping("/save")
	public String guardar(@ModelAttribute("producto") Producto producto, RedirectAttributes attribute, 
			Model model) {
		
		productoService.guardar(producto);
		System.out.println("Producto guardado con éxito...!!!");
		attribute.addFlashAttribute("success","Producto guardado con éxito...!!!");
		
		return "redirect:/productos/verTodos";
	}
	
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") Integer idProducto, Model model, RedirectAttributes attribute) {
		Producto producto = null;
		
		if (idProducto > 0) {
			producto = productoService.buscarPorId(idProducto);
			
			if (producto == null) {
				System.out.println("Error: El ID del Producto a EDITAR no existe...!!!");
				attribute.addFlashAttribute("error", "Atención: El ID del Producto a EDITAR no existe...!!!");
				return "redirect:/productos/verTodos";
			}
		} else {
			System.out.println("Error: Error con el ID del Producto a EDITAR...!!!");
			attribute.addFlashAttribute("error", "Atención: Error con el ID del Producto a EDITAR...!!!");
			return "redirect:/productos/verTodos";
		}
		
		model.addAttribute("titulo", "Edita un Producto existente");
		model.addAttribute("subtitulo", "Formulario: Editar Producto");
		model.addAttribute("producto", producto);
		
		return "/productos/reports/frmCrearProducto";
		//return "/productos/reports/listarProducto";
	}
	
	
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Integer idProducto, RedirectAttributes attribute) {
		Producto producto = null;
		
		if (idProducto > 0) {
			producto = productoService.buscarPorId(idProducto);
			
			if (producto == null) {
				System.out.println("Error: El ID del Producto a ELIMINAR no existe...!!!");
				attribute.addFlashAttribute("error", "Atención: El ID del Producto a ELIMINAR no existe...!!!");
				return "redirect:/views/productos/";
			}
		} else {
			System.out.println("Error: Error con el ID del Producto a ELIMINAR...!!!");
			attribute.addFlashAttribute("error", "Atención: Error con el ID del Producto a ELIMINAR...!!!");
			return "redirect:/views/productos/";
		}
		
		productoService.eliminar(idProducto);
		System.out.println("Registro eliminado con éxito...!!!");
		attribute.addFlashAttribute("warning", "Registro eliminado con éxito...!!!");
		
		return "redirect:/productos/verTodos";
	}
	

	// Para usar con Thymeleaf
	@GetMapping("/tableroBi")
	public String mostrarBi(Model model) {
		System.out.println("Reporte BI");
		model.addAttribute("subtitulo", "Reporte BI - Indicadores de Calidad");
		return "/productos/reports/reportBi";
	}

}
