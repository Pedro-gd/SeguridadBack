package sda.ejemplo.model.service;

import sda.ejemplo.model.entity.Usuario;

public interface IUsuarioService {
	public Usuario findByUsername(String username);
}
