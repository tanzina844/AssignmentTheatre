// Main Class File

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Main extends JFrame implements ActionListener
{

// Creates an icon, attached to a label to act as a banner  for the program
// Get resource is required for finding the image within the JAR achive once packed
final public ImageIcon icon = (new ImageIcon(getClass().getResource("images/ProgramBanner.png")));
JLabel iconHolder = new JLabel(icon);

//Labels and TextFields for the GUI
JLabel filmTimeLabel = new JLabel("Time ");
JLabel ticketTypeTitle = new JLabel("Ticket ");
JLabel ticketTypeCostTitle = new JLabel("Total Price: ");
JLabel ticketQuantityTitle = new JLabel("Ticket Quantity: ");
JLabel adultLabel = new JLabel("Adult");
JLabel childLabel = new JLabel("Child");
JLabel oapLabel = new JLabel("OAP");
JTextField ticketQuantityValue = new JTextField(1);
JTextField ticketTypeCostValue = new JTextField(4);

// GUI Buttons
JButton orderButton = new JButton("ORDER");
JButton resetButton = new JButton("Delete DB");

//Labels For Each Ticket (Do not appear until called by selecting a Ticket Type quantity)
JLabel seatLabel1 = new JLabel ("Ticket 1");
JLabel seatLabel2 = new JLabel ("Ticket 2");
JLabel seatLabel3 = new JLabel ("Ticket 3");
JLabel seatLabel4 = new JLabel ("Ticket 4");
JLabel seatLabel5 = new JLabel ("Ticket 5");

// Labels and ComboBoxes for for various Quantity of Tickets (Like their label, also do not appear untill called)
JComboBox seatCombo1 = new JComboBox();
JComboBox seatCombo2=  new JComboBox();
JComboBox seatCombo3=  new JComboBox();
JComboBox seatCombo4=  new JComboBox();
JComboBox seatCombo5=  new JComboBox();

// Arrays for Quantity of each ticket type
Integer[] adultQuantityList = {0,1,2,3,4,5};
Integer[] childQuantityList = {0,1,2,3,4,5};
Integer[] oapQuantityList = {0,1,2,3,4,5};

// Comboboxes to hold the state of the desired quantity of each ticket type
JComboBox adultQuantityCombo = new JComboBox(adultQuantityList);
JComboBox childQuantityCombo = new JComboBox(childQuantityList);
JComboBox oapQuantityCombo = new JComboBox(oapQuantityList);

//ArrayList for a Combobox that shows Film Times
String[] timeList = {"-", "1.00 PM", "3.00 PM", "5.00 PM", "7.00 PM", "9.00 PM"};
JComboBox timeCombo = new JComboBox(timeList);

// ArrayList that holds the vaules of seats that are available
ArrayList<Integer> seatArrayList = new ArrayList<Integer>();
String timeString = new String();


//Creation of JPanels to be added to the frame
JPanel bannerPanel = new JPanel();
JPanel p1 = new JPanel();
JPanel p2 = new JPanel();
JPanel p3 = new JPanel();



	public Main()	// Constructor Method for GUI
	{
		setLocationRelativeTo(null);					// Centers the Frame (NOTE: Multi-monitor setups may not center correctly depending on collective resolution
		setTitle("JSRP Cinema Booking System ver 1.2");	// Set Title of Main Window
		setSize(775,480);								// Set frame resolution to [x,y] pixels
		setResizable(false);							// Keeps Frame a constant resolution (Stops Resizing of Frame by user)
		setDefaultCloseOperation(EXIT_ON_CLOSE);		// Set frame to exit when 'CLOSE' window button is clicked

		// Add Panels to the Frame and state Layout Manager constructor arguments
		add(bannerPanel, BorderLayout.NORTH );
		add(p1, BorderLayout.EAST);
		add(p2, BorderLayout.WEST);
		add(p3, BorderLayout.SOUTH);

		// Addition of Content to respective Panel (Order determines position within panel)
		bannerPanel.add(iconHolder);
		bannerPanel.add(ticketQuantityTitle);
		bannerPanel.add(ticketQuantityValue);
		bannerPanel.add(ticketTypeCostTitle);
		bannerPanel.add(ticketTypeCostValue);
		p1.add(resetButton);
		p2.add(filmTimeLabel);
		p2.add(timeCombo);
		p2.add(adultLabel);
		p2.add(adultQuantityCombo);
		p2.add(childLabel);
		p2.add(childQuantityCombo);
		p2.add(oapLabel);
		p2.add(oapQuantityCombo);

		// Set number of visible entries when a combobox is selected
		adultQuantityCombo.setMaximumRowCount(4);
		childQuantityCombo.setMaximumRowCount(4);
		oapQuantityCombo.setMaximumRowCount(4);
		timeCombo.setMaximumRowCount(4);

		// Makes textfields non-editable, so that they can be used to display content
		ticketQuantityValue.setEditable(false);
		ticketTypeCostValue.setEditable(false);

		// Addition of Action Listeners to Objects
		timeCombo.addActionListener(this);
		adultQuantityCombo.addActionListener(this);
		childQuantityCombo.addActionListener(this);
		oapQuantityCombo.addActionListener(this);
		orderButton.addActionListener(this);
		resetButton.addActionListener(this);
		seatCombo1.addActionListener(this);
		seatCombo2.addActionListener(this);
		seatCombo3.addActionListener(this);
		seatCombo4.addActionListener(this);
		seatCombo5.addActionListener(this);


		setVisible(true);	// Set frame to be Visible, thus updating frame with all the selected elements
	}


//STARTOF ACTIONEVENTS

	public void actionPerformed (ActionEvent action)	// Method that contain all conditions where an ActionEvent is needed
		{

		// ActionListener for Combobox that displays Film Viewing Times
		if (action.getSource() == timeCombo)
		{
			// Create New Instance of the Database class
			FullDataBaseGenerator db = new FullDataBaseGenerator();

			// Get Name of database
			String selectedTime = db.returnFileName(timeCombo.getSelectedItem().toString());
			System.out.println(selectedTime);

			// Make Name of Database global
			timeString = selectedTime;

			// Call DataBase Generator (will generate fresh database for that time if one does not exist)
			db.FullDataBaseGeneration(selectedTime);

			//Fetch array of available seats and pass it to the global ArrayList 'seatArrayList'
			ArrayList<Integer> timeArray  = db.AvailableSeatsArrayReturn(selectedTime);
			seatArrayList= timeArray;

			//Reset any user selection of tickets when a new database is selected
			adultQuantityCombo.setSelectedIndex(0);
			childQuantityCombo.setSelectedIndex(0);
			oapQuantityCombo.setSelectedIndex(0);

			//Repaint the Frame
			repaint();
		}



		// ActionListener for all ticket type comboboxes collectively
		if (action.getSource() == adultQuantityCombo || action.getSource()
		== childQuantityCombo|| action.getSource() == oapQuantityCombo)
		{

			// Remove all existing items from each cb
			seatCombo1.removeAllItems();
			seatCombo2.removeAllItems();
			seatCombo3.removeAllItems();
			seatCombo4.removeAllItems();
			seatCombo5.removeAllItems();


			// If the arraylist no longer contains a zero (default answer)
			// Add a zero at the beginning of the array
			if (seatArrayList.contains(0) != true)
			{
				seatArrayList.add(0, 0);
			}

			// Add contents of the ArrayList to each combobox that display available seats
			for (int z =0; z< seatArrayList.size(); z++)
			{
				seatCombo1.addItem(seatArrayList.get(z));
				seatCombo2.addItem(seatArrayList.get(z));
				seatCombo3.addItem(seatArrayList.get(z));
				seatCombo4.addItem(seatArrayList.get(z));
				seatCombo5.addItem(seatArrayList.get(z));
			}

			// Get new total price as a String and affix to a Label for display on Frame
			String totalString = getTotal();
			ticketTypeCostValue.setText("£" + totalString);

			// Calculate total quantity of Tickets and affix to a label for display on Frame
			Integer adultCounter = Integer.parseInt((adultQuantityCombo.getSelectedItem().toString()));
			Integer childCounter = Integer.parseInt((childQuantityCombo.getSelectedItem().toString()));
			Integer oapCounter = Integer.parseInt((oapQuantityCombo.getSelectedItem().toString()));
			Integer countTotal = (adultCounter + childCounter + oapCounter);
			ticketQuantityValue.setText(countTotal.toString());

			repaint();


			// Only 5 tickets can be ordered at one time. Returns error message if more are selected
			if (countTotal >5)
			{	adultQuantityCombo.setSelectedIndex(0);
				childQuantityCombo.setSelectedIndex(0);
				oapQuantityCombo.setSelectedIndex(0);
				tooManyTickets();
				return;
			}

			// Following 6 if statements state what labels/comboboxes should show when the ticket number changes:
				// E.G. if no tickets are selected then no labels/cbs show
				// if 3 tickets are chosen, then the labels/cbs for Ticket 1,2 and 3 are shown
				// if 1 ticket is then chosen, cb/label for Ticket 2 and 3 dissapear by 1 stays
			if (countTotal ==1 || countTotal ==2 || countTotal ==3 || countTotal ==4|| countTotal ==5 )
			{
				p3.remove(seatLabel2);
				p3.remove(seatCombo2);
				p3.remove(seatLabel3);
				p3.remove(seatCombo3);
				p3.remove(seatLabel4);
				p3.remove(seatCombo4);
				p3.remove(seatLabel5);
				p3.remove(seatCombo5);
				p3.add(seatLabel1);
				p3.add(seatCombo1);
			}

			if (countTotal ==2 || countTotal ==3 || countTotal ==4|| countTotal ==5 )
			{
				p3.remove(seatLabel3);
				p3.remove(seatCombo3);
				p3.remove(seatLabel4);
				p3.remove(seatCombo4);
				p3.remove(seatLabel5);
				p3.remove(seatCombo5);
				p3.add(seatLabel2);
				p3.add(seatCombo2);
			}

			if (countTotal ==3 || countTotal ==4|| countTotal ==5 )
			{
				p3.remove(seatLabel4);
				p3.remove(seatCombo4);
				p3.remove(seatLabel5);
				p3.remove(seatCombo5);
				p3.add(seatLabel3);
				p3.add(seatCombo3);
			}

			if (countTotal ==4|| countTotal ==5 )
			{
				p3.remove(seatLabel5);
				p3.remove(seatCombo5);
				p3.add(seatLabel4);
				p3.add(seatCombo4);
			}

			if (countTotal ==5 )
			{
				p3.add(seatLabel5);
				p3.add(seatCombo5);

			}

			if (countTotal ==0 )
			{
				p3.remove(seatLabel1);
				p3.remove(seatCombo1);
				p3.remove(seatLabel2);
				p3.remove(seatCombo2);
				p3.remove(seatLabel3);
				p3.remove(seatCombo3);
				p3.remove(seatLabel4);
				p3.remove(seatCombo4);
				p3.remove(seatLabel5);
				p3.remove(seatCombo5);
				p3.remove(orderButton);
			}


			// if more tickets than available seats remaining is selected
			// Then an error message states this face
			if (countTotal > seatArrayList.size()-1)
			{
				notEnoughSeats();
				return;
			}

			// if no tickets are selected, then the 'order' button does not appear
			if (countTotal >0)
			{
				p3.add(orderButton);
			}

			repaint();
			setVisible(true);
		}


		// ActionListener for orderButton
		if (action.getSource() == orderButton)
		{

			// Validation for purchase of ticket(s)
			Integer orderConfirm = JOptionPane.showConfirmDialog(getContentPane(),
			"Are You sure you want you want to confirm this purchase?",
			"Confirm Ticket Purchase?",
			JOptionPane.YES_NO_OPTION);
			if (orderConfirm ==1)
			{
				return;
			}

			// Get the Values of Each Ticket Quantity ComboBox (e.g. 102, 301, etc)..
			Integer seat1Store = Integer.parseInt(seatCombo1.getSelectedItem().toString());
			Integer seat2Store = Integer.parseInt(seatCombo2.getSelectedItem().toString());
			Integer seat3Store = Integer.parseInt(seatCombo3.getSelectedItem().toString());
			Integer seat4Store = Integer.parseInt(seatCombo4.getSelectedItem().toString());
			Integer seat5Store = Integer.parseInt(seatCombo5.getSelectedItem().toString());

			//Create an array to hold theese values
			Integer[] proceedArray = new Integer[5];
			proceedArray[0] =seat1Store;
			proceedArray[1] =seat2Store;
			proceedArray[2] =seat3Store;
			proceedArray[3] =seat4Store;
			proceedArray[4] =seat5Store;


			// Repeat code to get value for number of Tickets
			Integer adultCounter = Integer.parseInt((adultQuantityCombo.getSelectedItem().toString()));
			Integer childCounter = Integer.parseInt((childQuantityCombo.getSelectedItem().toString()));
			Integer oapCounter = Integer.parseInt((oapQuantityCombo.getSelectedItem().toString()));
			Integer countTotal = (adultCounter + childCounter + oapCounter);


			// for the number of tickets selected
			// if that ticket number equals zero (the default value)
			// then state that not all tickets have been assined seats //128
			for (int z=0; z<countTotal; z++)
			{
				if (proceedArray[z] ==0)
				{
					notSelectedAllSeats();
					return;
				}
			}



			// Create a boolean that when true carries out the database portion of this ActionEvent
			boolean proceed = false;

			// Iterates through each object of the array and compares then with each other
			for(int i = 0; i<proceedArray.length;i++)
			{
				for(int p=0; p<proceedArray.length; p++)
				{
					if(i != p)
					{
						// if the two compared objects have the same seat number...
						if(proceedArray[i].equals(proceedArray[p]))
						{
							//...and is not a zero (this is a default value, not a seat number)
							// then call an error stating duplicate seats have been allocated

							if (proceedArray[i] != 0 || proceedArray[p] != 0)
							{
								duplicateSeats();
								return;
							}
						}
					}
				}

			// if no duplications are found, the rest of the event can proceed
			proceed = true;
			}

			if (proceed == true)
			{

				// Remove the send values from the array
				seatArrayList.remove(seat1Store);
				seatArrayList.remove(seat2Store);
				seatArrayList.remove(seat3Store);
				seatArrayList.remove(seat4Store);
				seatArrayList.remove(seat5Store);


				try{ // Start try/catch

					// State dependables for reading the database
					FileInputStream fs = new FileInputStream(timeString);
					DataInputStream in = new DataInputStream(fs);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));

					// While there are Lines left to be read
					String stringLine;
			    	while ((stringLine = br.readLine()) != null)
			    	{
						// Create dependencies for writing to same file
						BufferedWriter fw = new BufferedWriter(new FileWriter(timeString));

						int x=0;
						// Iterate through the new edited array (orginal array minus selected seat)
						while(x<seatArrayList.size())
						{
							// Rewrite every line of the text file with each entry in the new array
							String line = seatArrayList.get(x).toString();
							fw.write(line + ";");
							x++;
						}
					//Close the file writing dependency
					fw.close();

					}

					//try/catch end, if error- prints message to command line followed by error code
					}catch (Exception ex){ System.err.println("Error in database manipulation, code: " + ex.getMessage());}

			// Call Pop-up asking user if they want to restart the program for another transaction
			ticketBought();
		}
	}


		// ActionListener for database reset button
		if(action.getSource() == resetButton)
		{
			// When button is selected, A YES/NO messagebox displays
			Integer end = JOptionPane.showConfirmDialog(getContentPane(),
			"(NOTE: You can only delete databases upon initially loading"+
			" the program and before selecting any databases).\n"+
			"If you haven't already done so, please re-run the program and"+
			" select this option again if you wish to delete the databases.\n"+
			"The Command will still run regardless, but will not work without"+
			" the the above steps\n\n"+
			"Would you like to Reset all the Databases?\n",
			"Delete Databases?",
			JOptionPane.YES_NO_OPTION);

			// Is selected answer is YES
			if (end == 0)
			{
				// Ask for validation of deltion
				Integer yesno1 = JOptionPane.showConfirmDialog(getContentPane(),
				"Are You sure you want you want to delete all the databases?",
				"Delete Database?",
				JOptionPane.YES_NO_OPTION);

				// if selected yes
				if (yesno1 == 0)
				{
					// Delete the current instance of the program
					Main.this.dispose();

					//Delete Current database
					File fileToDelete = new File(timeString);
					fileToDelete.delete();

					// Delete all the databases (stated by name)
					File file1 = new File("SEAT DATABASE 1.00 PM.txt");
					File file2 = new File("SEAT DATABASE 3.00 PM.txt");
					File file3 = new File("SEAT DATABASE 5.00 PM.txt");
					File file4 = new File("SEAT DATABASE 7.00 PM.txt");
					File file5 = new File("SEAT DATABASE 9.00 PM.txt");
					file1.delete();
					file2.delete();
					file3.delete();
					file4.delete();
					file5.delete();

					// Create new instance of the program (hence restart it)
					new Main();
				}
			}
		}

	}

//END OF ACTIONEVENTS


	// Method that returns total price as as String
	public String getTotal()
	{
		// Get current value of each selected option that effects the price
		Integer childInput = Integer.parseInt(childQuantityCombo.getSelectedItem().toString());
		Integer oapInput = Integer.parseInt(oapQuantityCombo.getSelectedItem().toString());
		Integer adultInput = Integer.parseInt(adultQuantityCombo.getSelectedItem().toString());

		// Pass this value to 'Ticket.getSeatPrice' method to obtain price
		int childTicketPrice = Ticket.getSeatPrice(childInput, "child");
		int adultTicketPrice = Ticket.getSeatPrice(adultInput, "adult");
		int oapTicketPrice = Ticket.getSeatPrice(oapInput, "oap");

		// Calculate total
		int total = (childTicketPrice +adultTicketPrice+oapTicketPrice);

		//Convert this integer value to a string in the correct format
		String totalString = Ticket.calculateStringTotal(total);

		return totalString;
	}



	public void ticketBought()
	{
		// Display Message Stating the price of the ordered tickets
		JOptionPane.showMessageDialog(getContentPane(),
		"The total Cost is..£"+getTotal(), "Total Cost", JOptionPane.PLAIN_MESSAGE);

		// Ask user if they want to restart the program
		Integer a = JOptionPane.showConfirmDialog(getContentPane(),
		"Transaction complete\n Would you like to make another?",
		"Transaction Complete", JOptionPane.YES_NO_OPTION);

		// If yes, deletes current instance of program then creates a new one
		if (a == 0)
			{System.out.println("Program Restart Initiated");
			Main.this.dispose();
			new Main();
			}
		// If no, then instance of program is deleted but no new instance is created, hence ending the program
		if (a==1)
		{
			System.exit(0);
		}
	}


	// Methods for displaying error messages

	public void tooManyTickets()
	{
		JOptionPane.showMessageDialog(getContentPane(), "You cannot Process more than 5 tickets at a time!", "Ticket Quantity Error", JOptionPane.ERROR_MESSAGE);
	}

	public void notEnoughSeats()
	{
		JOptionPane.showMessageDialog(getContentPane(), "There are not enough seats remaining to process the number of tickets selected", "Ticket Quantity Error", JOptionPane.ERROR_MESSAGE);
	}

	public void duplicateSeats()
	{

		JOptionPane.showMessageDialog(getContentPane(), "There is a duplication of seats!", "Seat Selection Error", JOptionPane.ERROR_MESSAGE);
	}

	public void notSelectedAllSeats()
	{
		JOptionPane.showMessageDialog(getContentPane(), "Not all seats have been Assigned!", "Seat Selection Error", JOptionPane.ERROR_MESSAGE);
	}



	// Drawn Graphics Method
	public void paint(Graphics g)
	{
		//ArrayList<Integer> list = new ArrayList<Integer>(array);

		super.paint(g);					// Clears the frame when method is called

		int width = 32;					// State width of each Rectangle
		int height = 32;				// State height of each Rectangle

		int leftBlockSeatsCol = 6;		// State number of Rows in the Left Block
		int leftBlockSeatsRow = 6;		// State number of Columns in the Left Block
		int centerBlockSeatsRow = 5;	// State number of Rows in the Center Block
		int centerBlockSeatsCol = 8;	// State number of Columns in the Center Block
		int rightBlockSeatsRow = 6;		// State number of Rows in the Right Block
		int rightBlockSeatsCol = 6;		// State number of Columns in the Right Block

		int leftBlockPosX = 15;														// Sets Left Block X-axis Position (in Pixels)
		int leftBlockPosY = 225;														// Sets Left Block Y-axis Position (in Pixels)
		int centerBlockPosX = (leftBlockPosX+(leftBlockSeatsCol*width)) +50;		// Sets Center Block X-axis Position (in Pixels)
		int centerBlockPosY = 225;													// Sets Center Block Y-axis Position (in Pixels)
		int rightBlockPosX = (centerBlockPosX +(centerBlockSeatsCol*width)) +50;	// Sets Right Block X-axis Position (in Pixels)
		int rightBlockPosY = 225;													// Sets Right Block Y-axis Position (in Pixels)

		g.setColor(Color.black);		// Set Default Draw Color to black

		g.drawString("Left Block", (leftBlockPosX+(32*(leftBlockSeatsCol/2)-25)), (leftBlockPosY - 10));			// Title for Each Block
		g.drawString("Center Block", (centerBlockPosX+(32*(centerBlockSeatsCol/2)-30)), (centerBlockPosY - 10));
		g.drawString("Right Block", (rightBlockPosX+(32*(rightBlockSeatsCol/2)-25)), (rightBlockPosY - 10));

		Color custom_grey = new Color(175,175,175);

//DRAW LEFT BLOCK OF SEATS

		for(int i=0; i<leftBlockSeatsCol;i++)												// Loop while there are Columns..
		{
			String colString = new Integer (i+101).toString();								// Creates an Integer of relative Seat Number and converts it to a String
			g.drawString(colString, leftBlockPosX+5+(i*width), leftBlockPosY+20);			// String is affixed to drawSring method and co-ordinates tweaked to center the text in each box.
			g.drawRect(leftBlockPosX+(i*width), leftBlockPosY, width, height);				// Draw a rectangle at the stated X and Y- Pos. The next rect = X-Pos + (width of rectangle * horizontal psotion) [hence in a sequence]

			if (seatArrayList.contains((i+101)) != true)										// If The Array of available seats does not contain i+101 (Seat 1 of leftBlock is 101, Seat 2 is 102, etc)...
			{

				g.setColor(Color.red);														// Then change Draw Color to red
				g.fillRect(leftBlockPosX+(i*width), leftBlockPosY, width, height);			// Fill in the currently iterated rectangle
				g.setColor(Color.black);													// Change color back to default
				g.drawRect(leftBlockPosX+(i*width), leftBlockPosY, width, height);			// Redraw the Rectangle
				g.drawString(colString, leftBlockPosX+5+(i*width),leftBlockPosY+20);		// Redraw the number


			}

			for(int x=0; x<leftBlockSeatsRow; x++)													// For each column, loop while there are Rows..
			{
				String rowString = new Integer((i+(leftBlockSeatsCol*x))+101).toString();			// [As above]
				g.drawString (rowString, leftBlockPosX+5+(i*width), leftBlockPosY+(x*height)+20);

				g.drawRect(leftBlockPosX+(i*width),leftBlockPosY+(x*height), width, height);		// Draw A rectangle exactly like before but with Y-Pos + (height * vertical postition)
				if (seatArrayList.contains((i+(leftBlockSeatsCol*x))+101) != true)					// If the Array of available seats does not contain the relevent seat number...
				{
					g.setColor(Color.red);															 	// Change Draw Color ot red
					g.fillRect(leftBlockPosX+(i*width), leftBlockPosY+(x*height), width, height);		// Fill in the currently iterated rectangle
					g.setColor(Color.black);														 	// Change draw color back to default
					g.drawRect(leftBlockPosX+(i*width), leftBlockPosY+(x*height), width, height);		// Redraw outline of rectangle
					g.setColor(custom_grey);														 	// Set Color to Custom
					g.drawString (rowString, leftBlockPosX+5+(i*width), leftBlockPosY+(x*height)+20); 	// Redraw number
					g.setColor(Color.black);														 	// Change color back to default

				}
			}
		}


// DRAW CENTER BLOCK OF SEATS

		for(int i=0; i<centerBlockSeatsCol;i++)													// [Refer to Left Block Code comments]
		{
			String colString = new Integer (i+201).toString();
			g.drawString (colString, centerBlockPosX+5+(i*width), centerBlockPosY+20);
			g.drawRect(centerBlockPosX+(i*width), centerBlockPosY, width, height);

			if (seatArrayList.contains((i+201)) != true)
			{
				g.setColor(Color.red);
				g.fillRect(centerBlockPosX+(i*width), centerBlockPosY, width, height);
				g.setColor(Color.black);
				g.drawRect(centerBlockPosX+(i*width), centerBlockPosY, width, height);
				g.drawString(colString, centerBlockPosX+5+(i*width), centerBlockPosY+20);
			}

			for (int x=0; x<centerBlockSeatsRow; x++)
			{
				String rowString = new Integer ((i+(centerBlockSeatsCol*x))+201).toString();
				g.drawString(rowString, centerBlockPosX+5+(i*width), centerBlockPosY+(x*height)+20);

				g.drawRect(centerBlockPosX+(i*width), centerBlockPosY+(x*height), width, height);
				if (seatArrayList.contains((i+(centerBlockSeatsCol*x))+201) != true)
				{
					g.setColor(Color.red);
					g.fillRect(centerBlockPosX+(i*width), centerBlockPosY+(x*height), width, height);
					g.setColor(Color.black);
					g.drawRect(centerBlockPosX+(i*width), centerBlockPosY+(x*height), width, height);
					g.setColor(custom_grey);
					g.drawString (rowString, centerBlockPosX+5+(i*width), centerBlockPosY+(x*height)+20);
					g.setColor(Color.black);
				}
			}
		}

//DRAW RIGHT BLOCK OF SEATS

		for (int i=0; i<rightBlockSeatsCol;i++)													// [Refer to Left Block Code comments]
		{
			String colString = new Integer (i+301).toString();
			g.drawString(colString, rightBlockPosX+5+(i*width), rightBlockPosY+20);

			g.drawRect(rightBlockPosX+(i*width), rightBlockPosY, width, height);
			if (seatArrayList.contains((i+301)) != true)
			{
				g.setColor(Color.red);
				g.fillRect(rightBlockPosX+(i*width),rightBlockPosY,width,height);
				g.setColor(Color.black);
				g.drawRect(rightBlockPosX+(i*width), rightBlockPosY, width, height);
				g.drawString (colString, rightBlockPosX+5+(i*width), rightBlockPosY+20);
			}

			for(int x=0; x<rightBlockSeatsRow; x++)
			{
				String rowString = new Integer ((i+(rightBlockSeatsCol*x))+301).toString();
				g.drawString (rowString, rightBlockPosX+5+(i*width), rightBlockPosY+(x*height)+20);

				g.drawRect(rightBlockPosX+(i*width), rightBlockPosY+(x*height), width, height);
				if (seatArrayList.contains((i+(rightBlockSeatsCol*x))+301) != true)
				{
					g.setColor(Color.red);
					g.fillRect(rightBlockPosX+(i*width), rightBlockPosY+(x*height), width, height);
					g.setColor(Color.black);
					g.drawRect(rightBlockPosX+(i*width), rightBlockPosY+(x*height), width, height);
					g.setColor(custom_grey);
					g.drawString (rowString, rightBlockPosX+5+(i*width), rightBlockPosY+(x*height)+20);
					g.setColor(Color.black);
				}
			}
		}
	}


	public static void main (String[] args)		// Main Method Declaration
	{
		new Main();								// Create a new Instance of the main Program when ran
	}
}

