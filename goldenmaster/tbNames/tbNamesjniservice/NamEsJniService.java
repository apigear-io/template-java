package tbNames.tbNamesjniservice;

import android.os.Messenger;
import android.util.Log;

import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class NamEsJniService extends AbstractNamEs {


    private final static String TAG = "NamEsJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public NamEsJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setSwitch(boolean Switch)
    {
        Log.i(TAG, "request setSwitch called, will call native ");
        nativeSetSwitch(Switch);
    }

    @Override
    public boolean getSwitch()
    {
        Log.i(TAG, "request getSwitch called, will call native ");
        return nativeGetSwitch();
    }

  
    @Override
    public void setSomeProperty(int SOME_PROPERTY)
    {
        Log.i(TAG, "request setSomeProperty called, will call native ");
        nativeSetSomeProperty(SOME_PROPERTY);
    }

    @Override
    public int getSomeProperty()
    {
        Log.i(TAG, "request getSomeProperty called, will call native ");
        return nativeGetSomeProperty();
    }

  
    @Override
    public void setSomePoperty2(int Some_Poperty2)
    {
        Log.i(TAG, "request setSomePoperty2 called, will call native ");
        nativeSetSomePoperty2(Some_Poperty2);
    }

    @Override
    public int getSomePoperty2()
    {
        Log.i(TAG, "request getSomePoperty2 called, will call native ");
        return nativeGetSomePoperty2();
    }

  
    @Override
    public void setEnumProperty(EnumWithUnderScores enum_property)
    {
        Log.i(TAG, "request setEnumProperty called, will call native ");
        nativeSetEnumProperty(enum_property);
    }

    @Override
    public EnumWithUnderScores getEnumProperty()
    {
        Log.i(TAG, "request getEnumProperty called, will call native ");
        return nativeGetEnumProperty();
    }

  
    // methods

    @Override
    public void someFunction(boolean SOME_PARAM) {
        Log.i(TAG, "request method someFunction called, will call native");
         nativeSomeFunction(SOME_PARAM);
    }

    @Override
    public  CompletableFuture<Void> someFunctionAsync(boolean SOME_PARAM) {
        return CompletableFuture.runAsync(
                () -> { someFunction(SOME_PARAM); },
                executor);
    }

    @Override
    public void someFunction2(boolean Some_Param) {
        Log.i(TAG, "request method someFunction2 called, will call native");
         nativeSomeFunction2(Some_Param);
    }

    @Override
    public  CompletableFuture<Void> someFunction2Async(boolean Some_Param) {
        return CompletableFuture.runAsync(
                () -> { someFunction2(Some_Param); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetSwitch(boolean Switch);
    private native boolean nativeGetSwitch();
  
    private native void nativeSetSomeProperty(int SOME_PROPERTY);
    private native int nativeGetSomeProperty();
  
    private native void nativeSetSomePoperty2(int Some_Poperty2);
    private native int nativeGetSomePoperty2();
  
    private native void nativeSetEnumProperty(EnumWithUnderScores enum_property);
    private native EnumWithUnderScores nativeGetEnumProperty();
  
    // methods
    private native void nativeSomeFunction(boolean SOME_PARAM);
    private native void nativeSomeFunction2(boolean Some_Param);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onSwitchChanged(boolean newValue)
    {
         Log.i(TAG, "onSwitchChanged, will pass notification to all listeners");
         fireSwitchChanged(newValue);
    }
    public void onSomePropertyChanged(int newValue)
    {
         Log.i(TAG, "onSomePropertyChanged, will pass notification to all listeners");
         fireSomePropertyChanged(newValue);
    }
    public void onSomePoperty2Changed(int newValue)
    {
         Log.i(TAG, "onSomePoperty2Changed, will pass notification to all listeners");
         fireSomePoperty2Changed(newValue);
    }
    public void onEnumPropertyChanged(EnumWithUnderScores newValue)
    {
         Log.i(TAG, "onEnumPropertyChanged, will pass notification to all listeners");
         fireEnumPropertyChanged(newValue);
    }
    public void onSomeSignal(boolean SOME_PARAM)
    {
        Log.i(TAG, "onSomeSignal, will pass notification to all listeners");
        fireSomeSignal(SOME_PARAM);
    }
    public void onSomeSignal2(boolean Some_Param)
    {
        Log.i(TAG, "onSomeSignal2, will pass notification to all listeners");
        fireSomeSignal2(Some_Param);
    }

}
