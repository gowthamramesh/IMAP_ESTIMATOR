/*
 * 
 */

package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

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

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

// TODO: Auto-generated Javadoc
/**
 * The Class CostBenefitEstimate.
 */
public class CostBenefitEstimate
{

	/** The mainpanel. */
	private static JPanel mainpanel;

	/** The labor cost field. */
	private static JTextField laborCostField = new JTextField("15");

	private static JTextField fuelPrice = new JTextField("1.99");

	/** The truck cost field. */
	private static JTextField truckCostField = new JTextField("30");

	/** The capital cost field. */
	private static JTextField capitalCostField = new JTextField("5000");

	/** The oper to min. */

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
	private static int myID = 2;

	/** The text field width. */
	private static int textFieldWidth = 125;

	/** The benefit estimate table. */
	private static JTable benefitEstimateTable;

	/** The passenger vehicle. */
	private static float passengerVehicle = (float) 16.79;

	/** The commercial vehicle. */
	private static float commercialVehicle = (float) 86.81;

	/** The truck percent. */
	private static float truckPercent = 5;

	/** The delay savings. */
	private static float delaySavings = 0;

	/** The delay savings cost. */
	private static float delaySavingsCost = 0;

	/** The fuel savings cost. */
	private static double fuelSavingsCost = 0;

	/** The fuel savings. */
	private static double fuelSavings = 0;

	/** The imap operation cost. */
	private static float imapOperationCost = 0;

	private static int annualDaysOfOperation = 0;
	
	private static final DecimalFormat formatter0 = new DecimalFormat("#,###");

	private static final DecimalFormat formatter2 = new DecimalFormat("#,###.00");
	
	/**
	 * Gets the estimation benefit panel.
	 *
	 * @return the estimation benefit panel
	 */
	public static JPanel getEstimationBenefitPanel()
	{
		mainpanel = new JPanel();
		initComp();
		return mainpanel;
	}

	/**
	 * Inits the comp.
	 */
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
	
	public static void setTruckPercent(float truckPercent) {
		CostBenefitEstimate.truckPercent = truckPercent;
	}

	/**
	 * Gallon per mile for light veh.
	 *
	 * @param speed
	 *            the speed
	 * @return the double
	 */
	private static double gallonPerMileForLightVeh(double speed)
	{
		double y = 0;
		if (speed < 50)
		{
			y = Math.pow((0.3197 * speed), (-0.615));
		}
		else
		{
			y = Math.pow((0.009 * speed), (0.3337));
		}
		return y;
	}

	/**
	 * Gallon per mile for truck veh.
	 *
	 * @param speed
	 *            the speed
	 * @return the double
	 */
	private static double gallonPerMileForTruckVeh(double speed)
	{
		double y = 0;
		y = Math.pow((1.0662 * speed), (-0.483));
		return y;
	}

	/**
	 * Estimate cost benefit.
	 */
	private static void estimateCostBenefit()
	{
		// Set data to freeval and retrive op to Freeval file parser
		FreevalFileParser.runFreeval();
		annualDaysOfOperation = InformationScreen.getOperDays();
		computeDelaySaving();
		calculateFuelSaving();
		calculateFuelBenefit();
		calculateIMapOperationCost();
	}

	/**
	 * Calculate i map operation cost.
	 */
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
		
		//System.out.println(String.valueOf(CT) + ","+String.valueOf(CL) + ","+String.valueOf(NT) + ","+String.valueOf(OH) + ","+String.valueOf(OD));

		imapOperationCost = (CT + CL) * NT * OH * OD;
		//imapOperationCost = imapOperationCost / 2; // Unidirectional
	}

	/**
	 * Calculate fuel benefit.
	 */
	private static void calculateFuelBenefit()
	{
		System.out.println(Float.parseFloat(InformationScreen.getFuelPrice()));
		fuelSavingsCost = fuelSavings * Float.parseFloat(InformationScreen.getFuelPrice());
	}

	/**
	 * Calculate fuel saving.
	 */
	private static void calculateFuelSaving()
	{
		float totalDemandVMTWith = FreevalFileParser.getTotalDemandVMTWith();
		float totalDemandVMTWithout = FreevalFileParser.getTotalDemandVMTWithout();
		truckPercent = FreevalFileParser.getTruckPercentage() / 100;
		float gPerMileAvgSpeedWith = FreevalFileParser.getgPerMileAvgSpeedWith();
		float gPerMileAvgSpeedWithout = FreevalFileParser.getgPerMileAvgSpeedWithout();
		fuelSavings = ((totalDemandVMTWithout * gallonPerMileForTruckVeh(gPerMileAvgSpeedWithout) * truckPercent)
				+ (totalDemandVMTWithout * gallonPerMileForLightVeh(gPerMileAvgSpeedWithout) * (1 - truckPercent)))
				- ((totalDemandVMTWith * gallonPerMileForTruckVeh(gPerMileAvgSpeedWith) * truckPercent)
						+ (totalDemandVMTWith * gallonPerMileForLightVeh(gPerMileAvgSpeedWith) * (1 - truckPercent)));
	}

	/**
	 * Compute delay saving.
	 */
	private static void computeDelaySaving()
	{
		truckPercent = FreevalFileParser.getTruckPercentage() / 100;
		float totVehDelayWithout = FreevalFileParser.getTotalVehDelayWithoutImap();
		float totVehDelayWith = FreevalFileParser.getTotalVehDelayWithImap();
		delaySavings = totVehDelayWithout - totVehDelayWith;

		delaySavingsCost = (delaySavings * (1 - truckPercent) * (passengerVehicle))
				+ (delaySavings * (truckPercent) * (commercialVehicle));

	}

	/**
	 * Gets the cost benefit estimation table.
	 *
	 * @return the cost benefit estimation table
	 */
	private static JPanel getCostBenefitEstimationTable()
	{
		JPanel benefitTablePanel = new JPanel();
		benefitTablePanel.setPreferredSize(new Dimension(500, 400));
		benefitTablePanel.setLayout(new BoxLayout(benefitTablePanel, BoxLayout.Y_AXIS));
		final String columnNames[] = new String[2];
		columnNames[0] = "SAVINGS";
		columnNames[1] = "VALUE";

		Object[][] data = { { "ANNUAL DAYS OF OPERATION", annualDaysOfOperation },
				{ "DELAY SAVINGS (veh-hr)", formatter0.format(delaySavings) }, { "DELAY SAVING BENEFITS ($)", formatter0.format(delaySavingsCost) },
				{ "FUEL SAVINGS (GAL)", formatter0.format(fuelSavings) }, { "FUEL SAVINGS BENEFIT ($)", formatter0.format(fuelSavingsCost) },
				{ "OPER. COSTS ($)", formatter0.format(imapOperationCost) }, { "B/C RATIO", formatter2.format((delaySavingsCost / imapOperationCost)) } };

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
				Main.changePanel(1);
			}
		});
		containerPanel.add(prevButton);
		// containerPanel.add(nextButton);
		return containerPanel;
	}
}
