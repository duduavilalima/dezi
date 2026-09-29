--
-- AJUSTE DO CAMPO DESCRIÇÃO ABREVIADA DA TABELA COMPETÊNCIA.
--
ALTER TABLE competencia ALTER COLUMN descricao_abreviada TYPE character varying(150);
COMMENT ON COLUMN competencia.descricao_abreviada IS 'Descrição abreviada da competencia EX:  ENGAJAMENTO TRAHALHO EM EQUIPE COMUNICAÇÃO FLEXIBILIDADE ATITUDE INSPIRADORA';

