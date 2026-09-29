DROP VIEW view_detalhamentos_participante;

CREATE OR REPLACE VIEW view_detalhamentos_participante AS
 SELECT DISTINCT row_number() OVER () AS id, comp.id AS id_competencia, comp.descricao_abreviada AS descricao_competencia, t.id AS id_tipo_competencia, t.nome_curto AS tipo_competencia, cd.id AS id_detalhamento, cd.descricao AS descricao_detalhamento, av.id AS id_avaliacao, av.descricao_abreviada AS descricao_avaliacao, atc.id_perfil_agente, c.id AS id_cargo, c.nome AS nome_cargo
   FROM avaliacao av
   JOIN avaliacao_tipo_competencia atc ON av.id = atc.id_avaliacao
   JOIN competencia comp ON atc.id_tipo = comp.id_tipo_competencia
   JOIN cargo_competencia cc ON comp.id = cc.id_competencia
   JOIN cargo c ON cc.id_cargo = c.id
   JOIN competencia_detalhamento cd ON comp.id = cd.id_competencia
   JOIN cargo_compet_det_gabarito ccdg ON cd.id = ccdg.id_competencia_detalhamento
   JOIN tipo t ON comp.id_tipo_competencia = t.id
   JOIN graduacao g ON ccdg.id_graduacao = g.id
  WHERE cc.id = ccdg.id_cargo_competencia AND ccdg.aplicavel_cargo = true AND g.id_situacao = 1 AND c.id_situacao = 1 AND cd.id_situacao = 1 AND comp.id_situacao = 1 AND cc.id_situacao = 1
  GROUP BY comp.id, comp.descricao_abreviada, t.id, t.nome_curto, cd.id, cd.descricao, av.id, av.descricao_abreviada, atc.id_perfil_agente, c.id, c.nome
  ORDER BY comp.descricao_abreviada, t.nome_curto, cd.descricao;

ALTER TABLE view_detalhamentos_participante
  OWNER TO avduser;
