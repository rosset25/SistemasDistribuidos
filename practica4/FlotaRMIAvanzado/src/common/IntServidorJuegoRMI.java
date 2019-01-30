package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntServidorJuegoRMI extends Remote {
	
	/**
	 * Crea una nueva partida
	 * @return							referencia a la interfaz remota que permite jugar la partida
	 * @throws RemoteException
	 */
   public IntServidorPartidasRMI nuevoServidorPartidas() throws RemoteException;
   
   /**
 	 * Permite al cliente/jugador proponer una partida y registrarse para recibir un callback cuando otro cliente la acepte
 	 * @param	nombreJugador			nombre del jugador que propone la partida
 	 * @param	callbackClientObject	referencia al objeto del jugador que usara el servidor para hacer el callback
 	 * @return							valor logico indicando si se ha podido anyadir el jugador al registro
 	 * @throws	RemoteException
 	 */
  public boolean proponPartida( String nombreJugador, IntCallbackCliente callbackClientObject) throws RemoteException;
   
   
   /**
 	 * Permite a un cliente/jugador eliminar su registro para callback
 	 * @param	nombreJugador			nombre del jugador que propuso la partida
 	 * @param	callbackClientObject	referencia al objeto del jugador a eliminar
 	 * @return							valor logico indicando si se ha podido eliminar el jugador del registro
 	 * @throws	RemoteException
 	 */
   public boolean borraPartida( String nombreJugador) throws RemoteException;
   
   /**
 	 * Lista las partidas propuestas
 	 * @return							vector de cadenas con los nombres de los jugadores que han propuesto partidas
 	 * @throws	RemoteException
 	 */
   public String[] listaPartidas() throws RemoteException;
   
   /**
 	 * Acepta jugar una de las partidas propuestas
 	 * @param	nombreJugador	nombre del jugador que acepta la partida
 	 * @param	nombreRival		nombre del jugador que propuso la partida
 	 * @return					valor logico indicando si se ha podido aceptar la partida
 	 * @throws	RemoteException
 	 */
   public boolean aceptaPartida(String nombreJugador, String nombreRival) throws RemoteException;
	
}
