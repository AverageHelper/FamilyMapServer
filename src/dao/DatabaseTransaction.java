package dao;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

@FunctionalInterface
public interface DatabaseTransaction {
	public boolean run(@NotNull Connection conn) throws DataAccessException;
}
