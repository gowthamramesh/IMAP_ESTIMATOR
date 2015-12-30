
package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CrashRateData extends JDialog
{
	private CrashRateData	crashData;
	private JTextField		crashRatioField	= new JTextField("1.7");
	private JTextField		crashRateField	= new JTextField("100");

	public CrashRateData()
	{
		super(Main.getMainFrame());
		crashData = this;
		setTitle("CRASH RATE DATA");
		setModal(true);
		initComponents();
	}

	private void initComponents()
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		setPreferredSize(new Dimension(500, 200));
		setLocation(screenSize.width / 2 - 250, screenSize.height / 2 - 100);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(Box.createVerticalStrut(20));
		getContentPane().add(getCrashPanel());
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
				// Assigning crash rate data to FreevalFileParser
				FreevalFileParser.setIncidentRatesUsed(false);
				FreevalFileParser.setCrashRateRatioNoIMAP(Float.parseFloat(crashRatioField.getText()));
				FreevalFileParser.setCrashRateRatioWithIMAP(Float.parseFloat(crashRatioField.getText()));
				float[] crashRateArray = new float[12];
				Arrays.fill(crashRateArray, Float.parseFloat(crashRateField.getText()));
				FreevalFileParser.setCrashRateFrequenciesNoIMAP(crashRateArray);
				FreevalFileParser.setCrashRateFrequenciesWithIMAP(crashRateArray);
				crashData.dispose();
			}
		});
		buttonPanel.add(setValues);
		return buttonPanel;
	}

	private JPanel getCrashPanel()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel setupPanel = new JPanel();
		setupPanel.setLayout(new GridBagLayout());
		setupPanel.setBorder(BorderFactory.createTitledBorder("Crash Information"));
		setupPanel.setMaximumSize(new Dimension(800, 400));

		c.gridx = 0;
		c.gridy = 0;
		JLabel fileNameLabel = new JLabel("Incident/crash ratio (insert 1.7-4");
		setupPanel.add(fileNameLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 2;
		c.gridy = 0;
		crashRatioField.setColumns(100);
		crashRatioField.setMinimumSize(new Dimension(200, 20));
		setupPanel.add(crashRatioField);

		c.gridx = 0;
		c.gridy = 1;
		setupPanel.add(Box.createVerticalStrut(10), c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel facilityTypeLabel = new JLabel("Crash rate (crashes per 100 M VMT)");
		setupPanel.add(facilityTypeLabel, c);

		c.gridx = 1;
		c.gridy = 2;
		setupPanel.add(Box.createHorizontalStrut(20), c);

		c.gridx = 2;
		c.gridy = 2;
		crashRateField.setColumns(100);
		crashRateField.setMinimumSize(new Dimension(200, 20));
		setupPanel.add(crashRateField, c);

		c.gridx = 0;
		c.gridy = 3;
		setupPanel.add(Box.createVerticalStrut(10), c);

		return setupPanel;
	}

	public void displayCrashRateData(float defCrashRatio, int defaultCrashRate)
	{
		if (defaultCrashRate != -1)
		{
			crashRateField.setText(Integer.toString(defaultCrashRate));
		}
		if (defCrashRatio != -1)
		{
			crashRatioField.setText(Float.toString(defCrashRatio));
		}
		crashData.setVisible(true);
	}
}
