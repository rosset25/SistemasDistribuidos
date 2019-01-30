package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.Partida;

public interface IntServidorPartidasRMI extends Remote {

	public void nuevaPartida(int nFilas, int nColumnas, int nBarcos) throws RemoteException;
	
	public int pruebaCasilla(int fila, int columna) throws RemoteException;
	
	public String getBarco(int id) throws RemoteException;
	
	public String[] getSolucion() throws RemoteException;
	
}
