package com.evaluacion.evaluacionapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evaluacion.evaluacionapp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //Usuario findByNombre(String nombreUsuario);
    Usuario findByNombreUsuarioAndPassword(String nombreUsuario, String password);
    Usuario findByIdUsuario(Long idUsuario);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.nombresApellidos) LIKE LOWER(CONCAT('%', :query, '%'))  " 
           )
    List<Usuario> buscarUsuarios(@Param("query") String query);
}

