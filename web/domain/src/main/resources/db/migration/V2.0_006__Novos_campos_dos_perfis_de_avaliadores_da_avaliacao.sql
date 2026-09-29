/*  AVD-387 */

ALTER TABLE avaliacao
   ADD COLUMN gestores boolean;

ALTER TABLE avaliacao
   ADD COLUMN subordinados boolean;

ALTER TABLE avaliacao
   ADD COLUMN pares boolean;

UPDATE avaliacao
SET gestores = true, subordinados = true, pares = true;
