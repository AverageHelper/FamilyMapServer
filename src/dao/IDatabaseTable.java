package dao;

import org.jetbrains.annotations.NotNull;

public interface IDatabaseTable {
	/**
	 * @return The name of the database table.
	 */
	public @NotNull String getName();
	
	/**
	 * @return The name of the table's primary key.
	 */
	public @NotNull String getPrimaryKey();
}
