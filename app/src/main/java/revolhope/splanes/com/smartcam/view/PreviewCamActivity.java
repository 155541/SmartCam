package revolhope.splanes.com.smartcam.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.CameraSource;
import revolhope.splanes.com.smartcam.helper.CameraSourcePreview;
import revolhope.splanes.com.smartcam.helper.GraphicOverlay;
import revolhope.splanes.com.smartcam.helper.OcrDetectorProcessor;
import revolhope.splanes.com.smartcam.helper.OcrGraphic;

public class PreviewCamActivity extends AppCompatActivity {

    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private static final boolean AUTO_FOCUS = true;
    private static final boolean USE_FLASH = false;

    public static final String TextRead = "TextRead";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;

    // Helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_preview_camera);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPreview = findViewById(R.id.preview);
        mGraphicOverlay = findViewById(R.id.graphicOverlay);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED)
        {
            createCameraSource();
        }
        else
        {
            requestCameraPermission();
        }

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        findViewById(R.id.fabCapture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mGraphicOverlay != null && mGraphicOverlay.getGraphics() != null)
                {
                    String resultText = orderAndExtractGraphics(mGraphicOverlay.getGraphics());
                    Intent intent = new Intent(getApplicationContext(), DisplayResultsActivity.class);
                    intent.putExtra(TextRead, resultText);
                    startActivity(intent);
                }
            }
        });
    }

    @NonNull
    private String orderAndExtractGraphics(@NotNull List<OcrGraphic> graphicList)
    {

        StringBuilder stringBuilder = new StringBuilder();
        int size = graphicList.size();
        int minTop;
        int index;


        // Control
//        System.out.println(" :......: SIZE :......: " + size);
//        System.out.println(" :......: START :......: ");
//        for (OcrGraphic ocr : graphicList) System.out.println(" :......: OCR :......: " + ocr.getTextBlock().getValue());
//        System.out.println(" :......: END :......: ");


        for (int i = 0 ; i < size ; i++)
        {
            minTop = graphicList.get(0).getTextBlock().getBoundingBox().top;
            index = 0;

            for( OcrGraphic ocrGraphic : graphicList)
            {
                if (ocrGraphic != null && ocrGraphic.getTextBlock() != null)
                {
                    if (minTop > ocrGraphic.getTextBlock().getBoundingBox().top)
                    {
                        minTop = ocrGraphic.getTextBlock().getBoundingBox().top;
                        index = graphicList.indexOf(ocrGraphic);
                    }
                }
            }

            OcrGraphic top = graphicList.remove(index);
            stringBuilder.append(top.getTextBlock().getValue()).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission()
    {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        return  scaleGestureDetector.onTouchEvent(e) ||
                gestureDetector.onTouchEvent(e) ||
                super.onTouchEvent(e);
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    private void createCameraSource()
    {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated multi-processor instance
        // is set to receive the text recognition results, track the text, and maintain
        // graphics for each text block on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each text block.
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OcrDetectorProcessor(mGraphicOverlay));

        if (!textRecognizer.isOperational())
        {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage)
            {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.

        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), textRecognizer);
        mCameraSource  = builder.setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setFlashMode(USE_FLASH ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .setFocusMode(AUTO_FOCUS ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                .build();



    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        if (mPreview != null)
        {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mPreview != null)
        {
            mPreview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode != RC_HANDLE_CAMERA_PERM)
        {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException
    {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        if (code != ConnectionResult.SUCCESS)
        {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null)
        {
            try
            {
                mPreview.start(mCameraSource, mGraphicOverlay);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * onTap is called to speak the tapped TextBlock, if any, out loud.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the tap was on a TextBlock
     */
    private boolean onTap(float rawX, float rawY)
    {

        Log.w(TAG, "Fancy, you are here :D");

        Rect touchRect = new Rect(
                (int)(rawX - 100),
                (int)(rawY - 100),
                (int)(rawX + 100),
                (int)(rawY + 100));


        final Rect targetFocusRect = new Rect(
                touchRect.left * 2000/mPreview.getWidth() - 1000,
                touchRect.top * 2000/mPreview.getHeight() - 1000,
                touchRect.right * 2000/mPreview.getWidth() - 1000,
                touchRect.bottom * 2000/mPreview.getHeight() - 1000);

        List<Camera.Area> focusList = new ArrayList<>();
        Camera.Area focusArea = new Camera.Area(targetFocusRect, 1000);
        focusList.add(focusArea);
        mCameraSource.setFocusAreas(focusList);

        return true;

//        // TODO: FOCUS CAMERA ON TAP CLICKED..
//
//        OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(rawX, rawY);
//        TextBlock text = null;
//
//        if (graphic != null)
//        {
//            text = graphic.getTextBlock();
//            if (text != null && text.getValue() != null)
//            {
//                Log.d(TAG, "text data is being spoken! " + text.getValue());
//                // Speak the string.
//                //tts.speak(text.getValue(), TextToSpeech.QUEUE_ADD, null, "DEFAULT");
//            }
//            else {
//                Log.d(TAG, "text data is null");
//            }
//        }
//        else {
//            Log.d(TAG,"no text detected");
//        }
//        return text != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener
    {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector)
        {
            if (mCameraSource != null)
            {
                mCameraSource.doZoom(detector.getScaleFactor());
            }
        }
    }
}
