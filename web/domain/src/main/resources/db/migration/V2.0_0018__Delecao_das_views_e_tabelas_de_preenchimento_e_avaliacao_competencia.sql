DROP VIEW public.view_competencia_avaliacao;

DROP VIEW public.view_detalhamentos_participante;

ALTER TABLE public.avaliacao_preenchimento
DROP CONSTRAINT fk_avaliacao_competencias_avaliacao_preench;

DROP TABLE public.avaliacao_competencia;

DROP TABLE public.avaliacao_preenchimento;
