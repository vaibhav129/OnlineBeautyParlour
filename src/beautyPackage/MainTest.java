package beautyPackage;
import static org.junit.Assert.*;

import org.junit.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainTest {
    private Main app;
    Main main = new Main();

    private Connection mockConnection;

    @Before
    public void setUp() throws SQLException {
        app = new Main();
        mockConnection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @After
    public void tearDown() throws SQLException {
        if (mockConnection != null) {
            mockConnection.close();
        }
    }

    @Test
    public void testRegisterCustomer() throws SQLException {
        String input = "John Doe\njohndoe@example.com\n1234567890\n123 Main St\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.registerCustomer(mockConnection, mockScanner);

        try (PreparedStatement statement = mockConnection.prepareStatement("SELECT * FROM customers WHERE name = ?")) {
            statement.setString(1, "John Doe");
            ResultSet resultSet = statement.executeQuery();

            
            assertTrue(resultSet.next());

           
            assertEquals("johndoe@example.com", resultSet.getString("email"));
            assertEquals("1234567890", resultSet.getString("phone"));
            assertEquals("123 Main St", resultSet.getString("address"));
        }
    }

    @Test
    public void testViewAppointmentHistory() throws SQLException {
        
        String input = "1\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        
        main.viewAppointmentHistory(mockConnection, mockScanner);

        
        System.setOut(originalOut);

        
        String capturedOutput = outputStream.toString();

        assertTrue(capturedOutput.contains("Id: 1, Beautician Id:"));
        assertTrue(capturedOutput.contains("Service Id:"));
        assertTrue(capturedOutput.contains("Date:"));
        assertTrue(capturedOutput.contains("Time:"));
    }
    
    @Test
    public void testBookAppointment() throws SQLException {
        
        String input = "1\n1\n2023-08-20\n10:00\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

      
        main.bookAppointment(mockConnection, mockScanner);

        
        try (Statement statement = mockConnection.createStatement()) {
            
            String sql = "SELECT COUNT(*) FROM appointments";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            int appointmentCount = resultSet.getInt(1);
            assertEquals(1, appointmentCount);
        }
    }
    
    @Test
    public void testViewBeauticians() {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        main.viewBeauticians(mockConnection, new Scanner(System.in));

        System.setOut(originalOut);
        String capturedOutput = outputStream.toString();

        assertTrue(capturedOutput.contains("Id: 1, Name: Purushottam, Specialization: Hair Stylist"));
        assertTrue(capturedOutput.contains("Id: 2, Name: Vishal, Specialization: Makeup Artist"));
    }
    
    @Test
    public void testViewServices() {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        main.viewServices(mockConnection, new Scanner(System.in));

        System.setOut(originalOut);

        String capturedOutput = outputStream.toString();

        assertTrue(capturedOutput.contains("Id: 1, Name: Haircut, Price: 30.0"));
        assertTrue(capturedOutput.contains("Id: 2, Name: Manicure, Price: 20.0"));
    }
    
    @Test
    public void testModifyAppointment() throws SQLException {
        
        String input = "1\n2023-08-21\n11:00\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.modifyAppointment(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT date, time FROM appointments WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals("2023-08-21", resultSet.getString("date"));
            assertEquals("11:00:00", resultSet.getString("time"));
        }
    }

    @Test
    public void testCancelAppointment() throws SQLException {
        
        String input = "1\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.cancelAppointment(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT COUNT(*) FROM appointments WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            int appointmentCount = resultSet.getInt(1);
            assertEquals(0, appointmentCount);
        }
    }
    
    @Test
    public void testModifyCustomer() throws SQLException {
        String input = "1\nJane Doe\njane@example.com\n9876543210\n456 Elm St\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        
        main.modifyCustomer(mockConnection, mockScanner);
        try (Statement statement = mockConnection.createStatement()) {
            // Check if the customer is modified in the database
            String sql = "SELECT name, email, phone, address FROM customers WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals("Jane Doe", resultSet.getString("name"));
            assertEquals("jane@example.com", resultSet.getString("email"));
            assertEquals("9876543210", resultSet.getString("phone"));
            assertEquals("456 Elm St", resultSet.getString("address"));
        }
    }

    @Test
    public void testDeleteCustomer() throws SQLException {
        String input = "1\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.deleteCustomer(mockConnection, mockScanner);
        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT COUNT(*) FROM customers WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            int customerCount = resultSet.getInt(1);
            assertEquals(0, customerCount);

            sql = "SELECT COUNT(*) FROM appointments WHERE id = 1";
            resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            int appointmentCount = resultSet.getInt(1);
            assertEquals(0, appointmentCount);
        }
    }
    
    @Test
    public void testAddBeautician() throws SQLException {
      
        String input = "1\nJohn\nHair Stylist\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.addBeautician(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT name, specialization FROM beauticians WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals("John", resultSet.getString("name"));
            assertEquals("Hair Stylist", resultSet.getString("specialization"));
        }
    }
    
    @Test
    public void testDeleteBeautician() throws SQLException {
    
        try (Statement statement = mockConnection.createStatement()) {
            statement.executeUpdate("INSERT INTO beauticians (id, name, specialization) VALUES (1, 'John', 'Hair Stylist')");
        }

        String input = "1\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.deleteBeautician(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT COUNT(*) FROM beauticians WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            int beauticianCount = resultSet.getInt(1);
            assertEquals(0, beauticianCount);
        }
    }

    @Test
    public void testModifyBeautician() throws SQLException {
        try (Statement statement = mockConnection.createStatement()) {
            statement.executeUpdate("INSERT INTO beauticians (id, name, specialization) VALUES (1, 'John', 'Hair Stylist')");
        }

        String input = "1\nJane\nNail Technician\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.modifyBeautician(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT name, specialization FROM beauticians WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals("Jane", resultSet.getString("name"));
            assertEquals("Nail Technician", resultSet.getString("specialization"));
        }
    }
    
    @Test
    public void testAddService() throws SQLException {
        String input = "1\nHaircut\n20.0\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.addService(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT name, price FROM services WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals("Haircut", resultSet.getString("name"));
            assertEquals(20.0, resultSet.getDouble("price"), 0.001); // Add delta for double comparison
        }
    }

    @Test
    public void testModifyService() throws SQLException {
        try (Statement statement = mockConnection.createStatement()) {
            statement.executeUpdate("INSERT INTO services (id, name, price) VALUES (1, 'Haircut', 20.0)");
        }

        String input = "1\nNew Haircut\n25.0\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.modifyService(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT name, price FROM services WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals("New Haircut", resultSet.getString("name"));
            assertEquals(25.0, resultSet.getDouble("price"), 0.001); // Add delta for double comparison
        }
    }

    @Test
    public void testDeleteService() throws SQLException {
        try (Statement statement = mockConnection.createStatement()) {
            statement.executeUpdate("INSERT INTO services (id, name, price) VALUES (1, 'Haircut', 20.0)");
        }

        String input = "1\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner mockScanner = new Scanner(inputStream);

        main.deleteService(mockConnection, mockScanner);

        try (Statement statement = mockConnection.createStatement()) {
            String sql = "SELECT COUNT(*) FROM services WHERE id = 1";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            int serviceCount = resultSet.getInt(1);
            assertEquals(0, serviceCount);
        }
    }

    
}
