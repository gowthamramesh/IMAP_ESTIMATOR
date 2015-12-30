
package GUI;

import java.awt.Component;
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

public class EstimationScreen
{
	private static JPanel				mainpanel;
	private static JComboBox<String>	areaType			= new JComboBox<String>();
	private static JComboBox<String>	studyType			= new JComboBox<String>();
	private static JComboBox<String>	incidentRate		= new JComboBox<String>();
	private static JComboBox<String>	incidentSeverity	= new JComboBox<String>();
	private static JComboBox<String>	freevalFile			= new JComboBox<String>();
	private static JButton				changeIncidentRate	= new JButton("...");
	private static JButton				changeIncidentSev	= new JButton("...");
	private static JButton				prevButton			= new JButton("BACK");
	private static JButton				estimateCostBenefit	= new JButton("PERFORM COST BENEFIT ANALYSIS");
	private static int					myID				= 2;
	private static IncidentRateData		incidentData;
	private static CrashRateData		crashRateData;
	private static IncidentSeverityData	incidentSevData;

	public static JPanel getEstimationPanel()
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
		incidentData = new IncidentRateData();
		crashRateData = new CrashRateData();
		incidentSevData = new IncidentSeverityData();

		mainpanel.setPreferredSize(new Dimension(800, 400));
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(Box.createVerticalStrut(20));
		mainpanel.add(getSetupPanel());
		mainpanel.add(getEstimationButtonPanel());
		// mainpanel.add(getinfoTable());

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
		JLabel laborCostLabel = new JLabel("Area Type");
		setupPanel.add(laborCostLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel truckOperLabel = new JLabel("Study Type");
		setupPanel.add(truckOperLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel capitalCostLabel = new JLabel("Incident rate or crash rate");
		setupPanel.add(capitalCostLabel, c);

		c.gridx = 0;
		c.gridy = 5;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 6;
		JLabel operHoursLabel = new JLabel("Incident severity and duration");
		setupPanel.add(operHoursLabel, c);

		c.gridx = 0;
		c.gridy = 7;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 8;
		JLabel annualDaysLabel = new JLabel("Freeval file selection");
		setupPanel.add(annualDaysLabel, c);

		c.gridx = 0;
		c.gridy = 9;
		setupPanel.add(Box.createVerticalStrut(25), c);

		// adding gap between label and field

		c.gridx = 1;
		for (int i = 0; i < 10; i++)
		{
			c.gridy = i;
			setupPanel.add(Box.createHorizontalStrut(100), c);
		}

		populateComboBox();

		// Adding all text fields
		c.gridx = 2;
		c.gridy = 0;
		setupPanel.add(areaType, c);

		c.gridy = 2;
		setupPanel.add(studyType, c);

		c.gridy = 4;
		JPanel incidentRatePanel = new JPanel();
		incidentRatePanel.add(incidentRate);
		incidentRatePanel.add(changeIncidentRate);
		setupPanel.add(incidentRatePanel, c);

		c.gridy = 6;
		JPanel incidentSevPanel = new JPanel();
		incidentSevPanel.add(incidentSeverity);
		incidentSevPanel.add(changeIncidentSev);
		setupPanel.add(incidentSevPanel, c);

		c.gridy = 8;
		setupPanel.add(freevalFile, c);

		return setupPanel;
	}

	private static void populateComboBox()
	{
		areaType.addItem("Urban");
		areaType.addItem("Rural");

		studyType.addItem("Before Study");
		studyType.addItem("After Study");

		incidentRate.addItem("Site Specific - Incident Rate");
		incidentRate.addItem("Site Specific - Crash Rate");
		incidentRate.addItem("Statewide");

		incidentSeverity.addItem("Site Specific                         ");
		incidentSeverity.addItem("Statewide");

		changeIncidentRate.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				switch (incidentRate.getSelectedIndex())
				{
					case 0:
						incidentData.displayIncidentTable();
						break;
					case 1:
						crashRateData.displayCrashRateData(-1, -1);
						break;
					case 2:
						if (areaType.getSelectedIndex() == 0)
						{
							crashRateData.displayCrashRateData(2, 90);
						}
						else
						{
							crashRateData.displayCrashRateData(2, 60);
						}
						break;
				}
			}
		});

		changeIncidentSev.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				incidentSevData.displayIncidentTable(areaType.getSelectedIndex(), incidentSeverity.getSelectedIndex());
			}
		});

	}

	private static JPanel getinfoTable()
	{
		JPanel containerPanel = new JPanel();
		prevButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.changePanel(myID + 1);
			}
		});

		containerPanel.add(prevButton);
		return containerPanel;
	}

	private static JPanel getEstimationButtonPanel()
	{
		JPanel containerPanel = new JPanel();
		estimateCostBenefit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Assign necessary information to FreevalFileParser
				FreevalFileParser.selectSeedFile();
				//FreevalFileParser.setRngSeed(rngSeed);  // Activate to set random number generator seed
				FreevalFileParser.setActiveDays(new boolean[] {true, true, true, true, true, false, false}); // Activate to set active day array
				FreevalFileParser.setTruckPercentage(5); // Activate to set truck percentage
				// Check before or after study
				if (studyType.getSelectedIndex() == 0) {
					// Before Study
					//FreevalFileParser.setCrashRateFrequenciesNoIMAP(crashRateFrequencies);
					//FreevalFileParser.setCrashRateRatioNoIMAP(crashRateRatio);
				} else {
					// After study
					//FreevalFileParser.setCrashRateFrequencies(crashRateFrequencies);
					//FreevalFileParser.setCrashRateRatioWithIMAP(crashRateRatio);
				}
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
		containerPanel.add(estimateCostBenefit);
		return containerPanel;
	}
}
