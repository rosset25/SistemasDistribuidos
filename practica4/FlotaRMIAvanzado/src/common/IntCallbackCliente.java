package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntCallbackCliente extends Remote {

	public String notificaCliente(String mensaje) throws RemoteException;
	
	
}
