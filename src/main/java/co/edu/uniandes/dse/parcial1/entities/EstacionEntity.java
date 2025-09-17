package co.edu.uniandes.dse.parcial1.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class EstacionEntity extends BaseEntity {
    private String name;
    private String direccion;
    private Integer capacidad;

    @PodamExclude
    @ManyToMany
    private List<RutaEntity> rutas = new ArrayList<>();

    public void setRuta(RutaEntity ruta) {
        if (!rutas.contains(ruta)) {
            rutas.add(ruta);
        }
    }
}
