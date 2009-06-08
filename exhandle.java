public class exhandle
{

//private int verbosityLevel;

	public exhandle(String message, Throwable e, int verbosityLevel)
	{
		int flag = 0;
		String eX = "";
		if (e != null) { eX = e.toString(); }

		// Print everything that gets sent here.
		if (verbosityLevel > 1)
		{
			System.out.println(message + eX);
		}
		// Print only things that aren't usual ...
		else if (verbosityLevel == 1) {
			if (e != null) {
				//Usual list goes here
				if (e.toString().equals("SQL Exception: Invalid cursor state - no current row.")) { flag = 0; }
				else if (eX.equals("null")) { flag = 0; }
				else { flag = 1; }
			}
			
			if (flag == 1) { System.out.println(message + eX); }
		}
		
		//System.out.println("This has come from exhandle!");

	}
	
	/*
	public void setVerbosity(int verbosity)
	{
		verbosityLevel = verbosity;
	}
	*/
}
