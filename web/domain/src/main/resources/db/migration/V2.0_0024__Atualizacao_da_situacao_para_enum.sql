--Atualização da situação do cargo para enum--

ALTER TABLE public.cargo
  DROP CONSTRAINT fk_situacao_cargo;
UPDATE cargo  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação do cursoGraduacao para enum--

ALTER TABLE public.curso_graduacao
  DROP CONSTRAINT fk_situacao_curso_graduacao;
UPDATE curso_graduacao  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Competencia para enum--

ALTER TABLE public.competencia
  DROP CONSTRAINT fk_situacao_competencia;
UPDATE competencia  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de ModeloGraduacao para enum--

ALTER TABLE public.modelo_graduacao
  DROP CONSTRAINT fk_situacao_modelo_graduacao;
UPDATE modelo_graduacao  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de CompetenciaDetalhamento para enum--

ALTER TABLE public.competencia_detalhamento
  DROP CONSTRAINT fk_situacao_competencia_detalhamento;
UPDATE competencia_detalhamento  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Graduacao para enum--

ALTER TABLE public.graduacao
   DROP CONSTRAINT fk_situacao;
UPDATE graduacao  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de CargoCompetencia para enum--

ALTER TABLE public.cargo_competencia
  DROP CONSTRAINT fk_situacao_cargo_competencia;
UPDATE cargo_competencia  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Escolaridade para enum--

ALTER TABLE public.escolaridade
  DROP CONSTRAINT fk_situacao_escolaridade;
UPDATE escolaridade  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Conceito para enum--

ALTER TABLE public.conceito
  DROP CONSTRAINT fk_situacao_conceito;
UPDATE conceito  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Idioma para enum--

ALTER TABLE public.idioma
  DROP CONSTRAINT fk_situacao_idioma;
UPDATE idioma  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de CursoEspecializacao para enum--

ALTER TABLE public.curso_especializacao
  DROP CONSTRAINT fk_situacao_curso_especializacao;
UPDATE curso_especializacao  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de TipoCurso para enum--

ALTER TABLE public.tipo_curso
  DROP CONSTRAINT fk_situacao_tipo_curso;
UPDATE tipo_curso  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Empresa para enum--

ALTER TABLE public.empresa
  DROP CONSTRAINT fk_situacao_empresa;
UPDATE empresa  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Departamento para enum--

ALTER TABLE public.departamento
  DROP CONSTRAINT fk_situacao_departamento;
UPDATE departamento  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de FuncionarioFormacao para enum--

ALTER TABLE public.funcionario_formacao
  DROP CONSTRAINT fk_situacao_funcionario_formacao;


--Atualização da situação de FuncionarioIdioma para enum--

ALTER TABLE public.funcionario_idioma
  DROP CONSTRAINT fk_situacao_funcionario_idioma;
ALTER TABLE public.funcionario_idioma
  DROP CONSTRAINT fk_situacao_nivel_escrita;
ALTER TABLE public.funcionario_idioma
  DROP CONSTRAINT fk_situacao_nivel_leitura;


--Atualização da situação de ContratanteFasolti para enum--

ALTER TABLE public.contratante_fasolti
  DROP CONSTRAINT fk_situacao_contratante_fasolti;
UPDATE empresa  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.


--Atualização da situação de Funcionario para enum--

ALTER TABLE public.funcionario
  DROP CONSTRAINT fk_situacao_funcionario_situacao;
ALTER TABLE public.funcionario
  DROP CONSTRAINT fk_situacao_funcionario_situacao_funcional;

UPDATE funcionario  SET id_situacao = 98 WHERE id_situacao = 2; --Troca de situacao INATIVA (2) para (98) enum.
UPDATE funcionario  SET id_situacao_funcional = 1 WHERE id_situacao_funcional = 3; --Troca de situacao funcional EM_ATIVIDADE (3) para (1) enum.
UPDATE funcionario  SET id_situacao_funcional = 2 WHERE id_situacao_funcional = 4; --Troca de situacao funcional FERIAS (4) para (2) enum.
UPDATE funcionario  SET id_situacao_funcional = 3 WHERE id_situacao_funcional = 5; --Troca de situacao funcional LICENCA_MATERNIDADE (5) para (3) enum.
UPDATE funcionario  SET id_situacao_funcional = 4 WHERE id_situacao_funcional = 6; --Troca de situacao funcional LICENCA_SEM_REMUNERACAO (6) para (4) enum.
UPDATE funcionario  SET id_situacao_funcional = 5 WHERE id_situacao_funcional = 7; --Troca de situacao funcional DISPENSA_MEDICA (7) para (5) enum.
UPDATE funcionario  SET id_situacao_funcional = 6 WHERE id_situacao_funcional = 8; --Troca de situacao funcional SERVICO_MILITAR_OBRIGATORIO (8) para (6) enum.
UPDATE funcionario  SET id_situacao_funcional = 7 WHERE id_situacao_funcional = 23; --Troca de situacao funcional LICENCA_PATERNIDADE (23) para (7) enum.
UPDATE funcionario  SET id_situacao_funcional = 8 WHERE id_situacao_funcional = 24; --Troca de situacao funcional LUTO (24) para (8) enum.
UPDATE funcionario  SET id_situacao_funcional = 9 WHERE id_situacao_funcional = 25; --Troca de situacao funcional NUPCIAS (25) para (9) enum.
UPDATE funcionario  SET id_situacao_funcional = 10 WHERE id_situacao_funcional = 26; --Troca de situacao funcional LICENCA_ADOCAO (26) para (10) enum.
UPDATE funcionario  SET id_situacao_funcional = 11 WHERE id_situacao_funcional = 100; --Troca de situacao funcional DESLIGADO (100) para (11) enum.
UPDATE funcionario  SET id_situacao_funcional = 12 WHERE id_situacao_funcional = 102; --Troca de situacao funcional RECESSO (102) para (12) enum.














