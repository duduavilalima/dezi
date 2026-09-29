package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.ApontamentoFuncional;
import br.com.fasolti.avd.domain.QApontamentoFuncional;
import br.com.fasolti.avd.repository.ApontamentoFuncionalRepository;
import br.com.fasolti.avd.service.FeedForwardRecebidoService;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.ApontamentoFuncionalTO;
import br.com.fasolti.avd.transfer.FotoTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.FeedForwardMobileTO;
import br.com.fasolti.avd.transfer.mobile.TipoCompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.UsuarioMobileTO;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FeedForwardRecebidoServiceImpl
		extends BaseAvdService<ApontamentoFuncional, ApontamentoFuncionalTO, ApontamentoFuncionalTO>
		implements FeedForwardRecebidoService {

	@Autowired
	private FotoService fotoService;

	@Autowired
	private ApontamentoFuncionalRepository apontamentoFuncionalRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	public FeedForwardRecebidoServiceImpl(ApontamentoFuncionalRepository repository) {
		super(repository);
	}

	private static final QApontamentoFuncional qApontamentoFuncional = QApontamentoFuncional.apontamentoFuncional;

	/* O usuário logado É O AVALIADO */
	@Override
	protected ServiceFilter<ApontamentoFuncional> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO funcionario = getLoggedUser();
		switch (funcionario.getTipoPerfil()) {
		case ADMINISTRADOR:
		case GESTOR_RH:
			mandatoryFilter.and(qApontamentoFuncional.avaliado.empresa.id.eq(funcionario.getEmpresa().getId()));
			break;
		// case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
		case GESTOR_AREA:
		default:
			mandatoryFilter.and(qApontamentoFuncional.avaliado.id.eq(funcionario.getId()));
			break;
		}

		return ServiceFilter.create(qApontamentoFuncional, mandatoryFilter);
	}

	@Override
	public List<FeedForwardMobileTO> obterFeedForwardsRecebidos(UsuarioMobileTO avaliado) {
		List<ApontamentoFuncional> list = apontamentoFuncionalRepository
				.findByAvaliadoIdAndFeedforwardIsNotNull(avaliado.getId());
		Collections.sort(list, new Comparator<ApontamentoFuncional>() {
			@Override
			public int compare(ApontamentoFuncional o1, ApontamentoFuncional o2) {
				return o2.getDataApontamento().compareTo(o1.getDataApontamento());
			}
		});
		List<FeedForwardMobileTO> apontamentoMobileTOList = new ArrayList<>();
		for (ApontamentoFuncional apontamentoFuncional : list) {
			FeedForwardMobileTO feedForwardsRecebidoTO = new FeedForwardMobileTO();
			feedForwardsRecebidoTO.setFeedforward(apontamentoFuncional.getFeedforward());
			feedForwardsRecebidoTO.setId(apontamentoFuncional.getId());
			feedForwardsRecebidoTO.setAvaliadorId(apontamentoFuncional.getAvaliador().getId());
			feedForwardsRecebidoTO.setAvaliadoId(apontamentoFuncional.getAvaliado().getId());
			feedForwardsRecebidoTO.setGraduacaoVisual(apontamentoFuncional.getGraduacaoVisual());
			feedForwardsRecebidoTO.setDataApontamento(apontamentoFuncional.getDataApontamento());

			FotoTO foto = fotoService.obterFotoPerfilFuncionario(apontamentoFuncional.getAvaliador().getId());
			if (foto != null) {
				feedForwardsRecebidoTO.setFoto(foto.getBase64());
			} else {
				feedForwardsRecebidoTO.setFoto("");
			}

			CompetenciaMobileTO competenciaMobileTO = new CompetenciaMobileTO();
			competenciaMobileTO.setDescricaoAbreviada(apontamentoFuncional.getCompetencia().getDescricaoAbreviada());
			competenciaMobileTO.setId(apontamentoFuncional.getCompetencia().getId());

			/*TipoCompetenciaMobileTO tipoCompetenciaMobileTO = new TipoCompetenciaMobileTO();
			tipoCompetenciaMobileTO.setNomeCurto(apontamentoFuncional.getCompetencia().getTipo().getNomeCurto());
			tipoCompetenciaMobileTO.setNome(apontamentoFuncional.getCompetencia().getTipo().getNome());
			tipoCompetenciaMobileTO.setPaiId(apontamentoFuncional.getCompetencia().getTipo().getIdPai());
			tipoCompetenciaMobileTO.setDescricao(apontamentoFuncional.getCompetencia().getTipo().getDescricao());
			tipoCompetenciaMobileTO.setId(apontamentoFuncional.getCompetencia().getTipo().getId());

			competenciaMobileTO.setTipo(tipoCompetenciaMobileTO);
*/
			feedForwardsRecebidoTO.setCompetencia(competenciaMobileTO);

			apontamentoMobileTOList.add(feedForwardsRecebidoTO);
		}

		return apontamentoMobileTOList;
	}

 }
