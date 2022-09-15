import java.util.ArrayList;

public class FilaSimples {
    static public int fila = 0;
    public int K, servidores;
    public float tempo_cliente, T;
    String intervalo_chegada, intervalo_atendimento;
    ArrayList<Float> numeros_random;
    CongruenteLinear cl = new CongruenteLinear();
    static int contador = 0;
    ArrayList<Float> agenda_chegada = new ArrayList<>();
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
        this.tempo_por_quantidade = new float[this.K + 1];
        this.servidores = servidores;
        cl.gerarNumeros();
        numeros_random = cl.getNumeros();
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
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int numero) {
        this.fila = numero;
    }

    public void chegada(float T) {
        setT(T);
        float tempoAnteriorChegada = getTempoAnteriorChegada();
        setTempoAnteriorChegada(T);
        if (fila <= K) {
            fila++;
            tempo_por_quantidade[qntd_clientes] += ( T - Math.max(tempoAnteriorChegada, getTempoAnteriorSaida()));
            qntd_clientes++;
            printaTempoTotalPorQuantidade();
            setTempoGlobal(T);
            if (fila <= servidores) {
                float proxima_saida = T + gerarRandom(intervalo_atendimento);
                // setProximaSaida(aux);
                agenda_saida.add(proxima_saida);
            }
            float proxima_chegada = T + gerarRandom(intervalo_chegada);
            setProximaChegada(proxima_chegada);
            agenda_chegada.add(proxima_chegada);
        }
    }

    public void saida(float T) {
        float aux_tempo;
        setT(T);
        aux_tempo = T;
        tempo_por_quantidade[qntd_clientes] += ( T - Math.max(getTempoAnteriorChegada(),getTempoAnteriorSaida()));
        System.out.println(agenda_chegada.size() + "tamanhooo");
        qntd_clientes--;
        System.out.println("TEMPO SAIDA: TESTE" + Math.max(getTempoAnteriorChegada(),getTempoAnteriorSaida()) + " -- " + T);
        System.out.println("VAPO"+ fila);
        setTempoAnteriorSaida(aux_tempo);
        setTempoGlobal(T);
        fila--;
        agenda_chegada.remove(0);
        agenda_saida.remove(0);
        printaTempoTotalPorQuantidade();
        if (fila >= servidores) {
            agenda_saida.add(T + gerarRandom(intervalo_atendimento));
        }
    }

    public float gerarRandom(String intervalo) {
        float U;
        String[] split = intervalo.split(",");
        int A = Integer.parseInt(split[0]);
        int B = Integer.parseInt(split[1]);
        U = (B - A) * numeros_random.get(0) + A;
        numeros_random.remove(0);
        return U;
    }

    public void agendaChegada(float chegada) {
        this.agenda_chegada.add(chegada);
    }

    public void AgendaChegadaToString() {
        for (float chegada : agenda_chegada) {
            System.out.println("CHEGADA: " + chegada);
        }
    }

    public void AgendaSaidaToString() {
        for (float saida : agenda_saida) {
            System.out.println("SAIDA: " + saida);
        }
    }

    public void printaTempoTotalPorQuantidade() {
        int index = 0;
        for (float tempo_por_quantidade : tempo_por_quantidade) {
            System.out.println("Tempo total com " + index + " clientes na fila: " + tempo_por_quantidade);
            index ++;
        }
    }

    public void fila() {
        while (!numeros_random.isEmpty()) {
            if (agenda_chegada.isEmpty()) {
                agendaChegada(3);
                chegada(getT());
            }
            System.out.println("--------------------");
            System.out.println("Tempo Global: " + tempo_global);
            System.out.println("Ultima chegada: " + getTempoAnteriorChegada());
            System.out.println("Ultima saída: " + getTempoAnteriorSaida());
            AgendaChegadaToString();
            AgendaSaidaToString();
            System.out.println("--------------------");
            // System.out.println(getFila());
            if (verificaSaida() == true) {
                saida(agenda_saida.get(0));
            } else {
                if (getFila() < this.K) {
                    chegada(getProximaChegada());
                }
                /*
                 * else {
                 * float aux = agenda_chegada.get(agenda_chegada.indexOf(getProximaChegada()) +
                 * 1);
                 * setProximaChegada(aux);
                 * }
                 */
            }
        }
        System.out.println(tempo_global + "tempo total");
        // printaTempoTotalPorQuantidade();
    }

    public boolean agendaChegadaIsEmpty() {
        if (agenda_chegada.size() == this.K) {
            return true;
        }
        return false;
    }

    public boolean verificaSaida() {
        if (agendaChegadaIsEmpty() == false) {
            if (getProximaChegada() > agenda_saida.get(0)) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
