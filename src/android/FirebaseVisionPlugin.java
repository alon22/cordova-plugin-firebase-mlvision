package by.alon22.cordova.firebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import androidx.annotation.NonNull;

/**
 * This class echoes a string called from JavaScript.
 */
public class FirebaseVisionPlugin extends CordovaPlugin {

    CallbackContext callbackContext;
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
        if (action.equals("onDeviceRecognizeImage")) {
            String message = args.getString(0);
            this.onDeviceRecognizeImage(message, callbackContext);
            return true;
        }
        return false;
    }

    private void onDeviceRecognizeImage(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try {
                Uri uri = Uri.parse(message);
                FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(applicationContext, uri);
                FirebaseVisionTextRecognizer recognizer = firebaseVision.getOnDeviceTextRecognizer();
                recognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                callbackContext.success(firebaseVisionText.getText());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackContext.error(e.getMessage());
                            }
                        });
            } catch (Exception e) {
                callbackContext.error(e.getLocalizedMessage());
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }

    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String stringUrl = strings[0];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                callbackContext.error(e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                FirebaseVisionTextRecognizer recognizer = firebaseVision.getOnDeviceTextRecognizer();
                recognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                callbackContext.success(firebaseVisionText.getText());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackContext.error(e.getMessage());
                            }
                        });
            }
        }
    }
}
