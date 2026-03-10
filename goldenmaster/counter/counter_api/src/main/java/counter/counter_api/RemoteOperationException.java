package counter.counter_api;

/**
 * Exception thrown when a remote operation fails on the service side.
 *
 * <p>This exception is used to propagate errors across the IPC boundary.
 * The original exception type is not preserved -- only the message and an
 * error code are carried across.</p>
 *
 * <p>This class is generated per module. Each module has its own copy in
 * its own package, so they are distinct types at the Java level. Callers
 * handling errors from multiple modules cannot catch a single shared type
 * -- use {@code RuntimeException} as a common base in that case.</p>
 *
 * <h3>Error codes</h3>
 * <p>The error code classifies the failure for structured handling across
 * the IPC and JNI boundaries. The service maps standard Java exception
 * types to codes; the client and JNI bridge propagate them unchanged.</p>
 * <ul>
 *   <li>{@code ERROR_UNKNOWN (0)} -- generic/unclassified error</li>
 *   <li>{@code ERROR_SERVICE_DISCONNECTED (1)} -- binder connection lost</li>
 *   <li>{@code ERROR_SERVICE_NOT_READY (2)} -- backend not ready (reserved)</li>
 *   <li>{@code ERROR_INVALID_ARGUMENT (3)} -- bad input ({@code IllegalArgumentException})</li>
 *   <li>{@code ERROR_NOT_IMPLEMENTED (4)} -- operation not supported ({@code UnsupportedOperationException})</li>
 *   <li>{@code ERROR_INTERNAL (5)} -- unexpected backend exception (default for unrecognized exceptions)</li>
 * </ul>
 */
public class RemoteOperationException extends RuntimeException
{
    /** Generic/unclassified error. */
    public static final int ERROR_UNKNOWN = 0;
    /** Service process disconnected or binding died. */
    public static final int ERROR_SERVICE_DISCONNECTED = 1;
    /** Service exists but backend is not ready. */
    public static final int ERROR_SERVICE_NOT_READY = 2;
    /** Caller passed an invalid argument. */
    public static final int ERROR_INVALID_ARGUMENT = 3;
    /** Operation not implemented by the backend. */
    public static final int ERROR_NOT_IMPLEMENTED = 4;
    /** Unexpected internal error in the backend. */
    public static final int ERROR_INTERNAL = 5;

    private final int errorCode;

    public RemoteOperationException(String message)
    {
        this(message, ERROR_UNKNOWN);
    }

    public RemoteOperationException(String message, int errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode()
    {
        return errorCode;
    }
}
