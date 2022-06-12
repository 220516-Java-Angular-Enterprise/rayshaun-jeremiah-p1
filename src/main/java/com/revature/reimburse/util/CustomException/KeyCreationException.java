package com.revature.reimburse.util.CustomException;

import java.security.KeyException;

public class KeyCreationException extends KeyException {
    public KeyCreationException(String message) {
        super(message);
    }

    public KeyCreationException(Throwable cause) {
        super(cause);
    }
}
