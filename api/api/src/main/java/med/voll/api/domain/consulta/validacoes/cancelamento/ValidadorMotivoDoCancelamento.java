package med.voll.api.domain.consulta.validacoes.cancelamento;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMotivoDoCancelamento implements ValidadorCancelamentoDeConsulta {

    public void validar(DadosCancelamentoConsulta dados) {
        var motivo = dados.motivoCancelamento();
        if (motivo == null){
            throw new ValidationException("Motivo do cancelamento nao informado!");
        }
    }
}
