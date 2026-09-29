ALTER TABLE public.funcionario DROP CONSTRAINT fk_funcionario_avaliador;
ALTER TABLE public.funcionario DROP CONSTRAINT fk_funcionario_revisor;
ALTER TABLE public.funcionario DROP COLUMN id_avaliador;
ALTER TABLE public.funcionario DROP COLUMN id_revisor;
ALTER TABLE public.funcionario ADD COLUMN id_unidade bigint;
ALTER TABLE public.funcionario ADD CONSTRAINT unidade_fk FOREIGN KEY (id_unidade) REFERENCES public.unidade (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
