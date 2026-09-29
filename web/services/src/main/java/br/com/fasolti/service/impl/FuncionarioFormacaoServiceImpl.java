package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.FuncionarioFormacao;
import br.com.fasolti.avd.domain.QFuncionarioFormacao;
import br.com.fasolti.avd.repository.FuncionarioFormacaoRepository;
import br.com.fasolti.avd.service.FuncionarioFormacaoService;
import br.com.fasolti.avd.transfer.FuncionarioFormacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioFormacaoServiceImpl
	extends BaseAvdService<FuncionarioFormacao, FuncionarioFormacaoTO, FuncionarioFormacaoTO>
	implements FuncionarioFormacaoService {

	private static final QFuncionarioFormacao qFuncionarioFormacao = QFuncionarioFormacao.funcionarioFormacao;

	@Autowired
	private FuncionarioFormacaoRepository funcionarioFormacaoRepository;

	@Autowired
	public FuncionarioFormacaoServiceImpl(FuncionarioFormacaoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FuncionarioFormacaoTO exampleObj) {
		builder.and(qFuncionarioFormacao.funcionario.id.eq(exampleObj.getFuncionario().getId()));
	}

	@Override
	public List<FuncionarioFormacaoTO> listByFuncionario(FuncionarioTO funcionario) {
		return map(repository.findAll(qFuncionarioFormacao.funcionario.id.eq(funcionario.getId())));
	}

}
