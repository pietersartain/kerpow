public class kerpowObjectManager {

	public static int verbosityLevel;

	// Objects
	public static dbase runDB;
	public static preferences preferences;
	public static music music;
	private static admin admin;

	public kerpowObjectManager()
	{
	initmain();
	}
	
	private void initmain()
	{
		// Preferences
		preferences = new preferences();
		verbosityLevel = preferences.getVerbosity();
		
		// DB
		runDB = new dbase();
		
		// Admin
		admin = new admin();
		
		// Music
		music = new music();
		music.setColumns(preferences.getColumns());
	}

}
