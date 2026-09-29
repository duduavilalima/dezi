-- AVD-260

ALTER TABLE apontamento_funcional ADD COLUMN feedforward text;
ALTER TABLE apontamento_funcional ADD COLUMN id_competencia bigint NOT NULL;
ALTER TABLE apontamento_funcional ADD CONSTRAINT fk_competencia_apontamento FOREIGN KEY (id_competencia) REFERENCES competencia (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMENT ON COLUMN apontamento_funcional.feedforward IS 'Mensagens de feedforward.';
COMMENT ON COLUMN apontamento_funcional.id_competencia IS 'Competência relacionada ao apontamento';

-- Boolean que indica se o campo feedforward aparecerá no mobile.
ALTER TABLE empresa ADD COLUMN feedforward boolean NOT NULL DEFAULT false;
COMMENT ON COLUMN empresa.feedforward IS 'Indica se o campo feedforward aparecerá no mobile ou não.';
