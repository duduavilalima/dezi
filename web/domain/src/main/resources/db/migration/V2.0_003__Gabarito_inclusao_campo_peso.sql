/*  AVD-366 */
ALTER TABLE cargo_competencia
   DROP COLUMN peso;

ALTER TABLE cargo_compet_det_gabarito
   ADD COLUMN peso smallint;

UPDATE cargo_compet_det_gabarito
SET peso = 100;
