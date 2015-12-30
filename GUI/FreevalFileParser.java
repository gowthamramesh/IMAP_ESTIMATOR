
package GUI;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

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
import coreEngine.Helper.ASCIIAdapter.ASCIISeedFileAdapter_GPMLFormat;
import coreEngine.Helper.ASCIIAdapter.ASCIISeedFileAdapter_RLFormat;
import coreEngine.reliabilityAnalysis.ScenarioGenerator;
import coreEngine.reliabilityAnalysis.DataStruct.DemandData;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentData;
import coreEngine.reliabilityAnalysis.DataStruct.Scenario;

public class FreevalFileParser
{
	/*
	 * Seed to hold Reliability run without IMAP
	 */
	private static Seed seed1;
    
	/*
	 * Seed to hold Reliability run with IMAP
	 */
	private static Seed seed2;  // With Imap
    
	/*
	 * Default Truck Percentage
	 */
    private static int truckPercentage = 5;
    
    /*
     * Crash Rate Ratio without IMAP
     */
    private static float crashRateRatioNoIMAP = 4.9f;
    
    /*
     * Crash Rate Frequencies without IMAP
     */
    private static float[] crashRateFrequenciesNoIMAP = new float[12];
    
    /*
     * Crash Rate Ratio with IMAP
     */
    private static float crashRateRatioWithIMAP = 4.9f;
    
    /*
     * Crash Rate Frequencies with IMAP
     */
    private static float[] crashRateFrequenciesWithIMAP = new float[12];
    
    /*
     * Incident Frequencies without IMAP
     */
    private static float[] incidentRatesNoIMAP = new float[12];
    
    /*
     * Incident Frequencies with IMAP
     */
    private static float[] incidentRatesWithIMAP = new float[12];
    
    /*
     * Random Number Generator Seed
     */
    private static int rngSeed = -1;
    
    /*
     * Identifier to specify if incident rates (as opposed) to crash rates are used
     */
    private static boolean incidentRatesUsed = false;
    
    /**
     * Array holding duration information for the reliability without IMAP.
     * First index is incident type (0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
     * Second index is 0-distribution %, 1-Mean duration, 2-Std Deviation of duration, 3-Minimum Duration, 4-Maximum Duration
     */
    private static float[][] durationInfoNoIMAP = new float[5][5];
    
    /**
     * Array holding duration information for the reliability with IMAP.
     * First index is incident type (0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure)
     * Second index is 0-distribution %, 1-Mean duration, 2-Std Deviation of duration, 3-Minimum Duration, 4-Maximum Duration
     */
    private static float[][] durationInfoWithIMAP = new float[5][5];
    
    /**
     * Array indicating which days are active.
     * 0 - Monday, 1 - Tuesday, 2 - Wednesday, 3 - Thursday, 
     * 4 - Friday, 5 - Saturday, 6 - Sunday
     */
    private static boolean[] activeDays = new boolean[7];
    
    /**
     * Method to open the seed facility from a ASCII (.txt) file.
     * @param file File location (ASCII) of the seed facility.
     * @return
     */
    private static boolean setSeedFacilityFileASCII(File file) {
    	
    	boolean success = false;
    	try {
    	Scanner input = new Scanner(file);
        String firstLine = input.nextLine();
        input.close();
        //choose correct ASCII adapter based on ASCII input file format
        if (firstLine.startsWith("<")) {
            ASCIISeedFileAdapter_GPMLFormat textSeed = new ASCIISeedFileAdapter_GPMLFormat();
            seed1 = textSeed.importFromASCII(file.getAbsolutePath());
            seed2 = textSeed.importFromASCII(file.getAbsolutePath());
            if (seed1 != null && seed2 != null) {
                seed1.setValue(CEConst.IDS_SEED_FILE_NAME, null);
                seed2.setValue(CEConst.IDS_SEED_FILE_NAME, null);
                success = true;
            } else {
                return false;
            }
        } else {
            ASCIISeedFileAdapter_RLFormat textSeed = new ASCIISeedFileAdapter_RLFormat();
            seed1 = textSeed.importFromFile(file.getAbsolutePath());
            seed2 = textSeed.importFromFile(file.getAbsolutePath());
            if (seed1 != null && seed2 != null) {
            	seed1.setValue(CEConst.IDS_SEED_FILE_NAME, null);
            	seed2.setValue(CEConst.IDS_SEED_FILE_NAME, null);
            	success = true;
            } else {
                return false;
            }
        }
    	} catch (FileNotFoundException e) {
    		JOptionPane.showMessageDialog(null, "Something went wrong opening the seed file.", "Error: Seed File Invalid", JOptionPane.ERROR_MESSAGE);
    	}
    	return success;
    	
    }
    
    /**
     * Method to open the seed facility from a SEED (.seed) file.
     * @param file File location (SEED) of the seed facility.
     * @return
     */
    private static boolean setSeedFacilityFileSEED(File file) {
    	
    	boolean success = false;
    	String openFileName = file.getAbsolutePath();

        //Open seed from file
    	try {
	        FileInputStream fis = new FileInputStream(openFileName);
	        GZIPInputStream gzis = new GZIPInputStream(fis);
	        ObjectInputStream ois = new ObjectInputStream(gzis);
	        seed1 = (Seed) ois.readObject();
	        seed2 = (Seed) ois.readObject();
	        seed1.resetSeedToInputOnly();
	        seed2.resetSeedToInputOnly();
	        ois.close();
	        seed1.setValue(CEConst.IDS_SEED_FILE_NAME, openFileName);
	        seed2.setValue(CEConst.IDS_SEED_FILE_NAME, openFileName.substring(0,openFileName.lastIndexOf("."))+"_IMAP"+".seed");
	    	success = true;
    	} catch (IOException | ClassNotFoundException e) {
    		JOptionPane.showMessageDialog(null, "Something went wrong opening the seed file.", "Error: Seed File Invalid", JOptionPane.ERROR_MESSAGE);
    	}
        
    	return success;
    	
    }
    
    /**
     * Method to launch a file chooser to specify the seed facility.
     * @return
     */
    public static boolean selectSeedFile()
	{
		JFileChooser fc = new JFileChooser();  // TODO Specify initial directory
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".seed") || f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "SEED or ASCII Facility Files";
            }
        });
        
        int returnVal = fc.showOpenDialog(null);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	if (fc.getSelectedFile().getName().endsWith(".txt")) {
        		return setSeedFacilityFileASCII(fc.getSelectedFile());
        	} else {
        		return setSeedFacilityFileSEED(fc.getSelectedFile());
        	}
        } else {
        	return false;  // No file chosen
        }
	}
    
    /**
     * Getter for crash rate ratio without IMAP.
     * @return
     */
	public static float getCrashRateRatioNoIMAP() {
		return crashRateRatioNoIMAP;
	}

	/**
	 * Setter for crash rate ratio without IMAP.
	 * @param crashRateRatio new crash rate ratio value (national average  is 4.9)
	 */
	public static void setCrashRateRatioNoIMAP(float crashRateRatio) {
		FreevalFileParser.crashRateRatioNoIMAP = crashRateRatio;
	}
	
	/**
     * Getter for crash rate ratio with IMAP.
     * @return
     */
	public static float getCrashRateRatioWithIMAP() {
		return crashRateRatioWithIMAP;
	}

	/**
	 * Setter for crash rate ratio with IMAP.
	 * @param crashRateRatio new crash rate ratio value (national average  is 4.9)
	 */
	public static void setCrashRateRatioWithIMAP(float crashRateRatio) {
		FreevalFileParser.crashRateRatioWithIMAP = crashRateRatio;
	}

	/**
	 * Getter for monthly crash rate frequencies array without IMAP.
	 * @return
	 */
	public static float[] getCrashRateFrequenciesNoIMAP() {
		return crashRateFrequenciesNoIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array without IMAP
	 * @param crashRateFrequencies Array of monthly crash rate frequencies.
	 */
	public static void setCrashRateFrequenciesNoIMAP(float[] crashRateFrequencies) {
		FreevalFileParser.crashRateFrequenciesNoIMAP = crashRateFrequencies;
	}
	
	/**
	 * Getter for monthly crash rate frequencies array with IMAP
	 * @return
	 */
	public static float[] getCrashRateFrequenciesWithIMAP() {
		return crashRateFrequenciesWithIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array with IMAP
	 * @param crashRateFrequencies Array of monthly crash rate frequencies.
	 */
	public static void setCrashRateFrequenciesWithIMAP(float[] crashRateFrequencies) {
		FreevalFileParser.crashRateFrequenciesWithIMAP = crashRateFrequencies;
	}
	
	/**
	 * Getter for monthly crash rate frequencies array without IMAP.
	 * @return
	 */
	public static float[] getIncidentFrequenciesNoIMAP() {
		return incidentRatesNoIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array without IMAP
	 * @param crashRateFrequencies Array of monthly crash rate frequencies.
	 */
	public static void setIncidentFrequenciesNoIMAP(float[] incidentFrequenciesNoIMAP) {
		FreevalFileParser.incidentRatesNoIMAP = incidentFrequenciesNoIMAP;
	}
	
	/**
	 * Getter for monthly crash rate frequencies array with IMAP
	 * @return
	 */
	public static float[] getIncidentFrequenciesWithIMAP() {
		return incidentRatesWithIMAP;
	}

	/**
	 * Setter for monthly crash rate frequencies array with IMAP
	 * @param crashRateFrequencies Array of monthly crash rate frequencies.
	 */
	public static void setIncidentFrequenciesWithIMAP(float[] incidentFrequenciesWithIMAP) {
		FreevalFileParser.incidentRatesWithIMAP = incidentFrequenciesWithIMAP;
	}
	
	/**
	 * Getter for whether or not incident rates are being used
	 * @return
	 */
	public static boolean getIncidentRatesUsed() {
		return incidentRatesUsed;
	}
	
	/**
	 * Setter for boolean value indicating whether or not incident rates (as opposed to crash rates) are being used
	 * @param val
	 */
	public static void setIncidentRatesUsed(boolean val) {
		FreevalFileParser.incidentRatesUsed = val;
	}

	/**
	 * Getter for the random number generator seed.
	 * @return
	 */
	public static int getRngSeed() {
		return rngSeed;
	}
	
	/**
	 * Setter for the random number generator seed value.
	 * @param rngSeed New random number generator seed.
	 */
	public static void setRngSeed(int rngSeed) {
		FreevalFileParser.rngSeed = rngSeed;
	}

	/**
	 * Getter for facility truck percentage
	 * @return
	 */
	public static float getTruckPercentage()
	{
		// TODO Auto-generated method stub
		return truckPercentage;
	}
	
	/**
	 * Setter for facility truck percentage.
	 * @param truckPercentage Percentage (0 - 100) to be assigned to the facility.
	 */
	public static void setTruckPercentage(int truckPercentage) {
		FreevalFileParser.truckPercentage = truckPercentage;
	}
	
	/**
	 * Setter for active days array. 0 - Monday, 1 - Tuesday, 2 - Wednesday, 3 - Thursday, 
     * 4 - Friday, 5 - Saturday, 6 - Sunday
	 * @param activeDays  Boolean array indicating if a day is active.
	 */
	public static void setActiveDays(boolean[] activeDays) {
		FreevalFileParser.activeDays = activeDays.clone();
	}
	
	/**
	 * Individual setter for active days array. 0 - Monday, 1 - Tuesday, 2 - Wednesday, 3 - Thursday, 
     * 4 - Friday, 5 - Saturday, 6 - Sunday
	 * @param dayIdx Index of day.
	 * @param isActive Boolean value to mark whether or not it is active.
	 */
	public static void setActiveDay(int dayIdx, boolean isActive) {
		FreevalFileParser.activeDays[dayIdx] = isActive;
	}
	
	/**
	 * Setter for incident distribution value (0-100) without IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationDistributionNoIMAP(int incidentType, float value) {
		durationInfoNoIMAP[incidentType][0] = value;
	}
	
	/**
	 * Setter for mean incident duration without IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationMeanNoIMAP(int incidentType, float value) {
		durationInfoNoIMAP[incidentType][1] = value;
	}
	
	/**
	 * Setter for the standard deviation of the incident duration without IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationStdDevNoIMAP(int incidentType, float value) {
		durationInfoNoIMAP[incidentType][2] = value;
	}

	/**
	 * Setter for the minimum incident duration without IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationMinNoIMAP(int incidentType, float value) {
		durationInfoNoIMAP[incidentType][3] = value;
	}

	/**
	 * Setter for the maximum incident duration without IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationMaxNoIMAP(int incidentType, float value) {
		durationInfoNoIMAP[incidentType][4] = value;
	}

	/**
	 * Setter for incident distribution value (0-100) with IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationDistributionWithIMAP(int incidentType, float value) {
		durationInfoWithIMAP[incidentType][0] = value;
	}
	
	/**
	 * Setter for mean incident duration with IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationMeanWithIMAP(int incidentType, float value) {
		durationInfoWithIMAP[incidentType][1] = value;
	}
	
	/**
	 * Setter for the standard deviation of the incident duration with IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationStdDevWithIMAP(int incidentType, float value) {
		durationInfoWithIMAP[incidentType][2] = value;
	}

	/**
	 * Setter for the minimum incident duration with IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationMinWithIMAP(int incidentType, float value) {
		durationInfoWithIMAP[incidentType][3] = value;
	}

	/**
	 * Setter for the maximum incident duration without IMAP.
	 * @param incidentType 0-Shoulder, 1-1 Lane Closure, 2-2 Lane Closure, 3-3 Lane Closure, 4-4+ Lane Closure
	 * @param value 
	 */
	public static void setIncidentDurationMaxWithIMAP(int incidentType, float value) {
		durationInfoWithIMAP[incidentType][4] = value;
	}

	/**
	 * 	Getter for total vehicle hours delay (VHD) without IMAP
	 * @return
	 */
	public static float getTotalVehDelayWithoutImap()
	{
		
		return seed1.getValueFloat(CEConst.IDS_SP_VHD,0,0,0,-1);
	}
	
	/**
	 * 	Getter for total vehicle hours delay (VHD) with IMAP
	 * @return
	 */
	public static float getTotalVehDelayWithImap()
	{
		return seed2.getValueFloat(CEConst.IDS_SP_VHD,0,0,0,-1);
	}
	
	/**
	 * Getter for a particular total (summed) output for a reliability analysis.
	 * @param seed Seed Facility to extract the output from.
	 * @param identifier String identifier (from CEConst) specifying the output.
	 * @return
	 */
	private static float getTotalOutputFromSeed(Seed seed, String identifier) {
		float outputVal = 0.0f;
		for (int scen=0; scen < seed.getValueInt(CEConst.IDS_NUM_SCEN); scen++) {
		    for (int seg=0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
		        for (int per=0; per < seed.getValueInt(CEConst.IDS_NUM_PERIOD); per++) {
		            outputVal+=seed.getValueFloat(identifier, seg, per, scen, -1);
		        }
		    }
		}
		return outputVal;
	}

	/**
	 * 	Getter for total VMTD without IMAP
	 * @return
	 */
	public static float getTotalDemandVMTWithout()
	{
		return seed1.getValueFloat(CEConst.IDS_SP_VMTD,0,0,0,-1);
	}
	
	/**
	 * 	Getter for total VMTD with IMAP
	 * @return
	 */
	public static float getTotalDemandVMTWith()
	{
		return seed2.getValueFloat(CEConst.IDS_SP_VMTD,0,0,0,-1);
	}
	
	/**
	 * 	Getter for average speed without IMAP
	 * @return
	 */
	public static float getgPerMileAvgSpeedWithout()
	{
		return seed1.getValueFloat(CEConst.IDS_SP_SPACE_MEAN_SPEED,0,0,0,-1);
	}
	
	/**
	 * 	Getter for average speed with IMAP
	 * @return
	 */
	public static float getgPerMileAvgSpeedWith()
	{
		return seed2.getValueFloat(CEConst.IDS_SP_SPACE_MEAN_SPEED,0,0,0,-1);
	}

	/**
	 * Method to create and run the reliability analysis both with and without IMAP.
	 */
	public static void runFreeval()
	{
		// Setting truck % for both facilities.
        for (int per = 0; per < seed1.getValueInt(CEConst.IDS_NUM_PERIOD); per++) {
            seed1.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_MAINLINE, truckPercentage, 0, per);  // First segment only
            seed2.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_MAINLINE, truckPercentage, 0, per);  // First segment only
            for (int seg = 1; seg < seed1.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                if (seed1.getValueInt(CEConst.IDS_SEGMENT_TYPE, seg) == CEConst.SEG_TYPE_ONR) {
                    seed1.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_ONR, truckPercentage, seg, per);
                    seed2.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_ONR, truckPercentage, seg, per);
                }
                if (seed1.getValueInt(CEConst.IDS_SEGMENT_TYPE, seg) == CEConst.SEG_TYPE_OFR) {
                    seed1.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_OFR, truckPercentage, seg, per);
                    seed2.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_OFR, truckPercentage, seg, per);
                }
                if (seed1.getValueInt(CEConst.IDS_SEGMENT_TYPE, seg) == CEConst.SEG_TYPE_W) {
                    seed1.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_OFR, truckPercentage, seg, per);
                    seed1.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_ONR, truckPercentage, seg, per);
                    seed2.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_OFR, truckPercentage, seg, per);
                    seed2.setValue(CEConst.IDS_TRUCK_SINGLE_UNIT_PCT_ONR, truckPercentage, seg, per);
                }
            }
        }
        seed1.singleRun(0, -1);
        seed2.singleRun(0, -1);
        
		// Creating ScenarioGenerators
        if (rngSeed < 0) {  // Creating random seed if it has not been set
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
        for (int dayIdx = 0; dayIdx < 7; dayIdx++) {
            demand1.setDayActive(dayIdx, activeDays[dayIdx]); // Monday = 0, Sunday = 6
        }
        demand1.useUrbanDefaults();

        DemandData demand2 = new DemandData(seed2, DemandData.TYPE_GP);
        for (int dayIdx = 0; dayIdx < 7; dayIdx++) {
            demand2.setDayActive(dayIdx, activeDays[dayIdx]); // Monday = 0, Sunday = 6
        }
        demand2.useUrbanDefaults();

        // Creating IncidentData
        IncidentData inc1 = new IncidentData(seed1, IncidentData.TYPE_GP);
        IncidentData inc2 = new IncidentData(seed2, IncidentData.TYPE_GP);
        
        if (FreevalFileParser.getIncidentRatesUsed() == true) {
        	inc1.calcIncidentFrequenciesCR(incidentRatesNoIMAP, demand1, false);
        	inc2.calcIncidentFrequenciesCR(incidentRatesWithIMAP, demand2, false);
        } else {
        	inc1.setCrashRateRatio(crashRateRatioNoIMAP); // Sets the crash rate ratio
            inc1.calcIncidentFrequenciesCR(crashRateFrequenciesNoIMAP, demand1, true);
                    
            inc2.setCrashRateRatio(crashRateRatioWithIMAP); // Sets the crash rate ratio
            inc2.calcIncidentFrequenciesCR(crashRateFrequenciesWithIMAP, demand2, true);
        }
        
        for (int incidentType = 0; incidentType < 5; incidentType++) {
	        inc1.setIncidentDistribution(incidentType, durationInfoNoIMAP[incidentType][0]);  // Sets distribution for incident type
	        inc1.setIncidentDuration(incidentType, durationInfoNoIMAP[incidentType][1]); // Sets the average duration for an incident type
	        inc1.setIncidentDurationStdDev(incidentType, durationInfoNoIMAP[incidentType][2]); // Sets the std deviation for an incident type
	        inc1.setIncidentDurMin(incidentType, durationInfoNoIMAP[incidentType][3]); // Sets the minimum duration for an incident type
	        inc1.setIncidentDurMax(incidentType, durationInfoNoIMAP[incidentType][4]); // Sets the maximum duration for an incident type
	        
	
	        inc2.setIncidentDistribution(incidentType, durationInfoWithIMAP[incidentType][0]);  // Sets distribution for incident type
	        inc2.setIncidentDuration(incidentType, durationInfoWithIMAP[incidentType][1]); // Sets the average duration for an incident type
	        inc2.setIncidentDurationStdDev(incidentType, durationInfoWithIMAP[incidentType][2]); // Sets the std deviation for an incident type
	        inc2.setIncidentDurMin(incidentType, durationInfoWithIMAP[incidentType][3]); // Sets the minimum duration for an incident type
	        inc2.setIncidentDurMax(incidentType, durationInfoWithIMAP[incidentType][4]); // Sets the maximum duration for an incident type
	        
        }
        

        // Assigning Demand and incidents to Seeds and scenario generators
        seed1.setSpecifiedGPDemand(demand1.getSpecifiedDemand());  // Saves GP Demand multipliers
        seed1.setWeekdayUsed(demand1.getActiveDays());  // Saves days active
        seed1.setGPIncidentCAF(inc1.getIncidentCAF());
        seed1.setGPIncidentDAF(inc1.getIncidentDAF());
        seed1.setGPIncidentSAF(inc1.getIncidentFFSAF());
        seed1.setGPIncidentLAF(inc1.getIncidentLAF());
        sg1.setDemandDataGP(demand1); // Setting Demand Data
        sg1.setGPIncidentData(inc1);  // Setting Incident Data
        sg1.includeGPWorkZones(false);  // Turning off work zones
        sg1.includeWeather(false);  // Turning off weather
        sg1.includeIncidentsGP(true);  // Turning on Incidents

        seed2.setSpecifiedGPDemand(demand2.getSpecifiedDemand());  // Saves GP Demand multipliers
        seed2.setWeekdayUsed(demand2.getActiveDays());  // Saves days active
        seed2.setGPIncidentCAF(inc2.getIncidentCAF());
        seed2.setGPIncidentDAF(inc2.getIncidentDAF());
        seed2.setGPIncidentSAF(inc2.getIncidentFFSAF());
        seed2.setGPIncidentLAF(inc2.getIncidentLAF());
        sg2.setDemandDataGP(demand2); // Setting Demand Data
        sg2.setGPIncidentData(inc2);  // Setting Incident Data
        sg2.includeGPWorkZones(false);  // Turning off work zones
        sg2.includeWeather(false);  // Turning off weather
        sg2.includeIncidentsGP(true);  // Turning on Incidents

        boolean scenariosCreated1 = sg1.generateScenarios();  // Creates the scenarios
        if (scenariosCreated1) {
        	seed1.setRLScenarios(sg1.getGPScenario(), sg1.getMLScenario(), sg1.getScenarioInfoList());
        } else {
        	System.out.println("Something went wrong generating before scenarios");
        }
        RLBatchRunDialog batchRunDialog1 = new RLBatchRunDialog(seed1, null);
        batchRunDialog1.setVisible(true);  // These two functions will run all scenarios with a progressbar

        boolean scenariosCreated2 = sg2.generateScenarios();  // Creates the scenarios
        if (scenariosCreated2) {
        	seed2.setRLScenarios(sg2.getGPScenario(), sg2.getMLScenario(), sg2.getScenarioInfoList());
        } else {
        	System.out.println("Something went wrong generating after scenarios");
        }
        RLBatchRunDialog batchRunDialog2 = new RLBatchRunDialog(seed2, null);
        batchRunDialog2.setVisible(true);
		
	}

	/**
	 * This dialog is used to perform batch run of scenarios
	 *
	 * @author Shu Liu
	 */
	private static class RLBatchRunDialog extends javax.swing.JDialog
	        implements PropertyChangeListener {

	    /**
	     * Invoked when task's progress property changes.
	     */
	    @Override
	    public void propertyChange(PropertyChangeEvent evt) {
	        if ("progress".equals(evt.getPropertyName())) {
	            int progress = (Integer) evt.getNewValue();
	            progressBar.setValue(progress);
	        }
	    }
	    
	    private javax.swing.JProgressBar progressBar;                 

	    private int returnStatus = RET_CANCEL;

	    /**
	     * A return status code - returned if Cancel button has been pressed
	     */
	    public static final int RET_CANCEL = 0;
	    /**
	     * A return status code - returned if OK button has been pressed
	     */
	    public static final int RET_OK = 1;

	    private final Seed seed;

	    //private final MainWindow mainWindow;

	    /**
	     * Creates new form NewOkCancelDialog
	     *
	     * @param seed seed to be analyzed
	     * @param mainWindow mainWindow for return message
	     */
	    public RLBatchRunDialog(Seed seed, Frame parent) {
	        super(parent, true);
	        initComponents();

	        //set starting position
	        this.setLocationRelativeTo(this.getRootPane());

	        this.seed = seed;
	        //this.mainWindow = mainWindow;

	        // Close the dialog when Esc is pressed
	        String cancelName = "cancel";
	        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
	        ActionMap actionMap = getRootPane().getActionMap();
	        actionMap.put(cancelName, new AbstractAction() {
	            public void actionPerformed(ActionEvent e) {
	                doClose(RET_CANCEL);
	            }
	        });

	        doRun();
	    }

	    /**
	     * Getter for return status
	     *
	     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
	     */
	    public int getReturnStatus() {
	        return returnStatus;
	    }
                        
	    private void initComponents() {

	        progressBar = new javax.swing.JProgressBar();

	        setTitle("Batch Run");
	        setResizable(false);
	        addWindowListener(new java.awt.event.WindowAdapter() {
	            public void windowClosing(java.awt.event.WindowEvent evt) {
	                closeDialog(evt);
	            }
	        });

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );

	        pack();
	    }                       

	    private void doRun() {
	        final int start;
	        final int end;

	        try {
	            start = 0;
	            end = seed.getValueInt(CEConst.IDS_NUM_SCEN);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Invalid scenario number",
	                    "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        class Task extends SwingWorker<Void, Void> {
	            /*
	             * Main task. Executed in background thread.
	             */
	            @Override
	            public Void doInBackground() {
	                //timing starts
	                long timingStart = new Date().getTime();

	                Thread thread = new Thread(new Runnable() {
	                    public void run() {
	                        for (int scen = start; scen <= end; scen++) {
	                            setProgress((scen - start) * 100 / (end - start));
	                            if (!seed.hasValidOutput(scen, -1)) {
	                                seed.singleRun(scen, -1);//, threadIndex);
	                            }
	                        }
	                    }
	                });

	                thread.start();

	                try {
	                    thread.join();
	                } catch (InterruptedException ex) {
	                    Logger.getLogger(RLBatchRunDialog.class.getName()).log(Level.SEVERE, null, ex);
	                }

	                //timing ends
	                long timingEnd = new Date().getTime();
	                System.out.println("Batch run finished. Time cost: "
	                        + (timingEnd - timingStart) + " ms");

	                return null;
	            }

	            /*
	             * Executed in event dispatching thread
	             */
	            @Override
	            public void done() {
	                progressBar.setString("Finish");
	                doClose(RET_OK);
	            }
	        }

	        try {
	            progressBar.setStringPainted(true);
	            progressBar.setString("Analyzing, please wait...");

	            Task task = new Task();
	            task.addPropertyChangeListener(this);
	            task.execute();

	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Error occured in batch run",
	                    "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    /**
	     * Closes the dialog
	     */
	    private void closeDialog(java.awt.event.WindowEvent evt) {                             
	        doClose(RET_CANCEL);
	    }                            

	    private void doClose(int retStatus) {
	        returnStatus = retStatus;
	        setVisible(false);
	        dispose();
	    }
	}

	
}
