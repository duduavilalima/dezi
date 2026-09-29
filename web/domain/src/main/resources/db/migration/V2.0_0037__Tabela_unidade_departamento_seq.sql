CREATE SEQUENCE public.seq_unidade_departamento
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.seq_unidade_departamento OWNER TO avduser;

CREATE TABLE public.unidade_departamento
(
   id bigint NOT NULL DEFAULT nextval('seq_unidade_departamento'::regclass),
   id_unidade bigint NOT NULL,
   id_departamento bigint NOT NULL,
   CONSTRAINT unidade_departamento_pk PRIMARY KEY (id),
   CONSTRAINT unidade_fk FOREIGN KEY (id_unidade) REFERENCES public.unidade (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT departamento_fk FOREIGN KEY (id_departamento) REFERENCES public.departamento (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.unidade_departamento
  OWNER TO avduser;

ALTER TABLE public.unidade_departamento ADD COLUMN id_gestor bigint;
ALTER TABLE public.unidade_departamento ADD CONSTRAINT id_gestor_fk FOREIGN KEY (id_gestor) REFERENCES public.funcionario (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
