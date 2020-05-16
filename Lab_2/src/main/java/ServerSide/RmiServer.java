package ServerSide;

import OnBothSide.RmiVisit;
import salon.Visit;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer implements RmiVisit {

    private static final String UNIC_NAME = "The server sorts a salon.Visit";

    @Override
    public Visit sortServices(Visit vis) throws RemoteException {
        vis.sortServices();
        return vis;
    }

    public static void main(String[] args) throws Exception
    {
        System.setProperty("java.security.policy", "src\\OnBothSide\\Security.policy");

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        //создание объекта для удаленного доступа
        final RmiServer rv = new RmiServer();

        final Registry registry = LocateRegistry.createRegistry(2020);

        Remote stub = UnicastRemoteObject.exportObject(rv, 0);

        registry.bind(UNIC_NAME, stub);
    }
}
