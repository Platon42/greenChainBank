
package tears.request;

import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "credit")

public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="credit_id", unique = true, nullable = false)
    private Long credit_id;

    @SerializedName("Num")
    @Expose
    private Integer num;

    @SerializedName("CreditParams")
    @Expose
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},
            mappedBy = "credit")
    private Set<CreditParam> creditParams = null;

    @ManyToOne(fetch= FetchType.EAGER,cascade = CascadeType.ALL)
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

    public Set<CreditParam> getCreditParams() {
        return creditParams;
    }

    public void setCreditParams(Set<CreditParam> creditParams) {
        this.creditParams = creditParams;
    }

}
