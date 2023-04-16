package sda.ejemplo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sda.ejemplo.model.dao.IAlumnoDao;
import sda.ejemplo.model.entity.Alumno;

@Service
public class AlumnoServiceImp implements IAlumnoService{
	
	@Autowired
	private IAlumnoDao alumnoDao;

	@Override
	@Transactional(readOnly= true)
	public List<Alumno> findAll() {		
		return alumnoDao.findAll();
	}


	@Override
	@Transactional
	public Alumno save(Alumno alumno) {
		return alumnoDao.save(alumno);
	}

	@Override
	@Transactional(readOnly= true)
	public Alumno findById(Integer id) {
		return alumnoDao.findById(id).orElse(null);
	}


	@Override
	@Transactional
	public void delete(Integer id) {
		alumnoDao.deleteById(id);
	}


}
