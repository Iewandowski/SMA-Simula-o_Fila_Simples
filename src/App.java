import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class App {

    public static void main(String[] args) throws Exception {
        DecimalFormat format = new DecimalFormat("0.00");
        Scanner scanner = new Scanner(System.in);

        // ler intervalo de tempo para a chegada de clientes na fila:
        System.out.print("Digite o intervalo de tempo para a chegada de cliente: (numero..numero) ");
        String intervalo_chegada = scanner.next();

        // ler intervalo de tempo de atendimento de um cliente na fila:
        System.out.print("Digite o intervalo de tempo de atendimento para um cliente: (numero..numero) ");
        String intervalo_atendimento = scanner.next();

        // ler número de servidores:
        System.out.print("Digite o número de servidores: ");
        int servidores = scanner.nextInt();

        int repeticoes = 5;

        // ler a capacidade da fila:
        System.out.print("Digite a capacidade da fila: ");
        int K = scanner.nextInt();
        scanner.close();
        ArrayList<float[]> tempo_execucao = new ArrayList<>();

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
        tempoExecucaoToString(tempo_por_execucao, repeticoes);
        probabilidadeExecucaoToString(tempo_por_execucao, repeticoes, tempo_total);
    }

    public static void tempoExecucaoToString(float[] tempo_por_execucao, int repeticoes) {
        int index = 0;
        for (float tempo : tempo_por_execucao) {
            System.out.println(
                    "Tempo total médio do cliente(s) " + index + " durante as execucoes: " + (tempo / repeticoes));
            index++;
        }
        System.out.println(" --------------------------  ");
    }

    public static void probabilidadeExecucaoToString(float[] tempo_por_execucao, int repeticoes, float tempo_total) {
        int index = 0;
        for (float tempo : tempo_por_execucao) {
            System.out.println("Probabilidade de ter " + index + " cliente(s) durante as execucoes: "
                    + ((tempo / repeticoes) / tempo_total) * 100 + " % ");
            index++;
        }
    }
}
