
package tears.request;

import java.io.Serializable;
import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "product_info")
public class ProductInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_id")
    private Long product_info_id;

    @SerializedName("ProductQty")
    @Expose
    @Column(name = "product_qty")
    private Integer productQty;

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @SerializedName("Deposits")
    @Expose
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},
            mappedBy = "productInfo")
    private Set<Deposit> deposits = null;

    @SerializedName("Credits")
    @Expose
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},
            mappedBy = "productInfo")
    private Set<Credit> credits = null;

    public void setClient(Client client) { this.client = client; }

    public Client getClient () { return client; }

    public Set<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(Set<Deposit> deposits) {
        this.deposits = deposits;
    }

    public Set<Credit> getCredits() {
        return credits;
    }

    public void setCredits(Set<Credit> credits) {
        this.credits = credits;
    }

    public Integer getProductQty() { return productQty; }

    public void setProductQty(Integer productQty) { this.productQty = productQty;
    }

}
