package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Note {

    @PrimaryKey
    private int noteId;

    public int getNoteId()
    {
        return noteId;
    }

    public void setNoteId(int noteId)
    {
        this.noteId = noteId;
    }
}
