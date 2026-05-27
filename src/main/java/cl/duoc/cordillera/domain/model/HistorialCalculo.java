package cl.duoc.cordillera.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historial_calculos")
public class HistorialCalculo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public UUID valorKpiId;

    @Column(nullable = false)
    public LocalDateTime fechaCalculo;

    @Column(precision = 18, scale = 2)
    public BigDecimal valorAnterior;

    @Column(precision = 18, scale = 2)
    public BigDecimal valorNuevo;

    @Enumerated(EnumType.STRING)
    public EstadoCalculo estado;

    @Column(length = 500)
    public String mensaje;
}
