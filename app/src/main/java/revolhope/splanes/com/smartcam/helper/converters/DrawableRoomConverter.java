package revolhope.splanes.com.smartcam.helper.converters;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DrawableRoomConverter
{
    @TypeConverter
    public Bitmap convert(byte[] raw)
    {
        return BitmapFactory.decodeByteArray(raw, 0, raw.length);
    }

    @TypeConverter
    public byte[] revert(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
