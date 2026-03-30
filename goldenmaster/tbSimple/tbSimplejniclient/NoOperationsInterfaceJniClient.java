package tbSimple.tbSimplejniclient;

import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;

import tbSimple.tbSimple_android_client.NoOperationsInterfaceClient;
import tbSimple.tbSimple_android_messenger.Conversions;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NoOperationsInterfaceJniClient extends AbstractNoOperationsInterface implements INoOperationsInterfaceEventListener
{

    private static final String TAG = "NoOperationsInterfaceJniClient";

    private NoOperationsInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSimple.tbSimplejniservice.NoOperationsInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "got request setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "got request getPropBool");
        return mMessengerClient.getPropBool();
    }


    
    // Interface method — List types
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "got request setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    @Override
    public int getPropInt()
    {
        Log.i(TAG, "got request getPropInt");
        return mMessengerClient.getPropInt();
    }


    

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        return initServiceConnection(ctx, packageName, connectionID);
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new NoOperationsInterfaceClient(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (!lastServicePackage.equals(servicePackage) &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Will call native Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    // Event listener — receives List from messenger client, converts to array for native
    @Override
    public void onPropBoolChanged(boolean newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(newValue);
    }
    @Override
    public void onPropIntChanged(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(newValue);
    }
    @Override
    public void onSigVoid()
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigVoid ");
        nativeOnSigVoid();
    }
    @Override
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(paramBool);
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnPropBoolChanged(boolean propBool);
     private native void nativeOnPropIntChanged(int propInt);
    private native void nativeOnSigVoid();
    private native void nativeOnSigBool(boolean paramBool);
    private native void nativeIsReady(boolean isReady);
}
