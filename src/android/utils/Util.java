package com.everbluepartners.cordovaingenicosdk.utils;

import android.content.Context;
import android.widget.Toast;
import com.usdk.apiservice.aidl.printer.*;
import java.io.IOException;
import java.io.InputStream;

public class Util {

    public static String getErrorDetail(int error) {
        String message = getErrorMessage(error);
        if (error < 0) {
            return message + " [ " + error + " ]";
        }
        return message + String.format("[0x%02X]", error);
    }

    public static String getErrorMessage(int error) {
        switch (error) {
            case PrinterError.ERROR_NOT_INIT:
                return "ERROR_NOT_INIT";
            case PrinterError.ERROR_PARAM:
                return "ERROR_PARAM";
            case PrinterError.ERROR_BMBLACK:
                return "ERROR_BMBLACK";
            case PrinterError.ERROR_BUFOVERFLOW:
                return "ERROR_BUFOVERFLOW";
            case PrinterError.ERROR_BUSY:
                return "ERROR_BUSY";
            case PrinterError.ERROR_COMMERR:
                return "ERROR_COMMERR";
            case PrinterError.ERROR_CUTPOSITIONERR:
                return "ERROR_CUTPOSITIONERR";
            case PrinterError.ERROR_HARDERR:
                return "ERROR_HARDERR";
            case PrinterError.ERROR_LIFTHEAD:
                return "ERROR_LIFTHEAD";
            case PrinterError.ERROR_LOWTEMP:
                return "ERROR_LOWTEMP";
            case PrinterError.ERROR_LOWVOL:
                return "ERROR_LOWVOL";
            case PrinterError.ERROR_MOTORERR:
                return "ERROR_MOTORERR";
            case PrinterError.ERROR_NOBM:
                return "ERROR_NOBM";
            case PrinterError.ERROR_OVERHEAT:
                return "ERROR_OVERHEAT";
            case PrinterError.ERROR_PAPERENDED:
                return "ERROR_PAPERENDED";
            case PrinterError.ERROR_PAPERENDING:
                return "ERROR_PAPERENDING";
            case PrinterError.ERROR_PAPERJAM:
                return "ERROR_PAPERJAM";
            case PrinterError.ERROR_PENOFOUND:
                return "ERROR_PENOFOUND";
            case PrinterError.ERROR_WORKON:
                return "ERROR_WORKON";
            default:
                return "UNKNOWN_ERROR";
        }
    }

    public static byte[] readAssetsFile(Context ctx, String fileName) {
        InputStream input = null;
        try {
            input = ctx.getAssets().open(fileName);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getAlignMode(String alignMode) {
        switch (alignMode) {
            case "LEFT":
                return AlignMode.LEFT;
            case "CENTER":
                return AlignMode.CENTER;
            case "RIGHT":
                return AlignMode.RIGHT;
            default:
                return 0;
        }
    }

    public static int getSize(String size) {
        switch (size) {
            case "DOT5x7":
                return ASCSize.DOT5x7;
            case "DOT7x7":
                return ASCSize.DOT7x7;
            case "DOT16x8":
                return ASCSize.DOT16x8;
            case "DOT24x12":
                return ASCSize.DOT24x12;
            case "DOT24x8":
                return ASCSize.DOT24x8;
            case "DOT32x12":
                return ASCSize.DOT32x12;
            default:
                return 0;
        }
    }

    public static int getScale(String scale) {
        switch (scale) {
            case "SC1x1":
                return ASCScale.SC1x1;
            case "SC2x1":
                return ASCScale.SC2x1;
            case "SC2x2":
                return ASCScale.SC2x2;
            case "SC1x3":
                return ASCScale.SC1x3;
            case "SC2x3":
                return ASCScale.SC2x3;
            case "SC3x1":
                return ASCScale.SC3x1;
            case "SC3x2":
                return ASCScale.SC3x2;
            case "SC3x3":
                return ASCScale.SC3x3;
            default:
                return 0;
        }
    }

    public static int getFormat(String format) {
        switch (format) {
            case "VALUE_MOREDATAPROC_PRNONELINE":
                return PrintFormat.VALUE_MOREDATAPROC_PRNONELINE;
            case "VALUE_MOREDATAPROC_PRNTOEND":
                return PrintFormat.VALUE_MOREDATAPROC_PRNTOEND;
            default:
                return 0;
        }
    }

    public static int getFormatZero(String format) {
        switch (format) {
            case "VALUE_ZEROSPECTSET_DEFAULTZERO":
                return PrintFormat.VALUE_ZEROSPECSET_DEFAULTZERO;
            case "VALUE_ZEROSPECSET_SPECIALZERO":
                return PrintFormat.VALUE_ZEROSPECSET_SPECIALZERO;
            default:
                return 0;
        }
    }

    public static void handleException(Context context, Exception e) {
        e.printStackTrace();
        Toast.makeText(context, e.getClass().getSimpleName() + " : " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
