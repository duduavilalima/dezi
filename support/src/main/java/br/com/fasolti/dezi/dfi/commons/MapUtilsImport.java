package br.com.fasolti.dezi.dfi.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.CompetenciaDetalhamento;
import br.com.fasolti.avd.domain.QCargo;
import br.com.fasolti.avd.domain.QDepartamento;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.dezi.dfi.repositories.CompetenciaDetalhamentoRepository;
import br.com.fasolti.dezi.dfi.repositories.CompetenciaRepository;
import br.com.fasolti.dezi.dfi.repositories.TipoCompetenciaRepository;

/**
 * Classe usanda para montar HashMap caso seja necessário importa planilha individual
 * @author denis.oliveira
 *
 */
@Component
public class MapUtilsImport {
	
	@Autowired
	private JPAQueryFactory jPAQueryFactory;

	
	@Autowired
	private TipoCompetenciaRepository tipoCompetenciaRepository;
	
	@Autowired
	private CompetenciaRepository competenciaRepository;
	
	@Autowired
	private CompetenciaDetalhamentoRepository competenciaDetalhamentoRepository;
	
	@Value("${dezi.createdby.name}")
	private String createdByName;
	
	private Map<String, TipoCompetencia> mapTipoCompetencia = new HashMap<>();
	private Map<String, Competencia> mapCompetencia = new HashMap<>();
	private Map<String, CompetenciaDetalhamento> mapCompetenciaDetalhamento = new HashMap<>();
	private Map<String, Cargo> mapCargo = new HashMap<>();
	
	/**
	 * Map cargo formato de importação de planilha
	 * @return {@link HashMap}
	 */
	public Map<String, Cargo> getCargoMap(Long idEmpresa) {
		List<Cargo> list = new ArrayList<Cargo>(); 
		list = (List<Cargo>) jPAQueryFactory.select(
				Projections.fields(Cargo.class, 
						QCargo.cargo.id, 
						QCargo.cargo.nome,
						Projections.fields(QDepartamento.departamento, 
								QDepartamento.departamento.id,
								QDepartamento.departamento.nome)
						.as(QDepartamento.departamento)
				))
				.from(QCargo.cargo)
				.innerJoin(QCargo.cargo.departamento, QDepartamento.departamento)
				.where(QCargo.cargo.empresa.id.eq(idEmpresa)).fetch();
		for (Cargo x : list) {
			mapCargo.put(x.getNome(), x);
		}
		return mapCargo;
	}
	
	/**
	 * Map TipoCompetencia formato de importação de planilha
	 * @return {@link HashMap}
	 */
	public Map<String, TipoCompetencia> getTipoCompetenciaMap(Long idEmpresa) {
		List<TipoCompetencia> list = (List<TipoCompetencia>) tipoCompetenciaRepository.findByIdEmpresa(idEmpresa);
		list.forEach(x -> {
			mapTipoCompetencia.put(x.getNome(), x);
		});
		return mapTipoCompetencia;
	}
	
	/**
	 * Map Competencia formato de importação de planilha
	 * @return {@link HashMap}
	 */
	public Map<String, Competencia> getCompetenciaMap(Long idEmpresa) {
		List<Competencia> list = (List<Competencia>) competenciaRepository.findByEmpresaId(idEmpresa);
		list.forEach(x -> {
			mapCompetencia.put(x.getDescricaoAbreviada(), x);
		});
		return mapCompetencia;
	}
	
	/**
	 * Map CompetenciaDetalhamento formato de importação de planilha
	 * @return {@link HashMap}
	 */
	public Map<String, CompetenciaDetalhamento> getCompetenciaDetalhamentoMap() {
		//FIX: Refatorar, usar uma outra forma de busca
		List<CompetenciaDetalhamento> list = (List<CompetenciaDetalhamento>) competenciaDetalhamentoRepository.findBycriador(createdByName);
		list.forEach(x -> {
			mapCompetenciaDetalhamento.put(x.getDescricao(), x);
		});
		return mapCompetenciaDetalhamento;
	}
	
}
