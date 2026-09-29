CREATE TABLE IF NOT EXISTS public.log_auth (
	id bigserial NOT NULL,
	user_name varchar(100) NOT NULL,
	"data" timestamptz NULL,
	ip varchar(40) NULL,
	id_situacao int2 NOT NULL,
	CONSTRAINT log_auth_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.log_entity (
	id bigserial NOT NULL,
	id_entidade int8 NULL,
	data_criacao timestamp NULL,
	usuario varchar(100) NULL,
	nome_entidade varchar(150) NULL,
	objeto text NULL,
	uri text NULL,
	CONSTRAINT pk_log_entity PRIMARY KEY (id)
);
