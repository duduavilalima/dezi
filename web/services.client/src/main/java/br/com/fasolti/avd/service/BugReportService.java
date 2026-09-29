package br.com.fasolti.avd.service;

/**
 * Classe responsável por enviar email de relatório de erro.
 */
public interface BugReportService {

    /**
     * Médoto responsável pelo envio de email de bug.
     * @param title O título do email.
     * @param description A descrição do email.
     * @param objects O objetos do relatório.
     */
    void notificateBug(String title, String description, Object... objects);

}
