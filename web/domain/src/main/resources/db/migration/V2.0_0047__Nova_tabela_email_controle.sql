CREATE SEQUENCE public.seq_email_controle
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_email_controle
  OWNER TO deziuser;

CREATE TABLE public.email_controle
(
   id bigint NOT NULL DEFAULT nextval('seq_email_controle'::regclass),
   id_destinatario bigint,
   email character varying(255) NOT NULL,
   assunto text,
   mensagem text,
   template text,
   status smallint NOT NULL,
   msg_erro text,
   criador character varying(255),
   data_criacao timestamp with time zone,
   atualizador character varying(255),
   data_atualizacao timestamp with time zone,
   CONSTRAINT email_controle_pk PRIMARY KEY (id)
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.email_controle
  OWNER TO deziuser;
