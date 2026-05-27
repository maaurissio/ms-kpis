package cl.duoc.cordillera.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "periodos")
public class Periodo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TipoPeriodo tipo;

    @Column(nullable = false)
    public LocalDate fechaInicio;

    @Column(nullable = false)
    public LocalDate fechaFin;

    public boolean activo = true;
}
