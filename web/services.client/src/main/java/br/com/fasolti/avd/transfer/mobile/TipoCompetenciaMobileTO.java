package br.com.fasolti.avd.transfer.mobile;

public class TipoCompetenciaMobileTO {

    private Long id;
	private Long funcionarioId;
	private Long paiId;
	private Long empresaId;
    private String nome;
	private String descricao;
	private String nomeCurto;

	private TipoCompetenciaMobileTO pai;
	private EmpresaMobileTO empresa;

    public TipoCompetenciaMobileTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaiId() {
        return paiId;
    }

    public void setPaiId(Long paiId) {
        this.paiId = paiId;
    }

	public Long getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Long empresaId) {
		this.empresaId = empresaId;
	}

	public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

	public Long getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(Long funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public String getNomeCurto() {
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}

	public TipoCompetenciaMobileTO getPai() {
		return pai;
	}

	public void setPai(TipoCompetenciaMobileTO pai) {
		this.pai = pai;
	}

	public EmpresaMobileTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaMobileTO empresa) {
		this.empresa = empresa;
	}
}
