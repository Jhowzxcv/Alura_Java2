package med.voll.api.domain.consulta.validacoes.agendamento;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorEspecialidadeMedico implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private MedicoRepository repository;

    @Override
    public void validar(DadosAgendamentoConsulta dados) {
        // Só valida se um médico foi explicitamente escolhido
        if (dados.idMedico() == null || dados.especialidade() == null) {
            return;
        }

        var medico = repository.getReferenceById(dados.idMedico());
        var especialidadeMedico = medico.getEspecialidade();

        if (!dados.especialidade().equals(especialidadeMedico)) {
            throw new ValidationException("O médico informado não atende à especialidade informada!");
        }
    }
}
