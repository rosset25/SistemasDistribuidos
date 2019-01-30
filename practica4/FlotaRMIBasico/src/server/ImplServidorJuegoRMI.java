package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;

public class ImplServidorJuegoRMI extends UnicastRemoteObject implements IntServidorJuegoRMI {
	
	//el objeto remoto que se asociará al cliente que solicite una partida
	ImplServidorPartidasRMI partidaCliente;
	
	public ImplServidorJuegoRMI() throws RemoteException {
		super();
	}
	
	//Crea una nueva partida, devuelve una referencia
	@Override
	public IntServidorPartidasRMI nuevoServidorPartidas() throws RemoteException {
		partidaCliente = new ImplServidorPartidasRMI();
		return partidaCliente;
	}
	

}
