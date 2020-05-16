package OnBothSide;

import salon.Visit;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiVisit extends Remote {
    Visit sortServices(Visit vis) throws RemoteException;
}
