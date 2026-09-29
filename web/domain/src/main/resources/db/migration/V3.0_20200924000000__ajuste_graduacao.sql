UPDATE graduacao SET ordem = 1000, ponto = 0 WHERE id_situacao = 98;
UPDATE graduacao SET ponto = 0 WHERE "label" = 'Não posso avaliar';
UPDATE graduacao_competencia SET ponto = 0 WHERE "label" = 'Não posso avaliar';

UPDATE graduacao SET ordem = (
	SELECT MAX(ordem) + 1
	FROM graduacao grad
	WHERE grad.id_modelo_graduacao = graduacao.id_modelo_graduacao
	AND grad."label" <> 'Não posso avaliar'
)
WHERE "label" = 'Não posso avaliar';

UPDATE graduacao_competencia SET ordem = (
	SELECT MAX(ordem) + 1
	FROM graduacao_competencia gc
	WHERE gc.id_modelo_graduacao_comp = graduacao_competencia.id_modelo_graduacao_comp
	AND gc."label" <> 'Não posso avaliar'
)
WHERE "label" = 'Não posso avaliar';
