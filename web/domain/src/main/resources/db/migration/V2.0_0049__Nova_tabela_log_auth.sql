CREATE SEQUENCE public.seq_log_auth
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_log_auth
  OWNER TO deziuser;

CREATE TABLE public.log_auth
(
   id bigint NOT NULL DEFAULT nextval('seq_log_auth'::regclass),
   user_name character varying(100) NOT NULL,
   data timestamp with time zone,
   ip character varying(40),
   id_situacao smallint NOT NULL,
   CONSTRAINT log_auth_pk PRIMARY KEY (id)
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.log_auth
  OWNER TO deziuser;
