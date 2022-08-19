import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        // CsvJsonParser
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        // XmlJsonParser
        List<Employee> list2 = parseXML("data.xml");
        String json1 = listToJson(list2);
        writeString(json1, "data2.json");


    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        long id = 0;
        int age = 0;
        String firstName = null;
        String lastName = null;
        String country = null;

        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nodeItem = nodeList.item(i).getChildNodes();
            if (nodeItem.getLength() > 0) {
                for (int j = 0; j < nodeItem.getLength(); j++) {
                    switch (nodeItem.item(j).getNodeName()) {
                        case "id":
                            id = Integer.parseInt(nodeItem.item(j).getTextContent());
                            break;
                        case "firstName":
                            firstName = nodeItem.item(j).getTextContent();
                            break;
                        case "lastName":
                            lastName = nodeItem.item(j).getTextContent();
                            break;
                        case "country":
                            country = nodeItem.item(j).getTextContent();
                            break;
                        case "age":
                            age = Integer.parseInt(nodeItem.item(j).getTextContent());
                            break;
                    }
                }
            }
            if (id != 0) {
                Employee employee = new Employee(id, firstName, lastName, country, age);
                list.add(employee);
            }
            id = 0;
            age = 0;
            firstName = null;
            lastName = null;
            country = null;

        }
        return list;
    }


    public static String listToJson(List<Employee> list) {
        StringBuilder sb = new StringBuilder();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        sb.append(gson.toJson(list, listType));
        return sb.toString();
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
