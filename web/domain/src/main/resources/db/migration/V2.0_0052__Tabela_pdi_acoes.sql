CREATE SEQUENCE public.seq_pdi_acao
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_pdi_acao OWNER TO deziuser;


CREATE TABLE public.pdi_acao (
	id bigserial NOT NULL, -- Identificador primário da tabela
	id_avaliacao_participante int8 NULL, -- Id da avaliacao_participante
	id_funcionario int8 NULL, -- Id do funcionario avaliado
	id_situacao int8 NOT NULL, -- Identificador da situação daquele registro (em aprovacao, aprovado, em andamento, finalizado)
	acao_desenv text NULL, -- Descrição da ação
	data_prevista_inicio date NULL, -- Data prevista de início da ação
	data_prevista_fim date NULL, -- Data prevista de finalização da ação
	data_inicio date NULL, -- Data de início da ação
	data_fim date NULL, -- Data de finalização da ação
	obs_inicio text NULL, -- Observação do início da ação
	obs_fim text NULL, -- Observação de finalização da ação
	revisado bool NULL, -- Booleano de ação revisada
	criador varchar(255) NOT NULL,
	data_criacao timestamp NOT NULL,
	atualizador varchar(255) NULL,
	data_atualizacao timestamp NULL,
	CONSTRAINT pdi_acao_pkey PRIMARY KEY (id),
	CONSTRAINT fk_avaliacao_participante_pdi_acao FOREIGN KEY (id_avaliacao_participante) REFERENCES avaliacao_participante(id),
	CONSTRAINT fk_funcionario_pdi_acao FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)

);

-- Column comments

COMMENT ON COLUMN public.pdi_acao.id IS 'Identificador primário da tabela';
COMMENT ON COLUMN public.pdi_acao.id_avaliacao_participante IS 'Id da avaliacao_participante';
COMMENT ON COLUMN public.pdi_acao.id_situacao IS 'Identificador da situação daquele registro (em aprovacao, aprovado, em andamento, finalizado)';
COMMENT ON COLUMN public.pdi_acao.acao_desenv IS 'Descrição da ação';
COMMENT ON COLUMN public.pdi_acao.data_prevista_inicio IS 'Data prevista de início da ação';
COMMENT ON COLUMN public.pdi_acao.data_prevista_fim IS 'Data prevista de finalização da ação';
COMMENT ON COLUMN public.pdi_acao.data_inicio IS 'Data de início da ação';
COMMENT ON COLUMN public.pdi_acao.data_fim IS 'Data de finalização da ação';
COMMENT ON COLUMN public.pdi_acao.obs_inicio IS 'Observação do início da ação';
COMMENT ON COLUMN public.pdi_acao.obs_fim IS 'Observação de finalização da ação';
COMMENT ON COLUMN public.pdi_acao.revisado IS 'Booleano de ação revisada';
COMMENT ON COLUMN public.pdi_acao.id_funcionario IS 'Id do funcionario avaliado';


-- Permissions

ALTER TABLE public.pdi_acao OWNER TO deziuser;
GRANT ALL ON TABLE public.pdi_acao TO deziuser;



CREATE SEQUENCE public.seq_pdi_acao_competencia_detalhamento
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_pdi_acao_competencia_detalhamento OWNER TO deziuser;


CREATE TABLE public.pdi_acao_competencia_detalhamento (
	id bigserial NOT NULL, -- Identificador primário da tabela
	id_pdi_acao int8 NOT NULL, -- Id da ação de PDI
	id_competencia int8 NOT NULL, -- Id da competência
	id_competencia_detalhamento int8 NULL, -- Id do detalhamento
	CONSTRAINT pdi_acao_competencia_detalhamento_pkey PRIMARY KEY (id),
	CONSTRAINT fk_competencia_detalhamento_pdi_acao_competencia_detalhamento FOREIGN KEY (id_competencia_detalhamento) REFERENCES competencia_detalhamento(id),
	CONSTRAINT fk_competencia_pdi_acao_competencia_detalhamento FOREIGN KEY (id_competencia) REFERENCES competencia(id),
	CONSTRAINT fk_pdi_acao_pdi_acao_competencia_detalhamento FOREIGN KEY (id_pdi_acao) REFERENCES pdi_acao(id)
);

-- Column comments

COMMENT ON COLUMN public.pdi_acao_competencia_detalhamento.id IS 'Identificador primário da tabela';
COMMENT ON COLUMN public.pdi_acao_competencia_detalhamento.id_pdi_acao IS 'Id da ação de PDI';
COMMENT ON COLUMN public.pdi_acao_competencia_detalhamento.id_competencia IS 'Id da competência';
COMMENT ON COLUMN public.pdi_acao_competencia_detalhamento.id_competencia_detalhamento IS 'Id do detalhamento';

-- Permissions

ALTER TABLE public.pdi_acao_competencia_detalhamento OWNER TO deziuser;
GRANT ALL ON TABLE public.pdi_acao_competencia_detalhamento TO deziuser;
