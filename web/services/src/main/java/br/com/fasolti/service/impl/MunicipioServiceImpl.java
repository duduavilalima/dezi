package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Municipio;
import br.com.fasolti.avd.domain.QMunicipio;
import br.com.fasolti.avd.service.MunicipioService;
import br.com.fasolti.avd.transfer.MunicipioTO;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.domain.DomainObject;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class MunicipioServiceImpl implements MunicipioService {

	private static final QMunicipio qMunicipio = QMunicipio.municipio;

	@Autowired
	private Mapper dozerMapper;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MunicipioTO> byUf(String uf) {
		JPAQuery<Municipio> query = this.jpaQueryFactory.selectFrom(qMunicipio);
		return map(query
			.select(Projections.fields(
				Municipio.class,
				qMunicipio.id,
				qMunicipio.nome,
				qMunicipio.estado
			))
			.where(qMunicipio.uf.lower().eq(uf.toLowerCase()))
			.fetch());
	}

	@Override
	public List<MunicipioTO> containsNome(String nome) {
		JPAQuery<Municipio> query = this.jpaQueryFactory.selectFrom(qMunicipio);
		return map(query
			.select(Projections.fields(
				Municipio.class,
				qMunicipio.id,
				qMunicipio.nome
			))
			.where(qMunicipio.nome.lower().contains(nome.toLowerCase()))
			.fetch());
	}

	@Override
	public List<MunicipioTO> containsNomeByUf(String uf, String nome) {
		JPAQuery<Municipio> query = this.jpaQueryFactory.selectFrom(qMunicipio);
		return map(query
			.select(Projections.fields(
				Municipio.class,
				qMunicipio.id,
				qMunicipio.nome
			))
			.where(qMunicipio.nome.lower().contains(nome.toLowerCase()).and(
				qMunicipio.uf.lower().contains(uf.toLowerCase())
			))
			.fetch());
	}

	protected final MunicipioTO map(Municipio entity) {
		return entity == null ? null : this.dozerMapper.map(entity, MunicipioTO.class);
	}

	protected final Municipio map(MunicipioTO object) {
		return object == null ? null : this.dozerMapper.map(object, Municipio.class);
	}

	protected final List<MunicipioTO> map(Iterable<Municipio> iterable) {
		return this.dozerMapper.map(iterable, List.class);
	}

}
