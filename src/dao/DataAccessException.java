package dao;

/**
 * Indicates an issue accessing the database.
 */
public class DataAccessException extends Exception {
    DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}
