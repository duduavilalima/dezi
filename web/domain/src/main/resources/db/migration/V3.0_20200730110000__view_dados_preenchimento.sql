create view dado_preenchimento as
select max(pre.id) as id,
  -- Caracterização
  camp.id as id_campanha, max(camp.descricao) as campanha,
  av.id as id_avaliacao, max(av.descricao) as avaliacao,
  tipo_comp.id as id_tipo_competencia, max(tipo_comp.descricao) as tipo_competencia,
  comp.id as id_competencia, max(comp.descricao_abreviada) as competencia,
  -- Colaborador
  grad.id_responsavel, max(fr.nome) as responsavel,
  part.id_avaliado, max(fa.nome) as avaliado,
  max(part.id_depto_avaliado) as id_departamento, max(part.nome_depto_avaliado) as departamento,
  -- Autoavaliação
  sum(case when grad.tipo = 'av1' then 1 else 0 end) as total_auto,
  sum(case when grad.tipo = 'av1' and grad.id_graduacao is not null then 1 else 0 end) as preench_auto,
  -- Gestor
  sum(case when part.id_perfil_agente = 101 and grad.tipo = 'av2' then 1 else 0 end) as total_gestor,
  sum(case when part.id_perfil_agente = 101 and grad.tipo = 'av2' and grad.id_graduacao is not null then 1 else 0 end) as preench_gestor,
  -- Consenso
  sum(case when part.id_perfil_agente = 101 and grad.tipo = 'av3' then 1 else 0 end) as total_consenso,
  sum(case when part.id_perfil_agente = 101 and grad.tipo = 'av3' and grad.id_graduacao is not null then 1 else 0 end) as preench_consenso,
  -- Subordinados
  sum(case when part.id_perfil_agente = 102 then 1 else 0 end) as total_subord,
  sum(case when part.id_perfil_agente = 102 and grad.id_graduacao is not null then 1 else 0 end) as preench_subord,
  -- Pares
  sum(case when part.id_perfil_agente = 103 then 1 else 0 end) as total_pares,
  sum(case when part.id_perfil_agente = 103 and grad.id_graduacao is not null then 1 else 0 end) as preench_pares
from campanha camp
  join avaliacao av on (av.id_campanha = camp.id)
  join avaliacao_participante part on (part.id_avaliacao = av.id)
  join avaliacao_preenchimento pre on (pre.id_participante = part.id)
  cross join lateral (VALUES
  	('av1', av.preenche_av1, part.id_avaliado, pre.id_graduacao_av1),
  	('av2', av.preenche_av2, part.id_avaliador, pre.id_graduacao_av2),
  	('av3', av.preenche_av3, part.id_avaliador, pre.id_graduacao_av3)
  ) as grad(tipo, preenche, id_responsavel, id_graduacao)
  join avaliacao_comp_det av_comp_det on (av_comp_det.id = pre.id_avaliacao_comp_det)
  join avaliacao_competencia av_comp on (av_comp.id = av_comp_det.id_avaliacao_competencia)
  join competencia comp on (comp.id = av_comp.id_competencia)
  join tipo_competencia tipo_comp on (tipo_comp.id = comp.id_tipo_competencia)
  join funcionario fr on (fr.id = grad.id_responsavel)
  join funcionario fa on (fa.id = part.id_avaliado)
where grad.preenche
  and (id_perfil_agente in (100, 101) or tipo = 'av2')
group by camp.id, av.id, tipo_comp.id, comp.id, grad.id_responsavel, part.id_avaliado;
