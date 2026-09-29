-- Atualizacao dos dados do tipo da competencia no historico da avaliacao

CREATE TABLE public.de_para_tipo
(
  id_tipo bigint,
  id_tipo_competencia bigint,
  nome_curto character varying
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.de_para_tipo
  OWNER TO avduser;


INSERT INTO de_para_tipo(id_tipo, id_tipo_competencia, nome_curto)
(select tc.id As id_tipo, tc.id As id_tipo_competencia, tc.nome_curto
 from tipo_competencia tc);


UPDATE de_para_tipo d
SET id_tipo = t.id_t
FROM (select t1.id as id_t, t1.nome_curto as nome_curto_t,
	tc.id as id_tc, tc.nome_curto as nome_curto_tc
    from tipo t1, tipo_competencia tc
	where t1.nome_curto = tc.nome_curto) t
WHERE d.nome_curto = t.nome_curto_t and
      d.id_tipo_competencia = t.id_tc;


ALTER TABLE public.avaliacao_tipo_competencia DROP CONSTRAINT pk_tipo_competencia_tipo;


UPDATE avaliacao_tipo_competencia atc
SET id_tipo = d.id_tipo_competencia
FROM (select d1.*
    from de_para_tipo d1) d
WHERE atc.id_tipo = d.id_tipo;


ALTER TABLE public.avaliacao_tipo_competencia
  ADD CONSTRAINT fk_tipo_competencia FOREIGN KEY (id_tipo)
      REFERENCES public.tipo_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.avaliacao_tipo_competencia DROP CONSTRAINT pk_avaliacao_tipo_competencia_tipo;

ALTER TABLE public.avaliacao_tipo_competencia
  ADD CONSTRAINT fk_avaliacao FOREIGN KEY (id_avaliacao)
      REFERENCES public.avaliacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.avaliacao_competencia DROP CONSTRAINT fk_tipo_competencia;


UPDATE avaliacao_competencia ac
SET id_tipo_competencia = d.id_tipo_competencia
FROM (select d1.*
    from de_para_tipo d1) d
WHERE ac.id_tipo_competencia = d.id_tipo;


ALTER TABLE public.avaliacao_competencia
  ADD CONSTRAINT fk_tipo_competencia FOREIGN KEY (id_tipo_competencia)
      REFERENCES public.tipo_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


DROP TABLE public.de_para_tipo;
