
package tears.request;

import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "deposit")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="deposit_id", unique = true, nullable = false)
    private Long deposit_id;

    @SerializedName("Num")
    @Expose
    private Integer num;
    @SerializedName("DepositParams")
    @Expose

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},
            mappedBy = "deposit")
    private Set<DepositParam> depositParams = null;


    @ManyToOne(fetch=FetchType.EAGER,cascade = {CascadeType.ALL})
    @JoinColumn(name = "product_info_id")
    private ProductInfo productInfo;

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public ProductInfo getProductInfo(){
        return productInfo;
    }
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Set<DepositParam> getDepositParams() {
        return depositParams;
    }

    public void setDepositParams(Set<DepositParam> depositParams) {
        this.depositParams = depositParams;
    }

}
