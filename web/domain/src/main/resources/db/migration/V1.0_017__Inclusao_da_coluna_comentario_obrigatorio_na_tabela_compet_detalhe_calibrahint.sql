-- Inclusão da coluna comentario_obrigatorio na tabela compet_detalhe_calibrahint
ALTER TABLE compet_detalhe_calibrahint ADD COLUMN comentario_obrigatorio boolean;

-- Insere o valor false como default na coluna comentario_obrigatorio
UPDATE compet_detalhe_calibrahint set comentario_obrigatorio = false;
