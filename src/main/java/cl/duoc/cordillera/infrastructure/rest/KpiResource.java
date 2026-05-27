package cl.duoc.cordillera.infrastructure.rest;

import cl.duoc.cordillera.application.service.KpiService;
import cl.duoc.cordillera.domain.model.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/api/kpis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KpiResource {

    @Inject
    KpiService kpiService;

    @GET
    public List<KPI> getAll() {
        return KPI.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        return kpiService.findById(id)
                .map(kpi -> Response.ok(kpi).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "KPI con id " + id + " no encontrado"))
                        .build());
    }

    @GET
    @Path("/codigo/{codigo}")
    public Response getByCodigo(@PathParam("codigo") String codigo) {
        return kpiService.findByCodigo(codigo)
                .map(kpi -> Response.ok(kpi).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "KPI con codigo " + codigo + " no encontrado"))
                        .build());
    }

    @GET
    @Path("/codigo/{codigo}/simple")
    public Response getSimpleByCodigo(@PathParam("codigo") String codigo) {
        return kpiService.findByCodigo(codigo).map(kpi -> {
            ValorKPI ultimo = ValorKPI.find("kpi.id = ?1 ORDER BY fechaCalculo DESC", kpi.id).firstResult();
            return Response.ok(Map.of(
                "kpiCodigo", kpi.codigo,
                "nombre", kpi.nombre,
                "valor", ultimo != null ? ultimo.valor.doubleValue() : 0.0,
                "descripcion", kpi.descripcion,
                "categoria", kpi.tipo.name()
            )).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "KPI con codigo " + codigo + " no encontrado"))
                .build());
    }

    @GET
    @Path("/valores")
    public List<ValorKPI> getValores(@QueryParam("sucursalId") UUID sucursalId,
                                     @QueryParam("periodoId") UUID periodoId) {
        if (sucursalId != null && periodoId != null) {
            return ValorKPI.list("sucursal.id = ?1 and periodo.id = ?2", sucursalId, periodoId);
        }
        if (sucursalId != null) {
            return ValorKPI.list("sucursal.id", sucursalId);
        }
        if (periodoId != null) {
            return ValorKPI.list("periodo.id", periodoId);
        }
        return ValorKPI.listAll();
    }

    @POST
    @Path("/calcular")
    public Response calcular(Map<String, String> body) {
        UUID kpiId = body.get("kpiId") != null ? UUID.fromString(body.get("kpiId")) : null;
        UUID periodoId = body.get("periodoId") != null ? UUID.fromString(body.get("periodoId")) : null;
        UUID sucursalId = body.get("sucursalId") != null ? UUID.fromString(body.get("sucursalId")) : null;

        if (kpiId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El campo kpiId es obligatorio"))
                    .build();
        }

        HistorialCalculo resultado = kpiService.calcular(kpiId, periodoId, sucursalId);
        return Response.ok(resultado).build();
    }
}
