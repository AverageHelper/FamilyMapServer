package services;

import handlers.RegisterRequest;
import org.jetbrains.annotations.NotNull;

/**
 * An object that serves a single user registration request.
 */
public class RegisterService {
	/**
	 * Registers a new user.
	 * @param request Information about the new user's account.
	 * @return The result of the request.
	 */
	public @NotNull RegisterResult register(RegisterRequest request) {
		return new RegisterResult(RegisterFailureReason.UNIMPLEMENTED);
	}
}
