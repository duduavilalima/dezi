--
-- Name: seq_cbo_ocupacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_cbo_ocupacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;

--
-- Name: seq_cbo_perfil; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_cbo_perfil
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;

--
-- Name: seq_cbo_atribuicao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_cbo_atribuicao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;

--
-- Name: cbd_ocupacao; Type: TABLE; Schema: public; Owner: avduser | INSERT INTO cbo_ocupacao(codigo_cbo, descricao_ocupacao) VALUES (?, ?);
-- =CONCATENATE("INSERT INTO cbo_ocupacao(codigo_cbo, descricao_ocupacao) VALUES ('";A2;"','";B2;"');")

CREATE TABLE public."cbo_ocupacao"(
   id bigint DEFAULT nextval('seq_cbo_ocupacao'::regclass) NOT NULL,
   codigo_cbo character varying(10),
   descricao_ocupacao character varying(255),
   CONSTRAINT pk_cbo_ocupacao PRIMARY KEY (id)
) WITH (  OIDS = FALSE);
ALTER TABLE public."cbo_ocupacao"  OWNER TO avduser;

--
-- Name: cbo_perfil; Type: TABLE; Schema: public; Owner: avduser | INSERT INTO cbo_perfil(id_cbo_ocupacao, perfil_cbo, descricao_perfil) VALUES (?, ?, ?);
-- =CONCATENATE("INSERT INTO cbo_perfil(id_cbo_ocupacao, perfil_cbo, descricao_perfil) VALUES ((select id from cbo_ocupacao where codigo_cbo = '";A2;"'), '";B2;"', '";C2;"');")

CREATE TABLE public.cbo_perfil(
   id bigint DEFAULT nextval('seq_cbo_perfil'::regclass) NOT NULL,
   id_cbo_ocupacao bigint,
   perfil_cbo character varying(2),
   descricao_perfil character varying(255),
   CONSTRAINT pk_cbo_perfil PRIMARY KEY (id),
   CONSTRAINT fk_cbo_perfil_cbo_ocupacao FOREIGN KEY (id_cbo_ocupacao) REFERENCES "cbo_ocupacao" (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (  OIDS = FALSE);
ALTER TABLE public.cbo_perfil  OWNER TO avduser;


--
-- Name: cbo_atividade; Type: TABLE; Schema: public; Owner: avduser | INSERT INTO cbo_atividade(id_cbo_perfil, ordem, descricao_atividade) VALUES (?, ?, ?);
-- =CONCATENATE("INSERT INTO cbo_atividade(id_cbo_perfil, ordem, descricao_atividade) VALUES ((select id from cbo_perfil where id_cbo_ocupacao = (select id from cbo_ocupacao where codigo_cbo = '";A2;"') and perfil_cbo = '";B2;"'), 1, '";C2;"');")

CREATE TABLE public.cbo_atividade(
   id bigint DEFAULT nextval('seq_cbo_atribuicao'::regclass) NOT NULL,
   id_cbo_perfil bigint,
   ordem integer,
   descricao_atividade character varying(255),
   CONSTRAINT pk_cbo_atividade PRIMARY KEY (id),
   CONSTRAINT fk_cbo_perfil_cbo_atividade FOREIGN KEY (id_cbo_perfil) REFERENCES cbo_perfil (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (  OIDS = FALSE);
ALTER TABLE public.cbo_atividade OWNER TO avduser;

--
-- Adicionar campo CBO para indicar se a empresa tem CBO ou não (Exibição de tela).
--

ALTER TABLE empresa ADD COLUMN cbo boolean NOT NULL DEFAULT true;


--
-- Novos campo no cargo para referencia o CBO usado.
--

ALTER TABLE cargo ADD COLUMN id_cbo_ocupacao bigint;
ALTER TABLE cargo ADD COLUMN id_cbo_perfil bigint;
ALTER TABLE cargo ADD CONSTRAINT fk_cbo_ocupacao_cargo FOREIGN KEY (id_cbo_ocupacao) REFERENCES cbo_ocupacao (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE cargo ADD CONSTRAINT fk_cbo_perfil_cargo FOREIGN KEY (id_cbo_perfil) REFERENCES cbo_perfil (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
