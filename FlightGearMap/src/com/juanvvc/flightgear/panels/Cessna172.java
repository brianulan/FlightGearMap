package com.juanvvc.flightgear.panels;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.juanvvc.flightgear.MyBitmap;
import com.juanvvc.flightgear.MyLog;
import com.juanvvc.flightgear.PlaneData;
import com.juanvvc.flightgear.instruments.CalibratableRotateSurface;
import com.juanvvc.flightgear.instruments.Instrument;
import com.juanvvc.flightgear.instruments.InstrumentType;
import com.juanvvc.flightgear.instruments.RotateSurface;
import com.juanvvc.flightgear.instruments.SlippingSurface;
import com.juanvvc.flightgear.instruments.StaticSurface;
import com.juanvvc.flightgear.instruments.Surface;
import com.juanvvc.flightgear.instruments.SwitchSurface;

/** Distributed instruments as in a Cessna 172 */
public class Cessna172 {
	
	public static Instrument createInstrument(InstrumentType type, Context context, float col, float row) {
		MyBitmap hand1 = new MyBitmap("misc1.png", 380, 10, 40, 270); // used in the ASI (long hand)
		MyBitmap hand2 = new MyBitmap("misc3.png", 393, 297, 40, 208); // used in the ALTIMETER (short hand)
		MyBitmap hand3 = new MyBitmap("misc2.png", 4, 200, 140, 24); // used in small instruments
		MyBitmap hand5 = new MyBitmap("nav4.png", 496, 258, 16, 244); // NAV1 and NAV2
		MyBitmap headings = new MyBitmap("nav2.png", -1, -1, -1, -1);
		MyBitmap fromto = new MyBitmap("nav6.png", -1, -1, -1, -1);
		MyBitmap switches = new MyBitmap("switches.png", -1, -1, -1, -1);
		
		switch (type) {

		case SPEED:
			return new Instrument(col, row, context, new Surface[] {
				new StaticSurface(new MyBitmap("asi2.png", -1, -1, -1, -1), 0, 0),
				new C172AirSpeedSurface(hand1, 236, 56, PlaneData.SPEED, 1, 256, 256, 0, 0, 200, 320)
			});
		case ATTITUDE:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("ati3.png", -1, -1, -1, -1), 0, 0),
					new C172AtiSurface(new MyBitmap("misc1.png", 0, 268, 376, 236), 70, 138),
					new RotateSurface(new MyBitmap("ati2.png", -1, -1, -1, -1), 23, 23, PlaneData.ROLL, 1, 256, 256, -180, 180, 180, -180),
					new StaticSurface(new MyBitmap("ati4.png", -1, -1, -1, -1), 0, 0)
				});
		case ALTIMETER:
			return new Instrument(col, row, context, new Surface[] {
					new CalibratableRotateSurface(new MyBitmap("alt3.png", -1, -1, -1, -1), 0, 0, "/instrumentation/altimeter/setting-inhg", 1, true, -1, 256, 256, 27.9f, 210, 31.5f, -150),
					new StaticSurface(new MyBitmap("alt1.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(hand2, 236, 56, PlaneData.ALTITUDE, 0.001f, 256, 256, 0, 0, 30, 3 * 360),
					new C172AltimeterLongHandSurface(hand1, 236, 56, PlaneData.ALTITUDE, 1, 256, 256, 0, 0, 10, 360)
				});
		case NAV1:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("nav6.png", -1, -1, -1, -1), 0, 0),
					new CalibratableRotateSurface(headings, 0, 0, "/instrumentation/nav/radials/selected-deg", 1, true, -1, 256, 256, 0, 0, 360, -360),
					new C172FromToGSSurface(fromto, 310, 210, PlaneData.NAV1_TO, PlaneData.NAV1_FROM, -1),
					new C172FromToGSSurface(fromto, 185, 210, -1, -1, PlaneData.GS1_INRANGE),
					new RotateSurface(hand5, 245, 120, PlaneData.NAV1_DEFLECTION, 1, 256, 120, -10, 25, 10, -25),
					new RotateSurface(hand5, 125, 266, PlaneData.GS1_DEFLECTION, 1, 125, 266, -1, -65, 1, -115),
					new StaticSurface(new MyBitmap("nav1.png", -1, -1, -1, -1), 0, 0)
				});	
		case NAV2:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("nav3.png", 0, 190, 320, 320), 256-160, 256-160),
					new StaticSurface(new MyBitmap("nav4.png", 0, 122, 244, 148), 256-122, 256-30),
					new CalibratableRotateSurface(new MyBitmap("nav2.png", -1, -1, -1, -1), 0, 0, "/instrumentation/nav[1]/radials/selected-deg", 1, true, -1, 256, 256, 0, 0, 360, -360),
					new C172FromToGSSurface(fromto, 310, 210, PlaneData.NAV2_TO, PlaneData.NAV2_FROM, -1),
					new RotateSurface(hand5, 245, 120, PlaneData.NAV2_DEFLECTION, 1, 256, 120, -10, 25, 10, -25),
					new StaticSurface(new MyBitmap("nav1.png", -1, -1, -1, -1), 0, 0)
				});
		case ADF:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("nav3.png", 0, 190, 320, 320), 256-160, 256-160),
					new CalibratableRotateSurface(headings, 0, 0, "/instrumentation/adf/rotation-deg", 1, true, -1, 256, 256, 0, 0, 360, -360),
					new RotateSurface(new MyBitmap("nav4.png", 248, 200, 32, 300), 236, 100, PlaneData.ADF_DEFLECTION, 1, 256, 256, 0, 0, 360, 360),
					new StaticSurface(new MyBitmap("nav1.png", -1, -1, -1, -1), 0, 0)
				});
		case HEADING:
			return new Instrument(col, row, context, new Surface[] {
					new CalibratableRotateSurface(new MyBitmap("hdg1.png", -1, -1, -1, -1), 0, 0, "/instrumentation/heading-indicator/indicated-heading-deg", 1, true, PlaneData.HEADING, 256, 256, 0, 0, 360, -360),
					new StaticSurface(new MyBitmap("hdg2.png", -1, -1, -1, -1), 0, 0)
				});
		case TURN_RATE:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("misc1.png", 0, 0, 300, 78), 100, 300),
					new RotateSurface(new MyBitmap("misc1.png", 312,9,48,62), 230, 300, PlaneData.SLIP, 1, 256, 0, -1, -25, 1, 25),
					new StaticSurface(new MyBitmap("trn1.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(new MyBitmap("misc1.png", 438, 0, 74, 320), 256-37, 256-160, PlaneData.TURN_RATE, 1, 256, 256, -4, -130, 4, -50),
				});
		case CLIMB_RATE:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("vsi1.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(hand1, 236, 56, PlaneData.CLIMB_RATE, 1, 256, 256, -2000, -265, 2000, 85)
				});
		case RPM:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("rpm1.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(hand1, 236, 56, PlaneData.RPM, 1, 256, 256, 0, -125, 3500, 125)
				});
		case FUEL:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("fuel1.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(hand3, 0, 218, PlaneData.FUEL1, 1, 0, 230, 0, 60, 26, -60),
					new RotateSurface(hand3, 288, 218, PlaneData.FUEL2, 1, 288, 230, 0, -240, 26, -120)
				});
		case OIL:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("oil1.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(hand3, 0, 218, PlaneData.OIL_TEMP, 1, 0, 230, 75, 60, 250, -60),
					new RotateSurface(hand3, 288, 218, PlaneData.OIL_PRESS, 1, 288, 230, 0, -250, 115, -210)
				});
		case BATT:
			// TODO: change this for EGT
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("battery-c172p.png", -1, -1, -1, -1), 0, 0),
					new RotateSurface(hand3, 0, 218, PlaneData.AMP, 1, 0, 230, -40, 55, 40, -55),
					new RotateSurface(hand3, 288, 218, PlaneData.VOLT, 1, 288, 230, 0, -235, 40, -125)
				});
		case TRIMFLAPS:
			return new Instrument(col, row, context, new Surface[] {
					new StaticSurface(new MyBitmap("trimflaps.png", -1, -1, -1, -1), 65, 10),
					new SlippingSurface(hand3, 0, PlaneData.ELEV_TRIM, -1, 260, 394, 1, 260, 26),
					new SlippingSurface(hand3, 180, PlaneData.FLAPS, 0, 220, 66, 1, 220, 434)
				});
		case SWITCHES:
			return new Instrument(col, row, context, new Surface[] {
					new SwitchSurface(switches, 0, 152, "/controls/anti-ice/pitot-heat", "PTO"),
					new SwitchSurface(switches, 128, 152, "/controls/lighting/nav-lights", "NAV"),
					new SwitchSurface(switches, 256, 0, "/controls/lighting/taxi-light", "TAX"),
					new SwitchSurface(switches, 256, 152, "/controls/lighting/beacon", "BCN"),
					new SwitchSurface(switches, 384, 0, "/controls/lighting/landing-lights", "LNG"),
					new SwitchSurface(switches, 384, 152, "/controls/lighting/strobe", "DTR"),
				});
		default:
			MyLog.w(Cessna172.class.getSimpleName(), "Instrument not available: " + type);
			return null;
		}
	}
	
	public static ArrayList<Instrument> getInstrumentPanel(Context context) {
		final ArrayList<Instrument> instruments = new ArrayList<Instrument>();
		instruments.add(Cessna172.createInstrument(InstrumentType.SPEED, context, 1, 0));
		instruments.add(Cessna172.createInstrument(InstrumentType.ATTITUDE, context, 2, 0));
		instruments.add(Cessna172.createInstrument(InstrumentType.ALTIMETER, context, 3, 0));
		instruments.add(Cessna172.createInstrument(InstrumentType.NAV1, context, 4, 0));
		instruments.add(Cessna172.createInstrument(InstrumentType.TURN_RATE, context, 1, 1));
		instruments.add(Cessna172.createInstrument(InstrumentType.HEADING, context, 2, 1));
		instruments.add(Cessna172.createInstrument(InstrumentType.CLIMB_RATE, context, 3, 1));
		instruments.add(Cessna172.createInstrument(InstrumentType.NAV2, context, 4, 1));

		instruments.add(Cessna172.createInstrument(InstrumentType.RPM, context, 1, 2));
		instruments.add(Cessna172.createInstrument(InstrumentType.ADF, context, 4, 2));
		
		instruments.add(Cessna172.createInstrument(InstrumentType.BATT, context, 0.2f, 0));
		instruments.add(Cessna172.createInstrument(InstrumentType.OIL, context, 0.2f, 1));
		instruments.add(Cessna172.createInstrument(InstrumentType.FUEL, context, 0.2f, 2));
		
		instruments.add(Cessna172.createInstrument(InstrumentType.SWITCHES, context, 2, 2));
		instruments.add(Cessna172.createInstrument(InstrumentType.TRIMFLAPS, context, 3, 2));
		return instruments;
	}
}

/** The airspeed instrument that I chose for C172 is not linear.
 * This surface rotates the handle in a not linear scale using a polynomial to adjust the curve
 * that was calculated using octave.
 */
class C172AirSpeedSurface extends RotateSurface {

	public C172AirSpeedSurface(MyBitmap bitmap,
			float x, float y,
			int pdIdx, float rscale,
			int rcx, int rcy,
			float min, float amin, float max, float amax) {
		super(bitmap, x, y, pdIdx, rscale, rcx, rcy, min, amin, max, amax);
	}

	@Override
	protected float getRotationAngle(PlaneData pd) {
		float v = pd.getFloat(pdIdx);
		if (v < 40) {
			// from 0 to 40: approximate to a linear behavior: 0=0º, 40=20º
			// this simplifies the polynomial and in any case, speeds under 40knots are uncommon
			return v/2;
		} else {
			// curve adjustment using octave:
			// x=[40:20:200]; 
			// y=[20,70,120,160,205,240,270,290,310]; (angles calculated with gimp on speed1.png)
			// p=polyfit(x,y,2);
			// ans = -6.1147e-03   3.3009e+00  -1.0452e+02
			return -0.0061147f * v * v + 3.3f * v - 104.5f;
		}
	}
}

/** The long hand of the altimeter shows the modulus of the altitude.
 * Modules is not directly sent by FlightGear, and this class
 * manages the calculus.
 */
class C172AltimeterLongHandSurface extends RotateSurface {

	public C172AltimeterLongHandSurface(MyBitmap bitmap,
			float x, float y,
			int pdIdx, float rscale,
			int rcx, int rcy,
			float min, float amin, float max, float amax) {
		super(bitmap, x, y, pdIdx, rscale, rcx, rcy, min, amin, max, amax);
	}
	
	protected float getRotationAngle(PlaneData pd) {
		float v = pd.getFloat(pdIdx);
		return (v % 1000) * 360 /1000;
	}
}

/** A special surface to draw the attitude. See usage example in the C172 */
class C172AtiSurface extends Surface {
	private Matrix matrix;

	public C172AtiSurface(MyBitmap bitmap, float x, float y) {
		super(bitmap, x, y);
		matrix = new Matrix();
	}
	@Override
	public void onDraw(Canvas c) {
		if (planeData == null || bitmap == null || bitmap.getScaledBitmap() == null) {
			return;
		}
		
		// draw pitch
		matrix.reset();
		float col = parent.getCol();
		float row = parent.getRow();
		float gridSize = parent.getGridSize();
		float scale = parent.getScale();
		// translate 23 /  pixels each 5 degrees
		float roll = planeData.getFloat(PlaneData.ROLL);
		if (roll > 60) {
			roll = 60;
		}
		float pitch = planeData.getFloat(PlaneData.PITCH);
		if (pitch > 45) {
			pitch = 45;
		}
		Bitmap b = bitmap.getScaledBitmap();
		matrix.postTranslate(((0.5f + col) * gridSize) * scale - b.getWidth() / 2, ((0.5f + row) * gridSize + pitch * (23 * gridSize/ 512) / 5) * scale - b.getHeight() / 2);
		matrix.postRotate(-roll, ((0.5f + col) * gridSize) * scale, ((0.5f + row) * gridSize) * scale);
		c.drawBitmap(b, matrix, null);
	}
}


/** A special surface to draw the flag from/to in a VOR. See an example in the C172 */
class C172FromToGSSurface extends Surface {
	private int nav_to, nav_from, gs; // position of this flags in PlaneData

	public C172FromToGSSurface(MyBitmap bitmap, float x, float y, int nav_to, int nav_from, int gs) {
		super(bitmap, x, y);
		this.nav_from = nav_from;
		this.nav_to = nav_to;
		this.gs = gs;
	}
	@Override
	public void onDraw(Canvas c) {
		if (planeData == null || bitmap == null || bitmap.getScaledBitmap() == null) {
			return;
		}
		
		final float col = parent.getCol();
		final float row = parent.getRow();
		final float realscale = parent.getScale() * parent.getGridSize();
		final float scale = parent.getScale();
		
		final int left = (int) ((col + relx) * realscale);
		final int top = (int) ((row + rely) * realscale);

		Bitmap b = bitmap.getScaledBitmap();
		
		if (gs == -1) {
			if (planeData.getBool(nav_to)) {
				c.drawBitmap(b,
						new Rect(0, 0, b.getWidth() / 3, b.getHeight()),
						new Rect(left, top, (int)(left + b.getWidth() / 3 * scale), (int)(top + b.getHeight() * scale)),
						null);
			} else if (planeData.getBool(nav_from)) {
				c.drawBitmap(b,
						new Rect(b.getWidth() / 3, 0, 2 * b.getWidth() / 3, b.getHeight()),
						new Rect(left, top, (int)(left + b.getWidth() / 3 * scale), (int)(top + b.getHeight() * scale)),
						null);
			}
		} else {
			if (planeData.getBool(gs)) {
				c.drawBitmap(b,
						new Rect(2 * b.getWidth() / 3, 0, b.getWidth(), b.getHeight()),
						new Rect(left, top, (int)(left + b.getWidth() / 3 * scale), (int)(top + b.getHeight() * scale)),
						null);
			}
		}
	}
}


