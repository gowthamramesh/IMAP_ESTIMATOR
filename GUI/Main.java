
package GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class Main extends JFrame
{
	private static Main		mainFrame;
	private static JPanel	basePanel;
	private static JPanel	mainPanel;
	private static JPanel	inforPanel;
	private static JPanel	estimationPanel;
	private static JPanel	benefitPanel;
	private static JLabel	myLabel;
	private static int		currentPanelID	= 0;
	private static JPanel	contentPanel;
	private static String	title			= "IMAP COST/BENEFIT ESTIMATOR";

	public Main()
	{
		this.setTitle("IMAP Benefit/Cost Estimation tool");
		initComponents();
	}

	private void initComponents()
	{
		mainPanel = SetupPanel.getMainPanel();
		inforPanel = InformationScreen.getInforPanel();
		estimationPanel = EstimationScreen.getEstimationPanel();
		// benefitPanel = CostBenefitEstimate.getEstimationBenefitPanel();

		basePanel = new JPanel();
		contentPanel = new JPanel();
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		myLabel = new JLabel();
		Font font = myLabel.getFont();
		myLabel.setText(title);
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, 20);
		myLabel.setFont(boldFont);
		contentPanel.add(mainPanel);
		JPanel labelPanel = new JPanel();
		labelPanel.add(myLabel);
		basePanel.add(Box.createVerticalStrut(20));
		basePanel.add(labelPanel);
		basePanel.add(contentPanel);
		basePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(basePanel);
		setPreferredSize(new Dimension(800, 500));
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		setLocation(screenSize.width / 2 - 400, screenSize.height / 2 - 250);

		pack();

	}

	public static void main(String args[])
	{
		showMainForm();
	}

	private static void showMainForm()
	{
		// Create mainFrame in EDT thread
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					mainFrame = new Main();
					mainFrame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		});
	}

	public static void changePanel(int id)
	{
		currentPanelID = id;
		contentPanel.removeAll();
		switch (id)
		{
			case 0:
				contentPanel.add(mainPanel);
				break;
			case 1:
				contentPanel.add(inforPanel);
				break;
			case 2:
				contentPanel.add(estimationPanel);
				break;
			case 3:
				benefitPanel = CostBenefitEstimate.getEstimationBenefitPanel();
				contentPanel.add(benefitPanel);
				break;
		}
		basePanel.revalidate();
		basePanel.repaint();
	}

	public static JFrame getMainFrame()
	{
		return mainFrame;
	}

}
