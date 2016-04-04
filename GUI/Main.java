/*
 * IMAP ESTIMATION TOOL
 * 
 * @author: Gowtham Ramesh & Joseph L Trask
 * 
 * @date : 01/18/16
 * 
 * @version: 1.0
 */

package GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main extends JFrame
{

	/** The main frame. */
	private static Main mainFrame;

	/** The base panel. */
	private static JPanel basePanel;

	/** The main panel. */
	private static JPanel mainPanel;

	/** The infor panel. */
	private static JPanel inforPanel;

	/** The estimation panel. */
	private static JPanel estimationPanel;

	/** The benefit panel. */
	private static JPanel benefitPanel;

	/** The my label. */
	private static JLabel myLabel;

	/** The current panel id. */
	private static int currentPanelID = 0;

	/** The content panel. */
	private static JPanel contentPanel;

	/** The title. */
	private static String title = "IMAP COST/BENEFIT ESTIMATOR";

	/**
	 * Instantiates a new main.
	 */
	public Main()
	{
		this.setTitle("IMAP Benefit/Cost Estimation tool");
		//List<Image> icons = new ArrayList<>();
		//icons.add(new ImageIcon(getClass().getResource("ncdot.png")).getImage());
		//this.setIconImages(icons);
		//this.setIconImage(new ImageIcon(getClass().getResource("ncdot.png")).getImage());
		initComponents();
	}

	/**
	 * Inits the components.
	 */
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
		basePanel.add(Box.createVerticalStrut(10));
		basePanel.add(labelPanel);
		basePanel.add(contentPanel);
		basePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(basePanel);
		setPreferredSize(new Dimension(800, 450));
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		setLocation(screenSize.width / 2 - 400, screenSize.height / 2 - 250);

		pack();

	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[])
	{
		showMainForm();
	}

	/**
	 * Show main form.
	 */
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

	/**
	 * Change panel.
	 *
	 * @param id
	 *            the id
	 */
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

	/**
	 * Gets the main frame.
	 *
	 * @return the main frame
	 */
	public static JFrame getMainFrame()
	{
		return mainFrame;
	}

	/**
	 * Return float.
	 *
	 * @param val
	 *            the val
	 * @return the float
	 */
	public static float returnFloat(Object val)
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
