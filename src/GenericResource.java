import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/*
 * Programmer: Christopher Wells
 * Date Started: 02/13/2021
 * Updated: 06/04/2022
 * 	1. Preparation for the creation of the Generic Resource Manager.
 * Updated: 08/23/2021
 * 1. Added Handler for time based resources that have a window of time to be used
 * 2. Added a data type for the date a resource is acquired
 * 3. Added a data type to handle the deprecation in quantity over time
 * 4. Added a data type for the expiration of the resource.
 * 5. Added a data type for the type and subtype of a resource. 
 * Updated: 08/29/2021
 * 1. Worked on updating the add resource function to support new parameters for the resource.
 * Updated: 09/01/2021
 * 1. Worked on the helper functions.
 * Updated: 09/16/2022
 * 1. Changed Date format to YYYYMMDD	(There will need to be a day of the year for depricaton calculations)
 * 2. Fixed some of the functions to support the addition of elements to the list.
 * 3. Ran three basic tests.
 * Updates: 09/17/2022
 * 1. Started Work on setting the deprecation days number.
 * 2. Got the code to work to break down the date code.
 * Updates: 09/28/2022
 * 1. Added function to convert list to CVS.
 * Updates: 10/7/2022
 * 1. Added function to convert an incoming CVS String to a Resource Element
 * Date Completed:
 * 
 * Version: 0.0.2 
 * 
 * 	This code will define the generic structure for a resource element. The resources will need a resource manager for the management and 
 * allocation of the resources to a project.
 * Update: 06/04/2022
 * 1.  
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
		private float deprecatonAmount; //Handles cases where resource s consumed a little each day
		private long experationDateCode; //Handles cases where there is a window to use the resource.
		private int resourceNumber;	//The resource a number 
		private int dayofTheYear;	//What day of the years was this added. This is for the deprication calculations
	}
	
	private ArrayList<resourceNode> resourceList = new ArrayList<resourceNode> (); 
	private int maxResourcesDefined = 100;	//Holds the number of the resource types defined.
	private final int DAYS_OF_YEAR = 365;	//Holds the number of days in the year.
	//Constructors
	
	//Setters 
	 /*
	  * Adds a resource element to the list is it does not already exist in the list.
	  * This method allows for all of the elements to be added to the node at one time.
	  */
	public boolean addResource ( int quantity, String name, boolean consumable, int timeBased, int dateEnteredCode, float depAmount, long expDateCode, int resNumber)
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
		if ( checkInListByName(name) != null)
		{
			System.out.println("Element is in the list Failed. Element cannot be added twice");
			return false;
		}
		
		//Check to ensure that the name for the resource provided is not empty
		if ( name.isEmpty() == true)
		{
			System.out.println("Name Failed");
			return false;
			//The name string is empty and the operation can not be completed
		}

		//Check to see if the resource number is valid
		if ( checkResNumber(resNumber) == false)
		{
			System.out.println("Resource number is not valid");
			return false;
		}
		
		//Check to see that the code for the degradation of the resource is correct.
		// 0 = A resource that is not effected by time
		// 1 = A resource that is subject to degradation
		// 2 = A resource that is subject to expiration, but does not degrade
		// 3 = A resource that expires and degrades.
		// There is space for the addition of new types of change.
		if (checkTimeCode(timeBased) == false)
		{
			System.out.println("The time code provided is invalid");
			return false;
			
		}
		
		//Verify the date provide for the acquisition of the resource is an acceptable value.
		if ( checkDateCode(dateEnteredCode))
		{
			System.out.println("The date entered code is not valid");
			return false;
			
		}
		
		//Verify the value given for the deprication value is acceptable
		if ( checkDepCode( depAmount, timeBased) == false)
		{
			System.out.println("The deprication code or time code is incorrect for the depreication code");
			return false;
		}
		
		//Check the experation code
		if ( checkExpCode( expDateCode, timeBased, dateEnteredCode) == false)
		{
			System.out.println( "The experation code or time code is invald");
			return false;
		}
		
		//Check of the resource number is valid
		if (( checkInListByNumber(resNumber) == true) && (this.resourceList.isEmpty() == false))
		{
			System.out.println("Error the resource number is already in the list");
			return false;
			
		}
		
		//The initial easy tests have passed and now a node has to be constructed to be added to the list
		//Each element will be added to the node individually, and operations will be tested.
		
		resourceNode tempNode = new resourceNode();
		
		//Set the name of the resource node
		resultOfOperation = setNameOfResource( tempNode, name);	//Calls the function to set the name of the node
		if (resultOfOperation == false)
		{
			System.out.println("Set Name Failed");
			return false;	//Check the return to ensure that the name of the temporary node was set.
		}
		
		//Set the quantity of the resource 
		resultOfOperation = setQuantityOfResource( tempNode, quantity);
		if (resultOfOperation == false)
		{
			System.out.println("Set Quantity Failed");
			return false;	//Check the return to ensure that the quantity of the temporary node was set.
		}
		
		//Set the consumable variable
		resultOfOperation = setConsumable( tempNode, consumable);
		if (resultOfOperation == false)
		{
			System.out.println("Set Consumable Failed");
			return false;	//Check the return to ensure that the consumable value of the temporary node was set.
		}
		
		//Set the time code
		resultOfOperation = setTimeCode( tempNode, timeBased);
		if (resultOfOperation == false)
		{
			System.out.println("Set the time code has failed");
			return false;
		}
		
		//Set the date that the resource was aquired.
		resultOfOperation = setDateAquired( tempNode, dateEnteredCode);
		if ( resultOfOperation == false)
		{
			System.out.println("Set the date the resource aquired code failed");
			return false;
		}
		
		//Set the deprication amount.
		resultOfOperation = setDepAmount( tempNode, depAmount);
		if (  resultOfOperation == false)
		{
			System.out.println("Error setting the deprication amount.");
			return false;
		}
		
		//Set the experation date of the resource.
		if (( timeBased == 2) || ( timeBased == 3))
		{
			if ( expDateCode < 20210101)	//this is today, so no resources could be added before the program exists
			{
				System.out.println("Error the experation date is invald");
				return false;
			}
			else
			{
				resultOfOperation = setExpDate( tempNode, expDateCode);
				if ( resultOfOperation == false)
				{
					System.out.println("Error in  the setting of the experaton date");
					return false;
				}
			}
		}
		int year = 0;
		int month = 0;
		int day = 0;
		year = (int)(dateEnteredCode / 10000);
		month = (int)((dateEnteredCode - (year * 10000)) / 100);
		day = (int)(dateEnteredCode - ((year * 10000) + (month * 100)));
		System.out.println("Year: " + year + " Month: " + month + " day: " + day);
		System.out.println("Adding element to the list");
		resourceList.add(tempNode);
		
		return true;
	}
	
	/*
	 * This function is used to pull the resource list from the file
	 * Ideally the class will be loaded and will look for the file and if it is not 
	 * there it will start to make the resource list. It will alert and look for the 
	 * user to stop this if there needs to be a file.
	 */
	public boolean loadResourceListFromFile()
	{
		return false;
	}
	
	/*
	 * This function will be run every so often to back the file up, but also at the 
	 * end of the program use. This will store the file.
	 */
	public boolean saveResourceListToFile()
	{
		return false;
	}
	/*****************************************************************************************************
	 * Getter Functions
	 * @param tempNode
	 * @param subType
	 */
	
	//Get the quantity of the resource available 
	public int getQuantityAvalibe( resourceNode currentNode)
	{
		return currentNode.quantityAvalible;
	}
	
	//Get the Name of the resource
	public String getResourceName( resourceNode currentNode)
	{
		return currentNode.resourceName;
	}
	
	//Get if the resource is consumable
	public Boolean getResourceConsumable (resourceNode currentNode)
	{
		return currentNode.resourceConsumable;
	}
	
	//Get the code for the tme based resource.
	public int getTimeBasedResource (resourceNode currentNode)
	{
		return currentNode.timeBasedResource;
	}
	
	//Get the date that the resource was acquired
	public long getDateCodeEntered( resourceNode currentNode)
	{
		return currentNode.dateCodeEntered;
	}
	
	//Get the deprecation value
	public float getDeprecationAmount( resourceNode currentNode)
	{
		return currentNode.deprecatonAmount;
	}
	
	//Get the expiration date code
	public long getExperationDateCode( resourceNode currentNode)
	{
		return currentNode.experationDateCode;
	}
	
	//Get the resource number 
	public int getResourceNumber( resourceNode currentNode)
	{
		return currentNode.resourceNumber;
	}
	
	/*
	 * Setter Functions
	 */
	
	//Function sets the name of the resource.
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
	
	//Function sets the quantity of the resource available
	public boolean setQuantityOfResource( resourceNode tempNode, int quantity)
	{
		if (tempNode == null)
		{
			return false;
		}
		tempNode.quantityAvalible = quantity;	
		return true;		
	}
		
	//Function sets the value to identify if the resource is consumable or durable
	public boolean setConsumable( resourceNode tempNode, boolean consumable)
	{
		tempNode.resourceConsumable = consumable;
		return true;
	}
		
	//Function sets the code for if the resource is time based.
	public boolean setTimeCode(resourceNode tempNode, int timeBased) {
	// TODO Auto-generated method stub
		if (( timeBased < 0) || ( timeBased > 3))
		{
			return false;
		}
		if (tempNode == null)
		{
			return false;
		}
		tempNode.timeBasedResource = timeBased;
		return true;
	}

	//Function will set the value of the date the resource is entered.
	public boolean setDateAquired(resourceNode tempNode, int dateEnteredCode) {
		// TODO Auto-generated method stub
		tempNode.dateCodeEntered = dateEnteredCode;
		return true;
	}

	//Function sets the deprcation amount
	public boolean setDepAmount(resourceNode tempNode, float depAmount) {
		// TODO Auto-generated method stub
		tempNode.deprecatonAmount = depAmount;
		return true;
	}

	//Function sets the value for the experation of the resource.
	public boolean setExpDate(resourceNode tempNode, long expDateCode) {
		// TODO Auto-generated method stub
		tempNode.experationDateCode = expDateCode;
		return true;
	}

	//Checks to see if the resource number is in range
	//Checked: 6/3/2022
	private boolean checkResNumber(int resNumber) {
		// TODO Auto-generated method stub
		if ((resNumber < 1) || (resNumber > this.maxResourcesDefined))
		{
			System.out.println("Error the resource number is out of range");
			return false;
		}
		return true;
	}

	public boolean convertListToCSV()
	{
		String returnString = "";	//This will be the string that will be passed to the file for storage.
		int index= 0;
		resourceNode tempNode = new resourceNode();
		if (this.resourceList.isEmpty())
		{
			return false;
			
		}
		else
		{
			while (index < this.resourceList.size())
			{
				tempNode = resourceList.get(index);
				returnString += String.valueOf(tempNode.quantityAvalible);
				returnString += "'";
				returnString += tempNode.resourceName;
				returnString += "'";
				returnString += String.valueOf(tempNode.resourceConsumable);
				returnString += "'";
				returnString += String.valueOf(tempNode.timeBasedResource);
				returnString += "'";
				returnString += String.valueOf(tempNode.dateCodeEntered);
				returnString += "'";
				returnString += String.valueOf(tempNode.deprecatonAmount);
				returnString += "'";
				returnString += String.valueOf(tempNode.experationDateCode);
				returnString += "'";
				returnString += String.valueOf(tempNode.resourceNumber);
				returnString += "'";
				returnString += String.valueOf(tempNode.dayofTheYear);
				returnString += "'";
				System.out.println(returnString);
				index++;
			}
			return true;
		}
		
	}
	
	public boolean convertCVSStringToResource(String cvsInput)
	{
		boolean returnValue = false;
		resourceNode tempNode = new resourceNode();
		StringTokenizer st = new StringTokenizer(cvsInput, ",");
		String temp;
		int index = 0;
		
		if (cvsInput.isEmpty())
		{
			return returnValue;
		}
		else
		{
			while (st.hasMoreTokens()) {
		        temp = st.nextToken();
		        
		        index++;
		        if (index == 1)
		        {
		        	tempNode.quantityAvalible = Integer.parseInt(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 2)
		        {
		        	tempNode.resourceName = temp; 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 3)
		        {
		        	tempNode.resourceConsumable = Boolean.parseBoolean(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 4)
		        {
		        	tempNode.timeBasedResource = Integer.parseInt(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 5)
		        {
		        	tempNode.dateCodeEntered = Integer.parseInt(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 6)
		        {
		        	tempNode.deprecatonAmount = Float.parseFloat(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 7)
		        {
		        	tempNode.experationDateCode = Long.parseLong(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 8)
		        {
		        	tempNode.resourceNumber = Integer.parseInt(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index == 9)
		        {
		        	tempNode.dayofTheYear = Integer.parseInt(temp); 
		        	System.out.println(temp);
		        	System.out.println(index);
		        }
		        if (index > 9)
		        {
		        	return false;
		        }
		     }
			return true;
		}
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
		temp = checkInListByName(name);
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
	
	
	
	//Helper Functions
	
	//Checks to see if the resource is in the list, checks by name.
	//Returns a pointer to the resource if it is found
	//returns true if the element is in the list false otherwise.
	//Checked: 6/3/2022
	private resourceNode checkInListByName(String name)
	{
		if (this.resourceList.isEmpty())
		{
			System.out.println("EMPTY LIST");
			return null; //This should never be the case. There should be a header node.
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
	
	//Checks to see if the resource is in the list, checks by resource number.
	//Returns a pointer to the resource if it is found
	//returns null otherwise.
	private boolean checkInListByNumber(int resNumber) {
		// TODO Auto-generated method stub
		if (this.resourceList.isEmpty())
		{
			System.out.println("ERROR EMPTY LIST");
			return false;
		}
		else
		{
			int size = this.resourceList.size();
			int index = 0;
			resourceNode temp;
			while ( index < size)
			{
				
				if ( this.resourceList.get(index).resourceNumber == resNumber)
				{
					return true;
				}
				else
				{
					index++;
				}
			}
			return false;
		}
		
		//return false;
	}
	
	//Checks the quantity value to ensure that the value provided is valid
	//Situations like debt where the quantity of a resource will be negative will be 
	//handled as a task. This also needs to be less than the max type of resources defined
	//Checked 6/3/2022
	private boolean checkValue( int type)
	{
				
		if ( type < 0)	//Is not in the range of defined resources then there is an error
		{
			return false;
		}
		else
		{
			return true;	
		}
		
	}
	
	//This function checks to verify that the time code is in a valid range.
	//The time code is used to determine if the resources is effected by time
	// A value of 0 is a resource not effected by time
	// 1 = effected by time in a degredaton manner
	// 2 = a resource that will expire, but not degrade
	// 3 = a resource that will degrade and expire.
	//Checked: 6/3/2022
	private boolean checkTimeCode(int timeBased) {
		if ( (timeBased >= 0) && ( timeBased < 4))
		{
			return true;
		}
		System.out.println("ERROR THE TIME CODE IS INVALID");
		return false;
	}
	
	//Check the date code provided
	//The date-code is the following format YYMMDDHHMM
	//The acceptable date range will be from today until whenever
	private boolean checkDateCode(long dateEnteredCode) {
		// TODO Auto-generated method stub
		long temp = dateEnteredCode - 2109010000;
		if ( temp < 0)
		{
			return false; //A value less than zero means that it was added before the program was written.
		}
		return true;
	}
	
	//Check to see if the time code and the expiration date make sense.
	private boolean checkExpCode(long expDateCode, int timeBased, long dateEnteredCode) {
		// TODO Auto-generated method stub
		long temp = 0;
		temp = expDateCode - dateEnteredCode;
		if ( temp < 0)
		{
			return false; //The resource has expired before it was entered
		}
		if (timeBased == 0)
		{
			return false;
		}
		return true;
	}

	//Check to see that the deprecation code makes sense and the time based code is set correct.
	//Deprecation amount should be greater than 0 
	//The codes that will bee acceptable are 1 and 3
	private boolean checkDepCode(float depAmount, int timeBased) {
		// TODO Auto-generated method stub
		if ( (timeBased != 1) && ( timeBased != 3))
		{
			System.out.println("Error time code does not match degrade setting");
			return false;
		}
		if (depAmount < 0)
		{
			System.out.println("Error degrade is less than zero");
			return false;
		}
		return true;
	}


	//Setter Functions
	//These functions will be used to set the values of the elements of the node.
	
	
	//Get the 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenericResource test = new GenericResource();
		//public boolean addResource ( int quantity, String name, boolean consumable, int timeBased, long dateEnteredCode, float depAmount, long expDateCode, int resNumber)
		test.addResource(25, "Dollars", true, 3, 20220915, 1, 20230915, 1);
		test.addResource(30, "Hours", true, 1, 20220915, 1, 20230915, 2);
		test.addResource(1, "hammer", false,1, 20220915, 1, 20230915, 3);
		
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
		test.convertListToCSV();
		test.convertCVSStringToResource("8675301,Jenny,true,0,20221007,0.25,4545,12,7");
		System.out.println("Program Done");
	}

}
