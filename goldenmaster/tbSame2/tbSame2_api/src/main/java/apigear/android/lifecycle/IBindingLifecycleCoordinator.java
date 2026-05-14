package apigear.android.lifecycle;

/**
 * Interface implemented by the host application to coordinate ServiceConnection
 * cleanup across generated JNI clients and service starters. A coordinator
 * is injected via {@link BindingLifecycleRegistry#setCoordinator} at startup;
 * generated bind code registers a cleanup against it on successful bindService
 * and unregisters on voluntary unbind.
 */
public interface IBindingLifecycleCoordinator
{
    /**
     * Register a cleanup callback to be invoked when the host is about to tear
     * down the binding's owning Context. The callback should call
     * {@code Context.unbindService(...)} for the connection it owns.
     *
     * <p>Idempotent: implementations should treat duplicate registration of
     * the same Runnable as a no-op.</p>
     */
    void registerCleanup(Runnable cleanup);

    /**
     * Unregister a previously-registered callback. Called after a voluntary
     * unbind so the callback isn't run a second time on host shutdown.
     */
    void unregisterCleanup(Runnable cleanup);
}
