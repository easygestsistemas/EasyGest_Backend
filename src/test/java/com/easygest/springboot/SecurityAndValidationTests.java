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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "EASYGEST_SECURITY_USERNAME=test-user",
        "EASYGEST_SECURITY_PASSWORD=test-password",
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
