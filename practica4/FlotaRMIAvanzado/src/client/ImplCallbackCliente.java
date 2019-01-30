package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IntCallbackCliente;

public class ImplCallbackCliente extends UnicastRemoteObject implements IntCallbackCliente{
	
	public ImplCallbackCliente() throws RemoteException{
		super();
	}
	
	/*
	 * Esta notificación avisará, al jugador que había propuesto partida, de que alguien la
	 * ha aceptado. Al método se le envía el nombre de quién la ha aceptado, y luego al otro
	 * jugador se le indicará en el mensaje el apodo de su rival
	 */
	public String notificaCliente(String apodoRival) throws RemoteException {
		
		String mensaje = "El rival " + apodoRival + " ha aceptado tu partida";
		return mensaje;
	}
		
}
