import dto.Message;
import service.MessageExtractor;
import service.MessageExtractorImpl;

import java.io.IOException;
import java.util.List;

public class WillhabenJavaAssessment {

    public static void main(String[]args) throws IOException {

        String fileName = "C:\\Users\\studamits\\Desktop\\input.txt";

        MessageExtractor messageExtractor = new MessageExtractorImpl();
        List<Message> messageList = messageExtractor.getMessages(fileName);

        List<String> validHashes = messageExtractor.getValidHashList(messageList);
        validHashes.forEach(System.out::println);

    }

}
