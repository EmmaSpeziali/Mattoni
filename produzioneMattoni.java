import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProduzioneMattoni {
    static final int MATTONI_PER_CICLO = 1600;
    static final int MATTONI_PER_MESCOLATRICE = 200;
    static final int MATTONI_PER_FORNO = 400;

    public static void main(String[] args) throws InterruptedException {
        GestoreLog log = new GestoreLog("processo_log.txt");
        Malta malta = new Malta();

        // Step 1: Preparazione della malta
        ExecutorService mescolatrici = Executors.newFixedThreadPool(8);
        for (int i = 1; i <= 8; i++) {
            mescolatrici.submit(new Mescolatrice(i, malta, log));
        }
        mescolatrici.shutdown();
        while (!mescolatrici.isTerminated()) { }
        log.scriviLog("Tutte le mescolatrici hanno completato il lavoro.");

        // Step 2: Formatura e cottura
        Stampo stampo = new Stampo();
        Forno forno = new Forno(MATTONI_PER_FORNO, log);

        for (int i = 0; i < 4; i++) {
            stampo.formareMattoni(MATTONI_PER_FORNO);
            forno.cuociMattoni(i + 1);
        }

        // Step 3: Imballaggio
        Imballaggio imballaggio = new Imballaggio(log);
        imballaggio.imballa(MATTONI_PER_CICLO);

        log.scriviLog("Processo completato con successo.");
    }
}
