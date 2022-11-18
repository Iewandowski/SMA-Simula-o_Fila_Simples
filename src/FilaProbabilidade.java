import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class FilaProbabilidade {
    public int fila = 0;
    public int fila_dois = 0;
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

    public FilaProbabilidade(ObjetoFila[] filas) {
        this.filas = filas;
        this.geradorNumeros = new Random();
        this.a = Math.abs(geradorNumeros.nextInt(100000) + 100);
        this.c = Math.abs(geradorNumeros.nextInt(100000) + 100);
        this.M = Math.abs(geradorNumeros.nextInt(100000) + 100);
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

    public void setTempoCliente(float tempo) {
        this.tempo_cliente = tempo;
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

    public int getFila() {
        return fila;
    }

    public void setFila(int numero) {
        this.fila = numero;
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
        agenda_movimentacao.add(new TipoOperacao(proxima_chegada, "CH1"));
    }

    public void chegada(float T) {
        setT(T);
        if (filas[0].getQntdClientesAtuais() < filas[0].clientes) {
            setaTempoFilas(T);
            filas[0].incrementaCliente();
            filas[0].chegadas.add(T);
            setTempoAnteriorChegada(T);
            agenda_chegada.remove(0);
            if (filas[0].clientesAtuais <= filas[0].servidores) {
                float proxima_passagem = T + gerarRandom(filas[0].intervalo_atendimento);
                agenda_movimentacao.add(new TipoOperacao(proxima_passagem, calculaProximaOperacao(filas[0])));
            }
            agendarProximaChegada();
        } else if (agenda_movimentacao.size() < filas[0].servidores) {
            float proxima_passagem = T + gerarRandom(filas[0].intervalo_atendimento);
            agenda_chegada.remove(0);
            agenda_movimentacao.add(new TipoOperacao(proxima_passagem, calculaProximaOperacao(filas[0])));
            agendarProximaChegada();
        } else {
            agendarProximaChegada();
        }
    }

    public String calculaProximaOperacao(ObjetoFila fila) {
        float numeroGerado = geradorNumeros.nextFloat();
        for (int i = 0; i < fila.possiveisCaminhos.length; i++) {
            if (numeroGerado < fila.possiveisCaminhos[i].probabilidade) {
                return fila.nomeFila.concat(fila.possiveisCaminhos[i].caminho);
            }
            if (numeroGerado < (fila.possiveisCaminhos[i].probabilidade
                    + fila.possiveisCaminhos[i + 1].probabilidade)) {
                return fila.nomeFila.concat(fila.possiveisCaminhos[i].caminho);
            }
        }
        return null;
    }

    public void passagemFila(float T, ObjetoFila filaOrigem, ObjetoFila filaDestino) {
        System.out.println("Entrei aqui Passagem: " + T );
        float aux_tempo;
        setT(T);
        setaTempoFilas(T);
        filaOrigem.decrementaCliente();
        agenda_movimentacao.remove(0);
        filaOrigem.chegadas.remove(0);
        if (filaDestino.getQntdClientesAtuais() < filaDestino.clientes) {
            filaDestino.incrementaCliente();
            aux_tempo = T;
            filaDestino.chegadas.add(T);
            setTempoAnteriorPassagem(aux_tempo);
            if (filaDestino.clientesAtuais <= filaDestino.servidores) {
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
        System.out.println("Entrei aqui saida: " + T );
        setT(T);
        setaTempoFilas(T);
        setTempoAnteriorSaida(T);
        fila.clientesAtuais--;
        fila.chegadas.remove(0);
        if (fila.clientes >= fila.servidores) {
            agenda_movimentacao.add(
                    new TipoOperacao(T + gerarRandom(intervalo_atendimento_fila_dois), calculaProximaOperacao(fila)));
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
            System.out.println("\t\t\tFila " + i);
            System.out.println("****************************************************");
            System.out.println("****************************************************");
            System.out.println("Tempo Total: " + tempo_global);
            System.out.println("****************************************************");
            System.out.println("Estado\t\tTempo\t\t\tProbabilidade");
            System.out.println("====================================================");
            System.out.println();
            int index = 0;
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

    public ObjetoFila[] fila(int numeroExecucao) {
        if (numeroExecucao >= 1) {
            zerarAlgoritmo();
        }
        for (int i = 0; i < filas.length; i++) {
            this.filas[i].set_tempo_por_quantidade(new float[this.filas[i].clientes + 1]);
        }

        System.out.println("====================================================");
        System.out.println("================EXECUÇÃO NUMERO " + (numeroExecucao + 1) + "===================");
        System.out.println("====================================================");

        for (int i = 0; i < 10000; i++) {
            Collections.sort(agenda_movimentacao, new ComparadorTiposOperacao());
            if (agenda_chegada.isEmpty()) {
                System.out.println("Entrei aqui riri");
                agendaChegada((float) 1.0);
                chegada(getT());
            }
            // printaOrdemAgenda(agenda_movimentacao, i);
            if (agenda_movimentacao.get(0).tipoOperacao.contains("sa")
                    && agenda_movimentacao.get(0).tempoOperacao < getProximaChegada()) {
                        System.out.println("Entrei aqui SAIDA");

                saida(agenda_movimentacao.get(0).tempoOperacao,
                        retornaFilasOperacao(agenda_movimentacao.get(0).tipoOperacao, true)[0]);
            }
            if (!agenda_movimentacao.get(0).tipoOperacao.toLowerCase().contains("sa")
                    && !agenda_movimentacao.get(0).tipoOperacao.toLowerCase().contains("ch")
                    && agenda_movimentacao.get(0).tempoOperacao < getProximaChegada()) {
                        System.out.println("Entrei aqui Passagem");
                ObjetoFila[] filasPassagem = retornaFilasOperacao(agenda_movimentacao.get(0).tipoOperacao, false);
                passagemFila(agenda_movimentacao.get(0).tempoOperacao, filasPassagem[0], filasPassagem[1]);
            } else {
                chegada(getProximaChegada());
            }
        }
        setTempoGlobal(T);
        tempoTotalEProbabilidadePorQuantidadetoString();
        return filas;
    }

    public void printaOrdemAgenda(ArrayList<TipoOperacao> tipos , int laco){
        System.out.println("LACO : " + laco);
        for(int i = 0; i< tipos.size(); i++){
            System.out.println(tipos.get(i).tempoOperacao + " --- " + tipos.get(i).tipoOperacao);
        }
    }

    public ObjetoFila[] retornaFilasOperacao(String operacao, boolean isSaida) {
        ObjetoFila[] filasRetorno = new ObjetoFila[2];
        if (isSaida) {
            filasRetorno[0] = this.filas[Integer.parseInt(operacao.substring(1, 2)) - 1];
            return filas;
        } else {
            filasRetorno[0] = this.filas[Integer.parseInt(operacao.substring(1, 2)) - 1];
            filasRetorno[1] = this.filas[Integer.parseInt(operacao.substring(3, 4)) - 1];
            System.out.println(filas[0].nomeFila);
            return filas;
        }
    }

    class ComparadorTiposOperacao implements Comparator<TipoOperacao> {
        public int compare(TipoOperacao o1, TipoOperacao o2) {
            if (o1.tempoOperacao < o2.tempoOperacao) return -1;
            else if (o1.tempoOperacao > o2.tempoOperacao) return +1;
            else return 0;
        }
    }

    // Printar uma execução da simulação por vez:
    public void printAtualizacaoFila() {
        System.out.println("--------------------");
        System.out.println("Tempo Global: " + tempo_global);
        System.out.println("Ultima chegada: " + getTempoAnteriorChegada());
        System.out.println("Ultima passagem: " + getTempoAnteriorPassagem());
        System.out.println("Ultima saída: " + getTempoAnteriorSaida());
        System.out.println("--------------------");
    }

    public boolean agendaChegadaIsEmpty() {
        if (agenda_chegada.size() == this.K) {
            return true;
        }
        return false;
    }
}
