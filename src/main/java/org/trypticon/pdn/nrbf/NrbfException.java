package org.trypticon.pdn.nrbf;

import java.io.IOException;

public class NrbfException extends IOException {
    public NrbfException(String message) {
        super(message);
    }

    public NrbfException(String message, Throwable cause) {
        super(message, cause);
    }
}
