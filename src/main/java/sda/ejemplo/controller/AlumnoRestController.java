package sda.ejemplo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sda.ejemplo.model.entity.Alumno;
import sda.ejemplo.model.service.IAlumnoService;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"enctype", "Authorization"})
@RestController
@RequestMapping("/api")
public class AlumnoRestController {

	@Autowired 
	public IAlumnoService alumnoService;
	
	@GetMapping("/alumnos")
	public List<Alumno> index(){
		return alumnoService.findAll();
	}
	
	@GetMapping("/alumnos/{id}")
	public ResponseEntity<?> listar(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Alumno alumno= null;
		
		try {
			alumno= alumnoService.findById(id);
		} catch (DataAccessException e) {
			map.put("mensaje", "Se presento un problema al reañizar la consulta");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (alumno==null) {
			map.put("mensaje", "El alumno que se busca no existe");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);
		
	}
	
	@PostMapping("/alumnos")
	public ResponseEntity<?> guardar(@Valid @RequestBody Alumno alumno, BindingResult resultado) {
		Map<String, Object> mapa= new HashMap<String, Object>();
		Alumno alumnoNuevo= null;
		
		if(resultado.hasErrors()) {
			List<String> errores= resultado.getFieldErrors().stream()
					.map(e->"El campo "+e.getField() +" "+ e.getDefaultMessage()).collect(Collectors.toList());
			mapa.put("errors", errores);
			return ResponseEntity.badRequest().body(mapa);
		}
		
		try {
			alumnoNuevo= alumnoService.save(alumno);
		} catch (DataAccessException e2) {
			mapa.put("mensaje", "Fallo al guardar el alumno");
			if(e2.getRootCause().toString().contains("Duplicate entry")) {
				mapa.put("error", "El correo electrónico proporcionado ya está registrado. Por favor, verifica.");

			}else {
				mapa.put("error", e2.getMostSpecificCause()+" : "+ e2.getMessage());
			}
			return ResponseEntity.internalServerError().body(mapa);
			
		}
		
		mapa.put("mensaje", "El alumno se guardo con exito");
		mapa.put("alumno", alumnoNuevo);
		
		return ResponseEntity.ok(mapa);

	}
	
	@PutMapping("/alumnos/{id}")
	public ResponseEntity<?> actualizar(@Valid @RequestBody Alumno alumno, BindingResult resultado, @PathVariable Integer id) {
		Map<String, Object> mapa= new HashMap<String, Object>();
		
		Alumno alumnoActual= alumnoService.findById(id);
		Alumno alumnoActualizado=null;
				
		if(resultado.hasErrors()) {
			List<String> errores= resultado.getFieldErrors().stream()
					.map(e->"El campo "+e.getField() +" "+ e.getDefaultMessage()).collect(Collectors.toList());
			mapa.put("errors", errores);
			return ResponseEntity.badRequest().body(mapa);
		}
		
		if(alumnoActual==null) {
			mapa.put("mensaje", "Error el alumno no existe para actualizar");
			return new ResponseEntity<Map<String, Object>>(mapa, HttpStatus.NOT_FOUND);
		}
		
		
		try {
			alumnoActual.setNombre(alumno.getNombre());
			alumnoActual.setNumeroControl(alumno.getNumeroControl());
			alumnoActual.setEmail(alumno.getEmail());

			alumnoActualizado= alumnoService.save(alumnoActual);
		} catch (DataAccessException e2) {
			
			mapa.put("mensaje", "Fallo al actualizar el alumno");
			mapa.put("error", e2.getMostSpecificCause()+" : "+ e2.getMessage());

			return ResponseEntity.internalServerError().body(mapa);
			
		}
		
		mapa.put("mensaje", "El alumno se actualizo con exito");
		mapa.put("alumno", alumnoActual);
		
		return ResponseEntity.ok(mapa);

	}
	
	@DeleteMapping("/alumnos/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		Map<String, Object> map= new HashMap<String, Object>();
		
		try {
			alumnoService.delete(id);
		} catch (DataAccessException e) {
			map.put("mensaje", "Error al eliminar");
			map.put("error", e.getMostSpecificCause()+ " : "+ e.getMessage());
			return ResponseEntity.internalServerError().body(map);
		}
		
		map.put("mensaje", "Se elimino con exito");
		return ResponseEntity.ok(map);
		
	}
}
