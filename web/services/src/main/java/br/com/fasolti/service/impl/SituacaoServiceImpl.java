package br.com.fasolti.service.impl;

import org.springframework.stereotype.Service;

@Service
public class SituacaoServiceImpl { //extends BaseService<Situacao, SituacaoTO> implements SituacaoService {

//	@Autowired
//	private SituacaoFactory situacaoFactory;
//
//	@Autowired
//	public SituacaoServiceImpl(SituacaoFactory situacaoFactory) {
//		super(null); // Não vai usar nada do pai além dos métodos de map
//	}
//
//	@Override
//	public SituacaoTO situacaoAtiva() {
//		return map(situacaoFactory.situacaoAtiva());
//	}
//
//	@Override
//	public SituacaoTO situacaoInativa() {
//		return map(situacaoFactory.situacaoInativa());
//	}
//
//	@Override
//	public List<SituacaoTO> listTipoSituacao() {
//		return map(situacaoFactory.listTipoSituacao());
//	}
//
//	@Override
//	public List<SituacaoTO> listTipoFuncional() {
//        List<Situacao> list = new ArrayList<Situacao>();
//        // AVD-255
//        for (Situacao situacao : situacaoFactory.listTipoFuncional()) {
//            if (!situacao.getNomeCurto().equalsIgnoreCase("Desligado(a)")) {
//                list.add(situacao);
//            }
//        }
//		return map(list);
//	}
//
//	@Override
//	public List<SituacaoTO> listFormacaoSituacaoConclusao() {
//		return map(situacaoFactory.listFormacaoSituacaoConclusao());
//	}
//
//	@Override
//	public List<SituacaoTO> listNivelIdioma() {
//		return map(situacaoFactory.listNivelIdioma());
//	}
//
//	@Override
//	public SituacaoTO situacaoDesligado() {
//		return map(situacaoFactory.situacaoDesligado());
//	}
//
//	@Override
//	public SituacaoTO situacaoEmAtividade() {
//		return map(situacaoFactory.situacaoEmAtividade());
//	}
//	private final SituacaoFactory situacaoFactory;
//
//	@Autowired
//	public SituacaoServiceImpl(SituacaoFactory situacaoFactory) {
//		super(null); // Não vai usar nada do pai além dos métodos de map
//		this.situacaoFactory = situacaoFactory;
//	}
//
//	@Override
//	public SituacaoTO situacaoAtiva() {
//		return map(situacaoFactory.situacaoAtiva());
//	}
//
//	@Override
//	public SituacaoTO situacaoInativa() {
//		return map(situacaoFactory.situacaoInativa());
//	}
//
//	@Override
//	public List<SituacaoTO> listTipoSituacao() {
//		return map(situacaoFactory.listTipoSituacao());
//	}
//
//	@Override
//	public List<SituacaoTO> listTipoFuncional() {
//        List<Situacao> list = new ArrayList<Situacao>();
//        // AVD-255
//        for (Situacao situacao : situacaoFactory.listTipoFuncional()) {
//            if (!situacao.getNomeCurto().equalsIgnoreCase("Desligado(a)")) {
//                list.add(situacao);
//            }
//        }
//		return map(list);
//	}
//
//	@Override
//	public List<SituacaoTO> listFormacaoSituacaoConclusao() {
//		return map(situacaoFactory.listFormacaoSituacaoConclusao());
//	}
//
//	@Override
//	public List<SituacaoTO> listNivelIdioma() {
//		return map(situacaoFactory.listNivelIdioma());
//	}
//
//	@Override
//	public SituacaoTO situacaoDesligado() {
//		return map(situacaoFactory.situacaoDesligado());
//	}
//
//	@Override
//	public SituacaoTO situacaoEmAtividade() {
//		return map(situacaoFactory.situacaoEmAtividade());
//	}

}
