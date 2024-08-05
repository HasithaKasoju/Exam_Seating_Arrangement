import java.sql.*;  
import java.util.Scanner;  
  
public class Tablesswith {  
  
              public static void main(String[] args)  
   {  
        Scanner sc = new Scanner(System.in);  
        boolean running = true;  
  
        while (running) {  
            System.out.println("Select option 1 for the admin and select option 2 for the student (Enter ’exit’ to 
quit): ");  
            String ifadmin = sc.next();  
  
            switch (ifadmin) {  
                case "1":  
                    System.out.println("Enter 1 to get student details: ");  
                    System.out.println("Enter 2 to arrange students based on given data: ");  
                    String show = sc.next();  
  
                    if (show.equals("1")) {  
                        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentd
 etails", "root", "hasitha")) {  
                            StudentsData(con);   
                        } catch (SQLException e) {  
                            e.printStackTrace();  
                        }  
                    } else if (show.equals("2")) {  
                        System.out.println("Enter the number of classes: ");  
                        int numberOfClasses = sc.nextInt();  
                        System.out.println("Enter the number of benches: ");  
                        int numberOfBenches = sc.nextInt();  
                       
                        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentd
 etails", "root", "hasitha")) {  
                            createAndInsertData(con, numberOfClasses, numberOfBenches);  
                        } catch (SQLException e) {  
                            e.printStackTrace();  
                        }  
                    } else {  
                        System.out.println("Invalid option");  
                    }  
                    break;  
  
                case "2":  
                    System.out.println("Enter the roll number (Enter ’exit’ to quit): ");  
                    String userRollNumber = sc.next();  
                    if (userRollNumber.equalsIgnoreCase("exit")) {  
                        running = false;  
                        break;  
                    }  
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdet
 ails", "root", "hasitha")) {  
                        fetchClassAndBench(con, userRollNumber);  
                    } catch (SQLException e) {  
                        e.printStackTrace();  
                    }  
                    break;  
  
                case "exit":  
                    running = false;  
                    break;  
  
                default:  
                    System.out.println("No change");  
                    break;  
            }  
        }  
  
        sc.close();  
    }  
    private static void createAndInsertData(Connection con, int numberOfClasses, int numberOfBenches) t
 hrows SQLException {  
        try (Statement stmt = con.createStatement()) {  
            stmt.executeUpdate("DROP TABLE IF EXISTS class_bench2");  
            stmt.executeUpdate("CREATE TABLE class_bench2 (id INT AUTO_INCREMENT PRIMARY KEY
 , class_no INT, bench_no INT)");  
  
            String insertQuery = "INSERT INTO class_bench2 (class_no, bench_no) VALUES (?, ?)";  
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {  
                for (int i = 100; i <= 100+numberOfClasses; i++) {  
                    for (int j = 1; j <= numberOfBenches; j++) {  
                        preparedStatement.setInt(1, i);  
                        preparedStatement.setInt(2, j);  
                        preparedStatement.executeUpdate();  
                    }  
                }  
            }  
            System.out.println("students are arranged as per the given details ");  
  
            String selectQuery = "SELECT examsseatingarrangement.roll_no, examsseatingarrangement.se
 mester, examsseatingarrangement.year, class_bench2.bench_no, class_bench2.class_no "  
                                 + "FROM examsseatingarrangement "  
                                 + "JOIN class_bench2 ON examsseatingarrangement.id = class_bench2.id";  
  
            try (Statement statement = con.createStatement();  
                 ResultSet resultSet = statement.executeQuery(selectQuery)) {  
  
                ResultSetMetaData metaData = resultSet.getMetaData();  
                int columnCount = metaData.getColumnCount();  
  
        
                for (int i = 1; i <columnCount; i++) {  
                    System.out.print(metaData.getColumnName(i) + "\t");  
                }  
                System.out.println();  
  
                  
                while (resultSet.next()) {  
                    for (int i = 1; i <=columnCount; i++) {  
                        System.out.print(resultSet.getString(i) + "\t");  
                    }  
                    System.out.println();  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private static void fetchClassAndBench(Connection con, String userRollNumber) throws SQLException
 {  
        String selectQuery = "SELECT class_bench2.class_no, class_bench2.bench_no FROM class_benc
 h2 "  
                           + "JOIN examsseatingarrangement ON class_bench2.id = examsseatingarrangement.id 
"  
                           + "WHERE examsseatingarrangement.roll_no= ?";  
  
        try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {  
            preparedStatement.setString(1, userRollNumber);  
  
            try (ResultSet resultSet = preparedStatement.executeQuery()) {  
                if (resultSet.next()) {  
                    int classNumber = resultSet.getInt("class_no");  
                    int benchNumber = resultSet.getInt("bench_no");  
  
                    System.out.println("Class Number: " + classNumber);  
                    System.out.println("Bench Number: " + benchNumber);  
                } else {  
                    System.out.println("Roll Number not found in the database.");  
                }  
            }  
        }  
    }  
  
    public static void StudentsData(Connection con) {  
        String selectQuery = "SELECT * FROM examsseatingarrangement";   
        try (Statement statement = con.createStatement();  
             ResultSet resultSet = statement.executeQuery(selectQuery)) {  
  
            ResultSetMetaData metaData = resultSet.getMetaData();  
            int columnCount = metaData.getColumnCount();  
  
            // Print column names  
            for (int i = 1; i <= columnCount; i++) {  
                System.out.print(metaData.getColumnName(i) + "\t");  
            }  
            System.out.println();  
  
            // Print rows  
            while (resultSet.next()) {  
                for (int i = 1; i <= columnCount; i++) {  
                    System.out.print(resultSet.getString(i) + "\t");  
                }  
}  
System.out.println();  
} catch (SQLException e) {  
e.printStackTrace();  
}  
}  
}