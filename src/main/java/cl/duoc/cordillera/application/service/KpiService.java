package cl.duoc.cordillera.application.service;

import cl.duoc.cordillera.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class KpiService {

    public Optional<KPI> findById(UUID id) {
        return KPI.findByIdOptional(id);
    }

    public Optional<KPI> findByCodigo(String codigo) {
        return KPI.find("codigo", codigo).firstResultOptional();
    }

    @Transactional
    public HistorialCalculo calcular(UUID kpiId, UUID periodoId, UUID sucursalId) {
        KPI kpi = KPI.findById(kpiId);
        if (kpi == null) {
            HistorialCalculo error = new HistorialCalculo();
            error.valorKpiId = null;
            error.fechaCalculo = LocalDateTime.now();
            error.estado = EstadoCalculo.ERROR;
            error.mensaje = "KPI con ID " + kpiId + " no encontrado";
            error.persist();
            return error;
        }

        Periodo periodo = periodoId != null ? Periodo.findById(periodoId) : null;
        Sucursal sucursal = sucursalId != null ? Sucursal.findById(sucursalId) : null;

        ValorKPI ultimo = ValorKPI.find("kpi.id = ?1 ORDER BY fechaCalculo DESC", kpiId).firstResult();
        BigDecimal valorAnterior = ultimo != null ? ultimo.valor : BigDecimal.ZERO;

        BigDecimal valorNuevo = simularCalculo(kpi, periodo, sucursal);

        ValorKPI valorKPI = new ValorKPI();
        valorKPI.kpi = kpi;
        valorKPI.sucursal = sucursal;
        valorKPI.periodo = periodo;
        valorKPI.valor = valorNuevo;
        valorKPI.fechaCalculo = LocalDateTime.now();
        valorKPI.persist();

        HistorialCalculo historial = new HistorialCalculo();
        historial.valorKpiId = valorKPI.id;
        historial.fechaCalculo = LocalDateTime.now();
        historial.valorAnterior = valorAnterior;
        historial.valorNuevo = valorNuevo;
        historial.estado = EstadoCalculo.EXITO;
        historial.mensaje = "Calculo exitoso para KPI: " + kpi.codigo;
        historial.persist();

        return historial;
    }

    private BigDecimal simularCalculo(KPI kpi, Periodo periodo, Sucursal sucursal) {
        return switch (kpi.codigo) {
            case "VTA_TOT" -> BigDecimal.valueOf(125000000 + Math.random() * 5000000);
            case "TICKET_PROM" -> BigDecimal.valueOf(45600 + Math.random() * 2000);
            case "ROT_INV" -> BigDecimal.valueOf(6.8 + Math.random() * 0.5);
            case "ENT_REG" -> BigDecimal.valueOf(92.5 + Math.random() * 2);
            case "MGN_BRU" -> BigDecimal.valueOf(38.2 + Math.random() * 1.5);
            case "STOCK_CRIT" -> BigDecimal.valueOf(143 + (int)(Math.random() * 20));
            default -> BigDecimal.valueOf(Math.random() * 1000);
        };
    }
}
