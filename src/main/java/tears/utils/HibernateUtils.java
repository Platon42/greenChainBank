package tears.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;
import tears.request.*;
import tears.response.PDResponse;
import tears.telegram.TGUser;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HibernateUtils {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void setAllBankFields(Client client) {
        final Session session = HibernateUtils.getSession();

        Transaction tx = session.beginTransaction();
        client.setAddress(client.getAddress());

        ClientInfo clientInfo = client.getClientInfo();
        ProductInfo productInfo = client.getProductInfo();

        clientInfo.setAddress(client.getAddress());
        clientInfo.setIncome(clientInfo.getIncome());
        clientInfo.setAge(clientInfo.getAge());
        clientInfo.setEmail(clientInfo.getEmail());
        clientInfo.setFIO(clientInfo.getFIO());
        clientInfo.setDependQty(clientInfo.getDependQty());
        clientInfo.setPropertyType(clientInfo.getPropertyType());
        clientInfo.setSpending(clientInfo.getSpending());
        clientInfo.setSex(clientInfo.getSex());
        clientInfo.setEducation(clientInfo.getEducation());
        clientInfo.setClient(client);

        productInfo.setProductQty(2);

        Set<Credit> credits = productInfo.getCredits();
        Set<Deposit> deposits = productInfo.getDeposits();

        for (Credit credit : credits) {
            Set<CreditParam> creditParams = credit.getCreditParams();
            credit.setProductInfo(productInfo);
            productInfo.setCredits(credits);

            for (CreditParam creditParam : creditParams) {

                creditParam.setAmount(creditParam.getAmount());
                creditParam.setAddress(creditParam.getAddress());
                creditParam.setDateStart(creditParam.getDateStart());
                creditParam.setPayment(creditParam.getPayment());
                creditParam.setExpiration(creditParam.getExpiration());
                creditParam.setStatus(creditParam.getStatus());

                credit.setCreditParams(creditParams);
                creditParam.setCredit(credit);

            }
        }

        for (Deposit deposit : deposits) {
            Set<DepositParam> depositParams = deposit.getDepositParams();
            deposit.setProductInfo(productInfo);
            productInfo.setDeposits(deposits);

            for (DepositParam depositParam : depositParams) {

                depositParam.setAmount(depositParam.getAmount());
                depositParam.setAddress(depositParam.getAddress());
                depositParam.setDateStart(depositParam.getDateStart());
                depositParam.setExpiration(depositParam.getExpiration());
                depositParam.setStatus(depositParam.getStatus());

                deposit.setDepositParams(depositParams);
                depositParam.setDeposit(deposit);

            }
        }

        productInfo.setClient(client);
        client.setClientInfo(clientInfo);
        client.setProductInfo(productInfo);

        session.persist(client);
        tx.commit();

    }

    public static void setRiskMetrics(PDResponse pdResponse, Client client, Integer pd) {

        final Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();

        pdResponse.setAddress(client.getAddress());
        pdResponse.setPd(pd);

        session.saveOrUpdate(pdResponse);
        tx.commit();

    }

    public static void setTelegramUser(TGUser tgUser, Update update,int step, boolean isValid) {

        final Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();

        tgUser.setUserId(update.getMessage().getChat().getId());
        tgUser.setStep(step);
        tgUser.setDttm(LocalDateTime.now());
        //tgUser.setPhonenumber(update.getMessage().getContact().getPhoneNumber());

        if (update.getMessage().getText().startsWith("0x")){
            tgUser.setAddress(update.getMessage().getText());
        } else {
            tgUser.setUsertext(update.getMessage().getText());
        }
        tgUser.setUsername(update.getMessage().getChat().getUserName());
        tgUser.setIsvalid(isValid);
        session.save(tgUser);
        tx.commit();

    }

    public static List<String> getValidWallets(Update update) {

        final Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();

        Query qWallets = session.getNamedQuery("getAuthWallet").
                setParameter("userId", update.getMessage().getChat().getId());

        List<Object[]> resultList = qWallets.getResultList();
        List<String> validWallets = new ArrayList<>();

        for (Object[] objects : resultList){
            String wallet = (String) objects[0];
            if (wallet != null ) {
                validWallets.add(wallet);
            }
        }
        tx.commit();
        return validWallets;
    }

    public static String getTGAddress(Update update) {

        final Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();

        Query qAddress = session.getNamedQuery("getAddress").
                setParameter("userId", update.getMessage().getChat().getId())
                .setFirstResult(0)
                .setMaxResults(1);;

        String address = (String) qAddress.getSingleResult();
        tx.commit();
        return address;

    }

    public static String getTGText(Update update,int step) {

        final Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();

        Query qStep = session.getNamedQuery("getStepText").
                setParameter("userId", update.getMessage().getChat().getId())
                .setParameter("step",step)
                ;

        String text = (String) qStep.getSingleResult();
        tx.commit();
        return text;

    }

    public static Integer getTGStep(Update update) {

        final Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();

        Query qStep = session.getNamedQuery("getLastStep").
                setParameter("userId", update.getMessage().getChat().getId())
                .setFirstResult(0)
                .setMaxResults(1);

        Integer step = (Integer) qStep.getSingleResult();

        tx.commit();
        System.out.println("Step is "+step);

        return step;

    }

}