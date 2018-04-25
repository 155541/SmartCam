package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "table_contact",
        indices = {@Index(value = {"contact_name", "contact_phone"}, unique = true)})
public class Contact {
    
    @PrimaryKey
    @ColumnInfo(name = "contact_id")
    private String contactId;

    @ColumnInfo(name = "contact_name")
    private String contactName

    @ColumnInfo(name = "contact_email")
    private String contactEmail;
    
    @ColumnInfo(name = "contact_phone")
    private String contactPhone;
    
    @ColumnInfo(name = "contact_web")
    private String contactWeb;

    @ColumnInfo(name = "contact_address")
    private String contactAddress;
    
    @Ignore
    public Contact(String name, String phone, String mail, String address, String web)
    {
        this.contactId = UUID.randomUUID().toString();
        this.contactName = name;
        this.contactPhone = phone;
        this.contactEmail = mail;
        this.contactAddress = address;
        this.contactWeb = web;
    }
    
    public Contact(String id, String name, String phone, String mail, String address, String web)
    {
        this.contactId = id;
        this.contactName = name;
        this.contactPhone = phone;
        this.contactEmail = mail;
        this.contactAddress = address;
        this.contactWeb = web;
    }
    
    // TODO: Generate getters and setters
}
