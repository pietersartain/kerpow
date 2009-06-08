public class jamisii
{

    public static void main(String[] args)
    {
	new jamisii().parseArguments(args);

    }

    private void parseArguments(String[] args)
    {
        int length = args.length;

        for (int index = 0; index < length; index++)
        {
            if (args[index].equals("--update"))
	    {
	    	System.out.println("Update the database");
            }
	    else if (args[index].equals("--save"))
	    {
	    	System.out.println("Save user preferences");
	    }
	    else if (args[index].equals("--load"))
	    {
	    	System.out.println("Load user preferences");
	    }
	    else if (args[index].equals("--display"))
	    {
	    	System.out.println("Show database contents");
	    }

        }
    }
}
