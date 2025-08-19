package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.MedicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @MockitoBean
    private MedicoRepository medicoRepository;

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver codigo http 400 quando informações estao invalidas")
    void CadastrarCenario1() throws Exception {
        var response = mvc.perform(post("/medicos"))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver codigo http 200 quando informações estao validas")
    void CadastrarCenario2() throws Exception {
        var endereco = new Endereco(
                "Rua de Teste",
                "Bairro de Teste",
                "12345678",
                "42",
                "Apto 101",
                "Cidade de Teste",
                "TS"
        );

        var dadosEndereco = new DadosEndereco(  "Rua de Teste",
                "Bairro de Teste",
                "12345678",   // CEP válido (8 dígitos)
                "Cidade de Teste",
                "TS",         // UF fictícia
                "Apto 101",
                "42");
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalhamento = new DadosDetalhamentoMedico(null, "teste", "teste123@teste.com", "123456", "123456789", especialidade, endereco);

        var response = mvc.perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJson.write(
                                new DadosCadastroMedico(
                                        "teste",
                                        "teste123@teste.com",
                                        "123456789",
                                        "123456",              // CRM com 5 dígitos (válido pelo regex \\d{4,6})
                                        especialidade, // exemplo de enum (ajuste para uma que exista no seu projeto)
                                        dadosEndereco
                                )
                        ).getJson()))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(
                dadosDetalhamento
        ).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}