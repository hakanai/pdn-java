package org.trypticon.pdn;

import java.io.IOException;

public class PdnException extends IOException {
    public PdnException(String message) {
        super(message);
    }

    public PdnException(String message, Throwable cause) {
        super(message, cause);
    }
}
