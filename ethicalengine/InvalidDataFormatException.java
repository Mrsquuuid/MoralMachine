package ethicalengine;

/**
 * Description
 * This class is to handle Invalid number of data fields per row:
 * in case the number of values in one row is less than or exceeds 10 values.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class InvalidDataFormatException extends RuntimeException {
    /**
     * Default constructor
     * @param errorMessage the error message (String)
     */
    public InvalidDataFormatException (String errorMessage){
        super(errorMessage);
    }
}