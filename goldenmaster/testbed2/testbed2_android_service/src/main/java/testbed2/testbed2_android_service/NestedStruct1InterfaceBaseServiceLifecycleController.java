package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import java.util.concurrent.atomic.AtomicBoolean;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter;
import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceFactory;


// This class describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// It starts the service explicitly, it cannot be started on clients demand (with autocreate flag), because
// it requires the real backend service. The NestedStruct1InterfaceServiceAdapter is just an adapter,
// that forwards events from the backend to all clients (through the messenger), and forwards all requests
// from clients to the backend.
// Backend is available for the service between the start and stop calls in this class.
// After this time the service should not use it.
// The explicit start and stop of the service on the context does not guarantee that the service is alive through
//  the wholerequested time (between start and stop called). It may get killed due to low resources.
// This class also uses the ServiceConnection to connect to started service and receive the lifecycle notification.
// Based on those the service is restarted if required.
public abstract class NestedStruct1InterfaceBaseServiceLifecycleController
{
    private Context mContext = null;
    private LifecycleServiceConnection mLifecycleNotifier = new LifecycleServiceConnection();
    private AtomicBoolean mKeepServiceAlive = new AtomicBoolean(false);
    private AtomicBoolean mIsBound = new AtomicBoolean(false);

    protected abstract String getTag();
    protected abstract INestedStruct1InterfaceServiceFactory getFactoryInstance();
    protected abstract void onAndroidServiceConnectionStatusChanged(boolean status);

    public INestedStruct1Interface start(Context context)
    {
        mContext = context;
        if (mContext == null)
        {
            Log.e(getTag(), "Context is null");
            return null;
        }
        stop(mContext);
        mKeepServiceAlive.set(true);
        Intent androidService = new Intent(mContext, NestedStruct1InterfaceServiceAdapter.class);
        mContext.startService(androidService);
        Log.i(getTag(), "starter: Explicitly requested service start.");
        INestedStruct1Interface service =  NestedStruct1InterfaceServiceAdapter.setService(getFactoryInstance());
        bind(androidService);
        return service;
    }

    public  void stop(Context context)
    {
        mKeepServiceAlive.set(false);
        if (mContext != null && mContext != context)
        {
            Log.w(getTag(), "Context changed from " + mContext + " to " + context);
        }
        mContext = context;
        unbind();
        Intent androidService = new Intent(mContext, NestedStruct1InterfaceServiceAdapter.class);
        if (mContext != null)
        {
            Log.i(getTag(), "starter: Explicitly requested service stop.");
            mContext.stopService(androidService);
        }
        else
        {
            Log.e(getTag(), "cannot stop the service, cannot make intent for NestedStruct1InterfaceServiceAdapter.class");
        }
        INestedStruct1InterfaceServiceFactory factory = getFactoryInstance();
        factory.clear();
        NestedStruct1InterfaceServiceAdapter.setService(null);
    }

    void serviceConnectionStatusChanged(boolean status)
    {
        mIsBound.set(status);
        if (status == false)
        {
            if (mKeepServiceAlive.get() == true)
            {
                Log.w(getTag(), "Service was killed");
                onAndroidServiceConnectionStatusChanged(false);
            }
        }
        if (status == true)
        {
            Log.i(getTag(), "Service successfully started.");
            onAndroidServiceConnectionStatusChanged(true);
        }
    }

    public boolean bind(Intent serviceIntent)
    {
        if (mContext == null)
        {
            Log.e(getTag(), "Context is null, could not bind.");
            return false;
        }

        if (mIsBound.get())
        {
            Log.i(getTag(), "Already bound to service");
            return true;
        }

        // This is only "client" that can bind with BIND_AUTO_CREATE, as we are binding in early stage, the service may not yet be created
        // and binding for lifecycle events only, after the service was explicitly started and the backend was provided.
        boolean result = mContext.bindService(serviceIntent, mLifecycleNotifier, Context.BIND_AUTO_CREATE);

        Log.i(getTag(), "bindService result: " + result);
        return result;
    }

    public void unbind()
    {
        Log.i(getTag(), "Unbound from service");
        if (mIsBound.get() && mContext != null)
        {
            try
            {
                mContext.unbindService(mLifecycleNotifier);
            } catch (Exception e) {
                Log.e(getTag(), "Error unbinding: " + e);
            }
            mIsBound.set(false);
        }
    }
    
    private final class LifecycleServiceConnection implements ServiceConnection
    {
        private static final String TAG = "NestedStruct1Interface LifecycleServiceConnection";

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.i(TAG, "onServiceConnected: " + name);
            mIsBound.set(true);
            serviceConnectionStatusChanged(true);
        }
    
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Log.w(TAG, "onServiceDisconnected: " + name);
            mIsBound.set(false);
            serviceConnectionStatusChanged(false);
        }
    
        @Override
        public void onBindingDied(ComponentName name)
        {
            Log.e(TAG, "onBindingDied: " + name);
            mIsBound.set(false);
            serviceConnectionStatusChanged(false);
        }
    }

}
