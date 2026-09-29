--
-- Referente ao Issue: AVD-343
--
CREATE TABLE public.avaliacao_tipo_competencia(
   id bigserial NOT NULL,
   id_avaliacao bigint NOT NULL,
   id_tipo bigint NOT NULL,
   CONSTRAINT pk_avaliacao_tipo_competencia PRIMARY KEY (id),
   CONSTRAINT pk_avaliacao_tipo_competencia_tipo FOREIGN KEY (id_avaliacao) REFERENCES avaliacao (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT pk_tipo_competencia_tipo FOREIGN KEY (id_tipo) REFERENCES tipo (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE public.avaliacao_tipo_competencia OWNER TO avduser;

COMMENT ON COLUMN public.avaliacao_tipo_competencia.id_avaliacao IS 'O id da avaliação.';
COMMENT ON COLUMN public.avaliacao_tipo_competencia.id_tipo IS 'O id do tipo da competência.';
COMMENT ON TABLE public.avaliacao_tipo_competencia IS 'Tabela que representa os tipo de competências que uma avaliação deve ter no momento de sua criação.';

