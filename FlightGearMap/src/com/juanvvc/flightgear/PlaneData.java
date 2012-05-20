package com.juanvvc.flightgear;

import java.util.Date;

/** Models the data that FlilghtGear sent.
 * @author juanvi
 *
 */
public class PlaneData {
	
	String[] data;
	private Date date = new Date();
	private FGFSConnection conn;
	
	public PlaneData(FGFSConnection c) {
		conn = c;
		data = null;
	}
	
	public void parse(final String input) {
		// strip string with new line
		String realInput = input.substring(0, input.indexOf("\n"));
		data = realInput.split(":");
		
		date = new Date();
		
		// check that we have the desired number of parameters
		// just read the last data. If throws IndexOutOfBounds, the
		// other extreme is sending wrong data
		getFloat(NAV2_DEFLECTION);
	}
	
	public static final int SPEED = 0; // speed, in knots
	public static final int RPM = 1; // RPM
	public static final int HEADING_MOV = 2; // REAL heading, in degrees
	public static final int ALTITUDE = 3; // altitude, in feet, according to the instruments
	public static final int CLIMB_RATE = 4; // rate of climb, in feet per second
	public static final int PITCH = 5; // pitch, in degrees
	public static final int ROLL = 6; // roll, in degrees
	public static final int LATITUDE = 7; // latitude, in degrees
	public static final int LONGITUDE = 8; // longitude, in degrees
	public static final int SECONDS = 9; // seconds from GMT midnight
	public static final int TURN_RATE = 10; // turn rate, in turns per 2min
	public static final int SLIP = 11; // slip skid, in ??
	public static final int HEADING = 12; // Heading in degrees, according to the instruments
	public static final int FUEL1 = 13; // fuel in first tank, in us gals
	public static final int FUEL2 = 14; // fuel in second tank, in us gals
	public static final int OIL_PRESS = 15; // oil pressure in psi
	public static final int OIL_TEMP = 16; // oil temperature in degf
	public static final int AMP = 17; // amperes
	public static final int VOLT = 18; // voltage
	public static final int NAV1_RAD_SELECTED = 19; // selected radial in NAV1
	public static final int NAV1_TO = 20; // true if the to flag is set in NAV1
	public static final int NAV1_FROM = 21; // true if the from flag is set in NAV1
	public static final int NAV1_DEFLECTION = 22; // needle deflection in NAV1
	public static final int NAV2_RAD_SELECTED = 23; // selected radial in NAV2
	public static final int NAV2_TO = 24; // true if the to flag is set in NAV2
	public static final int NAV2_FROM = 25; // true if the from flag is set in NAV2
	public static final int NAV2_DEFLECTION = 26; // needle deflection in NAV2

	
	public int getInt(int i) {
		if (data == null) {
			return 0;
		}
		return new Integer(data[i]).intValue();
	}
	
	public float getFloat(int i) {
		if (data == null) {
			return 0;
		}
		return new Float(data[i]).floatValue();
	}
	
	public String getString(int i) {
		if (data == null) {
			return "";
		}
		return data[i];
	}
	
	public boolean getBool(int i) {
		if (data == null) {
			return false;
		}
		return data[i].equals("1");
	}
	
	public Date getDate() {
		return date;
	}
	
	public FGFSConnection getConnection() {
		return conn;
	}
	
	public boolean hasData() {
		return data != null;
	}
}