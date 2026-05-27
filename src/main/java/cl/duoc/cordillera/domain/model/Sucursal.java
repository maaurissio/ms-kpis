package cl.duoc.cordillera.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "sucursales")
public class Sucursal extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String codigo;

    @Column(nullable = false)
    public String nombre;

    public String ciudad;

    public boolean activo = true;
}
