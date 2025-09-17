package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;

@DataJpaTest
@Transactional
@Import(EstacionRutaService.class)
class EstacionRutaServiceTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private EstacionRutaService estacionRutaService;

    @Autowired
    private TestEntityManager entityManager;

    private List<EstacionEntity> estacionList = new ArrayList<>();
    private RutaEntity ruta = new RutaEntity(); 

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
    }

    private void insertData() {
        ruta = factory.manufacturePojo(RutaEntity.class);
        ruta.setEstaciones(new ArrayList<>());
        entityManager.persist(ruta);
        for (int i = 0; i < 3; i++) {
            EstacionEntity estacion = factory.manufacturePojo(EstacionEntity.class);
            entityManager.persist(estacion);
            estacionList.add(estacion);
            ruta.getEstaciones().add(estacion);
        }
    }

    @Test
	void testAddEstacionToRuta() throws EntityNotFoundException{
        EstacionEntity estacion = estacionList.get(0);
        RutaEntity resultado = estacionRutaService.addEstacionToRuta(ruta.getId(), estacion.getId());

        assertNotNull(resultado);
        assertEquals(ruta.getId(), resultado.getId());
        assertEquals(3, resultado.getEstaciones().size());
        assertEquals(estacion.getId(), resultado.getEstaciones().get(0).getId());

	}
    @Test
	void testAddEstacionWithInvalidEstacion() {
        assertThrows(EntityNotFoundException.class, () -> {
            estacionRutaService.addEstacionToRuta(ruta.getId(), 0L);
        });
	}

	@Test
	void testAddEstacionWithInvalidRuta() {
        assertThrows(EntityNotFoundException.class, () -> {
            estacionRutaService.addEstacionToRuta(0L, estacionList.get(0).getId());
        });
	}

    @Test
    void testAddEstacionToRutaYaAsociada() throws EntityNotFoundException {
        estacionRutaService.addEstacionToRuta(ruta.getId(), estacionList.get(0).getId());
        RutaEntity resultado = estacionRutaService.addEstacionToRuta(ruta.getId(), estacionList.get(0).getId());

        assertNotNull(resultado);
        assertEquals(ruta.getId(), resultado.getId());
        assertEquals(3, resultado.getEstaciones().size());
        assertEquals(estacionList.get(0).getId(), resultado.getEstaciones().get(0).getId());
    }
    @Test
	void testRemoveEstacion() throws EntityNotFoundException {
		for (EstacionEntity estacion : estacionList) {
			estacionRutaService.removeEstacionFromRuta(ruta.getId(), estacion.getId());
		}
        assertNotNull(ruta);
        assertEquals(0, ruta.getEstaciones().size());
	}

    @Test
	void testRemoveEstacionInvalidRuta() {
		assertThrows(EntityNotFoundException.class, () -> {
			for (EstacionEntity estacion : estacionList) {
				estacionRutaService.removeEstacionFromRuta(0L, estacion.getId());
			}
		});
	}

	@Test
	void testRemoveInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			estacionRutaService.removeEstacionFromRuta(ruta.getId(), 0L);
		});
	}

    @Test 
    void testRemoveEstacionNoAsociada() {
        assertThrows(EntityNotFoundException.class, () -> {
        for (EstacionEntity estacion : estacionList) {
			estacionRutaService.removeEstacionFromRuta(ruta.getId(), estacion.getId());
		}
        assertNotNull(ruta);
        estacionRutaService.removeEstacionFromRuta(ruta.getId(), estacionList.get(0).getId());
        });
    }


	
    
}
