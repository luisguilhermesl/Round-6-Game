package round6;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Luis
 */

public class BatatinhaFrita{

   private static final int DISTANCIA_TOTAL = 20;
    private int posicaoJogador;
    private boolean vivo;
    private final Random random;
    private final Scanner scanner;
    private final PlayerDAO playerDAO;
    private String playerName;
    
    public BatatinhaFrita() {
        this.posicaoJogador = 0;
        this.vivo = true;
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.playerDAO = new PlayerDAO();
    }
    
    public void inicio() {
        clearScreen();
        System.out.println("=== BATATINHA FRITA 1, 2, 3 ===");
        System.out.println("\nBem-vindo ao jogo mortal!");
        System.out.print("\nDigite seu nome: ");
        this.playerName = scanner.nextLine();
        
        System.out.println("\nRegras:");
        System.out.println("- Você precisa atravessar 20 metros sem ser pego se movendo");
        System.out.println("- Quando a boneca estiver de costas, você pode se mover");
        System.out.println("- Se você se mover quando ela estiver olhando, você morre!");
        System.out.println("\nPressione ENTER para começar...");
        scanner.nextLine();
    }
    
    private void mostrarProgresso() {
        clearScreen();
        System.out.println("\nProgresso:");
        StringBuilder progress = new StringBuilder();
        for (int i = 0; i < DISTANCIA_TOTAL; i++) {
            if (i == posicaoJogador) {
                progress.append("🏃");
            } else if (i < posicaoJogador) {
                progress.append("=");
            } else {
                progress.append("_");
            }
        }
        System.out.println(progress.toString());
        System.out.println("Metros percorridos: " + posicaoJogador + " de " + DISTANCIA_TOTAL);
    }
    
    private void jogarRodada() {
        try {
            System.out.println("\nBONECA: Batatinha frita 1, 2, 3!");
            Thread.sleep(random.nextInt(4000) + 1000);
            
            System.out.print("\nA boneca virou! Você quer se mover? (s/n): ");
            String decisao = scanner.nextLine().toLowerCase();
            
            if (decisao.equals("s")) {
                int movimento = random.nextInt(3) + 1;
                double chanceMorte = random.nextDouble();
                
                if (chanceMorte < 0.3) {
                    System.out.println("\n🔴 A boneca te pegou se movendo!");
                    System.out.println("💀 Você foi eliminado!");
                    vivo = false;
                } else {
                    posicaoJogador += movimento;
                    System.out.println("\n🟢 Você avançou " + movimento + " metros!");
                }
            }
            Thread.sleep(1500);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void jogar() {
        inicio();
        
        while (vivo && posicaoJogador < DISTANCIA_TOTAL) {
            mostrarProgresso();
            jogarRodada();
        }
        
        boolean won = vivo && posicaoJogador >= DISTANCIA_TOTAL;
        if (won) {
            System.out.println("\n🎉 PARABÉNS! Você sobreviveu e ganhou o jogo!");
        } else {
            System.out.println("\nGAME OVER");
        }
        
        // Salvar resultado no banco de dados
        playerDAO.savePlayer(playerName, won, posicaoJogador);
        
        // Mostrar ranking
        playerDAO.showLeaderboard();
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void main(String[] args) {
        BatatinhaFrita jogo = new BatatinhaFrita();
        jogo.jogar();
    }
}