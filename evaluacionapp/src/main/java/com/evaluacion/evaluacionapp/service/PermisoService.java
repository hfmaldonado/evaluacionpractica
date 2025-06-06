package com.evaluacion.evaluacionapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evaluacion.evaluacionapp.model.Permiso;

import com.evaluacion.evaluacionapp.repository.PermisoRepository;


@Service
public class PermisoService {
    PermisoRepository permisoRepository;


    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }


    public List<Permiso> obtenerTodos() {
       return permisoRepository.findAll();
    }

}
