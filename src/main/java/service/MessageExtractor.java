package service;

import dto.Message;

import java.io.IOException;
import java.util.List;

public interface MessageExtractor {

    List<Message> getMessages(String filename) throws IOException;

    List<String> getValidHashList(List<Message> messages);
}
