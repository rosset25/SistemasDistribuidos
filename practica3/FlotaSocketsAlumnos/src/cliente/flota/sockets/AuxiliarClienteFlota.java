
package cliente.flota.sockets;

import java.net.*;
import java.io.*;

import comun.flota.sockets.*;

/**
 * Esta clase implementa el intercambio de mensajes asociado a cada una de las
 * operaciones basicas que comunican cliente y servidor
 */

public class AuxiliarClienteFlota {
	private MyStreamSocket mySocket;
	private InetAddress serverHost;
	private int serverPort;

	/**
	 * Constructor del objeto auxiliar del cliente Crea un socket de tipo
	 * 'MyStreamSocket' y establece una conexión con el servidor 'hostName' en el
	 * puerto 'portNum'
	 * 
	 * @param hostName nombre de la máquina que ejecuta el servidor
	 * @param portNum  numero de puerto asociado al servicio en el servidor
	 */
	AuxiliarClienteFlota(String hostName, String portNum) throws SocketException, UnknownHostException, IOException {
	
		//se crea el socket con el que nos comunicaremos
		serverHost = InetAddress.getByName(hostName);
		serverPort = Integer.parseInt(portNum);
		mySocket = new MyStreamSocket(serverHost, serverPort);
	} // end constructor

	/**
	 * Usa el socket para enviar al servidor una petición de fin de conexión con el
	 * formato: "0"
	 * 
	 * @throws IOException
	 */
	public void fin() throws IOException {
		//System.out.println("Adiós");
		mySocket.sendMessage("0"); //envía la opción 0 para finalizar conexión
	} // end fin

	/**
	 * Usa el socket para enviar al servidor una petición de creación de nueva
	 * partida con el formato: "1#nf#nc#nb"
	 * 
	 * @param nf número de filas de la partida
	 * @param nc número de columnas de la partida
	 * @param nb número de barcos de la partida
	 * @throws IOException
	 */
	public void nuevaPartida(int nf, int nc, int nb) throws IOException {
		//envía los parámetros de la nueva partida
		mySocket.sendMessage("1#" + nf + "#" + nc + "#" + nb);
	} // end nuevaPartida

	/**
	 * Usa el socket para enviar al servidor una petición de disparo sobre una
	 * casilla con el formato: "2#f#c"
	 * 
	 * @param f fila de la casilla
	 * @param c columna de la casilla
	 * @return resultado del disparo devuelto por la operación correspondiente del
	 *         objeto Partida en el servidor.
	 * @throws IOException
	 */
	public int pruebaCasilla(int f, int c) throws IOException {
		// envía el los parámetros de la casilla seleccionada (opción 2)
		String mensaje = "2#" + f + "#" + c;
		mySocket.sendMessage(mensaje);
		
		//recibe el barco que debe hundir, o agua o tocado del servidor
		String res = mySocket.receiveMessage();
		return Integer.parseInt(res); // Cambiar por el retorno correcto
	} // End getCasilla

	/**
	 * Usa el socket para enviar al servidor una petición de los datos de un barco
	 * con el formato: "3#idBarco"
	 * 
	 * @param idBarco identidad del Barco
	 * @return resultado devuelto por la operación correspondiente del objeto
	 *         Partida en el servidor.
	 * @throws IOException
	 */
	public String getBarco(int idBarco) throws IOException {
		//envía la id del barco para obtener su información y poder hundirlo (opción 3)
		mySocket.sendMessage("3#" + idBarco);
		//recibe del servidor una cadena con los datos del barco
		String res = mySocket.receiveMessage();
		return res; 
	} // End getCasilla

	/**
	 * Usa el socket para enviar al servidor una petición de los datos de todos los
	 * barcos con el formato: "4"
	 * 
	 * @return resultado devuelto por la operación correspondiente del objeto
	 *         Partida en el servidor
	 * @throws IOException
	 */
	public String[] getSolucion() throws IOException {
		//envía 4 para obtener la solución (opción 4)
		mySocket.sendMessage("4");
		
		//el primer parámetro que recibe es la cantidad de barcos a hundir
		int numBarcos = Integer.parseInt(mySocket.receiveMessage());
		String[] soluciones = new String[numBarcos];

		//los siguientes que recibe son la info. de los barcos
		for (int i = 0; i < numBarcos; i++) {
			soluciones[i] = mySocket.receiveMessage();
		}
		return soluciones; 
	} // end getSolucion

} // end class
