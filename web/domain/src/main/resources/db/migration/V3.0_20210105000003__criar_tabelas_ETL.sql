CREATE TABLE bi_media_agente_detalhamento
(
  empresa_id BIGINT
, campanha_id BIGINT
, avaliacao_id BIGINT
, funcionario_id BIGINT
, departamento_id BIGINT
, tipo_competencia VARCHAR(100)
, competencia VARCHAR(150)
, detalhamento VARCHAR(2048)
, agente VARCHAR(25)
, peso_agente SMALLINT
, media_agente NUMERIC(13, 2)
, peso_cargo_tipo_competencia SMALLINT
, peso_cargo_competencia SMALLINT
, pontuacao SMALLINT
, pontuacao_auto SMALLINT
, pontuacao_consenso SMALLINT
)
;
ALTER TABLE public.bi_media_agente_detalhamento OWNER TO deziuser;

CREATE TABLE bi_media_detalhamento
(
  empresa_id BIGINT
, campanha_id BIGINT
, avaliacao_id BIGINT
, funcionario_id BIGINT
, departamento_id BIGINT
, tipo_competencia VARCHAR(100)
, competencia VARCHAR(150)
, detalhamento VARCHAR(2048)
, peso_cargo_tipo_competencia SMALLINT
, peso_cargo_competencia SMALLINT
, media_detalhamento NUMERIC(13, 2)
)
;

ALTER TABLE public.bi_media_detalhamento OWNER TO deziuser;

CREATE TABLE bi_media_competencia
(
  avaliacao_id BIGINT
, campanha_id BIGINT
, competencia VARCHAR(150)
, departamento_id BIGINT
, empresa_id BIGINT
, funcionario_id BIGINT
, tipo_competencia VARCHAR(100)
, media_competencia NUMERIC(13, 2)
, peso_cargo_tipo_competencia SMALLINT
, peso_cargo_competencia SMALLINT
)
;

ALTER TABLE public.bi_media_competencia OWNER TO deziuser;

CREATE TABLE bi_media_tipo_competencia
(
  empresa_id BIGINT
, campanha_id BIGINT
, avaliacao_id BIGINT
, funcionario_id BIGINT
, departamento_id BIGINT
, tipo_competencia VARCHAR(100)
, peso_cargo_tipo_competencia SMALLINT
, media_tipo_competencia NUMERIC(13, 2)
)
;

ALTER TABLE public.bi_media_tipo_competencia OWNER TO deziuser;

CREATE TABLE bi_media_departamento
(
  empresa_id BIGINT
, campanha_id BIGINT
, avaliacao_id BIGINT
, funcionario_id BIGINT
, departamento_id BIGINT
, media_departamento NUMERIC(13, 2)
)
;
ALTER TABLE public.bi_media_departamento OWNER TO deziuser;

CREATE TABLE bi_media_avaliacao_agente
(
  empresa_id BIGINT
, campanha_id BIGINT
, avaliacao_id BIGINT
, funcionario_id BIGINT
, departamento_id BIGINT
, agente VARCHAR(20)
, media_avaliacao NUMERIC(13, 2)
, peso_agente SMALLINT
)
;

ALTER TABLE public.bi_media_avaliacao_agente OWNER TO deziuser;

CREATE TABLE bi_media_geral_detalhamento
(
  empresa_id BIGINT
, campanha_id BIGINT
, avaliacao_id BIGINT
, tipo_competencia VARCHAR(100)
, competencia VARCHAR(150)
, detalhamento VARCHAR(1024)
, media_geral_detalhamento NUMERIC(13, 2)
)
;

ALTER TABLE public.bi_media_geral_detalhamento OWNER TO deziuser;