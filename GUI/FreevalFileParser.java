/*
 * 
 */

package GUI;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import coreEngine.Seed;
import coreEngine.Helper.CEConst;
import coreEngine.Helper.CEDate;
import coreEngine.Helper.ASCIIAdapter.ASCIISeedFileAdapter_GPMLFormat;
import coreEngine.Helper.ASCIIAdapter.ASCIISeedFileAdapter_RLFormat;
import coreEngine.reliabilityAnalysis.ScenarioGenerator;
import coreEngine.reliabilityAnalysis.DataStruct.DemandData;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentData;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentEvent;
import coreEngine.reliabilityAnalysis.DataStruct.Scenario;
import coreEngine.reliabilityAnalysis.DataStruct.WeatherEvent;
import coreEngine.reliabilityAnalysis.DataStruct.WorkZone;

// TODO: Auto-generated Javadoc
/**
 * The Class FreevalFileParser.
 */
public class FreevalFileParser
{

	/** The seed1. */
	/*
	 * Seed to hold Reliability run without IMAP
	 */
	private static Seed seed1;

	/** The seed2. */
	/*
	 * Seed to hold Reliability run with IMAP
	 */
	private static Seed seed2; // With Imap
	
	/**
	 * Year in which the IMAP analysis is to take place
	 */
	private static int year = 2016;

	/** The truck percentage. */
	/*
	 * Default Truck Percentage
	 */
	private static int truckPercentage = 5;

	/** The crash rate ratio no imap. */
	/*
	 * Crash Rate Ratio without IMAP
	 */
	private static float crashRateRatioNoIMAP = 4.9f;

	/** The crash rate frequencies no imap. */
	/*
	 * Crash Rate Frequencies without IMAP
	 */
	private static float[] crashRateFrequenciesNoIMAP = new float[12];

	/** The crash rate ratio with imap. */
	/*
	 * Crash Rate Ratio with IMAP
	 */
	private static float crashRateRatioWithIMAP = 4.9f;

	/** The crash rate frequencies with imap. */
	/*
	 * Crash Rate Frequencies with IMAP
	 */
	private static float[] crashRateFrequenciesWithIMAP = new float[12];

	/** The incident rates no imap. */
	/*
	 * Incident Frequencies without IMAP
	 */
	private static float[] incidentRatesNoIMAP = new float[12];

	/** The incident rates with imap. */
	/*
	 * Incident Frequencies with IMAP
	 */
	private static float[] incidentRatesWithIMAP = new float[12];

	/** The rng seed. */
	/*
	 * Random Number Generator Seed
	 */
	private static int rngSeed = -1;

	/** The incident rates used. */
	/*
	 * Identifier to specify if incident rates (as opposed) to crash rates are
	 * used
	 */
	private static boolean incidentRatesUsed = false;

	/**
	 * Array holding duration information for the reliability without IMAP.
	 * First index is incident type (0-Shoulder, 1-1 Lane Closure, 2-2 Lane
	 * Closure, 3-3 Lane Closure, 4-4+ Lane Closure Second index is
	 * 0-distribution %, 1-Mean duration, 2-Std Deviation of duration, 3-Minimum
	 * Duration, 4-Maximum Duration
	 */
	private static float[][] durationInfoNoIMAP = new float[5][5];

	/**
	 * Array holding duration information for the reliability with IMAP. First
	 * index is incident type (0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure,
	 * 3-3 Lane Closure, 4-4+ Lane Closure) Second index is 0-distribution %,
	 * 1-Mean duration, 2-Std Deviation of duration, 3-Minimum Duration,
	 * 4-Maximum Duration
	 */
	private static float[][] durationInfoWithIMAP = new float[5][5];

	/**
	 * Array indicating which days are active. 0 - Monday, 1 - Tuesday, 2 -
	 * Wednesday, 3 - Thursday, 4 - Friday, 5 - Saturday, 6 - Sunday
	 */
	private static boolean[] activeDays = new boolean[7];
	
	/**
	 * Boolean value indicating whether or not holidays are included the reliability analysis.
	 * Default value is false.
	 */
	private static boolean includeHolidays = false;
	
	private static final DecimalFormat formatter2 = new DecimalFormat("#,###.00");

	/**
	 * Method to open the seed facility from a ASCII (.txt) file.
	 *
	 * @param file
	 *            File location (ASCII) of the seed facility.
	 * @return true, if successful
	 */
	private static boolean setSeedFacilityFileASCII(File file)
	{

		boolean success = false;
		try
		{
			Scanner input = new Scanner(file);
			String firstLine = input.nextLine();
			input.close();
			// choose correct ASCII adapter based on ASCII input file format
			if (firstLine.startsWith("<"))
			{
				ASCIISeedFileAdapter_GPMLFormat textSeed = new ASCIISeedFileAdapter_GPMLFormat();
				seed1 = textSeed.importFromASCII(file.getAbsolutePath());
				seed2 = textSeed.importFromASCII(file.getAbsolutePath());
				if (seed1 != null && seed2 != null)
				{
					seed1.setValue(CEConst.IDS_SEED_FILE_NAME, null);
					seed2.setValue(CEConst.IDS_SEED_FILE_NAME, null);
					success = true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				ASCIISeedFileAdapter_RLFormat textSeed = new ASCIISeedFileAdapter_RLFormat();
				seed1 = textSeed.importFromFile(file.getAbsolutePath());
				seed2 = textSeed.importFromFile(file.getAbsolutePath());
				if (seed1 != null && seed2 != null)
				{
					seed1.setValue(CEConst.IDS_SEED_FILE_NAME, null);
					seed2.setValue(CEConst.IDS_SEED_FILE_NAME, null);
					success = true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Something went wrong opening the seed file.",
					"Error: Seed File Invalid", JOptionPane.ERROR_MESSAGE);
		}
		return success;

	}

	/**
	 * Method to open the seed facility from a SEED (.seed) file.
	 *
	 * @param file
	 *            File location (SEED) of the seed facility.
	 * @return true, if successful
	 */
	private static boolean setSeedFacilityFileSEED(File file)
	{

		boolean success = false;
		String openFileName = file.getAbsolutePath();

		// Open seed from file
		try
		{
			FileInputStream fis = new FileInputStream(openFileName);
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gzis);
			FileInputStream fis1 = new FileInputStream(openFileName);
			GZIPInputStream gzis1 = new GZIPInputStream(fis1);
			ObjectInputStream ois1 = new ObjectInputStream(gzis1);
			seed1 = (Seed) ois.readObject();
			seed2 = (Seed) ois1.readObject();
			seed1.resetSeedToInputOnly();
			seed2.resetSeedToInputOnly();
			ois.close();
			ois1.close();
			seed1.setValue(CEConst.IDS_SEED_FILE_NAME, openFileName);
			seed2.setValue(CEConst.IDS_SEED_FILE_NAME,
					openFileName.substring(0, openFileName.lastIndexOf(".")) + "_IMAP" + ".seed");
			success = true;
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Something went wrong opening the seed file.",
					"Error: Seed File Invalid", JOptionPane.ERROR_MESSAGE);
		}

		return success;

	}

	/**
	 * Method to launch a file chooser to specify the seed facility.
	 *
	 * @return true, if successful
	 */
	public static boolean selectSeedFile()
	{
		JFileChooser fc = new JFileChooser(); // TODO Specify initial directory
		fc.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return f.isDirectory() || f.getName().endsWith(".seed") || f.getName().endsWith(".txt");
			}

			@Override
			public String getDescription()
			{
				return "SEED or ASCII Facility Files";
			}
		});

		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (fc.getSelectedFile().getName().endsWith(".txt"))
			{
				return setSeedFacilityFileASCII(fc.getSelectedFile());
			}
			else
			{
				return setSeedFacilityFileSEED(fc.getSelectedFile());
			}
		}
		else
		{
			return false; // No file chosen
		}
	}

	/**
	 * Getter for crash rate ratio without IMAP.
	 *
	 * @return the crash rate ratio no imap
	 */
	public static float getCrashRateRatioNoIMAP()
	{
		return crashRateRatioNoIMAP;
	}

	/**
	 * Setter for crash rate ratio without IMAP.
	 * 
	 * @param crashRateRatio
	 *            new crash rate ratio value (national average is 4.9)
	 */
	public static void setCrashRateRatioNoIMAP(float crashRateRatio)
	{
		FreevalFileParser.crashRateRatioNoIMAP = crashRateRatio;
	}

	/**
	 * Getter for crash rate ratio with IMAP.
	 *
	 * @return the crash rate ratio with imap
	 */
	public static float getCrashRateRatioWithIMAP()
	{
		return crashRateRatioWithIMAP;
	}

	/**
	 * Setter for crash rate ratio with IMAP.
	 * 
	 * @param crashRateRatio
	 *            new crash rate ratio value (national average is 4.9)
	 */
	public static void setCrashRateRatioWithIMAP(float crashRateRatio)
	{
		FreevalFileParser.crashRateRatioWithIMAP = crashRateRatio;
	}

	/**
	 * Getter for monthly crash rate frequencies array without IMAP.
	 *
	 * @return the crash rate frequencies no imap
	 */
	public static float[] getCrashRateFrequenciesNoIMAP()
	{
		return crashRateFrequenciesNoIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array without IMAP.
	 *
	 * @param crashRateFrequencies
	 *            Array of monthly crash rate frequencies.
	 */
	public static void setCrashRateFrequenciesNoIMAP(float[] crashRateFrequencies)
	{
		FreevalFileParser.crashRateFrequenciesNoIMAP = crashRateFrequencies;
	}

	/**
	 * Getter for monthly crash rate frequencies array with IMAP.
	 *
	 * @return the crash rate frequencies with imap
	 */
	public static float[] getCrashRateFrequenciesWithIMAP()
	{
		return crashRateFrequenciesWithIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array with IMAP.
	 *
	 * @param crashRateFrequencies
	 *            Array of monthly crash rate frequencies.
	 */
	public static void setCrashRateFrequenciesWithIMAP(float[] crashRateFrequencies)
	{
		FreevalFileParser.crashRateFrequenciesWithIMAP = crashRateFrequencies;
	}

	/**
	 * Getter for monthly crash rate frequencies array without IMAP.
	 *
	 * @return the incident frequencies no imap
	 */
	public static float[] getIncidentFrequenciesNoIMAP()
	{
		return incidentRatesNoIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array without IMAP.
	 *
	 * @param incidentFrequenciesNoIMAP
	 *            the new incident frequencies no imap
	 */
	public static void setIncidentFrequenciesNoIMAP(float[] incidentFrequenciesNoIMAP)
	{
		FreevalFileParser.incidentRatesNoIMAP = incidentFrequenciesNoIMAP;
	}

	/**
	 * Getter for monthly crash rate frequencies array with IMAP.
	 *
	 * @return the incident frequencies with imap
	 */
	public static float[] getIncidentFrequenciesWithIMAP()
	{
		return incidentRatesWithIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array with IMAP.
	 *
	 * @param incidentFrequenciesWithIMAP
	 *            the new incident frequencies with imap
	 */
	public static void setIncidentFrequenciesWithIMAP(float[] incidentFrequenciesWithIMAP)
	{
		FreevalFileParser.incidentRatesWithIMAP = incidentFrequenciesWithIMAP;
	}

	/**
	 * Getter for whether or not incident rates are being used.
	 *
	 * @return the incident rates used
	 */
	public static boolean getIncidentRatesUsed()
	{
		return incidentRatesUsed;
	}

	/**
	 * Setter for boolean value indicating whether or not incident rates (as
	 * opposed to crash rates) are being used.
	 *
	 * @param val
	 *            the new incident rates used
	 */
	public static void setIncidentRatesUsed(boolean val)
	{
		FreevalFileParser.incidentRatesUsed = val;
	}

	/**
	 * Getter for the random number generator seed.
	 *
	 * @return the rng seed
	 */
	public static int getRngSeed()
	{
		return rngSeed;
	}

	/**
	 * Setter for the random number generator seed value.
	 * 
	 * @param rngSeed
	 *            New random number generator seed.
	 */
	public static void setRngSeed(int rngSeed)
	{
		FreevalFileParser.rngSeed = rngSeed;
	}

	/**
	 * Getter for facility truck percentage.
	 *
	 * @return the truck percentage
	 */
	public static float getTruckPercentage()
	{
		// TODO Auto-generated method stub
		return truckPercentage;
	}

	/**
	 * Setter for facility truck percentage.
	 * 
	 * @param truckPercentage
	 *            Percentage (0 - 100) to be assigned to the facility.
	 */
	public static void setTruckPercentage(int truckPercentage)
	{
		FreevalFileParser.truckPercentage = truckPercentage;
	}

	/**
	 * Setter for active days array. 0 - Monday, 1 - Tuesday, 2 - Wednesday, 3 -
	 * Thursday, 4 - Friday, 5 - Saturday, 6 - Sunday
	 * 
	 * @param activeDays
	 *            Boolean array indicating if a day is active.
	 */
	public static void setActiveDays(boolean[] activeDays)
	{
		FreevalFileParser.activeDays = activeDays.clone();
	}

	/**
	 * Individual setter for active days array. 0 - Monday, 1 - Tuesday, 2 -
	 * Wednesday, 3 - Thursday, 4 - Friday, 5 - Saturday, 6 - Sunday
	 * 
	 * @param dayIdx
	 *            Index of day.
	 * @param isActive
	 *            Boolean value to mark whether or not it is active.
	 */
	public static void setActiveDay(int dayIdx, boolean isActive)
	{
		FreevalFileParser.activeDays[dayIdx] = isActive;
	}
	
	/**
	 * Setter to indicate whether or not holidays should be included.
	 * @param includeHolidays True if holidays should be included, false otherwise
	 */
	public static void setHolidaysIncluded(boolean includeHolidays) {
		FreevalFileParser.includeHolidays = includeHolidays;
	}

	/**
	 * Setter for incident distribution value (0-100) without IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationDistributionNoIMAP(int incidentType, float value)
	{
		durationInfoNoIMAP[incidentType][0] = value;
	}

	/**
	 * Setter for mean incident duration without IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationMeanNoIMAP(int incidentType, float value)
	{
		durationInfoNoIMAP[incidentType][1] = value;
	}

	/**
	 * Setter for the standard deviation of the incident duration without IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationStdDevNoIMAP(int incidentType, float value)
	{
		durationInfoNoIMAP[incidentType][2] = value;
	}

	/**
	 * Setter for the minimum incident duration without IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationMinNoIMAP(int incidentType, float value)
	{
		durationInfoNoIMAP[incidentType][3] = value;
	}

	/**
	 * Setter for the maximum incident duration without IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationMaxNoIMAP(int incidentType, float value)
	{
		durationInfoNoIMAP[incidentType][4] = value;
	}

	/**
	 * Setter for incident distribution value (0-100) with IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationDistributionWithIMAP(int incidentType, float value)
	{
		durationInfoWithIMAP[incidentType][0] = value;
	}

	/**
	 * Setter for mean incident duration with IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationMeanWithIMAP(int incidentType, float value)
	{
		durationInfoWithIMAP[incidentType][1] = value;
	}

	/**
	 * Setter for the standard deviation of the incident duration with IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationStdDevWithIMAP(int incidentType, float value)
	{
		durationInfoWithIMAP[incidentType][2] = value;
	}

	/**
	 * Setter for the minimum incident duration with IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationMinWithIMAP(int incidentType, float value)
	{
		durationInfoWithIMAP[incidentType][3] = value;
	}

	/**
	 * Setter for the maximum incident duration without IMAP.
	 *
	 * @param incidentType
	 *            0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane
	 *            Closure, 4-4+ Lane Closure
	 * @param value
	 *            the value
	 */
	public static void setIncidentDurationMaxWithIMAP(int incidentType, float value)
	{
		durationInfoWithIMAP[incidentType][4] = value;
	}

	/**
	 * Getter for total vehicle hours delay (VHD) without IMAP.
	 *
	 * @return the total veh delay without imap
	 */
	public static float getTotalVehDelayWithoutImap()
	{

		float total_VHD = 0;
		for (int scen = 0; scen < seed1.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_VHD+= seed1.getValueFloat(CEConst.IDS_SP_VHD, 0, 0, scen+1, -1);
		}
		return total_VHD;
	}

	/**
	 * Getter for total vehicle hours delay (VHD) with IMAP.
	 *
	 * @return the total veh delay with imap
	 */
	public static float getTotalVehDelayWithImap()
	{
		float total_VHD = 0;
		for (int scen = 0; scen < seed2.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_VHD+= seed2.getValueFloat(CEConst.IDS_SP_VHD, 0, 0, scen+1, -1);
		}
		return total_VHD;
	}

	/**
	 * Getter for a particular total (summed) output for a reliability analysis.
	 *
	 * @param seed
	 *            Seed Facility to extract the output from.
	 * @param identifier
	 *            String identifier (from CEConst) specifying the output.
	 * @return the total output from seed
	 */
	private static float getTotalOutputFromSeed(Seed seed, String identifier)
	{
		float outputVal = 0.0f;
		for (int scen = 0; scen < seed.getValueInt(CEConst.IDS_NUM_SCEN); scen++)
		{
			for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++)
			{
				for (int per = 0; per < seed.getValueInt(CEConst.IDS_NUM_PERIOD); per++)
				{
					outputVal += seed.getValueFloat(identifier, seg, per, scen+1, -1);
				}
			}
		}
		return outputVal;
	}
	
	
	/**
	 * Getter for a particular average output across all reliability scenarios for a reliability analysis.
	 *
	 * @param seed
	 *            Seed Facility to extract the output from.
	 * @param identifier
	 *            String identifier (from CEConst) specifying the output.
	 * @return the total output from seed
	 */
	private static float getAverageRLOutputFromSeed(Seed seed, String identifier)
	{
		float outputVal = 0.0f;
		for (int scen = 0; scen < seed.getValueInt(CEConst.IDS_NUM_SCEN); scen++)
		{
			for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++)
			{
				for (int per = 0; per < seed.getValueInt(CEConst.IDS_NUM_PERIOD); per++)
				{
					outputVal += seed.getValueFloat(identifier, seg, per, scen+1, -1);
				}
			}
		}
		return outputVal/seed.getValueInt(CEConst.IDS_NUM_SCEN);
	}

	/**
	 * Getter for total VMTD without IMAP.
	 *
	 * @return the total demand vmt without
	 */
	public static float getTotalDemandVMTWithout()
	{
		float total_VMTD = 0;
		for (int scen = 0; scen < seed1.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_VMTD+= seed1.getValueFloat(CEConst.IDS_SP_VMTD, 0, 0, scen+1, -1);
		}
		return total_VMTD;
	}

	/**
	 * Getter for total VMTD with IMAP.
	 *
	 * @return the total demand vmt with
	 */
	public static float getTotalDemandVMTWith()
	{
		float total_VMTD = 0;
		for (int scen = 0; scen < seed2.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_VMTD+= seed2.getValueFloat(CEConst.IDS_SP_VMTD, 0, 0, scen+1, -1);
		}
		return total_VMTD;
	}

	/**
	 * Getter for average speed without IMAP.
	 *
	 * @return the g per mile avg speed without
	 */
	public static float getgPerMileAvgSpeedWithout()
	{
		float total_SMS = 0;
		for (int scen = 0; scen < seed1.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_SMS+= seed1.getValueFloat(CEConst.IDS_SP_SPACE_MEAN_SPEED, 0, 0, scen+1, -1);
		}
		return total_SMS/seed1.getValueInt(CEConst.IDS_NUM_SCEN);
	}

	/**
	 * Getter for average speed with IMAP.
	 *
	 * @return the g per mile avg speed with
	 */
	public static float getgPerMileAvgSpeedWith()
	{
		float total_SMS = 0;
		for (int scen = 0; scen < seed2.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_SMS+= seed2.getValueFloat(CEConst.IDS_SP_SPACE_MEAN_SPEED, 0, 0, scen+1, -1);
		}
		return total_SMS/seed2.getValueInt(CEConst.IDS_NUM_SCEN);
	}
	
	/**
	 * Getter for total speed without IMAP. Speed is summed over all scenarios.
	 * 
	 * @return the g per mile total speed without IMAP
	 */
	public static float getgPerMileTotalSpeedWithout() {
		float total_SMS = 0;
		for (int scen = 0; scen < seed1.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_SMS+= seed1.getValueFloat(CEConst.IDS_SP_SPACE_MEAN_SPEED, 0, 0, scen+1, -1);
		}
		return total_SMS;
	}
	
	/**
	 * Getter for total speed with IMAP. Speed is summed over all scenarios.
	 * 
	 * @return the g per mile total speed with IMAP
	 */
	public static float getgPerMileTotalSpeedWith() {
		float total_SMS = 0;
		for (int scen = 0; scen < seed2.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
			total_SMS+= seed2.getValueFloat(CEConst.IDS_SP_SPACE_MEAN_SPEED, 0, 0, scen+1, -1);
		}
		return total_SMS;
	}
	
	private static ArrayList<CEDate> getHolidayList() {
		// Determining MLK Day (3rd Monday of January)
		int dayCount = 0;
		int dayIdx = 1;
		while (dayCount != 3) {
			if (CEDate.dayOfWeek(dayIdx, 1, year) == 0) {
				dayCount++;
			}
			dayIdx++;
		}
		CEDate mlkDay = new CEDate(year, 1, dayIdx);
		
		// Determining Memorial Day (Last Monday of May)
		dayCount = 0;
		dayIdx = 31;
		while (dayCount != 1) {
			if (CEDate.dayOfWeek(dayIdx, 5, year) == 0) {
				dayCount++;
			}
			dayIdx--;
		}
		CEDate memorialDay = new CEDate(year, 5, dayIdx);
		
		// Determining Labor Day (1st Monday of September)
		dayCount = 0;
		dayIdx = 1;
		while (dayCount != 1) {
			if (CEDate.dayOfWeek(dayIdx, 9, year) == 0) {
				dayCount++;
			}
			dayIdx++;
		}
		CEDate laborDay = new CEDate(year, 9, dayIdx);
		
		// Determining Thanksgiving Thursday (4th Thursday of November)
		dayCount = 0;
		dayIdx = 1;
		while (dayCount != 4) {
			if (CEDate.dayOfWeek(dayIdx, 11, year) == 3) {
				dayCount++;
			}
			dayIdx++;
		}
		CEDate thanksgivingThu = new CEDate(year, 11, dayIdx);
		
		
		// Determining Thanksgiving Friday
		dayCount = 0;
		dayIdx = 1;
		while (dayCount != 4) {
			if (CEDate.dayOfWeek(dayIdx, 11, year) == 4) {
				dayCount++;
			}
			dayIdx++;
		}
		CEDate thanksgivingFri = new CEDate(year, 11, dayIdx);
		
		ArrayList<CEDate>  holidays = new ArrayList<CEDate>();
		holidays.add(new CEDate(year, 1,1));  // New Years
		holidays.add(mlkDay);   // MLK Day
		holidays.add(memorialDay);   // Memorial Day
		holidays.add(new CEDate(year, 7,4));   // Independence Day
		holidays.add(laborDay);   // Labor Day
		holidays.add(thanksgivingThu);   // Thanksgiving (Thur)
		holidays.add(thanksgivingFri);   // Thanksgiving (Fri)
		holidays.add(new CEDate(year, 12,25));   // Christmas
		
		return holidays;
	}

	/**
	 * Method to create and run the reliability analysis both with and without
	 * IMAP.
	 */
	public static void runFreeval()
	{
		
		seed1.singleRun(0, -1);
		seed1.cleanScenarios();
		seed2.singleRun(0, -1);
		seed2.cleanScenarios();
		
		// Extracting Truck % (demand-weighted average across all periods)
		float truckPct = 0.0f;
		float totalDemand = 0;
		for (int per = 0; per < seed1.getValueInt(CEConst.IDS_NUM_PERIOD); per++) {
			truckPct += seed1.getValueFloat(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_MAINLINE, 0, per, 0, -1) * seed1.getValueInt(CEConst.IDS_MAIN_DEMAND_VEH, 0, per, 0, -1);
			totalDemand += seed1.getValueInt(CEConst.IDS_MAIN_DEMAND_VEH, 0, per, 0, -1);
		}
		truckPct = truckPct/totalDemand;
		System.out.println("Truck Pct: " + formatter2.format( truckPct));
		
		CostBenefitEstimate.setTruckPercent(truckPct);
		
		// Creating ScenarioGenerators
		if (rngSeed < 0)
		{ // Creating random seed if it has not been set
			rngSeed = (int) (System.currentTimeMillis() % 1000000);
		}
		ScenarioGenerator sg1 = new ScenarioGenerator(seed1);
		sg1.setRNGSeed(rngSeed);
		seed1.setRLRNGSeed(rngSeed);

		ScenarioGenerator sg2 = new ScenarioGenerator(seed2);
		sg2.setRNGSeed(rngSeed);
		seed2.setRLRNGSeed(rngSeed);

		// Creating Demand Data
		DemandData demand1 = new DemandData(seed1, DemandData.TYPE_GP);
		for (int dayIdx = 0; dayIdx < 7; dayIdx++)
		{
			demand1.setDayActive(dayIdx, activeDays[dayIdx]); // Monday = 0,
																// Sunday = 6
		}		
		demand1.useUrbanDefaults();

		DemandData demand2 = new DemandData(seed2, DemandData.TYPE_GP);
		for (int dayIdx = 0; dayIdx < 7; dayIdx++)
		{
			demand2.setDayActive(dayIdx, activeDays[dayIdx]); // Monday = 0,
																// Sunday = 6
		}
		demand2.useUrbanDefaults();

		// Creating IncidentData
		IncidentData inc1 = new IncidentData(seed1, IncidentData.TYPE_GP);
		IncidentData inc2 = new IncidentData(seed2, IncidentData.TYPE_GP);

		if (FreevalFileParser.getIncidentRatesUsed() == true)
		{
			inc1.calcIncidentFrequenciesCR(incidentRatesNoIMAP, demand1, false);
			inc2.calcIncidentFrequenciesCR(incidentRatesWithIMAP, demand2, false);
		}
		else
		{
			inc1.setCrashRateRatio(crashRateRatioNoIMAP); // Sets the crash rate
															// ratio
			inc1.calcIncidentFrequenciesCR(crashRateFrequenciesNoIMAP, demand1, true);
			//float[] a = inc1.getIncidentFrequencyArr();
			//String s = "";
			//for (int i = 0; i < a.length; i++) {
			//	s=s+formatter2.format( a[i])+",";
			//}
			//System.out.println(s);
			inc2.setCrashRateRatio(crashRateRatioWithIMAP); // Sets the crash
															// rate ratio
			inc2.calcIncidentFrequenciesCR(crashRateFrequenciesWithIMAP, demand2, true);
		}

		for (int incidentType = 0; incidentType < 5; incidentType++)
		{
			inc1.setIncidentDistribution(incidentType, durationInfoNoIMAP[incidentType][0]); // Sets
																								// distribution
																								// for
																								// incident
																								// type
			inc1.setIncidentDuration(incidentType, durationInfoNoIMAP[incidentType][1]); // Sets
																							// the
																							// average
																							// duration
																							// for
																							// an
																							// incident
																							// type
			inc1.setIncidentDurationStdDev(incidentType, durationInfoNoIMAP[incidentType][2]); // Sets
																								// the
																								// std
																								// deviation
																								// for
																								// an
																								// incident
																								// type
			inc1.setIncidentDurMin(incidentType, durationInfoNoIMAP[incidentType][3]); // Sets
																						// the
																						// minimum
																						// duration
																						// for
																						// an
																						// incident
																						// type
			inc1.setIncidentDurMax(incidentType, durationInfoNoIMAP[incidentType][4]); // Sets
																						// the
																						// maximum
																						// duration
																						// for
																						// an
																						// incident
																						// type

			inc2.setIncidentDistribution(incidentType, durationInfoWithIMAP[incidentType][0]); // Sets
																								// distribution
																								// for
																								// incident
																								// type
			inc2.setIncidentDuration(incidentType, durationInfoWithIMAP[incidentType][1]); // Sets
																							// the
																							// average
																							// duration
																							// for
																							// an
																							// incident
																							// type
			inc2.setIncidentDurationStdDev(incidentType, durationInfoWithIMAP[incidentType][2]); // Sets
																									// the
																									// std
																									// deviation
																									// for
																									// an
																									// incident
																									// type
			inc2.setIncidentDurMin(incidentType, durationInfoWithIMAP[incidentType][3]); // Sets
																							// the
																							// minimum
																							// duration
																							// for
																							// an
																							// incident
																							// type
			inc2.setIncidentDurMax(incidentType, durationInfoWithIMAP[incidentType][4]); // Sets
																							// the
																							// maximum
																							// duration
																							// for
																							// an
																							// incident
																							// type

		}

		// Assigning Demand and incidents to Seeds and scenario generators
		seed1.setSpecifiedGPDemand(demand1.getSpecifiedDemand()); // Saves GP
																	// Demand
																	// multipliers
		seed1.setWeekdayUsed(demand1.getActiveDays()); // Saves days active
		if (!includeHolidays) {
			seed1.setDayExcluded(FreevalFileParser.getHolidayList());
		}
		seed1.setGPIncidentCAF(inc1.getIncidentCAF());
		seed1.setGPIncidentDAF(inc1.getIncidentDAF());
		seed1.setGPIncidentSAF(inc1.getIncidentFFSAF());
		seed1.setGPIncidentLAF(inc1.getIncidentLAF());
		seed1.setGPIncidentDistribution(inc1.getIncidentDistribution());
		seed1.setGPIncidentDuration(inc1.getIncidentDurationInfo());
		seed1.setGPIncidentFrequency(inc1.getIncidentFrequencyArr());
		sg1.setDemandDataGP(demand1); // Setting Demand Data
		sg1.setGPIncidentData(inc1); // Setting Incident Data
		sg1.includeGPWorkZones(false); // Turning off work zones
		sg1.includeWeather(false); // Turning off weather
		sg1.includeIncidentsGP(true); // Turning on Incidents

		seed2.setSpecifiedGPDemand(demand2.getSpecifiedDemand()); // Saves GP
																	// Demand
																	// multipliers
		seed2.setWeekdayUsed(demand2.getActiveDays()); // Saves days active
		if (!includeHolidays) {
			seed2.setDayExcluded(FreevalFileParser.getHolidayList());
		}
		seed2.setGPIncidentCAF(inc2.getIncidentCAF());
		seed2.setGPIncidentDAF(inc2.getIncidentDAF());
		seed2.setGPIncidentSAF(inc2.getIncidentFFSAF());
		seed2.setGPIncidentLAF(inc2.getIncidentLAF());
		seed2.setGPIncidentDistribution(inc2.getIncidentDistribution());
		seed2.setGPIncidentDuration(inc2.getIncidentDurationInfo());
		seed2.setGPIncidentFrequency(inc2.getIncidentFrequencyArr());
		sg2.setDemandDataGP(demand2); // Setting Demand Data
		sg2.setGPIncidentData(inc2); // Setting Incident Data
		sg2.includeGPWorkZones(false); // Turning off work zones
		sg2.includeWeather(false); // Turning off weather
		sg2.includeIncidentsGP(true); // Turning on Incidents

		boolean scenariosCreated1 = sg1.generateScenarios(); // Creates the
																// scenarios
		if (scenariosCreated1)
		{
			seed1.setRLScenarios(sg1.getGPScenario(), sg1.getMLScenario(), sg1.getScenarioInfoList());
		}
		else
		{
			System.out.println("Something went wrong generating before scenarios");
		}
		RLBatchRunDialog batchRunDialog1 = new RLBatchRunDialog(seed1, null);
		batchRunDialog1.setVisible(true); // These two functions will run all
											// scenarios with a progressbar

		boolean scenariosCreated2 = sg2.generateScenarios(); // Creates the
																// scenarios
		if (scenariosCreated2)
		{
			seed2.setRLScenarios(sg2.getGPScenario(), sg2.getMLScenario(), sg2.getScenarioInfoList());
		}
		else
		{
			System.out.println("Something went wrong generating after scenarios");
		}
		RLBatchRunDialog batchRunDialog2 = new RLBatchRunDialog(seed2, null);
		batchRunDialog2.setVisible(true);
		
		// Debug outputs
		//System.out.println("Seed 1 # Scen: " + String.valueOf(seed1.getValueInt(CEConst.IDS_NUM_SCEN)));
		//System.out.println("Seed 2 # Scen: " + String.valueOf(seed2.getValueInt(CEConst.IDS_NUM_SCEN)));
		//System.out.println("Seed 1 Total VHD: " + formatter2.format(FreevalFileParser.getTotalVehDelayWithoutImap()));
		//System.out.println("Seed 2 Total VHD: " + formatter2.format(FreevalFileParser.getTotalVehDelayWithImap()));
		//System.out.println("Seed 1 Avg SMS: " + formatter2.format(FreevalFileParser.getgPerMileAvgSpeedWithout()));
		//System.out.println("Seed 2 Avg SMS: " + formatter2.format(FreevalFileParser.getgPerMileAvgSpeedWith()));
		
		//saveAsSeed(seed2);
		
		rngSeed = -1;

	}

	/**
	 * This dialog is used to perform batch run of scenarios.
	 *
	 * @author Shu Liu
	 */
	private static class RLBatchRunDialog extends javax.swing.JDialog implements PropertyChangeListener
	{

		/**
		 * Invoked when task's progress property changes.
		 *
		 * @param evt
		 *            the evt
		 */
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			if ("progress".equals(evt.getPropertyName()))
			{
				int progress = (Integer) evt.getNewValue();
				progressBar.setValue(progress);
			}
		}

		/** The progress bar. */
		private javax.swing.JProgressBar progressBar;

		/** The return status. */
		private int returnStatus = RET_CANCEL;

		/**
		 * A return status code - returned if Cancel button has been pressed.
		 */
		public static final int RET_CANCEL = 0;

		/** A return status code - returned if OK button has been pressed. */
		public static final int RET_OK = 1;

		/** The seed. */
		private final Seed seed;

		// private final MainWindow mainWindow;

		/**
		 * Creates new form NewOkCancelDialog.
		 *
		 * @param seed
		 *            seed to be analyzed
		 * @param parent
		 *            the parent
		 */
		public RLBatchRunDialog(Seed seed, Frame parent)
		{
			super(parent, true);
			initComponents();

			// set starting position
			this.setLocationRelativeTo(this.getRootPane());

			this.seed = seed;
			// this.mainWindow = mainWindow;

			// Close the dialog when Esc is pressed
			String cancelName = "cancel";
			InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
			ActionMap actionMap = getRootPane().getActionMap();
			actionMap.put(cancelName, new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					doClose(RET_CANCEL);
				}
			});

			doRun();
		}

		/**
		 * Getter for return status.
		 *
		 * @return the return status of this dialog - one of RET_OK or
		 *         RET_CANCEL
		 */
		public int getReturnStatus()
		{
			return returnStatus;
		}

		/**
		 * Inits the components.
		 */
		private void initComponents()
		{

			progressBar = new javax.swing.JProgressBar();

			setTitle("Batch Run");
			setResizable(false);
			addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent evt)
				{
					closeDialog(evt);
				}
			});

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
					javax.swing.GroupLayout.Alignment.TRAILING,
					layout.createSequentialGroup().addContainerGap()
							.addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
							.addContainerGap()));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup().addContainerGap()
							.addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

			pack();
		}

		/**
		 * Do run.
		 */
		private void doRun()
		{
			final int start;
			final int end;

			try
			{
				start = 0;
				end = seed.getValueInt(CEConst.IDS_NUM_SCEN);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, "Invalid scenario number", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			class Task extends SwingWorker<Void, Void>
			{
				/*
				 * Main task. Executed in background thread.
				 */
				@Override
				public Void doInBackground()
				{
					// timing starts
					long timingStart = new Date().getTime();

					Thread thread = new Thread(new Runnable()
					{
						public void run()
						{
							for (int scen = start; scen <= end; scen++)
							{
								setProgress((scen - start) * 100 / (end - start));
								if (!seed.hasValidOutput(scen, -1))
								{
									seed.singleRun(scen, -1);// , threadIndex);
								}
							}
						}
					});

					thread.start();

					try
					{
						thread.join();
					}
					catch (InterruptedException ex)
					{
						Logger.getLogger(RLBatchRunDialog.class.getName()).log(Level.SEVERE, null, ex);
					}

					// timing ends
					long timingEnd = new Date().getTime();
					System.out.println("Batch run finished. Time cost: " + (timingEnd - timingStart) + " ms");

					return null;
				}

				/*
				 * Executed in event dispatching thread
				 */
				@Override
				public void done()
				{
					progressBar.setString("Finish");
					doClose(RET_OK);
				}
			}

			try
			{
				progressBar.setStringPainted(true);
				progressBar.setString("Analyzing, please wait...");

				Task task = new Task();
				task.addPropertyChangeListener(this);
				task.execute();

			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, "Error occured in batch run", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Closes the dialog.
		 *
		 * @param evt
		 *            the evt
		 */
		private void closeDialog(java.awt.event.WindowEvent evt)
		{
			doClose(RET_CANCEL);
		}

		/**
		 * Do close.
		 *
		 * @param retStatus
		 *            the ret status
		 */
		private void doClose(int retStatus)
		{
			returnStatus = retStatus;
			setVisible(false);
			dispose();
		}
	}

	public static int getAnnualDaysOfOperation()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static String saveAsSeed(Seed seed) {
        if (seed == null) {
            JOptionPane.showMessageDialog(null, "No seed is selected", "Error", JOptionPane.ERROR_MESSAGE);
            return "Fail to save seed";
        }
        try {
            JFileChooser seedFileChooser = new JFileChooser();
            int option = seedFileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                String saveFileName = seedFileChooser.getSelectedFile().getAbsolutePath();
                if (!saveFileName.endsWith(".seed")) {
                    saveFileName += ".seed";
                }
                //save seed to file

                FileOutputStream fos = new FileOutputStream(saveFileName);
                GZIPOutputStream gzos = new GZIPOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(gzos);
                oos.writeObject(seed);
                oos.close();
                seed.setValue(CEConst.IDS_SEED_FILE_NAME, saveFileName);
                return "Seed saved to " + seed.getValueString(CEConst.IDS_SEED_FILE_NAME);
            } else {
                return "Save cancelled by user";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to save seed " + e.toString();
        }
    }
	
	public static boolean createSummaryReport(File file) {
		// Collecting Summary Information
		String fileName = "";  // TODO: Add File Name
		String facilityType = SetupPanel.getFacilityType(); // New
		String division = SetupPanel.getDivisionString(); // New
		String county = SetupPanel.getCounty();
		String roadFrom = SetupPanel.getRoadFrom(); // New
		String roadTo = SetupPanel.getRoadTo(); // New
		String laborCost = formatter2.format(InformationScreen.getCostLabor());
		String truckOpCost = formatter2.format(InformationScreen.getCostTruck());
		String fixedCost = formatter2.format(InformationScreen.getOtherFixedCosts());  // New
		String hoursOp1 = InformationScreen.getOperationHoursString();  // New
		String hoursOp2 = formatter2.format(InformationScreen.getOperationHoursInt());
		String includeStr = InformationScreen.getDaysIncludedString(); // New
		String centerlineMiles = InformationScreen.getCenterLineMiles();
		String fuelPrice = InformationScreen.getFuelPrice();
		String areaType = EstimationScreen.getAreaType(); // New
		String studyType = EstimationScreen.getStudyType(); // New
		String incidentRateType = EstimationScreen.getIncidentRateType();  // New
		String incidentRatesLine1 = "Month,Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec";
		String incidentRatesLine2 = "No IMAP," + String.format("%.2f", incidentRatesNoIMAP[0])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[1])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[2])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[3])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[4])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[5])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[6])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[7])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[8])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[9])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[10])
				+ "," + String.format("%.2f", incidentRatesNoIMAP[11]);
		String incidentRatesLine3 = "With IMAP," + String.format("%.2f", incidentRatesWithIMAP[0])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[1])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[2])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[3])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[4])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[5])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[6])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[7])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[8])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[9])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[10])
				+ "," + String.format("%.2f", incidentRatesWithIMAP[11]);
		String crashRate = formatter2.format( crashRateFrequenciesNoIMAP[0]); 
		String incCrashRatio = formatter2.format( crashRateRatioNoIMAP);  
		String shoulderInfo = formatter2.format(durationInfoNoIMAP[0][0])  // Distribution
				+ "," + formatter2.format(durationInfoNoIMAP[0][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoNoIMAP[0][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoNoIMAP[0][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoNoIMAP[0][4]) // Max Duration
				+ " ," // Blank Column
				+ "," + formatter2.format(durationInfoWithIMAP[0][0])  // Distribution
				+ "," + formatter2.format(durationInfoWithIMAP[0][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoWithIMAP[0][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoWithIMAP[0][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoWithIMAP[0][4]); // Max Duration
		String onelcInfo = formatter2.format( durationInfoNoIMAP[1][0])  // Distribution
				+ "," + formatter2.format(durationInfoNoIMAP[1][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoNoIMAP[1][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoNoIMAP[1][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoNoIMAP[1][4]) // Max Duration
				+ " ," // Blank Column
				+ "," + formatter2.format(durationInfoWithIMAP[1][0])  // Distribution
				+ "," + formatter2.format(durationInfoWithIMAP[1][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoWithIMAP[1][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoWithIMAP[1][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoWithIMAP[1][4]); // Max Duration
		String twolcInfo = formatter2.format(durationInfoNoIMAP[2][0])  // Distribution
				+ "," + formatter2.format(durationInfoNoIMAP[2][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoNoIMAP[2][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoNoIMAP[2][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoNoIMAP[2][4]) // Max Duration
				+ " ," // Blank Column
				+ "," + formatter2.format(durationInfoWithIMAP[2][0])  // Distribution
				+ "," + formatter2.format(durationInfoWithIMAP[2][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoWithIMAP[2][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoWithIMAP[2][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoWithIMAP[2][4]); // Max Duration
		String threelcInfo = formatter2.format(durationInfoNoIMAP[3][0])  // Distribution
				+ "," + formatter2.format(durationInfoNoIMAP[3][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoNoIMAP[3][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoNoIMAP[3][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoNoIMAP[3][4]) // Max Duration
				+ " ," // Blank Column
				+ "," + formatter2.format(durationInfoWithIMAP[3][0])  // Distribution
				+ "," + formatter2.format(durationInfoWithIMAP[3][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoWithIMAP[3][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoWithIMAP[3][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoWithIMAP[3][4]); // Max Duration
		String fourlcInfo = formatter2.format(durationInfoNoIMAP[4][0])  // Distribution
				+ "," + formatter2.format(durationInfoNoIMAP[4][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoNoIMAP[4][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoNoIMAP[4][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoNoIMAP[4][4]) // Max Duration
				+ " ," // Blank Column
				+ "," + formatter2.format(durationInfoWithIMAP[4][0])  // Distribution
				+ "," + formatter2.format(durationInfoWithIMAP[4][1]) // Mean Duration
				+ "," + formatter2.format(durationInfoWithIMAP[4][2]) // StdDev Duration
				+ "," + formatter2.format(durationInfoWithIMAP[4][3]) // Minimum Duration
				+ "," + formatter2.format(durationInfoWithIMAP[4][4]); // Max Duration
		String delaySavings = CostBenefitEstimate.getDelaySavingsString(); // New
		String delaySavingsBenefit = CostBenefitEstimate.getDelaySavingsBenefitString(); // New
		String fuelSavings = CostBenefitEstimate.getFuelSavingsString(); // New
		String fuelSavingsBenefit = CostBenefitEstimate.getFuelSavingsBenefitString(); // New
		String operationCost = CostBenefitEstimate.getOperationCostString(); // New
		String bcRatio = CostBenefitEstimate.getBCRatioString(); // New
		RLSummary rlNoImap = computeRLSummaryOutput(seed1);
		String beforeRLSummaryString = formatter2.format(rlNoImap.meanTTI) 
				+ "," + formatter2.format(rlNoImap.semiSTD)
				+ "," + formatter2.format(rlNoImap.p50)
				+ "," + formatter2.format(rlNoImap.p80)
				+ "," + formatter2.format(rlNoImap.p95)
				+ "," + formatter2.format(rlNoImap.misery)
				+ "," + formatter2.format(rlNoImap.vmtat2)
				+ "\n";
		RLSummary rlWithImap = computeRLSummaryOutput(seed2);
		String afterRLSummaryString = formatter2.format(rlWithImap.meanTTI) 
				+ "," + formatter2.format(rlWithImap.semiSTD)
				+ "," + formatter2.format(rlWithImap.p50)
				+ "," + formatter2.format(rlWithImap.p80)
				+ "," + formatter2.format(rlWithImap.p95)
				+ "," + formatter2.format(rlWithImap.misery)
				+ "," + formatter2.format(rlWithImap.vmtat2)
				+ "\n";
		
		// Writing Summary File
		try {
			String csvFileName = file.getAbsolutePath();
			if (!csvFileName.endsWith(".csv")) {
                csvFileName += ".csv";
            }
			FileWriter fw = new FileWriter(csvFileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("IMAP Estimation Tool Summary Report\n");
			// General Information
			bw.write("General Information\n");
			bw.write("File Name,"+fileName+"\n");
			bw.write("Facility Type,"+facilityType+"\n");
			bw.write("Division,"+division+"\n");
			bw.write("County,"+county+"\n");
			bw.write("Road,From,"+roadFrom+"\n");
			bw.write(",To,"+roadTo+"\n");
			bw.write("\n");
			// Cost Information
			bw.write("Cost Information"+"\n");
			bw.write("Cost for Labor ($),"+laborCost+"\n");
			bw.write("Cost for Truck Operation/hr ($),"+truckOpCost+"\n");
			bw.write("Other Fixed Costs ($),"+fixedCost+"\n");
			bw.write("Hours of Operation (hr),"+hoursOp1+"\n");
			bw.write("Hours of Operation (hr),"+hoursOp2+"\n");
			bw.write("Include,"+includeStr+"\n");
			bw.write("Number of Scenarios Generated,"+String.valueOf(seed1.getValueInt(CEConst.IDS_NUM_SCEN))+"\n");
			bw.write("Centerline Miles,"+centerlineMiles+"\n");
			bw.write("Fuel Price ($ per GAL),"+fuelPrice+"\n");
			bw.write("\n");
			// Benefit Cost Parameters
			bw.write("Benefit Cost Parameters\n");
			bw.write("Area Type,"+areaType+"\n");
			bw.write("Study Type,"+studyType+"\n");
			bw.write("Incident Rate,"+incidentRateType+"\n");
			if (incidentRatesUsed) {
				bw.write(incidentRatesLine1 + "\n");
				bw.write(incidentRatesLine2 + "\n");
				bw.write(incidentRatesLine3 + "\n");
			} else {
				bw.write("Crash Rate,"+crashRate+"\n");
				bw.write("Incident/Crash Ratio,"+incCrashRatio+"\n");
			}
			bw.write(""+"\n");
			// Source of Incident Severity and Duration Characteristics
			bw.write("Source of Incident Severity and Duration Characteristics"+"\n");
			bw.write(""+"\n");
			bw.write("Before, , , , , , ,After"+"\n");
			bw.write("Incident Severity,Distribution,Mean Duration,StDev Duration,Min Duration,Max Duration, ,Distribution,Mean Duration,StDev Duration,Min Duration,Max Duration"+"\n");
			bw.write("Shoulder Closure,"+shoulderInfo+"\n");
			bw.write("One Lane Closure,"+onelcInfo+"\n");
			bw.write("Two Lane Closure,"+twolcInfo+"\n");
			bw.write("Three Lane Closure,"+threelcInfo+"\n");
			bw.write("Four Lane Closure,"+fourlcInfo+"\n");
			bw.write(""+"\n");
			// Outputs
			bw.write("Outputs"+"\n");
			bw.write("Reliability Summary,Mean TTI,Semi Std Dev,50th Percentile, 80th Percentile, PTI (95th), Misery Index, VMT at TTI>2\n");
			bw.write("Before IMAP," + beforeRLSummaryString +"\n");
			bw.write("With IMAP," + afterRLSummaryString +"\n");
			bw.write("Delay Savings (veh-hr),"+delaySavings+"\n");
			bw.write("Delay Savings Benefit ($),"+delaySavingsBenefit+"\n");
			bw.write("Fuel Savings (GAL),"+fuelSavings+"\n");
			bw.write("Fuel Savings Benefit ($),"+fuelSavingsBenefit+"\n");
			bw.write("Operation Cost ($),"+operationCost+"\n");
			bw.write("B/C Ratio,"+bcRatio+ "\n");
			
			bw.close();
			
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private static RLSummary computeRLSummaryOutput(Seed seed) {
		RLSummary res = new RLSummary();
		
		ArrayList<RLResult> RLResults = new ArrayList<>();

        float mean = 0;
        float ratingCount = 0;
        float VMT2Count = 0;
        float semiSTD = 0;

        //extract data from seed, and modify probability to match per period
        for (int scen = 1; scen <= seed.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
            for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                float prob = seed.getValueFloat(CEConst.IDS_SCEN_PROB, 0, 0, scen, -1) / seed.getValueInt(CEConst.IDS_NUM_PERIOD);
                float TTI = seed.getValueFloat(CEConst.IDS_P_TTI, 0, period, scen, -1);
                //float actualTravelTime = 0.0f;
                //float ffsTravelTime = 0.0f;
                //for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                //    if (includeSegmentForTTI[seg]) {
                //        actualTravelTime += seed.getValueFloat(CEConst.IDS_ACTUAL_TIME, seg, period, scen, -1);
                //        ffsTravelTime += seed.getValueFloat(CEConst.IDS_FFS_TIME, seg, period, scen, -1);
                //    }
                //}
                //float TTI = actualTravelTime / ffsTravelTime;

                RLResults.add(new RLResult(prob, TTI));
                mean += TTI * prob;
                semiSTD += (TTI - 1) * (TTI - 1) * prob;
                if (TTI < 1.333333f) {
                    ratingCount += prob;
                }
                if (TTI > 2) {
                    VMT2Count += prob;
                }
            }
        }
        Collections.sort(RLResults);

        //--------------------------------------------------------------------------
        res.minTTI = RLResults.get(0).TTI;
        res.maxTTI = RLResults.get(RLResults.size() - 1).TTI;

        res.meanTTI = mean;

        res.semiSTD = (float) Math.sqrt(semiSTD);

        float probCount = 0;
        float miseryTotal = 0, miseryWeight = 0;
        for (RLResult RLResult : RLResults) {
            //find 50th %
            if (probCount <= 0.5 && probCount + RLResult.prob >= 0.5) {
                res.p50 = RLResult.TTI;
            }
            //find 80th %
            if (probCount <= 0.8 && probCount + RLResult.prob >= 0.8) {
                res.p80 = RLResult.TTI;
            }
            //find 95th %
            if (probCount <= 0.95 && probCount + RLResult.prob >= 0.95) {
                res.p95 = RLResult.TTI;
            }

            probCount += RLResult.prob;

            if (probCount >= 0.95) {
                miseryTotal += RLResult.TTI * RLResult.prob;
                miseryWeight += RLResult.prob;
            }
        }

        res.misery = miseryTotal / miseryWeight;
        //ratingText.setText(formatter2dp.format(ratingCount));
        res.vmtat2 = VMT2Count;
        
        return res;
	}
	
	public static boolean createBeforeIMAPRLOutput(File file) {
		return createRLOutputFile(file, seed1);
	}
	
	public static boolean createAfterIMAPRLOutput(File file) {
		return createRLOutputFile(file, seed2);
	}
	
	private static boolean createRLOutputFile(File file, Seed seed) {
		
		//Extract data from seed
		String[] csvHeader = new String[]{
		        "Scenario #", "Analysis Period #", "Probability", "TTI",
		        "max d/c ratio", "Total Queue length at end of time interval (ft)",
		        "Total Denied Entry Queue Length (ft)", "Total On-Ramp queue (veh)",
		        "Freeway mainline delay (min)", //"System delay-- includes on-ramps  (min)",
		        "VMTD Veh-miles / interval  (Demand)", "VMTV Veh-miles / interval (Volume served)",
		        "VHT travel / interval (hrs)", "VHD  delay /interval  (hrs)",
		        "Number of Scenario Weather Events",
		        "Number of Weather Events Active in AP",
		        "Number of Scenario Incidents (GP)",
		        "Number of Incidents Active in AP",
		        "Number of Scenario Work Zones",
		        "Number of Work Zones Active in AP",
		        "Average Space Mean Speed",
		        "Fuel Use"}; //21 items for now
        String[][] resultData = new String[seed.getValueInt(CEConst.IDS_NUM_SCEN) * seed.getValueInt(CEConst.IDS_NUM_PERIOD)][csvHeader.length];
        for (int scen = 1; scen <= seed.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
            for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                int row = (scen - 1) * seed.getValueInt(CEConst.IDS_NUM_PERIOD) + period;
                resultData[row][0] = Integer.toString(scen);
                resultData[row][1] = Integer.toString(period + 1);
                resultData[row][2] = Float.toString(seed.getValueFloat(CEConst.IDS_SCEN_PROB, 0, 0, scen, -1) / seed.getValueInt(CEConst.IDS_NUM_PERIOD));
                resultData[row][3] = seed.getValueString(CEConst.IDS_P_TTI, 0, period, scen, -1);
                resultData[row][4] = seed.getValueString(CEConst.IDS_P_MAX_DC, 0, period, scen, -1);
                resultData[row][5] = seed.getValueString(CEConst.IDS_P_TOTAL_MAIN_QUEUE_LENGTH_FT, 0, period, scen, -1);
                resultData[row][6] = seed.getValueString(CEConst.IDS_P_TOTAL_DENY_QUEUE_VEH, 0, period, scen, -1);
                resultData[row][7] = seed.getValueString(CEConst.IDS_P_TOTAL_ON_QUEUE_VEH, 0, period, scen, -1);
                resultData[row][8] = seed.getValueString(CEConst.IDS_P_MAIN_DELAY, 0, period, scen, -1);
                resultData[row][9] = seed.getValueString(CEConst.IDS_P_VMTD, 0, period, scen, -1);
                resultData[row][10] = seed.getValueString(CEConst.IDS_P_VMTV, 0, period, scen, -1);
                resultData[row][11] = seed.getValueString(CEConst.IDS_P_VHT, 0, period, scen, -1);
                resultData[row][12] = seed.getValueString(CEConst.IDS_P_VHD, 0, period, scen, -1);
                resultData[row][13] = String.valueOf(seed.getRLScenarioInfo().get(scen).getNumberOfWeatherEvents());
                int evtCounter = 0;
                if (seed.getRLScenarioInfo().get(scen).hasWeatherEvent()) {
                    for (WeatherEvent event : seed.getRLScenarioInfo().get(scen).getWeatherEventList()) {
                        evtCounter += event.checkActiveInPeriod(period) ? 1 : 0;
                    }
                }
                resultData[row][14] = String.valueOf(evtCounter);
                resultData[row][15] = String.valueOf(seed.getRLScenarioInfo().get(scen).getNumberOfGPIncidentEvents());
                evtCounter = 0;
                if (seed.getRLScenarioInfo().get(scen).hasIncidentGP()) {
                    for (IncidentEvent event : seed.getRLScenarioInfo().get(scen).getGPIncidentEventList()) {
                        evtCounter += event.checkActiveInPeriod(period) ? 1 : 0;
                    }
                }
                resultData[row][16] = String.valueOf(evtCounter);
                resultData[row][17] = String.valueOf(seed.getRLScenarioInfo().get(scen).getNumberOfWorkZones());
                evtCounter = 0;
                if (seed.getRLScenarioInfo().get(scen).hasWorkZone()) {
                    for (WorkZone event : seed.getRLScenarioInfo().get(scen).getWorkZoneEventList()) {
                        evtCounter += event.isActiveIn(period) ? 1 : 0;
                    }
                }
                resultData[row][18] = String.valueOf(evtCounter);
                resultData[row][19] = String.valueOf(seed.getValueFloat(CEConst.IDS_P_SPACE_MEAN_SPEED, 0, period, scen, -1));
                resultData[row][20] = String.valueOf(
                		(seed.getValueFloat(CEConst.IDS_P_VMTD, 0, period, scen, -1) * truckPercentage/100.0f
                				* CostBenefitEstimate.gallonPerMileForTruckVeh(seed.getValueFloat(CEConst.IDS_P_SPACE_MEAN_SPEED, 0, period, scen, -1)))
        				+ (seed.getValueFloat(CEConst.IDS_P_VMTD, 0, period, scen, -1) * (1 - truckPercentage/100.0f)
        						* CostBenefitEstimate.gallonPerMileForLightVeh(seed.getValueFloat(CEConst.IDS_P_SPACE_MEAN_SPEED, 0, period, scen, -1)))
                		);
            }
        }
		
		try {
			String csvFileName = file.getAbsolutePath();
			if (!csvFileName.endsWith(".csv")) {
                csvFileName += ".csv";
            }
			FileWriter fw = new FileWriter(csvFileName);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String str : csvHeader) {
				bw.write(str);
				bw.write(",");
			}
			bw.write("\n");
			for (String[] strRow : resultData) {
				for (String str : strRow) {
					bw.write(str);
					bw.write(",");
				}
				bw.write("\n");
			}
			
			bw.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private static class RLResult implements Comparable {

        float TTI;

        float prob;

        /**
         * Constructor
         *
         * @param prob probability of this result
         * @param TTI travel time index of this result
         */
        public RLResult(float prob, float TTI) {
            this.prob = prob;
            this.TTI = TTI;
        }

        @Override
        public int compareTo(Object o) {
            return Float.compare(TTI, ((RLResult) o).TTI);
        }
    }
	
	private static class RLSummary {
		
		public float minTTI = 00.0f;
		public float maxTTI = 0.0f;
		public float meanTTI = 0.0f;
		public float semiSTD = 0.0f;
		public float p50 = 0.0f;
		public float p80 = 0.0f;
		public float p95 = 0.0f;
		public float vmtat2 = 0.0f;
		public float misery = 0.0f;
		
	}

}
