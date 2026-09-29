/*  AVD-373 */

ALTER TABLE empresa ADD COLUMN label_gestores character varying(255) NOT NULL DEFAULT 'Gestores';
ALTER TABLE empresa ADD COLUMN label_subordinados character varying(255) NOT NULL DEFAULT 'Subordinados';
ALTER TABLE empresa ADD COLUMN label_pares character varying(255) NOT NULL DEFAULT 'Pares';
COMMENT ON COLUMN empresa.label_gestores IS 'Label para o processo da avaliação "Gestores".';
COMMENT ON COLUMN empresa.label_subordinados IS 'Label para o processo da avaliação "Subordinados".';
COMMENT ON COLUMN empresa.label_pares IS 'Label para o processo da avaliação "Pares".';