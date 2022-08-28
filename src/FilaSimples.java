import java.util.ArrayList;

public class FilaSimples {
    public int fila = 0;
    public int K, servidores;
    public float tempo_cliente, T;
    String intervalo_chegada, intervalo_atendimento;
    ArrayList<Float> numeros_random;
    CongruenteLinear cl = new CongruenteLinear();

    public FilaSimples(String intervalo_chegada, String intervalo_atendimento, int servidores, int K) {
        this.intervalo_chegada = intervalo_chegada;
        this.intervalo_atendimento = intervalo_atendimento;
        this.K = K;
        this.servidores = servidores;
        cl.gerarNumeros();
        numeros_random = cl.getNumeros();
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

    public void chegada(float T) {
        if (fila < K) {
            fila++;
            if (fila <= servidores) {
                saida(T + gerarRandom(intervalo_chegada));
            }
        }
    }

    public void saida(float T) {
        fila--;
        setTempoCliente(T);
        if (fila >= servidores) {
            saida(T + gerarRandom(intervalo_atendimento));
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

}
