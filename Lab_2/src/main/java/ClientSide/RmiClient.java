package ClientSide;

import OnBothSide.RmiVisit;
import salon.Visit;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.System.out;

public class RmiClient {

    private static final String UNIC_NAME = "The server sorts a salon.Visit";

    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.policy", "src\\OnBothSide\\Security.policy");

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2020);
        PrintStream wr  = new PrintStream(new FileOutputStream(args[1]));
        try {
            //получаем объект
            RmiVisit rv = (RmiVisit) registry.lookup(UNIC_NAME);
            Visit visit = new Visit(1);
            visit.readFile(args[0]);
            out.println("---------Visit state before serialization---------\n" + visit);
            Visit result = rv.sortServices(visit);
            result.writeFile(args[1]);
            out.println("---------Visit state after serialization---------\n" + result);
        }
        catch (Throwable e){
            e.printStackTrace(wr);
        }
        finally {
            wr.close();
        }
    }

}
