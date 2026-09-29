ALTER TABLE cargo_competencia ADD COLUMN peso smallint;

UPDATE cargo_competencia
SET peso = 1;
