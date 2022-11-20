public class TaxaVisita {
    public int n_fila, recebe;
    public double probabilidade_rotacao, taxa;

    public TaxaVisita(int n_fila, double probabilidade_rotacao, int recebe) {
        this.n_fila = n_fila;
        this.probabilidade_rotacao = probabilidade_rotacao;
        this.recebe = recebe;
    }

    public TaxaVisita() {

    }

    public double getTaxa() {
        return this.taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }
}
