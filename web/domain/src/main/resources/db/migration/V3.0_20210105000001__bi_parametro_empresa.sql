CREATE SEQUENCE public.seq_bi_parametro_empresa
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_bi_parametro_empresa OWNER TO deziuser;

CREATE TABLE public.bi_parametro_empresa
(
  id bigint NOT NULL DEFAULT nextval('seq_bi_parametro_empresa'::regclass),
  id_empresa bigint,
  valor smallint NOT NULL DEFAULT 2,
  tipo_parametro smallint NOT NULL, -- Tipo 0 = número de casas decimais
  CONSTRAINT bi_parametro_empresa_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.bi_parametro_empresa OWNER TO deziuser;
COMMENT ON TABLE public.bi_parametro_empresa IS 'Parametrização de informações necessárias para geração dos relatórios do BI';

CREATE INDEX fki_bi_parametro_empresa ON public.bi_parametro_empresa USING btree(id_empresa);

INSERT INTO bi_parametro_empresa (id, id_empresa, valor, tipo_parametro) VALUES(nextval('seq_bi_parametro_empresa'), 100, 1, 0); --RTM
INSERT INTO bi_parametro_empresa (id, id_empresa, valor, tipo_parametro) VALUES(nextval('seq_bi_parametro_empresa'), null, 2, 0); --DEFAULT
