--AVD-711
ALTER TABLE "cargo_compet_det_gabarito" RENAME TO "detalhamento_cargo_competencia";

--AVD-719
ALTER TABLE "avaliacao_det_gab" RENAME TO "avaliacao_det_cargo_comp";

ALTER TABLE avaliacao_det_cargo_comp RENAME id_gabarito TO id_det_cargo_comp;

ALTER TABLE avaliacao_det_cargo_comp RENAME peso_gabarito TO peso_det_cargo_comp;

ALTER TABLE public.avaliacao_det_cargo_comp DROP CONSTRAINT fk_gabarito;
