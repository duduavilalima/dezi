/*  AVD-405 */

CREATE OR REPLACE VIEW view_competencia_avaliacao AS
 SELECT row_number() OVER () AS id, c.id AS id_competencia, c.descricao_abreviada AS descricao_competencia, t.nome_curto AS tipo_competencia, av.id AS id_avaliacao, av.descricao_abreviada AS descricao_avaliacao, ap.id_avaliador, ap.id_revisor, ap.id_perfil_agente AS id_perfil, count(DISTINCT ap.id) AS qtd_avaliados, count(ap.id) AS qtd_respostas, ( SELECT count(*) AS count
           FROM avaliacao_competencia ac1
      JOIN avaliacao_participante ap1 ON ac1.id_avaliacao_participante = ap1.id
   JOIN competencia_detalhamento cd1 ON ac1.id_competencia_detalhamento = cd1.id
   JOIN competencia c1 ON cd1.id_competencia = c1.id
   JOIN avaliacao av1 ON ap1.id_avaliacao = av1.id
  WHERE (ap1.id_avaliador = ap.id_avaliador OR ap1.id_revisor = ap.id_avaliador) AND av1.id_situacao = 2 AND ap1.id_perfil_agente = ap.id_perfil_agente AND ap1.limite_av2 >= 'now'::text::date AND c1.id = c.id) AS qtd_perguntas
   FROM avaliacao_competencia ac
   LEFT JOIN avaliacao_preenchimento preen ON ac.id = preen.id_avaliacao_competencia
   JOIN competencia_detalhamento cd ON ac.id_competencia_detalhamento = cd.id
   JOIN competencia c ON cd.id_competencia = c.id
   JOIN avaliacao_participante ap ON ac.id_avaliacao_participante = ap.id
   JOIN tipo t ON c.id_tipo_competencia = t.id
   JOIN avaliacao av ON ap.id_avaliacao = av.id
  WHERE av.id_situacao = 2 AND ap.limite_av2 >= 'now'::text::date
  GROUP BY c.id, c.descricao_abreviada, t.nome_curto, av.id, av.descricao_abreviada, ap.id_avaliador, ap.id_revisor, ap.id_perfil_agente
  ORDER BY t.nome_curto, c.descricao_abreviada;

ALTER TABLE view_competencia_avaliacao
  OWNER TO avduser;
