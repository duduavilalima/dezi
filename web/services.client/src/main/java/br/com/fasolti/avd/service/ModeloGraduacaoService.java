package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;

public interface ModeloGraduacaoService extends AvdService<ModeloGraduacaoTO, ModeloGraduacaoTO> {

	/**
	 * Obter o modelo de graduação pela empresa e descrição.
	 *
	 * @param idEmpresa O ID da empresa.
	 * @param descricaoModelo A descrição do modelo.
     * @return O modelo de gradução pela empresa e descrição.
     */
	ModeloGraduacaoTO findByEmpresaAndDescricaoModelo(Long idEmpresa, String descricaoModelo);

	ModeloGraduacaoTO fullObject(ModeloGraduacaoTO modeloGraduacaoTO);

	ModeloGraduacaoTO obterModeloGraduacaoProjections(Long idModeloGraduacao);

}
