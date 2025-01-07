package round6;
import java.sql.*;

public class PlayerDAO {
    public void savePlayer(String playerName, boolean won, int score) {
        String sql = "INSERT INTO players (player_name, games_played, games_won, highest_score) " +
                    "VALUES (?, 1, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "games_played = games_played + 1, " +
                    "games_won = games_won + ?, " +
                    "highest_score = GREATEST(highest_score, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, playerName);
            stmt.setInt(2, won ? 1 : 0);
            stmt.setInt(3, score);
            stmt.setInt(4, won ? 1 : 0);
            stmt.setInt(5, score);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void showLeaderboard() {
        String sql = "SELECT player_name, games_played, games_won, highest_score " +
                    "FROM players ORDER BY highest_score DESC LIMIT 5";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n=== RANKING DOS JOGADORES ===");
            System.out.println("Nome | Jogos | Vitórias | Maior Pontuação");
            System.out.println("------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%s | %d | %d | %d%n",
                    rs.getString("player_name"),
                    rs.getInt("games_played"),
                    rs.getInt("games_won"),
                    rs.getInt("highest_score")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
