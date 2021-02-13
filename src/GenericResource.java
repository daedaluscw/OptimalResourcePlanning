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
		private resourceNode nextNode; //Points to the next resource node in the list
		private resourceNode previousNode;	//Points to the previous node in the list
		private T quantityAvalible;	//How much of the resource is available?
		private String resourceName;	//What is the mane of this resource.
		private Boolean resourceConsumable;	//Is the resource consumed in the process of being used. ie a hammer can be used many times, but money only once.
	}
	
	resourceNode currentPointer;
	//Constructors
	
	public GenericResource( T quanity, String name) 
	{
		currentPointer = new resourceNode();
		currentPointer.nextNode = null;
		currentPointer.previousNode = null;
		currentPointer.resourceName = name;
		currentPointer.quantityAvalible = quanity;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
