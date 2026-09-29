package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Foto;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.QFoto;
import br.com.fasolti.avd.repository.FotoRepository;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.transfer.FotoTO;
import br.com.fasolti.avd.types.TipoFoto;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FotoServiceImpl extends BaseAvdService<Foto, FotoTO, FotoTO> implements FotoService {

	private final QFoto qFoto = QFoto.foto;

	@Autowired
	private FotoRepository fotoRepository;

	@Autowired
	public FotoServiceImpl(FotoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FotoTO exampleObj) {
		builder
			.and(dslHelper.eq(qFoto.funcionario, exampleObj.getFuncionario()))
			.and(dslHelper.eq(qFoto.tipo, exampleObj.getTipo()));
	}

	@Override
	public FotoTO obterFotoPerfilFuncionario(Long funcionarioId) {
		Funcionario funcionario = new Funcionario();
		funcionario.setId(funcionarioId);
		Foto foto = fotoRepository.findByTipoAndFuncionario(TipoFoto.FOTO_PERFIL, funcionario);
		if (foto == null) {
			return null;
		}
		return map(foto);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public FotoTO obterAvatarFuncionario(Long funcionarioId) {
		Funcionario funcionario = new Funcionario();
		funcionario.setId(funcionarioId);
		//Se tiver fotoAvatar, retorna-a
		Foto fotoAvatar = fotoRepository.findByTipoAndFuncionario(TipoFoto.FOTO_AVATAR, funcionario);

		if (fotoAvatar == null) {
			Foto fotoPerfil = fotoRepository.findByTipoAndFuncionario(TipoFoto.FOTO_PERFIL, funcionario);
			if (fotoPerfil == null) {
				return null;
			}
			fotoAvatar = transformSaveFotoMobile(fotoPerfil);
		}
		return map(fotoAvatar);
	}

	private Foto transformSaveFotoMobile(Foto fotoPerfil) {
		Foto fotoAvatar = new Foto();

		//Se existir, transforma a imagem em um avatar e salva-a
		String base64transformed = transformFotoTO(fotoPerfil, 80, 80, 7);
		fotoAvatar.setBase64(base64transformed);
		fotoAvatar.setFuncionario(fotoPerfil.getFuncionario());
		fotoAvatar.setTipo(TipoFoto.FOTO_AVATAR);

		fotoRepository.save(fotoAvatar);

		return fotoAvatar;
	}

	@Override
	protected ServiceFilter<Foto> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		return ServiceFilter.create(qFoto, mandatoryFilter);
	}

	@Override
	protected void doBeforeSave(FotoTO object) throws ServiceException {
		if (object.getTipo() != null && object.getTipo().equals(TipoFoto.FOTO_PERFIL))
			object.setTipo(TipoFoto.FOTO_PERFIL);
	}

	@Override
	protected void doBeforeUpdate(FotoTO object) throws ServiceException {
		if (object.getTipo() != null && object.getTipo().equals(TipoFoto.FOTO_PERFIL))
			object.setTipo(TipoFoto.FOTO_PERFIL);
	}


	/**
	 * Redimensiona e comprime a FotoTO de um funcionario
	 *
	 * @param foto        FotoTO do funcionário
	 * @param width       Largura desejada no resize
	 * @param height      Altura desejada no resize
	 * @param compression Qualidade da compressäo (1~10), onde 10 representa 100%
	 * @return String do Base64 redimensionado e comprimido
	 */

	//FIXME: Coloco o throw IOException?
	private String transformFotoTO(Foto foto, int width, int height, int compression) {

		String base64Transformed = null;
		try {
		/* Por default, o ImageIO usa o disco para cachear o InputOutputStream
		 o que pode deixar a escrita em memória lenta, podemos retirá-lo */

			//ImageIO.setUseCache(false);

			// Obtem a extensão do Base64 (PNG, JPG, TIFF...)
			String mimeType = extractExtensionImg(foto.getBase64());

			// Pega, a partir da vírgula o base64 da fotoTO
			byte[] dataBytes = Base64.getMimeDecoder().decode(foto.getBase64().split(",")[1]);

			// Cria uma imagem em buffer, a partir dos bytes da base64
			ByteArrayInputStream stream = new ByteArrayInputStream(dataBytes);
			BufferedImage originalImage = ImageIO.read(stream);
			stream.close();

			// Resize da imagem original
			BufferedImage resizedImage = resizeImg(width, height, originalImage);


			// Compressão da imagem com resize
			String base64foto = compressImg(mimeType, compression, resizedImage);

			// Parte inicial do base64
			String base64Mime = "data:image/" + mimeType + ";base64,";

			base64Transformed = base64Mime + base64foto;

			/*Código apenas para verificar o tamanho da compressão*/

			ByteArrayOutputStream tmpBef = new ByteArrayOutputStream();
			ImageIO.write(originalImage, mimeType, tmpBef);

			System.out.println("--Tamanho da foto [antes]: " + tmpBef.size() + "b");
			System.out.println("--Dimensao foto [antes]: " + originalImage.getWidth() + "x" + originalImage.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Transformed;
	}

	private BufferedImage resizeImg(int width, int height, BufferedImage originalImage) {
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		// Resizing
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}

	private String compressImg(String extension, int compression, BufferedImage resizedImage) throws
		FileNotFoundException, IOException {

		String fotoBase64 = null;

		//Compression
		try {
			ImageWriter writerCompressor = ImageIO.getImageWritersByFormatName(extension).next();

			// Qualidade da compressão
			ImageWriteParam param = writerCompressor.getDefaultWriteParam();
			if (param.canWriteCompressed()) {
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(0.1f * compression);
			}
			ByteArrayOutputStream compressed = new ByteArrayOutputStream();
			// Output stream, que o ImageIO usa como retorno da compressão
			ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed);
			writerCompressor.setOutput(outputStream);

			/* Usa a imagem que foi aplicada a transformação de tamanho
			/ e aplica a compressão de qualidade */
			writerCompressor.write(null, new IIOImage(resizedImage, null, null), param);

			//Base64 da compressão, expresso em bytes
			byte[] base64Byte = compressed.toByteArray();

			//Transformação dos bytes em Base64
			fotoBase64 = Base64.getEncoder().encodeToString(base64Byte);
			outputStream.flush();


			/*CODIGO APENAS PARA CHECAR O TAMANHO DA IMG*/
			ByteArrayInputStream test = new ByteArrayInputStream(base64Byte);
			BufferedImage bi2 = ImageIO.read(test);

			ByteArrayOutputStream tmp = new ByteArrayOutputStream();
			ImageIO.write(bi2, extension, tmp);
			tmp.close();
			System.out.println("--Dimensao foto [depois]: " + resizedImage.getWidth() + "x" + resizedImage.getHeight());
			System.out.println("--Tamanho da foto [depois]: " + tmp.size() + "b");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fotoBase64;
	}


	/**
	 * Extrai a extensão de um Base64
	 *
	 * @param encoded Base64 string
	 * @return ext string
	 */
	private String extractExtensionImg(String encoded) {
		final Pattern mime = Pattern.compile("^data:image/([a-zA-Z0-9]+).*,.*");
		final Matcher matcher = mime.matcher(encoded);
		if (!matcher.find())
			return "";
		return matcher.group(1).toLowerCase();
	}

}
