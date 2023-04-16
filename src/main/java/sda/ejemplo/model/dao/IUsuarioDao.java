package sda.ejemplo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import sda.ejemplo.model.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{
	public Usuario findByUsername(String username);
}
