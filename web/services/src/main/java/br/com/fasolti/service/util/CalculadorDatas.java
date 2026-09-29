package br.com.fasolti.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class CalculadorDatas {

	/**
	 * Compara a data1 com a data2.
	 * Retorna maior que zero se a data1 for maior que a data2.
	 * Retorna menor que zero se a data1 for menor que a data2.
	 * Retorno zero se a data1 for igual a data2.
	 *
	 * @param data1
	 * @param data2
	 * @return Retorna um inteiro maior, menor ou igual a zero.
	 */
	public static int comparaDatas(Date data1, Date data2) {
		if(data1 == null || data2 == null)
			return 0;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.setTime(data1);
		cal2.setTime(data2);


		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
			return cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		} else if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
			return cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
		} else {
			return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
		}
	}


	/**
	 * Adiciona dias a uma data.
	 */
	public static Date acrescentarDias(Date data, int qtdDias) {
		Calendar novoData = Calendar.getInstance();
		novoData.setTime(data);
		novoData.add(Calendar.DAY_OF_MONTH, +qtdDias);
		return novoData.getTime();
	}

	/**
	 * Subtrair dias a uma data.
	 */
	public static Date subtrairDias(Date data, int qtdDias) {
		Calendar novoData = Calendar.getInstance();
		novoData.setTime(data);
		novoData.add(Calendar.DAY_OF_MONTH, -qtdDias);
		return novoData.getTime();
	}

	/**
	 * Adiciona meses a uma data.
	 */
	public static Date acrescentarMeses(Date data, int qtdMeses) {
		Calendar novoData = Calendar.getInstance();
		novoData.setTime(data);
		novoData.add(Calendar.MONTH, +qtdMeses);
		return novoData.getTime();
	}

	/**
	 * Subtrair meses a uma data.
	 */
	public static Date subtrairMeses(Date data, int qtdMeses) {
		Calendar novoData = Calendar.getInstance();
		novoData.setTime(data);
		novoData.add(Calendar.MONTH, -qtdMeses);
		return novoData.getTime();
	}

	/**
	 * Adiciona anos a uma data.
	 */
	public static Date acrescentarAnos(Date data, int qtdAnos) {
		Calendar novoData = Calendar.getInstance();
		novoData.setTime(data);
		novoData.add(Calendar.YEAR, +qtdAnos);
		return novoData.getTime();
	}

	/**
	 * Subtrair anos a uma data.
	 */
	public static Date subtrairAnos(Date data, int qtdAnos) {
		Calendar novoData = Calendar.getInstance();
		novoData.setTime(data);
		novoData.add(Calendar.YEAR, -qtdAnos);
		return novoData.getTime();
	}


	/**
	 * Obtém a data corrente, mais conhecida como hoje.
	 */
	public static Date hoje() {
		return new Date();
	}

	/**
	 * Retorna a diferença entre as datas em dias.
	 */
	public static int diferencaEntreDatas(Date data1, Date data2) {
		if (data1 == null || data2 == null) {
			return 0;
		}
		/*GregorianCalendar ini = new GregorianCalendar();
		GregorianCalendar fim = new GregorianCalendar();*/
		Calendar ini = Calendar.getInstance();
		Calendar fim = Calendar.getInstance();

		ini.setTime(data1);
		fim.setTime(data2);
		long dt1 = ini.getTimeInMillis();
		long dt2 = fim.getTimeInMillis();

		return (int) (((dt2 - dt1) / 86400000));
	}


	/**
	 * Converte uma String em Date.
	 */
	public static Date converteStringParaDate(String dataStr, String formato) {
		Date data = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(formato);
			data = new Date(format.parse(dataStr).getTime());
		} catch (ParseException e) {
			return null;
		}
		return data;
	}

	/**
	 * Converte um Date em String conforme o formato passado por parametro.
	 */
	public static String converteDateParaString(Date dt, String formato) {
		SimpleDateFormat format;

		if (formato == null || formato.trim().equals(""))
			format = new SimpleDateFormat("dd/MM/yyyy");
		else
			format = new SimpleDateFormat(formato);

		String data = format.format(dt);
		return data;
	}

	/**
	 * Obtem a quantidade de anos entre as datas. data2(maior data) e data1(menor data)
     */
	public static int obterAnos(Date data1, Date data2) {
		int anos = 0;

		int dias = CalculadorDatas.diferencaEntreDatas(data1, data2);
		if(dias > 0)
			anos = dias/365;

		return anos;
	}

	/**
	 * Obtem a quantidade de meses entre as datas. data2(maior data) e data1(menor data)
	 */
	public static int obterMeses(Date data1, Date data2) {
		int anos = 0;

		int dias = CalculadorDatas.diferencaEntreDatas(data1, data2);
		if(dias > 0)
			anos = dias/12;

		return anos;
	}
}
