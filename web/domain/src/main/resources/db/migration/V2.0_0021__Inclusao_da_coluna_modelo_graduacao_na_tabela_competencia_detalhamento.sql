-- AVD-592

ALTER TABLE public.competencia_detalhamento ADD COLUMN id_modelo_graduacao bigint;


UPDATE competencia_detalhamento cd
SET id_modelo_graduacao = c.id_modelo_graduacao
FROM (select c1.*
    from competencia c1) c
WHERE cd.id_competencia = c.id;

ALTER TABLE public.competencia_detalhamento
  ADD CONSTRAINT fk_modelo_graduacao FOREIGN KEY (id_modelo_graduacao)
      REFERENCES public.modelo_graduacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.competencia_detalhamento ALTER COLUMN id_modelo_graduacao SET NOT NULL;

ALTER TABLE public.competencia ALTER COLUMN id_modelo_graduacao DROP NOT NULL;
ALTER TABLE public.competencia ALTER COLUMN id_modelo_graduacao DROP DEFAULT;
