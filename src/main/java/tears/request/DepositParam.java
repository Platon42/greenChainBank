
package tears.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposit_info")
public class DepositParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="deposit_info_id", unique = true, nullable = false)
    private Long deposit_info_id;

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

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("address")
    @Expose
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deposit_id")
    private Deposit deposit;

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
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
