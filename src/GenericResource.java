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
	public Boolean addResource ( T quantity, String name, Boolean consumable)
	{
		Boolean resultOfOperation = false; //The return value from the operation used to determine the sucess of the operation
		
		if (checkTtype(quantity) == false )
		{
			return false;
			//The value of the T is not an acceptable type
		}
		
		if ( name.isEmpty() == true)
		{
			return false;
			//The name string is empty and the operation can not be completed
		}
		
		//The inital easy tests have passed and now a node has to be constructed to be added to the list
		
		resourceNode tempNode = new resourceNode();
		
		resultOfOperation = setNameOfResource( tempNode, name);	//Calls the function to set the name of the node
		if (resultOfOperation == false)
		{
			return false;	//Check the return to ensure that the name of the temporary node was set.
		}
		
		resultOfOperation = setQuantityOfResource( tempNode, quantity);
		if (resultOfOperation == false)
		{
			return false;	//Check the return to ensure that the quantity of the temporary node was set.
		}
		
		resultOfOperation = setConsumable( tempNode, consumable);
		if (resultOfOperation == false)
		{
			return false;	//Check the return to ensure that the consumable value of the temporary node was set.
		}
		
		return true;
	}
	
	private boolean checkTtype ( T type)
	{
		Boolean returnValue = false; //Check to see if the type is one of the types that are supported as a value.
		
		if ( type instanceof Integer)
		{
			return true;
		}
		
		if (type instanceof Float)
		{
			return true;
		}
		
		if (type instanceof Long)
		{
			return true;
		}
		
		return returnValue;
	}
		
		
		
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
