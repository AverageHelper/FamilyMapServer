package dao;

import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteErrorCode;

import java.sql.SQLException;

/**
 * Indicates an issue accessing the database.
 */
public class DataAccessException extends Exception {
	private final @NotNull SQLiteErrorCode code;
	
	DataAccessException(@NotNull SQLiteErrorCode code, String message) {
		super(message);
		this.code = code;
	}
	
	DataAccessException(@NotNull SQLiteErrorCode code) {
		super();
		this.code = code;
	}
	
	DataAccessException(@NotNull SQLException exception, String message) {
		super(message);
		this.code = SQLiteErrorCode.getErrorCode(exception.getErrorCode());
		this.initCause(exception);
	}
	
	DataAccessException(@NotNull SQLException exception) {
		super();
		this.code = SQLiteErrorCode.getErrorCode(exception.getErrorCode());
		this.initCause(exception);
	}
	
	public @NotNull SQLiteErrorCode getErrorCode() {
		return code;
	}
}
