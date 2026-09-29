package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.PendenciaRepository;
import br.com.fasolti.avd.service.PendenciaService;
import br.com.fasolti.avd.transfer.PendenciaTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PendenciaServiceImpl extends BaseAvdService<Pendencia, PendenciaTO, PendenciaTO> implements PendenciaService {

	QPendencia qPendencia = QPendencia.pendencia;
	private static final QFuncionario qAvaliador = new QFuncionario("avaliador");
	private static final QFuncionario qAvaliado = new QFuncionario("avaliado");
	@Autowired
	private PendenciaRepository pendenciaRepository;

	@Autowired
	public PendenciaServiceImpl(PendenciaRepository pendenciaRepository) {
		super(pendenciaRepository);
	}

	@Override
	public List<PendenciaTO> findByAvaliacaoId(Long idAvaliacao) {

		JPAQuery<Pendencia> pendenciaQuery = select(Projections.fields(
			Pendencia.class,
			qPendencia.id,
			qPendencia.codigoErro,
			qPendencia.mensagemErro,
			qPendencia.dataUltimaAtualizacao,
			qPendencia.tipoPendencia,
			Projections.fields(Funcionario.class,
				qAvaliado.id,
				qAvaliado.nome
			).as("avaliado"),
			Projections.fields(Funcionario.class,
				qAvaliador.id,
				qAvaliador.nome
			).as("avaliador"),
			Projections.fields(Avaliacao.class,
				QAvaliacao.avaliacao.id,
				QAvaliacao.avaliacao.descricao
			).as("avaliacao"),
			Projections.fields(Cargo.class,
				QCargo.cargo.id,
				QCargo.cargo.nome
			).as("cargo")
		))
			.leftJoin(qPendencia.avaliado, qAvaliado)
			.leftJoin(qPendencia.avaliador, qAvaliador)
			.leftJoin(qPendencia.avaliacao, QAvaliacao.avaliacao)
			.leftJoin(qPendencia.cargo, QCargo.cargo)
		  	.where(qPendencia.avaliacao.id.eq(idAvaliacao));

		return map(pendenciaQuery.fetch());
	}
}
