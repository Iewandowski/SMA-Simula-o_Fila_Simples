import java.util.ArrayList;

public class ObjetoFila {

    public String intervalo_chegada;
    public String intervalo_atendimento;
    public int servidores;
    public int clientes;
    public PossivelCaminho[] possiveisCaminhos;
    public int clientesAtuais;
    private float[] tempo_por_quantidade;
    public String nomeFila;
    public ArrayList<Float> chegadas = new ArrayList<>();
    public ArrayList<Float> agendas = new ArrayList<>();
    public int servidoresOcupados = 0;

    public ObjetoFila(String intervalo_chegada, String intervalo_atendimento, int K, int servidores,
            PossivelCaminho[] possiveisCaminhos, String nomeFila) {
        this.intervalo_atendimento = intervalo_atendimento;
        this.intervalo_chegada = intervalo_chegada;
        this.servidores = servidores;
        this.clientes = K;
        this.possiveisCaminhos = possiveisCaminhos;
        this.nomeFila = nomeFila;
    }

    public float[] get_tempo_por_quantidade() {
        return tempo_por_quantidade;
    }

    public void incrementaCliente() {
        clientesAtuais++;
    }

    public void decrementaCliente() {
        clientesAtuais--;
    }

    public void incrementaServidoresOcupados() {
        servidoresOcupados++;
    }

    public void decrementaServidoresOcupados() {
        servidoresOcupados--;
    }

    public int getQntdClientesAtuais() {
        return clientesAtuais;
    }

    public void set_tempo_por_quantidade(float[] tempo_por_quantidade) {
        this.tempo_por_quantidade = tempo_por_quantidade;
    }

    public boolean contemChegada() {
        if (this.intervalo_chegada != null) {
            return true;
        }
        return false;
    }

}
