package service;

import dto.Message;

import java.io.IOException;
import java.util.List;

public interface MessageExtractor {

    /**
     * Method to extract the Messages from file
     * @param filename (full path of input file from where to extract the messages)
     * @return List of extracted messages
     * @throws IOException
     */
    List<Message> getMessages(String filename) throws IOException;

    /**
     * Method to get the list of Valid Hash
     * A valid hash is the one, whose stamp value is equal to the message's BASE64_SHA value
     * @param List of messages
     * @return List of Valid Hashes
     */
    List<String> getValidHashList(List<Message> messages);
}
