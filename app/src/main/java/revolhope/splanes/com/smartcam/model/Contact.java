package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "table_contact")
public class Contact {
    
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "contact_id")
    private String contactId;

    @NonNull
    @ColumnInfo(name = "contact_name")
    private String contactName;

    @ColumnInfo(name = "contact_email")
    private String contactEmail;
    
    @ColumnInfo(name = "contact_phone")
    private String contactPhone;
    
    @ColumnInfo(name = "contact_web")
    private String contactWeb;

    @ColumnInfo(name = "contact_address")
    private String contactAddress;
    
    @Ignore
    public Contact(@NonNull String name, String phone, String mail, String address, String web)
    {
        this.contactId = UUID.randomUUID().toString();
        this.contactName = name;
        this.contactPhone = phone;
        this.contactEmail = mail;
        this.contactAddress = address;
        this.contactWeb = web;
    }
    
    public Contact(@NonNull String contactId, @NonNull String contactName, String contactPhone,
                   String contactEmail, String contactAddress, String contactWeb)
    {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.contactAddress = contactAddress;
        this.contactWeb = contactWeb;
    }

    @NonNull
    public String getContactId() {
        return contactId;
    }

    public void setContactId(@NonNull String contactId) {
        this.contactId = contactId;
    }

    @NonNull
    public String getContactName() {
        return contactName;
    }

    public void setContactName(@NonNull String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactWeb() {
        return contactWeb;
    }

    public void setContactWeb(String contactWeb) {
        this.contactWeb = contactWeb;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
}
