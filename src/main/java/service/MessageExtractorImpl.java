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

public class MessageExtractorImpl implements MessageExtractor {

    @Override
    public List<Message> getMessages(String filename) {
        List<String> fileContent = readFileContent(filename);

        List<Message> messages = extractMessages(fileContent);
        return messages;
    }

    @Override
    public List<String> getValidHashList(List<Message> messages) {
        List<String> validHashes = new ArrayList<>();

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

    private List<String> readFileContent(String filename) {
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
            System.out.println("IOException thrown" + ex.getStackTrace());
        }
        return fileLines;
    }

    private List<Message> extractMessages(List<String> fileContent) {
        List<String> messagesLines = new ArrayList<>();
        StringBuffer messageBuffer = new StringBuffer();

        for (String s : fileContent) {
            if (isMessageEndingLine(s)) {
                messagesLines.add(messageBuffer.toString());
                messageBuffer = new StringBuffer();
            } else {
                messageBuffer.append(s);
            }
        }

        List<Message> validMessages = new ArrayList<>();
        for (String line : messagesLines) {
            Message message = extractMessage(line);
            validMessages.add(message);
        }
        return validMessages;
    }

    private boolean isMessageEndingLine(String line) {
        return line.equals(".\n") ? true : false;
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

        boolean isMessageHeaderValid = isMessageHeaderValid(fromEmailField, toEmailField, stampField, nonceField);
        if (!isMessageHeaderValid) {
            return null;
        }

        StringBuffer messageBody = new StringBuffer();
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

        return messageStamp.equals(encodedMessageBase64SHA) ? true : false;
    }

    private String computeBase64SHA_ForMessage(Message message) throws NoSuchAlgorithmException {
        String encodingInput = message.getFrom() + message.getNonce() + message.getTo() + message.getNonce() + message.getMessageBody();
        String encodedOutput = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(encodingInput.getBytes(StandardCharsets.UTF_8)));

        return encodedOutput;
    }
}
