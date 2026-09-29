ALTER TABLE public.cargo
  DROP COLUMN gestor;
ALTER TABLE public.cargo
  ADD COLUMN id_departamento bigint;
ALTER TABLE public.cargo
  ADD CONSTRAINT depatamento_fk FOREIGN KEY (id_departamento) REFERENCES public.departamento (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
