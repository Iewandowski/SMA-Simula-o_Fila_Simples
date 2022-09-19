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
        ArrayList<float[]> tempo_execucao = new ArrayList<>();

        // Iniciando leitura de arquivo txt
        FileReader arq = new FileReader("src\\fila.txt");
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine();

        // Atribuindo informações de txt às variaveis
        String[] split = linha.split(",");
        String[] split2 = split[0].split("/");

        intervalo_chegada = split[1];
        intervalo_atendimento = split[2];
        servidores = Integer.parseInt(split2[2]);
        K = Integer.parseInt(split2[3]);

        // Iniciando fila com repetições
        FilaSimples filaSimples = new FilaSimples(intervalo_chegada, intervalo_atendimento, servidores, K);
        for (int i = 0; i < repeticoes; i++) {
            filaSimples.setT(3); // primeiro cliente chega no tempo 3.0
            tempo_execucao.add(filaSimples.fila(i));
        }

        calculaMediaExecucoes(tempo_execucao, repeticoes);

    }

    public static void calculaMediaExecucoes(ArrayList<float[]> tempo_execucao, int repeticoes) {
        float tempo_total = 0;
        float[] tempo_por_execucao = new float[tempo_execucao.get(0).length];
        for (int i = 0; i < tempo_execucao.get(0).length; i++) {
            for (int j = 0; j < repeticoes; j++) {
                tempo_por_execucao[i] += tempo_execucao.get(j)[i];
                tempo_total += tempo_execucao.get(j)[i];
            }
        }
        tempo_total = tempo_total / repeticoes;
        totalExecucaoToString(tempo_por_execucao, repeticoes, tempo_total);
    }

    public static void totalExecucaoToString(float[] tempo_por_execucao, int repeticoes, float tempo_total) {
        int index = 0;
        System.out.println("====================================================");
        System.out.println("==================RESULTADO GERAL===================");
        System.out.println("====================================================");
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
    }
}
