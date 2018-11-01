package tears.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "risk_metrics")

public class PDResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="metric_id", unique = true, nullable = false)
    private Long metric_id;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pd")
    @Expose
    private Integer pd;

    @Transient
    @SerializedName("decision")
    @Expose
    private Boolean decision;

    @Transient
    @SerializedName("rating")
    @Expose
    private Integer rating;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPd() {
        return pd;
    }

    public void setPd(Integer pd) {
        this.pd = pd;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
