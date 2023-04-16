package sda.ejemplo.model.service;

import java.util.List;

import sda.ejemplo.model.entity.Alumno;

public interface IAlumnoService {
	public List<Alumno> findAll();

	public Alumno save(Alumno alumno);
		
	public Alumno findById(Integer id);
	
	public void delete(Integer id);
}
