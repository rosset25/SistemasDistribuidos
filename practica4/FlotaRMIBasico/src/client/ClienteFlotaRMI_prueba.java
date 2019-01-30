package client;

import java.rmi.Naming;

import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;

public class ClienteFlotaRMI_prueba {
	
	
	public static void main(String[] args) {
		
		try {
			
			int portNum = 1099;
			
			//misma dirección que el servidor
			String registryURL = "rmi://localhost:" + portNum + "/flota";
			/* El cliente obtiene del registro un stub (juegoObj) del objeto remoto (la partida)
			 * Este stub se guarda en una referencia a la interfaz IntServidorJuegoRMI (registryURL)
			 */
			IntServidorJuegoRMI juegoObj = (IntServidorJuegoRMI) Naming.lookup(registryURL); 
			
			IntServidorPartidasRMI partida =juegoObj.nuevoServidorPartidas();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
