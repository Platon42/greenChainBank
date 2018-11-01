
package tears.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_info")
@NamedQueries({
        @NamedQuery(name = "cntCredits",
                query = "SELECT count(*) FROM CreditParam WHERE status = :status and address = :address"),
        @NamedQuery(name = "sumCredits",
                query = "SELECT sum(amount) FROM CreditParam  WHERE status = :status and address = :address"),
        @NamedQuery(name = "cntOverdue",
                query = " select count (*) FROM CreditParam WHERE (extract(minute from current_timestamp)-extract(minute from dateStart)) " +
                        "> :period and address = :address")

})
public class CreditParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="credit_info_id", unique = true, nullable = false)
    private Long credit_info_id;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("date_start")
    @Expose
    @Column(name = "date_start")
    private LocalDateTime dateStart;

    @SerializedName("expiration")
    @Expose
    private Integer expiration;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("payment")
    @Expose
    private Integer payment;

    @SerializedName("address")
    @Expose
    private String address;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    @JoinColumn(name = "credit_id")
    private Credit credit;

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Credit getCredit() {
        return credit;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
