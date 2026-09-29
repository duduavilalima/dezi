ALTER TABLE public.avaliacao_preenchimento ADD COLUMN id_graduacao_comp_av1 bigint;

ALTER TABLE public.avaliacao_preenchimento ADD COLUMN id_graduacao_comp_av2 bigint;

ALTER TABLE public.avaliacao_preenchimento ADD COLUMN id_graduacao_comp_av3 bigint;

ALTER TABLE public.avaliacao_preenchimento
  ADD CONSTRAINT fk_graduacao_comp_av1 FOREIGN KEY (id_graduacao_comp_av1)
      REFERENCES public.graduacao_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.avaliacao_preenchimento
  ADD CONSTRAINT fk_graduacao_comp_av2 FOREIGN KEY (id_graduacao_comp_av2)
      REFERENCES public.graduacao_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.avaliacao_preenchimento
  ADD CONSTRAINT fk_graduacao_comp_av3 FOREIGN KEY (id_graduacao_comp_av3)
      REFERENCES public.graduacao_competencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
