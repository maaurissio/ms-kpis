package cl.duoc.cordillera;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class KpiResourceTest {

    @Test
    void testGetAllKpis() {
        given()
          .when().get("/api/kpis")
          .then()
             .statusCode(200)
             .body("$.size()", greaterThan(5));
    }

    @Test
    void testGetKpiByCodigo() {
        given()
          .when().get("/api/kpis/codigo/VTA_TOT")
          .then()
             .statusCode(200)
             .body("codigo", is("VTA_TOT"))
             .body("nombre", is("Ingresos por Ventas Totales"));
    }

    @Test
    void testGetKpiByCodigoNotFound() {
        given()
          .when().get("/api/kpis/codigo/NO_EXISTE")
          .then()
             .statusCode(404);
    }

    @Test
    void testGetValores() {
        given()
          .when().get("/api/kpis/valores")
          .then()
             .statusCode(200)
             .body("$.size()", greaterThan(5));
    }
}
