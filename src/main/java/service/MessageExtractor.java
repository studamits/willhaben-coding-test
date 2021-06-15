package service;

import dto.Message;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MessageExtractor {

    List<Message> getMessages(String filename);

    List<String> getValidHashList(List<Message> messages) throws NoSuchAlgorithmException;
}
