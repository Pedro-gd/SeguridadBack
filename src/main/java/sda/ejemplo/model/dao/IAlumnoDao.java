package sda.ejemplo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import sda.ejemplo.model.entity.Alumno;

public interface IAlumnoDao  extends JpaRepository<Alumno, Integer>{

}
