package tears.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tears.request.Client;

import javax.persistence.Query;
import java.util.HashMap;

public class PDClient {

    private final static Session session = HibernateUtils.getSession();
    private static Transaction transaction = null;

    public static int points;

    public static HashMap<String,Integer> calcPD(Client client) {

        transaction = session.beginTransaction();
        points = 6;

        Query openCredits = session.getNamedQuery("cntCredits").
                setParameter("address", client.getAddress())
                .setParameter("status", "open");

        Query clsdCredits = session.getNamedQuery("cntCredits").
                setParameter("address", client.getAddress())
                .setParameter("status", "closed");

        Query ovrDue3 = session.getNamedQuery("cntOverdue")
                .setParameter("address", client.getAddress())
                .setParameter("period", 3);

        Query ovrDue6 = session.getNamedQuery("cntOverdue")
                .setParameter("address", client.getAddress())
                .setParameter("period", 6);

        Query ovrDue9 = session.getNamedQuery("cntOverdue")
                .setParameter("address", client.getAddress())
                .setParameter("period", 9);

        Integer age = client.getClientInfo().getAge();
        switch ((18 <= age && age <= 24) ? 1 :
                (24 <= age && age <= 29) ? 2 :
                        (29 <= age && age <= 34) ? 3 :
                                (34 <= age && age <= 45) ? 4 :
                                        (45 <= age && age <= 65) ? 5 :
                                                6
        ) {
            case 1:
                points +=23;
                break;
            case 2:
                points +=29;
                break;
            case 3:
                points +=15;
                break;
            case 4:
                points +=30;
                break;
            case 5:
                points +=33;
                break;
            case 6:
                points +=38;
                break;
            default:
                points +=11;
                break;
        }

        String sex = client.getClientInfo().getSex();
        switch ((sex.toUpperCase().contains("M")) ? 1 : 0) {
            case 0:
                points +=11;
                break;

            default:
                points +=8;
                break;
        }

        String education = client.getClientInfo().getEducation();
        switch ((education.toLowerCase().contains("high")) ? 1 :
                (education.toLowerCase().contains("middle")) ? 2 :
                        (education.toLowerCase().contains("low")) ? 3 : 4) {
            case 1:
                points +=3;
                break;
            case 2:
                points +=9;
                break;
            case 3:
                points +=15;
                break;
            case 4:
                points +=23;
                break;
            default:
                points +=23;
                break;
        }

        Integer income = client.getClientInfo().getIncome();
        switch ((800 <= income && income <= 1400) ? 1 :
                (1400 <= income && income <= 2800) ? 2 :
                        (2800 <= income && income <= 5000) ? 3 :
                                (5000 <= income && income <= 8700) ? 4 :
                                        (8700 <= income && income <= 10000) ? 5 : 6) {
            case 1:
                points +=36;
                break;
            case 2:
                points +=30;
                break;
            case 3:
                points +=24;
                break;
            case 4:
                points +=15;
                break;
            case 5:
                points +=6;
                break;
            case 6:
                points +=6;
                break;
        }

        Long cntOpnd = (Long) openCredits.getSingleResult();
        Long cntClsd = (Long) clsdCredits.getSingleResult();
        Long cntOvrd3 = (Long) ovrDue3.getSingleResult();
        Long cntOvrd6 = (Long) ovrDue6.getSingleResult();
        Long cntOvrd9 = (Long) ovrDue9.getSingleResult();

        if (cntOpnd > 2 || cntOvrd3!=0) {
            points += 48;
        }

        HashMap<String,Integer> result = new HashMap<>();

        result.put("PD",points);
        result.put("cntOpnd",cntOpnd.intValue());
        result.put("cntClsd",cntClsd.intValue());
        result.put("cntOvrd3",cntOvrd3.intValue());

        transaction.commit();

        return result;
    }

}
