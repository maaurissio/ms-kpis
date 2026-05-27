package cl.duoc.cordillera.infrastructure.config;

import cl.duoc.cordillera.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import io.quarkus.runtime.StartupEvent;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApplicationScoped
public class DataSeeder {

    @Transactional
    public void seed(@Observes StartupEvent event) {
        if (KPI.count() > 0) {
            return;
        }

        Sucursal s1 = new Sucursal();
        s1.codigo = "SCL001";
        s1.nombre = "Sucursal Santiago Centro";
        s1.ciudad = "Santiago";
        s1.activo = true;
        s1.persist();

        Sucursal s2 = new Sucursal();
        s2.codigo = "VPA001";
        s2.nombre = "Sucursal Vina del Mar";
        s2.ciudad = "Vina del Mar";
        s2.activo = true;
        s2.persist();

        Periodo pActual = new Periodo();
        pActual.tipo = TipoPeriodo.MENSUAL;
        pActual.fechaInicio = LocalDate.of(2026, 5, 1);
        pActual.fechaFin = LocalDate.of(2026, 5, 31);
        pActual.activo = true;
        pActual.persist();

        seedKpi("VTA_TOT", "Ingresos por Ventas Totales", TipoKPI.VENTA, "$",
                "Monto total de ingresos generados por ventas en el periodo evaluado.",
                "Suma de todas las ventas del periodo", "Sistema POS", 130000000, 120000000,
                125000000, s1, pActual);

        seedKpi("TICKET_PROM", "Ticket Promedio por Cliente", TipoKPI.VENTA, "$/transaccion",
                "Valor promedio de compra por cliente en las sucursales.",
                "Ventas totales / N° transacciones", "Sistema POS", 48000, 42000,
                45600, s1, pActual);

        seedKpi("ROT_INV", "Rotacion de Inventario (veces)", TipoKPI.INVENTARIO, "veces",
                "Numero de veces que el inventario se renueva en un periodo.",
                "Costo ventas / Inventario promedio", "SAP", 8.0, 5.0,
                6.8, s1, pActual);

        seedKpi("ENT_REG", "Entregas a Tiempo (%)", TipoKPI.CLIENTE, "%",
                "Porcentaje de pedidos entregados dentro del plazo comprometido.",
                "Entregas a tiempo / Total entregas * 100", "Sistema Logistico", 95.0, 85.0,
                92.5, s1, pActual);

        seedKpi("MGN_BRU", "Margen Bruto (%)", TipoKPI.FINANCIERO, "%",
                "Diferencia porcentual entre ingresos y costo de bienes vendidos.",
                "(Ventas - COGS) / Ventas * 100", "ERP", 42.0, 30.0,
                38.2, s1, pActual);

        seedKpi("STOCK_CRIT", "Productos con Stock Critico", TipoKPI.INVENTARIO, "productos",
                "Cantidad de productos con inventario bajo el minimo requerido.",
                "Conteo de SKUs con stock < stock_minimo", "SAP", 100, 200,
                143, s1, pActual);
    }

    private void seedKpi(String codigo, String nombre, TipoKPI tipo, String unidad,
                         String descripcion, String formula, String fuenteDato,
                         double objetivo, double umbralCritico, double valorInicial,
                         Sucursal sucursal, Periodo periodo) {

        KPI kpi = new KPI();
        kpi.codigo = codigo;
        kpi.nombre = nombre;
        kpi.tipo = tipo;
        kpi.unidad = unidad;
        kpi.descripcion = descripcion;
        kpi.activo = true;
        kpi.persist();

        DefinicionKPI def = new DefinicionKPI();
        def.kpiId = kpi.id;
        def.formula = formula;
        def.fuenteDato = fuenteDato;
        def.objetivo = String.valueOf(objetivo);
        def.umbralAlerta = BigDecimal.valueOf(objetivo * 0.85);
        def.umbralCritico = BigDecimal.valueOf(umbralCritico);
        def.persist();

        ValorKPI valor = new ValorKPI();
        valor.kpi = kpi;
        valor.sucursal = sucursal;
        valor.periodo = periodo;
        valor.valor = BigDecimal.valueOf(valorInicial);
        valor.fechaCalculo = LocalDateTime.now();
        valor.persist();
    }
}
