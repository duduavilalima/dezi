ALTER TABLE public.configuracao OWNER TO deziuser;
ALTER TABLE public.configuracao DROP CONSTRAINT IF EXISTS FK_empresa_visivel_funcionario;
ALTER TABLE public.configuracao DROP COLUMN  id_empresa_visivel;
