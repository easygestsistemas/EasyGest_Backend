package com.easygest.springboot;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de seguranca (sem JWT).
 * Cenarios cobertos:
 *  - /health disponivel sem autenticacao (health check do Render)
 *  - preflight CORS (OPTIONS) disponivel sem autenticacao para Vercel e localhost
 *  - endpoint protegido sem autenticacao retorna 401
 *  - credenciais incorretas sao rejeitadas com 401
 *  - payload invalido enviado com autenticacao retorna 400
 *
 * As credenciais sao injetadas como propriedade do sistema (antes do contexto
 * subir) e valem somente para este teste. Nao e senha de producao.
 */
@SpringBootTest(properties = {
        "EASYGEST_CORS_ALLOWED_ORIGINS=https://*.vercel.app,http://localhost:5173",
        "SPRING_MONGODB_URI=mongodb://localhost:27017/EasyGest"
})
@AutoConfigureMockMvc
class SecurityAndValidationTests {

    static {
        System.setProperty("EASYGEST_SECURITY_USERNAME", "easygest_teste");
        System.setProperty("EASYGEST_SECURITY_PASSWORD", "senha_de_teste_123");
    }

    private static final String USUARIO_TESTE = "easygest_teste";
    private static final String SENHA_TESTE = "senha_de_teste_123";

    @Autowired
    private MockMvc mockMvc;

    private String headerBasic(String usuario, String senha) {
        String token = Base64.getEncoder()
                .encodeToString((usuario + ":" + senha).getBytes(StandardCharsets.UTF_8));
        return "Basic " + token;
    }

    @Test
    @DisplayName("Health check disponivel sem autenticacao")
    void healthCheckIsAvailableWithoutAuth() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Preflight CORS (OPTIONS) disponivel sem autenticacao")
    void corsPreflightIsAvailableWithoutAuth() throws Exception {
        mockMvc.perform(options("/produtos")
                        .header(HttpHeaders.ORIGIN, "https://easygest.vercel.app")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "https://easygest.vercel.app"));
    }

    @Test
    @DisplayName("Preflight CORS para localhost (npm run dev no Vite port 5173)")
    void corsPreflightLocalhostViteAvailable() throws Exception {
        mockMvc.perform(options("/produtos")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://localhost:5173"));
    }

    @Test
    @DisplayName("Preflight CORS para localhost (npm run dev em outra porta como 3000)")
    void corsPreflightLocalhost3000Available() throws Exception {
        mockMvc.perform(options("/produtos")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://localhost:3000"));
    }

    @Test
    @DisplayName("Endpoint protegido sem autenticacao deve retornar 401 Unauthorized")
    void endpointProtegidoSemAutenticacaoDeveRetornar401() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Credenciais incorretas devem ser rejeitadas com 401 Unauthorized")
    void credenciaisErradasDevemRetornar401() throws Exception {
        mockMvc.perform(get("/produtos")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, "senha_errada")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Payload invalido com autenticacao deve retornar 400 Bad Request")
    void payloadInvalidoComAutenticacaoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/produtos")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeProduto": " ",
                                  "precoProduto": -1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
