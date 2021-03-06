/*
 * 
 */

package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// TODO: Auto-generated Javadoc
/**
 * The Class InformationScreen.
 */
public class InformationScreen
{

	/** The mainpanel. */
	private static JPanel mainpanel;

	/** The labor cost field. */
	private static JTextField laborCostField = new JTextField("15");

	/** The truck cost field. */
	private static JTextField truckCostField = new JTextField("30");

	/** The capital cost field. */
	private static JTextField capitalCostField = new JTextField("0");

	/** The oper from hour. */
	private static JComboBox operFromHour = new JComboBox();

	/** The oper from min. */
	private static JComboBox operFromMin = new JComboBox();

	/** The oper to hour. */
	private static JComboBox operToHour = new JComboBox();

	/** The oper to min. */
	private static JComboBox operToMin = new JComboBox();

	/** The annual oper days field. */
	private static JTextField annualOperDaysField = new JTextField("240");

	/** The no imap trucks field. */
	private static JTextField noImapTrucksField = new JTextField("1");

	/** The centerline miles field. */
	private static JTextField centerlineMilesField = new JTextField("12");

	/** The next button. */
	private static JButton nextButton = new JButton("NEXT");

	/** The prev button. */
	private static JButton prevButton = new JButton("BACK");

	/** The my id. */
	private static int myID = 1;

	/** The columns. */
	private static int columns = 200;

	/** The text field width. */
	private static int textFieldWidth = 125;

	private static JTextField fuelPrice = new JTextField("1.99");

	//private static JCheckBox	weekday	= new JCheckBox("Workday");
	//private static JCheckBox	weekend	= new JCheckBox("Weekend");
	private static JCheckBox	monday  = new JCheckBox("Monday");
	private static JCheckBox	tuesday = new JCheckBox("Tuesday");
	private static JCheckBox	wednesday = new JCheckBox("Wednesday");
	private static JCheckBox	thursday = new JCheckBox("Thursday");
	private static JCheckBox	friday  = new JCheckBox("Friday");
	private static JCheckBox	saturday = new JCheckBox("Saturday");
	private static JCheckBox	sunday  = new JCheckBox("Sunday");
	private static JCheckBox	holiday	= new JCheckBox("Holidays");

	/**
	 * Gets the infor panel.
	 *
	 * @return the infor panel
	 */
	public static JPanel getInforPanel()
	{
		if (mainpanel != null)
		{
			return mainpanel;
		}

		mainpanel = new JPanel();
		initComp();
		return mainpanel;
	}

	/**
	 * Inits the comp.
	 */
	private static void initComp()
	{
		mainpanel.setPreferredSize(new Dimension(900, 400));
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(getSetupPanel());
		mainpanel.add(getinfoTable());

	}

	/**
	 * Gets the setup panel.
	 *
	 * @return the setup panel
	 */
	private static JPanel getSetupPanel()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel setupPanel = new JPanel();
		setupPanel.setLayout(new GridBagLayout());
		setupPanel.setBorder(BorderFactory.createTitledBorder("Estimation Information"));
		setupPanel.setMaximumSize(new Dimension(700, 300));
		
		int strutsize = 7;

		c.gridx = 0;
		c.gridy = 0;
		JLabel laborCostLabel = new JLabel("Cost for labor/hr ($)");
		setupPanel.add(laborCostLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel truckOperLabel = new JLabel("Cost for truck operation/hr ($)");
		setupPanel.add(truckOperLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel capitalCostLabel = new JLabel("Other fixed costs ($)");
		setupPanel.add(capitalCostLabel, c);

		c.gridx = 0;
		c.gridy = 5;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 6;
		JLabel operHoursLabel = new JLabel("Hours of operation");
		setupPanel.add(operHoursLabel, c);

		c.gridx = 0;
		c.gridy = 7;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 8;
		JLabel annualDaysLabel = new JLabel("Annual days of operation");
		// setupPanel.add(annualDaysLabel, c);

		c.gridx = 0;
		c.gridy = 9;
		setupPanel.add(Box.createVerticalStrut(0), c);

		c.gridx = 0;
		c.gridy = 10;
		JLabel excludeLabel = new JLabel("Days Included");
		setupPanel.add(excludeLabel, c);

		c.gridx = 0;
		c.gridy = 11;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 12;
		JLabel imapTrucksLabel = new JLabel("Number of deployed IMAP trucks");
		setupPanel.add(imapTrucksLabel, c);

		c.gridx = 0;
		c.gridy = 13;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 14;
		JLabel centerlineLabel = new JLabel("Centerline miles");
		setupPanel.add(centerlineLabel, c);

		c.gridx = 0;
		c.gridy = 15;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		c.gridx = 0;
		c.gridy = 16;
		JLabel FuelPriceLabel = new JLabel("Fuel Price ($ per GAL)");
		setupPanel.add(FuelPriceLabel, c);

		c.gridx = 0;
		c.gridy = 17;
		setupPanel.add(Box.createVerticalStrut(strutsize), c);

		// adding gap between label and field

		c.gridx = 1;
		for (int i = 0; i < 13; i++)
		{
			c.gridy = i;
			setupPanel.add(Box.createHorizontalStrut(180), c);
		}

		laborCostField.setColumns(150);
		laborCostField.setMinimumSize(new Dimension(textFieldWidth, 20));

		fuelPrice.setColumns(150);
		fuelPrice.setMinimumSize(new Dimension(textFieldWidth, 20));

		truckCostField.setMinimumSize(new Dimension(textFieldWidth, 20));
		capitalCostField.setMinimumSize(new Dimension(textFieldWidth, 20));
		annualOperDaysField.setMinimumSize(new Dimension(textFieldWidth, 20));
		noImapTrucksField.setMinimumSize(new Dimension(textFieldWidth, 20));
		centerlineMilesField.setMinimumSize(new Dimension(textFieldWidth, 20));
		centerlineMilesField.setText(String.format("%.2f", FreevalFileParser.getSeedFacilityLength()));
		
		// Adding all text fields
		c.gridx = 2;
		c.gridy = 0;
		setupPanel.add(laborCostField, c);

		c.gridy = 2;
		setupPanel.add(truckCostField, c);

		c.gridy = 4;
		setupPanel.add(capitalCostField, c);

		populateComboBox();
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
		timePanel.add(operFromHour);
		timePanel.add(new JLabel(":"));
		timePanel.add(operFromMin);
		timePanel.add(new JLabel("-"));
		timePanel.add(operToHour);
		timePanel.add(new JLabel(":"));
		timePanel.add(operToMin);

		c.gridy = 6;
		setupPanel.add(timePanel, c);

		c.gridy = 8;
		// setupPanel.add(annualOperDaysField, c);

		c.gridy = 10;
		JPanel excludePanel = new JPanel();
		excludePanel.setLayout(new GridLayout(3,3));
		excludePanel.add(monday);
		excludePanel.add(tuesday);
		excludePanel.add(wednesday);
		excludePanel.add(thursday);
		excludePanel.add(friday);
		excludePanel.add(saturday);
		excludePanel.add(sunday);
		excludePanel.add(holiday);
		//excludePanel.add(weekday);
		//weekday.setSelected(true);
		monday.setSelected(true);
		tuesday.setSelected(true);
		wednesday.setSelected(true);
		thursday.setSelected(true);
		friday.setSelected(true);
		//monday.setSelected(true);
		//monday.setSelected(true);

		setupPanel.add(excludePanel, c);

		c.gridy = 12;
		setupPanel.add(noImapTrucksField, c);

		c.gridy = 14;
		setupPanel.add(centerlineMilesField, c);

		c.gridy = 16;
		setupPanel.add(fuelPrice, c);

		return setupPanel;
	}

	/**
	 * Populate combo box.
	 */
	private static void populateComboBox()
	{
		//for (int i = FreevalFileParser.getSeedStartHour(); i <= FreevalFileParser.getSeedEndHour(); i++) {
		//	operFromHour.addItem(Integer.toString(i));
		//	operToHour.addItem(Integer.toString(i));
		//}
		int startHour = FreevalFileParser.getSeedStartHour();
		int endHour = FreevalFileParser.getSeedEndHour();
		int startMin = FreevalFileParser.getSeedStartMin();
		int endMin = FreevalFileParser.getSeedEndMin();
		int numHours;
		if (startHour == endHour && startMin == endMin) {
			numHours = 24;
		} else if (startHour > endHour) {
			numHours = endHour - startHour + 24;
		} else {
			numHours = endHour - startHour;
		}
		for (int i=0; i <= Math.min(23, numHours); i++) {
			operFromHour.addItem(Integer.toString((startHour + i) % 24));
			operToHour.addItem(Integer.toString((startHour + i) % 24));
		}
		
		operFromHour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sameStartHour = Integer.parseInt(operFromHour.getSelectedItem().toString()) == FreevalFileParser.getSeedStartHour();
				boolean invalidStartMin = Integer.parseInt(operFromMin.getSelectedItem().toString()) < FreevalFileParser.getSeedStartMin();
				if (sameStartHour && invalidStartMin) {
					JOptionPane.showMessageDialog(null, "The operation start time cannot be before the start time of the seed facility study period.","Warning: Invalid Start Time", JOptionPane.WARNING_MESSAGE);
					operFromHour.setSelectedIndex(0);
					operFromMin.setSelectedIndex(FreevalFileParser.getSeedStartMin() / 15);
				}
			}
		});
		
		operToHour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sameEndHour = Integer.parseInt(operToHour.getSelectedItem().toString()) == FreevalFileParser.getSeedEndHour();
				boolean invalidEndMin = Integer.parseInt(operToMin.getSelectedItem().toString()) > FreevalFileParser.getSeedEndMin();
				if (sameEndHour && invalidEndMin) {
					JOptionPane.showMessageDialog(null, "The operation end time cannot be after the end time of the seed facility study period.","Warning: Invalid End Time", JOptionPane.WARNING_MESSAGE);
					operToHour.setSelectedIndex(operToHour.getItemCount() - 1);
					operToMin.setSelectedIndex(FreevalFileParser.getSeedEndMin() / 15);
				}
			}
		});
		
		
		operFromMin.addItem("0");
		operFromMin.addItem("15");
		operFromMin.addItem("30");
		operFromMin.addItem("45");
		operFromMin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean sameStartHour = Integer.parseInt(operFromHour.getSelectedItem().toString()) == FreevalFileParser.getSeedStartHour();
					boolean invalidStartMin = Integer.parseInt(operFromMin.getSelectedItem().toString()) < FreevalFileParser.getSeedStartMin();
					if (sameStartHour && invalidStartMin) {
						JOptionPane.showMessageDialog(null, "The operation start time cannot be before the start time of the seed facility study period.","Warning: Invalid Start Time", JOptionPane.WARNING_MESSAGE);
						operFromHour.setSelectedIndex(0);
						operFromMin.setSelectedIndex(FreevalFileParser.getSeedStartMin() / 15);
					}
				}
		});
		
		
		operToMin.addItem("0");
		operToMin.addItem("15");
		operToMin.addItem("30");
		operToMin.addItem("45");
		operToMin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sameEndHour = Integer.parseInt(operToHour.getSelectedItem().toString()) == FreevalFileParser.getSeedEndHour();
				boolean invalidEndMin = Integer.parseInt(operToMin.getSelectedItem().toString()) > FreevalFileParser.getSeedEndMin();
				if (sameEndHour && invalidEndMin) {
					JOptionPane.showMessageDialog(null, "The operation end time cannot be after the end time of the seed facility study period.","Warning: Invalid End Time", JOptionPane.WARNING_MESSAGE);
					operToHour.setSelectedIndex(operToHour.getItemCount() - 1);
					operToMin.setSelectedIndex(FreevalFileParser.getSeedEndMin() / 15);
				}
			}
		});
		

		//operFromHour.setSelectedIndex(8);
		//operToHour.setSelectedIndex(17);
		//operFromMin.setSelectedIndex(3);
		//operToMin.setSelectedIndex(3);
		operFromHour.setSelectedIndex(0);
		operToHour.setSelectedIndex(operToHour.getItemCount() - 1);
		operFromMin.setSelectedIndex(FreevalFileParser.getSeedStartMin() / 15);
		operToMin.setSelectedIndex(FreevalFileParser.getSeedEndMin() / 15);

	}

	/**
	 * Gets the info table.
	 *
	 * @return the info table
	 */
	private static JPanel getinfoTable()
	{
		JPanel containerPanel = new JPanel();
		nextButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int startHour = Integer.parseInt(operFromHour.getSelectedItem().toString());
				int startMin = Integer.parseInt(operFromMin.getSelectedItem().toString());
				int endHour = Integer.parseInt(operToHour.getSelectedItem().toString());
				int endMin = Integer.parseInt(operToMin.getSelectedItem().toString());
				FreevalFileParser.updateSeedStudyPeriods(startHour, startMin, endHour, endMin);
				Main.changePanel(myID + 1);
			}
		});

		prevButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.changePanel(myID - 1);
			}
		});
		containerPanel.add(prevButton);
		containerPanel.add(nextButton);
		return containerPanel;
	}

	/**
	 * Gets the center line miles.
	 *
	 * @return the center line miles
	 */
	public static String getCenterLineMiles()
	{
		return centerlineMilesField.getText();
	}

	public static String getFuelPrice()
	{
		return fuelPrice.getText();
	}

	/**
	 * Gets the operation hours.
	 *
	 * @return the operation hours
	 */
	public static String getOperationHours() {
		boolean is24Hour = (operToHour.getSelectedIndex() == operFromHour.getSelectedIndex()) && (operToMin.getSelectedIndex() == operFromMin.getSelectedIndex());
		if (is24Hour) {
			return String.format("%.2f", 24.0f);
		} else {
			
			int noOfHours = Integer.parseInt(operToHour.getSelectedItem().toString()) - Integer.parseInt(operFromHour.getSelectedItem().toString());
			if (noOfHours < 0) {
				noOfHours += 24;
			}
			int noOfMin = Integer.parseInt(operToMin.getSelectedItem().toString()) - Integer.parseInt(operFromMin.getSelectedItem().toString());
			//noOfMin = operToMin.getSelectedIndex() - operFromMin.getSelectedIndex();
			//if (operToMin.getSelectedIndex() < operFromMin.getSelectedIndex())
			//{
			//	noOfHours--;
			//	noOfMin = 4 + noOfMin;
			//
			//}
			//return String.format("%.2f",(noOfHours + (noOfMin * 15 / 60.0f)));
			return String.format("%.2f",(noOfHours + (noOfMin / 60.0f)));
		}
	}

	/**
	 * Gets the oper days.
	 *
	 * @return the oper days
	 */
	public static int getOperDays()
	{
		int operDays = 0;
		if (monday.isSelected())
		{
			operDays += 48;
		}
		if (tuesday.isSelected())
		{
			operDays += 48;
		}
		if (wednesday.isSelected())
		{
			operDays += 48;
		}
		if (thursday.isSelected())
		{
			operDays += 48;
		}
		if (friday.isSelected())
		{
			operDays += 48;
		}
		if (saturday.isSelected())
		{
			operDays += 48;
		}
		if (sunday.isSelected())
		{
			operDays += 48;
		}
		if (!holiday.isSelected())
		{
			operDays -= 8;
		}
		return operDays;
	}

	/**
	 * Gets the operation hours int.
	 *
	 * @return the operation hours int
	 */
	public static float getOperationHoursInt()
	{
		//int noOfHours = operToHour.getSelectedIndex() - operFromHour.getSelectedIndex();
		//int noOfMin = 0;
		//noOfMin = operToMin.getSelectedIndex() - operFromMin.getSelectedIndex();
		//if (operToMin.getSelectedIndex() < operFromMin.getSelectedIndex())
		//{
		//	noOfHours--;
		//	noOfMin = 4 + noOfMin;
		//}
		//return (noOfHours + noOfMin / 4);
		boolean is24Hour = (operToHour.getSelectedIndex() == operFromHour.getSelectedIndex()) && (operToMin.getSelectedIndex() == operFromMin.getSelectedIndex());
		if (is24Hour) {
			return 24;
		} else {
			int noOfHours = Integer.parseInt(operToHour.getSelectedItem().toString()) - Integer.parseInt(operFromHour.getSelectedItem().toString());
			if (noOfHours < 0) {
				noOfHours += 24;
			}
			int noOfMin = Integer.parseInt(operToMin.getSelectedItem().toString()) - Integer.parseInt(operFromMin.getSelectedItem().toString());
			return (noOfHours + (noOfMin / 60.0f));
		}
	}

	/**
	 * Gets the no trucks.
	 *
	 * @return the no trucks
	 */
	public static float getNoTrucks()
	{
		return Float.parseFloat(noImapTrucksField.getText());
	}

	/**
	 * Gets the cost labor.
	 *
	 * @return the cost labor
	 */
	public static float getCostLabor()
	{
		return Float.parseFloat(laborCostField.getText());
	}

	/**
	 * Gets the cost truck.
	 *
	 * @return the cost truck
	 */
	public static float getCostTruck()
	{
		return Float.parseFloat(truckCostField.getText());
	}
	
	/**
	 * Gets the other fixed costs.
	 *
	 * @return the fixed costs
	 */
	public static float getOtherFixedCosts() {
		return Float.parseFloat(capitalCostField.getText());
	}
	
	public static boolean[] getDaysActive() {
		boolean[] daysActive = new boolean[7];
		daysActive[0] = monday.isSelected();  // Monday
		daysActive[1] = tuesday.isSelected();  // Tuesday
		daysActive[2] = wednesday.isSelected();  // Wednesday
		daysActive[3] = thursday.isSelected();  // Thursday
		daysActive[4] = friday.isSelected();  // Friday
		daysActive[5] = saturday.isSelected();  // Saturday
		daysActive[6] = sunday.isSelected();  // Sunday
		return daysActive;
	}
	
	public static boolean getHolidaysIncluded() {
		return holiday.isSelected();
	}
	
	public static String getOperationHoursString() {
		return operFromHour.getSelectedItem().toString()+":"+(operFromMin.getSelectedItem().toString().equalsIgnoreCase("0") ? "00" : operFromMin.getSelectedItem().toString())
				+"-" +
				operToHour.getSelectedItem().toString()+":"+(operToMin.getSelectedItem().toString().equalsIgnoreCase("0") ? "00" : operToMin.getSelectedItem().toString());
	}
	
	public static String getDaysIncludedString() {
		String includeStr = (monday.isSelected() ? "Monday," : "")
			+ (tuesday.isSelected() ? "Tuesday," : "")
			+ (wednesday.isSelected() ? "Wednesday," : "")
			+ (thursday.isSelected() ? "Thursday," : "")
			+ (friday.isSelected() ? "Friday," : "")
			+ (saturday.isSelected() ? "Saturday," : "")
			+ (sunday.isSelected() ? "Sunday," : "")
			+ (holiday.isSelected() ? "Holidays," : "");
		return includeStr.substring(0,includeStr.length()-1);
	}
}
