import org.junit.jupiter.api.*;

import javax.swing.*;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvJsonParserTest {
    Employee employee1 = new Employee(1,"John","Smith","USA",25);
    Employee employee2 = new Employee(2,"Inav","Petrov","RU",23);

    @BeforeAll
    public static void initAll() {
        System.out.println("CsvToJson tests started");
    }

    @BeforeEach
    public void init() {
        System.out.println("Test started");
    }

    @Test
    public void parseCsvTest() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> expected = new ArrayList<>();
        expected.add(employee1);
        expected.add(employee2);

        List<Employee> result = Main.parseCSV(columnMapping, fileName);

        assertAll("result",
                () -> assertEquals(expected.size(), result.size()),
                () -> assertEquals(result.get(0).getId(), employee1.getId()),
                () -> assertEquals(result.get(1).getId(), employee2.getId())
        );
    }

    @AfterEach
    public void finished() {
        System.out.println("Test completed");
    }

    @AfterAll
    public static void finishedAll() {
        System.out.println("All CsvToJson tests completed");
    }
}
