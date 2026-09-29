package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.BiParametroUsuario;
import br.com.fasolti.avd.domain.Campanha;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.repository.BiParametroUsuarioRepository;
import br.com.fasolti.avd.repository.CampanhaRepository;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.service.BiParametroUsuarioService;
import br.com.fasolti.avd.transfer.BiParametroUsuarioTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.service.util.BaseAvdService;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class BiParametroServiceImpl extends BaseAvdService<BiParametroUsuario, BiParametroUsuarioTO, BiParametroUsuarioTO> implements BiParametroUsuarioService {

	@Autowired
	BiParametroUsuarioRepository biParametroUsuarioRepository;

	@Autowired
	CampanhaRepository campanhaRepository;

	@Autowired
	FuncionarioRepository funcionarioRepository;

	@Autowired
	public BiParametroServiceImpl(BiParametroUsuarioRepository biParametroUsuarioRepository) {
		super(biParametroUsuarioRepository);
	}

	@Override
	@Transactional
	public BiParametroUsuarioTO criarParametros(Long campanhaId, Long idFuncionario) {
		Campanha campanha = null;
		if (campanhaId != null && campanhaId > 0 )
			campanha = campanhaRepository.findOne(campanhaId);
		UserTO userTO = getLoggedUser();

		BiParametroUsuario biParametroUsuario = new BiParametroUsuario();
		biParametroUsuario.setIdFuncionario(userTO.getId());
		biParametroUsuario.setIdPerfil(userTO.getTipoPerfil().getCode());
		biParametroUsuario.setIdEmpresa(getLoggedUser().getEmpresa().getId());
		biParametroUsuario.setIdCampanha(campanhaId);
		biParametroUsuario.setNomeGestor(TipoPerfil.GESTOR_AREA.equals(userTO.getTipoPerfil())?userTO.getNome():"%");
		biParametroUsuario.setCampanhaDescricao(campanha != null ? campanha.getDescricao() : "");
		biParametroUsuario.setDataCriacao(new Date(System.currentTimeMillis()));
		biParametroUsuario.setNomeFuncionario(TipoPerfil.FUNCIONARIO.equals(userTO.getTipoPerfil())?userTO.getNome():"%");

		if (campanhaId != null && campanhaId > 0 && !TipoPerfil.FUNCIONARIO.equals(userTO.getTipoPerfil()))
			biParametroUsuario.setNomeFuncionario("%");
		else if (campanhaId != null && campanhaId > 0)
			biParametroUsuario.setNomeFuncionario(userTO.getNome());
		else
			biParametroUsuario.setNomeFuncionario(funcionarioRepository.findOne(idFuncionario).getNome());

		String chaveOriginal = System.currentTimeMillis() + "" + biParametroUsuario.getIdFuncionario();
		String sha256hex = Hashing.sha256()
			.hashString(chaveOriginal, StandardCharsets.UTF_8)
			.toString();

		biParametroUsuario.setChave(sha256hex);
		biParametroUsuarioRepository.save(biParametroUsuario);

		// Objeto criado apenas para redudir a quantidade de dados transmitida, no frontend somente o campo chave é necessário
		BiParametroUsuarioTO to = new BiParametroUsuarioTO();
		to.setChave(sha256hex);

		return to;
	}
}
