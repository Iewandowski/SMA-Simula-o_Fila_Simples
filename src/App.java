import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;

public class App {

    public static void main(String[] args) throws Exception {

        DecimalFormat format = new DecimalFormat("0.00");

        // Iniciando variaveis
        ArrayList<float[][]> tempo_execucao = new ArrayList<>();
        ArrayList<String> numero_linhas = new ArrayList<String>();
        String intervalo_chegada, intervalo_atendimento;
        int servidores, K;
        int repeticoes = 5;

        // Iniciando leitura de arquivo txt
        FileReader arq = new FileReader(
                "src\\fila3.txt");
        BufferedReader lerArq = new BufferedReader(arq);
        String line;

        while ((line = lerArq.readLine()) != null) {
            numero_linhas.add(line);
        }

        if (numero_linhas.size() == 1) {
            ArrayList<float[]> tempo_execucao_simples = new ArrayList<>();
            String[] split = numero_linhas.get(0).split(",");
            String[] split2 = split[0].split("/");

            intervalo_chegada = split[1];
            intervalo_atendimento = split[2];
            servidores = Integer.parseInt(split2[2]);
            K = Integer.parseInt(split2[3]);

            System.out.print("RESULTADO - FILA SIMPLES: ");
            FilaSimples filaSimples = new FilaSimples(intervalo_chegada, intervalo_atendimento, servidores, K);

            for (int i = 0; i < repeticoes; i++) {
                filaSimples.setT((float) 2.5); // primeiro cliente chega no tempo 2.5
                tempo_execucao_simples.add(filaSimples.fila(i));
            }

            calculaMediaExecucoesSimples(tempo_execucao_simples, repeticoes);

        } else if (numero_linhas.size() == 2) {
            String[] split = numero_linhas.get(0).split(",");
            String[] split2 = split[0].split("/");

            intervalo_chegada = split[1];
            intervalo_atendimento = split[2];
            servidores = Integer.parseInt(split2[2]);
            K = Integer.parseInt(split2[3]);

            String linha_dois = numero_linhas.get(1);

            // Atribuindo informações de txt às variaveis
            String[] split_dois = linha_dois.split(",");
            String[] split2_dois = split_dois[0].split("/");

            String intervalo_atendimento_fila_dois = split_dois[1];
            int servidores_fila_dois = Integer.parseInt(split2_dois[2]);
            int K_fila_dois = Integer.parseInt(split2_dois[3]);

            System.out.print("RESULTADO - FILA TANDEM: ");
            System.out.println("intervalo: " + intervalo_atendimento_fila_dois + " servidores: " + servidores_fila_dois
                    + " capacidade " + K_fila_dois);

            FilaTandem filaTandem = new FilaTandem(intervalo_chegada, intervalo_atendimento, servidores, K,
                    intervalo_atendimento_fila_dois, servidores_fila_dois, K_fila_dois);
            for (int i = 0; i < repeticoes; i++) {
                filaTandem.setT((float) 2.5); // primeiro cliente chega no tempo 2.5
                tempo_execucao.add(filaTandem.fila(i));
            }
            calculaMediaExecucoesTandem(tempo_execucao, repeticoes, K, K_fila_dois);

        } else if (numero_linhas.size() >= 3) {
            System.out.print("Fila de Probabilidade ");
            String[] split = numero_linhas.get(0).split(",");
            String[] split2 = split[0].split("/");
            ObjetoFila[] filas = new ObjetoFila[numero_linhas.size()];

            for (int i = 0; i < numero_linhas.size(); i++) {
                ObjetoFila fila = retornaDadosFila(numero_linhas.get(i), true, "F" + (i + 1));
                // System.out.println(fila.possiveisCaminhos[i].probabilidade + "Testando");
                filas[i] = fila;
            }

            FilaProbabilidade filaProbabilidade = new FilaProbabilidade(filas);

            filaProbabilidade.setT((float) 1.0); // primeiro cliente chega no tempo 2.5
            filaProbabilidade.fila(0);

            // calculaMediaExecucoesTandem(fila1,fila2,fila3);

        }

        lerArq.close();
    }

    public static void calculaMediaExecucoesTandem(ArrayList<float[][]> tempo_execucao, int repeticoes,
            int tamanho_fila_um,
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
            totalExecucaoTandemToString(tempo_por_execucao, repeticoes, tempo_total, k);
            tempo_total = 0;
        }
    }

    public static void totalExecucaoTandemToString(float[] tempo_por_execucao, int repeticoes, float tempo_total,
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

    public static void calculaMediaExecucoesSimples(ArrayList<float[]> tempo_execucao, int repeticoes) {
        float tempo_total = 0;
        float[] tempo_por_execucao = new float[tempo_execucao.get(0).length];
        for (int i = 0; i < tempo_execucao.get(0).length; i++) {
            for (int j = 0; j < repeticoes; j++) {
                tempo_por_execucao[i] += tempo_execucao.get(j)[i];
                tempo_total += tempo_execucao.get(j)[i];
            }
        }
        tempo_total = tempo_total / repeticoes;
        totalExecucaoSimplesToString(tempo_por_execucao, repeticoes, tempo_total);
    }

    public static ObjetoFila retornaDadosFila(String linha, boolean contemChegada, String nomeFila) {
        int index = 0;
        // Atribuindo informações de txt às variaveis
        String[] split = linha.split(",");
        String[] split_dois = split[0].split("/");

        if (!contemChegada) {
            String intervalo_atendimento = split[1];
            int servidores = Integer.parseInt(split_dois[2]);
            int K = Integer.parseInt(split_dois[3]);

            PossivelCaminho[] destinos = new PossivelCaminho[(split.length - 2) / 2];

            for (int i = 2; i < split.length; i = i + 2) {
                destinos[index] = new PossivelCaminho(split[i], Double.parseDouble(split[i + 1]));
                index++;
            }
            return new ObjetoFila(" ", intervalo_atendimento, servidores, K, destinos, nomeFila);
        } else {
            String chegada_intervalo = split[1];
            String intervalo_atendimento = split[2];
            int servidores = Integer.parseInt(split_dois[2]);
            int K = Integer.parseInt(split_dois[3]);
            if (K == 0) {
                K = Integer.MAX_VALUE;
            }

            PossivelCaminho[] destinos = new PossivelCaminho[(split.length - 3) / 2];
            for (int i = 3; i < split.length; i = i + 2) {
                destinos[index] = new PossivelCaminho(split[i], Double.parseDouble(split[i + 1]));
                index++;
            }
            return new ObjetoFila(chegada_intervalo, intervalo_atendimento, servidores, K, destinos, nomeFila);
        }
    }

    public static void totalExecucaoSimplesToString(float[] tempo_por_execucao, int repeticoes, float tempo_total) {
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
        System.out.println("====================================================");
    }
}
