import java.sql.*;

public class InspectDb {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/tech_blog_java?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            String[] tables = {"users", "roles", "user_roles", "categories", "tags", "posts", "post_tags"};
            for (String table : tables) {
                System.out.println("TABLE=" + table);
                try (PreparedStatement ps = conn.prepareStatement("SHOW COLUMNS FROM " + table);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getString("Field") + " | " + rs.getString("Type") + " | " + rs.getString("Null") + " | " + rs.getString("Default"));
                    }
                } catch (Exception ex) {
                    System.out.println("ERR=" + ex.getMessage());
                }
                System.out.println();
            }
        }
    }
}
