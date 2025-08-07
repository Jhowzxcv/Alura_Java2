package med.voll.api.domain.consulta.validacoes.cancelamento;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaDeCancelamento implements ValidadorCancelamentoDeConsulta {

    @Autowired
    ConsultaRepository repository;

    public void validar(DadosCancelamentoConsulta dados) {
        var agora = LocalDateTime.now();
        var consulta = repository.getReferenceById(dados.idConsulta());
        var dataConsulta = consulta.getData();

        if (dataConsulta.isBefore(agora.plusHours(24))){
            throw new ValidationException("O cancelamento deve ser solicitado no maximo 24 horas antes da consulta");
        }
    }
}
