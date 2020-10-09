package services;

import handlers.LoginRequest;
import org.jetbrains.annotations.NotNull;

/**
 * An object that serves a single user login request.
 */
public class LoginService {
	/**
	 * Performs a login request.
	 * @param request The login request.
	 * @return The result of the login operation.
	 */
	public @NotNull LoginResult login(LoginRequest request) {
		return new LoginResult(LoginFailureReason.UNIMPLEMENTED);
	}
}
