ALTER TABLE public.avaliacao_preenchimento
  ADD COLUMN data_preenchimento date;
COMMENT ON COLUMN public.avaliacao_preenchimento.data_preenchimento IS 'Data de preenchimento do detalhamento';


ALTER TABLE public.avaliacao_preenchimento
  ADD COLUMN data_gravacao date;
COMMENT ON COLUMN public.avaliacao_preenchimento.data_gravacao IS 'Data em que o registro foi gravado localmente no Mobile.';


ALTER TABLE public.avaliacao_preenchimento
  ADD COLUMN id_tipo_client bigint;
COMMENT ON COLUMN public.avaliacao_preenchimento.id_tipo_client IS 'Identificador do tipo do client utilizado para preencher (web ou app)';


ALTER TABLE public.avaliacao_preenchimento ADD COLUMN criador character varying(255);
COMMENT ON COLUMN public.avaliacao_preenchimento.criador IS 'Criador do registro';
UPDATE avaliacao_preenchimento SET criador = '100%admin';
ALTER TABLE public.avaliacao_preenchimento ALTER COLUMN criador SET NOT NULL;


ALTER TABLE public.avaliacao_preenchimento ADD COLUMN data_criacao timestamp without time zone;
COMMENT ON COLUMN public.avaliacao_preenchimento.data_criacao IS 'Data de criação do registro';
UPDATE avaliacao_preenchimento SET data_criacao = current_date;
ALTER TABLE public.avaliacao_preenchimento ALTER COLUMN data_criacao SET NOT NULL;


ALTER TABLE public.avaliacao_preenchimento ADD COLUMN atualizador character varying(255);
COMMENT ON COLUMN public.avaliacao_preenchimento.atualizador IS 'Último atualizador do registro';
UPDATE avaliacao_preenchimento SET atualizador = '100%admin';


ALTER TABLE public.avaliacao_preenchimento ADD COLUMN data_atualizacao timestamp without time zone;
COMMENT ON COLUMN public.avaliacao_preenchimento.data_atualizacao IS 'Última data de atualização do registro';
UPDATE avaliacao_preenchimento SET data_atualizacao = current_date;
