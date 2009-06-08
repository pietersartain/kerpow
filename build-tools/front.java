import cliK.*;
import guiK.*;

public class front
{
	public static void main(String[] args)
	{
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
				if (args.length == 0) {
					new kerpowgui();
				} else if (args[0].equals("--gui"))	{
					new kerpowgui();
				} else {
					new kerpowgui();
				}
//			}
//		});
	}
}
