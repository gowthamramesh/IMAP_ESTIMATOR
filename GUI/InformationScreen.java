/*
 * 
 */

package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
	private static JTextField noImapTrucksField = new JTextField("18");

	/** The centerline miles field. */
	private static JTextField centerlineMilesField = new JTextField("148");

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

	private static JCheckBox	weekday	= new JCheckBox("Workday");
	private static JCheckBox	weekend	= new JCheckBox("Weekend");
	private static JCheckBox	holiday	= new JCheckBox("Holiday");

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
		mainpanel.setPreferredSize(new Dimension(800, 400));
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
		setupPanel.setMaximumSize(new Dimension(800, 300));

		c.gridx = 0;
		c.gridy = 0;
		JLabel laborCostLabel = new JLabel("Cost for labor/hr ($)");
		setupPanel.add(laborCostLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel truckOperLabel = new JLabel("Cost for truck operation/hr ($)");
		setupPanel.add(truckOperLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel capitalCostLabel = new JLabel("Other fixed costs ($)");
		setupPanel.add(capitalCostLabel, c);

		c.gridx = 0;
		c.gridy = 5;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 6;
		JLabel operHoursLabel = new JLabel("Hours of operation");
		setupPanel.add(operHoursLabel, c);

		c.gridx = 0;
		c.gridy = 7;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 8;
		JLabel annualDaysLabel = new JLabel("Annual days of operation");
		// setupPanel.add(annualDaysLabel, c);

		c.gridx = 0;
		c.gridy = 9;
		setupPanel.add(Box.createVerticalStrut(0), c);

		c.gridx = 0;
		c.gridy = 10;
		JLabel excludeLabel = new JLabel("Include ");
		setupPanel.add(excludeLabel, c);

		c.gridx = 0;
		c.gridy = 11;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 12;
		JLabel imapTrucksLabel = new JLabel("Number of deployed IMAP trucks");
		setupPanel.add(imapTrucksLabel, c);

		c.gridx = 0;
		c.gridy = 13;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 14;
		JLabel centerlineLabel = new JLabel("Centerline miles");
		setupPanel.add(centerlineLabel, c);

		c.gridx = 0;
		c.gridy = 15;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 16;
		JLabel FuelPriceLabel = new JLabel("Fuel Price ($ per GAL)");
		setupPanel.add(FuelPriceLabel, c);

		c.gridx = 0;
		c.gridy = 17;
		setupPanel.add(Box.createVerticalStrut(15), c);

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
		excludePanel.setLayout(new BoxLayout(excludePanel, BoxLayout.X_AXIS));
		excludePanel.add(weekend);
		excludePanel.add(holiday);
		excludePanel.add(weekday);
		weekday.setSelected(true);

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
		for (int i = 0; i < 24; i++)
		{
			operFromHour.addItem(Integer.toString(i));
			operToHour.addItem(Integer.toString(i));
		}

		operFromMin.addItem("0");
		operFromMin.addItem("15");
		operFromMin.addItem("30");
		operFromMin.addItem("45");

		operToMin.addItem("0");
		operToMin.addItem("15");
		operToMin.addItem("30");
		operToMin.addItem("45");

		operFromHour.setSelectedIndex(8);
		operToHour.setSelectedIndex(17);
		operFromMin.setSelectedIndex(3);
		operToMin.setSelectedIndex(3);

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
	public static String getOperationHours()
	{
		int noOfHours = operToHour.getSelectedIndex() - operFromHour.getSelectedIndex();
		int noOfMin = 0;
		noOfMin = operToMin.getSelectedIndex() - operFromMin.getSelectedIndex();
		if (operToMin.getSelectedIndex() < operFromMin.getSelectedIndex())
		{
			noOfHours--;
			noOfMin = 4 + noOfMin;

		}
		return Integer.toString(noOfHours) + ":" + Integer.toString(noOfMin * 15);
	}

	/**
	 * Gets the oper days.
	 *
	 * @return the oper days
	 */
	public static int getOperDays()
	{
		int operDays = 0;
		if (weekday.isSelected())
		{
			operDays += 232;
		}
		if (weekend.isSelected())
		{
			operDays += 96;
		}
		if (holiday.isSelected())
		{
			operDays += 8;
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
		int noOfHours = operToHour.getSelectedIndex() - operFromHour.getSelectedIndex();
		int noOfMin = 0;
		noOfMin = operToMin.getSelectedIndex() - operFromMin.getSelectedIndex();
		if (operToMin.getSelectedIndex() < operFromMin.getSelectedIndex())
		{
			noOfHours--;
			noOfMin = 4 + noOfMin;

		}
		return (noOfHours + noOfMin / 4);
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
	
	public static boolean[] getDaysActive() {
		boolean[] daysActive = new boolean[7];
		daysActive[0] = weekday.isSelected();  // Monday
		daysActive[1] = weekday.isSelected();  // Tuesday
		daysActive[2] = weekday.isSelected();  // Wednesday
		daysActive[3] = weekday.isSelected();  // Thursday
		daysActive[4] = weekday.isSelected();  // Friday
		daysActive[5] = weekend.isSelected();  // Saturday
		daysActive[6] = weekend.isSelected();  // Sunday
		return daysActive;
	}
	
	public static boolean getHolidaysIncluded() {
		return holiday.isSelected();
	}
}
