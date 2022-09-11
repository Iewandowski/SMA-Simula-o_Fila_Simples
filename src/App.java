import java.util.Scanner;
import java.text.DecimalFormat;

public class App {

    public static void main(String[] args) throws Exception {
        DecimalFormat format = new DecimalFormat("0.00");
        Scanner scanner = new Scanner(System.in);

        // ler intervalo de tempo para a chegada de clientes na fila:
        System.out.print("Digite o intervalo de tempo para a chegada de cliente: (numero,numero) ");
        String intervalo_chegada = scanner.next();

        // ler intervalo de tempo de atendimento de um cliente na fila:
        System.out.print("Digite o intervalo de tempo de atendimento para um cliente: (numero,numero) ");
        String intervalo_atendimento = scanner.next();

        // ler número de servidores:
        System.out.print("Digite o número de servidores: ");
        int servidores = scanner.nextInt();

        // ler a capacidade da fila:
        System.out.print("Digite a capacidade da fila: ");
        int K = scanner.nextInt();
        scanner.close();

        FilaSimples filaSimples = new FilaSimples(intervalo_chegada, intervalo_atendimento, servidores, K);
        filaSimples.setT(3); // primeiro cliente chega no tempo 3.0
        filaSimples.fila();
    }
}
