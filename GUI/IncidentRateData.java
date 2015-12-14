
package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class IncidentRateData extends JDialog
{
	private IncidentRateData	incidentData;
	private JRadioButton		both	= new JRadioButton("Both");
	private JRadioButton		before	= new JRadioButton("Only Before");
	private JRadioButton		after	= new JRadioButton("Only After");
	private JTable				incidentTable;

	public IncidentRateData()
	{
		super(Main.getMainFrame());
		incidentData = this;
		setTitle("INCIDENT RATE");
		setModal(true);
		initComponents();
	}

	private void initComponents()
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		setPreferredSize(new Dimension(500, 430));
		setLocation(screenSize.width / 2 - 250, screenSize.height / 2 - 250);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(getRadioPanel());
		getContentPane().add(getIncidentTable());
		getContentPane().add(getButtonPanel());
		pack();
	}

	private JPanel getButtonPanel()
	{
		JPanel buttonPanel = new JPanel();
		JButton setValues = new JButton("Continue");
		setValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				incidentData.dispose();
			}
		});
		buttonPanel.add(setValues);
		return buttonPanel;
	}

	private JPanel getIncidentTable()
	{
		JPanel incidentPanel = new JPanel();
		incidentPanel.setPreferredSize(new Dimension(500, 400));
		incidentPanel.setLayout(new BoxLayout(incidentPanel, BoxLayout.Y_AXIS));
		final String columnNames[] = new String[3];
		columnNames[0] = "MONTH";
		columnNames[1] = "BEFORE";
		columnNames[2] = "AFTER";

		Object[][] data = {
				{ "<html><b>" + columnNames[0] + "</b></html>", "<html><b>" + columnNames[1] + "</b></html>",
						"<html><b>" + columnNames[2] + "</b></html>" },
				{ "<html><b>" + "JAN" + "</b></html>" }, { "<html><b>" + "FEB" + "</b></html>" },
				{ "<html><b>" + "MAR" + "</b></html>" }, { "<html><b>" + "APR" + "</b></html>" },
				{ "<html><b>" + "MAY" + "</b></html>" }, { "<html><b>" + "JUN" + "</b></html>" },
				{ "<html><b>" + "JUL" + "</b></html>" }, { "<html><b>" + "AUG" + "</b></html>" },
				{ "<html><b>" + "SEP" + "</b></html>" }, { "<html><b>" + "OCT" + "</b></html>" },
				{ "<html><b>" + "NOV" + "</b></html>" }, { "<html><b>" + "DEC" + "</b></html>" },

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

		incidentTable = new JTable(model)
		{
			public TableCellEditor getCellEditor(int row, int column)
			{
				return super.getCellEditor(row, column);
			}
		};
		incidentTable.setRowHeight(25);
		incidentTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		incidentTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		incidentTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
		incidentTable.getColumnModel().getColumn(0).setMinWidth(150);
		incidentTable.getColumnModel().getColumn(0).setMaxWidth(150);
		incidentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		incidentTable.getColumnModel().getColumn(1).setMinWidth(150);
		incidentTable.getColumnModel().getColumn(1).setMaxWidth(150);
		incidentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		incidentTable.getColumnModel().getColumn(2).setMinWidth(150);
		incidentTable.getColumnModel().getColumn(2).setMaxWidth(150);
		incidentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		incidentTable.setRowSelectionAllowed(false);
		incidentTable.setCellSelectionEnabled(false);
		incidentTable.getTableHeader().setResizingAllowed(false);
		incidentTable.setDefaultRenderer(Object.class, renderer);
		incidentTable.setGridColor(new Color(255, 255, 255));
		incidentTable.setShowGrid(true);
		incidentTable.setTableHeader(null);
		incidentPanel.add(incidentTable);
		return incidentPanel;

	}

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
					incidentTable.getColumnModel().getColumn(0).setMinWidth(225);
					incidentTable.getColumnModel().getColumn(0).setMaxWidth(225);
					incidentTable.getColumnModel().getColumn(0).setPreferredWidth(225);
					incidentTable.getColumnModel().getColumn(1).setMinWidth(0);
					incidentTable.getColumnModel().getColumn(1).setMaxWidth(0);
					incidentTable.getColumnModel().getColumn(1).setPreferredWidth(0);
					incidentTable.getColumnModel().getColumn(2).setMinWidth(225);
					incidentTable.getColumnModel().getColumn(2).setMaxWidth(225);
					incidentTable.getColumnModel().getColumn(2).setPreferredWidth(225);

					incidentTable.revalidate();
					incidentTable.repaint();
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
					incidentTable.getColumnModel().getColumn(0).setMinWidth(225);
					incidentTable.getColumnModel().getColumn(0).setMaxWidth(225);
					incidentTable.getColumnModel().getColumn(0).setPreferredWidth(225);
					incidentTable.getColumnModel().getColumn(2).setMinWidth(0);
					incidentTable.getColumnModel().getColumn(2).setMaxWidth(0);
					incidentTable.getColumnModel().getColumn(2).setPreferredWidth(0);
					incidentTable.getColumnModel().getColumn(1).setMinWidth(225);
					incidentTable.getColumnModel().getColumn(1).setMaxWidth(225);
					incidentTable.getColumnModel().getColumn(1).setPreferredWidth(225);

					incidentTable.revalidate();
					incidentTable.repaint();
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
					incidentTable.getColumnModel().getColumn(0).setMinWidth(150);
					incidentTable.getColumnModel().getColumn(0).setMaxWidth(150);
					incidentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					incidentTable.getColumnModel().getColumn(1).setMinWidth(150);
					incidentTable.getColumnModel().getColumn(1).setMaxWidth(150);
					incidentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
					incidentTable.getColumnModel().getColumn(2).setMinWidth(150);
					incidentTable.getColumnModel().getColumn(2).setMaxWidth(150);
					incidentTable.getColumnModel().getColumn(2).setPreferredWidth(150);

					incidentTable.revalidate();
					incidentTable.repaint();
				}
			}

		});

		return radioPanel;
	}

	public void displayIncidentTable()
	{
		incidentData.setVisible(true);
	}
}
