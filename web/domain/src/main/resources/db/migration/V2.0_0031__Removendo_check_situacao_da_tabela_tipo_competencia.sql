ALTER TABLE tipo_competencia DROP CONSTRAINT check_situacao_tipo_competencia;

-- index_tabela_cargo

CREATE UNIQUE INDEX uq_id_empresa_id_cargo_cliente
  ON public.cargo
  USING btree
  (id_empresa, id_cargo_cliente COLLATE pg_catalog."default");
