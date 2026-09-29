/*  AVD-366 */
ALTER TABLE avaliacao_participante
   ADD COLUMN id_perfil_agente bigint;

ALTER TABLE avaliacao_participante
  ADD CONSTRAINT fk_tipo_perfil_agente FOREIGN KEY (id_perfil_agente)
      REFERENCES tipo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
