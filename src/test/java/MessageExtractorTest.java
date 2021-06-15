import dto.Message;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import service.MessageExtractor;
import service.MessageExtractorImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageExtractorTest {

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private MessageExtractor messageExtractor = new MessageExtractorImpl();

    private File file;
    private static String FILE_NAME = "input.txt";


    /* executed before every test: create temporary files */
    @Before
    public void setUp() {
        try {
            file = folder.newFile(FILE_NAME);
        } catch (IOException ioe) {
            System.err.println("error creating temporary test file in " + this.getClass().getSimpleName());
        }
    }

    @Test(expected = java.io.FileNotFoundException.class)
    public void getMessages_InvalidFileName_ThrowsFileNotFoundException() throws IOException {
        String INVALID_FILE_NAME = "DUMMY_NAME";
        messageExtractor.getMessages(INVALID_FILE_NAME);
    }

    @Test
    public void getMessages_EmptyFileContent_ReturnsEmptyMessageList() throws IOException {
        String fileContent = "";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertTrue(file.exists());
        assertEquals(0, messages.size());
    }

    @Test
    public void getMessages_MessageLastLineNotContainsDot_ReturnsEmptyMessageList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertEquals(0, messages.size());
    }

    @Test
    public void getMessages_MessageHeaderAndMessageBodyNotContainsEmptyLine_ReturnsEmptyMessageList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n" +
                ".";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertEquals(0, messages.size());
    }

    @Test
    public void getMessages_MessageHeaderFromNotValid_ReturnsEmptyMessageList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertEquals(0, messages.size());
    }

    @Test
    public void getMessages_MessageHeaderToNotValid_ReturnsEmptyMessageList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertEquals(0, messages.size());
    }

    @Test
    public void getMessages_MessageHeaderStampNotValid_ReturnsEmptyMessageList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stampa: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertEquals(0, messages.size());
    }

    @Test
    public void getMessages_ValidMessageString_ReturnsMessage() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n" +
                ".";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);

        assertEquals(1, messages.size());
    }

    @Test
    public void getValidHashList_PassedMessageListNull_ReturnsEmptyStringList() throws IOException {
        List<String> validMessagesSHA = messageExtractor.getValidHashList(null);

        assertEquals(0, validMessagesSHA.size());
    }

    @Test
    public void getValidHashList_MessageBase64SHAAndStampNotEquals_ReturnsEmptyStringList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: test=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n" +
                ".";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);
        List<String> validMessagesSHA = messageExtractor.getValidHashList(messages);

        assertEquals(0, validMessagesSHA.size());
    }

    @Test
    public void getValidHashList_MessageBase64SHAAndStampValid_ReturnsStringList() throws IOException {
        String fileContent = "From: bob@example.com\n" +
                "To: alice@example.com\n" +
                "Stamp: 0000JJTueU2utLQnAA0NIZ3NQZwGaskSQFkviXQyJsU=\n" +
                "Nonce: 24830964\n" +
                "\n" +
                "Hello Alice.\n" +
                "I found some exciting new thing called bitcoin. Have you heard about it?\n" +
                "Your friend,\n" +
                "Bob\n" +
                ".";

        writeFileContent(fileContent );
        List<Message> messages = messageExtractor.getMessages(FILE_NAME);
        List<String> validMessagesSHA = messageExtractor.getValidHashList(messages);

        assertEquals(1, validMessagesSHA.size());
    }

    private void writeFileContent(String content) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_NAME);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.close();
    }


}
