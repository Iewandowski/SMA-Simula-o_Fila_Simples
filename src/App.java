import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;

public class App {

    public static void main(String[] args) throws Exception {
        DecimalFormat format = new DecimalFormat("0.00");

        // Iniciando variaveis
        String intervalo_chegada, intervalo_atendimento;
        int servidores, K;
        int repeticoes = 5;
        ArrayList<float[][]> tempo_execucao = new ArrayList<>();

        // Iniciando leitura de arquivo txt
        FileReader arq = new FileReader(
                "src\\fila.txt");
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine();

        // Atribuindo informações de txt às variaveis
        String[] split = linha.split(",");
        String[] split2 = split[0].split("/");

        intervalo_chegada = split[1];
        intervalo_atendimento = split[2];
        servidores = Integer.parseInt(split2[2]);
        K = Integer.parseInt(split2[3]);

        String linha_dois = lerArq.readLine();

        // Atribuindo informações de txt às variaveis
        String[] split_dois = linha_dois.split(",");
        String[] split2_dois = split_dois[0].split("/");

        String intervalo_atendimento_fila_dois = split_dois[1];
        int servidores_fila_dois = Integer.parseInt(split2_dois[2]);
        int K_fila_dois = Integer.parseInt(split2_dois[3]);

        System.out.println("intervalo: " + intervalo_atendimento_fila_dois + " servidores: " + servidores_fila_dois
                + " capacidade " + K_fila_dois);
        lerArq.close();

        // Iniciando fila com repetições
        // FilaSimples filaSimples = new FilaSimples(intervalo_chegada,
        // intervalo_atendimento, servidores, K);
        FilaTandem filaTandem = new FilaTandem(intervalo_chegada, intervalo_atendimento, servidores, K,
                intervalo_atendimento_fila_dois, servidores_fila_dois, K_fila_dois);
        for (int i = 0; i < repeticoes; i++) {
            filaTandem.setT((float) 2.5); // primeiro cliente chega no tempo 2.5
            tempo_execucao.add(filaTandem.fila(i));
        }
        calculaMediaExecucoes(tempo_execucao, repeticoes, K, K_fila_dois);
    }

    public static void calculaMediaExecucoes(ArrayList<float[][]> tempo_execucao, int repeticoes, int tamanho_fila_um,
            int tamanho_fila_dois) {
        float[] tempo_por_execucao = new float[tempo_execucao.get(0)[0].length];
        float tempo_total = 0;
        int tamanho_fila = 0;
        for (int k = 0; k < 2; k++) {
            if (k == 0) {
                tamanho_fila = tamanho_fila_um;
            } else {
                tempo_por_execucao = new float[tempo_execucao.get(0)[0].length];
                tamanho_fila = tamanho_fila_dois;
            }
            for (int i = 0; i < tempo_execucao.size(); i++) {
                for (int j = 0; j <= tamanho_fila; j++) {
                    tempo_por_execucao[j] += tempo_execucao.get(i)[k][j];
                    tempo_total += tempo_execucao.get(i)[k][j];

                }
            }
            tempo_total = tempo_total / repeticoes;
            totalExecucaoToString(tempo_por_execucao, repeticoes, tempo_total, k);
            tempo_total = 0;
        }
    }

    public static void totalExecucaoToString(float[] tempo_por_execucao, int repeticoes, float tempo_total,
            int index_fila) {
        int index = 0;
        System.out.println("====================================================");
        System.out.println("==================RESULTADO GERAL===================");
        System.out.println("==================      FILA " + (index_fila + 1) + "    ===================");
        System.out.println("====================================================");
        System.out.println("Tempo total: " + tempo_total);
        System.out.println("Estado\t\tTempo\t\t\tProbabilidade");
        for (float tempo : tempo_por_execucao) {
            // calcular probabilidade geral e formatar número para impressão
            double prob = ((tempo / repeticoes) / tempo_total) * 100;
            String probFormat = String.format("%.4f", prob);

            // imprimir resultado geral
            System.out.println(index + "\t\t" + (tempo / repeticoes) + "\t\t"
                    + probFormat + "% ");
            index++;
        }
        System.out.println("====================================================");
    }
}
