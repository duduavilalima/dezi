-- Alteração dos ids de envio e destino (setando not null) e inserção das colunas de identificação de
-- envio e recebimento de mensagens da avaliação.

ALTER TABLE mensageria
   ALTER COLUMN id_envio DROP NOT NULL;
ALTER TABLE mensageria
   ALTER COLUMN id_destino DROP NOT NULL;
ALTER TABLE mensageria
  ADD COLUMN id_av_envio bigint;
ALTER TABLE mensageria
  ADD COLUMN id_av_destino bigint;
ALTER TABLE mensageria
ADD CONSTRAINT fk_avaliacao_envio FOREIGN KEY (id_av_envio) REFERENCES avaliacao (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE mensageria
ADD CONSTRAINT fk_avaliacao_destino FOREIGN KEY (id_av_destino) REFERENCES avaliacao (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE mensageria
ADD CONSTRAINT fk_funcionario_envio FOREIGN KEY (id_envio) REFERENCES funcionario (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE mensageria
ADD CONSTRAINT fk_funcionario_destino FOREIGN KEY (id_destino) REFERENCES funcionario (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMENT ON COLUMN mensageria.id_envio IS 'Idenfificador do usuário (remente)';
COMMENT ON COLUMN mensageria.id_destino IS 'Idenfificador do usuário (destinatário)';
COMMENT ON COLUMN mensageria.id_av_envio IS 'Idenfificador da avaliação (remetente)';
COMMENT ON COLUMN mensageria.id_av_destino IS 'Idenfificador da avaliação (destinatário)';
