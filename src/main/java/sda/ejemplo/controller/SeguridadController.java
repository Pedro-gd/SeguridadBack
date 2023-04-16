package sda.ejemplo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sda.ejemplo.auth.auth.ConfigSecurity;
import sda.ejemplo.auth.jwt.JwtProvider;
import sda.ejemplo.model.entity.Usuario;
import sda.ejemplo.model.service.UsuarioServiceImpl;
import sda.ejemplo.utils.Mensaje;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"enctype", "Authorization"})
@RestController
@RequestMapping("/auth")
public class SeguridadController {

	
	@Autowired
	UsuarioServiceImpl usuarioServiceImpl;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	ConfigSecurity configSecurity;
	
	@PostMapping("/login")
	public ResponseEntity<Mensaje> login(@Valid @RequestBody Usuario loginUsuario, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(new Mensaje("Usuario y/o contrseña son requeridos", ""));
		}
		
		Authentication authentication= configSecurity.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt= jwtProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new Mensaje("Exito", jwt));
		
	}
}
