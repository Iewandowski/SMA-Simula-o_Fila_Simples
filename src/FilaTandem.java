import java.util.ArrayList;
import java.util.Random;

public class FilaTandem {
    static public int fila = 0;
    static public int fila_dois = 0;
    public int K, servidores, servidores_fila_dois, K_fila_dois;
    public float tempo_cliente, T;
    String intervalo_chegada, intervalo_atendimento, intervalo_atendimento_fila_dois, intervalo_chegada_fila_dois;
    ArrayList<Float> numeros_random;
    CongruenteLinear cl = new CongruenteLinear();
    static int contador = 0;
    ArrayList<Float> agenda_chegada = new ArrayList<>();
    ArrayList<Float> chegada_fila = new ArrayList<>();
    ArrayList<Float> chegada_passagem = new ArrayList<>();
    ArrayList<Float> agenda_saida = new ArrayList<>();
    ArrayList<Float> agenda_passagem = new ArrayList<>();
    private float[] tempo_por_quantidade;
    private float[] tempo_por_quantidade_fila_dois;
    private float tempo_global = 0;
    private float tempo_anterior_chegada = 0;
    private float tempo_anterior_saida = 0;
    private float tempo_anterior_passagem = 0;
    private float proxima_chegada;
    private float proxima_saida;
    private float proxima_passagem;
    private int qntd_clientes = 0;
    private int qntd_clientes_fila_dois = 0;

    public FilaTandem(String intervalo_chegada, String intervalo_atendimento, int servidores, int K,
            String intervalo_atendimento_fila_dois, int servidores_fila_dois,
            int K_fila_dois) {
        this.intervalo_chegada = intervalo_chegada;
        this.intervalo_atendimento = intervalo_atendimento;
        this.K = K;
        this.servidores = servidores;
        this.intervalo_atendimento_fila_dois = intervalo_atendimento_fila_dois;
        this.servidores_fila_dois = servidores_fila_dois;
        this.K_fila_dois = K_fila_dois;
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

    public float getProximaPassagem() {
        return agenda_passagem.size() > 0 ? agenda_passagem.get(0) : 1000000000;
    }

    public void setProximaPassagem(float prox_passagem) {
        this.proxima_passagem = prox_passagem;
    }

    public float getProximaSaida() {
        return agenda_saida.size() > 0 ? agenda_saida.get(0) : 1000000000;
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

    public void zerarAlgoritmo() {
        this.tempo_global = 0;
        this.tempo_anterior_chegada = 0;
        this.tempo_anterior_saida = 0;
        this.proxima_chegada = 0;
        this.qntd_clientes = 0;
        this.proxima_saida = 0;
        this.proxima_passagem = 0;
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

    public void agendarProximaPassagem() {
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
            tempo_por_quantidade_fila_dois[qntd_clientes_fila_dois] += (T
                    - Math.max(getTempoAnteriorChegada(),
                            Math.max(getTempoAnteriorSaida(), getTempoAnteriorPassagem())));
            qntd_clientes++;
            chegada_fila.add(T);
            setTempoAnteriorChegada(T);
            if (fila <= servidores) {
                float proxima_passagem = T + gerarRandom(intervalo_atendimento);
                setProximaPassagem(proxima_passagem);
                agenda_passagem.add(proxima_passagem);
            }
            agendarProximaChegada();
        } else if (chegada_passagem.size() < servidores) {
            float proxima_passagem = T + gerarRandom(intervalo_atendimento);
            setProximaPassagem(proxima_passagem);
            agenda_passagem.add(proxima_passagem);
            agendarProximaChegada();
        } else {
            agendarProximaChegada();
        }
    }

    public void passagemFilaUmDois(float T) {
        float aux_tempo;
        setT(T);
        fila--;
        tempo_por_quantidade[qntd_clientes] += (T
                - Math.max(getTempoAnteriorChegada(), Math.max(getTempoAnteriorSaida(), getTempoAnteriorPassagem())));
        tempo_por_quantidade_fila_dois[qntd_clientes_fila_dois] += (T
                - Math.max(getTempoAnteriorChegada(), Math.max(getTempoAnteriorSaida(), getTempoAnteriorPassagem())));
        qntd_clientes--;
        agenda_passagem.remove(0);
        chegada_fila.remove(0);
        if (fila_dois < K_fila_dois) {
            fila_dois++;
            aux_tempo = T;
            qntd_clientes_fila_dois++;
            chegada_passagem.add(T);
            setTempoAnteriorPassagem(aux_tempo);
            if (fila_dois <= servidores_fila_dois) {
                agenda_saida.add(T + gerarRandom(intervalo_atendimento_fila_dois));
            }
        }
    }

    public void saida(float T) {
        float aux_tempo;
        setT(T);
        aux_tempo = T;
        tempo_por_quantidade[qntd_clientes] += (T
                - Math.max(getTempoAnteriorChegada(), Math.max(getTempoAnteriorSaida(), getTempoAnteriorPassagem())));
        tempo_por_quantidade_fila_dois[qntd_clientes_fila_dois] += (T
                - Math.max(getTempoAnteriorChegada(), Math.max(getTempoAnteriorSaida(), getTempoAnteriorPassagem())));
        qntd_clientes_fila_dois--;
        setTempoAnteriorSaida(aux_tempo);
        fila_dois--;
        chegada_passagem.remove(0);
        agenda_saida.remove(0);
        if (fila_dois >= servidores_fila_dois) {
            agenda_saida.add(T + gerarRandom(intervalo_atendimento_fila_dois));
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

    public void passagemFilaToString() {
        for (float passagem : this.chegada_passagem) {
            System.out.println("Passagem: " + passagem);
        }
    }

    public void agendaPassagemFilaToString() {
        for (float passagem : this.agenda_chegada) {
            System.out.println("Agenda: " + passagem);
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
        this.tempo_por_quantidade_fila_dois = new float[this.K_fila_dois + 1];

        System.out.println("====================================================");
        System.out.println("================EXECUÇÃO NUMERO " + (numeroExecucao + 1) + "===================");
        System.out.println("====================================================");

        while (!numeros_random.isEmpty()) {
            printAtualizacaoFila();
            if (agenda_chegada.isEmpty()) {
                agendaChegada((float) 2.5);
                chegada(getT());
            }
            if (verificaSaida() == true) {
                saida(agenda_saida.get(0));
            }
            if (verificaPassagemDeFila() == true) {
                passagemFilaUmDois(agenda_passagem.get(0));
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
        System.out.println("Ultima passagem: " + getTempoAnteriorPassagem());
        System.out.println("Ultima saída: " + getTempoAnteriorSaida());
        chegadaFilaToString();
        AgendaSaidaToString();
        passagemFilaToString();
        agendaPassagemFilaToString();
        System.out.println("--------------------");
    }

    public boolean agendaChegadaIsEmpty() {
        if (agenda_chegada.size() == this.K) {
            return true;
        }
        return false;
    }

    public boolean verificaSaida() {
        if (agendaChegadaIsEmpty() == false && agenda_saida.size() > 0 && chegada_passagem.size() > 0) {
            if (getProximaPassagem() > getProximaSaida() && getProximaChegada() > getProximaSaida()) {
                return true;
            }
        }
        return false;
    }

    public boolean verificaPassagemDeFila() {
        if (agendaChegadaIsEmpty() == false && agenda_passagem.size() > 0) {
            // System.out.println("AQUI ENTREI TESTE: " + getProximaPassagem()+ " -- " +
            // getProximaSaida());
            if ((getProximaPassagem() < getProximaSaida() || agenda_saida.size() == 0)
                    && getProximaChegada() > getProximaPassagem()) {
                // System.out.println("ENTREI AQUII ksakasksa");
                return true;
            }
        }
        return false;
    }
}
