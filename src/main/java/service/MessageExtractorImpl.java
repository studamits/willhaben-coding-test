package service;

import dto.Message;
import utils.RegexUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class MessageExtractorImpl implements MessageExtractor {

    private static final Logger LOGGER = Logger.getLogger(MessageExtractorImpl.class.getName());;

    @Override
    public List<Message> getMessages(String filename) throws IOException {
        List<String> fileContent = readFileContent(filename);

        return extractMessages(fileContent);
    }

    @Override
    public List<String> getValidHashList(List<Message> messages) {
        List<String> validHashes = new ArrayList<>();

        if(messages == null)
            return validHashes;

        for (Message message : messages) {
            try {
                if (isMessageValid(message))
                    validHashes.add(message.getStamp());
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("NoSuchAlgorithmException thrown" + ex.getStackTrace());
            }
        }
        return validHashes;
    }

    private List<String> readFileContent(String filename) throws IOException {
        List<String> fileLines = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            Scanner sc = new Scanner(fis);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                fileLines.add(line + "\n");
            }
            fis.close();
            sc.close();
        } catch (IOException ex) {
            LOGGER.warning("IOException thrown " + ex);
            throw ex;
        }
        return fileLines;
    }

    private List<Message> extractMessages(List<String> fileContent) {
        List<String> messagesLines = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (String s : fileContent) {
            if (isMessageEndingLine(s)) {
                messagesLines.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(s);
            }
        }

        List<Message> validMessages = new ArrayList<>();
        for (String line : messagesLines) {
            Message message = extractMessage(line);
            if(message != null)
                validMessages.add(message);
        }
        return validMessages;
    }

    private boolean isMessageEndingLine(String line) {
        return line.equals(".\n");
    }

    private Message extractMessage(String messageLine) {
        String[] messageData = messageLine.split("\n");

        if (messageData.length < 6) {
            return null;
        }

        String fromEmailField = messageData[0];
        String toEmailField = messageData[1];
        String stampField = messageData[2];
        String nonceField = messageData[3];
        String emptyLine = messageData[4];

        if(!emptyLine.equals(""))
            return null;

        boolean isMessageHeaderValid = isMessageHeaderValid(fromEmailField, toEmailField, stampField, nonceField);
        if (!isMessageHeaderValid) {
            return null;
        }

        StringBuilder messageBody = new StringBuilder();
        for (int i = 5; i < messageData.length; i++) {
            messageBody.append(messageData[i].concat("\n"));
        }

        String fromEmail = extractHeaderFieldValue(fromEmailField);
        String toEmail = extractHeaderFieldValue(toEmailField);
        String stamp = extractHeaderFieldValue(stampField);
        String nonce = extractHeaderFieldValue(nonceField);

        Message message = new Message(fromEmail, toEmail, stamp, nonce, messageBody.toString());
        return message;
    }

    private boolean isMessageHeaderValid(String fromEmail, String toEmail, String stamp, String nonce) {
        boolean isFromEmailValid = fromEmail.matches(RegexUtil.FROM_EMAIL_REGEX);
        boolean isToEmailValid = toEmail.matches(RegexUtil.TO_EMAIL_REGEX);
        boolean isStampValid = stamp.matches(RegexUtil.STAMP_REGEX);
        boolean isNonceValid = nonce.matches(RegexUtil.NONCE_REGEX);

        return isFromEmailValid && isToEmailValid && isStampValid && isNonceValid;
    }

    private String extractHeaderFieldValue(String headerField) {
        String[] values = headerField.split(": ");

        return values[1];
    }

    private boolean isMessageValid(Message message) throws NoSuchAlgorithmException {
        String messageStamp = message.getStamp();
        String encodedMessageBase64SHA = computeBase64SHA_ForMessage(message);

        return messageStamp.equals(encodedMessageBase64SHA);
    }

    private String computeBase64SHA_ForMessage(Message message) throws NoSuchAlgorithmException {
        String encodingInput = message.getFrom() + message.getNonce() + message.getTo() + message.getNonce() + message.getMessageBody();
        String encodedOutput = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(encodingInput.getBytes(StandardCharsets.UTF_8)));

        return encodedOutput;
    }
}
