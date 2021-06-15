
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

//        String input = "test input";;

        MessageExtractor messageExtractor = new MessageExtractorImpl();

        String fileName = "C:\\Users\\studamits\\Desktop\\input.txt";
        List<Message> messageList = messageExtractor.getMessages(fileName);
        List<String> validHashes = messageExtractor.validHashesList(messageList);
        validHashes.forEach(System.out::println);


//        Base64 base64 = new Base64();
//
//
//        String input = "bob@example.com"+24830964+"alice@example.com"+24830964+"Hello Alice.\n" + "I found some exciting new thing called bitcoin. Have you heard about it?\n" + "Your friend,\n" + "Bob\n";
//
//
//        String encodingString2 = java.util.Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8)));
//
//      //  String decodedString1 = new String(base64.decode(encodedString1.getBytes()));
//        String decodedString2 = new String(java.util.Base64.getDecoder().decode(encodingString2.getBytes()));
//        String decodedString3 = new String(MessageDigest.getInstance("SHA-256").digest(java.util.Base64.getDecoder().decode(encodingString2.getBytes())));
//
//        System.out.println("EncodingString2 is  \t" + new String(encodingString2));
//
//        System.out.println("DecodedString2 is  \t" + new String(decodedString2));


        //testInput2();
    }

    private static void testInput2() throws NoSuchAlgorithmException {
        Base64 base64 = new Base64();
        String input = "alice@example.com"+38385307+"bob@example.com"+38385307+"Hello Bob,\n" +
                "> I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "\n" +
                "I read something about it, sounds like a scam to me.\n" +
                "Yours, Alice\n" ;
        String encodingString = java.util.Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8)));
        String decodedString = new String(base64.decode(encodingString.getBytes()));

        System.out.println("EncodedString2 is  \t" + new String(encodingString));
        System.out.println("Decoded String2 is  \t" + new String(decodedString));
    }
}
