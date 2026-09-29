--AVD-729
ALTER TABLE public.conceito RENAME descricao_conceito TO descricao;

ALTER TABLE public.conceito ALTER COLUMN descricao TYPE character varying(255);

ALTER TABLE public.conceito ADD COLUMN nome character varying(255);
