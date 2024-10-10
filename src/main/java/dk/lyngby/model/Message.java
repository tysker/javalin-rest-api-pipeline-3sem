package dk.lyngby.model;

public record Message(int status, String message) {
    public Message(String message) {
        this(0, message);
    }
}