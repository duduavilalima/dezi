--DROP VIEW conjunto_resultados;

CREATE OR REPLACE VIEW conjunto_resultados AS
SELECT
	empresa.id AS empresa_id,
	empresa.identificador AS empresa_identificador,
	campanha.id  AS campanha_id,
	campanha.descricao AS campanha_descricao,
	avaliacao.id AS avaliacao_id,
	avaliacao.descricao AS avaliacao_descricao,
	avaliacao.descricao_abreviada AS avaliacao_descricao_abreviada,
	funcionario.id as funcionario_id, 
	funcionario.nome as nome_funcionario, 
	departamento.id as departamento_id,
	departamento.nome as nome_departamento,
	avaliacao_competencia.descricao_tipo_competencia AS tipo_competencia,
	avaliacao_competencia.descricao_abreviada AS competencia,
	avaliacao_comp_det.descricao AS detalhamento,
	avaliacao_participante.id_perfil_agente AS perfil_agente,
	CASE avaliacao_participante.id_perfil_agente
            WHEN 101 THEN 'gestor'::text
            WHEN 102 THEN 'subordinados'::text
            WHEN 103 THEN 'pares'::text
            ELSE NULL::text
        END AS agente,
	avaliacao_tipo_competencia.peso AS peso_agente,
        NULLIF(graduacao_av1.ponto, 0) AS pontuacao_auto,
        NULLIF(graduacao_av2.ponto, 0) AS pontuacao,
        NULLIF(graduacao_av3.ponto, 0) AS pontuacao_consenso,
        NULLIF(COALESCE(graduacao_av3.ponto, graduacao_av2.ponto), 0)::numeric(11,2) AS pontuacao_final,
	/*CASE WHEN apcc.peso_cargo_tipo_competencia IS NULL THEN 1 ELSE apcc.peso_cargo_tipo_competencia END peso_cargo_tipo_competencia, 
	CASE WHEN apcc.peso_cargo_competencia IS NULL THEN 1 ELSE apcc.peso_cargo_competencia END peso_cargo_competencia */
	1 AS peso_cargo_tipo_competencia, 
	1 AS peso_cargo_competencia 
FROM funcionario JOIN empresa ON funcionario.id_empresa = empresa.id
JOIN departamento ON funcionario.id_departamento = departamento.id
JOIN avaliacao_participante ON funcionario.id = avaliacao_participante.id_avaliado
JOIN avaliacao ON avaliacao.id = avaliacao_participante.id_avaliacao
JOIN campanha ON avaliacao.id_campanha = campanha.id
JOIN avaliacao_preenchimento ON avaliacao_participante.id = avaliacao_preenchimento.id_participante
JOIN avaliacao_comp_det ON avaliacao_comp_det.id = avaliacao_preenchimento.id_avaliacao_comp_det
JOIN avaliacao_competencia ON avaliacao_competencia.id = avaliacao_comp_det.id_avaliacao_competencia
JOIN avaliacao_tipo_competencia ON avaliacao_participante.id_avaliacao = avaliacao_tipo_competencia.id_avaliacao AND avaliacao_participante.id_perfil_agente = avaliacao_tipo_competencia.id_perfil_agente
LEFT JOIN graduacao_competencia graduacao_av1 ON avaliacao_preenchimento.id_graduacao_comp_av1 = graduacao_av1.id
LEFT JOIN graduacao_competencia graduacao_av2 ON avaliacao_preenchimento.id_graduacao_comp_av2 = graduacao_av2.id
LEFT JOIN graduacao_competencia graduacao_av3 ON avaliacao_preenchimento.id_graduacao_comp_av3 = graduacao_av3.id
LEFT JOIN aval_peso_cargo_comp apcc ON avaliacao_preenchimento.id = apcc.id_preenchimento

WHERE graduacao_av3.ponto IS NOT NULL OR graduacao_av2.ponto IS NOT NULL AND avaliacao.id_situacao NOT IN (98,99)

ORDER BY 
	empresa.identificador,
	campanha.descricao, 
	funcionario.nome, 
	avaliacao.descricao, 
	avaliacao_competencia.descricao_tipo_competencia,
	avaliacao_competencia.descricao_abreviada,
	avaliacao_comp_det.descricao,
	avaliacao_participante.id_perfil_agente;

ALTER TABLE public.conjunto_resultados OWNER TO deziuser;

COMMENT ON VIEW public.conjunto_resultados
  IS 'View para listar todos os valores referentes aos resultados de todas as avaliações de todas empresas. Cria um "tabelão" desnormalizado que será refinado em tabelas menores, normalizadas, com valores agrupados de acordo com a finalidade do relatório que serão utilizadas.';