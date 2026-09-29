ALTER TABLE public.departamento
  ADD COLUMN id_pai bigint;
ALTER TABLE public.departamento
  ADD CONSTRAINT id_pai_departamento_fk FOREIGN KEY (id_pai) REFERENCES public.departamento (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
