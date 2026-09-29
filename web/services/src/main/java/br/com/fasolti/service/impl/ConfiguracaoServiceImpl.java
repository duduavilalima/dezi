package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Configuracao;
import br.com.fasolti.avd.repository.ConfiguracaoRepository;
import br.com.fasolti.avd.service.ConfiguracaoService;
import br.com.fasolti.avd.transfer.ConfiguracaoTO;
import br.com.fasolti.commons.services.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ConfiguracaoServiceImpl
	extends BaseCrudService<Configuracao, ConfiguracaoTO>
	implements ConfiguracaoService {

	@Autowired
	private ConfiguracaoRepository configuracaoRepository;

	@Autowired
	public ConfiguracaoServiceImpl(ConfiguracaoRepository configuracaoRepository) {
		super(configuracaoRepository);
	}

	@Override
	@Transactional
	public ConfiguracaoTO findByFuncionarioId(Long funcionarioId) {
		List<Configuracao> configs = configuracaoRepository.findByFuncionarioIdOrderByIdDesc(funcionarioId);
		Configuracao config = configs.get(0);
		configs.remove(config);
		this.delete(map(configs));
		return map(config);
	}
}
