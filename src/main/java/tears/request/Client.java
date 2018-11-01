
package tears.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="client_id", unique = true, nullable = false)
    private Long client_id;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("clientInfo")
    @Expose
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},
            mappedBy = "client")
    private ClientInfo clientInfo;

    @SerializedName("ProductInfo")
    @Expose
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},
            mappedBy = "client")
    private ProductInfo productInfo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

}
