ALTER TABLE public.apontamento_funcional
   ALTER COLUMN texto DROP NOT NULL;
COMMENT ON COLUMN public.apontamento_funcional.texto IS 'Descrição do apontamento ';
