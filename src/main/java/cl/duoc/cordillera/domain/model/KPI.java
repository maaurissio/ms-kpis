package cl.duoc.cordillera.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kpis")
public class KPI extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String codigo;

    @Column(nullable = false)
    public String nombre;

    @Column(length = 500)
    public String descripcion;

    @Enumerated(EnumType.STRING)
    public TipoKPI tipo;

    public String unidad;

    public boolean activo = true;

    @OneToMany(mappedBy = "kpi", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ValorKPI> valores = new ArrayList<>();
}
