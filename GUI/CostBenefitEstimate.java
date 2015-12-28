
package GUI;

import java.awt.Color;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class CostBenefitEstimate
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
	private static int			myID					= 2;
	private static int			columns					= 200;
	private static int			textFieldWidth			= 125;
	private static JTable		benefitEstimateTable;
	private static float		passengerVehicle		= (float) 16.79;
	private static float		commercialVehicle		= (float) 86.81;
	private static float		truckPercent			= 5;
	private static float		delaySavings			= 0;
	private static float		delaySavingsCost		= 0;
	private static float		fuelSavingsCost			= 0;
	private static float		imapOperationCost		= 0;

	public static JPanel getEstimationBenefitPanel()
	{
		mainpanel = new JPanel();
		initComp();
		return mainpanel;
	}

	private static void initComp()
	{
		estimateCostBenefit();
		mainpanel.setPreferredSize(new Dimension(800, 400));
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(Box.createVerticalStrut(10));
		mainpanel.add(getSetupPanel());
		mainpanel.add(getCostBenefitEstimationTable());
		mainpanel.add(getinfoTable());

	}

	private static void estimateCostBenefit()
	{
		// Set data to freeval and retrive op to Freeval file parrser
		FreevalFileParser.runFreeval();
		computeDelaySaving();
		calculateFuelBenefit();
		calculateIMapOperationCost();
	}

	private static void calculateIMapOperationCost()
	{
		float CT = 30;
		float CL = 15;
		float NT = (float) 1.5;
		float OH = 6;
		int OD = 240;

		CT = InformationScreen.getCostTruck();
		CL = InformationScreen.getCostLabor();
		NT = InformationScreen.getNoTrucks();
		OH = InformationScreen.getOperationHoursInt();
		OD = InformationScreen.getOperDays();

		imapOperationCost = (CT + CL) * NT * OH * OD;

	}

	private static void calculateFuelBenefit()
	{
		int totalDemandVMTWith = FreevalFileParser.getTotalDemandVMTWith();
		int totalDemandVMTWithout = FreevalFileParser.getTotalDemandVMTWithout();
		int gPerMileAvgSpeedWith = FreevalFileParser.getgPerMileAvgSpeedWith();
		int gPerMileAvgSpeedWithout = FreevalFileParser.getgPerMileAvgSpeedWithout();
		fuelSavingsCost = (totalDemandVMTWith * gPerMileAvgSpeedWith)
				- (totalDemandVMTWithout * gPerMileAvgSpeedWithout);
	}

	private static void computeDelaySaving()
	{
		truckPercent = FreevalFileParser.getTruckPercentage();
		int totVehDelayWithout = FreevalFileParser.getTotalVehDelayWithoutImap();
		int totVehDelayWith = FreevalFileParser.getTotalVehDelayWithImap();
		delaySavings = totVehDelayWithout - totVehDelayWith;

		delaySavingsCost = (delaySavings * (1 - truckPercent) * (passengerVehicle))
				+ (delaySavings * (truckPercent) * (commercialVehicle));

	}

	private static JPanel getCostBenefitEstimationTable()
	{
		JPanel benefitTablePanel = new JPanel();
		benefitTablePanel.setPreferredSize(new Dimension(500, 400));
		benefitTablePanel.setLayout(new BoxLayout(benefitTablePanel, BoxLayout.Y_AXIS));
		final String columnNames[] = new String[2];
		columnNames[0] = "SAVINGS";
		columnNames[1] = "VALUE";

		Object[][] data = { { "DELAY SAVINGS (beh-hr)", delaySavings },
				{ "DELAY SAVING BENEFITS ($)", delaySavingsCost }, { "FUEL SAVINGS ($)", fuelSavingsCost },
				{ "OPER. COSTS ($)", imapOperationCost }, { "B/C RATIO", (delaySavingsCost / imapOperationCost) } };

		// Create a new table instance

		final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column)
			{
				Component rendererComp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				boolean cellEditable = table.getModel().isCellEditable(row, column);
				rendererComp.setForeground(Color.black);
				rendererComp.setBackground(new Color(230, 230, 230));
				if (row % 2 == 0)
				{
					rendererComp.setBackground(new Color(200, 200, 200));
				}
				return rendererComp;
			}
		};
		renderer.setHorizontalAlignment(JLabel.CENTER);

		DefaultTableModel model = new DefaultTableModel(data, columnNames)
		{
			public boolean isCellEditable(int row, int column)
			{
				if (column < 1)
				{
					return false;
				}
				return true;
			}
		};

		benefitEstimateTable = new JTable(model)
		{
			public TableCellEditor getCellEditor(int row, int column)
			{
				return super.getCellEditor(row, column);
			}
		};
		benefitEstimateTable.setRowHeight(25);
		benefitEstimateTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		benefitEstimateTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		benefitEstimateTable.setRowSelectionAllowed(false);
		benefitEstimateTable.setCellSelectionEnabled(false);
		benefitEstimateTable.getTableHeader().setResizingAllowed(false);
		benefitEstimateTable.setDefaultRenderer(Object.class, renderer);
		benefitEstimateTable.setGridColor(new Color(10, 10, 10));
		benefitEstimateTable.setShowGrid(true);
		benefitEstimateTable.setTableHeader(null);
		benefitTablePanel.add(benefitEstimateTable);
		return benefitTablePanel;

	}

	private static JPanel getSetupPanel()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel setupPanel = new JPanel();
		setupPanel.setLayout(new GridBagLayout());
		setupPanel.setBorder(BorderFactory.createTitledBorder("Facility Input Data"));
		setupPanel.setMaximumSize(new Dimension(800, 300));

		c.gridx = 0;
		c.gridy = 0;
		JLabel laborCostLabel = new JLabel("Facility:");
		setupPanel.add(laborCostLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel truckOperLabel = new JLabel("Centerline miles:");
		setupPanel.add(truckOperLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel capitalCostLabel = new JLabel("Operation hours");
		setupPanel.add(capitalCostLabel, c);

		c.gridx = 0;
		c.gridy = 5;
		setupPanel.add(Box.createVerticalStrut(15), c);

		c.gridx = 0;
		c.gridy = 6;
		JLabel operHoursLabel = new JLabel("County:");
		setupPanel.add(operHoursLabel, c);

		c.gridx = 0;
		c.gridy = 7;
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
		setupPanel.add(new JLabel(SetupPanel.getFacilityData()), c);

		c.gridy = 2;
		setupPanel.add(new JLabel(InformationScreen.getCenterLineMiles()), c);

		c.gridy = 4;
		setupPanel.add(new JLabel(InformationScreen.getOperationHours()), c);

		c.gridy = 6;
		setupPanel.add(new JLabel(SetupPanel.getCounty()), c);

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

		prevButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.changePanel(1);
			}
		});
		containerPanel.add(prevButton);
		containerPanel.add(nextButton);
		return containerPanel;
	}
}
