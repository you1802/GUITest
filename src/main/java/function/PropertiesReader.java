package function;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class PropertiesReader {
    Scanner scanner;
    private String username;
    private String passwordStr;

    public static final String FILE_PASS = "config.properties";

    public PropertiesReader(Scanner scanner){
        this.scanner = scanner;
        proCreate();
    }

    public void proCreate(){
        Path p = Paths.get(FILE_PASS);

        if (Files.exists(p)) {            //ファイルがある場合
            Properties properties = new Properties();

            try (Reader reader = new FileReader(FILE_PASS)){
                properties.load(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            username = properties.getProperty("user");
            passwordStr = properties.getProperty("pass");

        } else {                          //ファイルがない場合
            System.out.println("USER名:?");
            username = scanner.nextLine();
            System.out.println("PASSWORD:?");
            passwordStr = scanner.nextLine();

            File file = new File(FILE_PASS);
            try (FileWriter fileWriter = new FileWriter(file)){
                fileWriter.write("""
                        user=%s
                        pass=%s
                        """.formatted(username, passwordStr));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
//アクセッサー
    public String getUserName(){
        return username;
    }
    public String getPasswordStr(){
        return passwordStr;
    }
}
