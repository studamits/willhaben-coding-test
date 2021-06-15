import dto.Message;
import service.MessageExtractor;
import service.MessageExtractorImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class WillhabenJavaAssessment {

    public static void main(String[] args) throws IOException {

        System.out.println("Please provide the input file name");
        System.out.println("Please provide the full path, for example C:\\Users\\user\\package_name\\file_name.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();

        MessageExtractor messageExtractor = new MessageExtractorImpl();
        List<Message> messageList = messageExtractor.getMessages(fileName);

        System.out.println("***********************************************");
        System.out.println("***********************************************");
        System.out.println("Followings are the  hashes of the valid messages\n");

        List<String> validHashes = messageExtractor.getValidHashList(messageList);
        validHashes.forEach(System.out::println);

    }

}
