/*
 * 
 */

package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

// TODO: Auto-generated Javadoc
/**
 * The Class IncidentSeverityData.
 */
public class IncidentSeverityData extends JDialog
{
	
	/** The incident data. */
	private IncidentSeverityData	incidentData;
	
	/** The both. */
	private JRadioButton			both				= new JRadioButton("Both");
	
	/** The before. */
	private JRadioButton			before				= new JRadioButton("Only Before");
	
	/** The after. */
	private JRadioButton			after				= new JRadioButton("Only After");
	
	/** The before table. */
	private JTable					beforeTable;
	
	/** The after table. */
	private JTable					afterTable;
	
	/** The area type val. */
	private int						areaTypeVal			= 0;
	
	/** The is state wide val. */
	private int						isStateWideVal		= 0;
	
	/** The after table editing. */
	private boolean					afterTableEditing	= true;
	
	/** The before table editing. */
	private boolean					beforeTableEditing	= true;

	/**
	 * Instantiates a new incident severity data.
	 */
	public IncidentSeverityData()
	{
		super(Main.getMainFrame());
		incidentData = this;
		setTitle("INCIDENT SEVERITY");
		setModal(true);
		initComponents();
	}

	/**
	 * Inits the components.
	 */
	private void initComponents()
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		setPreferredSize(new Dimension(900, 450));
		setLocation(screenSize.width / 2 - 450, screenSize.height / 2 - 225);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(getRadioPanel());
		getContentPane().add(getIncidentTable());
		getContentPane().add(getButtonPanel());
		pack();
	}

	/**
	 * Gets the button panel.
	 *
	 * @return the button panel
	 */
	private JPanel getButtonPanel()
	{
		JPanel buttonPanel = new JPanel();
		JButton setValues = new JButton("Continue");
		setValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Setting information for both
				for (int i = 1; i < 6; i++) {
					// Setting Before
					FreevalFileParser.setIncidentDurationDistributionNoIMAP(i-1, returnFloat(beforeTable.getValueAt(i, 1)));
					FreevalFileParser.setIncidentDurationMeanNoIMAP(i-1, returnFloat(beforeTable.getValueAt(i, 2)));
					FreevalFileParser.setIncidentDurationStdDevNoIMAP(i-1, returnFloat(beforeTable.getValueAt(i, 3)));
					FreevalFileParser.setIncidentDurationMinNoIMAP(i-1, returnFloat(beforeTable.getValueAt(i, 4)));
					FreevalFileParser.setIncidentDurationMaxNoIMAP(i-1, Math.max(returnFloat(beforeTable.getValueAt(i, 5)), returnFloat(beforeTable.getValueAt(i, 2))));
					// Setting After
					FreevalFileParser.setIncidentDurationDistributionWithIMAP(i-1, returnFloat(afterTable.getValueAt(i, 1)));
					FreevalFileParser.setIncidentDurationMeanWithIMAP(i-1, returnFloat(afterTable.getValueAt(i, 2)));
					FreevalFileParser.setIncidentDurationStdDevWithIMAP(i-1, returnFloat(afterTable.getValueAt(i, 3)));
					FreevalFileParser.setIncidentDurationMinWithIMAP(i-1, returnFloat(afterTable.getValueAt(i, 4)));
					FreevalFileParser.setIncidentDurationMaxWithIMAP(i-1, Math.max(returnFloat(afterTable.getValueAt(i, 5)), returnFloat(afterTable.getValueAt(i, 2))));
				}
				incidentData.dispose();
			}
		});
		buttonPanel.add(setValues);
		return buttonPanel;
	}

	/**
	 * Gets the incident table.
	 *
	 * @return the incident table
	 */
	private JPanel getIncidentTable()
	{
		JPanel incidentSevPanel = new JPanel();
		incidentSevPanel.setPreferredSize(new Dimension(900, 300));
		incidentSevPanel.setLayout(new BoxLayout(incidentSevPanel, BoxLayout.X_AXIS));
		initializeBeforeTable();
		initializeAfterTable();
		incidentSevPanel.add(beforeTable);
		incidentSevPanel.add(Box.createHorizontalStrut(20));
		incidentSevPanel.add(afterTable);
		return incidentSevPanel;

	}

	/**
	 * Initialize before table.
	 */
	private void initializeBeforeTable()
	{
		final String columnNames[] = new String[6];
		columnNames[0] = "Incident Severity";
		columnNames[1] = "Distribution %";
		columnNames[2] = "Mean Duration (min)";
		columnNames[3] = "Std. Dev.";
		columnNames[4] = "Min Duration (min)";
		columnNames[5] = "Max Duration (max)";

		Object[][] data = {
				{ "<html><b>" + columnNames[0] + "</b></html>", "<html><b>" + columnNames[1] + "</b></html>",
						"<html><b>" + columnNames[2] + "</b></html>", "<html><b>" + columnNames[3] + "</b></html>",
						"<html><b>" + columnNames[4] + "</b></html>", "<html><b>" + columnNames[5] + "</b></html>" },
				{ "<html>Shoulder Closure</html>" }, { "<html>One Lane Closure</html>" },
				{ "<html>Two Lane Closure</html>" }, { "<html>Three Lane Closure</html>" },
				{ "<html>Four Lane Closure</html>" }

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
				rendererComp.setBackground(new Color(200, 200, 200));
				if (row % 2 == 0)
				{
					rendererComp.setBackground(new Color(230, 230, 230));
				}
				if (row == 0)
				{
					rendererComp.setBackground(new Color(30, 30, 30));
					rendererComp.setForeground(Color.white);
				}
				return rendererComp;
			}
		};
		renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer.setVerticalAlignment(JLabel.TOP);

		DefaultTableModel model = new DefaultTableModel(data, columnNames)
		{
			public boolean isCellEditable(int row, int column)
			{
				if (column < 1 || row < 1)
				{
					return false;
				}
				return beforeTableEditing;
			}

			public void setValueAt(Object aValue, int row, int column)
			{
				super.setValueAt(aValue, row, column);
				if (afterTableEditing == false)
				{
					if (areaTypeVal == 0)
					{
						for (int i = 1; i < 5; i++)
						{
							float avgIncDur = returnFloat(getValueAt(i, 2));
							avgIncDur = (float) (avgIncDur * 0.77);
							afterTable.getModel().setValueAt(avgIncDur, i, 2);

							float stdDev = returnFloat(getValueAt(i, 3));
							stdDev = (float) (stdDev * 0.79);
							afterTable.getModel().setValueAt(stdDev, i, 3);
							float maxDur = (avgIncDur + 2 * stdDev);
							afterTable.getModel().setValueAt(Math.min(maxDur, 600), i, 5);

						}
					}
					else
					{
						for (int i = 1; i < 5; i++)
						{
							float avgIncDur = returnFloat(getValueAt(i, 2));
							avgIncDur = (float) (avgIncDur * 0.51);
							afterTable.getModel().setValueAt(avgIncDur, i, 2);

							float stdDev = returnFloat(getValueAt(i, 3));
							stdDev = (float) (stdDev * 0.64);
							afterTable.getModel().setValueAt(stdDev, i, 3);
							float maxDur = (avgIncDur + 2 * stdDev);
							afterTable.getModel().setValueAt(Math.min(maxDur, 600), i, 5);

						}
					}
				}
			}
		};

		beforeTable = new JTable(model)
		{
			public TableCellEditor getCellEditor(int row, int column)
			{
				return super.getCellEditor(row, column);
			}
		};
		beforeTable.setRowHeight(50);
		beforeTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		beforeTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		beforeTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
		beforeTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
		beforeTable.getColumnModel().getColumn(4).setCellRenderer(renderer);
		beforeTable.getColumnModel().getColumn(5).setCellRenderer(renderer);
		beforeTable.setRowSelectionAllowed(false);
		beforeTable.setCellSelectionEnabled(false);
		beforeTable.getTableHeader().setResizingAllowed(false);
		beforeTable.setDefaultRenderer(Object.class, renderer);
		beforeTable.setGridColor(new Color(255, 255, 255));
		beforeTable.setShowGrid(true);
		beforeTable.setTableHeader(null);
	}

	/**
	 * Initialize after table.
	 */
	private void initializeAfterTable()
	{
		final String columnNames[] = new String[6];
		columnNames[0] = "Incident Severity";
		columnNames[1] = "Distribution %";
		columnNames[2] = "Mean Duration (min)";
		columnNames[3] = "Std. Dev.";
		columnNames[4] = "Min Duration (min)";
		columnNames[5] = "Max Duration (max)";

		Object[][] data = {
				{ "<html><b>" + columnNames[0] + "</b></html>", "<html><b>" + columnNames[1] + "</b></html>",
						"<html><b>" + columnNames[2] + "</b></html>", "<html><b>" + columnNames[3] + "</b></html>",
						"<html><b>" + columnNames[4] + "</b></html>", "<html><b>" + columnNames[5] + "</b></html>" },
				{ "<html>Shoulder Closure</html>" }, { "<html>One Lane Closure</html>" },
				{ "<html>Two Lane Closure</html>" }, { "<html>Three Lane Closure</html>" },
				{ "<html>Four Lane Closure</html>" }

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
				rendererComp.setBackground(new Color(200, 200, 200));
				if (row % 2 == 0)
				{
					rendererComp.setBackground(new Color(230, 230, 230));
				}
				if (row == 0)
				{
					rendererComp.setBackground(new Color(30, 30, 30));
					rendererComp.setForeground(Color.white);
				}
				return rendererComp;
			}
		};
		renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer.setVerticalAlignment(JLabel.TOP);

		DefaultTableModel model = new DefaultTableModel(data, columnNames)
		{
			public boolean isCellEditable(int row, int column)
			{
				if (column < 1 || row < 1)
				{
					return false;
				}
				return afterTableEditing;
			}

			public void setValueAt(Object aValue, int row, int column)
			{
				super.setValueAt(aValue, row, column);
				if (beforeTableEditing == false)
				{
					if (areaTypeVal == 0)
					{
						for (int i = 1; i < 5; i++)
						{
							float avgIncDur = returnFloat(getValueAt(i, 2));
							avgIncDur = (float) (avgIncDur * 1.31);
							beforeTable.getModel().setValueAt(avgIncDur, i, 2);

							float stdDev = returnFloat(getValueAt(i, 3));
							stdDev = (float) (stdDev * 1.26);
							beforeTable.getModel().setValueAt(stdDev, i, 3);
							float maxDur = (avgIncDur + 2 * stdDev);
							beforeTable.getModel().setValueAt(Math.min(maxDur, 600), i, 5);
						}
					}
					else
					{
						for (int i = 1; i < 5; i++)
						{
							float avgIncDur = returnFloat(getValueAt(i, 2));
							avgIncDur = (float) (avgIncDur * 1.97);
							beforeTable.getModel().setValueAt(avgIncDur, i, 2);

							float stdDev = returnFloat(getValueAt(i, 3));
							stdDev = (float) (avgIncDur * 1.56);
							beforeTable.getModel().setValueAt(stdDev, i, 3);
							float maxDur = (avgIncDur + 2 * stdDev);
							beforeTable.getModel().setValueAt(Math.min(maxDur, 600), i, 5);

						}
					}
				}
			}
		};

		afterTable = new JTable(model)
		{
			public TableCellEditor getCellEditor(int row, int column)
			{
				return super.getCellEditor(row, column);
			}
		};
		afterTable.setRowHeight(50);
		afterTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		afterTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		afterTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
		afterTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
		afterTable.getColumnModel().getColumn(4).setCellRenderer(renderer);
		afterTable.getColumnModel().getColumn(5).setCellRenderer(renderer);
		afterTable.setRowSelectionAllowed(false);
		afterTable.setCellSelectionEnabled(false);
		afterTable.getTableHeader().setResizingAllowed(false);
		afterTable.setDefaultRenderer(Object.class, renderer);
		afterTable.setGridColor(new Color(255, 255, 255));
		afterTable.setShowGrid(true);
		afterTable.setTableHeader(null);
	}

	/**
	 * Gets the radio panel.
	 *
	 * @return the radio panel
	 */
	private JPanel getRadioPanel()
	{
		JPanel radioPanel = new JPanel();
		radioPanel.setPreferredSize(new Dimension(500, 50));
		radioPanel.setMaximumSize(new Dimension(500, 50));
		ButtonGroup group = new ButtonGroup();
		both.setSelected(true);
		group.add(both);
		group.add(before);
		group.add(after);

		radioPanel.add(both);
		radioPanel.add(before);
		radioPanel.add(after);

		after.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (after.isSelected())
				{
					beforeTableEditing = false;
					afterTableEditing = true;
					if (isStateWideVal == 1)
					{
						beforeTableEditing = true;
						afterTableEditing = true;
						both.setSelected(true);
					}
				}
			}

		});

		before.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (before.isSelected())
				{
					beforeTableEditing = true;
					afterTableEditing = false;
					if (isStateWideVal == 1)
					{
						beforeTableEditing = true;
						afterTableEditing = true;
						both.setSelected(true);
					}
				}
			}

		});

		both.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (both.isSelected())
				{
					beforeTableEditing = true;
					afterTableEditing = true;
				}
			}

		});

		return radioPanel;
	}

	/**
	 * Display incident table.
	 *
	 * @param areaType the area type
	 * @param isStateWide the is state wide
	 */
	public void displayIncidentTable(int areaType, int isStateWide)
	{
		areaTypeVal = areaType;
		isStateWideVal = isStateWide;
		if (isStateWideVal == 1)
		{
			setDefaultValuesStateWide();
		}
		incidentData.setVisible(true);
	}

	/**
	 * Sets the default values state wide.
	 */
	private void setDefaultValuesStateWide()
	{
		if (areaTypeVal == 0)
		{

			beforeTable.getModel().setValueAt(52.9, 1, 1);
			beforeTable.getModel().setValueAt(97.2, 1, 2);
			beforeTable.getModel().setValueAt(88.3, 1, 3);
			beforeTable.getModel().setValueAt(15.0, 1, 4);
			beforeTable.getModel().setValueAt(273.9, 1, 5);
			beforeTable.getModel().setValueAt(38.1, 2, 1);
			beforeTable.getModel().setValueAt(81.6, 2, 2);
			beforeTable.getModel().setValueAt(86.8, 2, 3);
			beforeTable.getModel().setValueAt(15.0, 2, 4);
			beforeTable.getModel().setValueAt(255.2, 2, 5);
			beforeTable.getModel().setValueAt(7.5, 3, 1);
			beforeTable.getModel().setValueAt(109.0, 3, 2);
			beforeTable.getModel().setValueAt(111.1, 3, 3);
			beforeTable.getModel().setValueAt(15.0, 3, 4);
			beforeTable.getModel().setValueAt(331.3, 3, 5);
			beforeTable.getModel().setValueAt(1.2, 4, 1);
			beforeTable.getModel().setValueAt(120.0, 4, 2);
			beforeTable.getModel().setValueAt(110.5, 4, 3);
			beforeTable.getModel().setValueAt(15.0, 4, 4);
			beforeTable.getModel().setValueAt(341.0, 4, 5);
			beforeTable.getModel().setValueAt(0.3, 5, 1);
			beforeTable.getModel().setValueAt(226.2, 5, 2);
			beforeTable.getModel().setValueAt(216.5, 5, 3);
			beforeTable.getModel().setValueAt(15.0, 5, 4);
			beforeTable.getModel().setValueAt(600.0, 5, 5);
			
			afterTable.getModel().setValueAt(52.9, 1, 1); // 52.9
			afterTable.getModel().setValueAt(74.2, 1, 2);
			afterTable.getModel().setValueAt(70.1, 1, 3);
			afterTable.getModel().setValueAt(15.0, 1, 4);
			afterTable.getModel().setValueAt(214.4, 1, 5);
			afterTable.getModel().setValueAt(38.1, 2, 1);
			afterTable.getModel().setValueAt(62.3, 2, 2);
			afterTable.getModel().setValueAt(68.9, 2, 3);
			afterTable.getModel().setValueAt(15.0, 2, 4);
			afterTable.getModel().setValueAt(200.2, 2, 5);
			afterTable.getModel().setValueAt(7.5, 3, 1);
			afterTable.getModel().setValueAt(83.2, 3, 2);
			afterTable.getModel().setValueAt(88.2, 3, 3);
			afterTable.getModel().setValueAt(15.0, 3, 4);
			afterTable.getModel().setValueAt(259.6, 3, 5);
			afterTable.getModel().setValueAt(1.2, 4, 1);
			afterTable.getModel().setValueAt(91.6, 4, 2);
			afterTable.getModel().setValueAt(87.7, 4, 3);
			afterTable.getModel().setValueAt(15.0, 4, 4);
			afterTable.getModel().setValueAt(267.0, 4, 5);
			afterTable.getModel().setValueAt(0.3, 5, 1);
			afterTable.getModel().setValueAt(172.7, 5, 2);
			afterTable.getModel().setValueAt(171.8, 5, 3);
			afterTable.getModel().setValueAt(15.0, 5, 4);
			afterTable.getModel().setValueAt(516.3, 5, 5);

		}
		else
		{
			beforeTable.getModel().setValueAt(37.4, 1, 1);
			beforeTable.getModel().setValueAt(177.1, 1, 2);
			beforeTable.getModel().setValueAt(150.1, 1, 3);
			beforeTable.getModel().setValueAt(15.0, 1, 4);
			beforeTable.getModel().setValueAt(477.2, 1, 5);
			beforeTable.getModel().setValueAt(52.5, 2, 1);
			beforeTable.getModel().setValueAt(180.8, 2, 2);
			beforeTable.getModel().setValueAt(181.6, 2, 3);
			beforeTable.getModel().setValueAt(15.0, 2, 4);
			beforeTable.getModel().setValueAt(544.0, 2, 5);
			beforeTable.getModel().setValueAt(8.8, 3, 1);
			beforeTable.getModel().setValueAt(201.5, 3, 2);
			beforeTable.getModel().setValueAt(181.4, 3, 3);
			beforeTable.getModel().setValueAt(15.0, 3, 4);
			beforeTable.getModel().setValueAt(564.4, 3, 5);
			beforeTable.getModel().setValueAt(1.4, 4, 1);
			beforeTable.getModel().setValueAt(279.7, 4, 2);
			beforeTable.getModel().setValueAt(239.3, 4, 3);
			beforeTable.getModel().setValueAt(15.0, 4, 4);
			beforeTable.getModel().setValueAt(600.0, 4, 5);
			beforeTable.getModel().setValueAt(0.0, 5, 1);
			beforeTable.getModel().setValueAt(279.7, 5, 2);
			beforeTable.getModel().setValueAt(239.9, 5, 3);
			beforeTable.getModel().setValueAt(15.0, 5, 4);
			beforeTable.getModel().setValueAt(600.0, 5, 5);
			
			afterTable.getModel().setValueAt(37.4, 1, 1);
			afterTable.getModel().setValueAt(89.9, 1, 2);
			afterTable.getModel().setValueAt(96.2, 1, 3);
			afterTable.getModel().setValueAt(15.0, 1, 4);
			afterTable.getModel().setValueAt(214.4, 1, 5);
			afterTable.getModel().setValueAt(52.5, 2, 1);
			afterTable.getModel().setValueAt(91.8, 2, 2);
			afterTable.getModel().setValueAt(116.4, 2, 3);
			afterTable.getModel().setValueAt(15.0, 2, 4);
			afterTable.getModel().setValueAt(200.2, 2, 5);
			afterTable.getModel().setValueAt(8.8, 3, 1);
			afterTable.getModel().setValueAt(102.3, 3, 2);
			afterTable.getModel().setValueAt(116.3, 3, 3);
			afterTable.getModel().setValueAt(15.0, 3, 4);
			afterTable.getModel().setValueAt(259.6, 3, 5);
			afterTable.getModel().setValueAt(1.4, 4, 1);
			afterTable.getModel().setValueAt(142.0, 4, 2);
			afterTable.getModel().setValueAt(153.4, 4, 3);
			afterTable.getModel().setValueAt(15.0, 4, 4);
			afterTable.getModel().setValueAt(267.0, 4, 5);
			afterTable.getModel().setValueAt(0.0, 5, 1);
			afterTable.getModel().setValueAt(142.0, 5, 2);
			afterTable.getModel().setValueAt(153.4, 5, 3);
			afterTable.getModel().setValueAt(15.0, 5, 4);
			afterTable.getModel().setValueAt(516.3, 5, 5);
		}
	}

	/**
	 * Return float.
	 *
	 * @param val the val
	 * @return the float
	 */
	public float returnFloat(Object val)
	{
		float value = 0;
		if (val instanceof Float)
		{
			value = (float) val;

		}
		else if (val instanceof String)
		{
			if (((String) val).trim().length() == 0)
			{
				value = 0;
			}
			else
			{
				value = Float.parseFloat((String) val);
			}
		}
		else if (val instanceof Integer)
		{
			value = (int) val;
		}
		else if (val instanceof Double)
		{
			value = (float) (double) val;
		}
		return value;
	}
}
