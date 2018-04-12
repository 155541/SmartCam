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
        if (rounds < 6)
        {
            checkRepeats(detections.getDetectedItems());
            rounds++;
        }
        else
        {
            rounds = 0;
            mGraphicOverlay.clear();

            for (TextBlock textBlock : getRepeatedGreaterThan(3))
            {
                OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, textBlock);
                mGraphicOverlay.add(graphic);
            }

            repeats.clear();
            textBlockSparseArray.clear();
        }


//        mGraphicOverlay.clear();
//        if(previousDetections != null)
//        {
//            SparseArray<TextBlock> items = detections.getDetectedItems();
//            TextBlock prevItem;
//            TextBlock currItem;
//
//            int prevSize = previousDetections.size();
//            int currSize = items.size();
//
//            int j;
//            for ( int i = 0 ; i < prevSize ; i++ )
//            {
//                for ( j = 0 ; j < currSize ; j++ )
//                {
//                    prevItem = previousDetections.valueAt(i);
//                    currItem = items.valueAt(j);
//
//                    // Check by content & location
//                    if (areNearby(prevItem, currItem ) && haveSimilarContent(prevItem, currItem))
//                    {
//                        OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, getBestOption(prevItem, currItem));
//                        mGraphicOverlay.add(graphic);
//                    }
//                }
//            }
//        }
//        previousDetections = detections.getDetectedItems();
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



    // TESTING


    private void checkRepeats(@NotNull SparseArray<TextBlock> items)
    {
        int itemsSize = items.size();
        int itemKey;
        TextBlock itemValue;
        String itemValueString;

        List<String> listStringValues = new ArrayList<>();
        int textBlockSparseArraySize = textBlockSparseArray.size();

        int i;
        for ( i = 0 ; i < textBlockSparseArraySize ; i++)
        {
            listStringValues.add(textBlockSparseArray.valueAt(i).getValue());
        }

        for ( i = 0 ; i < itemsSize ; i++)
        {
            itemKey = items.keyAt(i);
            itemValue = items.valueAt(i);
            itemValueString = itemValue.getValue();

            int numRepeats = repeats.get(itemKey, VALUE_IF_NOT_FOUND);
            if(numRepeats != VALUE_IF_NOT_FOUND)
            {
                repeats.put(itemKey, numRepeats+1);
            }
            else
            {
                repeats.put(itemKey, 1);
            }
            if (!listStringValues.contains(itemValueString))
            {
                textBlockSparseArray.put(itemKey, itemValue);
                listStringValues.add(itemValueString);
            }
        }

    }

    private List<TextBlock> getRepeatedGreaterThan(int count)
    {
        List<TextBlock> list = new ArrayList<>();
        int itemsSize = textBlockSparseArray.size();
        int textBlocksSize = textBlockSparseArray.size();

        int j;
        for (int i = 0 ; i < itemsSize ; i++)
        {
            int itemKey = textBlockSparseArray.keyAt(i);
            if (repeats.get(itemKey) > count)
            {
                TextBlock itemValue = textBlockSparseArray.get(itemKey);
                boolean b = false;
                for (j = 0 ; j < textBlocksSize ;  j++)
                {
                    if(haveSimilarContent(itemValue, textBlockSparseArray.valueAt(j)))
                    {
                        b = true;
                    }
                }
                if(!b)
                {
                    list.add(itemValue);
                }
            }
        }

        return list;
    }










    private static final int VALUE_IF_NOT_FOUND = -200;

    private static int rounds = 0;
    private SparseIntArray repeats = new SparseIntArray();
    private SparseArray<TextBlock> textBlockSparseArray = new SparseArray<>();

    private void onDetect(@NotNull SparseArray<TextBlock> items)
    {
        SparseArray<TextBlock> realItems = new SparseArray<>();
        int itemsSize = items.size();
        int itemKey;
        TextBlock itemValue;

        // =========================================================================================
        //          ATTEMPT TO DELETE REPEATED ITEMS OF SAME DETECTION SET
        // =========================================================================================
        for (int i = 0; i < itemsSize ; i++)
        {
            itemKey = items.keyAt(i);
            itemValue = items.valueAt(i);

            int j;
            for (j = 0; j < itemsSize; j++) {
                int auxItemKey = items.keyAt(j);
                TextBlock auxItemValue = items.valueAt(j);

                if (itemKey != auxItemKey) {
                    String lineEvaluateItem = itemValue.getValue();
                    String lineIterationItem = auxItemValue.getValue();

                    if (lineIterationItem.contains(lineEvaluateItem) &&
                            lineIterationItem.length() > lineEvaluateItem.length() &&
                            areNearby(itemValue, auxItemValue)) {
                        realItems.put(auxItemKey, auxItemValue);
                    }
                }
            }
        }

        itemsSize = realItems.size();
        for (int i = 0 ; i < itemsSize ; i++)
        {
        // =========================================================================================
        //          UPDATE REPETITIONS FOR EACH 'TEXT BLOCK' FOUND
        // =========================================================================================

            itemKey = items.keyAt(i);
            itemValue = items.valueAt(i);

            int numRepeats = repeats.get(itemKey, VALUE_IF_NOT_FOUND);
            if(numRepeats != VALUE_IF_NOT_FOUND)
            {
                repeats.delete(itemKey);
                repeats.put(itemKey, numRepeats+1);
            }
            else
            {
                repeats.put(itemKey, 1);
            }

        // =========================================================================================
        //          CHECK IF NEW ITEM DETECTED IS ALREADY STORED,
        //          IF IT IS CHECK IF IT'S CONTENT IS BETTER THAN OLDER,
        //          IF IT'S BETTER, REPLACE IT
        // =========================================================================================

            TextBlock textBlock = textBlockSparseArray.get(itemKey, null);
            if(textBlock != null)
            {
                // Already found, check if length content is the same
                int numWordsNew = itemValue.getValue().split(" ").length;
                int numWordsExisting = textBlock.getValue().split(" ").length;

                if(numWordsExisting < numWordsNew)
                {
                    textBlockSparseArray.delete(itemKey);
                    textBlockSparseArray.put(itemKey, itemValue);
                }
            }
            else
            {
                textBlockSparseArray.put(itemKey, itemValue);
            }
        }
        // Increasing 'rounds' value up 1
        rounds++;
    }
    
    // private SparseArray<TextBlock> detectionsSparseArray;
    // private List<String> auxiliaryStringList;
    private void saveDetectionsDistinc(@NotNull SparseArray<TextBlock> detections)
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
