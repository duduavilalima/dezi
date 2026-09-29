--
-- NOVA COLUNA INDICANDO SE UM DETALHAMENTO É APLICÁVEL A UM CARGO RELACIONADO.
--
ALTER TABLE cargo_compet_det_gabarito
  ADD COLUMN aplicavel_cargo boolean NOT NULL DEFAULT false;
COMMENT ON COLUMN cargo_compet_det_gabarito.aplicavel_cargo IS 'Indica se o detalhamento será aplicado para o cargo associado.';
