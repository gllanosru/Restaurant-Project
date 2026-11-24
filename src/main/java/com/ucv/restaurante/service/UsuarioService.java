package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Usuario;
import com.ucv.restaurante.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository repository = new UsuarioRepository();

    public boolean autenticar(String user, String pass) {
        return repository.validar(user, pass);
    }

    public Usuario autenticarConRol(String user, String pass) {
        return repository.buscarPorCredenciales(user, pass);
    }
}