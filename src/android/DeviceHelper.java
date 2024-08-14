package com.everbluepartners.cordovaingenicosdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.usdk.apiservice.aidl.UDeviceService;
import com.usdk.apiservice.aidl.printer.UPrinter;

public class DeviceHelper implements ServiceConnection {
    private static final String TAG = "DeviceHelper";
    private static DeviceHelper instance;
    private Context context;
    private ServiceReadyListener serviceListener;
    private boolean isBound = false;
    private UDeviceService deviceService;

    public static DeviceHelper getInstance() {
        if (instance == null) {
            instance = new DeviceHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public void setServiceListener(ServiceReadyListener listener) {
        serviceListener = listener;
        if (isBound) {
            notifyReady();
        }
    }

    public void register() {
        try {
            if (deviceService != null) {
                deviceService.register(null, new Binder());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void bindService() {
        if (isBound) {
            return;
        }
        Intent service = new Intent("com.usdk.apiservice");
        service.setPackage("com.usdk.apiservice");
        boolean bindSuccess = context.bindService(service, this, Context.BIND_AUTO_CREATE);

        if (!bindSuccess) {
            Log.e(TAG, "=> bind fail");
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "=> onServiceConnected");
        isBound = true;
        deviceService = UDeviceService.Stub.asInterface(service);
        notifyReady();
    }

    private void notifyReady() {
        if (serviceListener != null) {
            try {
                serviceListener.onReady(deviceService.getVersion());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e(TAG, "=> onServiceDisconnected");
        deviceService = null;
        isBound = false;
        bindService();
    }

    public void unbindService() {
        if (isBound) {
            Log.e(TAG, "=> unbindService");
            context.unbindService(this);
            isBound = false;
        }
    }

    public UPrinter getPrinter() throws IllegalStateException {
        if (deviceService == null) {
            throw new IllegalStateException("Service unbound, please retry later!");
        }
        try {
            return UPrinter.Stub.asInterface(deviceService.getPrinter());
        } catch (RemoteException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public interface ServiceReadyListener {
        void onReady(String version);
    }
}
