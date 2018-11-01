package tears.telegram;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "telegram")
@NamedQueries({
        @NamedQuery(name = "getAddress",
                query = "select address from TGUser where userId=:userId and address is not null order by dttm desc"),
        @NamedQuery(name = "getStepText",
                query = "select usertext from TGUser where userId=:userId and step=:step order by dttm desc"),
        @NamedQuery(name = "getLastStep",
                query = "select coalesce(max(step),0) from TGUser where userId=:userId"),
        @NamedQuery(name = "getAuthWallet",
                query = "select address from TGUser where userId=:userId  and isvalid=true")
        })
public class TGUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;

    private String address;
    private String username;

    @Column(name = "phone_number")
    private String phonenumber;


    @Column(name = "user_text")
    private String usertext;

    @Column(name = "user_id")
    private Long userId;

    private int step;

    private LocalDateTime dttm;

    public boolean isIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid = isvalid;
    }

    @Column(name = "is_valid")
    private boolean isvalid;

    public LocalDateTime getDttm() {
        return dttm;
    }

    public void setDttm(LocalDateTime dttm) {
        this.dttm = dttm;
    }




    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }


    public String getUsertext() {
        return usertext;
    }

    public void setUsertext(String usertext) {
        this.usertext = usertext;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
