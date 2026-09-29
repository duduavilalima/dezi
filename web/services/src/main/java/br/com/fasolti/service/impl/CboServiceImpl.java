package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AtividadeCBO;
import br.com.fasolti.avd.domain.OcupacaoCBO;
import br.com.fasolti.avd.domain.PerfilCBO;
import br.com.fasolti.avd.repository.AtividadeCBORepository;
import br.com.fasolti.avd.repository.OcupacaoCBORepository;
import br.com.fasolti.avd.repository.PerfilCBORepository;
import br.com.fasolti.avd.service.CboService;
import br.com.fasolti.avd.transfer.AtividadeCBOTO;
import br.com.fasolti.avd.transfer.OcupacaoCBOTO;
import br.com.fasolti.avd.transfer.PerfilCBOTO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CboServiceImpl implements CboService {

	@Autowired
	private Mapper mapper;

	@Autowired
	private OcupacaoCBORepository ocupacaoCBORepository;

	@Autowired
	private PerfilCBORepository perfilCBORepository;

	@Autowired
	private AtividadeCBORepository atividadeCBORepository;

	@Override
	public List<OcupacaoCBOTO> listarTodosCboOcupcoes() {
		List<OcupacaoCBOTO> list = new ArrayList<OcupacaoCBOTO>();
		for (OcupacaoCBO ocupacaoCBO : ocupacaoCBORepository.findAll()) {
			list.add(mapper.map(ocupacaoCBO, OcupacaoCBOTO.class));
		}
		return list;
	}

	@Override
	public List<PerfilCBOTO> listarPerfisPorOcupacao(Long ocupacaoId) {
		List<PerfilCBOTO> list = new ArrayList<PerfilCBOTO>();
		for (PerfilCBO perfilCBO : perfilCBORepository.findByOcupacaoId(ocupacaoId)) {
            /*
             * Segundo a tarefa AVD-344, perfis pessoais não entram
             * na lista da ocupação. Perfil pessoal é igual ao código "Z".
             */
            if (!perfilCBO.getPerfil().equalsIgnoreCase("Z")) {
                PerfilCBOTO perfilCBOTO = mapper.map(perfilCBO, PerfilCBOTO.class);

                perfilCBOTO.setListaDeAtividades(new ArrayList<AtividadeCBOTO>());

                List<AtividadeCBO> listaDeAtividades = atividadeCBORepository.findByPerfilId(perfilCBO.getId());

                for (AtividadeCBO atividadeCBO : listaDeAtividades) {
                    perfilCBOTO.getListaDeAtividades().add(mapper.map(atividadeCBO, AtividadeCBOTO.class));
                }

                Collections.sort(perfilCBOTO.getListaDeAtividades(), (AtividadeCBOTO o1, AtividadeCBOTO o2) -> o1.getDescricao().compareTo(o2.getDescricao()));

                list.add(perfilCBOTO);
            }
		}

		Collections.sort(list, (PerfilCBOTO o1, PerfilCBOTO o2) -> o1.getDescricao().compareTo(o2.getDescricao()));

		return list;
	}

	@Override
	public List<AtividadeCBOTO> listarAtividadesPorPerfil(Long perfilId) {
		List<AtividadeCBOTO> list = new ArrayList<AtividadeCBOTO>();
		for (AtividadeCBO atividadeCBO : atividadeCBORepository.findByPerfilId(perfilId)) {
			list.add(mapper.map(atividadeCBO, AtividadeCBOTO.class));
		}
		return list;
	}

	@Override
	public List<OcupacaoCBOTO> autocompleteBuscar(OcupacaoCBOTO to) {
		List<OcupacaoCBO> list = ocupacaoCBORepository.findByCodigoContainingIgnoreCaseOrDescricaoContainingIgnoreCase(to.getCodigo(), to.getDescricao());
        Collections.sort(list, (OcupacaoCBO obj1, OcupacaoCBO obj2) -> obj1.getCodigo().compareTo(obj2.getCodigo()));
        Collections.sort(list, (OcupacaoCBO obj1, OcupacaoCBO obj2) -> obj1.getDescricao().compareTo(obj2.getDescricao()));

        List<OcupacaoCBOTO> listTO = new ArrayList<OcupacaoCBOTO>();

        for (OcupacaoCBO ocupacaoCBO : list) {
            listTO.add(mapper.map(ocupacaoCBO, OcupacaoCBOTO.class));
        }

        return listTO;
	}
}
