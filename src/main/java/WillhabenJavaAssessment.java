
import dto.Message;
import org.apache.commons.codec.binary.Base64;
import service.MessageExtractor;
import service.MessageExtractorImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;

public class WillhabenJavaAssessment {

    public static void main(String[]args) throws NoSuchAlgorithmException {

        String fileName = "C:\\Users\\studamits\\Desktop\\input.txt";

        MessageExtractor messageExtractor = new MessageExtractorImpl();
        List<Message> messageList = messageExtractor.getMessages(fileName);

        List<String> validHashes = messageExtractor.getValidHashList(messageList);
        validHashes.forEach(System.out::println);

    }

}
