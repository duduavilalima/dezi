CREATE SEQUENCE public.seq_bi_parametro_usuario
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_bi_parametro_usuario OWNER TO deziuser;

CREATE TABLE public.bi_parametro_usuario (
	id int8 NOT NULL DEFAULT nextval('seq_bi_parametro_usuario'::regclass),
	id_empresa int8 NOT NULL,
	id_funcionario int8 NOT NULL,
	id_perfil smallint NOT NULL,
	id_campanha int8 NOT NULL,
	nome_gestor varchar(255) NOT NULL DEFAULT '%',
	nome_funcionario varchar(255) NOT NULL DEFAULT '%',
	campanha_descricao varchar(255) NOT NULL,
	data_criacao timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	chave varchar(255) NOT NULL,
	CONSTRAINT bi_parametro_usuario_pk PRIMARY KEY (id)
);

ALTER TABLE public.bi_parametro_usuario OWNER TO deziuser;


