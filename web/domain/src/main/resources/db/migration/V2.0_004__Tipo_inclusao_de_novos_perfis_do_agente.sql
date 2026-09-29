/*  AVD-366 */
INSERT INTO tipo(
            id, id_situacao, nome_curto, descricao, valor, ordem, tabela,
            sub_tipo, criador, data_criacao, atualizador, data_atualizacao)
    VALUES (nextval('seq_tipo'), 1, 'Gestor', 'Gestor', 'gestor', 1, 'avaliacao',
            'avaliacao_perfil', '1%admin', current_date, '1%admin', current_date);


INSERT INTO tipo(
            id, id_situacao, nome_curto, descricao, valor, ordem, tabela,
            sub_tipo, criador, data_criacao, atualizador, data_atualizacao)
    VALUES (nextval('seq_tipo'), 1, 'Subordinado', 'Subordinado', 'subordinado', 2, 'avaliacao',
            'avaliacao_perfil', '1%admin', current_date, '1%admin', current_date);


INSERT INTO tipo(
    id, id_situacao, nome_curto, descricao, valor, ordem, tabela,
    sub_tipo, criador, data_criacao, atualizador, data_atualizacao)
VALUES (nextval('seq_tipo'), 1, 'Pares', 'Pares', 'pares', 3, 'avaliacao',
    'avaliacao_perfil', '1%admin', current_date, '1%admin', current_date);
