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

public class GenericResource {

	//Data Definitions
	
	private class resourceNode {
		private int quantityAvalible;	//How much of the resource is available?
		private String resourceName;	//What is the mane of this resource.
		private Boolean resourceConsumable;	//Is the resource consumed in the process of being used. ie a hammer can be used many times, but money only once.
	}
	
	ArrayList<resourceNode> resourceList = new ArrayList<resourceNode> ();
	
	//Constructors
	
	//Setters 
	 /*
	  * Adds a resource element to the list is it does not already exist in the list.
	  */
	public boolean addResource ( int quantity, String name, boolean consumable)
	{
		boolean resultOfOperation = false; //The return value from the operation used to determine the success of the operation
		
		if (checkValue(quantity) == false )
		{
			System.out.println("Quantity Failed");
			return false;
			//The value of the T is not an acceptable type
		}
		
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
