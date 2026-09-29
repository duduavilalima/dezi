ALTER TABLE public.configuracao ADD data_ultimo_aceite timestamp;

DROP TABLE public.test_fact;
INSERT INTO public.parametros (chave, valor) VALUES('data_revisao_TU_PP', '2020/01/01');
