-- DROP SEQUENCE public.seq_endereco;
CREATE SEQUENCE public.seq_endereco
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	CACHE 1
	NO CYCLE;

CREATE TABLE public.endereco (
	id int8 NOT NULL,
	id_funcionario int8 NOT NULL,
	id_municiopio int8 NOT NULL
);
CREATE INDEX endereco_id_idx ON public.endereco (id,id_funcionario);
COMMENT ON TABLE public.endereco IS 'Tabela de endreço de um funcionário.';

-- Column comments

COMMENT ON COLUMN public.endereco.id IS 'O identificador do endereço.';
COMMENT ON COLUMN public.endereco.id_funcionario IS 'O código do funcionário dono do endereço.';
COMMENT ON COLUMN public.endereco.id_municiopio IS 'O código do município do endereço.';

ALTER TABLE public.endereco ALTER COLUMN id SET DEFAULT nextval('seq_endereco'::regclass);
