package dto;

public class Message {
    private String from;
    private String to;
    private String stamp;
    private String nonce;
    private String messageBody;

    public Message(String from,
                   String to,
                   String stamp,
                   String nonce,
                   String messageBody) {
        this.from = from;
        this.to = to;
        this.stamp = stamp;
        this.nonce = nonce;
        this.messageBody = messageBody;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
