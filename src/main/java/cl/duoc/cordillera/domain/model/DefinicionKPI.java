package cl.duoc.cordillera.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "definiciones_kpi")
public class DefinicionKPI extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public UUID kpiId;

    @Column(length = 1000)
    public String formula;

    public String fuenteDato;

    public String objetivo;

    @Column(precision = 18, scale = 2)
    public BigDecimal umbralAlerta;

    @Column(precision = 18, scale = 2)
    public BigDecimal umbralCritico;
}
