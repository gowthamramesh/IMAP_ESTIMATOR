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
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
	private static JTextField noImapTrucksField = new JTextField("1");

	/** The centerline miles field. */
	private static JTextField centerlineMilesField = new JTextField("12");

	/** The next button. */
	private static JButton nextButton = new JButton("NEXT");

	/** The prev button. */
	private static JButton prevButton = new JButton("BACK");
	
	/** The overall summary report button. */
	private static JButton reportButton = new JButton("CREATE SUMMARY REPORT");
	
	private static JButton beforeReportButton = new JButton("RL OUTPUT BEFORE IMAP");
	
	private static JButton afterReportButton = new JButton("RL OUTPUT WITH IMAP");
	
	/** The my id. */
	private static int myID = 2;

	/** The text field width. */
	private static int textFieldWidth = 125;

	/** The benefit estimate table. */
	private static JTable benefitEstimateTable;
	
	private static JTable fuelBreakdownTable;

	/** The passenger vehicle. */
	private static double passengerVehicle = 16.79;

	/** The commercial vehicle. */
	private static double commercialVehicle = 86.81;

	/** The truck percent. */
	private static double truckPercent = 5;

	/** The delay savings. */
	private static double delaySavings = 0;

	/** The delay savings cost. */
	private static double delaySavingsCost = 0;

	/** The fuel savings cost. */
	private static double fuelSavingsCost = 0;

	/** The fuel savings. */
	private static double fuelSavings = 0;
	
	private static double totalFuelUseBeforeIMAP;
	
	private static double totalFuelUseWithIMAP;
	
	private static double totalVMTVBeforeIMAP;
	
	private static double totalVMTVWithIMAP;

	/** The imap operation cost. */
	private static double imapOperationCost = 0;

	private static int annualDaysOfOperation = 0;
	
	private static final DecimalFormat formatter0 = new DecimalFormat("#,###");

	private static final DecimalFormat formatter2 = new DecimalFormat("#,##0.00");
	
	private static final DecimalFormat formatter3 = new DecimalFormat("#,##0.000");
	
	private static final DecimalFormat formatter4 = new DecimalFormat("#,##0.0000");
	
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
		mainpanel.setPreferredSize(new Dimension(700, 400));
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(Box.createVerticalStrut(10));
		mainpanel.add(getSetupPanel());
		mainpanel.add(getCostBenefitEstimationTable());
		mainpanel.add(getinfoTable());

	}
	
	public static void setTruckPercent(double truckPercent) {
		CostBenefitEstimate.truckPercent = truckPercent;
	}

	/**
	 * Gallon per mile for light veh.
	 *
	 * @param speed
	 *            the speed
	 * @return the double
	 */
	public static double gallonPerMileForLightVeh(double speed)
	{
		double y = 0;
		if (speed < 50)
		{
			y = 0.3197 *Math.pow((speed), (-0.615));
		}
		else
		{
			y = 0.009 *Math.pow((speed), (0.3337));
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
	public static double gallonPerMileForTruckVeh(double speed)
	{
		double y = 0;
		y = 1.0662 *Math.pow((speed), (-0.483));
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
		double CT = 30;
		double CL = 15;
		double NT = (float) 1.5;
		double OH = 6;
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
		//System.out.println(Float.parseFloat(InformationScreen.getFuelPrice()));
		fuelSavingsCost = fuelSavings * Float.parseFloat(InformationScreen.getFuelPrice());
	}

	/**
	 * Calculate fuel saving.
	 */
	private static void calculateFuelSaving()
	{
		//float totalDemandVMTWith = FreevalFileParser.getTotalDemandVMTWith();
		//float totalDemandVMTWithout = FreevalFileParser.getTotalDemandVMTWithout();
		truckPercent = FreevalFileParser.getTruckPercentage() / 100;
		//float gPerMileAvgSpeedWith = FreevalFileParser.getgPerMileAvgSpeedWith();
		//float gPerMileAvgSpeedWithout = FreevalFileParser.getgPerMileAvgSpeedWithout();
		//float gPerMileTotalRLSpeedWith = FreevalFileParser.getgPerMileTotalSpeedWith();
		//float gPerMileTotalRLSpeedWithout = FreevalFileParser.getgPerMileTotalSpeedWithout();
		fuelSavings = 0;
		//float noIMAPScenAvgSMS, noIMAPScenVMTD, withIMAPScenAvgSMS, withIMAPScenVMTD;
		double noIMAPScenPerAvgSMS, noIMAPScenPerVMTD, noIMAPScenPerVMTV, withIMAPScenPerAvgSMS, withIMAPScenPerVMTD, withIMAPScenPerVMTV;
		double beforeFuel, withIMAPFuel;
		totalFuelUseBeforeIMAP = totalFuelUseWithIMAP = totalVMTVBeforeIMAP = totalVMTVWithIMAP = 0.0;
		for (int scen = 1; scen <= FreevalFileParser.getNumberScenarios(); scen++) {  // Index starts at 1 (and not 0) to correctly access scenarios (0 is base scenario, not an RL scenario)
			//noIMAPScenAvgSMS = FreevalFileParser.getScenarioAvgSMSBeforeIMAP(scen);
			//noIMAPScenVMTD = FreevalFileParser.getScenarioVMTDBeforeIMAP(scen);
			//withIMAPScenAvgSMS = FreevalFileParser.getScenarioAvgSMSWithIMAP(scen);
			//withIMAPScenVMTD = FreevalFileParser.getScenarioVMTDWithIMAP(scen);
			for (int period = 0; period < FreevalFileParser.getNumberPeriods(); period++) {
				noIMAPScenPerAvgSMS = FreevalFileParser.getScenarioPeriodAvgSMSBeforeIMAP(scen, period);
				noIMAPScenPerVMTD = FreevalFileParser.getScenarioPeriodVMTDBeforeIMAP(scen, period);
				noIMAPScenPerVMTV = FreevalFileParser.getScenarioPeriodVMTVBeforeIMAP(scen, period);
				withIMAPScenPerAvgSMS = FreevalFileParser.getScenarioPeriodAvgSMSWithIMAP(scen, period);
				withIMAPScenPerVMTD = FreevalFileParser.getScenarioPeriodVMTDWithIMAP(scen, period);
				withIMAPScenPerVMTV = FreevalFileParser.getScenarioPeriodVMTVWithIMAP(scen, period);
				beforeFuel = ((noIMAPScenPerVMTV * gallonPerMileForTruckVeh(noIMAPScenPerAvgSMS) * truckPercent)  // Replaced noIMAPScenPerVMTD with noIMAPScenPerVMTV
						+ (noIMAPScenPerVMTV * gallonPerMileForLightVeh(noIMAPScenPerAvgSMS) * (1 - truckPercent))); // Replaced noIMAPScenPerVMTD with noIMAPScenPerVMTV
				withIMAPFuel = ((withIMAPScenPerVMTV * gallonPerMileForTruckVeh(withIMAPScenPerAvgSMS) * truckPercent) // Replaced withIMAPScenPerVMTD with withIMAPScenPerVMTV
						+ (withIMAPScenPerVMTV * gallonPerMileForLightVeh(withIMAPScenPerAvgSMS) * (1 - truckPercent))); // Replaced withIMAPScenPerVMTD with withIMAPScenPerVMTV
				fuelSavings += beforeFuel - withIMAPFuel;
				totalFuelUseBeforeIMAP += beforeFuel;
				totalFuelUseWithIMAP += withIMAPFuel;
				totalVMTVBeforeIMAP += noIMAPScenPerVMTV;
				totalVMTVWithIMAP += withIMAPScenPerVMTV;
			}
		}
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
		benefitTablePanel.setMaximumSize(new Dimension(700,180)); //.setPreferredSize(new Dimension(600, 300)); 
		benefitTablePanel.setLayout(new BoxLayout(benefitTablePanel, BoxLayout.X_AXIS));
		final String columnNames[] = new String[2];
		columnNames[0] = "SAVINGS";
		columnNames[1] = "VALUE";
		final String[] fuelColumnNames = new String[4];
		fuelColumnNames[0] = "";
		fuelColumnNames[1] = "VMTV (veh-mi)";
		fuelColumnNames[2] = "Fuel Used (gal)" ;
		fuelColumnNames[3] = "MPG";
		
		double fuelSavingsPerVMT = (totalFuelUseBeforeIMAP / totalVMTVBeforeIMAP) - (totalFuelUseWithIMAP / totalVMTVWithIMAP);
		//System.out.println(String.valueOf(totalFuelUseBeforeIMAP / totalVMTVBeforeIMAP));
		//System.out.println(String.valueOf(totalFuelUseWithIMAP / totalVMTVWithIMAP));
		double fuelSavingsCostPerVMT = fuelSavingsPerVMT * Float.parseFloat(InformationScreen.getFuelPrice());
		
		Object[][] data = { 
				{ "Annual Days of Operation", annualDaysOfOperation },
				{ "Delay Savings (veh-hr)", formatter0.format(delaySavings) },
				{ "Delay Savings Benefit ($)", formatter0.format(delaySavingsCost) },
				//{ "FUEL CONSUMPTION IMPACT (GAL)", formatter0.format(fuelSavings) },
				{ "Fuel Cost Impact ($)", formatter0.format(fuelSavingsCost) },
				//{ "FUEL CONSUMPTION IMPACT PER VMT (GAL)", formatter2.format(fuelSavingsPerVMT) },
				//{ "FUEL COST IMPACT PER VMT ($)", formatter2.format(fuelSavingsCostPerVMT) },
				{ "Oper. Costs ($)", formatter0.format(imapOperationCost) },
				{ "B/C Ratio", formatter2.format((delaySavingsCost / imapOperationCost)) } };
		
		Object[][] fuelData = {
				{" ","<HTML><b>VMTV (veh-mi)","<HTML><b>Fuel Used (gal)", "<HTML><b>MPG"},
				{"Before IMAP",formatter0.format(totalVMTVBeforeIMAP),formatter0.format(totalFuelUseBeforeIMAP), formatter2.format(totalVMTVBeforeIMAP / totalFuelUseBeforeIMAP)},
				{"With IMAP",formatter0.format(totalVMTVWithIMAP),formatter0.format(totalFuelUseWithIMAP), formatter2.format(totalVMTVWithIMAP / totalFuelUseWithIMAP)},
				{" "," "," "," "},
				{"% Difference",formatter3.format((totalVMTVWithIMAP - totalVMTVBeforeIMAP)/totalVMTVBeforeIMAP*100.0),
					formatter3.format((totalFuelUseWithIMAP - totalFuelUseBeforeIMAP)/totalFuelUseBeforeIMAP*100.0), 
					formatter3.format((((totalVMTVWithIMAP / totalFuelUseWithIMAP) - (totalVMTVBeforeIMAP / totalFuelUseBeforeIMAP))/(totalVMTVBeforeIMAP / totalFuelUseBeforeIMAP))*100.0)},
				{"Absolute Difference",formatter2.format(totalVMTVWithIMAP - totalVMTVBeforeIMAP),formatter2.format(totalFuelUseWithIMAP - totalFuelUseBeforeIMAP), formatter4.format((totalVMTVWithIMAP / totalFuelUseWithIMAP) - (totalVMTVBeforeIMAP / totalFuelUseBeforeIMAP))}
				
		};

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
				return false;
			}
		};
		
		DefaultTableModel fuelModel = new DefaultTableModel(fuelData,fuelColumnNames)
		{
			public boolean isCellEditable(int row, int column)
			{
				if (column < 1)
				{
					return false;
				}
				return false;
			}
		};

		benefitEstimateTable = new JTable(model)
		{
			public TableCellEditor getCellEditor(int row, int column)
			{
				return super.getCellEditor(row, column);
			}
		};
		
		fuelBreakdownTable = new JTable(fuelModel) {
			public TableCellEditor getCellEditor(int row, int column)
			{
				return super.getCellEditor(row, column);
			}
		};
		
		benefitEstimateTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		benefitEstimateTable.setRowHeight(25);
		benefitEstimateTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		benefitEstimateTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		//benefitEstimateTable.getColumnModel().getColumn(0).setMaxWidth(90);
		benefitEstimateTable.getColumnModel().getColumn(1).setMaxWidth(75);
		benefitEstimateTable.getColumnModel().getColumn(1).setMinWidth(75);
		benefitEstimateTable.setRowSelectionAllowed(false);
		benefitEstimateTable.setCellSelectionEnabled(false);
		benefitEstimateTable.getTableHeader().setResizingAllowed(false);
		benefitEstimateTable.setDefaultRenderer(Object.class, renderer);
		benefitEstimateTable.setGridColor(new Color(10, 10, 10));
		benefitEstimateTable.setShowGrid(true);
		benefitEstimateTable.setTableHeader(null);
		
		fuelBreakdownTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		fuelBreakdownTable.setRowHeight(25);
		fuelBreakdownTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		fuelBreakdownTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		fuelBreakdownTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
		fuelBreakdownTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
		fuelBreakdownTable.getColumnModel().getColumn(0).setMaxWidth(130);
		fuelBreakdownTable.getColumnModel().getColumn(0).setMinWidth(130);
		//fuelBreakdownTable.getColumnModel().getColumn(1).setMaxWidth(110);
		//fuelBreakdownTable.getColumnModel().getColumn(1).setMinWidth(110);
		//fuelBreakdownTable.getColumnModel().getColumn(2).setMaxWidth(110);
		//fuelBreakdownTable.getColumnModel().getColumn(2).setMinWidth(110);
		fuelBreakdownTable.getColumnModel().getColumn(3).setMaxWidth(65);
		fuelBreakdownTable.getColumnModel().getColumn(3).setMinWidth(65);
		fuelBreakdownTable.setRowSelectionAllowed(false);
		fuelBreakdownTable.setCellSelectionEnabled(false);
		fuelBreakdownTable.getTableHeader().setResizingAllowed(false);
		fuelBreakdownTable.setDefaultRenderer(Object.class, renderer);
		fuelBreakdownTable.setGridColor(new Color(10, 10, 10));
		fuelBreakdownTable.setShowGrid(true);
		fuelBreakdownTable.setTableHeader(null);
		JPanel benefitTP1 = new JPanel();
		benefitTP1.setLayout(new BoxLayout(benefitTP1, BoxLayout.Y_AXIS));
		JLabel label1 = new JLabel("SAVINGS AND BENEFITS");
		label1.setHorizontalTextPosition(JLabel.CENTER);
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
		//label1.setMaximumSize(new Dimension(999,999));
		benefitTP1.add(label1);
		benefitTP1.add(benefitEstimateTable);
		
		JPanel benefitTP2 = new JPanel();
		benefitTP2.setLayout(new BoxLayout(benefitTP2, BoxLayout.Y_AXIS));
		JLabel label2 = new JLabel("FUEL IMPACT BREAKDOWN");
		label2.setHorizontalTextPosition(JLabel.CENTER);
		label2.setAlignmentX(Component.CENTER_ALIGNMENT);
		//label2.setHorizontalAlignment(SwingConstants.CENTER);
		//label2.setMaximumSize(new Dimension(150,16));
		benefitTP2.add(label2);
		benefitTP2.add(fuelBreakdownTable);
		
		benefitTablePanel.add(benefitTP1);
		benefitTablePanel.add(Box.createHorizontalStrut(15));
		benefitTablePanel.add(benefitTP2);
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
		setupPanel.setMaximumSize(new Dimension(700, 100));

		c.gridx = 0;
		c.gridy = 0;
		JLabel laborCostLabel = new JLabel("Facility:");
		setupPanel.add(laborCostLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(5), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel truckOperLabel = new JLabel("Centerline miles:");
		setupPanel.add(truckOperLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(5), c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel opHoursLabel = new JLabel("Operation hours");
		setupPanel.add(opHoursLabel, c);

		c.gridx = 0;
		c.gridy = 5;
		setupPanel.add(Box.createVerticalStrut(5), c);

		c.gridx = 0;
		c.gridy = 6;
		JLabel operHoursLabel = new JLabel("County:");
		setupPanel.add(operHoursLabel, c);

		c.gridx = 0;
		c.gridy = 7;
		setupPanel.add(Box.createVerticalStrut(5), c);

		// adding gap between label and field

		c.gridx = 1;
		for (int i = 0; i < 13; i++)
		{
			c.gridy = i;
			setupPanel.add(Box.createHorizontalStrut(85), c);
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
		
		reportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileFilter csvFilter = new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory() ||  f.getName().endsWith(".txt");
					}
					
					@Override
					public String getDescription() {
						return ".txt files";
					}
				};
				fc.setFileFilter(csvFilter);
				int fileSelected = fc.showSaveDialog(null);
				if (fileSelected == JFileChooser.APPROVE_OPTION) {
					boolean success = FreevalFileParser.createSummaryReport(fc.getSelectedFile());
					if (success) {
						JOptionPane.showMessageDialog(null,
								"<HTML>Report generated successfully. File saved at:<br>"
								+fc.getSelectedFile().getAbsolutePath() + (fc.getSelectedFile().getAbsolutePath().endsWith(".txt") ? "" : ".txt"), 
								"Report Created",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"<HTML>Something went wrong while generating the report.  If overwriting<br>"
								+ "an existing file, please make sure the file is closed before proceeding.", 
								"Report Creator Failed",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
		
		beforeReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileFilter csvFilter = new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory() ||  f.getName().endsWith(".csv");
					}
					
					@Override
					public String getDescription() {
						return ".csv files";
					}
				};
				fc.setFileFilter(csvFilter);
				int fileSelected = fc.showSaveDialog(null);
				if (fileSelected == JFileChooser.APPROVE_OPTION) {
					boolean success = FreevalFileParser.createBeforeIMAPRLOutput(fc.getSelectedFile());
					if (success) {
						JOptionPane.showMessageDialog(null,
								"<HTML>Report generated successfully. File saved at:<br>"
								+fc.getSelectedFile().getAbsolutePath() + (fc.getSelectedFile().getAbsolutePath().endsWith(".csv") ? "" : ".csv"), 
								"Report Created",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"<HTML>Something went wrong while generating the report.  If overwriting<br>"
								+ "an existing file, please make sure the file is closed before proceeding.", 
								"Report Creator Failed",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		
		afterReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileFilter csvFilter = new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory() ||  f.getName().endsWith(".csv");
					}
					
					@Override
					public String getDescription() {
						return ".csv files";
					}
				};
				fc.setFileFilter(csvFilter);
				int fileSelected = fc.showSaveDialog(null);
				if (fileSelected == JFileChooser.APPROVE_OPTION) {
					boolean success = FreevalFileParser.createAfterIMAPRLOutput(fc.getSelectedFile());
					if (success) {
						JOptionPane.showMessageDialog(null,
								"<HTML>Report generated successfully. File saved at:<br>"
								+ fc.getSelectedFile().getAbsolutePath() + (fc.getSelectedFile().getAbsolutePath().endsWith(".csv") ? "" : ".csv"), 
								"Report Created",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"<HTML>Something went wrong while generating the report.  If overwriting<br>"
								+ "an existing file, please make sure the file is closed before proceeding.", 
								"Report Creator Failed",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		containerPanel.setMaximumSize(new Dimension(700,50));
		containerPanel.add(prevButton);
		containerPanel.add(reportButton);
		containerPanel.add(beforeReportButton);
		containerPanel.add(afterReportButton);
		// containerPanel.add(nextButton);
		return containerPanel;
	}
	
	public static String getDelaySavingsString() {
		return formatter2.format(delaySavings);
	}
	
	public static String getDelaySavingsBenefitString() {
		return formatter2.format(delaySavingsCost);
	}
	
	public static String getFuelSavingsString() {
		return formatter2.format(fuelSavings);
	}
	
	public static String getFuelSavingsBenefitString() {
		return formatter2.format(fuelSavingsCost);
	}
	
	public static String getOperationCostString() {
		return formatter2.format(imapOperationCost);
	}
	
	public static String getBCRatioString() {
		return formatter2.format((delaySavingsCost / imapOperationCost));
	}
	
	public static double getFuelUsePerVMTVBeforeIMAP() {
		return totalFuelUseBeforeIMAP / totalVMTVBeforeIMAP;
	}
	
	public static double getFuelUsePerVMTVWithIMAP() {
		return totalFuelUseWithIMAP / totalVMTVWithIMAP;
	}
	
	public static double getTotalVMTVBeforeIMAP() {
		return totalVMTVBeforeIMAP;
	}
	
	public static double getTotalVMTVWithIMAP() {
		return totalVMTVWithIMAP;
	}
	
	public static double getTotalFuelUseBeforeIMAP() {
		return totalFuelUseBeforeIMAP;
	}
	
	public static double getTotalFuelUseWithIMAP() {
		return totalFuelUseWithIMAP;
	}
}
