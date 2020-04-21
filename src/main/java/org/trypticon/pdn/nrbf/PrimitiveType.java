package org.trypticon.pdn.nrbf;

/**
 * Common interface for primitive types.
 */
public interface PrimitiveType {

    /**
     * Converts to a sensible Java representation.
     *
     * @return the Java value.
     */
    Object toJavaValue();
}
