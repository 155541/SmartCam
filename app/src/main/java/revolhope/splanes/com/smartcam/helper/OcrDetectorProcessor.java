package revolhope.splanes.com.smartcam.helper;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private static final int NEARBY_CONDITION = 6;
    private static final int NEARBY_LONGITUDE_CONDITION = 100;
    private static final int VALUE_IF_NOT_FOUND = -200;
    private static final int REPEAT_COUNT = 2;

    private static int round = 0;
    
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private SparseArray<TextBlock> detectionsSparseArray;
    private SparseIntArray repeats = new SparseIntArray();
    private List<String> auxiliaryStringList;
    

    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay)
    {
        mGraphicOverlay = ocrGraphicOverlay;
        detectionsSparseArray = new SparseArray<>();
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
        if(round < 5)
        {
            saveDetectionsDistinct(detections.getDetectedItems());
            round++;
        }
        else
        {
            mGraphicOverlay.clear();
            List<TextBlock> list = getRepeatedGreaterThan(REPEAT_COUNT);
            for (TextBlock block : list)
            {
                OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, block);
                mGraphicOverlay.add(graphic);
            }
            round = 0;
            detectionsSparseArray.clear();
            auxiliaryStringList.clear();
            repeats.clear();
        }
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
    public void release()
    {
        mGraphicOverlay.clear();
    }


// =============================================================================================================================
//      TESTING
// =============================================================================================================================
    
    private List<TextBlock> getRepeatedGreaterThan(int count)
    {
        List<TextBlock> list = new ArrayList<>();
        int detectionsSize = detectionsSparseArray.size();
        int itemKey;
        
        for (int i = 0 ; i < detectionsSize ; i++)
        {
            itemKey = detectionsSparseArray.keyAt(i);
            if (repeats.get(itemKey) > count)
            {
                list.add(detectionsSparseArray.get(itemKey));
            }
        }
        return list;
    }
    
    private void saveDetectionsDistinct(@NotNull SparseArray<TextBlock> detections)
    {
        if(auxiliaryStringList == null)
        {
            auxiliaryStringList = new ArrayList<>();
        }
        
        int size = detections.size();
        for(int i = 0 ; i < size ; i++)
        {
            String str = detections.valueAt(i).getValue().toLowerCase().trim();
            if(!auxiliaryStringList.contains(str))
            {
                auxiliaryStringList.add(str);
                detectionsSparseArray.put(detections.keyAt(i), detections.valueAt(i));
                repeats.put(detections.keyAt(i), 1);
            }
            else
            {
                int numRepeats = repeats.get(detections.keyAt(i), VALUE_IF_NOT_FOUND);
                if(numRepeats != VALUE_IF_NOT_FOUND)
                {
                    repeats.put(detections.keyAt(i), numRepeats+1);
                }
                else
                {
                    System.out.println(":....: STRANGE CASE :....:");
                }
            }
        }
    }
}
