
package tears.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "client_info")
@NamedQueries({
        @NamedQuery(name = "AllInfoByAddress",
                query = "SELECT fIO,email,age,maritalStatus,propertyType,income,education,sex,spending,workIndustry " +
                        "FROM ClientInfo WHERE address = :address"),
        @NamedQuery(name = "findByPublishingDate",
                query = "SELECT sex FROM ClientInfo  WHERE address = :address")
})
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="client_info_id", unique = true, nullable = false)
    private Long client_info_id;

    @Column(name = "address")
    private String address;

    @SerializedName("FIO")
    @Expose
    private String fIO;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("Sex")
    @Expose
    private String sex;
    @SerializedName("MaritalStatus")
    @Expose
    @Column(name = "marital_status")
    private String maritalStatus;
    @SerializedName("Income")
    @Expose
    private Integer income;
    @SerializedName("Spending")
    @Expose
    private Integer spending;
    @SerializedName("DependQty")
    @Expose
    @Column(name = "depend_qty")
    private Integer dependQty;
    @SerializedName("PropertyType")
    @Expose
    @Column(name = "property_type")
    private Integer propertyType;
    @SerializedName("WorkIndustry")
    @Expose
    @Column(name = "work_industry")
    private String workIndustry;
    @SerializedName("Education")
    @Expose
    @Column(name = "education")
    private String education;

    @OneToOne(fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;


    public void setClient(Client client) { this.client = client; }

    public Client getClient () { return client; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }
    public String getFIO() {
        return fIO;
    }

    public void setFIO(String fIO) {
        this.fIO = fIO;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getSpending() {
        return spending;
    }

    public void setSpending(Integer spending) {
        this.spending = spending;
    }

    public Integer getDependQty() {
        return dependQty;
    }

    public void setDependQty(Integer dependQty) {
        this.dependQty = dependQty;
    }

    public Integer getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Integer propertyType) {
        this.propertyType = propertyType;
    }

    public String getWorkIndustry() {
        return workIndustry;
    }

    public void setWorkIndustry(String workIndustry) {
        this.workIndustry = workIndustry;
    }

}
