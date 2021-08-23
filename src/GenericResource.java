import java.util.ArrayList;
import java.util.Date;

/*
 * Programmer: Christopher Wells
 * Date Started: 02/13/2021
 * Updated: 08/15/2021 
 * 	1. Preparation for the creation of the Generic Resource Manager.
 * Updated: 08/232021
 * 1. Added Handler for time based resources that have a window of time to be used
 * 2. Added a data type for the date a resource is acquired
 * 3. Added a data type to handle the deprecation in quantity over time
 * 4. Added a data type for the expiration of the resource.
 * 5. Added a data type for the type and subtype of a resource. 
 * Date Completed:
 * 
 * Version: 0.0.2 
 * 
 * 	This code will define the generic structure for a resource element. The resources will need a resource manager for the management and 
 * allocation of the resources to a project. 
 */

public class GenericResource {

	//Data Definitions
	
	/*
	 * Defines the basic node that will store the resource information information.
	 */
	private class resourceNode {
		private int quantityAvalible;	//How much of the resource is available?
		private String resourceName;	//What is the mane of this resource.
		private Boolean resourceConsumable;	//Is the resource consumed in the process of being used. ie a hammer can be used many times, but money only once.
		private int timeBasedResource; //Stores if this is a time base resource 0=false 1=true (date based) 2=expiration by date Left room for other states
		private int dateCodeEntered; //Date the resource was acquired
		private int deprecatonAmount; //Handles cases where resource s consumed a little each day
		private int experationDateCode; //Handles cases where there is a window to use the resource.
		private int subType; 	//Will hold the subtype number for the resource
		
	}
	
	private ArrayList<resourceNode> resourceList = new ArrayList<resourceNode> ();
	private int resType; 	//Holds the type code for the resource. This will be specific to this list
	private int subtypeIndex; //Holds the last subtype defined number. Note: deleted sub-type number will not be re-assigned. 
	//Constructors
	
	//Setters 
	 /*
	  * Adds a resource element to the list is it does not already exist in the list.
	  */
	public boolean addResource ( int quantity, String name, boolean consumable, int timeBased, int dateEnteredCode, int depAmount, int expDateCode)
	{
		boolean resultOfOperation = false; //The return value from the operation used to determine the success of the operation
		
		//Check to see if the value provided was within acceptable bounds. Will have to deal with loans and negatives later
		if (checkValue(quantity) == false )
		{
			System.out.println("Quantity Failed");
			return false;
			//The value of the T is not an acceptable type
		}
		
		//Check to see if the element being added is already in the list
		if ( checkInList(name) != null)
		{
			System.out.println("Element is in the list Failed");
			return false;
		}
		
		if ( name.isEmpty() == true)
		{
			System.out.println("Name Failed");
			return false;
			//The name string is empty and the operation can not be completed
		}
		
		//The initial easy tests have passed and now a node has to be constructed to be added to the list
		
		resourceNode tempNode = new resourceNode();
		
		resultOfOperation = setNameOfResource( tempNode, name);	//Calls the function to set the name of the node
		if (resultOfOperation == false)
		{
			System.out.println("Set Name Failed");
			return false;	//Check the return to ensure that the name of the temporary node was set.
		}
		
		resultOfOperation = setQuantityOfResource( tempNode, quantity);
		if (resultOfOperation == false)
		{
			System.out.println("Set Quantity Failed");
			return false;	//Check the return to ensure that the quantity of the temporary node was set.
		}
		
		resultOfOperation = setConsumable( tempNode, consumable);
		if (resultOfOperation == false)
		{
			System.out.println("Set Consumable Failed");
			return false;	//Check the return to ensure that the consumable value of the temporary node was set.
		}
		System.out.println("Adding element to the list");
		resourceList.add(tempNode);
		
		return true;
	}
	
	private boolean checkValue( int type)
	{
				
		if ( type < 0)	
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
		
	public boolean setNameOfResource( resourceNode tempNode, String name)
	{
		boolean resultOfOperation = false; //Stores the value for the result of the operation
		
		tempNode.resourceName = name;
		
		if ( tempNode.resourceName.isEmpty())
		{
			return resultOfOperation;
		}
		
		return true;
	}
		
		
	public boolean setQuantityOfResource( resourceNode tempNode, int quantity)
	{
		
		
		tempNode.quantityAvalible = quantity;
		
		return true;		
	}
	
	public boolean setConsumable( resourceNode tempNode, boolean consumable)
	{
		tempNode.resourceConsumable = consumable;
		return true;
	}
	
	public void printResourceList()
	{
		int listSize = this.resourceList.size();
		int index = 0;
		resourceNode tempNode = new resourceNode();
		System.out.println("Resource List size = " + listSize);
		while ( index < listSize)
		{
			tempNode = this.resourceList.get(index);
			System.out.println("------------------------------------------------------------");
			System.out.println("Name: " + tempNode.resourceName);
			System.out.println("Quantity: " + tempNode.quantityAvalible);
			if ( tempNode.resourceConsumable)
			{
				System.out.println("Consumable: True");
			}
			else
			{
				System.out.println("Consumable: False");
			}
			index++;
		}
	}
	
	public int addToResources ( resourceNode tempNode, int quantity)
	{
		
		
		if ( tempNode == null)
		{
			return -1;	//Error no node passed
		}
		else
		{
			int temp = tempNode.quantityAvalible;
			if ( quantity < 0)
			{
				return -2;	//Invalid quantity flag
			}
			else
			{
				tempNode.quantityAvalible = tempNode.quantityAvalible + quantity;
				if ( tempNode.quantityAvalible < 0)
				{
					tempNode.quantityAvalible =  temp;
					return -3; 	//Overflow error
				}
				else
				{
					return 0;
				}
				
			}
		}
		
		
	}
	
	public int consumeResource( resourceNode tempNode, int quantity)
	{
		if (tempNode == null)
		{
			return -1;	//Null pointer error
		}
		
		if ( quantity > tempNode.quantityAvalible)
		{
			return -2;	//Insufficient quantity error
		}
		else
		{
			if (tempNode.resourceConsumable)
			{
				tempNode.quantityAvalible = tempNode.quantityAvalible - quantity;
				return 0;
			}
			else
			{
				return 0;
			}
			
		}
	}
	public int removeElement(String name)
	{
		resourceNode temp;
		temp = checkInList(name);
		int result = deleteAResource(temp);
		return result;
	}
	
	private int deleteAResource( resourceNode tempNode)
	{
		if (tempNode == null)
		{
			return -1;	//Null pointer error
		}
		
		this.resourceList.remove(tempNode);
		return 0;
	}
	
	private resourceNode checkInList(String name)
	{
		if (this.resourceList.isEmpty())
		{
			return null;
		}
		else
		{
			int size = this.resourceList.size();
			int index = 0;
			resourceNode temp;
			while ( index < size)
			{
				temp = this.resourceList.get(index);
				if ( temp.resourceName.equalsIgnoreCase(name))
				{
					return temp;
				}
				else
				{
					index++;
				}
			}
			return null;
		}
		
		
	}
	
	//Get the 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenericResource test = new GenericResource();
		test.addResource(25, "Dollars", true);
		test.addResource(30, "Hours", true);
		test.addResource(1, "hammer", false);
		
		test.printResourceList();
		
		int result = test.removeElement("Bill");
		if ( result < 0)
		{
			System.out.println("Error deleting bill");
		}
		result = test.removeElement("Hours");
		if ( result < 0)
		{
			System.out.println("Error deleting Hours");
		}
		System.out.println("Program Done");
	}

}
