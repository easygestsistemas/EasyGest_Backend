package com.easygest.springboot;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

@SpringBootTest(properties = {
        "EASYGEST_SECURITY_USERNAME=test-user",
        "EASYGEST_SECURITY_PASSWORD=test-password",
        "EASYGEST_CORS_ALLOWED_ORIGINS=https://easygest.vercel.app,http://localhost:5173",
        "SPRING_MONGODB_URI=mongodb://localhost:27017/EasyGest"
})
@AutoConfigureMockMvc
class SecurityAndValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void healthCheckAndCorsPreflightArePublic() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());

        mockMvc.perform(options("/produtos")
                        .header(HttpHeaders.ORIGIN, "https://easygest.vercel.app")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "https://easygest.vercel.app"));
    }

    @Test
    void invalidCredentialsAreRejected() throws Exception {
        String invalidCredentials = Base64.getEncoder()
                .encodeToString("test-user:wrong-password".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(get("/produtos")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + invalidCredentials))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidProductPayloadIsRejected() throws Exception {
        mockMvc.perform(post("/produtos")
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeProduto": " ",
                                  "precoProduto": -1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private String basicAuthHeader() {
        String credentials = "test-user:test-password";
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encodedCredentials;
    }
}
