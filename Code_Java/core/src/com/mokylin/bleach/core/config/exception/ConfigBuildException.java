package com.mokylin.bleach.core.config.exception;

/**
 * {@code ConfigBuildException} is a subclass of {@code RuntimeException}
 * that can be thrown during the parsing of the configuration file.
 * 
 * <p>the class can not be extended.
 * 
 * @author yaguang.xiao
 *
 */

public final class ConfigBuildException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a <code>ConfigBuildException</code> with the
     * specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public ConfigBuildException(String msg) {
		super(msg);
	}
	
	/**
     * Constructs a <code>ConfigBuildException</code> with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
	public ConfigBuildException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
