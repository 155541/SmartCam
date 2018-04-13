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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    // TODO: Delete if not use of export to Constants class
    private static final int NEARBY_CONDITION = 6;
    private static final int NEARBY_LONGITUDE_CONDITION = 100;

    private static int round = 0;
    
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private SparseIntArray idsSparseIntArray;
    private SparseArray<TextBlock> blockSparseArray;
    

    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay)
    {
        mGraphicOverlay = ocrGraphicOverlay;
        idsSparseIntArray = new SparseIntArray();
        blockSparseArray = new SparseArray<>();
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

        if(round == 10)
        {
            mGraphicOverlay.clear();
            for (TextBlock block : getAndTryToClean())
            {
                if(block != null)
                {
                    OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, block);
                    mGraphicOverlay.add(graphic);
                }
            }
            idsSparseIntArray.clear();
            blockSparseArray.clear();
            round = 0;
        }
        else
        {
            registerDetections(detections.getDetectedItems());
            round++;
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
//                  DEVELOPING
// =============================================================================================================================

    private List<TextBlock> getAndTryToClean()
    {
        List<TextBlock> textBlockList = getPersistedTextBlocks();
        List<Integer> indexToDelete = new ArrayList<>();
        String str1;
        String str2;

        for (TextBlock evaluated : textBlockList)
        {
            for (TextBlock iterated : textBlockList)
            {
                if (!evaluated.equals(iterated))
                {
                    str1 = evaluated.getValue().toUpperCase().trim();
                    str2 = iterated.getValue().toUpperCase().trim();
                    if (str2.contains(str1))
                    {
                        indexToDelete.add(textBlockList.indexOf(evaluated));
                    }
                }
            }
        }

        for(int index : indexToDelete)
        {
            if (index < textBlockList.size())
                textBlockList.remove(index);
        }

        return textBlockList;
    }

    private List<TextBlock> getPersistedTextBlocks()
    {
        List<TextBlock> textBlocks = new ArrayList<>();
        int id;
        int size = idsSparseIntArray.size();

        for (int i = 0 ; i < size ; i++)
        {
            if ( idsSparseIntArray.valueAt(i) >= Constants.REPEAT_COUNT)
            {
                id = idsSparseIntArray.keyAt(i);
                textBlocks.add(blockSparseArray.get(id, null));
            }
        }
        return textBlocks;
    }

    private void registerDetections(@NotNull SparseArray<TextBlock> detections)
    {
        int r;
        int size = detections.size();
        int id;
        TextBlock textBlock;

        for ( int i = 0 ; i < size ; i++ )
        {
            textBlock = detections.valueAt(i);
            id = detections.keyAt(i);
            r = idsSparseIntArray.get(id, Constants.VALUE_IF_NOT_FOUND);

            if (r != Constants.VALUE_IF_NOT_FOUND)
            {
                idsSparseIntArray.put(id, r+1);
            }
            else
            {
                idsSparseIntArray.put(id, 0);
                blockSparseArray.put(id, textBlock);
            }
        }
    }
}
