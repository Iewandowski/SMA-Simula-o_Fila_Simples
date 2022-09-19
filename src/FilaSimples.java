import java.util.ArrayList;
import java.util.Random;

public class FilaSimples {
    static public int fila = 0;
    public int K, servidores;
    public float tempo_cliente, T;
    String intervalo_chegada, intervalo_atendimento;
    ArrayList<Float> numeros_random;
    CongruenteLinear cl = new CongruenteLinear();
    static int contador = 0;
    ArrayList<Float> agenda_chegada = new ArrayList<>();
    ArrayList<Float> chegada_fila = new ArrayList<>();
    ArrayList<Float> agenda_saida = new ArrayList<>();
    private float[] tempo_por_quantidade;
    private float tempo_global = 0;
    private float tempo_anterior_chegada = 0;
    private float tempo_anterior_saida = 0;
    private float proxima_chegada;
    private int qntd_clientes = 0;

    public FilaSimples(String intervalo_chegada, String intervalo_atendimento, int servidores, int K) {
        this.intervalo_chegada = intervalo_chegada;
        this.intervalo_atendimento = intervalo_atendimento;
        this.K = K;
        this.servidores = servidores;
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

    public void setProximaChegada(float tempo) {
        this.proxima_chegada = tempo;
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

    public void zerarAlgoritmo() {
        this.tempo_global = 0;
        this.tempo_anterior_chegada = 0;
        this.tempo_anterior_saida = 0;
        this.proxima_chegada = 0;
        this.qntd_clientes = 0;
        this.fila = 0;
        this.agenda_chegada = new ArrayList<>();
        this.chegada_fila = new ArrayList<>();
        this.agenda_saida = new ArrayList<>();
    }

    public void agendarProximaChegada() {
        if (agenda_chegada.size() >= 1) {
            agenda_chegada.remove(0);
        }
        float proxima_chegada = T + gerarRandom(intervalo_chegada);
        setProximaChegada(proxima_chegada);
        agenda_chegada.add(proxima_chegada);
    }

    public void chegada(float T) {
        setT(T);
        float tempoAnteriorChegada = getTempoAnteriorChegada();
        if (fila < K) {
            fila++;
            tempo_por_quantidade[qntd_clientes] += (T - Math.max(tempoAnteriorChegada, getTempoAnteriorSaida()));
            qntd_clientes++;
            chegada_fila.add(T);
            setTempoAnteriorChegada(T);
            if (fila <= servidores) {
                float proxima_saida = T + gerarRandom(intervalo_atendimento);
                agenda_saida.add(proxima_saida);
            }
            agendarProximaChegada();
        } else {
            agendarProximaChegada();
        }
    }

    public void saida(float T) {
        float aux_tempo;
        setT(T);
        aux_tempo = T;
        tempo_por_quantidade[qntd_clientes] += (T - Math.max(getTempoAnteriorChegada(), getTempoAnteriorSaida()));
        qntd_clientes--;
        setTempoAnteriorSaida(aux_tempo);
        fila--;
        chegada_fila.remove(0);
        agenda_saida.remove(0);
        if (fila >= servidores) {
            agenda_saida.add(T + gerarRandom(intervalo_atendimento));
        }
    }

    public float gerarRandom(String intervalo) {
        float U;
        String[] split = intervalo.split("[..]+");
        int A = Integer.parseInt(split[0]);
        int B = Integer.parseInt(split[1]);
        if (numeros_random.size() == 0) {
            return 0;
        }
        U = (B - A) * numeros_random.get(0) + A;
        numeros_random.remove(0);
        return U;
    }

    public void agendaChegada(float chegada) {
        this.agenda_chegada.add(chegada);
    }

    public void chegadaFilaToString() {
        for (float chegada : this.chegada_fila) {
            System.out.println("CHEGADA: " + chegada);
        }
    }

    public void AgendaSaidaToString() {
        for (float saida : agenda_saida) {
            System.out.println("SAIDA: " + saida);
        }
    }

    public void tempoTotalEProbabilidadePorQuantidadetoString() {
        int index = 0;
        System.out.println("****************************************************");
        System.out.println("Tempo Total: " + tempo_global);
        System.out.println("****************************************************");
        System.out.println("Estado\t\tTempo\t\t\tProbabilidade");
        for (float tempo_por_quantidade : tempo_por_quantidade) {
            double prob = (tempo_por_quantidade / tempo_global) * 100;
            String probFormat = String.format("%.4f", prob);
            System.out.println(
                    index + "\t\t" + tempo_por_quantidade + "\t\t" + probFormat
                            + "%");
            index++;
        }
        System.out.println("====================================================");
        System.out.println();
    }

    public float[] fila(int numeroExecucao) {
        if (numeroExecucao >= 1) {
            zerarAlgoritmo();
        }
        Random geradorNumeros = new Random();
        cl.gerarNumeros(Math.abs(geradorNumeros.nextInt(100000) + 100), Math.abs(geradorNumeros.nextInt(100000) + 100),
                Math.abs(geradorNumeros.nextInt(100000) + 100));
        numeros_random = cl.getNumeros();
        this.tempo_por_quantidade = new float[this.K + 1];

        System.out.println("====================================================");
        System.out.println("================EXECUÇÃO NUMERO " + (numeroExecucao + 1) + "===================");
        System.out.println("====================================================");

        while (!numeros_random.isEmpty()) {
            if (agenda_chegada.isEmpty()) {
                agendaChegada(3);
                chegada(getT());
            }
            if (verificaSaida() == true) {
                saida(agenda_saida.get(0));
            } else {
                chegada(getProximaChegada());
            }
        }
        tempoTotalEProbabilidadePorQuantidadetoString();
        return tempo_por_quantidade;
    }

    // Printar uma execução da simulação por vez:
    public void printAtualizacaoFila() {
        System.out.println("--------------------");
        System.out.println("Tempo Global: " + tempo_global);
        System.out.println("Ultima chegada: " + getTempoAnteriorChegada());
        System.out.println("Ultima saída: " + getTempoAnteriorSaida());
        chegadaFilaToString();
        AgendaSaidaToString();
        System.out.println("--------------------");
    }

    public boolean agendaChegadaIsEmpty() {
        if (agenda_chegada.size() == this.K) {
            return true;
        }
        return false;
    }

    public boolean verificaSaida() {
        if (agendaChegadaIsEmpty() == false && agenda_saida.size() > 0) {
            if (getProximaChegada() > agenda_saida.get(0)) {
                return true;
            }
        }
        return false;
    }
}
