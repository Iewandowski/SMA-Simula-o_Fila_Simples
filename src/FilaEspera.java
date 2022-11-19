import java.util.ArrayList;
import java.util.Random;

public class FilaEspera {

    public enum Rede {
        Aberta, Fechada;
    }

    public float T;
    private float tempo_anterior_chegada = 0;
    private float tempo_anterior_saida = 0;
    String intervalo_chegada, intervalo_atendimento;
    private float proxima_chegada;
    public int K, servidores;
    private int qntd_clientes = 0;
    static public int fila = 0;
    private float[] tempo_por_quantidade;
    public ObjetoFila[] filas;
    public Simulacao simulacao = new Simulacao();
    public Rede tipoRede;
    ArrayList<Float> agenda_chegada = new ArrayList<>();
    ArrayList<Float> agenda_saida = new ArrayList<>();
    ArrayList<Float> chegada_fila = new ArrayList<>();
    private int a, c, M;
    private float Xi = 3;

    public void FilaEspera(ObjetoFila[] filas) {
        filas = new ObjetoFila[filas.length];
        this.filas = filas;
        verificaRede(filas);
    }

    public float getTempoAnteriorChegada() {
        return tempo_anterior_chegada;
    }

    public void setTempoAnteriorChegada(float tempo) {
        this.tempo_anterior_chegada = tempo;
    }

    public float getTempoAnteriorSaida() {
        return tempo_anterior_saida;
    }

    public void setTempoAnteriorSaida(float tempo) {
        this.tempo_anterior_saida = tempo;
    }

    public float getProximaChegada() {
        return proxima_chegada;
    }

    public void setProximaChegada(float tempo) {
        this.proxima_chegada = tempo;
    }

    public void verificaRede(ObjetoFila[] filas) {
        for (int i = 0; i < filas.length; i++) {
            if (filas[i].contemChegada()) {
                this.tipoRede = Rede.Aberta;
                break;
            }
        }
        this.tipoRede = Rede.Fechada;
    }

    public Rede getTipoRede() {
        return this.tipoRede;
    }

    public float getT() {
        return T;
    }

    public void setT(float T) {
        this.T = T;
    }

    public ObjetoFila[] fila() {
        if (tipoRede == Rede.Aberta) {
            for (int i = 0; i < 10; i++) {
                if (agenda_chegada.isEmpty()) {
                    for (int j = 0; j < filas.length; i++) {

                    }
                    agendarChegada((float) 01.0);
                    chegada(getT());
                }

            }
        } else if (tipoRede == Rede.Fechada) {

        }

        return filas;
    }

    public void agendarChegada(float tempo_chegada) {
        this.agenda_chegada.add(tempo_chegada);
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
        if (fila < K) {
            fila++;
            tempo_por_quantidade[qntd_clientes] += (T - Math.max(getTempoAnteriorChegada(), getTempoAnteriorSaida()));
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

    public float getLastXi() {
        return this.Xi;
    }

    public void setLastXi(float Xi) {
        this.Xi = Xi;
    }
}
