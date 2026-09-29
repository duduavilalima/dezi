package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.PDIAcaoRepository;
import br.com.fasolti.avd.service.AvaliacaoParticipanteService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.service.PDIAcaoCompetenciaDetalhamentoService;
import br.com.fasolti.avd.service.PDIAcaoService;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.PDIAcaoSearchTO;
import br.com.fasolti.avd.transfer.simple.CompetenciaSimpleTO;
import br.com.fasolti.avd.types.SituacaoPDI;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseDeziService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PDIAcaoServiceImpl extends BaseDeziService<PDIAcao, PDIAcaoTO> implements PDIAcaoService {

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private PDIAcaoRepository pdiAcaoRepository;

	@Autowired
	private PDIAcaoCompetenciaDetalhamentoService pdiAcaoCompDetService;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private ResourceLoader resourceLoader;

	private final QPDIAcao qpdiAcao = QPDIAcao.pDIAcao;
	private final QPDIAcaoCompetenciaDetalhamento qpdiAcaoCompDet = QPDIAcaoCompetenciaDetalhamento.pDIAcaoCompetenciaDetalhamento;
	private final QParticipante qParticipante = QParticipante.participante;
	private final QFuncionario qFuncionario = QFuncionario.funcionario;
	private final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private final QCampanha qCampanha = QCampanha.campanha;
	private final QDepartamento qDepartamento = QDepartamento.departamento;

	@Autowired
	public PDIAcaoServiceImpl(PDIAcaoRepository pdiAcaoRepository) {
		super(pdiAcaoRepository);
	}

	@Override
	protected ServiceFilter<PDIAcao> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO user = getLoggedUser();
		switch (user.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
				mandatoryFilter.and(qpdiAcao.funcionario.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case GESTOR_AREA:
				if(qDepartamento.departamentoPai != null) {
					mandatoryFilter.and(qDepartamento.id.eq(user.getDepartamento().getId())
						.or(qDepartamento.departamentoPai.id.eq(user.getDepartamento().getId())));
				} else {
					mandatoryFilter.and(qDepartamento.id.eq(user.getDepartamento().getId()));
				}
				break;
			default:
				mandatoryFilter.and(qpdiAcao.funcionario.id.eq(user.getId()));
				break;
		}

		return ServiceFilter.create(qpdiAcao, mandatoryFilter);
	}

	private String mapToJson(Map<String, String> templateValues) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(templateValues);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*@Override
	protected void doBeforeSave(PDIAcaoTO object) throws ServiceException {
		UserTO loggedUser = getLoggedUser();
		if (!loggedUser.getTipoPerfil().equals(TipoPerfil.ADMINISTRADOR)
			&& !loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_AREA)
			&& !loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_RH) ) {
			throw new ValidationException("validation.permission", "pdi", object.getAcaoParaDesenvolvimento());
		}
	}*/

	@Override
	protected void doBeforeUpdate(PDIAcaoTO object) throws ServiceException {
		UserTO loggedUser = getLoggedUser();
		PDIAcao pdiAcao = pdiAcaoRepository.findOne(object.getId());
		/*APROVADO -> EM ANDAMENTO ou EM ANDAMENTO -> FINALIZADO: apenas o funcionário do PDI pode executar essa ação*/
		if (pdiAcao.getSituacao().equals(SituacaoPDI.APROVADO) && object.getSituacao().equals(SituacaoPDI.EM_ANDAMENTO)
			|| pdiAcao.getSituacao().equals(SituacaoPDI.EM_ANDAMENTO) && object.getSituacao().equals(SituacaoPDI.FINALIZADO)) {
			if (!getLoggedUser().getId().equals(object.getFuncionario().getId())) {
				throw new ValidationException("validation.not-employee", "pdi",  object.getAcaoParaDesenvolvimento());
			}
		} else {
			/*Apenas gestores podem editar o PDI*/
			if (!loggedUser.getTipoPerfil().equals(TipoPerfil.ADMINISTRADOR)
				&& !loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_AREA)
				&& !loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_RH)) {
				throw new ValidationException("validation.permission", "pdi", object.getAcaoParaDesenvolvimento());
			}
		}
	}

	@Override
	protected void doBeforeDelete(PDIAcaoTO object) throws ServiceException {
		/*Apenas gestores podem deletar o PDI*/
		UserTO loggedUser = getLoggedUser();
		if (!loggedUser.getTipoPerfil().equals(TipoPerfil.ADMINISTRADOR)
			&& !loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_AREA)
			&& !loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_RH)) {
			throw new ValidationException("validation.permission", "pdi", object.getAcaoParaDesenvolvimento());
		}

		List<PDIAcaoCompetenciaDetalhamentoTO> listAcoesCompDet = pdiAcaoCompDetService.listarAcaoPDICompetenciaaDetalhamento(object.getId());
		if (listAcoesCompDet != null || !listAcoesCompDet.isEmpty()) {
			pdiAcaoCompDetService.delete(listAcoesCompDet);
		}
	}

	@Override
	protected void doAfterUpdate(PDIAcao pdiAcao, PDIAcaoTO pdiAcaoTO) throws ServiceException {
		updateCompetencias(pdiAcaoTO);
	}

	@Override
	protected void doAfterSave(PDIAcao pdiAcao, PDIAcaoTO pdiAcaoTO) throws ServiceException {
		if (pdiAcaoTO.getPdiAcaoCompetencias() != null && pdiAcaoTO.getPdiAcaoCompetencias().size() > 0) {
			salvarCompetencias(pdiAcaoTO);
		}
	}

	@Override
	public List<PDIAcaoTO> listarAcaoPDI(Long IdFuncionario) {
		return map(
			select(
				Projections.fields(
					PDIAcao.class,
					qpdiAcao.id,
					qpdiAcao.situacao,
					qpdiAcao.acaoParaDesenvolvimento,
					qpdiAcao.dataPrevistaInicio,
					qpdiAcao.dataPrevistaFim,
					qpdiAcao.dataInicio,
					qpdiAcao.dataFim,
					qpdiAcao.observacaoInicial,
					qpdiAcao.observacaoFinal,
					qpdiAcao.revisado,
					qpdiAcao.criador,
					qpdiAcao.dataCriacao,
					qpdiAcao.atualizador,
					qpdiAcao.dataAtualizacao,
					Projections.fields(
						Funcionario.class,
						qFuncionario.id,
						qFuncionario.nome,
						qFuncionario.matricula
					).as(qpdiAcao.funcionario),
					Projections.fields(
						Participante.class,
						qParticipante.id,
						qParticipante.idCargoAvaliado,
						qParticipante.nomeCargoAvaliado,
						qParticipante.idDeptoAvaliado,
						qParticipante.nomeDeptoAvaliado,
						Projections.fields(
							Avaliacao.class,
							qAvaliacao.id,
							qAvaliacao.descricaoAbreviada,
							Projections.fields(
								Campanha.class,
								qCampanha.id,
								qCampanha.descricao
							).as(qAvaliacao.campanha)
						).as(qParticipante.avaliacao)
					).as(qpdiAcao.participante)
				)
			)
				.innerJoin(qpdiAcao.funcionario, qFuncionario)
				.leftJoin(qpdiAcao.participante, qParticipante)
				.leftJoin(qParticipante.avaliacao, qAvaliacao)
				.leftJoin(qAvaliacao.campanha, qCampanha)
				.where(qpdiAcao.funcionario.id.eq(IdFuncionario))
				.orderBy(qpdiAcao.id.asc())
				.fetch()
		);
	}

	@Override
	public List<PDIAcaoTO> listarAcaoPDIPorSituacao(Long IdFuncionario, SituacaoPDI situacaoPDI) {
		return map(
			select(
				Projections.fields(
					PDIAcao.class,
					qpdiAcao.id,
					qpdiAcao.situacao,
					qpdiAcao.acaoParaDesenvolvimento,
					qpdiAcao.dataPrevistaInicio,
					qpdiAcao.dataPrevistaFim,
					qpdiAcao.dataInicio,
					qpdiAcao.dataFim,
					qpdiAcao.observacaoInicial,
					qpdiAcao.observacaoFinal,
					qpdiAcao.revisado,
					qpdiAcao.criador,
					qpdiAcao.dataCriacao,
					qpdiAcao.atualizador,
					qpdiAcao.dataAtualizacao,
					Projections.fields(
						Funcionario.class,
						qFuncionario.id,
						qFuncionario.nome,
						qFuncionario.matricula
					).as(qpdiAcao.funcionario),
					Projections.fields(
						Participante.class,
						qParticipante.id,
						qParticipante.idCargoAvaliado,
						qParticipante.nomeCargoAvaliado,
						qParticipante.idDeptoAvaliado,
						qParticipante.nomeDeptoAvaliado,
						Projections.fields(
							Avaliacao.class,
							qAvaliacao.id,
							qAvaliacao.descricaoAbreviada,
							Projections.fields(
								Campanha.class,
								qCampanha.id,
								qCampanha.descricao
							).as(qAvaliacao.campanha)
						).as(qParticipante.avaliacao)
					).as(qpdiAcao.participante)
				)
			)
				.innerJoin(qpdiAcao.funcionario, qFuncionario)
				.leftJoin(qpdiAcao.participante, qParticipante)
				.leftJoin(qParticipante.avaliacao, qAvaliacao)
				.leftJoin(qAvaliacao.campanha, qCampanha)
				.join(qFuncionario.departamento, qDepartamento)
				.where(qpdiAcao.funcionario.id.eq(IdFuncionario).and(qpdiAcao.situacao.eq(situacaoPDI)))
				.orderBy(qpdiAcao.id.asc())
				.fetch());
	}

	@Override
	public List<PDIAcaoTO> listarPorPDIacaoAvaliado(Long IdFuncionario) {
		List<PDIAcaoTO> listAcoes = listarAcaoPDI(IdFuncionario);
		List<PDIAcaoTO> listAcoesTemp = new ArrayList<>();

		if (!listAcoes.isEmpty()) {
			for (PDIAcaoTO pdi : listAcoes) {
				//FIXME: Está trazendo a participante com um objeto vazio, na hora de persistir, dá erro de transient {}
				if(pdi.getParticipante().getId() == null) {
					pdi.setParticipante(null);
				}
				pdi.setPdiAcaoCompetencias(pdiAcaoCompDetService.listarAcaoPDICompetenciaaDetalhamento(pdi.getId()));
				listAcoesTemp.add(pdi);
			}
		}
		return listAcoesTemp;
	}

	@Override
	public List<PDIAcaoTO> listarPorPDIacaoAvaliadoPorSituacao(Long IdFuncionario, SituacaoPDI situacaoPDI) {
		List<PDIAcaoTO> listAcoes = listarAcaoPDIPorSituacao(IdFuncionario, situacaoPDI);
		List<PDIAcaoTO> listAcoesTemp = new ArrayList<>();

		if (!listAcoes.isEmpty()) {
			for (PDIAcaoTO pdi : listAcoes) {
				//FIXME: Está trazendo a participante com um objeto vazio, na hora de persistir, dá erro de transient {}
				if(pdi.getParticipante() != null && pdi.getParticipante().getId() == null) {
					pdi.setParticipante(null);
				}
				pdi.setPdiAcaoCompetencias(pdiAcaoCompDetService.listarAcaoPDICompetenciaaDetalhamento(pdi.getId()));
				listAcoesTemp.add(pdi);
			}
		}
		return listAcoesTemp;
	}

	private PDIAcaoTO obterPDIAcao(Long idPDI) {
		return map(pdiAcaoRepository.findOne(idPDI));
	}

	private void salvarCompetencias(PDIAcaoTO pdiAcaoTO) throws ValidationException {
		pdiAcaoTO.getPdiAcaoCompetencias().forEach(competencia -> {
			if (pdiAcaoTO.getPdiAcaoCompetencias().size() > 1 && competencia.getCompetenciaDetalhamento() != null) {
				throw new ValidationException("validation.detalhamentoPDI", "pdi", "competencia", pdiAcaoTO.getAcaoParaDesenvolvimento());
			}
			PDIAcaoTO savedPDIAcao = new PDIAcaoTO();
			savedPDIAcao.setId(pdiAcaoTO.getId());

			competencia.setPdiAcao(savedPDIAcao);
		/*	if(competencia.getCompetenciaDetalhamento() != null && competencia.getCompetenciaDetalhamento().getId() != null) {
				CompetenciaDetalhamentoTO compDet = new CompetenciaDetalhamentoTO();
				compDet.setId(competencia.getCompetenciaDetalhamento().getId());
				competencia.setCompetenciaDetalhamento(compDet);
			} else {
				competencia.setCompetenciaDetalhamento(null);
			}*/
			pdiAcaoCompDetService.saveOrUpdate(competencia);
		});
	}

	private void updateCompetencias(PDIAcaoTO pdiAcaoTO) throws ValidationException {
		/* Obtém as competencias do PDI salvos */
		List<PDIAcaoCompetenciaDetalhamentoTO> acaoCompetenciaDetalhamentoTOList = pdiAcaoCompDetService.listByPDIAcao(pdiAcaoTO.getId());
		if(pdiAcaoTO.getPdiAcaoCompetencias() != null) {
			verifyAndDeletePDICompetencia(pdiAcaoTO, acaoCompetenciaDetalhamentoTOList);
			salvarCompetencias(pdiAcaoTO);

		}

	}
	private void verifyAndDeletePDICompetencia(PDIAcaoTO pdiAcaoTO, List<PDIAcaoCompetenciaDetalhamentoTO> acaoCompetenciaDetalhamentoTOList) {
		/* Percorre e verifica se não existe o objeto no TO (com isso, quer dizer que a competencia foi deletada) */
		acaoCompetenciaDetalhamentoTOList.forEach(pdiCompetenciaEntity -> {
			boolean noMatch = pdiAcaoTO
				.getPdiAcaoCompetencias()
				.stream()
				.noneMatch(pdiCompetenciaTO -> pdiCompetenciaTO.getId().equals(pdiCompetenciaEntity.getId()));
			/*E, finalmente, remove-a do banco*/
			if(noMatch) {
				pdiAcaoCompDetService.delete(pdiCompetenciaEntity);
			}
		});
	}

	private final QBean<PDIAcao> PDI_COMPETENCIA_RELATORIO = Projections.fields(
		PDIAcao.class,
		qpdiAcao.id,
		qpdiAcao.acaoParaDesenvolvimento,
		qpdiAcao.dataInicio,
		qpdiAcao.dataFim,
		qpdiAcao.situacao,
		Projections.fields(
			Funcionario.class,
			qFuncionario.id,
			qFuncionario.nome,
			qFuncionario.matricula,
			qFuncionario.cargo,
			qFuncionario.departamento
		).as("funcionario")
	);

	//TODO: Ver se posso usar o framework de Search aqui!
	@Override
	public List<PDIAcaoTO> filterSearchRelatorioPDI(PDIAcaoSearchTO searchTO) {
		JPAQuery<PDIAcao> query = select(PDI_COMPETENCIA_RELATORIO).innerJoin(qpdiAcao.funcionario, qFuncionario);

		BooleanBuilder builder = new BooleanBuilder();
		/*Cargo (participante)*/
		if (searchTO.getCargo() != null && searchTO.getCargo().getId() != null) {
			builder.and(qpdiAcao.funcionario.cargo.id.eq(searchTO.getCargo().getId()));
		}
		/*TODO: Ver caso passe avaliacao/campanha! E alterar essa comparacao para o funcionario e nao participante*/

		if (searchTO.getCampanha() != null && searchTO.getCampanha().getId() != null) {
			query.innerJoin(qpdiAcao.participante, qParticipante)
				.innerJoin(qParticipante.avaliacao, qAvaliacao)
				.innerJoin(qAvaliacao.campanha, qCampanha)
				.innerJoin(qpdiAcao.participante, qParticipante);

			builder.and(qCampanha.id.eq(searchTO.getCampanha().getId()));

			if (searchTO.getAvaliacao() != null && searchTO.getAvaliacao().getId() != null) {
				builder.and(qpdiAcao.participante.avaliacao.id.eq(searchTO.getAvaliacao().getId()));
			}
		}

		/*avaliado (participante)*/
		if (searchTO.getAvaliado() != null && searchTO.getAvaliado().getId() != null) {
			builder.and(qpdiAcao.funcionario.id.eq(searchTO.getAvaliado().getId()));
		}
		/*departamento (participante)*/
		if (searchTO.getDepartamento() != null && searchTO.getDepartamento().getId() != null) {
			builder.and(qpdiAcao.funcionario.departamento.id.eq(searchTO.getDepartamento().getId()));
		}
		/*Situacao*/
		if (searchTO.getSituacao() != null && searchTO.getSituacao().getCode() != null) {
			builder.and(qpdiAcao.situacao.eq(searchTO.getSituacao()));
		}
		/*Data inicial*/
		if (searchTO.getDataInicio() != null) {
			builder.and(qpdiAcao.dataInicio.goe(searchTO.getDataInicio()));
		}
		/*Data final*/
		if (searchTO.getDataFim() != null) {
			builder.and(qpdiAcao.dataFim.loe(searchTO.getDataFim()));
		}
		query.where(builder);

		List<PDIAcaoTO> acoes = map(query.fetch());
		obterCompetencias(acoes);
		if (searchTO.getCompetencia() != null && searchTO.getCompetencia().getId() != null) {
			acoes = filtrarAcoesPorCompetencia(acoes, searchTO.getCompetencia());
		}

		return acoes;
	}

	private void obterCompetencias(List<PDIAcaoTO> acoesPDI) {
		acoesPDI.forEach(acao -> {
			acao.setPdiAcaoCompetencias(pdiAcaoCompDetService.listByPDIAcao(acao.getId()));
		});
	}

	private List<PDIAcaoTO> filtrarAcoesPorCompetencia(List<PDIAcaoTO> acoesPDI, CompetenciaSimpleTO competencia) {
		List<PDIAcaoTO> acoes = new ArrayList<>();
		acoesPDI.forEach(acao -> {
			if (acao.getPdiAcaoCompetencias().size() > 0) {
				if (acao.getPdiAcaoCompetencias().stream()
					.anyMatch(competenciaDetalhamento -> competenciaDetalhamento.getCompetencia().getId().equals(competencia.getId()))) {
					acoes.add(acao);
				}
			}
		});
		return acoes;
	}

	@Override
	public String exportarRelatorio(List<PDIAcaoTO> pdiAcaoTOList) throws IOException {
		//Cria um workbook (XLSX)
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*Arquivo de saída*/
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		/*Colunas do título*/
		String[] columns = {"Ação para Desenvolvimento", "Colaborador", "Setor", "Cargo", "Competência", "Período inicial", "Período final", "Situação"};

        /* CreationHelper ajuda a criar instâncias como DataFormat,
        Hyperlink, RichTextString etc, no formato (HSSF, XSSF) */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Cria uma planilha
		Sheet sheet = workbook.createSheet("Ações");

		// Estilo de fonte do título
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Estilo da célula do título
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Cria uma linha
		Row headerRow = sheet.createRow(0);

		// Cria as células dos títulos
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Cria uma célula com o tipo de data formatada
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		dateCellStyle.setAlignment(HorizontalAlignment.LEFT);

		// Cria as outras células com a informação da ação
		//TODO: Ver se recebe uma lista ou APENAS um componente
//		int rowNum = 1;
//		Row row = sheet.createRow(rowNum++);
//
//		CellStyle bodyStyle = workbook.createCellStyle();
//		bodyStyle.setAlignment(HorizontalAlignment.LEFT);
//		row.setRowStyle(bodyStyle);
//		/*Ação para Desenvolvimento*/
//		row.createCell(0)
//			.setCellValue(pdiAcaoTO.getAcaoParaDesenvolvimento());
//
//		/*Colaborador*/
//		row.createCell(1)
//			.setCellValue(pdiAcaoTO.getParticipante().getAvaliado().getNome());
//
//		/*Setor*/
//		row.createCell(2)
//			.setCellValue(pdiAcaoTO.getParticipante().getAvaliado().getCargo().getNome());
//
//		/*Cargo*/
//		row.createCell(3)
//			.setCellValue(pdiAcaoTO.getParticipante().getAvaliado().getDepartamento().getNome());
//
//		/*Competência*/
//		if (pdiAcaoTO.getPdiAcaoCompetencias().size() > 0) {
//			row.createCell(4)
//				.setCellValue(pdiAcaoTO
//					.getPdiAcaoCompetencias()
//					.stream()
//					.map(competenciaDetalhamentoTO -> competenciaDetalhamentoTO.getCompetencia().getDescricaoAbreviada())
//					.collect(Collectors.joining(", ")));
//		}
//
//		/*Período inicial*/
//		Cell dataInicio = row.createCell(5);
//		dataInicio.setCellValue(pdiAcaoTO.getDataPrevistaInicio());
//		dataInicio.setCellStyle(dateCellStyle);
//
//		/*Período final*/
//		Cell dataFim = row.createCell(6);
//		dataFim.setCellValue(pdiAcaoTO.getDataFim());
//		dataFim.setCellStyle(dateCellStyle);
//
//		/*Situação*/
//		row.createCell(7)
//			.setCellValue(pdiAcaoTO.getSituacao().getNome());
		int rowNum = 1;
		//Caso for uma lista, usar isso aqui!!
		for (PDIAcaoTO pdiAcaoTO : pdiAcaoTOList) {
			Row row = sheet.createRow(rowNum++);

			/*Ação para Desenvolvimento*/
			row.createCell(0)
				.setCellValue(pdiAcaoTO.getAcaoParaDesenvolvimento());

			/*Colaborador*/
			row.createCell(1)
				.setCellValue(pdiAcaoTO.getFuncionario().getNome());

			/*Setor*/
			row.createCell(2)
				.setCellValue(pdiAcaoTO.getFuncionario().getCargo().getNome());

			/*Cargo*/
			row.createCell(3)
				.setCellValue(pdiAcaoTO.getFuncionario().getDepartamento().getNome());

			/*Competência*/
			row.createCell(4)
				.setCellValue(pdiAcaoTO.getFuncionario().getNome());

			/*Período inicial*/
			Cell dataInicio = row.createCell(5);
			dataInicio.setCellValue(pdiAcaoTO.getDataPrevistaInicio());
			dataInicio.setCellStyle(dateCellStyle);

			/*Período final*/
			Cell dataFim = row.createCell(6);
			dataFim.setCellValue(pdiAcaoTO.getDataFim());
			dataFim.setCellStyle(dateCellStyle);

			/*Situação*/
			row.createCell(7)
				.setCellValue(pdiAcaoTO.getSituacao().getNome());
		}

		// Formata todas as colunas para redimensionar para o conteúdo
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		/*Escreve o workbook criado no output*/
		workbook.write(out);

		/*Transforma o output em bytes*/
		byte[] byteRpt = out.toByteArray();

		/*Transforma os bytes em base64*/
		String encodedBytes = Base64.getMimeEncoder().encodeToString(byteRpt);

		workbook.close();
		out.close();

		return encodedBytes;
	}

	@Override
	public String imprimirRelatorioJasper(List<PDIAcaoTO> pdiAcaoTOList) throws IOException, JRException {
		List<PDIAcaoReportTO> reports = new ArrayList<PDIAcaoReportTO>();
		FuncionarioTO funcionarioTO = funcionarioService.findById(pdiAcaoTOList.get(0).getFuncionario().getId());
		String pattern = "dd/MM/YYYY";
		String dataAtual = new SimpleDateFormat("EEEEE, dd 'de' MMMMM 'de' YYYY",new Locale("pt", "BR")).format(new Date());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		final Date[] dataInicialDesenv = {new Date(Long.MIN_VALUE)};
		final Date[] dataFinalDesenv = {new Date(Long.MIN_VALUE)};

		pdiAcaoTOList.forEach(pdiAcaoTO -> {
			PDIAcaoReportTO pdiAcaoReportTO = new PDIAcaoReportTO();
			pdiAcaoReportTO.setId(pdiAcaoTO.getId());
			pdiAcaoReportTO.setAcaoParaDesenvolvimento(pdiAcaoTO.getAcaoParaDesenvolvimento());
			pdiAcaoReportTO.setSituacao(pdiAcaoTO.getSituacao().getNome());
			if(pdiAcaoTO.getDataInicio() != null) {
				if(pdiAcaoTO.getDataInicio().after(dataInicialDesenv[0])) {
					dataInicialDesenv[0] = pdiAcaoTO.getDataInicio();
				}
				pdiAcaoReportTO.setDataInicio(simpleDateFormat.format(pdiAcaoTO.getDataInicio()));
			}
			if(pdiAcaoTO.getDataFim() != null) {
				if(pdiAcaoTO.getDataFim().after(dataFinalDesenv[0])) {
					dataFinalDesenv[0] = pdiAcaoTO.getDataFim();
				}
				pdiAcaoReportTO.setDataFim(simpleDateFormat.format(pdiAcaoTO.getDataFim()));
			}

			if(pdiAcaoTO.getPdiAcaoCompetencias() != null) {
				pdiAcaoReportTO.setCompetencia(
					pdiAcaoTO.getPdiAcaoCompetencias()
						.stream().map(competenciaDetalhamentoTO -> competenciaDetalhamentoTO.getCompetencia().getDescricaoAbreviada()).collect(Collectors.joining(", ")));
				if (pdiAcaoTO.getPdiAcaoCompetencias().size() == 1 && pdiAcaoTO.getPdiAcaoCompetencias().get(0).getCompetenciaDetalhamento() != null)
					pdiAcaoReportTO.setDetalhamento(pdiAcaoTO.getPdiAcaoCompetencias().get(0).getCompetenciaDetalhamento().getDescricao());
			}
			if(pdiAcaoTO.getObservacaoInicial() != null) {
				pdiAcaoReportTO.setObservacoes(pdiAcaoTO.getObservacaoInicial());
			}
			if(pdiAcaoTO.getObservacaoFinal() != null) {
				pdiAcaoReportTO.setObservacoes(pdiAcaoTO.getObservacaoFinal());
			}
			if(pdiAcaoTO.getObservacaoInicial()!= null && pdiAcaoTO.getObservacaoFinal() != null) {
				pdiAcaoReportTO.setObservacoes(pdiAcaoReportTO.getObservacoes() + "\n" + pdiAcaoTO.getObservacaoFinal());
			}
			reports.add(pdiAcaoReportTO);
		});
		JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(reports);
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("CollectionBeanParam", itemsJRBean);
		if(dataFinalDesenv[0].after(new Date(Long.MIN_VALUE)) &&
			dataInicialDesenv[0].after(new Date(Long.MIN_VALUE))) {
			parameters.put("PeriodoDesenvovilmentoParam", simpleDateFormat.format(dataInicialDesenv[0]) + " até " + simpleDateFormat.format(dataFinalDesenv[0]));
		}
		Resource resImg = resourceLoader.getResource("classpath:images/logo-dezi-sq.png");
		parameters.put("dataAtualParam", dataAtual);
		parameters.put("LogoDeziParam", resImg.getFile().getAbsolutePath());
		parameters.put("NomeFuncionarioParam", funcionarioTO.getNome());
		parameters.put("CargoFuncionarioParam", funcionarioTO.getCargo().getNome());
		parameters.put("SetorFuncionarioParam", funcionarioTO.getDepartamento().getNome());

		Resource res = resourceLoader.getResource("classpath:reports/AcaoPDIReport_A4.jrxml");

		InputStream input = res.getInputStream();

		JasperDesign jasperDesign = JRXmlLoader.load(input);

		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

		//JasperViewer.viewReport(jasperPrint);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, out);

		/*Transforma o output em bytes*/
		byte[] byteRpt = out.toByteArray();

		/*Transforma os bytes em base64*/
		String encodedBytes = Base64.getMimeEncoder().encodeToString(byteRpt);

		out.close();
		return encodedBytes;
	}

	@Override
	@Transactional
	public void excluirAcao(Long id) {
		pdiAcaoCompDetService.delete(pdiAcaoCompDetService.listByPDIAcao(id));
		pdiAcaoRepository.delete(id);
	}
}
