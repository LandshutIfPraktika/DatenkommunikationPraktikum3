import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Georg on 07.05.2016.
 */
public class Server implements Runnable {
    private final Socket socket;
    private ServerState state;


    public Server(final Socket socket) {
        this.socket = socket;
        this.state = ServerState.Init;
    }

    @Override
    public void run() {
        System.out.println("started");
        try (final InputStreamReader inputReader = new InputStreamReader(new BufferedInputStream(socket.getInputStream()));
             final PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true)) {
            while (!socket.isClosed()) {
                final char[] buffer = new char[128];
                inputReader.read(buffer,0,128);
                final String incoming = new String(buffer).trim();
                if("quit".equals(incoming)){
                    socket.close();
                    return;
                }
                System.out.println(incoming);
                final ServerState newState = this.state.process(incoming);
                final String message = this.state.getAnswer(newState);
                if (message != null) {
                    outputWriter.println(message);
                    outputWriter.flush();
                }
                this.state = newState;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


    public static void main(String[] args) {
        try (final ServerSocket socket = new ServerSocket(4040)) {
            final ArrayList<Thread> threads = new ArrayList<>();
            final Thread first = new Thread(new Server(socket.accept()));
            threads.add(first);
            first.start();
            socket.setSoTimeout(60 * 1000);
            while (threads.stream().filter(s -> s.isAlive()).findAny().isPresent()) {
                try {
                    final Thread thread = new Thread(new Server(socket.accept()));
                    threads.add(thread);
                    thread.start();
                } catch (SocketTimeoutException ex) {
                    System.err.println("timed out");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
