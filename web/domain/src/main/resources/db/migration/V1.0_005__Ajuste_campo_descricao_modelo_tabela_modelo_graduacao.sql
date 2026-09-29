-- AVD-326 / “Escala de graduação”: Alterar o número de caracteres de 40 para 80. Tabela no banco DESCRICAO_MODELO.
ALTER TABLE modelo_graduacao ALTER COLUMN descricao_modelo TYPE character varying(80);
