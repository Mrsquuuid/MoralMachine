package ethicalengine;

/**
 * Description
 * This class is to handle Invalid field values:
 * in case the program does not accommodate a specific value.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class InvalidCharacteristicException extends RuntimeException {
    /**
     * Default constructor
     * @param errorMessage the error message (String)
     */
    public InvalidCharacteristicException(String errorMessage) {
        super(errorMessage);
    }
}
