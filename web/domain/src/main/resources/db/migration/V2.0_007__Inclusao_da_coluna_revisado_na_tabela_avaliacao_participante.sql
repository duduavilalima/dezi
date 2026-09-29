/*  AVD-387 */

ALTER TABLE avaliacao_participante ADD COLUMN revisado boolean;

UPDATE avaliacao_participante ap
SET revisado = av.situacao
FROM (SELECT a.*, CASE WHEN id_situacao > 1 THEN true ELSE false END "situacao"
	FROM avaliacao a) av
WHERE ap.id_avaliacao = av.id;
