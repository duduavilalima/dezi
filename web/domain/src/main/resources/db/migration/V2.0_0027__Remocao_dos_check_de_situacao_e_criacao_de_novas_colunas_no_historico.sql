-- Remocao dos check de situacao
ALTER TABLE public.avaliacao
DROP CONSTRAINT check_situacao;

ALTER TABLE public.avaliacao
DROP CONSTRAINT check_situacao_preenchimento;

ALTER TABLE public.campanha
DROP CONSTRAINT check_situacao;


-- Criacao de novas colunas no historico
ALTER TABLE public.modelo_graduacao_aval_comp
   ALTER COLUMN id_avaliacao_competencia DROP NOT NULL;


ALTER TABLE public.modelo_graduacao_aval_comp ADD COLUMN id_avaliacao_comp_det bigint;

ALTER TABLE public.modelo_graduacao_aval_comp
  ADD CONSTRAINT fk_avaliacao_comp_det FOREIGN KEY (id_avaliacao_comp_det)
      REFERENCES public.avaliacao_comp_det (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.avaliacao_comp_det ADD COLUMN id_modelo_graduacao bigint;

ALTER TABLE public.avaliacao_comp_det
  ADD CONSTRAINT fk_modelo_graduacao FOREIGN KEY (id_modelo_graduacao)
      REFERENCES public.modelo_graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.modelo_graduacao_comp ADD COLUMN id_avaliacao bigint;

ALTER TABLE public.modelo_graduacao_comp
  ADD CONSTRAINT fk_avaliacao FOREIGN KEY (id_avaliacao) REFERENCES public.avaliacao (id) ON UPDATE NO ACTION ON DELETE NO ACTION;


