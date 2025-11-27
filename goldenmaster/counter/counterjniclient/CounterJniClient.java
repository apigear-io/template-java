package counter.counterjniclient;

import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_api.ICounterEventListener;

import counter.counter_android_client.CounterClient;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class CounterJniClient extends AbstractCounter implements ICounterEventListener
{

    private static final String TAG = "CounterJniClient";

    private CounterClient mMessengerClient = null;


    private static String ModuleName = "counter.counterjniservice.CounterJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }
    @Override
    public void setVector(customTypes.customTypes_api.Vector3D vector)
    {
        Log.i(TAG, "got request from ue, setVector" + (vector));
        mMessengerClient.setVector(vector);
    }
    @Override
    public customTypes.customTypes_api.Vector3D getVector()
    {
        Log.i(TAG, "got request from ue, getVector");
        return mMessengerClient.getVector();
    }
    
    @Override
    public void setExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector)
    {
        Log.i(TAG, "got request from ue, setExternVector" + (extern_vector));
        mMessengerClient.setExternVector(extern_vector);
    }
    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D getExternVector()
    {
        Log.i(TAG, "got request from ue, getExternVector");
        return mMessengerClient.getExternVector();
    }
    
    @Override
    public void setVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray)
    {
        Log.i(TAG, "got request from ue, setVectorArray" + (vectorArray));
        mMessengerClient.setVectorArray(vectorArray);
    }
    @Override
    public customTypes.customTypes_api.Vector3D[] getVectorArray()
    {
        Log.i(TAG, "got request from ue, getVectorArray");
        return mMessengerClient.getVectorArray();
    }
    
    @Override
    public void setExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "got request from ue, setExternVectorArray" + (extern_vectorArray));
        mMessengerClient.setExternVectorArray(extern_vectorArray);
    }
    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] getExternVectorArray()
    {
        Log.i(TAG, "got request from ue, getExternVectorArray");
        return mMessengerClient.getExternVectorArray();
    }
    
     public org.apache.commons.math3.geometry.euclidean.threed.Vector3D increment(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec)
     {
        Log.v(TAG, "Blocking callincrement - should not be used ");
        return mMessengerClient.increment(vec);
    }

    public void incrementAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec){
        Log.v(TAG, "non blocking call increment ");
        mMessengerClient.incrementAsync(vec).thenAccept(i -> {
            nativeOnIncrementResult(i, callId);});
    }

    //Should not be called directly, use incrementAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec)
    public CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.incrementAsync(vec);
    }
     public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] incrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec)
     {
        Log.v(TAG, "Blocking callincrementArray - should not be used ");
        return mMessengerClient.incrementArray(vec);
    }

    public void incrementArrayAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec){
        Log.v(TAG, "non blocking call incrementArray ");
        mMessengerClient.incrementArrayAsync(vec).thenAccept(i -> {
            nativeOnIncrementArrayResult(i, callId);});
    }

    //Should not be called directly, use incrementArrayAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec)
    public CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> incrementArrayAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.incrementArrayAsync(vec);
    }
     public customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec)
     {
        Log.v(TAG, "Blocking calldecrement - should not be used ");
        return mMessengerClient.decrement(vec);
    }

    public void decrementAsync(String callId, customTypes.customTypes_api.Vector3D vec){
        Log.v(TAG, "non blocking call decrement ");
        mMessengerClient.decrementAsync(vec).thenAccept(i -> {
            nativeOnDecrementResult(i, callId);});
    }

    //Should not be called directly, use decrementAsync(String callId, customTypes.customTypes_api.Vector3D vec)
    public CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.decrementAsync(vec);
    }
     public customTypes.customTypes_api.Vector3D[] decrementArray(customTypes.customTypes_api.Vector3D[] vec)
     {
        Log.v(TAG, "Blocking calldecrementArray - should not be used ");
        return mMessengerClient.decrementArray(vec);
    }

    public void decrementArrayAsync(String callId, customTypes.customTypes_api.Vector3D[] vec){
        Log.v(TAG, "non blocking call decrementArray ");
        mMessengerClient.decrementArrayAsync(vec).thenAccept(i -> {
            nativeOnDecrementArrayResult(i, callId);});
    }

    //Should not be called directly, use decrementArrayAsync(String callId, customTypes.customTypes_api.Vector3D[] vec)
    public CompletableFuture<customTypes.customTypes_api.Vector3D[]> decrementArrayAsync(customTypes.customTypes_api.Vector3D[] vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.decrementArrayAsync(vec);
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
            mMessengerClient = new CounterClient(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (lastServicePackage != servicePackage &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    @Override
    public void onVectorChanged(customTypes.customTypes_api.Vector3D newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnVectorChanged(newValue);
    }
    @Override
    public void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnExternVectorChanged(newValue);
    }
    @Override
    public void onVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnVectorArrayChanged(newValue);
    }
    @Override
    public void onExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnExternVectorArrayChanged(newValue);
    }
    @Override
    public void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal valueChanged "+ " " + vector+ " " + extern_vector+ " " + vectorArray+ " " + extern_vectorArray);
        nativeOnValueChanged(vector, extern_vector, vectorArray, extern_vectorArray);
    }
     private native void nativeOnVectorChanged(customTypes.customTypes_api.Vector3D vector);
     private native void nativeOnExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector);
     private native void nativeOnVectorArrayChanged(customTypes.customTypes_api.Vector3D[] vectorArray);
     private native void nativeOnExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    private native void nativeOnValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    private native void nativeOnIncrementResult(org.apache.commons.math3.geometry.euclidean.threed.Vector3D result, String callId);
    private native void nativeOnIncrementArrayResult(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] result, String callId);
    private native void nativeOnDecrementResult(customTypes.customTypes_api.Vector3D result, String callId);
    private native void nativeOnDecrementArrayResult(customTypes.customTypes_api.Vector3D[] result, String callId);
    private native void nativeIsReady(boolean isReady);
}
