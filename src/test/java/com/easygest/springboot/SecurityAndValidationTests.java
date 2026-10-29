package com.easygest.springboot;

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
        "EASYGEST_CORS_ALLOWED_ORIGINS=https://*.vercel.app,http://localhost:5173",
        "SPRING_MONGODB_URI=mongodb://localhost:27017/EasyGest"
})
@AutoConfigureMockMvc
class SecurityAndValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthCheckAndCorsPreflightAreAvailable() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());

        mockMvc.perform(options("/produtos")
                        .header(HttpHeaders.ORIGIN, "https://easygest.vercel.app")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "https://easygest.vercel.app"));
    }

    @Test
    void invalidProductPayloadIsRejected() throws Exception {
        mockMvc.perform(post("/produtos")
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
