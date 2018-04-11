package revolhope.splanes.com.smartcam.model;


@Entity
public class Card {
    
    @PrimaryKey
    private int cardId;

    @ColumnInfo(name = "company_name")
    private String company;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "email")
    private String email;
    
    @ColumnInfo(name = "phone")
    private String phone;
    
    @ColumnInfo(name = "web")
    private String web;

    @ColumnInfo(name = "address")
    private String address;
    
    // TODO: Create getters & setters. Are needed!
    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.
}
