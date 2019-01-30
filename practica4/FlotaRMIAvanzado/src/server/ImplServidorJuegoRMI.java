package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import common.IntCallbackCliente;
import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;

public class ImplServidorJuegoRMI extends UnicastRemoteObject implements IntServidorJuegoRMI {

	// el objeto remoto que se asociará al cliente que solicite una partida
	private ImplServidorPartidasRMI partidaCliente;
	private ConcurrentHashMap<String, IntCallbackCliente> listaClientes; /* Mapa sincronizado, por tanto no hace falta 
																   		  * el uso de cerrojos (synchronized en los métodos)
																   		  */

	public ImplServidorJuegoRMI() throws RemoteException {
		super();
		listaClientes = new ConcurrentHashMap<>();
	}

	// Crea una nueva partida, devuelve una referencia
	@Override
	public IntServidorPartidasRMI nuevoServidorPartidas() throws RemoteException {
		partidaCliente = new ImplServidorPartidasRMI();
		return partidaCliente;
	}
	
	   /**
	 	 * Permite al cliente/jugador proponer una partida y registrarse para recibir un callback cuando otro cliente la acepte
	 	 * @param	nombreJugador			nombre del jugador que propone la partida
	 	 * @param	callbackClientObject	referencia al objeto del jugador que usara el servidor para hacer el callback
	 	 * @return							valor logico indicando si se ha podido anyadir el jugador al registro
	 	 * @throws	RemoteException
	 	 */
	
	@Override
	public boolean proponPartida(String nombreJugador, IntCallbackCliente callbackClientObject) throws RemoteException{
		
		if (listaClientes.putIfAbsent(nombreJugador, callbackClientObject) == null) {
			System.out.println("Nuevo cliente registrado: " + nombreJugador); //se indica en el servidor qué cliente se ha registrado
			return true;
		}	
		System.out.println("El cliente ya está registrado"); //se imprime en el servidor si el cliente ya existía
		return false;
	}
	

	/**
	 * Permite a un cliente/jugador eliminar su registro para callback
	 * 
	 * @param nombreJugador        nombre del jugador que propuso la partida
	 * @param callbackClientObject referencia al objeto del jugador a eliminar
	 * @return valor logico indicando si se ha podido eliminar el jugador del registro
	 * @throws RemoteException
	 */
	public boolean borraPartida(String nombreJugador) throws RemoteException{
		
		if (listaClientes.remove(nombreJugador) != null ) {
			System.out.println("El jugador " + nombreJugador + " ha sido borrado."); //se imprime en el servidor
			return true;
		}
		System.out.println("El jugador " + nombreJugador + " no existe."); // se imprime en el servidor
		return false;
	}

	/**
	 * Lista las partidas propuestas
	 * 
	 * @return vector de cadenas con los nombres de los jugadores que han propuesto partidas
	 * @throws RemoteException
	 */

	public String[] listaPartidas() throws RemoteException{
		
		String [] clientes= new String[listaClientes.size()];
		
		/* Se recorre la lista de las claves del mapa (nombres de los jugadores)
		 * y se guardan en un vector para que posteriormente el cliente que reciba
		 * la lista lo imprima por pantalla, así sabrá los clientes a la espera de
		 * encontrar rivales
		 */
		int i=0;
		for (String cliente : listaClientes.keySet()) {
			System.out.println(cliente);
			clientes[i++] = cliente;
		}		
		
		return clientes;
	}

	/**
	 * Acepta jugar una de las partidas propuestas
	 * 
	 * @param nombreJugador nombre del jugador que acepta la partida
	 * @param nombreRival   nombre del jugador que propuso la partida
	 * @return valor logico indicando si se ha podido aceptar la partida
	 * @throws RemoteException
	 */
	public boolean aceptaPartida(String nombreJugador, String nombreRival) throws RemoteException{
		
		IntCallbackCliente jugador1 = listaClientes.get(nombreJugador);
		IntCallbackCliente jugador2 = listaClientes.get(nombreRival);
		
		if(jugador1 != null && jugador2 != null) {
			jugador2.notificaCliente(nombreJugador);
			listaClientes.remove(jugador1);
			listaClientes.remove(jugador2);
			return true;
		}
		
		return false;
	}

}
