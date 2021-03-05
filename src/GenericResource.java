import java.util.ArrayList;

/*
 * Programmer: Christopher Wells
 * Date Started: 02/13/2021
 * Date Completed:
 * 
 * Version: 0.0.1 
 * 
 * 	This code will define the generic structure for a resource element. The resources will need a resource manager for the management and 
 * allocation of the resources to a project. 
 */

public class GenericResource<T> {

	//Data Definitions
	
	private class resourceNode {
		private T quantityAvalible;	//How much of the resource is available?
		private String resourceName;	//What is the mane of this resource.
		private Boolean resourceConsumable;	//Is the resource consumed in the process of being used. ie a hammer can be used many times, but money only once.
	}
	
	ArrayList<resourceNode> resourceList = new ArrayList<resourceNode> ();
	
	//Constructors
	
	//Setters 
	 /*
	  * Adds a resource element to the list is it does not already exist in the list.
	  */
	public Boolean addResource ( T quantity, String name)
	{
		Boolean resultOfOperation = false; //The return value from the operation
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
