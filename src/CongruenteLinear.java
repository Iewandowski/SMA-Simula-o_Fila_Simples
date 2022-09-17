import java.util.ArrayList;

public class CongruenteLinear {
    ArrayList<Float> numeros_random = new ArrayList<>();

    public void gerarNumeros( int a, int c, int M) {
        // int a = 569;
        // int c = 571;
        // int M = 397845;
        System.out.println(a + c + M + " TESTANDOO");
        float Xi = 3;
        float Ui;
        for (int i = 0; i <= 20; i++) {
            Xi = (a * Xi + c) % M;
            Ui = Xi / M;
            numeros_random.add(Ui);
        }
    }

    public ArrayList<Float> getNumeros() {
        return this.numeros_random;
    }
}
