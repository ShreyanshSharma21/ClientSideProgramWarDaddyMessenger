import javax.swing.JFrame;
public class ClientTest {
	
	public static void main(String [] args)
	{
		Client tango;
		tango = new Client("127.0.0.1");
		tango.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tango.startRunning();
	}
	

}
