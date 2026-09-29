--
-- Name: seq_apontamento_funcional; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_apontamento_funcional
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: apontamento_funcional; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE apontamento_funcional (
    id bigint DEFAULT nextval('seq_apontamento_funcional'::regclass) NOT NULL,
    id_avaliado bigint NOT NULL,
    id_avaliador bigint NOT NULL,
    data_apontamento timestamp without time zone NOT NULL,
    graduacao_visual double precision NOT NULL,
    texto text NOT NULL
);


--
-- Name: TABLE apontamento_funcional; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE apontamento_funcional IS 'Tabela que tem por finalidade o armazenamento dos apontamentos funcionais (realizadas pelo APP)';


--
-- Name: COLUMN apontamento_funcional.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN apontamento_funcional.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN apontamento_funcional.id_avaliado; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN apontamento_funcional.id_avaliado IS 'Identificador do funcionáro avaliado';


--
-- Name: COLUMN apontamento_funcional.id_avaliador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN apontamento_funcional.id_avaliador IS 'Identificador do funcionáro avaliador';


--
-- Name: COLUMN apontamento_funcional.data_apontamento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN apontamento_funcional.data_apontamento IS 'Data em que foi feito o apontamento funcional';


--
-- Name: COLUMN apontamento_funcional.graduacao_visual; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN apontamento_funcional.graduacao_visual IS 'Graduação visual (estrelinhas) do app';


--
-- Name: COLUMN apontamento_funcional.texto; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN apontamento_funcional.texto IS 'Descrição do apontamento ';


--
-- Name: seq_avaliacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_avaliacao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: avaliacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE avaliacao (
    id bigint DEFAULT nextval('seq_avaliacao'::regclass) NOT NULL,
    id_campanha bigint NOT NULL,
    id_situacao bigint NOT NULL,
    id_situacao_preenchimento bigint NOT NULL,
    descricao_abreviada character varying(100) NOT NULL,
    descricao text NOT NULL,
    preenche_av1 boolean,
    limite_av1 date,
    preenche_av2 boolean,
    limite_av2 date,
    preenche_av3 boolean,
    limite_av3 date,
    preenche_av4 boolean,
    limite_av4 date,
    email_padrao_da_avaliacao text,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    CONSTRAINT check_situacao CHECK ((id_situacao = ANY (ARRAY[(1)::bigint, (2)::bigint, (98)::bigint, (99)::bigint]))),
    CONSTRAINT check_situacao_preenchimento CHECK ((id_situacao_preenchimento = ANY (ARRAY[(1)::bigint, (2)::bigint, (3)::bigint])))
);


--
-- Name: TABLE avaliacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE avaliacao IS 'Tabela que armazena as avaliações  Esta tabela funciona como se fosse um agregador dos dados É atraves dela que agregamos as competências e seus participantes (avaliados e avaliadores), os quais irão responder às competências';


--
-- Name: COLUMN avaliacao.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN avaliacao.id_campanha; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.id_campanha IS 'Id da campanha Toda avaliação pertence a 1 (e somente 1) campanha';


--
-- Name: COLUMN avaliacao.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.id_situacao IS 'Situações possíveis  1 - em planejamento 2 - liberada (ativa) 3 - inativa';


--
-- Name: COLUMN avaliacao.id_situacao_preenchimento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.id_situacao_preenchimento IS 'Situações possíveis  1 - em aberto 2 - parcialmente preenchida 3 - totalmente preenchida';


--
-- Name: COLUMN avaliacao.descricao_abreviada; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.descricao_abreviada IS 'Descrição (simples) da avaliação';


--
-- Name: COLUMN avaliacao.descricao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.descricao IS 'Descrição (detalhada da avaliação)';


--
-- Name: COLUMN avaliacao.preenche_av1; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.preenche_av1 IS 'Indicador se a avaliação terá (ou não) a autoavaliação';


--
-- Name: COLUMN avaliacao.limite_av1; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.limite_av1 IS 'Data limite de preenchimento da autoavaliação';


--
-- Name: COLUMN avaliacao.preenche_av2; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.preenche_av2 IS 'Indicador se a avaliação terá (ou não) a avaliação do gestor';


--
-- Name: COLUMN avaliacao.limite_av2; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.limite_av2 IS 'Data limite de preenchimento da avaliação do gestor';


--
-- Name: COLUMN avaliacao.preenche_av3; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.preenche_av3 IS 'Indicador se a avaliação terá (ou não) a avaliação de consenso';


--
-- Name: COLUMN avaliacao.limite_av3; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.limite_av3 IS 'Data limite de preenchimento da avaliação de consenso';


--
-- Name: COLUMN avaliacao.preenche_av4; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.preenche_av4 IS 'Indicador se a avaliação terá (ou não) a etapa de revisão';


--
-- Name: COLUMN avaliacao.limite_av4; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.limite_av4 IS 'Data limite de preenchimento da revisão';


--
-- Name: COLUMN avaliacao.email_padrao_da_avaliacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.email_padrao_da_avaliacao IS 'Aqui é armazenado o email padrão da avaliação Sempre que um comunicado a respeito desta avaliação for ser gerado... é neste modelo que o mesmo deve se basear';


--
-- Name: COLUMN avaliacao.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.criador IS 'Criador do registro';


--
-- Name: COLUMN avaliacao.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN avaliacao.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN avaliacao.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_avaliacao_competencias; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_avaliacao_competencias
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;


--
-- Name: avaliacao_competencia; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE avaliacao_competencia (
    id bigint DEFAULT nextval('seq_avaliacao_competencias'::regclass) NOT NULL,
    id_avaliacao_participante bigint NOT NULL,
    id_competencia_detalhamento bigint NOT NULL,
    preenchida_av1 boolean,
    preenchida_av2 boolean,
    preenchida_av3 boolean
);


--
-- Name: COLUMN avaliacao_competencia.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_competencia.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN avaliacao_competencia.id_avaliacao_participante; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_competencia.id_avaliacao_participante IS 'Id dos participantes da campanha (por consequencia eu já tenho avaliado, avaliador, revisor)';


--
-- Name: COLUMN avaliacao_competencia.id_competencia_detalhamento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_competencia.id_competencia_detalhamento IS 'Id do detlhamento das competências desta avalição';


--
-- Name: seq_avaliacao_participantes; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_avaliacao_participantes
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: avaliacao_participante; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE avaliacao_participante (
    id bigint DEFAULT nextval('seq_avaliacao_participantes'::regclass) NOT NULL,
    id_avaliacao bigint NOT NULL,
    id_avaliado bigint NOT NULL,
    id_avaliador bigint NOT NULL,
    id_revisor bigint,
    consenso_liberado boolean DEFAULT false,
    pdi text,
    comentario_final_avaliado text,
    comentario_final_avaliador text,
    comentario_final_revisor text,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    limite_av1 date,
    limite_av2 date,
    limite_av3 date,
    limite_av4 date,
    revisao_liberada boolean DEFAULT false,
    preenchida_av1 boolean DEFAULT false,
    preenchida_av2 boolean DEFAULT false,
    preenchida_av3 boolean DEFAULT false,
    consenso_iniciado boolean DEFAULT false,
    revisao_iniciada boolean DEFAULT false,
    preenchida_av4 boolean DEFAULT false,
    data_conclusao_av1 timestamp without time zone,
    data_conclusao_av2 timestamp without time zone,
    data_conclusao_av3 timestamp without time zone,
    data_conclusao_av4 timestamp without time zone
);


--
-- Name: TABLE avaliacao_participante; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE avaliacao_participante IS 'Tabela que armazena os participantes (avaliados e avaliadores) de uma determinada avaliação';


--
-- Name: COLUMN avaliacao_participante.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN avaliacao_participante.id_avaliacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.id_avaliacao IS 'Identificação da avaliação à qual esta distribuição está associada';


--
-- Name: COLUMN avaliacao_participante.id_avaliado; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.id_avaliado IS 'Identificação do funcionário que será avaliado ';


--
-- Name: COLUMN avaliacao_participante.id_avaliador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.id_avaliador IS 'Identificação do funcionário que será o avaliador';


--
-- Name: COLUMN avaliacao_participante.id_revisor; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.id_revisor IS 'Identificação do funcionário que será o revisor';


--
-- Name: COLUMN avaliacao_participante.consenso_liberado; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.consenso_liberado IS 'Campo booleano que determina se avaliação de consenso está ou não liberada para preenchimento. Uma avaliação de consenso só está liberada em 2 situações... sendo:  1) AV1 e AV2 estão preenchidas e a AV3 ainda não está preenhcida 2) AV1 e AV2 estão preenchidas e a AV3 já foi preenchida (mas o RH - a pedido do avaliado ou avaliador - abre ela para ser revisada)';


--
-- Name: COLUMN avaliacao_participante.pdi; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.pdi IS 'Campo texto onde é traçado (em linhas gerais) o plano de ação a ser realizado pelo funcionário avaliado';


--
-- Name: COLUMN avaliacao_participante.comentario_final_avaliado; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.comentario_final_avaliado IS 'Comentário final do avaliado (no momento do consenso)';


--
-- Name: COLUMN avaliacao_participante.comentario_final_avaliador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.comentario_final_avaliador IS 'Comentário final do avaliador (no momento do consenso)';


--
-- Name: COLUMN avaliacao_participante.comentario_final_revisor; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.comentario_final_revisor IS 'Comentário final do revisor (no momento da revisão)';


--
-- Name: COLUMN avaliacao_participante.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.criador IS 'Criador do registro';


--
-- Name: COLUMN avaliacao_participante.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN avaliacao_participante.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN avaliacao_participante.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_participante.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_avaliacao_preenchimento; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_avaliacao_preenchimento
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;


--
-- Name: avaliacao_preenchimento; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE avaliacao_preenchimento (
    id bigint DEFAULT nextval('seq_avaliacao_preenchimento'::regclass) NOT NULL,
    id_avaliacao_competencia bigint NOT NULL,
    id_graduacao_av1 bigint,
    id_graduacao_av2 bigint,
    id_graduacao_av3 bigint,
    observacao_av3 text,
    concordancia_revisor boolean,
    justificativa_revisor text,
    observacao_av1 text,
    observacao_av2 text
);


--
-- Name: TABLE avaliacao_preenchimento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE avaliacao_preenchimento IS 'Tabela que armazena os as graduações de cada competência para cada avaliado de uma determinada avaliação. Ao criar a avaliação... (exemeplo: 3 competencias + 2 usuários) esta tabela já terá (automaticamente) 6 registros A princípio (no momento da sua criação) esta tabela';


--
-- Name: COLUMN avaliacao_preenchimento.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN avaliacao_preenchimento.id_avaliacao_competencia; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.id_avaliacao_competencia IS 'Identificação da Avaliação x Competência que está sendo preenchida';


--
-- Name: COLUMN avaliacao_preenchimento.id_graduacao_av1; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.id_graduacao_av1 IS 'Graduação da autoavaliação';


--
-- Name: COLUMN avaliacao_preenchimento.id_graduacao_av2; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.id_graduacao_av2 IS 'Graduação da avaliação';


--
-- Name: COLUMN avaliacao_preenchimento.id_graduacao_av3; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.id_graduacao_av3 IS 'Graduação da avaliação de consenso';


--
-- Name: COLUMN avaliacao_preenchimento.observacao_av3; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.observacao_av3 IS 'Observação (no momento da avaliação de consenso)';


--
-- Name: COLUMN avaliacao_preenchimento.concordancia_revisor; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.concordancia_revisor IS 'Campo booleano Indica se o revisor concordou ou não com a graduação que o avaliador atribuiu ao avaliado (na competência estudada)';


--
-- Name: COLUMN avaliacao_preenchimento.justificativa_revisor; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN avaliacao_preenchimento.justificativa_revisor IS 'Campo texto Observação do revisor (no momento da revisão)';


--
-- Name: seq_campanha; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_campanha
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: campanha; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE campanha (
    id bigint DEFAULT nextval('seq_campanha'::regclass) NOT NULL,
    id_empresa bigint NOT NULL,
    id_situacao bigint NOT NULL,
    descricao character varying(100) NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    CONSTRAINT check_situacao CHECK ((id_situacao = ANY (ARRAY[(1)::bigint, (98)::bigint])))
);


--
-- Name: TABLE campanha; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE campanha IS 'Tabela que armazena os dados principais(*) de uma campanha (*) cabeçalho da campanha';


--
-- Name: COLUMN campanha.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN campanha.id_empresa; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.id_empresa IS 'Identificador da empresa à qual aquela campanha se refere';


--
-- Name: COLUMN campanha.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.id_situacao IS 'Situação Ativa ou Inativa';


--
-- Name: COLUMN campanha.descricao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.descricao IS 'Descrição da campanha Ex:  CAMPANHA ANUAL 2015 CAMPANHA 1O. SEMESTRE 2015 CAMPANHA VERÃO 2016   ';


--
-- Name: COLUMN campanha.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.criador IS 'Criador do registro';


--
-- Name: COLUMN campanha.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN campanha.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN campanha.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN campanha.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_cargo; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_cargo
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cargo; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE cargo (
    id bigint DEFAULT nextval('seq_cargo'::regclass) NOT NULL,
    id_empresa bigint NOT NULL,
    id_cargo_pai bigint,
    id_situacao bigint NOT NULL,
    nome character varying(50) NOT NULL,
    id_cargo_cliente character varying(10) NOT NULL,
    gestor boolean,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE cargo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE cargo IS 'Tabela de cargos do sistema  Normalmente esta tabela vem carregada do sistema de RH da empresa contratante';


--
-- Name: COLUMN cargo.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN cargo.id_empresa; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.id_empresa IS 'Identificador da empresa à qual aquele cargo se aplica';


--
-- Name: COLUMN cargo.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN cargo.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.nome IS 'Nome do cargo';


--
-- Name: COLUMN cargo.id_cargo_cliente; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.id_cargo_cliente IS 'Identificação daquele cargo na tabela do RH do cliente Normalmente este campo é utilizado na atualização de dados para possamos achar co-relações entre os sistemas';


--
-- Name: COLUMN cargo.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.criador IS 'Criador do registro';


--
-- Name: COLUMN cargo.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN cargo.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN cargo.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_cargo_compet_det_gabarito; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_cargo_compet_det_gabarito
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cargo_compet_det_gabarito; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE cargo_compet_det_gabarito (
    id bigint DEFAULT nextval('seq_cargo_compet_det_gabarito'::regclass) NOT NULL,
    id_cargo_competencia bigint NOT NULL,
    id_competencia_detalhamento bigint NOT NULL,
    id_graduacao bigint NOT NULL,
    criador character varying(255),
    data_criacao timestamp without time zone,
    atualizador character(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: seq_cargo_competencia; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_cargo_competencia
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cargo_competencia; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE cargo_competencia (
    id bigint DEFAULT nextval('seq_cargo_competencia'::regclass) NOT NULL,
    id_cargo bigint NOT NULL,
    id_competencia bigint NOT NULL,
    id_situacao bigint NOT NULL,
    ordem smallint,
    peso smallint NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE cargo_competencia; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE cargo_competencia IS 'Tabela que se destina a apontar que competências são necessárias para cada cargo';


--
-- Name: COLUMN cargo_competencia.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo_competencia.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN cargo_competencia.id_cargo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo_competencia.id_cargo IS 'Identificador do cargo';


--
-- Name: COLUMN cargo_competencia.id_competencia; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo_competencia.id_competencia IS 'Identificador da competência';


--
-- Name: COLUMN cargo_competencia.peso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN cargo_competencia.peso IS 'Peso desta competência na composição dos funcionários que ocupam aquele cargo Peso é diferente de graduação ideal Graduação ideal é a nota de corte para aquele cargo Peso e o quanto aquela competência tem importância para o meu cargo (dentre as inúmeras competências às quais eu respondo)';


--
-- Name: seq_compet_detalhe_calibrahint; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_compet_detalhe_calibrahint
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;


--
-- Name: compet_detalhe_calibrahint; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE compet_detalhe_calibrahint (
    id bigint DEFAULT nextval('seq_compet_detalhe_calibrahint'::regclass) NOT NULL,
    id_det_competencia bigint NOT NULL,
    id_graduacao bigint NOT NULL,
    hint text NOT NULL
);


--
-- Name: seq_competencia; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_competencia
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competencia; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE competencia (
    id bigint DEFAULT nextval('seq_competencia'::regclass) NOT NULL,
    id_empresa bigint NOT NULL,
    id_situacao bigint NOT NULL,
    descricao_abreviada character varying(50) NOT NULL,
    direcionamento_gestor text,
    direcionamento_nao_gestor text,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    id_modelo_graduacao bigint DEFAULT 2 NOT NULL,
    id_tipo_competencia bigint DEFAULT 1 NOT NULL
);


--
-- Name: TABLE competencia; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE competencia IS 'Tabela que armazena as competências do sistema';


--
-- Name: COLUMN competencia.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN competencia.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN competencia.descricao_abreviada; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.descricao_abreviada IS 'Descrição abreviada da competencia EX:  ENGAJAMENTO TRAHALHO EM EQUIPE COMUNICAÇÃO FLEXIBILIDADE ATITUDE INSPIRADORA';


--
-- Name: COLUMN competencia.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.criador IS 'Criador do registro';


--
-- Name: COLUMN competencia.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN competencia.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN competencia.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_competencia_detalhamento; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_competencia_detalhamento
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competencia_detalhamento; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE competencia_detalhamento (
    id bigint DEFAULT nextval('seq_competencia_detalhamento'::regclass) NOT NULL,
    id_competencia bigint NOT NULL,
    id_situacao bigint NOT NULL,
    ordem smallint,
    descricao text,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE competencia_detalhamento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE competencia_detalhamento IS 'Tabela que armazena os sub-itens (perguntas em si) de uma competência   Ex: ENGAJAMENTO (é a competência)  Detalhamento Demonstra compromisso com os rumos definidos para a empresa através de atitude disciplinada e positiva  Respeita, segue e cumpre e defente o direcionamento, as decisões e os objetivos estabelecidos Garante sincronia dos esforços';


--
-- Name: COLUMN competencia_detalhamento.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia_detalhamento.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN competencia_detalhamento.id_competencia; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia_detalhamento.id_competencia IS 'Identificador da competência';


--
-- Name: COLUMN competencia_detalhamento.descricao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia_detalhamento.descricao IS 'Descrição da competência a ser apresentada ao gestor (questão de semântica)';


--
-- Name: COLUMN competencia_detalhamento.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN competencia_detalhamento.criador IS 'Criador do registro';


--
-- Name: seq_conceito; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_conceito
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: conceito; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE conceito (
    id bigint DEFAULT nextval('seq_conceito'::regclass) NOT NULL,
    id_empresa bigint NOT NULL,
    id_situacao bigint NOT NULL,
    id_tipo_conceito bigint NOT NULL,
    descricao_conceito character varying(5),
    pontuacao_inicial double precision,
    pontuacao_final double precision
);


--
-- Name: seq_configuracao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_configuracao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: configuracao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE configuracao (
    id bigint DEFAULT nextval('seq_configuracao'::regclass) NOT NULL,
    id_funcionario bigint NOT NULL,
    id_empresa_visivel bigint NOT NULL,
    linguagem character(10),
    tamanho_fonte character(10)
);


--
-- Name: TABLE configuracao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE configuracao IS 'Tabela que armazena as configurações para o funcionário.';


--
-- Name: COLUMN configuracao.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN configuracao.id IS 'Identificação do registro.';


--
-- Name: COLUMN configuracao.id_funcionario; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN configuracao.id_funcionario IS 'Identificação do funcionário.';


--
-- Name: COLUMN configuracao.id_empresa_visivel; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN configuracao.id_empresa_visivel IS 'Empresa visivel no sistema, selecionado pelo usuário.';


--
-- Name: COLUMN configuracao.linguagem; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN configuracao.linguagem IS 'Internacionalização selecionada pelo usuário.';


--
-- Name: COLUMN configuracao.tamanho_fonte; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN configuracao.tamanho_fonte IS 'Tamanho da fonte selecionada pelo usuário no sistema.';


--
-- Name: seq_contratante_fasolti; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_contratante_fasolti
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contratante_fasolti; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE contratante_fasolti (
    id bigint DEFAULT nextval('seq_contratante_fasolti'::regclass) NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(40) NOT NULL
);


--
-- Name: TABLE contratante_fasolti; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE contratante_fasolti IS 'Tabela que armazenará os contratantes FASOLTI  Ex:  CONTRATANTE   - EMPRESA SGR           - GLOBO                 CBN                 BEAT 98                 EXCELSIOR                 ELDORADO  LABS          - LABS DOR                 LABORATORIO SERGIO FRANCO                 LABORATORIO A+                 LABORATÓRIO RICHET';


--
-- Name: COLUMN contratante_fasolti.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN contratante_fasolti.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN contratante_fasolti.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN contratante_fasolti.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN contratante_fasolti.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN contratante_fasolti.nome IS 'Nome da empresa contratante';


--
-- Name: seq_curso_especializacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_curso_especializacao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: curso_especializacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE curso_especializacao (
    id bigint DEFAULT nextval('seq_curso_especializacao'::regclass) NOT NULL,
    id_contratante_fasolti bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(80) NOT NULL,
    preenche_outros_cursos boolean NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE curso_especializacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE curso_especializacao IS 'Tabela que armazena os cursos de especialização que o funcionário poderá apontar em sua formação Esta tabela vem carregada do site VAGAS.COM.BR';


--
-- Name: COLUMN curso_especializacao.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN curso_especializacao.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN curso_especializacao.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.nome IS 'Nome do curso de especialização';


--
-- Name: COLUMN curso_especializacao.preenche_outros_cursos; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.preenche_outros_cursos IS 'Este campo tem por finalidade informar se o campo OUTROS CURSOS existente na tela de preenchimento de formação do funcionário tem ou não que ser preenchido';


--
-- Name: COLUMN curso_especializacao.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.criador IS 'Criador do registro';


--
-- Name: COLUMN curso_especializacao.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN curso_especializacao.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN curso_especializacao.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_especializacao.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_curso_graduacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_curso_graduacao
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: curso_graduacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE curso_graduacao (
    id bigint DEFAULT nextval('seq_curso_graduacao'::regclass) NOT NULL,
    id_contratante_fasolti bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(80) NOT NULL,
    preenche_outros_cursos boolean NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE curso_graduacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE curso_graduacao IS 'Tabela que armazena os cursos de graduação que o funcionário poderá apontar em sua formação Esta tabela vem carregada do site VAGAS.COM.BR';


--
-- Name: COLUMN curso_graduacao.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN curso_graduacao.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN curso_graduacao.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.nome IS 'Nome do curso de graduação';


--
-- Name: COLUMN curso_graduacao.preenche_outros_cursos; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.preenche_outros_cursos IS 'Este campo tem por finalidade informar se o campo OUTROS CURSOS existente na tela de preenchimento de formação do funcionário tem ou não que ser preenchido';


--
-- Name: COLUMN curso_graduacao.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.criador IS 'Criador do registro';


--
-- Name: COLUMN curso_graduacao.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN curso_graduacao.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN curso_graduacao.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN curso_graduacao.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_departamento; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_departamento
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: departamento; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE departamento (
    id bigint DEFAULT nextval('seq_departamento'::regclass) NOT NULL,
    id_empresa bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(50) NOT NULL,
    id_departamento_cliente character varying(10) NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE departamento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE departamento IS 'Tabela de departamentos do sistema  Normalmente esta tabela vem carregada do sistema de RH da empresa contratante';


--
-- Name: COLUMN departamento.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN departamento.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN departamento.id_departamento_cliente; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN departamento.id_departamento_cliente IS 'Identificação daquele departamento na tabela do RH do cliente Normalmente este campo é utilizado na atualização de dados para possamos achar co-relações entre os sistemas';


--
-- Name: COLUMN departamento.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN departamento.criador IS 'Criador do registro';


--
-- Name: COLUMN departamento.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN departamento.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN departamento.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN departamento.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN departamento.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN departamento.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_dimensao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_dimensao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dimensao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE dimensao (
    id bigint DEFAULT nextval('seq_dimensao'::regclass) NOT NULL,
    dimensao character varying(60)
);


--
-- Name: seq_empresa; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_empresa
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: empresa; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE empresa (
    id bigint DEFAULT nextval('seq_empresa'::regclass) NOT NULL,
    id_contratante_fasolti bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(120) NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    foto_nome_arquivo character varying(500),
    tamanho_nome_arquivo integer
);


--
-- Name: TABLE empresa; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE empresa IS 'Tabela que armazenará as empresas dos contratantes FASOLTI  Ex:  CONTRATANTE   - EMPRESA SGR           - GLOBO                 CBN                 BEAT 98                 EXCELSIOR                 ELDORADO  LABS          - LABS DOR                 LABORATORIO SERGIO FRANCO                 LABORATORIO A+                 LABORATÓRIO RICHET';


--
-- Name: COLUMN empresa.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN empresa.id_contratante_fasolti; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.id_contratante_fasolti IS 'Identificador do contratante Fasolti';


--
-- Name: COLUMN empresa.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN empresa.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.nome IS 'Nome da empresa';


--
-- Name: COLUMN empresa.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.criador IS 'Criador do registro';


--
-- Name: COLUMN empresa.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN empresa.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN empresa.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: COLUMN empresa.foto_nome_arquivo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.foto_nome_arquivo IS 'Foto (ou logo) da empresa';


--
-- Name: COLUMN empresa.tamanho_nome_arquivo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN empresa.tamanho_nome_arquivo IS 'Tamanho do arquivo de foto (ou logo) da empresa';


--
-- Name: seq_escolaridade; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_escolaridade
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: escolaridade; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE escolaridade (
    id bigint DEFAULT nextval('seq_escolaridade'::regclass) NOT NULL,
    id_contratante_fasolti bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(35) NOT NULL,
    preenche_tipo_curso boolean NOT NULL,
    preenche_outros_cursos boolean NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE escolaridade; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE escolaridade IS 'Tabela que armazena as escolaridades que o funcionário poderá apontar em sua formação Esta tabela vem carregada do site VAGAS.COM.BR';


--
-- Name: COLUMN escolaridade.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN escolaridade.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN escolaridade.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.nome IS 'Nome (ou descrição) da escolaridade';


--
-- Name: COLUMN escolaridade.preenche_tipo_curso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.preenche_tipo_curso IS 'Este campo tem por finalidade informar se o campo TIPO DE CURSO existente na tela de preenchimento de formação do funcionário tem ou não que ser preenchido';


--
-- Name: COLUMN escolaridade.preenche_outros_cursos; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.preenche_outros_cursos IS 'Este campo tem por finalidade informar se o campo OUTROS CURSOS existente na tela de preenchimento de formação do funcionário tem ou não que ser preenchido';


--
-- Name: COLUMN escolaridade.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.criador IS 'Criador do registro';


--
-- Name: COLUMN escolaridade.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN escolaridade.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN escolaridade.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN escolaridade.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_foto; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_foto
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;


--
-- Name: foto; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE foto (
    id bigint DEFAULT nextval('seq_foto'::regclass) NOT NULL,
    id_funcionario bigint NOT NULL,
    id_tipo bigint NOT NULL,
    base64 text
);


--
-- Name: COLUMN foto.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN foto.id IS 'Identificação do registro.';


--
-- Name: COLUMN foto.id_funcionario; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN foto.id_funcionario IS 'Identificação do funcionário.';


--
-- Name: COLUMN foto.id_tipo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN foto.id_tipo IS 'Tipo da foto do funcionário.';


--
-- Name: COLUMN foto.base64; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN foto.base64 IS 'Caracteres codificada da imagem.';


--
-- Name: seq_funcionario; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_funcionario
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: funcionario; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE funcionario (
    id bigint DEFAULT nextval('seq_funcionario'::regclass) NOT NULL,
    id_empresa bigint NOT NULL,
    id_departamento bigint NOT NULL,
    id_cargo bigint NOT NULL,
    id_tipo_sexo bigint NOT NULL,
    id_tipo_perfil_uso bigint NOT NULL,
    id_avaliador bigint,
    id_situacao_funcional bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(80) NOT NULL,
    matricula character varying(20) NOT NULL,
    senha character varying(255) NOT NULL,
    nascimento date NOT NULL,
    admissao date NOT NULL,
    cargo date NOT NULL,
    primeiro_acesso boolean,
    preenche_formacao_e_idiomas boolean NOT NULL,
    email_funcional character varying(40),
    desligamento date,
    ponto_focal_rh boolean,
    foto_nome_arquivo character varying(500),
    foto_tamanho_arquivo integer,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    id_revisor bigint
);


--
-- Name: COLUMN funcionario.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN funcionario.id_departamento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_departamento IS 'Id do departamento do funcionário';


--
-- Name: COLUMN funcionario.id_cargo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_cargo IS 'Identificador cargo do funcionário';


--
-- Name: COLUMN funcionario.id_tipo_sexo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_tipo_sexo IS 'Identificador do sexo do funcionário';


--
-- Name: COLUMN funcionario.id_tipo_perfil_uso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_tipo_perfil_uso IS 'Identificador do perfil de uso do funcionário (ADM ou Func)';


--
-- Name: COLUMN funcionario.id_avaliador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_avaliador IS 'Identificador do avaliador do funcionário';


--
-- Name: COLUMN funcionario.id_situacao_funcional; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_situacao_funcional IS 'Identificador da situação funcional do funcionário';


--
-- Name: COLUMN funcionario.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN funcionario.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.nome IS 'Nome do funcionário';


--
-- Name: COLUMN funcionario.matricula; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.matricula IS 'Matrícula funcional';


--
-- Name: COLUMN funcionario.senha; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.senha IS 'Senha';


--
-- Name: COLUMN funcionario.nascimento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.nascimento IS 'Data de nascimento';


--
-- Name: COLUMN funcionario.admissao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.admissao IS 'Data de admissão';


--
-- Name: COLUMN funcionario.cargo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.cargo IS 'Data do cargo';


--
-- Name: COLUMN funcionario.primeiro_acesso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.primeiro_acesso IS 'Identificador se o funcionário já realizou ou não o primeiro acesso';


--
-- Name: COLUMN funcionario.preenche_formacao_e_idiomas; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.preenche_formacao_e_idiomas IS 'Identificador se o funcionário já preencheu ou não sua formação e idiomas';


--
-- Name: COLUMN funcionario.email_funcional; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.email_funcional IS 'Email funcional';


--
-- Name: COLUMN funcionario.desligamento; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.desligamento IS 'Data de desligamento';


--
-- Name: COLUMN funcionario.foto_nome_arquivo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.foto_nome_arquivo IS 'Foto (ou logo) da empresa';


--
-- Name: COLUMN funcionario.foto_tamanho_arquivo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.foto_tamanho_arquivo IS 'Tamanho do arquivo de foto (ou logo) da empresa';


--
-- Name: COLUMN funcionario.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.criador IS 'Criador do registro';


--
-- Name: COLUMN funcionario.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN funcionario.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN funcionario.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_funcionario_empresa; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_funcionario_empresa
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;


--
-- Name: funcionario_empresa; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE funcionario_empresa (
    id bigint DEFAULT nextval('seq_funcionario_empresa'::regclass) NOT NULL,
    id_funcionario bigint,
    id_empresa bigint
);


--
-- Name: TABLE funcionario_empresa; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE funcionario_empresa IS 'Tabela que relaciona o funcionário e as empresas que ele pode ver.';


--
-- Name: COLUMN funcionario_empresa.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_empresa.id IS 'O identificador da tabela';


--
-- Name: COLUMN funcionario_empresa.id_funcionario; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_empresa.id_funcionario IS 'O identificador do funcionário.';


--
-- Name: COLUMN funcionario_empresa.id_empresa; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_empresa.id_empresa IS 'O identificador da empresa.';


--
-- Name: seq_funcionario_formacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_funcionario_formacao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: funcionario_formacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE funcionario_formacao (
    id bigint DEFAULT nextval('seq_funcionario_formacao'::regclass) NOT NULL,
    id_funcionario bigint NOT NULL,
    id_escolaridade bigint,
    id_tipo_curso bigint,
    id_curso_graduacao bigint,
    id_curso_especializacao bigint,
    id_situacao_conclusao bigint,
    outro_curso character varying(100),
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE funcionario_formacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE funcionario_formacao IS 'Tabela que armazena a formação acadêmica de um funcionário';


--
-- Name: COLUMN funcionario_formacao.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN funcionario_formacao.id_funcionario; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id_funcionario IS 'Identificador do funcionário';


--
-- Name: COLUMN funcionario_formacao.id_escolaridade; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id_escolaridade IS 'Identificador da escolaridade';


--
-- Name: COLUMN funcionario_formacao.id_tipo_curso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id_tipo_curso IS 'Identificador do tipo de curso';


--
-- Name: COLUMN funcionario_formacao.id_curso_graduacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id_curso_graduacao IS 'Identificador do curso de graduação';


--
-- Name: COLUMN funcionario_formacao.id_curso_especializacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id_curso_especializacao IS 'Identificador do curso de especialização';


--
-- Name: COLUMN funcionario_formacao.id_situacao_conclusao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.id_situacao_conclusao IS 'Identificador da situação daquela formação apontada pelo usuário (em andamento, suspenso ou concluído)';


--
-- Name: COLUMN funcionario_formacao.outro_curso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.outro_curso IS 'Campo texto (para ser preenchido manualmente) pelo usuário Seu preenchimento depende do flag existente nas tabelas TIPOS DE CURSO, CURSOS_DE GRADUAÇÃO e CURSOS DE ESPECIALIZAÇÃO';


--
-- Name: COLUMN funcionario_formacao.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.criador IS 'Criador do registro';


--
-- Name: COLUMN funcionario_formacao.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN funcionario_formacao.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN funcionario_formacao.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_formacao.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_funcionario_idioma; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_funcionario_idioma
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: funcionario_idioma; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE funcionario_idioma (
    id bigint DEFAULT nextval('seq_funcionario_idioma'::regclass) NOT NULL,
    id_funcionario bigint NOT NULL,
    id_idioma bigint NOT NULL,
    id_situacao_nivel_fala bigint NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    id_situacao_nivel_escrita bigint NOT NULL,
    id_situacao_nivel_leitura bigint NOT NULL
);


--
-- Name: TABLE funcionario_idioma; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE funcionario_idioma IS 'Tabela que armazena os idiomas que um funcionário conhece';


--
-- Name: COLUMN funcionario_idioma.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN funcionario_idioma.id_funcionario; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.id_funcionario IS 'Idenficador do funcionário';


--
-- Name: COLUMN funcionario_idioma.id_idioma; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.id_idioma IS 'Identificador do idioma';


--
-- Name: COLUMN funcionario_idioma.id_situacao_nivel_fala; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.id_situacao_nivel_fala IS 'Identificador do nível de conhecimento daquele funcionário naquele idioma (básico, intermediário ou avançado)';


--
-- Name: COLUMN funcionario_idioma.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.criador IS 'Criador do registro';


--
-- Name: COLUMN funcionario_idioma.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN funcionario_idioma.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN funcionario_idioma.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN funcionario_idioma.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_graduacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_graduacao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: graduacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE graduacao (
    id bigint DEFAULT nextval('seq_graduacao'::regclass) NOT NULL,
    id_modelo_graduacao bigint NOT NULL,
    ordem smallint,
    label character varying(40) NOT NULL,
    hint text,
    ponto smallint,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone,
    id_situacao bigint NOT NULL
);


--
-- Name: seq_idioma; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_idioma
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: idioma; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE idioma (
    id bigint DEFAULT nextval('seq_idioma'::regclass) NOT NULL,
    id_contratante_fasolti bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(15) NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE idioma; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE idioma IS 'Tabela que armazena os idiomas que o funcionário poderá apontar em sua formação Esta tabela vem carregada do site VAGAS.COM.BR';


--
-- Name: COLUMN idioma.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN idioma.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN idioma.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.nome IS 'Nome do idioma em si (Inglês, Francês, Espanhol,...)';


--
-- Name: COLUMN idioma.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.criador IS 'Criador do registro';


--
-- Name: COLUMN idioma.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN idioma.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN idioma.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN idioma.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_mensagem; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_mensagem
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: mensagem; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE mensagem (
    id bigint DEFAULT nextval('seq_mensagem'::regclass) NOT NULL,
    id_tipo_mensagem bigint NOT NULL,
    mensagem character varying(200) NOT NULL,
    icone character varying(500),
    som character varying(500),
    criador character varying(255),
    data_criacao timestamp without time zone,
    atualizador character varying(40),
    data_atualizacao character(40)
);


--
-- Name: TABLE mensagem; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE mensagem IS 'Esta tabela se propõe a armazenar todas as mensagens que o sistema deverá colocar em tela (alertas, erros, ...) Desta forma... não precisaríamos recompilar um programa para poder mudar uma mensagem, bastanto assim alterar no banco de dados. A intenção é que (quando necessário)... o sistema irá apenas chamar a rotina que apresenta uma mensagem e vai passar um parâmetro para esta rotina Assim... a rotina pesquisa na base de dados de mensagem e opera segundo as configurações da mesma.';


--
-- Name: COLUMN mensagem.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.id IS 'Identificador da tabela (chave primária)';


--
-- Name: COLUMN mensagem.id_tipo_mensagem; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.id_tipo_mensagem IS 'Identificador de tipo de mensagem  Ex: Erro Atenção';


--
-- Name: COLUMN mensagem.mensagem; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.mensagem IS 'Mensagem textual a ser exibida';


--
-- Name: COLUMN mensagem.icone; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.icone IS 'Ícone a ser exibido com a mensagem';


--
-- Name: COLUMN mensagem.som; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.som IS 'Arquivo de som som a ser tocado em caso de uso da mensagem';


--
-- Name: COLUMN mensagem.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.criador IS 'Criador do registro';


--
-- Name: COLUMN mensagem.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN mensagem.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN mensagem.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensagem.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: seq_mensageria; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_mensageria
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;


--
-- Name: mensageria; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE mensageria (
    id bigint DEFAULT nextval('seq_mensageria'::regclass) NOT NULL,
    id_envio bigint NOT NULL,
    id_destino bigint NOT NULL,
    titulo character varying(255) NOT NULL,
    id_avaliacao bigint,
    data_hora_envio timestamp without time zone NOT NULL,
    data_hora_abertura timestamp without time zone,
    id_original bigint,
    texto text NOT NULL
);


--
-- Name: COLUMN mensageria.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensageria.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN mensageria.id_envio; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensageria.id_envio IS 'Idenfificador do usuário (remente)';


--
-- Name: COLUMN mensageria.id_destino; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensageria.id_destino IS 'Idenfificador do usuário (destinatário)';


--
-- Name: COLUMN mensageria.id_avaliacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensageria.id_avaliacao IS 'Idenfificador da avaliação à qual a mensagem se refere (se houver)';


--
-- Name: COLUMN mensageria.data_hora_envio; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensageria.data_hora_envio IS 'Data e hora de envio';


--
-- Name: COLUMN mensageria.data_hora_abertura; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN mensageria.data_hora_abertura IS 'Data e hora de abertura (recebimento)';


--
-- Name: seq_modelo_graduacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_modelo_graduacao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: modelo_graduacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE modelo_graduacao (
    id bigint DEFAULT nextval('seq_modelo_graduacao'::regclass) NOT NULL,
    id_dimensao bigint NOT NULL,
    id_empresa bigint NOT NULL,
    id_situacao bigint NOT NULL,
    descricao_modelo character varying(40),
    qtd_graduacoes smallint,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: parametros; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE parametros (
    chave character varying(200) NOT NULL,
    valor character varying(400) NOT NULL
);


--
-- Name: TABLE parametros; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE parametros IS 'Tabela Interna FASOLTI Armazena uma série de parãmetros operacionais';


--
-- Name: COLUMN parametros.chave; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN parametros.chave IS 'Identificador da tabela (chave primária)';


--
-- Name: seq_avaliacao_preench; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_avaliacao_preench
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_situacao; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_situacao
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_tipo; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_tipo
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_tipo_curso; Type: SEQUENCE; Schema: public; Owner: avduser
--

CREATE SEQUENCE seq_tipo_curso
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: situacao; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE situacao (
    id bigint DEFAULT nextval('seq_situacao'::regclass) NOT NULL,
    nome_curto character varying(20) NOT NULL,
    descricao character varying(50) NOT NULL,
    chave character varying(10) NOT NULL,
    permissao_editar integer NOT NULL,
    permissao_suspender integer NOT NULL,
    permissao_reativar integer NOT NULL,
    permissao_cancelar integer NOT NULL,
    ordem smallint NOT NULL,
    tabela character varying(100) NOT NULL,
    sub_tipo character varying(100)
);


--
-- Name: TABLE situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE situacao IS 'Esta tabela se propõe a controlar as diversas ações do sistema dependendo da situação de cada registro. Assim... podemos manter (e até mesmo montar) as opções de tela de acordo com o status do registro.';


--
-- Name: COLUMN situacao.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.id IS 'Identificador da tabela (chave primária)';


--
-- Name: COLUMN situacao.nome_curto; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.nome_curto IS 'Nome que será exposto em tela (exposição ao usuário)';


--
-- Name: COLUMN situacao.descricao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.descricao IS 'Descrição (pode ser utilizado internamente FASOLTI ou ainda como TIP ao usuário)';


--
-- Name: COLUMN situacao.chave; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.chave IS 'Identificação FASOLTI ';


--
-- Name: COLUMN situacao.permissao_editar; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.permissao_editar IS 'Identificador se o registro que possui esta situação é passível de edição';


--
-- Name: COLUMN situacao.permissao_suspender; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.permissao_suspender IS 'Identificador se o registro que possui esta situação é passível de suspensão';


--
-- Name: COLUMN situacao.permissao_reativar; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.permissao_reativar IS 'Identificador se o registro que possui esta situação é passível de reativação';


--
-- Name: COLUMN situacao.permissao_cancelar; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.permissao_cancelar IS 'Identificador se o registro que possui esta situação é passível de cancelamento';


--
-- Name: COLUMN situacao.ordem; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.ordem IS 'Ordem através da qual as escolhas serão expostas ao usuário na tela Pode demonstrar também (de forma natural) a temporalidade de um fato';


--
-- Name: COLUMN situacao.tabela; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.tabela IS 'Identificador de qual tabela esta situação cadastrada se aplica (utilização interna FASOLTI)';


--
-- Name: COLUMN situacao.sub_tipo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN situacao.sub_tipo IS 'Identificador de qual tipo da tabela esta situação cadastrada se aplica (utilização interna FASOLTI)';


--
-- Name: tipo; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE tipo (
    id bigint DEFAULT nextval('seq_tipo'::regclass) NOT NULL,
    id_situacao bigint NOT NULL,
    nome_curto character varying(30) NOT NULL,
    descricao character varying(100) NOT NULL,
    valor character varying(40),
    ordem smallint NOT NULL,
    tabela character varying(100) NOT NULL,
    sub_tipo character varying(40),
    criador character varying(255),
    data_criacao timestamp without time zone,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE tipo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE tipo IS 'Esta tabela se propõe a armazenar todos os tipos de todas as tabelas do sistema.';


--
-- Name: COLUMN tipo.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.id IS 'Identificador da tabela (chave primária)';


--
-- Name: COLUMN tipo.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.id_situacao IS 'Identificador da situação do tipo   Ex: Um tipo pode ter sido utilizado por um período, mas depois caso seja considerado indesejado (a partir de um determinado momento) é através deste campo que iremos desativá-lo para que não mais apareça ao usuário  Isto possibilita, principalmente, manter uma tela agradável de input, sem prejudicar o histórico de operações (tipos não mais utilizados)';


--
-- Name: COLUMN tipo.nome_curto; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.nome_curto IS 'Nome que será exposto em tela (exposição ao usuário)';


--
-- Name: COLUMN tipo.descricao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.descricao IS 'Descrição (pode ser utilizado internamente FASOLTI ou ainda como TIP ao usuário)';


--
-- Name: COLUMN tipo.valor; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.valor IS 'Identificação FASOLTI ';


--
-- Name: COLUMN tipo.ordem; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.ordem IS 'Ordem através da qual as escolhas serão expostas ao usuário na tela Pode demonstrar também (de forma natural) a temporalidade de um fato';


--
-- Name: COLUMN tipo.tabela; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.tabela IS 'Identificador de qual tabela esta situação cadastrada se aplica (utilização interna FASOLTI)';


--
-- Name: COLUMN tipo.sub_tipo; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.sub_tipo IS 'Identificador de qual tipo da tabela esta situação cadastrada se aplica (utilização interna FASOLTI)';


--
-- Name: COLUMN tipo.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.criador IS 'Criador do registro';


--
-- Name: COLUMN tipo.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN tipo.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN tipo.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: tipo_curso; Type: TABLE; Schema: public; Owner: avduser
--

CREATE TABLE tipo_curso (
    id bigint DEFAULT nextval('seq_tipo_curso'::regclass) NOT NULL,
    id_contratante_fasolti bigint NOT NULL,
    id_situacao bigint NOT NULL,
    nome character varying(35) NOT NULL,
    preenche_graduacao boolean NOT NULL,
    preenche_especializacao boolean NOT NULL,
    criador character varying(255) NOT NULL,
    data_criacao timestamp without time zone NOT NULL,
    atualizador character varying(255),
    data_atualizacao timestamp without time zone
);


--
-- Name: TABLE tipo_curso; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON TABLE tipo_curso IS 'Tabela que armazena os tipos de cursos que o funcionário poderá apontar em sua formação Esta tabela vem carregada do site VAGAS.COM.BR';


--
-- Name: COLUMN tipo_curso.id; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.id IS 'Identificador primário da tabela';


--
-- Name: COLUMN tipo_curso.id_situacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.id_situacao IS 'Identificador da situação daquele registro (ativo ou inativo)';


--
-- Name: COLUMN tipo_curso.nome; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.nome IS 'Nome (ou descrição) do tipo de curso  GRADUAÇÃO POS-GRADUAÇÃO MBA MESTRADO DOUTORADO';


--
-- Name: COLUMN tipo_curso.preenche_graduacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.preenche_graduacao IS 'Este campo tem por finalidade informar se o campo CURSOS DE GRADUAÇÃO existente na tela de preenchimento de formação do funcionário tem ou não que ser preenchido';


--
-- Name: COLUMN tipo_curso.preenche_especializacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.preenche_especializacao IS 'Este campo tem por finalidade informar se o campo CURSOS DE ESPECIALIZAÇÃO existente na tela de preenchimento de formação do funcionário tem ou não que ser preenchido';


--
-- Name: COLUMN tipo_curso.criador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.criador IS 'Criador do registro';


--
-- Name: COLUMN tipo_curso.data_criacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.data_criacao IS 'Data de criação do registro';


--
-- Name: COLUMN tipo_curso.atualizador; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.atualizador IS 'Último atualizador do registro';


--
-- Name: COLUMN tipo_curso.data_atualizacao; Type: COMMENT; Schema: public; Owner: avduser
--

COMMENT ON COLUMN tipo_curso.data_atualizacao IS 'Última data de atualização do registro';


--
-- Name: PK_FUNCIONARIO_EMPRESA; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_empresa
    ADD CONSTRAINT "PK_FUNCIONARIO_EMPRESA" PRIMARY KEY (id);


--
-- Name: PK_configuracao; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY configuracao
    ADD CONSTRAINT "PK_configuracao" PRIMARY KEY (id);


--
-- Name: avaliacao_participantes_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_participante
    ADD CONSTRAINT avaliacao_participantes_pkey PRIMARY KEY (id);


--
-- Name: avaliacao_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao
    ADD CONSTRAINT avaliacao_pkey PRIMARY KEY (id);


--
-- Name: avaliacao_preench_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_preenchimento
    ADD CONSTRAINT avaliacao_preench_pkey PRIMARY KEY (id);


--
-- Name: campanha_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY campanha
    ADD CONSTRAINT campanha_pkey PRIMARY KEY (id);


--
-- Name: cargo_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo
    ADD CONSTRAINT cargo_pkey PRIMARY KEY (id);


--
-- Name: competencia_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia
    ADD CONSTRAINT competencia_pkey PRIMARY KEY (id);


--
-- Name: contratante_fasolti_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY contratante_fasolti
    ADD CONSTRAINT contratante_fasolti_pkey PRIMARY KEY (id);


--
-- Name: curso_especializacao_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY curso_especializacao
    ADD CONSTRAINT curso_especializacao_pkey PRIMARY KEY (id);


--
-- Name: curso_graduacao_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY curso_graduacao
    ADD CONSTRAINT curso_graduacao_pkey PRIMARY KEY (id);


--
-- Name: departamento_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY departamento
    ADD CONSTRAINT departamento_pkey PRIMARY KEY (id);


--
-- Name: empresa_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT empresa_pkey PRIMARY KEY (id);


--
-- Name: escolaridade_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY escolaridade
    ADD CONSTRAINT escolaridade_pkey PRIMARY KEY (id);


--
-- Name: funcionario_formacao_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT funcionario_formacao_pkey PRIMARY KEY (id);


--
-- Name: funcionario_idioma_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_idioma
    ADD CONSTRAINT funcionario_idioma_pkey PRIMARY KEY (id);


--
-- Name: idioma_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY idioma
    ADD CONSTRAINT idioma_pkey PRIMARY KEY (id);


--
-- Name: mensagem_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY mensagem
    ADD CONSTRAINT mensagem_pkey PRIMARY KEY (id);


--
-- Name: modelo_graduacao_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY modelo_graduacao
    ADD CONSTRAINT modelo_graduacao_pkey PRIMARY KEY (id);


--
-- Name: pk_apontamento_funcional; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY apontamento_funcional
    ADD CONSTRAINT pk_apontamento_funcional PRIMARY KEY (id);


--
-- Name: pk_avaliacao_competencias; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_competencia
    ADD CONSTRAINT pk_avaliacao_competencias PRIMARY KEY (id);


--
-- Name: pk_cadpsen_1; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT pk_cadpsen_1 PRIMARY KEY (id);


--
-- Name: pk_cargo_compet_det_gabarito; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_compet_det_gabarito
    ADD CONSTRAINT pk_cargo_compet_det_gabarito PRIMARY KEY (id);


--
-- Name: pk_cargo_competencia; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_competencia
    ADD CONSTRAINT pk_cargo_competencia PRIMARY KEY (id);


--
-- Name: pk_competencia_detalhamento; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia_detalhamento
    ADD CONSTRAINT pk_competencia_detalhamento PRIMARY KEY (id);


--
-- Name: pk_conceito; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY conceito
    ADD CONSTRAINT pk_conceito PRIMARY KEY (id);


--
-- Name: pk_dimensao; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY dimensao
    ADD CONSTRAINT pk_dimensao PRIMARY KEY (id);


--
-- Name: pk_foto; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY foto
    ADD CONSTRAINT pk_foto PRIMARY KEY (id);


--
-- Name: pk_graduacao; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY graduacao
    ADD CONSTRAINT pk_graduacao PRIMARY KEY (id);


--
-- Name: pk_id_detalhe_compet_hint; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY compet_detalhe_calibrahint
    ADD CONSTRAINT pk_id_detalhe_compet_hint PRIMARY KEY (id);


--
-- Name: pk_mensageria; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY mensageria
    ADD CONSTRAINT pk_mensageria PRIMARY KEY (id);


--
-- Name: pk_parametros; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY parametros
    ADD CONSTRAINT pk_parametros PRIMARY KEY (chave);


--
-- Name: pk_situacao; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY situacao
    ADD CONSTRAINT pk_situacao PRIMARY KEY (id);


--
-- Name: pk_tipo; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY tipo
    ADD CONSTRAINT pk_tipo PRIMARY KEY (id);


--
-- Name: tipo_curso_pkey; Type: CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY tipo_curso
    ADD CONSTRAINT tipo_curso_pkey PRIMARY KEY (id);


--
-- Name: ix_cargo_competencia; Type: INDEX; Schema: public; Owner: avduser
--

CREATE INDEX ix_cargo_competencia ON cargo_compet_det_gabarito USING btree (id_cargo_competencia);


--
-- Name: ix_competencia_detalhamento; Type: INDEX; Schema: public; Owner: avduser
--

CREATE INDEX ix_competencia_detalhamento ON cargo_compet_det_gabarito USING btree (id_competencia_detalhamento);


--
-- Name: uq_cargo_competencia_competencia_detalhamento; Type: INDEX; Schema: public; Owner: avduser
--

CREATE UNIQUE INDEX uq_cargo_competencia_competencia_detalhamento ON cargo_compet_det_gabarito USING btree (id_cargo_competencia, id_competencia_detalhamento);


--
-- Name: FK_AVALIACAO_MENSAGERIA; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY mensageria
    ADD CONSTRAINT "FK_AVALIACAO_MENSAGERIA" FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id);


--
-- Name: FK_EMPRESA_FUNCIONARIO; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_empresa
    ADD CONSTRAINT "FK_EMPRESA_FUNCIONARIO" FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: FK_FUNCIONARIO_EMPRESA; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_empresa
    ADD CONSTRAINT "FK_FUNCIONARIO_EMPRESA" FOREIGN KEY (id_funcionario) REFERENCES funcionario(id);


--
-- Name: FK_empresa_visivel_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY configuracao
    ADD CONSTRAINT "FK_empresa_visivel_funcionario" FOREIGN KEY (id_empresa_visivel) REFERENCES empresa(id);


--
-- Name: FK_funcionario_configuracao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY configuracao
    ADD CONSTRAINT "FK_funcionario_configuracao" FOREIGN KEY (id_funcionario) REFERENCES funcionario(id);


--
-- Name: fk_avaliacao_avaliacao_participantes; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_participante
    ADD CONSTRAINT fk_avaliacao_avaliacao_participantes FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id);


--
-- Name: fk_avaliacao_competencias_avaliacao_preench; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_preenchimento
    ADD CONSTRAINT fk_avaliacao_competencias_avaliacao_preench FOREIGN KEY (id_avaliacao_competencia) REFERENCES avaliacao_competencia(id);


--
-- Name: fk_avaliacao_participantes_avaliacao_competencias; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_competencia
    ADD CONSTRAINT fk_avaliacao_participantes_avaliacao_competencias FOREIGN KEY (id_avaliacao_participante) REFERENCES avaliacao_participante(id);


--
-- Name: fk_campanha_avaliacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao
    ADD CONSTRAINT fk_campanha_avaliacao FOREIGN KEY (id_campanha) REFERENCES campanha(id);


--
-- Name: fk_cargo_cargo; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo
    ADD CONSTRAINT fk_cargo_cargo FOREIGN KEY (id_cargo_pai) REFERENCES cargo(id);


--
-- Name: fk_cargo_cargo_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_competencia
    ADD CONSTRAINT fk_cargo_cargo_competencia FOREIGN KEY (id_cargo) REFERENCES cargo(id);


--
-- Name: fk_cargo_competencia_cargo_compet_det_gabarito; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_compet_det_gabarito
    ADD CONSTRAINT fk_cargo_competencia_cargo_compet_det_gabarito FOREIGN KEY (id_cargo_competencia) REFERENCES cargo_competencia(id);


--
-- Name: fk_cargo_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_cargo_funcionario FOREIGN KEY (id_cargo) REFERENCES cargo(id);


--
-- Name: fk_competencia_cargo_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_competencia
    ADD CONSTRAINT fk_competencia_cargo_competencia FOREIGN KEY (id_competencia) REFERENCES competencia(id);


--
-- Name: fk_competencia_competencia_detalhamento; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia_detalhamento
    ADD CONSTRAINT fk_competencia_competencia_detalhamento FOREIGN KEY (id_competencia) REFERENCES competencia(id);


--
-- Name: fk_competencia_detalhamento_avaliacao_competencias; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_competencia
    ADD CONSTRAINT fk_competencia_detalhamento_avaliacao_competencias FOREIGN KEY (id_competencia_detalhamento) REFERENCES competencia_detalhamento(id);


--
-- Name: fk_competencia_detalhamento_cargo_compet_det_gabarito; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_compet_det_gabarito
    ADD CONSTRAINT fk_competencia_detalhamento_cargo_compet_det_gabarito FOREIGN KEY (id_competencia_detalhamento) REFERENCES competencia_detalhamento(id);


--
-- Name: fk_contratante_fasolti_curso_especializacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY curso_especializacao
    ADD CONSTRAINT fk_contratante_fasolti_curso_especializacao FOREIGN KEY (id_contratante_fasolti) REFERENCES contratante_fasolti(id);


--
-- Name: fk_contratante_fasolti_curso_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY curso_graduacao
    ADD CONSTRAINT fk_contratante_fasolti_curso_graduacao FOREIGN KEY (id_contratante_fasolti) REFERENCES contratante_fasolti(id);


--
-- Name: fk_contratante_fasolti_empresa; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT fk_contratante_fasolti_empresa FOREIGN KEY (id_contratante_fasolti) REFERENCES contratante_fasolti(id);


--
-- Name: fk_contratante_fasolti_escolaridade; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY escolaridade
    ADD CONSTRAINT fk_contratante_fasolti_escolaridade FOREIGN KEY (id_contratante_fasolti) REFERENCES contratante_fasolti(id);


--
-- Name: fk_contratante_fasolti_idioma; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY idioma
    ADD CONSTRAINT fk_contratante_fasolti_idioma FOREIGN KEY (id_contratante_fasolti) REFERENCES contratante_fasolti(id);


--
-- Name: fk_contratante_fasolti_tipo_curso; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY tipo_curso
    ADD CONSTRAINT fk_contratante_fasolti_tipo_curso FOREIGN KEY (id_contratante_fasolti) REFERENCES contratante_fasolti(id);


--
-- Name: fk_curso_especializacao_funcionario_formacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT fk_curso_especializacao_funcionario_formacao FOREIGN KEY (id_curso_especializacao) REFERENCES curso_especializacao(id);


--
-- Name: fk_curso_graduacao_funcionario_formacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT fk_curso_graduacao_funcionario_formacao FOREIGN KEY (id_curso_graduacao) REFERENCES curso_graduacao(id);


--
-- Name: fk_departamento_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_departamento_funcionario FOREIGN KEY (id_departamento) REFERENCES departamento(id);


--
-- Name: fk_detalhe_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY compet_detalhe_calibrahint
    ADD CONSTRAINT fk_detalhe_competencia FOREIGN KEY (id_det_competencia) REFERENCES competencia_detalhamento(id);


--
-- Name: fk_dimensao_modelo_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY modelo_graduacao
    ADD CONSTRAINT fk_dimensao_modelo_graduacao FOREIGN KEY (id_dimensao) REFERENCES dimensao(id);


--
-- Name: fk_empresa_campanha; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY campanha
    ADD CONSTRAINT fk_empresa_campanha FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_empresa_cargo; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo
    ADD CONSTRAINT fk_empresa_cargo FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_empresa_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia
    ADD CONSTRAINT fk_empresa_competencia FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_empresa_conceito; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY conceito
    ADD CONSTRAINT fk_empresa_conceito FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_empresa_departamento; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY departamento
    ADD CONSTRAINT fk_empresa_departamento FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_empresa_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_empresa_funcionario FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_empresa_modelo_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY modelo_graduacao
    ADD CONSTRAINT fk_empresa_modelo_graduacao FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- Name: fk_escolaridade_funcionario_formacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT fk_escolaridade_funcionario_formacao FOREIGN KEY (id_escolaridade) REFERENCES escolaridade(id);


--
-- Name: fk_funcionario_apontamento_funcional_avaliado; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY apontamento_funcional
    ADD CONSTRAINT fk_funcionario_apontamento_funcional_avaliado FOREIGN KEY (id_avaliado) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_apontamento_funcional_avaliador; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY apontamento_funcional
    ADD CONSTRAINT fk_funcionario_apontamento_funcional_avaliador FOREIGN KEY (id_avaliador) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_avaliacao_participantes; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_participante
    ADD CONSTRAINT fk_funcionario_avaliacao_participantes FOREIGN KEY (id_revisor) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_avaliacao_participantes_avaliado; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_participante
    ADD CONSTRAINT fk_funcionario_avaliacao_participantes_avaliado FOREIGN KEY (id_avaliado) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_avaliacao_participantes_avaliador; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_participante
    ADD CONSTRAINT fk_funcionario_avaliacao_participantes_avaliador FOREIGN KEY (id_avaliador) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_avaliador; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_funcionario_avaliador FOREIGN KEY (id_avaliador) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_foto; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY foto
    ADD CONSTRAINT fk_funcionario_foto FOREIGN KEY (id_funcionario) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_funcionario_formacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT fk_funcionario_funcionario_formacao FOREIGN KEY (id_funcionario) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_funcionario_idioma; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_idioma
    ADD CONSTRAINT fk_funcionario_funcionario_idioma FOREIGN KEY (id_funcionario) REFERENCES funcionario(id);


--
-- Name: fk_funcionario_revisor; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_funcionario_revisor FOREIGN KEY (id_revisor) REFERENCES funcionario(id);


--
-- Name: fk_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY compet_detalhe_calibrahint
    ADD CONSTRAINT fk_graduacao FOREIGN KEY (id_graduacao) REFERENCES graduacao(id);


--
-- Name: fk_graduacao_avaliacao_preench_grad_av1; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_preenchimento
    ADD CONSTRAINT fk_graduacao_avaliacao_preench_grad_av1 FOREIGN KEY (id_graduacao_av1) REFERENCES graduacao(id);


--
-- Name: fk_graduacao_avaliacao_preench_grad_av2; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_preenchimento
    ADD CONSTRAINT fk_graduacao_avaliacao_preench_grad_av2 FOREIGN KEY (id_graduacao_av2) REFERENCES graduacao(id);


--
-- Name: fk_graduacao_avaliacao_preench_grad_av3; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY avaliacao_preenchimento
    ADD CONSTRAINT fk_graduacao_avaliacao_preench_grad_av3 FOREIGN KEY (id_graduacao_av3) REFERENCES graduacao(id);


--
-- Name: fk_graduacao_cargo_compet_det_gabarito; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_compet_det_gabarito
    ADD CONSTRAINT fk_graduacao_cargo_compet_det_gabarito FOREIGN KEY (id_graduacao) REFERENCES graduacao(id);


--
-- Name: fk_idioma_funcionario_idioma; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_idioma
    ADD CONSTRAINT fk_idioma_funcionario_idioma FOREIGN KEY (id_idioma) REFERENCES idioma(id);


--
-- Name: fk_modelo_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia
    ADD CONSTRAINT fk_modelo_graduacao FOREIGN KEY (id_modelo_graduacao) REFERENCES modelo_graduacao(id);


--
-- Name: fk_modelo_graduacao_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY graduacao
    ADD CONSTRAINT fk_modelo_graduacao_graduacao FOREIGN KEY (id_modelo_graduacao) REFERENCES modelo_graduacao(id);


--
-- Name: fk_original; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY mensageria
    ADD CONSTRAINT fk_original FOREIGN KEY (id) REFERENCES mensageria(id);


--
-- Name: fk_situacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY graduacao
    ADD CONSTRAINT fk_situacao FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_cargo; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo
    ADD CONSTRAINT fk_situacao_cargo FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_cargo_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY cargo_competencia
    ADD CONSTRAINT fk_situacao_cargo_competencia FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia
    ADD CONSTRAINT fk_situacao_competencia FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_competencia_detalhamento; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia_detalhamento
    ADD CONSTRAINT fk_situacao_competencia_detalhamento FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_conceito; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY conceito
    ADD CONSTRAINT fk_situacao_conceito FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_contratante_fasolti; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY contratante_fasolti
    ADD CONSTRAINT fk_situacao_contratante_fasolti FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_curso_especializacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY curso_especializacao
    ADD CONSTRAINT fk_situacao_curso_especializacao FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_curso_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY curso_graduacao
    ADD CONSTRAINT fk_situacao_curso_graduacao FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_departamento; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY departamento
    ADD CONSTRAINT fk_situacao_departamento FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_empresa; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT fk_situacao_empresa FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_escolaridade; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY escolaridade
    ADD CONSTRAINT fk_situacao_escolaridade FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_funcionario_formacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT fk_situacao_funcionario_formacao FOREIGN KEY (id_situacao_conclusao) REFERENCES situacao(id);


--
-- Name: fk_situacao_funcionario_idioma; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_idioma
    ADD CONSTRAINT fk_situacao_funcionario_idioma FOREIGN KEY (id_situacao_nivel_fala) REFERENCES situacao(id);


--
-- Name: fk_situacao_funcionario_situacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_situacao_funcionario_situacao FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_funcionario_situacao_funcional; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_situacao_funcionario_situacao_funcional FOREIGN KEY (id_situacao_funcional) REFERENCES situacao(id);


--
-- Name: fk_situacao_idioma; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY idioma
    ADD CONSTRAINT fk_situacao_idioma FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_modelo_graduacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY modelo_graduacao
    ADD CONSTRAINT fk_situacao_modelo_graduacao FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_situacao_nivel_escrita; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_idioma
    ADD CONSTRAINT fk_situacao_nivel_escrita FOREIGN KEY (id_situacao_nivel_escrita) REFERENCES situacao(id);


--
-- Name: fk_situacao_nivel_leitura; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_idioma
    ADD CONSTRAINT fk_situacao_nivel_leitura FOREIGN KEY (id_situacao_nivel_leitura) REFERENCES situacao(id);


--
-- Name: fk_situacao_tipo_curso; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY tipo_curso
    ADD CONSTRAINT fk_situacao_tipo_curso FOREIGN KEY (id_situacao) REFERENCES situacao(id);


--
-- Name: fk_tipo_competencia; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY competencia
    ADD CONSTRAINT fk_tipo_competencia FOREIGN KEY (id_tipo_competencia) REFERENCES tipo(id);


--
-- Name: fk_tipo_conceito; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY conceito
    ADD CONSTRAINT fk_tipo_conceito FOREIGN KEY (id_tipo_conceito) REFERENCES tipo(id);


--
-- Name: fk_tipo_curso_funcionario_formacao; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario_formacao
    ADD CONSTRAINT fk_tipo_curso_funcionario_formacao FOREIGN KEY (id_tipo_curso) REFERENCES tipo_curso(id);


--
-- Name: fk_tipo_foto; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY foto
    ADD CONSTRAINT fk_tipo_foto FOREIGN KEY (id_tipo) REFERENCES tipo(id);


--
-- Name: fk_tipo_funcionario_perfil_uso; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_tipo_funcionario_perfil_uso FOREIGN KEY (id_tipo_perfil_uso) REFERENCES tipo(id);


--
-- Name: fk_tipo_funcionario_sexo; Type: FK CONSTRAINT; Schema: public; Owner: avduser
--

ALTER TABLE ONLY funcionario
    ADD CONSTRAINT fk_tipo_funcionario_sexo FOREIGN KEY (id_tipo_sexo) REFERENCES tipo(id);


--
-- Data for Name: parametros; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO parametros VALUES ('notificacao.email.funcionario.esqueci.senha', 'true');
INSERT INTO parametros VALUES ('notificacao.email.avaliacao.nova.mensagem', 'true');
INSERT INTO parametros VALUES ('notificacao.sms.funcionario.troca.senha', 'true');
INSERT INTO parametros VALUES ('notificacao.sms.funcionario.novo', 'true');
INSERT INTO parametros VALUES ('notificacao.sms.funcionario.esqueci.senha', 'true');
INSERT INTO parametros VALUES ('notificacao.email.funcionario.novo', 'true');
INSERT INTO parametros VALUES ('avd.url.link.email', 'http://avd.fasolti.com.br/index.html');
INSERT INTO parametros VALUES ('email.contato.suporte', 'suporte_sgi@copony.com.br');
INSERT INTO parametros VALUES ('notificacao.email.funcionario.troca.senha', 'true');


--
-- Data for Name: situacao; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO situacao VALUES (1, 'Ativo', 'Ativo', 'ativo', 0, 0, 0, 0, 1, 'Todas', 'situacao');
INSERT INTO situacao VALUES (2, 'Inativo', 'Inativo', 'inativo', 0, 0, 0, 0, 2, 'Todas', 'situacao');
INSERT INTO situacao VALUES (3, 'Em atividade', 'Em atividade', 'atividade', 0, 0, 0, 0, 1, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (4, 'Férias', 'Férias', 'ferias', 0, 0, 0, 0, 2, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (5, 'Licença maternidade', 'Licença maternidade', 'lic. mat.', 0, 0, 0, 0, 3, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (6, 'Licença sem remu.', 'Licença sem remuneração', 'lic. s rem', 0, 0, 0, 0, 4, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (7, 'Dispensa médica', 'Dispensa médica', 'disp. med.', 0, 0, 0, 0, 5, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (8, 'Serviço militar obr.', 'Serviço militar obrigatório', 'serv mil', 0, 0, 0, 0, 6, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (17, 'Básico', 'Básico', 'basico', 0, 0, 0, 0, 1, 'funcionario_idioma', 'situacao_nivel');
INSERT INTO situacao VALUES (18, 'Intermediário', 'Intermediário', 'intermed', 0, 0, 0, 0, 2, 'funcionario_idioma', 'situacao_nivel');
INSERT INTO situacao VALUES (19, 'Avançado', 'Avançado', 'avancado', 0, 0, 0, 0, 3, 'funcionario_idioma', 'situacao_nivel');
INSERT INTO situacao VALUES (20, 'Em andamento', 'Em andamento', 'em andamen', 0, 0, 0, 0, 1, 'funcionario_formacao', 'situacao_conclusao');
INSERT INTO situacao VALUES (21, 'Suspenso', 'Suspenso', 'suspenso', 0, 0, 0, 0, 2, 'funcionario_formacao', 'situacao_conclusao');
INSERT INTO situacao VALUES (22, 'Concluído', 'Concluído', 'concluido', 0, 0, 0, 0, 3, 'funcionario_formacao', 'situacao_conclusao');
INSERT INTO situacao VALUES (23, 'Licença paternidade', 'Licença paternidade', 'lit. pat', 0, 0, 0, 0, 7, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (24, 'Luto', 'Luto', 'luto', 0, 0, 0, 0, 8, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (25, 'Núpcias', 'Núpcias', 'nupcias', 0, 0, 0, 0, 9, 'funcionario', 'funcional');
INSERT INTO situacao VALUES (26, 'Licença adoção', 'Licença adoção', 'lic. adoc.', 0, 0, 0, 0, 10, 'funcionario', 'funcional');


--
-- Data for Name: tipo; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO tipo VALUES (1, 1, 'Masc', 'Masculino', 'sexo', 1, 'funcionario', 'sexo', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (2, 1, 'Fem', 'Feminino', 'sexo', 2, 'funcionario', 'sexo', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (3, 1, 'Admin', 'Administrador', 'administrador', 1, 'funcionario', 'perfil', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (4, 1, 'Func', 'Funcionário', 'funcionario', 2, 'funcionario', 'perfil', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (5, 1, 'Organizacional', 'Organizacional', 'org', 1, 'competencia', 'tipo_competencia', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (6, 1, 'Gerencial', 'Gerencial', 'gerenc', 2, 'competencia', 'tipo_competencia', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (7, 1, 'Técnica', 'Técnica', 'tecnica', 3, 'competencia', 'tipo_competencia', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (8, 1, 'Informação', 'Informação', 'info', 1, 'mensagem', 'tipo_mensagem', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (9, 1, 'Atenção', 'Atenção', 'warn', 2, 'mensagem', 'tipo_mensagem', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (10, 1, 'Erro', 'Erro', 'error', 3, 'mensagem', 'tipo_mensagem', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (11, 1, 'Comportamental', 'Comportamental', 'comportament', 4, 'competencia', 'tipo_competencia', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (13, 1, 'foto_perfil', 'Foto perfil', 'foto_perfil', 0, 'foto', 'tipo', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (14, 1, 'Consult RH', 'Consultor de RH', 'consultorRH', 3, 'funcionario', 'perfil', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (15, 1, 'Gestor RH', 'Gestor de RH', 'gestorRH', 4, 'funcionario', 'perfil', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');
INSERT INTO tipo VALUES (16, 1, 'Gestor Área', 'Gestor de Área', 'gestorArea', 5, 'funcionario', 'perfil', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');


--
-- Data for Name: contratante_fasolti; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO contratante_fasolti VALUES (1, 1, 'FASOLTI');


--
-- Data for Name: empresa; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO empresa VALUES (1, 1, 1, 'FASOLTI', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00', NULL, NULL);


--
-- Data for Name: departamento; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO departamento VALUES (1, 1, 1, 'Administração e serviços', '0002', '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');


--
-- Data for Name: cargo; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO cargo VALUES (1, 1, NULL, 1, 'Administrador Sistema', 'ADM', false, '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00');


--
-- Data for Name: funcionario; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO funcionario VALUES (1, 1, 1, 1, 1, 3, NULL, 3, 1, 'Administrador', 'admin', '$2a$10$6DPdvDDyggwO0MbB2JHNguWFKn/0UjneRCRUGWsGz/mBnITdnKLXy', '2016-12-13', '2016-12-12', '2016-12-12', false, false, 'services@fasolti.com.br', NULL, false, NULL, NULL, '1%admin', '2016-06-03 00:00:00', '1%admin', '2016-06-03 00:00:00', NULL);

--
-- Data for Name: configuracao; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO configuracao(id_funcionario, id_empresa_visivel, linguagem, tamanho_fonte) VALUES (1, 1, '', '');

--
-- Data for Name: tipo_curso; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (1, 1, 1, 'Graduação', true, false, 'admin', '2016-06-03');
INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (2, 1, 1, 'Pós-graduação', false, true, 'admin', '2016-06-03');
INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (3, 1, 1, 'MBA', false, true, 'admin', '2016-06-03');
INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (4, 1, 1, 'Mestrado', false, true, 'admin', '2016-06-03');
INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (5, 1, 1, 'Doutorado', false, true, 'admin', '2016-06-03');
INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (6, 1, 1, 'Formação geral', false, false, 'admin', '2016-07-01');
INSERT INTO tipo_curso(id, id_contratante_fasolti, id_situacao, nome, preenche_graduacao, preenche_especializacao, criador, data_criacao) VALUES (7, 1, 1, 'Técnico', false, true, 'admin', '2016-07-08');


--
-- Data for Name: idioma; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (1, 1, 1, 'Inglês', 'system', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (2, 1, 1, 'Italiano', 'admin', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (3, 1, 1, 'Hindi/Bengalês', 'admin', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (4, 1, 1, 'Outro', 'admin', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (5, 1, 1, 'Espanhol', 'system', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (6, 1, 1, 'Português', 'system', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (7, 1, 1, 'Francês', 'admin', '2016-07-08');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (8, 1, 1, 'Alemão', 'admin', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (9, 1, 1, 'Japonês', 'admin', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (10, 1, 2, 'Mandarin', 'admin', '2016-06-03');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (11, 1, 1, 'Mandarim ', '1%000002', '2016-08-08');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (12, 1, 1, 'Mandarim', 'admin', '2016-07-01');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (13, 1, 1, 'Russo', 'admin', '2016-07-08');
INSERT INTO idioma(id, id_contratante_fasolti, id_situacao, nome, criador, data_criacao) VALUES (14, 1, 2, 'Irlandês', 'admin', '2016-07-08');


--
-- Data for Name: escolaridade; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO escolaridade(id, id_contratante_fasolti, id_situacao, nome, preenche_tipo_curso, preenche_outros_cursos, criador, data_criacao) VALUES (1, 1, 1, 'Ensino Superior', true, false, 'admin', '2016-06-03 00:00:00');
INSERT INTO escolaridade(id, id_contratante_fasolti, id_situacao, nome, preenche_tipo_curso, preenche_outros_cursos, criador, data_criacao) VALUES (2, 1, 1, 'Outros', false, true, 'admin', '2016-06-03 00:00:00');
INSERT INTO escolaridade(id, id_contratante_fasolti, id_situacao, nome, preenche_tipo_curso, preenche_outros_cursos, criador, data_criacao) VALUES (3, 1, 1, 'Ensino Médio', false, false, 'admin', '2016-06-03 00:00:00');
INSERT INTO escolaridade(id, id_contratante_fasolti, id_situacao, nome, preenche_tipo_curso, preenche_outros_cursos, criador, data_criacao) VALUES (4, 1, 1, 'Ensino Fundamental', false, false, 'admin', '2016-06-03 00:00:00');
INSERT INTO escolaridade(id, id_contratante_fasolti, id_situacao, nome, preenche_tipo_curso, preenche_outros_cursos, criador, data_criacao) VALUES (5, 1, 2, 'Ensino Médio (profissionalizante)', true, false, 'admin', '2016-06-03 00:00:00');


--
-- Data for Name: curso_graduacao; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO curso_graduacao VALUES (1, 1, 1, 'Administração', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (2, 1, 1, 'Administração com ênfase em comércio exterior', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (3, 1, 1, 'Administração com ênfase em sist. de informação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (4, 1, 1, 'Administração de empresas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (5, 1, 1, 'Administração de produção', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (6, 1, 1, 'Administração de recursos humanos', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (7, 1, 1, 'Administração hospitalar', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (8, 1, 1, 'Administração hoteleira', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (9, 1, 1, 'Administração pública', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (10, 1, 1, 'Administração rural', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (11, 1, 1, 'Agronomia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (12, 1, 1, 'Análise de sistemas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (13, 1, 1, 'Arquitetura e urbanismo', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (14, 1, 1, 'Arquivologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (15, 1, 1, 'Artes cênicas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (16, 1, 1, 'Artes plásticas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (17, 1, 1, 'Astronomia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (18, 1, 1, 'Audio visual', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (19, 1, 1, 'Biblioteconomia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (20, 1, 1, 'Ciência da computação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (21, 1, 1, 'Ciências atuariais', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (22, 1, 1, 'Ciências biológicas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (23, 1, 1, 'Ciências biomédicas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (24, 1, 1, 'Ciências contábeis', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (25, 1, 1, 'Ciências sociais', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (26, 1, 1, 'Cinema e vídeo', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (27, 1, 1, 'Comércio exterior', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (28, 1, 1, 'Comunicação social', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (29, 1, 1, 'Comunicação visual', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (30, 1, 1, 'Dança', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (31, 1, 1, 'Desenho industrial', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (32, 1, 1, 'Design de interiores', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (33, 1, 1, 'Design gráfico', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (34, 1, 1, 'Direito', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (35, 1, 1, 'Direito internacional', true, 'admin', '2016-07-18 14:51:13.677');
INSERT INTO curso_graduacao VALUES (36, 1, 1, 'Ecologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (37, 1, 1, 'Economia / ciências econômicas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (38, 1, 1, 'Economia doméstica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (39, 1, 1, 'Educação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (40, 1, 1, 'Educação física', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (41, 1, 1, 'Enfermagem', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (42, 1, 1, 'Engenharia aeronáutica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (43, 1, 1, 'Engenharia agrícola', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (44, 1, 1, 'Engenharia agronômica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (45, 1, 1, 'Engenharia ambiental', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (46, 1, 1, 'Engenharia aquicultura', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (47, 1, 1, 'Engenharia bioquímica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (48, 1, 1, 'Engenharia cartográfica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (49, 1, 1, 'Engenharia civil', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (50, 1, 1, 'Engenharia da computação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (51, 1, 1, 'Engenharia de agrimensura', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (52, 1, 1, 'Engenharia de alimentos', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (53, 1, 1, 'Engenharia de controle e automação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (54, 1, 1, 'Engenharia de horticultura', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (55, 1, 1, 'Engenharia de materiais', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (56, 1, 1, 'Engenharia de minas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (57, 1, 1, 'Engenharia de pesca', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (58, 1, 1, 'Engenharia de produção', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (59, 1, 1, 'Engenharia de telecomunicações', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (60, 1, 1, 'Engenharia elétrica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (61, 1, 1, 'Engenharia eletrotécnica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (62, 1, 1, 'Engenharia fiscal', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (63, 1, 1, 'Engenharia florestal', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (64, 1, 1, 'Engenharia hídrica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (65, 1, 1, 'Engenharia industrial', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (66, 1, 1, 'Engenharia mecânica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (67, 1, 1, 'Engenharia mecatrônica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (68, 1, 1, 'Engenharia metalúrgica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (69, 1, 1, 'Engenharia naval', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (70, 1, 1, 'Engenharia química', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (71, 1, 1, 'Engenharia sanitária', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (72, 1, 1, 'Engenharia têxtil', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (73, 1, 1, 'Esporte', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (74, 1, 1, 'Estatística', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (75, 1, 1, 'Farmácia e bioquímica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (76, 1, 1, 'Filosofia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (77, 1, 1, 'Fisioterapia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (78, 1, 1, 'Fonoaudiologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (79, 1, 1, 'Fotografia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (80, 1, 1, 'Geofísica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (81, 1, 1, 'Geografia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (82, 1, 1, 'Geologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (83, 1, 1, 'Graduação 1', true, 'admin', '2016-07-01 15:00:23.27');
INSERT INTO curso_graduacao VALUES (84, 1, 1, 'Graduação em Eae', true, 'admin', '2016-07-01 16:24:34.896');
INSERT INTO curso_graduacao VALUES (85, 1, 1, 'História', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (86, 1, 1, 'Hotelaria', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (87, 1, 1, 'Informática', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (88, 1, 1, 'Jornalismo', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (89, 1, 1, 'Letras', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (90, 1, 1, 'Linguística', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (91, 1, 1, 'Matemática', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (92, 1, 1, 'Medicina', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (93, 1, 1, 'Medicina veterinária', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (94, 1, 1, 'Meteorologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (95, 1, 1, 'Microbilogia e/ou imunologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (96, 1, 1, 'Moda', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (97, 1, 1, 'Multimídia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (98, 1, 1, 'Museologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (99, 1, 1, 'Música', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (100, 1, 1, 'Musicoterapia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (101, 1, 1, 'Naturologia aplicada', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (102, 1, 1, 'Nutrição', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (103, 1, 1, 'Oceanografia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (104, 1, 1, 'Odontologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (105, 1, 1, 'Optometria', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (106, 1, 1, 'Outros', true, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (107, 1, 1, 'Pedagogia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (108, 1, 1, 'Processamento de dados', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (109, 1, 1, 'Produção cultural', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (110, 1, 1, 'Produção editorial', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (111, 1, 1, 'Propaganda e marketing', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (112, 1, 1, 'Psicologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (113, 1, 1, 'Psicopedagogia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (114, 1, 1, 'Publicidade e propaganda', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (115, 1, 1, 'Publicidade, propaganda e marketing', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (116, 1, 1, 'Química', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (117, 1, 1, 'Química industrial', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (118, 1, 1, 'Quiropraxia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (119, 1, 1, 'Rádio e tv', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (120, 1, 1, 'Relacões internacionais', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (121, 1, 1, 'Relações púlicas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (122, 1, 1, 'Secretariado executivo', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (123, 1, 1, 'Serviço social', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (124, 1, 1, 'Sistema de informação (análise de sistemas)', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (125, 1, 1, 'Tecnologia de laticínios', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (126, 1, 1, 'Teologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (127, 1, 1, 'Terapia ocupacional', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (128, 1, 1, 'Tradução e interpretação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (129, 1, 1, 'Turismo', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_graduacao VALUES (130, 1, 1, 'Zootecnia', false, 'admin', '2016-06-03 13:45:17.103');

--
-- Data for Name: curso_especializacao; Type: TABLE DATA; Schema: public; Owner: avduser
--

INSERT INTO curso_especializacao VALUES (1, 1, 1, 'Ciências agrárias - agronomia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (2, 1, 1, 'Ciências agrárias - ciência e tecnol. de alimentos', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (3, 1, 1, 'Ciências agrárias - engenharia agrícola', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (4, 1, 1, 'Ciências agrárias - medicina veterinária', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (5, 1, 1, 'Ciências agrárias - recursos e engenharia de pesca', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (6, 1, 1, 'Ciências agrárias - recursos/engenharia florestal', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (7, 1, 1, 'Ciências agrárias - zootecnia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (8, 1, 1, 'Ciências biológicas - biofísica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (9, 1, 1, 'Ciências biológicas - biologia geral', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (10, 1, 1, 'Ciências biológicas - bioquímica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (11, 1, 1, 'Ciências biológicas - botânica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (12, 1, 1, 'Ciências biológicas -  ecologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (13, 1, 1, 'Ciências biológicas - farmacologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (14, 1, 1, 'Ciências biológicas - fisiologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (15, 1, 1, 'Ciências biológicas - genética', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (16, 1, 1, 'Ciências biológicas - imunologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (17, 1, 1, 'Ciências biológicas - microbiologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (18, 1, 1, 'Ciências biológicas - morfologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (19, 1, 1, 'Ciências biológicas - parasitologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (20, 1, 1, 'Ciências biológicas - zoologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (21, 1, 1, 'Ciências da saúde - educação física', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (22, 1, 1, 'Ciências da saúde - enfermagem', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (23, 1, 1, 'Ciências da saúde - farmácia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (24, 1, 1, 'Ciências da saúde - fisioterapia ocupacional', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (25, 1, 1, 'Ciências da saúde - fonoaudiologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (26, 1, 1, 'Ciências da saúde - medicina', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (27, 1, 1, 'Ciências da saúde - nutrição', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (28, 1, 1, 'Ciências da saúde - odontologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (29, 1, 1, 'Ciências da saúde - saúde coletiva', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (30, 1, 1, 'Ciências exatas e da terra - astronomia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (31, 1, 1, 'Ciências exatas e da terra - computação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (32, 1, 1, 'Ciências exatas e da terra - estatística', false, 'admin', '2016-06-17 18:28:41.606');
INSERT INTO curso_especializacao VALUES (33, 1, 1, 'Ciências exatas e da terra - física', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (34, 1, 1, 'Ciências exatas e da terra - geociências', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (35, 1, 1, 'Ciências exatas e da terra - matemática', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (36, 1, 1, 'Ciências exatas e da terra - oceanografia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (37, 1, 1, 'Ciências exatas e da terra - probabilidade', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (38, 1, 1, 'Ciências exatas e da terra - química', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (39, 1, 1, 'Ciências humanas - antropologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (40, 1, 1, 'Ciências humanas - arqueologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (41, 1, 1, 'Ciências humanas - ciências políticas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (42, 1, 1, 'Ciências humanas - educação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (43, 1, 1, 'Ciências humanas - filosofia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (44, 1, 1, 'Ciências humanas - geografia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (45, 1, 1, 'Ciências humanas - história', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (46, 1, 1, 'Ciências humanas - psicologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (47, 1, 1, 'Ciências humanas - sociologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (48, 1, 1, 'Ciências humanas - teologia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (49, 1, 1, 'Ciências sociais e aplicadas - administração', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (50, 1, 1, 'Ciências sociais e aplicadas - arquitetura', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (51, 1, 1, 'Ciências sociais e aplicadas - ciência da informação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (52, 1, 1, 'Ciências sociais e aplicadas - comunicação', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (53, 1, 1, 'Ciências sociais e aplicadas - demografia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (54, 1, 1, 'Ciências sociais e aplicadas - desenho industrial', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (55, 1, 1, 'Ciências sociais e aplicadas - direito', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (56, 1, 1, 'Ciências sociais e aplicadas - economia', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (57, 1, 1, 'Ciências sociais e aplicadas - planejamento urbano e regional', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (58, 1, 1, 'Ciências sociais e aplicadas - serviço social', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (59, 1, 1, 'Ciências sociais e aplicadas - turismo', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (60, 1, 1, 'Ciências sociais e aplicadas - urbanismo', false, 'admin', '2016-06-17 18:32:49.064');
INSERT INTO curso_especializacao VALUES (61, 1, 1, 'Engenharias - engenharia aeroespacial', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (62, 1, 1, 'Engenharias - engenharia biomédica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (63, 1, 1, 'Engenharias - engenharia de materiais e metalúrgica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (64, 1, 1, 'Engenharias - engenharia de minas', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (65, 1, 1, 'Engenharias - engenharia de produção', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (66, 1, 1, 'Engenharias - engenharia de transporte', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (67, 1, 1, 'Engenharias - engenharia elétrica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (68, 1, 1, 'Engenharias - engenharia mecânica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (69, 1, 1, 'Engenharias - engenharia naval e oceânica', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (70, 1, 1, 'Engenharias - engenharia nuclear', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (71, 1, 1, 'Engenharias - engenharia química', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (72, 1, 1, 'Engenharias - engenharia sanitária', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (73, 1, 1, 'Especialização BR', true, 'admin', '2016-07-01 15:03:29.454');
INSERT INTO curso_especializacao VALUES (74, 1, 1, 'Linguística, letras e artes - artes', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (75, 1, 1, 'Linguística, letras e artes - letras', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (76, 1, 1, 'Linguística, letras e artes - linguística', false, 'admin', '2016-06-03 13:45:17.103');
INSERT INTO curso_especializacao VALUES (77, 1, 1, 'Outros', true, 'admin', '2016-06-03 13:45:17.103');
