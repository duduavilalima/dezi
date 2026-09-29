-- HOST PARA LOGIN AUTOMÁTICO
ALTER TABLE empresa
  ADD COLUMN host character varying(255);
COMMENT ON COLUMN empresa.host IS 'O host da URL da empresa.';

-- ATUALIZANDO A SITUAÇÃO
update tipo_competencia
set situacao = 98
where situacao = 2;
