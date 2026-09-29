/* Nova linha de situacao do funcionário: recesso, do tipo funcional. */

INSERT INTO public.situacao(
            nome_curto, descricao, chave, permissao_editar, permissao_suspender,
            permissao_reativar, permissao_cancelar, ordem, tabela, sub_tipo)
    VALUES ('Recesso', 'Recesso', 'recesso', 0, 0,
            0, 0, 11, 'funcionario', 'funcional');



/* Novas colunas de funcionário: data de inicio e fim do recesso. */

ALTER TABLE public.funcionario
   ADD COLUMN data_inicio_recesso date;
COMMENT ON COLUMN public.funcionario.data_inicio_recesso
  IS 'Data de inicio do recesso';

ALTER TABLE public.funcionario
   ADD COLUMN data_fim_recesso date;
COMMENT ON COLUMN public.funcionario.data_fim_recesso
  IS 'Data de fim do recesso';

