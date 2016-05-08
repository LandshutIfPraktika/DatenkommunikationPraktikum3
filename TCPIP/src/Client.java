import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Created by Georg on 07.05.2016.
 */
public class Client {
    public static void main(String[] args) {
        try (final Socket socket = new Socket("localhost", 4040)) {
            final InputStreamReader networkInput = new InputStreamReader(socket.getInputStream());
            final Scanner localInput = new Scanner(System.in);
            final PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            socket.setSoTimeout(500);

            while (localInput.hasNext()) {
                final String line = localInput.next();
                output.write(line);
                output.flush();
                try {
                    final char[] answerBuffer = new char[128];
                    networkInput.read(answerBuffer, 0, 128);
                    System.out.println(new String(answerBuffer));
                } catch (SocketTimeoutException ex) {
                    System.out.println("no Answer");
                }
                if ("quit".equals(line)) {
                    socket.close();
                    System.exit(0);
                }
            }


        } catch (Exception ex) {

        }
    }
}
