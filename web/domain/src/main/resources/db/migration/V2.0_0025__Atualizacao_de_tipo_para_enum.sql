--Alterando Tipo da Foto para Enum--

ALTER TABLE public.foto
  DROP CONSTRAINT fk_tipo_foto;


--Alterando Tipo do perfil de Funcionario para Enum--

ALTER TABLE public.funcionario
  DROP CONSTRAINT fk_tipo_funcionario_perfil_uso;

--Alterando Tipo de sexo de Funcionario para Enum--

ALTER TABLE public.funcionario
  DROP CONSTRAINT fk_tipo_funcionario_sexo;


--Alterando Tipo do Conceito para Enum--

ALTER TABLE public.conceito
  DROP CONSTRAINT fk_tipo_conceito;


--Alterando Tipo de AvaliacaoParticipante para Enum--

ALTER TABLE public.avaliacao_participante
  DROP CONSTRAINT fk_tipo_perfil_agente;


--Alterando Tipo de AvaliacaoCompetencia para Enum--

ALTER TABLE public.avaliacao_competencia
  DROP CONSTRAINT fk_tipo_competencia;


--Alterando Tipo de AvaliacaoTipoCompetencia para Enum--

ALTER TABLE public.avaliacao_tipo_competencia
  DROP CONSTRAINT fk_tipo_perfil_agente;
