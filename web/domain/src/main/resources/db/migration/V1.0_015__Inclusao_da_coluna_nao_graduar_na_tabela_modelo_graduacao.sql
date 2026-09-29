-- Inclusão da coluna nao_graduar na tabela modelo de graduação
ALTER TABLE modelo_graduacao ADD COLUMN nao_graduar boolean;

-- Insere o valor false como default na coluna nao_graduar
UPDATE modelo_graduacao set nao_graduar = false;
