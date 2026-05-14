package apigear.android.lifecycle;

/**
 * Process-wide injection point for an {@link IBindingLifecycleCoordinator}.
 * The host application calls {@link #setCoordinator} at startup; generated
 * JNI bind code calls {@link #get} on every bind/unbind and silently no-ops
 * when no coordinator has been set (so the generated code is host-agnostic
 * and safe to run in test environments without a host).
 */
public final class BindingLifecycleRegistry
{
    private static volatile IBindingLifecycleCoordinator sCoordinator;

    private BindingLifecycleRegistry() {}

    /**
     * Install the host's coordinator. Typically called once at process startup
     * from host wiring code (e.g. a UPL-injected static initializer).
     */
    public static void setCoordinator(IBindingLifecycleCoordinator coordinator)
    {
        sCoordinator = coordinator;
    }

    /**
     * Returns the installed coordinator, or {@code null} if the host has not
     * registered one. Generated code must null-check the result.
     */
    public static IBindingLifecycleCoordinator get()
    {
        return sCoordinator;
    }
}
