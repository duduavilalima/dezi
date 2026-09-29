CREATE TABLE public.avaliacao_competencia
(
  id bigint NOT NULL DEFAULT nextval('seq_avaliacao_competencias'::regclass), -- Identificador primário da tabela
  id_avaliacao bigint NOT NULL, -- Identificador da avaliação
  id_competencia bigint NOT NULL, -- Idenficador da competência
  id_tipo_competencia bigint NOT NULL, -- Idenficador do tipo de competência
  descricao_tipo_competencia character varying(100) NOT NULL, -- Descrição do tipo de competência
  descricao_abreviada character varying(150) NOT NULL, -- Descrição da competência
  direcionamento_gestor text,
  direcionamento_nao_gestor text,
  atualizador_original character varying(255) NOT NULL,
  data_atualizacao_original date NOT NULL,
  criador character varying(255),
  data_criacao date,
  atualizador character varying(255),
  data_atualizacao date,
  CONSTRAINT pk_avaliacao_competencia PRIMARY KEY (id),
  CONSTRAINT fk_avaliacao FOREIGN KEY (id_avaliacao)
      REFERENCES public.avaliacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_competencia FOREIGN KEY (id_competencia)
      REFERENCES public.competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_tipo_competencia FOREIGN KEY (id_tipo_competencia)
      REFERENCES public.tipo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE SEQUENCE public.seq_modelo_graduacao_comp;
ALTER SEQUENCE public.seq_modelo_graduacao_comp
  OWNER TO avduser;

CREATE TABLE public.modelo_graduacao_comp
(
  id bigint NOT NULL DEFAULT nextval('seq_modelo_graduacao_comp'::regclass), -- Identificador primário da tabela
  id_modelo_graduacao bigint NOT NULL, -- Identificador do modelo de graduacao
  descricao_modelo character varying(80), -- Descrição do modelo de graduacao
  nao_graduar boolean,
  id_dimensao bigint NOT NULL, -- Identificador da dimensão
  id_empresa bigint NOT NULL, -- Identificador da empresa
  atualizador_original character varying(255) NOT NULL,
  data_atualizacao_original date NOT NULL,
  criador character varying(255) NOT NULL,
  data_criacao timestamp without time zone NOT NULL,
  atualizador character varying(255),
  data_atualizacao timestamp without time zone,
  CONSTRAINT pk_modelo_graduacao_comp PRIMARY KEY (id),
  CONSTRAINT fk_dimensao FOREIGN KEY (id_dimensao)
      REFERENCES public.dimensao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_empresa FOREIGN KEY (id_empresa)
      REFERENCES public.empresa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_modelo_graduacao FOREIGN KEY (id_modelo_graduacao)
      REFERENCES public.modelo_graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE SEQUENCE public.seq_graduacao_competencia;
ALTER SEQUENCE public.seq_graduacao_competencia
  OWNER TO avduser;

CREATE TABLE public.graduacao_competencia
(
  id bigint NOT NULL DEFAULT nextval('seq_graduacao_competencia'::regclass), -- Identificador primário da tabela
  id_modelo_graduacao_comp bigint NOT NULL, -- Identificador do modelo de graduação comp
  id_graduacao bigint, -- Identificador da graduação
  ordem smallint,
  label character varying(40) NOT NULL,
  hint text,
  ponto smallint,
  atualizador_original character varying(255) NOT NULL,
  data_atualizacao_original date NOT NULL,
  criador character varying(255) NOT NULL,
  data_criacao timestamp without time zone NOT NULL,
  atualizador character varying(255),
  data_atualizacao timestamp without time zone,
  CONSTRAINT pk_graduacao_competencia PRIMARY KEY (id),
  CONSTRAINT fk_graduacao FOREIGN KEY (id_graduacao)
      REFERENCES public.graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_modelo_graduacao_comp FOREIGN KEY (id_modelo_graduacao_comp)
      REFERENCES public.modelo_graduacao_comp (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE SEQUENCE public.seq_modelo_graduacao_aval_comp;
ALTER SEQUENCE public.seq_modelo_graduacao_aval_comp
  OWNER TO avduser;


CREATE TABLE public.modelo_graduacao_aval_comp
(
  id bigint NOT NULL DEFAULT nextval('seq_modelo_graduacao_aval_comp'::regclass),
  id_avaliacao_competencia bigint NOT NULL,
  id_modelo_graduacao_comp bigint NOT NULL,
  CONSTRAINT pk_modelo_graduacao_aval_comp PRIMARY KEY (id),
  CONSTRAINT fk_avaliacao_competencia FOREIGN KEY (id_avaliacao_competencia)
      REFERENCES public.avaliacao_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_modelo_graduacao_comp FOREIGN KEY (id_modelo_graduacao_comp)
      REFERENCES public.modelo_graduacao_comp (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE SEQUENCE public.seq_avaliacao_comp_det;
ALTER SEQUENCE public.seq_avaliacao_comp_det
  OWNER TO avduser;

CREATE TABLE public.avaliacao_comp_det
(
  id bigint NOT NULL DEFAULT nextval('seq_avaliacao_comp_det'::regclass), -- Identificador primário da tabela
  id_avaliacao_competencia bigint NOT NULL, -- Identificador da avaliação competência
  id_competencia_detalhamento bigint NOT NULL, -- Identificador do detalhamento competência
  ordem smallint NOT NULL,
  descricao text,
  atualizador_original character varying(255) NOT NULL,
  data_atualizacao_original date NOT NULL,
  criador character varying(255) NOT NULL,
  data_criacao timestamp without time zone NOT NULL,
  atualizador character varying(255),
  data_atualizacao timestamp without time zone,
  CONSTRAINT pk_seq_avaliacao_comp_det PRIMARY KEY (id),
  CONSTRAINT fk_avaliacao_competencia FOREIGN KEY (id_avaliacao_competencia)
      REFERENCES public.avaliacao_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_competencia_detalhamento FOREIGN KEY (id_competencia_detalhamento)
      REFERENCES public.competencia_detalhamento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE SEQUENCE public.seq_avaliacao_det_gab;
ALTER SEQUENCE public.seq_avaliacao_det_gab
  OWNER TO avduser;

CREATE TABLE public.avaliacao_det_gab
(
  id bigint NOT NULL DEFAULT nextval('seq_avaliacao_det_gab'::regclass), -- Identificador primário da tabela
  id_avaliacao_comp_det bigint NOT NULL,
  id_gabarito bigint NOT NULL,
  id_cargo_competencia bigint NOT NULL,
  id_competencia_detalhamento bigint NOT NULL,
  id_graduacao bigint NOT NULL,
  peso_gabarito smallint NOT NULL,
  peso_cargo_competencia smallint NOT NULL,
  aplicavel_cargo boolean NOT NULL,
  atualizador_original character varying(255) NOT NULL,
  data_atualizacao_original date NOT NULL,
  criador character varying(255) NOT NULL,
  data_criacao timestamp without time zone NOT NULL,
  atualizador character varying(255),
  data_atualizacao timestamp without time zone,
  CONSTRAINT pk_aval_det_gab PRIMARY KEY (id),
  CONSTRAINT fk_avaliacao_comp_det FOREIGN KEY (id_avaliacao_comp_det)
      REFERENCES public.avaliacao_comp_det (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_cargo_competencia FOREIGN KEY (id_cargo_competencia)
      REFERENCES public.cargo_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_competencia_detalhamento FOREIGN KEY (id_competencia_detalhamento)
      REFERENCES public.competencia_detalhamento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_gabarito FOREIGN KEY (id_gabarito)
      REFERENCES public.cargo_compet_det_gabarito (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_graduacao FOREIGN KEY (id_graduacao)
      REFERENCES public.graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE SEQUENCE public.seq_avaliacao_comp_det_hint;
ALTER SEQUENCE public.seq_avaliacao_comp_det_hint
  OWNER TO avduser;

CREATE TABLE public.avaliacao_comp_det_hint
(
  id bigint NOT NULL DEFAULT nextval('seq_avaliacao_comp_det_hint'::regclass), -- Identificador primário da tabela
  id_avaliacao_comp_det bigint NOT NULL,
  id_graduacao bigint NOT NULL,
  hint text NOT NULL,
  comentario_obrigatorio boolean,
  atualizador_original character varying(255) NOT NULL,
  data_atualizacao_original date NOT NULL,
  criador character varying(255) NOT NULL,
  data_criacao timestamp without time zone NOT NULL,
  atualizador character varying(255),
  data_atualizacao timestamp without time zone,
  id_hint bigint,
  CONSTRAINT pk_aval_comp_det_hint PRIMARY KEY (id),
  CONSTRAINT fk_avaliacao_comp_det FOREIGN KEY (id_avaliacao_comp_det)
      REFERENCES public.avaliacao_comp_det (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_compet_detalhe_calibrahint FOREIGN KEY (id_hint)
      REFERENCES public.compet_detalhe_calibrahint (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_graduacao FOREIGN KEY (id_graduacao)
      REFERENCES public.graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE public.avaliacao_preenchimento
(
  id bigint NOT NULL DEFAULT nextval('seq_avaliacao_preenchimento'::regclass), -- Identificador primário da tabela
  id_avaliacao_comp_det bigint NOT NULL, -- Identificação da Avaliação x Competência que está sendo preenchida
  id_participante bigint NOT NULL,
  id_graduacao_av1 bigint, -- Graduação da autoavaliação
  id_graduacao_av2 bigint, -- Graduação da avaliação
  id_graduacao_av3 bigint, -- Graduação da avaliação de consenso
  concordancia_revisor boolean, -- Campo booleano Indica se o revisor concordou ou não com a graduação que o avaliador atribuiu ao avaliado (na competência estudada)
  justificativa_revisor text, -- Campo texto Observação do revisor (no momento da revisão)
  observacao_av1 text,
  observacao_av2 text,
  observacao_av3 text, -- Observação (no momento da avaliação de consenso)
  CONSTRAINT avaliacao_preench_pkey PRIMARY KEY (id),
  CONSTRAINT fk_avaliacao_comp_det FOREIGN KEY (id_avaliacao_comp_det)
      REFERENCES public.avaliacao_comp_det (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_avaliacao_participante FOREIGN KEY (id_participante)
      REFERENCES public.avaliacao_participante (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_graduacao_av1 FOREIGN KEY (id_graduacao_av1)
      REFERENCES public.graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_graduacao_av2 FOREIGN KEY (id_graduacao_av2)
      REFERENCES public.graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_graduacao_av3 FOREIGN KEY (id_graduacao_av3)
      REFERENCES public.graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE UNIQUE INDEX uq_avaliacao_preench_avaliacao_comp_det
 ON public.avaliacao_preenchimento USING BTREE
 ( id_avaliacao_comp_det, id_participante );


ALTER TABLE public.avaliacao_participante ADD COLUMN id_cargo_avaliado bigint;

ALTER TABLE public.avaliacao_participante ADD COLUMN nome_cargo_avaliado character varying(255);

ALTER TABLE public.avaliacao_participante ADD COLUMN id_depto_avaliado bigint;

ALTER TABLE public.avaliacao_participante ADD COLUMN nome_depto_avaliado character varying(255);
