package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.FuncionarioIdioma;
import br.com.fasolti.avd.domain.QFuncionarioIdioma;
import br.com.fasolti.avd.repository.FuncionarioIdiomaRepository;
import br.com.fasolti.avd.service.FuncionarioIdiomaService;
import br.com.fasolti.avd.transfer.FuncionarioIdiomaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioIdiomaServiceImpl
	extends BaseAvdService<FuncionarioIdioma, FuncionarioIdiomaTO, FuncionarioIdiomaTO>
	implements FuncionarioIdiomaService {

	private static final QFuncionarioIdioma qFuncionarioIdioma = QFuncionarioIdioma.funcionarioIdioma;

	@Autowired
	private FuncionarioIdiomaRepository funcionarioIdiomaRepository;

	@Autowired
	public FuncionarioIdiomaServiceImpl(FuncionarioIdiomaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FuncionarioIdiomaTO exampleObj) {
		builder.and(dslHelper.eq(qFuncionarioIdioma.funcionario, exampleObj.getFuncionario()));
	}

	@Override
	public List<FuncionarioIdiomaTO> listByFuncionario(FuncionarioTO funcionario) {
		return map(repository.findAll(qFuncionarioIdioma.funcionario.id.eq(funcionario.getId())));
	}
}
