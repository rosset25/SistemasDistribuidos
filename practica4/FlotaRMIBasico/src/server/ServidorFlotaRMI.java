package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorFlotaRMI {

	
	public ServidorFlotaRMI() {
		
	}
	
	public static void main(String[] args) {
		
		int portNum;
		String registryURL;

        try {
          
            portNum = 1099;
           
//             start a security manager - this is needed if
//             stub downloading is in use for this application.
//             Te following sentence avoids the need to use
//             the option -DJava.security.policy=..." when launching the clinet

            startRegistry(portNum);
            
            System.out.println("Port: 1099.");
            
            //se le pasará este objeto a los clientes
            ImplServidorJuegoRMI exportedObj = new ImplServidorJuegoRMI();
            
            //El servidor registra exportedObj (el objeto remoto)
            registryURL = "rmi://localhost:" + portNum + "/flota";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server registered.");
            System.out.println("FlotaServidorRMI Server ready.");
        }// end try
        catch (Exception re) {
            System.out.println("Exception in ServidorFlotaRMI.main: " + re);
        } // end catch
    } // end main

	
    // This method starts a RMI registry on the local host, if
    // it doesn't already exists at the specified port number.
    private static void startRegistry(int RMIPortNum) throws RemoteException {
    	
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();  // This call will throw an
            //exception if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    } // end startRegistry

	
}
