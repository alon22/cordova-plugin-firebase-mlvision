package by.alon22.cordova.firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * This class echoes a string called from JavaScript.
 */
public class FirebaseVisionPlugin extends CordovaPlugin {

    protected static Context applicationContext = null;
    private static Activity cordovaActivity = null;
    private FirebaseVision firebaseVision;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        cordovaActivity = this.cordova.getActivity();
        applicationContext = cordovaActivity.getApplicationContext();
        this.cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                FirebaseApp.initializeApp(applicationContext);
                firebaseVision = FirebaseVision.getInstance();
            }
        });
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("onDeviceTextRecognizer")) {
            String message = args.getString(0);
            this.onDeviceTextRecognizer(message, callbackContext);
            return true;
        } else if (action.equals("barcodeDetector")) {
            String message = args.getString(0);
            this.barcodeDetector(message, callbackContext);
            return true;
        }
        return false;
    }

    private void onDeviceTextRecognizer(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try {
                Uri uri = Uri.parse(message);
                FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(applicationContext, uri);
                FirebaseVisionTextRecognizer recognizer = firebaseVision.getOnDeviceTextRecognizer();
                recognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                try {
                                    JSONObject text = FirebaseUtils.parseText(firebaseVisionText);
                                    callbackContext.success(text);
                                } catch (Exception e) {
                                    callbackContext.error(e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackContext.error(e.getLocalizedMessage());
                            }
                        });
            } catch (Exception e) {
                callbackContext.error(e.getLocalizedMessage());
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void barcodeDetector(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try {
                Uri uri = Uri.parse(message);
                FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(applicationContext, uri);
                FirebaseVisionBarcodeDetector detector = firebaseVision.getVisionBarcodeDetector();
                detector.detectInImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                                try {
                                    JSONArray barcodes = FirebaseUtils.parseBarcode(firebaseVisionBarcodes);
                                    callbackContext.success(barcodes);
                                } catch (Exception e) {
                                    callbackContext.error(e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackContext.error(e.getLocalizedMessage());
                            }
                        });
            } catch (Exception e) {
                callbackContext.error(e.getLocalizedMessage());
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
