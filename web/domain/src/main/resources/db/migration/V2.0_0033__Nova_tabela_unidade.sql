CREATE SEQUENCE public.seq_unidade
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_unidade
  OWNER TO avduser;

CREATE TABLE public.unidade
(
   id bigint NOT NULL DEFAULT nextval('seq_unidade'::regclass),
   id_empresa bigint NOT NULL,
   nome character varying(255) NOT NULL,
   descricao text NOT NULL,
   id_pai bigint,
   id_situacao bigint NOT NULL,
   criador character varying(255) NOT NULL,
   data_criacao timestamp with time zone NOT NULL,
   atualizador character varying(255),
   data_atualizacao timestamp with time zone,
   CONSTRAINT unidade_pk PRIMARY KEY (id),
   CONSTRAINT unidade_empresa_pk FOREIGN KEY (id_empresa) REFERENCES public.empresa (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT unidade_pai_pk FOREIGN KEY (id_pai) REFERENCES public.unidade (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.unidade
  OWNER TO avduser;


