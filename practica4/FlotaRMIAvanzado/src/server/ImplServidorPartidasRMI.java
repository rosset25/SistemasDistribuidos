package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IntServidorPartidasRMI;

public class ImplServidorPartidasRMI extends UnicastRemoteObject implements IntServidorPartidasRMI {
	
	Partida partida;
	
	//super(), ya que extendemos de la clase UnicastRemoteObject
	public ImplServidorPartidasRMI() throws RemoteException {
		super();
		partida = null;
	}
	
	//Desde aqu� se llamar� a todos los m�todos de la clase Partida (la original)
	@Override
	public void nuevaPartida(int nFilas, int nColumnas, int nBarcos) throws RemoteException {
		partida = new Partida(nFilas, nColumnas, nBarcos);
		
	}

	@Override
	public int pruebaCasilla(int fila, int columna) throws RemoteException {
		return partida.pruebaCasilla(fila, columna);
	}

	@Override
	public String getBarco(int idBarco) throws RemoteException {
		return partida.getBarco(idBarco);
	}

	@Override
	public String[] getSolucion() throws RemoteException {
		return partida.getSolucion();
	}

	
}
