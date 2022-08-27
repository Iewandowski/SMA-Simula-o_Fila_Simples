import java.util.Random;

public class App {
    public int fila = 0;
    public int K;
    public int servidores;
    public float T;
    public float tempo_cliente;

    public App(int servidores, int K) {
        this.K = K;
        this.servidores = servidores;
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
                saida(T + 1); // TROCAR 1 POR NUMERO RANDOM(3,6)
            }
        }
    }

    public void saida(float T) {
        setTempoCliente(T);
        fila--;
        if (fila >= servidores) {
            saida(T + 1); // TROCAR 1 POR NUMERO RANDOM(3,6)
        }
    }

    public static void main(String[] args) throws Exception {
        // ler e setar dados:
        // - intervalo de tempo para a chegada de clientes na fila;
        // - intervalo de tempo de atendimento de um cliente na fila;
        // - número de servidores
        // - capacidade da fila

        App app = new App(1, 3);
        app.setT(3); // primeiro cliente chega no tempo 3,0

        for (int i = 0; i < 6; i++) {
            float tempo;
            if (i == 0) {
                tempo = app.getT();
            } else {
                tempo = app.getTempoCliente();
            }
            app.chegada(tempo);
            System.out.println("Tempo do cliente " + i + ": " + app.getTempoCliente());
        }
    }
}
