package org.ith.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson2.JSONException;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import org.ith.model.Contribuyente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TuRucClientTest {

    private HttpServer server;
    private TuRucClient client;
    private final BlockingQueue<URI> requests = new LinkedBlockingQueue<>();

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        client = new TuRuc("http://127.0.0.1:" + server.getAddress().getPort() + "/", 5, 5, 5, 5).getClient();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    @Test
    void looksUpContributorWithFullRuc() throws Exception {
        respond(200, """
                {"data":{"doc":80024191,"razonSocial":"MINISTERIO DE PRUEBA","dv":6,
                "ruc":"80024191-6","estado":"ACTIVO","esPersonaJuridica":false,
                "esEntidadPublica":true},"message":"OK"}
                """);

        var response = client.getContribuyente("80024191-6").execute();

        assertTrue(response.isSuccessful());
        assertEquals("OK", response.body().getMessage());
        var contribuyente = response.body().getData();
        assertEquals("80024191-6", contribuyente.getRuc());
        assertEquals(80024191, contribuyente.getDoc());
        assertEquals(6, contribuyente.getDv());
        assertEquals("MINISTERIO DE PRUEBA", contribuyente.getRazonSocial());
        assertEquals("ACTIVO", contribuyente.getEstado());
        assertFalse(contribuyente.getEsPersonaJuridica());
        assertTrue(contribuyente.getEsEntidadPublica());
        assertEquals(URI.create("/api/contribuyente?ruc=80024191-6"), requests.remove());
    }

    @Test
    void searchesWithUtf8AndPagination() throws Exception {
        respond(200, """
                {"data":{"contribuyentes":[{"doc":1234567,"razonSocial":"NUÑEZ & ASOCIADOS",
                "dv":8,"ruc":"1234567-8","estado":null,"esPersonaJuridica":true,
                "esEntidadPublica":false}],"paginas":3},"message":"OK"}
                """);

        var response = client.searchContribuyentes("NUÑEZ & ASOCIADOS", 2).execute();

        assertTrue(response.isSuccessful());
        var page = response.body().getData();
        assertEquals(3, page.getPaginas());
        assertEquals(1, page.getContribuyentes().size());
        var contribuyente = page.getContribuyentes().get(0);
        assertEquals("1234567-8", contribuyente.getRuc());
        assertEquals(1234567, contribuyente.getDoc());
        assertEquals(8, contribuyente.getDv());
        assertEquals("NUÑEZ & ASOCIADOS", contribuyente.getRazonSocial());
        assertNull(contribuyente.getEstado());
        assertTrue(contribuyente.getEsPersonaJuridica());
        assertFalse(contribuyente.getEsEntidadPublica());
        URI request = requests.remove();
        assertEquals("/api/contribuyente/search", request.getPath());
        assertEquals(Map.of("search", "NUÑEZ & ASOCIADOS", "page", "2"), queryParameters(request));
    }

    @Test
    void returnsEmptySearchPage() throws Exception {
        respond(200, """
                {"data":{"contribuyentes":[],"paginas":0},"message":"OK"}
                """);

        var response = client.searchContribuyentes("SIN RESULTADOS", 0).execute();

        assertTrue(response.isSuccessful());
        assertEquals(0, response.body().getData().getPaginas());
        assertTrue(response.body().getData().getContribuyentes().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 500})
    void preservesHttpErrorStatusAndBody(int status) throws Exception {
        String error = "{\"data\":null,\"message\":\"No se pudo completar la búsqueda\"}";
        respond(status, error);

        var response = client.getContribuyente("invalid").execute();

        assertFalse(response.isSuccessful());
        assertEquals(status, response.code());
        assertNull(response.body());
        assertEquals(error, response.errorBody().string());
    }

    @Test
    void ignoresAdditionalFieldsAndPreservesNullState() throws Exception {
        respond(200, """
                {"data":{"doc":80024191,"razonSocial":"NUÑEZ & ASOCIADOS","dv":6,
                "ruc":"80024191-6","estado":null,"esPersonaJuridica":false,
                "esEntidadPublica":true,"nuevoCampo":{"valor":123}},
                "message":"OK","nuevaMetadata":[1,2,3]}
                """);

        var response = client.getContribuyente("80024191-6").execute();

        assertTrue(response.isSuccessful());
        assertEquals("OK", response.body().getMessage());
        assertEquals(
                new Contribuyente(80024191, "NUÑEZ & ASOCIADOS", 6, "80024191-6", null, false, true),
                response.body().getData());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"data\": {\"doc\":not-a-number}}", "{\"data\":", "{\"data\":null} {\"data\":null}"})
    void rejectsMalformedJsonAsIoError(String json) throws Exception {
        respond(200, json);

        var error = assertThrows(
                IOException.class, () -> client.getContribuyente("80024191-6").execute());
        assertInstanceOf(JSONException.class, error.getCause());
    }

    private Map<String, String> queryParameters(URI uri) {
        return Arrays.stream(uri.getRawQuery().split("&"))
                .map(parameter -> parameter.split("=", 2))
                .collect(Collectors.toMap(
                        pair -> URLDecoder.decode(pair[0], StandardCharsets.UTF_8),
                        pair -> URLDecoder.decode(pair[1], StandardCharsets.UTF_8)));
    }

    private void respond(int status, String json) {
        server.createContext("/", exchange -> {
            requests.add(exchange.getRequestURI());
            byte[] body = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(status, body.length);
            try (var output = exchange.getResponseBody()) {
                output.write(body);
            }
        });
        server.start();
    }
}
