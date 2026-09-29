CREATE TABLE public.cargo_tipo_competencia
(
   id bigint NOT NULL,
   id_cargo bigint NOT NULL,
   id_tipo_competencia bigint NOT NULL,
   id_situacao bigint NOT NULL,
   ordem smallint,
   peso smallint NOT NULL,
   criador character varying(255) NOT NULL,
   data_criacao timestamp without time zone NOT NULL,
   atualizador character varying(255),
   data_atualizacao timestamp with time zone,
   CONSTRAINT "PK_cargo_tipo_competencia" PRIMARY KEY (id),
   CONSTRAINT "FK_cargo" FOREIGN KEY (id_cargo) REFERENCES cargo (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT "FK_tipo_compotencia" FOREIGN KEY (id_tipo_competencia) REFERENCES tipo_competencia (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.cargo_tipo_competencia
  OWNER TO avduser;
COMMENT ON TABLE public.cargo_tipo_competencia
  IS 'Tabela de associação de cargo com tipo de competência';


CREATE SEQUENCE public.seq_cargo_tipo_competencia
   INCREMENT 1
   START 1
   MINVALUE 1
   MAXVALUE 9223372036854775807
   CACHE 1;
ALTER SEQUENCE public.seq_cargo_tipo_competencia
  OWNER TO avduser;


ALTER TABLE public.cargo_tipo_competencia
   ALTER COLUMN id SET DEFAULT nextval('seq_cargo_tipo_competencia');
