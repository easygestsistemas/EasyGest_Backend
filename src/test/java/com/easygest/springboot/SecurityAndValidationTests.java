package com.easygest.springboot;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import com.easygest.springboot.model.Fornecedor;
import com.easygest.springboot.model.Funcionario;
import com.easygest.springboot.model.Produto;
import com.easygest.springboot.repository.FornecedorRepository;
import com.easygest.springboot.repository.FuncionarioRepository;
import com.easygest.springboot.repository.ProdutoRepository;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockitoBean
    private FornecedorRepository fornecedorRepository;

    @MockitoBean
    private FuncionarioRepository funcionarioRepository;

    @MockitoBean
    private ProdutoRepository produtoRepository;

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

    @Test
    @DisplayName("Cadastro de fornecedor aceita os nomes enviados pelo formulário")
    void cadastroFornecedorComPayloadDoFrontend() throws Exception {
        when(fornecedorRepository.save(any(Fornecedor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/fornecedores")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeFornecedor": "Razão Social Ltda",
                                  "nomeFantasiaFornecedor": "Nome Fantasia",
                                  "cnpjFornecedor": "12345678000199",
                                  "telefoneFornecedor": "11999999999",
                                  "emailFornecedor": "contato@example.com",
                                  "categoriaFornecedor": "Alimentos",
                                  "dataCadastro": "2026-11-18"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razaoSocialFornecedor").value("Razão Social Ltda"));

        ArgumentCaptor<Fornecedor> fornecedorCaptor = ArgumentCaptor.forClass(Fornecedor.class);
        verify(fornecedorRepository).save(fornecedorCaptor.capture());
        Fornecedor salvo = fornecedorCaptor.getValue();
        assertEquals("Razão Social Ltda", salvo.getRazaoSocialFornecedor());
        assertEquals("Nome Fantasia", salvo.getNomeFantasiaFornecedor());
        assertEquals("Alimentos", salvo.getCategoriaFornecedor());
        assertEquals("2026-11-18", salvo.getDataCadastroFornecedor().toString());
        assertNull(salvo.getEnderecoFornecedor());
    }

    @Test
    @DisplayName("Fornecedor sem razão social ou com e-mail inválido retorna 400 detalhado")
    void fornecedorComCamposObrigatoriosOuInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/fornecedores")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeFornecedor": " ",
                                  "emailFornecedor": "email-invalido"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nomeFornecedor").exists())
                .andExpect(jsonPath("$.errors.emailFornecedor").exists());
    }

    @Test
    @DisplayName("Cadastro de funcionário não depende de usuario enviado pelo cliente")
    void cadastroFuncionarioSemUsuario() throws Exception {
        when(funcionarioRepository.save(any(Funcionario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/funcionarios")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeFuncionario": "Maria Silva",
                                  "cpfFuncionario": "12345678900",
                                  "ctpsFuncionario": "12345",
                                  "cargoFuncionario": "Vendedora",
                                  "salarioFuncionario": 2500.00,
                                  "dataAdmissaoFuncionario": "2026-11-01",
                                  "dataDemissaoFuncionario": null,
                                  "usuario": {"codUsuario": "id-arbitrario-do-cliente"}
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeFuncionario").value("Maria Silva"));

        ArgumentCaptor<Funcionario> funcionarioCaptor = ArgumentCaptor.forClass(Funcionario.class);
        verify(funcionarioRepository).save(funcionarioCaptor.capture());
        Funcionario salvo = funcionarioCaptor.getValue();
        assertNull(salvo.getUsuario());
        assertEquals("Maria Silva", salvo.getNomeFuncionario());
        assertEquals("2500.00", salvo.getSalarioFuncionario().toString());
        assertEquals("2026-11-01", salvo.getDataAdmissaoFuncionario().toString());
    }

    @Test
    @DisplayName("Funcionário sem nome/cargo ou com salário negativo retorna 400 detalhado")
    void funcionarioComCamposObrigatoriosOuInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/funcionarios")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeFuncionario": " ",
                                  "cargoFuncionario": "",
                                  "salarioFuncionario": -1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nomeFuncionario").exists())
                .andExpect(jsonPath("$.errors.cargoFuncionario").exists())
                .andExpect(jsonPath("$.errors.salarioFuncionario").exists());
    }

    @Test
    @DisplayName("Data de cadastro com formato inválido retorna 400")
    void dataInvalidaRetorna400Claro() throws Exception {
        mockMvc.perform(post("/fornecedores")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeFornecedor": "Razão Social Ltda",
                                  "dataCadastro": "18/11/2026"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Corpo da requisição inválido. Verifique o JSON, as datas e os valores informados."));
    }

    @Test
    @DisplayName("PUT e GET de produto retornam fornecedor resolvido pelo código")
    void produtoAtualizadoEConsultadoComFornecedor() throws Exception {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodFornecedor("forn-1");
        fornecedor.setRazaoSocialFornecedor("Fornecedor ABC Ltda");
        fornecedor.setNomeFornecedor("Fornecedor ABC Ltda");
        fornecedor.setCnpjFornecedor("12345678000199");

        Produto produto = new Produto();
        produto.setCodProduto("prod-1");
        produto.setNomeProduto("Produto");
        when(produtoRepository.findById("prod-1")).thenReturn(Optional.of(produto));
        when(fornecedorRepository.findById("forn-1")).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/produtos/prod-1")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeProduto": "Produto",
                                  "fornecedor": {"codFornecedor": "forn-1"}
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedor.codFornecedor").value("forn-1"))
                .andExpect(jsonPath("$.fornecedor.nomeFornecedor").value("Fornecedor ABC Ltda"))
                .andExpect(jsonPath("$.fornecedor.cnpjFornecedor").doesNotExist());

        mockMvc.perform(get("/produtos/prod-1")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedor.codFornecedor").value("forn-1"))
                .andExpect(jsonPath("$.fornecedor.nomeFornecedor").value("Fornecedor ABC Ltda"));
    }

    @Test
    @DisplayName("Produto sem fornecedor continua sem fornecedor após PUT e GET")
    void produtoSemFornecedorContinuaSemVinculo() throws Exception {
        Produto produto = new Produto();
        produto.setCodProduto("prod-sem-fornecedor");
        produto.setNomeProduto("Produto avulso");
        when(produtoRepository.findById("prod-sem-fornecedor")).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/produtos/prod-sem-fornecedor")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nomeProduto": "Produto avulso"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedor").value(org.hamcrest.Matchers.nullValue()));

        mockMvc.perform(get("/produtos/prod-sem-fornecedor")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedor").value(org.hamcrest.Matchers.nullValue()));

        verifyNoInteractions(fornecedorRepository);
    }

    @Test
    @DisplayName("PUT rejeita fornecedor inexistente com erro 400 claro")
    void produtoRejeitaFornecedorInexistente() throws Exception {
        Produto produto = new Produto();
        produto.setCodProduto("prod-1");
        produto.setNomeProduto("Produto");
        when(produtoRepository.findById("prod-1")).thenReturn(Optional.of(produto));
        when(fornecedorRepository.findById("forn-inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(put("/produtos/prod-1")
                        .header(HttpHeaders.AUTHORIZATION, headerBasic(USUARIO_TESTE, SENHA_TESTE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeProduto": "Produto",
                                  "fornecedor": {"codFornecedor": "forn-inexistente"}
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Fornecedor não encontrado para codFornecedor: forn-inexistente"));
    }
}
