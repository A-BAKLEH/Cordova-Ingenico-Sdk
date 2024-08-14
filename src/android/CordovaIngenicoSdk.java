package com.everbluepartners.cordovaingenicosdk;

import org.apache.cordova.CordovaPlugin;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.everbluepartners.cordovaingenicosdk.utils.Util;
import com.usdk.apiservice.aidl.printer.AlignMode;
import com.usdk.apiservice.aidl.printer.ECLevel;
import com.usdk.apiservice.aidl.printer.OnPrintListener;
import com.usdk.apiservice.aidl.printer.PrintFormat;
import com.usdk.apiservice.aidl.printer.PrinterData;
import com.usdk.apiservice.aidl.printer.UPrinter;

public class CordovaIngenicoSdk extends CordovaPlugin {

    private UPrinter printer;

    @Override
    protected void pluginInitialize() {
        System.out.println("pluginInitialize called");

        DeviceHelper deviceHelper = DeviceHelper.getInstance();
        deviceHelper.init(cordova.getActivity().getApplicationContext());
        deviceHelper.setServiceListener(new DeviceHelper.ServiceReadyListener() {
            @Override
            public void onReady(String version) {
                // Printer service is ready, get the printer instance
                deviceHelper.register();
                printer = deviceHelper.getPrinter();
                System.out.println(printer);
            }
        });
        deviceHelper.bindService();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("printHelloWorld")) {
            String base64Input = args.getString(0); 
            this.printHelloWorld(callbackContext, base64Input);
            return true;
        }
        return false;
    }

    private void printHelloWorld(final CallbackContext callbackContext, String base64Input) {

        if (printer == null) {
            callbackContext.error("Printer not initialized");
            return;
        }
    
        try {

            System.out.println(base64Input);
            // Decode the Base64 encoded string
            byte[] decodedBytes = Base64.decode(base64Input, Base64.NO_WRAP);
            String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);
            System.out.println(decodedText);

            // Define regex patterns for BITMAP and BARCODE sections
            String bitmapPattern = "(?s)BITMAP \\[.*?\\]";
            String barcodePattern = "(?s)BARCODE \\[.*?\\]";
    
            // Load the image from assets
            byte[] image = Util.readAssetsFile(cordova.getContext(), "logo.bmp");
            if (image != null && image.length > 0) {
                // Replace the BITMAP array section with an empty string
                decodedText = decodedText.replaceAll(bitmapPattern, "");
                printer.addImage(AlignMode.CENTER, image);
            } else {
                callbackContext.error("Image not loaded correctly");
                return;
            }
    
            // Extract and handle the BARCODE section
            Pattern barcodePatternCompiled = Pattern.compile(barcodePattern);
            Matcher barcodeMatcher = barcodePatternCompiled.matcher(decodedText);
    
            String barcodeValue = "";
            if (barcodeMatcher.find()) {
                String barcodeSection = barcodeMatcher.group(0);
    
                // Extract the VALUE from the BARCODE section
                Pattern valuePattern = Pattern.compile("VALUE = (\\w+)");
                Matcher valueMatcher = valuePattern.matcher(barcodeSection);
    
                if (valueMatcher.find()) {
                    barcodeValue = valueMatcher.group(1);
                }
    
                // Remove the BARCODE section from the decoded text
                decodedText = decodedText.replace(barcodeSection, "");
            }
    
            // Print the remaining text
            printer.setAscScale(1);
            printer.setAscSize(1);
            printer.setPrintFormat(PrintFormat.FORMAT_MOREDATAPROC, 0);
            printer.addText(AlignMode.LEFT, decodedText);
    
            // Add the extracted barcode
            if (!barcodeValue.isEmpty()) {
                printer.addBarCode(AlignMode.CENTER, 2, 48, barcodeValue);
            }
            System.out.println(decodedText);
            printer.setPrnGray(5);
            printer.feedLine(5);
    
            // Start Print
            printer.startPrint(new OnPrintListener.Stub() {
                @Override
                public void onFinish() throws RemoteException {
                    callbackContext.success("Print completed");
                }
                @Override
                public void onError(int error) throws RemoteException {
                    callbackContext.error("Print failed with error code: " + error);
                }
            });
    
        } catch (RemoteException e) {
            callbackContext.error("RemoteException: " + e.getMessage());
        } catch (Exception e) {
            callbackContext.error("Exception: " + e.getMessage());
        }
    }
    
}
