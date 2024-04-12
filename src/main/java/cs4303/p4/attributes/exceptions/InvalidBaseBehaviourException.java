package cs4303.p4.attributes.exceptions;

public class InvalidBaseBehaviourException extends RuntimeException {
    public InvalidBaseBehaviourException(String location) {
        super("Attribute cannot act as base in " + location);
    }
}
