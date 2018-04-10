package revolhope.splanes.com.smartcam.helper;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private static final int NEARBY_CONDITION = 6;
    private static final int NEARBY_LONGITUDE_CONDITION = 100;

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private SparseArray<TextBlock> previousDetections;

    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay)
    {
        mGraphicOverlay = ocrGraphicOverlay;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections)
    {

        mGraphicOverlay.clear();
        if(previousDetections != null)
        {
            SparseArray<TextBlock> items = detections.getDetectedItems();

            TextBlock prevItem;
            TextBlock currItem;

            int prevSize = previousDetections.size();
            int currSize = items.size();

            int j;
            for ( int i = 0 ; i < prevSize ; i++ )
            {
                for ( j = 0 ; j < currSize ; j++ )
                {
                    prevItem = previousDetections.valueAt(i);
                    currItem = items.valueAt(j);

                    // Check by content & location
                    if (areNearby(prevItem, currItem ) && haveSimilarContent(prevItem, currItem))
                    {
                        OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, getBestOption(prevItem, currItem));
                        mGraphicOverlay.add(graphic);
                    }
                }
            }
        }
        previousDetections = detections.getDetectedItems();
    }

    @Contract(pure = true)
    private boolean areNearby(@NotNull TextBlock previous, @NotNull TextBlock current)
    {
        boolean[] booleans = new boolean[8];
        Point[] prevPoints = previous.getCornerPoints();
        Point[] currPoints = current.getCornerPoints();

        for(int i = 0 ; i < 4 ; i++)
        {
            Point prevPoint = prevPoints[i];
            Point currPoint = currPoints[i];

            booleans[i*2] = currPoint.x > prevPoint.x + NEARBY_LONGITUDE_CONDITION || currPoint.x < prevPoint.x - NEARBY_LONGITUDE_CONDITION;
            booleans[(i*2)+1] = currPoint.y > prevPoint.y + NEARBY_LONGITUDE_CONDITION || currPoint.y < prevPoint.y - NEARBY_LONGITUDE_CONDITION;
        }

        // Not nearby point's count
        int count = 0;
        for (boolean b : booleans) count += b ? 1 : 0;

        // Considering nearby if, at least, 5 (from 8) coordinates are close
        return count < NEARBY_CONDITION;
    }

    @Contract(pure = true)
    private boolean haveSimilarContent(@NotNull TextBlock previous, @NotNull TextBlock current)
    {
        String[] prevWords = previous.getValue().split(" ");
        String[] currWords = current.getValue().split(" ");
        List<String> listPrevWords = Arrays.asList(prevWords);

        int count = 0;
        for(String word : currWords)
        {
            count += listPrevWords.contains(word) ? 1 : 0;
        }

        int minSize = Math.min(prevWords.length, currWords.length);

        // If is matching the half of words, return true
        return (minSize - count) < minSize / 2;
    }

    @Nullable
    @Contract(pure = true)
    private TextBlock getBestOption(@NotNull TextBlock previous, @NotNull TextBlock current)
    {
        Rect prevRect = previous.getBoundingBox();
        Rect currRect = current.getBoundingBox();
        int prevSum = prevRect.width() + prevRect.height();
        int currSum = currRect.width() + currRect.height();

        int prevSize = previous.getValue().split(" ").length;
        int currSize = current.getValue().split(" ").length;

        if(prevSize == currSize)
        {
            if(prevSum >= currSum)
            {
                return previous;
            }
            else if(prevSum < currSum)
            {
                return current;
            }
        }
        else if (prevSize > currSize)
        {
            if(prevSum >= currSum)
            {
                return previous;
            }
            else if(prevSum < currSum)
            {
                return null; // TODO: Check!
            }
        }
        else
        {
            if(prevSum > currSum)
            {
                return null; // TODO: Check!
            }
            else if(prevSum <= currSum)
            {
                return current;
            }
        }
        return null;
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}