package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.service.EmailService;
import edu.ufrn.imd.Biblicron.service.EmprestimoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class EmailScheduler {

    private final EmprestimoService emprestimoService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(EmailScheduler.class);


    public EmailScheduler(EmprestimoService emprestimoService, EmailService emailService) {
        this.emprestimoService = emprestimoService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Agendamento para executar uma vez ao dia à meia-noite
    /*@Scheduled(fixedRate = 5000) esse aqui é para fins de testes, ele vai executar uma vez a cada 5 segundos (o tempo está
    em milliseconds. O correto é deixar o de cima.
    */
    public void enviarEmailsDeLembrete() {
        List<Emprestimo> emprestimosProximosDoVencimento = emprestimoService.findEmprestimosComMaxReturnDate(LocalDate.now());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Emprestimo emprestimo : emprestimosProximosDoVencimento) {
            String emailUsuario = emprestimo.getUsuario().getEmail();
            String assunto = "Lembrete de devolução do livro " + emprestimo.getLivro().getTitulo();
            String mensagem = "Prezad@, " + emprestimo.getUsuario().getUsername() + ".\n" +
                    "\n" +
                    "Gostaríamos de informar que a data limite para devolução do livro " + emprestimo.getLivro().getTitulo() +
                    " é " + dateFormat.format(Date.from(emprestimo.getMaxReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant())) + ".\n" +
                    "O número de identificação do seu empréstimo é id: " + emprestimo.getId() + ".\n" +
                    "\n" +
                    "Pedimos encarecidamente que não atrase a devolução do livro, podendo haver penalidades caso tal situação ocorra.\n" +
                    "\n" +
                    "Agradecidamente,\n" +
                    "Biblicron.";

            emailService.enviarEmail(emailUsuario, assunto, mensagem);
        }
    }
}
