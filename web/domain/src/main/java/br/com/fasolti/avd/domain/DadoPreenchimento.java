package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.fasolti.commons.domain.DomainObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dado_preenchimento")
public class DadoPreenchimento implements DomainObject {
	private static final long serialVersionUID = -3360986279617847223L;

	@Id
	private Long id;

	@Column(name = "id_campanha")
	private Long campanhaId;

	@Column(name = "campanha")
	private String campanha;

	@Column(name = "id_avaliacao")
	private Long avaliacaoId;

	@Column(name = "avaliacao")
	private String avaliacao;

	@Column(name = "id_responsavel")
	private Long responsavelId;

	@Column(name = "responsavel")
	private String responsavel;

	@Column(name = "id_avaliado")
	private Long avaliadoId;

	@Column(name = "avaliado")
	private String avaliado;

	@Column(name = "id_departamento")
	private Long departamentoId;

	@Column(name = "departamento")
	private String departamento;

	@Column(name = "id_tipo_competencia")
	private Long tipoCompetenciaId;

	@Column(name = "tipo_competencia")
	private String tipoCompetencia;

	@Column(name = "id_competencia")
	private Long competenciaId;

	@Column(name = "competencia")
	private String competencia;

	@Column(name = "total_auto")
	private Integer totalAuto;

	@Column(name = "preench_auto")
	private Integer preenchAuto;

	@Column(name = "total_gestor")
	private Integer totalGestor;

	@Column(name = "preench_gestor")
	private Integer preenchGestor;

	@Column(name = "total_consenso")
	private Integer totalConsenso;

	@Column(name = "preench_consenso")
	private Integer preenchConsenso;

	@Column(name = "total_subord")
	private Integer totalSubordinados;

	@Column(name = "preench_subord")
	private Integer preenchSubordinados;

	@Column(name = "total_pares")
	private Integer totalPares;

	@Column(name = "preench_pares")
	private Integer preenchPares;

	@Column(name = "email_responsavel")
	private String emailResponsavel;

	@Column(name = "email_avaliado")
	private String emailAvaliado;

	@Column(name = "cargo_responsavel")
	private String cargoResponsavel;

	@Column(name = "id_departamento_responsavel")
	private Long departamentoResponsavelId;

	@Column(name = "departamento_responsavel")
	private String departamentoResponsavel;
}
