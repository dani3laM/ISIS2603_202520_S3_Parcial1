package co.edu.uniandes.dse.parcial1.services;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;

@Service
public class EstacionRutaService {

    @Autowired
    private EstacionRepository estacionRepository;

    @Autowired
    private RutaRepository rutaRepository;
    
    @Transactional
    public RutaEntity addEstacionToRuta(Long rutaId, Long estacionId) throws EntityNotFoundException {
        Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
        if (rutaEntity.isEmpty()) {
            throw new EntityNotFoundException("Ruta no encontrada");
        }

        Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
        if (estacionEntity.isEmpty()) {
            throw new EntityNotFoundException("Estacion no encontrada");
        }

        if (rutaEntity.get().getEstaciones().contains(estacionEntity.get())==false) {
            EstacionEntity estacion = estacionEntity.get();
            rutaEntity.get().getEstaciones().add(estacion);
        }

        

        return rutaEntity.get();
    }
    
    public RutaEntity removeEstacionFromRuta(Long rutaId, Long estacionId) throws EntityNotFoundException {
        Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
        if (rutaEntity.isEmpty()) {
            throw new EntityNotFoundException("Ruta no encontrada");
        }

        Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
        if (estacionEntity.isEmpty()) {
            throw new EntityNotFoundException("Estacion no encontrada");
        }

        if (rutaEntity.get().getEstaciones().contains(estacionEntity.get())) {
            EstacionEntity estacion = estacionEntity.get();
            rutaEntity.get().getEstaciones().remove(estacion);
        } else {
            throw new EntityNotFoundException("La estacion no está asociada a la ruta");
        }

        

        return rutaEntity.get();
    }
}