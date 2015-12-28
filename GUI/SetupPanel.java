
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

public class SetupPanel
{
	private static JPanel				mainpanel;
	private static JTextField			fileName			= new JTextField("I40Raleigh");
	private static JTextField			facilityType		= new JTextField("40");
	private static JComboBox<String>	divisionType		= new JComboBox<String>();
	private static JTextField			County				= new JTextField("Wake");
	private static JTextField			fromIntersection	= new JTextField("I-40/Exit1");
	private static JTextField			toIntersection		= new JTextField("I-40/Exit16");
	private static JButton				nextButton			= new JButton("NEXT");
	private static JComboBox<String>	facilityTypeCombo	= new JComboBox<String>();
	private static int					myID				= 0;
	private static int					columns				= 200;
	private static int					textFieldWidth		= 220;

	public static JPanel getMainPanel()
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
		mainpanel.add(Box.createVerticalStrut(30));
		mainpanel.add(getSetupPanel());
		mainpanel.add(getinfoTable());

	}

	private static JPanel getSetupPanel()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel setupPanel = new JPanel();
		setupPanel.setLayout(new GridBagLayout());
		setupPanel.setBorder(BorderFactory.createTitledBorder("Setup Information"));
		setupPanel.setMaximumSize(new Dimension(800, 400));

		c.gridx = 0;
		c.gridy = 0;
		JLabel fileNameLabel = new JLabel("File Name");
		setupPanel.add(fileNameLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 2;
		c.gridy = 0;
		fileName.setColumns(columns);
		fileName.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(fileName);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel facilityTypeLabel = new JLabel("Facility Type");
		setupPanel.add(facilityTypeLabel, c);

		c.gridx = 1;
		c.gridy = 2;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		facilityTypeCombo.addItem("I");
		facilityTypeCombo.addItem("US");
		facilityTypeCombo.addItem("NC");
		facilityTypeCombo.addItem("SR");

		c.gridx = 2;
		c.gridy = 2;
		facilityType.setColumns(columns);
		facilityType.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(facilityTypeCombo, c);

		c.gridx = 3;
		c.gridy = 2;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 4;
		c.gridy = 2;
		facilityType.setColumns(columns);
		facilityType.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(facilityType, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel divisionLabel = new JLabel("Division");
		setupPanel.add(divisionLabel, c);

		c.gridx = 1;
		c.gridy = 4;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		for (int i = 0; i < 40; i++)
		{
			divisionType.addItem(Integer.toString(i + 1));
		}
		c.gridx = 2;
		c.gridy = 4;
		divisionType.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(divisionType, c);

		c.gridx = 0;
		c.gridy = 5;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 0;
		c.gridy = 6;
		JLabel countyLabel = new JLabel("County");
		setupPanel.add(countyLabel, c);

		c.gridx = 1;
		c.gridy = 6;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 2;
		c.gridy = 6;
		County.setColumns(columns);
		County.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(County, c);

		c.gridx = 0;
		c.gridy = 7;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 0;
		c.gridy = 8;
		JLabel road = new JLabel("Road");
		setupPanel.add(road, c);

		c.gridx = 1;
		c.gridy = 8;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 2;
		c.gridy = 8;
		JLabel interLabel = new JLabel(" From: Intersection");
		setupPanel.add(interLabel, c);

		c.gridx = 3;
		c.gridy = 8;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 4;
		c.gridy = 8;
		JLabel toInter = new JLabel(" To: Intersection");
		setupPanel.add(toInter, c);

		c.gridx = 0;
		c.gridy = 9;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 0;
		c.gridy = 10;

		c.gridx = 1;
		c.gridy = 10;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 2;
		c.gridy = 10;
		fromIntersection.setColumns(columns);
		fromIntersection.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(fromIntersection, c);

		c.gridx = 3;
		c.gridy = 10;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 4;
		c.gridy = 10;
		toIntersection.setColumns(columns);
		toIntersection.setMinimumSize(new Dimension(textFieldWidth, 20));
		setupPanel.add(toIntersection, c);

		// adding gap between label and field

		c.gridx = 0;
		for (int i = 0; i < 12; i++)
		{
			c.gridy = i;
			setupPanel.add(Box.createVerticalStrut(20), c);
		}

		return setupPanel;
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

		containerPanel.add(nextButton);
		return containerPanel;
	}

	public static String getFacilityData()
	{
		String facilityData = facilityTypeCombo.getSelectedItem().toString() + " " + facilityType.getText() + " from "
				+ fromIntersection.getText() + "to " + toIntersection.getText();
		return facilityData;
	}

	public static String getCounty()
	{
		return County.getText();
	}
}
