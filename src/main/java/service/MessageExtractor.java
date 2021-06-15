package service;

import dto.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.List;

public interface MessageExtractor {

    List<Message> getMessages(String filename);

    List<String> validHashesList(List<Message> messages) throws NoSuchAlgorithmException;

    default String readFile(String filename) {
        StringBuffer sb=new StringBuffer();
        try {

            FileInputStream fis = new FileInputStream(filename);
            Scanner sc = new Scanner(fis);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                sb.append(line);
                System.out.println(line);
            }
            sc.close();     //closes the scanner
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
