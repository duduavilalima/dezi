-- criacao da tabela tipo de competencia e da sua sequence

CREATE SEQUENCE public.seq_tipo_competencia
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 199
  CACHE 1;
ALTER TABLE public.seq_tipo_competencia
  OWNER TO avduser;


CREATE TABLE public.tipo_competencia
(
  id bigint NOT NULL DEFAULT nextval('seq_tipo_competencia'::regclass),
  id_pai bigint,
  id_empresa bigint NOT NULL,
  nome character varying(150),
  descricao character varying(255),
  nome_curto character varying(255),
  situacao bigint,
  criador character varying(255) NOT NULL, -- Criador do registro
  data_criacao timestamp without time zone NOT NULL, -- Data de criação do registro
  atualizador character varying(255), -- Último atualizador do registro
  data_atualizacao timestamp without time zone, -- Última data de atualização do registro
  CONSTRAINT "pk_tipo_competência" PRIMARY KEY (id),
  CONSTRAINT fk_empresa FOREIGN KEY (id_empresa)
      REFERENCES public.empresa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_tipo_competencia FOREIGN KEY (id_pai)
      REFERENCES public.tipo_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT check_situacao_tipo_competencia CHECK (situacao = ANY (ARRAY[1::bigint, 2::bigint]))
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tipo_competencia
  OWNER TO avduser;
COMMENT ON COLUMN public.tipo_competencia.criador IS 'Criador do registro';
COMMENT ON COLUMN public.tipo_competencia.data_criacao IS 'Data de criação do registro';
COMMENT ON COLUMN public.tipo_competencia.atualizador IS 'Último atualizador do registro';
COMMENT ON COLUMN public.tipo_competencia.data_atualizacao IS 'Última data de atualização do registro';



-- carga de tipo de competencia

INSERT INTO public.tipo_competencia(
            id, id_pai, id_empresa, nome, descricao, nome_curto, situacao,
            criador, data_criacao, atualizador, data_atualizacao)
    VALUES (NEXTVAL('seq_tipo_competencia'), null, 1, 'Competências Organizacionais', 'Competências Organizacionais', 'Organizacional', 1,
            'admin', current_date, null, null);


INSERT INTO public.tipo_competencia(
            id, id_pai, id_empresa, nome, descricao, nome_curto, situacao,
            criador, data_criacao, atualizador, data_atualizacao)
    VALUES (NEXTVAL('seq_tipo_competencia'), null, 1, 'Competências Gerenciais', 'Competências Gerenciais', 'Gerencial', 1,
            'admin', current_date, null, null);


INSERT INTO public.tipo_competencia(
            id, id_pai, id_empresa, nome, descricao, nome_curto, situacao,
            criador, data_criacao, atualizador, data_atualizacao)
    VALUES (NEXTVAL('seq_tipo_competencia'), null, 1, 'Competências Técnicas', 'Competências Técnicas', 'Técnica', 1,
            'admin', current_date, null, null);


INSERT INTO public.tipo_competencia(
            id, id_pai, id_empresa, nome, descricao, nome_curto, situacao,
            criador, data_criacao, atualizador, data_atualizacao)
    VALUES (NEXTVAL('seq_tipo_competencia'), null, 1, 'Competências Comportamentais', 'Competências Comportamentais', 'Comportamental', 1,
            'admin', current_date, null, null);



INSERT INTO public.tipo_competencia(
            id, id_pai, id_empresa, nome, descricao, nome_curto, situacao,
            criador, data_criacao, atualizador, data_atualizacao)
    VALUES (NEXTVAL('seq_tipo_competencia'), null, 1, 'Responsabilidades', 'Responsabilidades', 'Responsabilidades', 1,
            'admin', current_date, null, null);

-- criacao de tabela temporaria para migrar os tipos de competencia

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



ALTER TABLE public.competencia DROP CONSTRAINT fk_tipo_competencia;

UPDATE competencia c
SET id_tipo_competencia = d.id_tipo_competencia
FROM (select d1.*
    from de_para_tipo d1) d
WHERE c.id_tipo_competencia = d.id_tipo;

ALTER TABLE public.competencia
  ADD CONSTRAINT fk_tipo_competencia FOREIGN KEY (id_tipo_competencia)
      REFERENCES public.tipo_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


DROP TABLE public.de_para_tipo;
