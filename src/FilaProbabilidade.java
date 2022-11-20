import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class FilaProbabilidade {

    public enum Rede {
        Aberta, Fechada;
    }

    public int K, servidores, servidores_fila_dois, K_fila_dois;
    public float tempo_cliente, T;
    String intervalo_chegada, intervalo_atendimento, intervalo_atendimento_fila_dois, intervalo_chegada_fila_dois;
    CongruenteLinear cl = new CongruenteLinear();
    public ObjetoFila[] filas = new ObjetoFila[3];
    public ArrayList<TipoOperacao> agenda_movimentacao = new ArrayList<>();
    public ArrayList<Float> agenda_chegada = new ArrayList<>();
    static int contador = 0;
    private float tempo_global = 0;
    private float tempo_anterior_chegada = 0;
    private float tempo_anterior_saida = 0;
    private float tempo_anterior_passagem = 0;
    private float proxima_chegada;
    Random geradorNumeros;
    private int a, c, M;
    private float Xi = 3;
    public Rede tipoRede;

    public FilaProbabilidade(ObjetoFila[] filas) {
        this.filas = filas;
        this.geradorNumeros = new Random();
        this.a = Math.abs(geradorNumeros.nextInt(100000) + 100);
        this.c = Math.abs(geradorNumeros.nextInt(100000) + 100);
        this.M = Math.abs(geradorNumeros.nextInt(100000) + 100);
        verificaRede(filas);
    }

    public void verificaRede(ObjetoFila[] filas) {
        for (int i = 0; i < filas.length; i++) {
            if (filas[i].contemChegada()) {
                this.tipoRede = Rede.Aberta;
                return;
            }
        }
        this.tipoRede = Rede.Fechada;
    }

    public Rede getTipoRede() {
        return this.tipoRede;
    }

    public float getTempoGlobal() {
        return tempo_global;
    }

    public void setTempoGlobal(float tempo) {
        this.tempo_global = tempo;
    }

    public float getTempoAnteriorChegada() {
        return tempo_anterior_chegada;
    }

    public void setTempoAnteriorChegada(float tempo) {
        this.tempo_anterior_chegada = tempo;
    }

    public float getProximaChegada() {
        return proxima_chegada;
    }

    public void setProximaChegada(float prox_chegada) {
        this.proxima_chegada = prox_chegada;
    }

    public void setTempoAnteriorPassagem(float proxPassagem) {
        this.tempo_anterior_passagem = proxPassagem;
    }

    public float getTempoAnteriorPassagem() {
        return tempo_anterior_passagem;
    }

    public float getTempoAnteriorSaida() {
        return tempo_anterior_saida;
    }

    public void setTempoAnteriorSaida(float tempo) {
        this.tempo_anterior_saida = tempo;
    }

    public float getTempoCliente() {
        return this.tempo_cliente;
    }

    public float getT() {
        return this.T;
    }

    public void setT(float T) {
        this.T = T;
        setTempoGlobal(T);
    }

    public float getLastXi() {
        return this.Xi;
    }

    public void setLastXi(float Xi) {
        this.Xi = Xi;
    }

    public void zerarAlgoritmo() {
        this.tempo_global = 0;
        this.tempo_anterior_chegada = 0;
        this.tempo_anterior_saida = 0;
        this.tempo_anterior_passagem = 0;
        this.proxima_chegada = 0;
        this.geradorNumeros = new Random();
        this.a = Math.abs(geradorNumeros.nextInt(100000) + 100);
        this.c = Math.abs(geradorNumeros.nextInt(100000) + 100);
        this.M = Math.abs(geradorNumeros.nextInt(100000) + 100);
    }

    public void zeraChegadaAgendasClientes() {
        for (int i = 0; i < filas.length; i++) {
            filas[i].chegadas = new ArrayList<>();
            filas[i].agendas = new ArrayList<>();
            filas[i].clientesAtuais = 0;
        }
    }

    public void agendarProximaChegada() {
        if (agenda_chegada.size() >= 1) {
            agenda_chegada.remove(0);
        }
        float proxima_chegada = T + gerarRandom(filas[0].intervalo_chegada);
        agenda_chegada.add(proxima_chegada);
        setProximaChegada(proxima_chegada);
        agenda_movimentacao.add(new TipoOperacao(proxima_chegada, "CH1"));
    }

    public void chegada(float T, int numero_fila) {
        setT(T);
        if (filas[numero_fila].getQntdClientesAtuais() < filas[numero_fila].clientes) {
            setaTempoFilas(T);
            setTempoAnteriorChegada(T);
            agenda_chegada.remove(0);
            agenda_movimentacao.remove(0);
            filas[numero_fila].incrementaCliente();
            filas[numero_fila].chegadas.add(T);
            if (filas[numero_fila].servidoresOcupados < filas[numero_fila].servidores) {
                filas[numero_fila].incrementaServidoresOcupados();
                float proxima_passagem = T + gerarRandom(filas[numero_fila].intervalo_atendimento);
                agenda_movimentacao.add(new TipoOperacao(proxima_passagem, calculaProximaOperacao(filas[numero_fila])));
            }
            agendarProximaChegada();
        } else if (agenda_movimentacao.size() < filas[numero_fila].servidores) {
            float proxima_passagem = T + gerarRandom(filas[numero_fila].intervalo_atendimento);
            agenda_chegada.remove(0);
            agenda_movimentacao.add(new TipoOperacao(proxima_passagem, calculaProximaOperacao(filas[numero_fila])));
            agendarProximaChegada();
        } else {
            agendarProximaChegada();
        }
    }

    public String calculaProximaOperacao(ObjetoFila fila) {
        float probabilidadeTotal = 0;
        float numeroGerado = geradorNumeros.nextFloat();
        for (int i = 0; i < fila.possiveisCaminhos.length; i++) {
            probabilidadeTotal += fila.possiveisCaminhos[i].probabilidade;
            if (probabilidadeTotal > numeroGerado) {
                return fila.nomeFila.concat(fila.possiveisCaminhos[i].caminho);
            }
        }
        return null;
    }

    public void passagemFila(float T, ObjetoFila filaOrigem, ObjetoFila filaDestino) {
        setT(T);
        setaTempoFilas(T);
        setTempoAnteriorPassagem(T);
        filaOrigem.decrementaCliente();
        agenda_movimentacao.remove(0);
        filaOrigem.chegadas.remove(0);
        filaOrigem.decrementaServidoresOcupados();
        if (filaDestino.getQntdClientesAtuais() < filaDestino.clientes) {
            filaDestino.incrementaCliente();
            filaDestino.chegadas.add(T);
            if (filaDestino.servidoresOcupados < filaDestino.servidores) {
                filaDestino.incrementaServidoresOcupados();
                agenda_movimentacao
                        .add(new TipoOperacao(T + gerarRandom(filaDestino.intervalo_atendimento),
                                calculaProximaOperacao(filaDestino)));
            }
        }
    }

    public void setaTempoFilas(float T) {
        for (int i = 0; i < filas.length; i++) {
            filas[i].get_tempo_por_quantidade()[filas[i].getQntdClientesAtuais()] += (T
                    - Math.max(getTempoAnteriorChegada(),
                            Math.max(getTempoAnteriorSaida(), getTempoAnteriorPassagem())));
        }
    }

    public void saida(float T, ObjetoFila fila) {
        setT(T);
        setaTempoFilas(T);
        setTempoAnteriorSaida(T);
        fila.clientesAtuais--;
        System.out.println(fila.chegadas.size());
        fila.chegadas.remove(0);
        agenda_movimentacao.remove(0);
        if (fila.servidoresOcupados < fila.servidores) {
            fila.servidoresOcupados++;
            agenda_movimentacao.add(
                    new TipoOperacao(T + gerarRandom(fila.intervalo_atendimento), calculaProximaOperacao(fila)));
        }
    }

    public float gerarRandom(String intervalo) {
        float U, Ui;
        String[] split = intervalo.split("[..]+");
        int A = Integer.parseInt(split[0]);
        int B = Integer.parseInt(split[1]);

        Xi = (a * getLastXi() + c) % M;
        setLastXi(Xi);
        Ui = Xi / M;

        U = (B - A) * Ui + A;
        return U;
    }

    public void agendaChegada(float chegada) {
        this.agenda_chegada.add(chegada);
        this.agenda_movimentacao.add(new TipoOperacao(chegada, "CH1"));
    }

    public void agendaPassagemFilaToString() {
        for (float passagem : this.agenda_chegada) {
            System.out.println("Agenda: " + passagem);
        }
    }

    public void printaQuantidadeTempoPorFila() {
        int index = 0;
        for (int i = 0; i < filas.length; i++) {
            for (int j = 0; j < filas[i].get_tempo_por_quantidade().length; j++) {
                System.out.println("FILA " + i + " : ");
                System.out.println(
                        "Tempo fila 1: " + " estado " + index + " - " + filas[i].get_tempo_por_quantidade()[j]);
                index++;
            }
        }
    }

    public void tempoTotalEProbabilidadePorQuantidadetoString() {
        for (int i = 0; i < filas.length; i++) {
            System.out.println("****************************************************");
            System.out.println("\t\t\tFila " + (i + 1));
            System.out.println("****************************************************");
            System.out.println("****************************************************");
            System.out.println("Tempo Total: " + tempo_global);
            System.out.println("****************************************************");
            System.out.println("Estado\t\tTempo\t\t\tProbabilidade");
            System.out.println("====================================================");
            System.out.println();
            int index = 0;
            if (filas[i].get_tempo_por_quantidade().length == 100001) {
                for (float tempo_por_quantidade : filas[i].get_tempo_por_quantidade()) {
                    if (tempo_por_quantidade == 0) {
                        break;
                    }
                    double prob = (tempo_por_quantidade / tempo_global) * 100;
                    String probFormat = String.format("%.4f", prob);
                    System.out.println(
                            index + "\t\t" + tempo_por_quantidade + "\t\t" + probFormat
                                    + "%");
                    index++;
                }
            } else {
                for (float tempo_por_quantidade : filas[i].get_tempo_por_quantidade()) {
                    double prob = (tempo_por_quantidade / tempo_global) * 100;
                    String probFormat = String.format("%.4f", prob);
                    System.out.println(
                            index + "\t\t" + tempo_por_quantidade + "\t\t" + probFormat
                                    + "%");
                    index++;
                }
            }
        }
    }

    public ObjetoFila[] fila(int numeroExecucao) {
        if (tipoRede == Rede.Aberta) {
            System.out.println("FILA DE REDE ABERTA");

            for (int i = 0; i < filas.length; i++) {
                this.filas[i].set_tempo_por_quantidade(new float[this.filas[i].clientes + 1]);
            }

            for (int j = 0; j < filas.length; j++) {
                if (filas[j].contemChegada()) {
                    System.out.println("====================================================");
                    System.out.println("================CHEGADA DA FILA " + (j + 1) + "===================");
                    System.out.println("====================================================");
                    for (int i = 0; i < 100000; i++) {
                        Collections.sort(agenda_movimentacao, new ComparadorTiposOperacao());
                        if (agenda_chegada.isEmpty()) {
                            agendaChegada((float) 1.0);
                            chegada(getT(), j);
                        }
                        if (agenda_movimentacao.get(0).tipoOperacao.contains("sa")) {
                            saida(agenda_movimentacao.get(0).tempoOperacao,
                                    retornaFilasOperacao(agenda_movimentacao.get(0).tipoOperacao, true)[0]);
                        } else if (!agenda_movimentacao.get(0).tipoOperacao.toLowerCase().contains("sa")
                                && !agenda_movimentacao.get(0).tipoOperacao.toLowerCase().contains("ch")) {
                            ObjetoFila[] filasPassagem = retornaFilasOperacao(agenda_movimentacao.get(0).tipoOperacao,
                                    false);
                            passagemFila(agenda_movimentacao.get(0).tempoOperacao, filasPassagem[0], filasPassagem[1]);
                        } else {
                            chegada(getProximaChegada(), j);
                        }
                    }
                    setTempoGlobal(T);
                    tempoTotalEProbabilidadePorQuantidadetoString();
                }
                zerarAlgoritmo();
            }
        } else if (tipoRede == Rede.Fechada) {
            System.out.println("FILA DE REDE FECHADA");
            for (int i = 0; i < 100000; i++) {

                if (agenda_chegada.isEmpty()) {
                    agenda_movimentacao.add(null);
                }
            }
            calculaTaxaDeVisita(filas);
        }
        return filas;
    }

    public void printaOrdemAgenda(ArrayList<TipoOperacao> tipos, int laco) {
        System.out.println("LACO : " + laco);
        for (int i = 0; i < tipos.size(); i++) {
            System.out.println(tipos.get(i).tempoOperacao + " --- " + tipos.get(i).tipoOperacao);
        }
    }

    public ObjetoFila[] retornaFilasOperacao(String operacao, boolean isSaida) {
        ObjetoFila[] filasRetorno = new ObjetoFila[2];
        if (isSaida) {
            filasRetorno[0] = this.filas[Integer.parseInt(operacao.substring(1, 2)) - 1];
            return filasRetorno;
        } else {
            filasRetorno[0] = this.filas[Integer.parseInt(operacao.substring(1, 2)) - 1];
            filasRetorno[1] = this.filas[Integer.parseInt(operacao.substring(3, 4)) - 1];
            return filasRetorno;
        }
    }

    class ComparadorTiposOperacao implements Comparator<TipoOperacao> {
        public int compare(TipoOperacao o1, TipoOperacao o2) {
            if (o1.tempoOperacao < o2.tempoOperacao)
                return -1;
            else if (o1.tempoOperacao > o2.tempoOperacao)
                return +1;
            else
                return 0;
        }
    }

    // Printar uma execução da simulação por vez:
    public void printAtualizacaoFila() {
        System.out.println("--------------------");
        System.out.println("Tempo Global: " + tempo_global);
        System.out.println("Ultima chegada: " + getTempoAnteriorChegada());
        System.out.println("Ultima passagem: " + getTempoAnteriorPassagem());
        System.out.println("Ultima saída: " + getTempoAnteriorSaida());
        System.out.print("Operacao: ");
        movimentacaoToString();
        tempoTotalEProbabilidadePorQuantidadetoString();
        System.out.println("--------------------");
    }

    public void movimentacaoToString() {
        for (TipoOperacao movimenacao : agenda_movimentacao) {
            System.out.println(
                    "Movimentacao: " + movimenacao.tipoOperacao + ", Tempo operacao: " + movimenacao.tempoOperacao);
        }
    }

    public boolean agendaChegadaIsEmpty() {
        if (agenda_chegada.size() == this.K) {
            return true;
        }
        return false;
    }

    public void calculaTaxaDeVisita(ObjetoFila[] filas) {
        System.out.println("====================================================");
        System.out.println("==================TAXA DE VISITA====================");
        System.out.println("====================================================");
        String[] sFilas = new String[filas.length];
        ArrayList<TaxaVisita> taxaVisita = new ArrayList<TaxaVisita>();
        for (int i = 0; i < filas.length; i++) {
            sFilas[i] = "f" + (i + 1);
        }
        int[] v = new int[filas.length];
        for (int i = 0; i < filas.length; i++) {
            for (int j = 0; j < filas.length; j++) {
                for (int k = 0; k < filas[j].possiveisCaminhos.length; k++) {
                    if (sFilas[i].equals(filas[j].possiveisCaminhos[k].caminho)) {
                        TaxaVisita aux = new TaxaVisita(i, filas[j].possiveisCaminhos[k].probabilidade, j);
                        taxaVisita.add(aux);
                    }
                }
            }
        }
        double[] valores = new double[filas.length];
        valores[0] = 1;
        String[] sValores = new String[filas.length];
        for (TaxaVisita taxa : taxaVisita) {
            if (sValores[taxa.n_fila] == null) {
                sValores[taxa.n_fila] = "";
                sValores[taxa.n_fila] += taxa.probabilidade_rotacao + " x V" + (taxa.recebe + 1)
                        + " + ";
            } else {
                // valores[taxa.n_fila] += taxa.probabilidade_rotacao * valores[taxa.recebe];
                sValores[taxa.n_fila] += taxa.probabilidade_rotacao + " x V" + (taxa.recebe + 1)
                        + " + ";

            }
        }

        int i = 1;
        for (String valor_fila : sValores) {
            System.out.println("V" + i + "= " + valor_fila);
            i++;
        }
    }

}
