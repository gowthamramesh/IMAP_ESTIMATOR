
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InformationScreen
{
	private static JPanel		mainpanel;
	private static JTextField	laborCostField			= new JTextField("15");
	private static JTextField	truckCostField			= new JTextField("30");
	private static JTextField	capitalCostField		= new JTextField("5000");
	private static JComboBox	operFromHour			= new JComboBox();
	private static JComboBox	operFromMin				= new JComboBox();
	private static JComboBox	operToHour				= new JComboBox();
	private static JComboBox	operToMin				= new JComboBox();
	private static JTextField	annualOperDaysField		= new JTextField("240");
	private static JTextField	noImapTrucksField		= new JTextField("18");
	private static JTextField	centerlineMilesField	= new JTextField("148");
	private static JButton		nextButton				= new JButton("NEXT");
	private static JButton		prevButton				= new JButton("BACK");
	private static int			myID					= 1;
	private static int			columns					= 200;
	private static int			textFieldWidth			= 125;

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

	private static void initComp()
	{
		mainpanel.setPreferredSize(new Dimension(800, 400));
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(Box.createVerticalStrut(10));
		mainpanel.add(getSetupPanel());
		mainpanel.add(getinfoTable());

	}

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
		JLabel capitalCostLabel = new JLabel("Capital cost/hr ($)");
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
		setupPanel.add(annualDaysLabel, c);

		c.gridx = 0;
		c.gridy = 9;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 10;
		JLabel imapTrucksLabel = new JLabel("Number of IMAP trucks");
		setupPanel.add(imapTrucksLabel, c);

		c.gridx = 0;
		c.gridy = 11;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 12;
		JLabel centerlineLabel = new JLabel("Centerline miles");
		setupPanel.add(centerlineLabel, c);

		c.gridx = 0;
		c.gridy = 13;
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
		setupPanel.add(annualOperDaysField, c);

		c.gridy = 10;
		setupPanel.add(noImapTrucksField, c);

		c.gridy = 12;
		setupPanel.add(centerlineMilesField, c);

		return setupPanel;
	}

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

	public static String getCenterLineMiles()
	{
		return centerlineMilesField.getText();
	}

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

	public static int getOperDays()
	{
		return Integer.parseInt(annualOperDaysField.getText());
	}

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

	public static float getNoTrucks()
	{
		return Float.parseFloat(noImapTrucksField.getText());
	}

	public static float getCostLabor()
	{
		return Float.parseFloat(laborCostField.getText());
	}

	public static float getCostTruck()
	{
		return Float.parseFloat(truckCostField.getText());
	}
}
