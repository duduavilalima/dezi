CREATE SEQUENCE public.seq_departamento_gestor;
ALTER SEQUENCE public.seq_departamento_gestor OWNER TO deziuser;

CREATE TABLE public.departamento_gestor
(
   id bigint NOT NULL DEFAULT nextval('seq_departamento_gestor'),
   id_gestor bigint NOT NULL,
   id_departamento bigint NOT NULL,
	criador character varying(255) NOT NULL, -- Criador do registro
	data_criacao timestamp without time zone NOT NULL, -- Data de criação do registro
	atualizador character varying(255), -- Último atualizador do registro
	data_atualizacao timestamp without time zone, -- Última data de atualização do registro
   CONSTRAINT pk_departamento_gestor PRIMARY KEY (id),
   CONSTRAINT fk_departamento_gestor_departamento FOREIGN KEY (id_departamento) REFERENCES public.departamento (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_departamento_gestor_gestor FOREIGN KEY (id_gestor) REFERENCES public.funcionario (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT uq_departamento_gestor UNIQUE (id_gestor, id_departamento)
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.departamento_gestor OWNER TO deziuser;
COMMENT ON TABLE public.departamento_gestor
  IS 'Mantém as informações completas de gestores de departamento (setor), para toda a hierarquia de departamentos, ou seja, conterá não somente o departamento ao qual o gestor está ligado diretamento, mas como toda a hierarquia de departamentos abaixo.';

