package com.juanvvc.flightgear;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Shows a small panel on the screen. This panel resize controls to the
 * available space.
 * 
 * @author juanvi
 * 
 */
public class PanelView extends View {

	/** Specifies the distribution type. */
	// TODO: change this to a enum
	public class Distribution {
		/** A 2x3 panel with simple instruments. */
		public static final int SIMPLE_VERTICAL_PANEL = 0;
		/** 6x1 panel with simple instruments. */
		public static final int HORIZONTAL_PANEL = 1;
		/** A 3x2 panel with simple instruments. */
		public static final int SIMPLE_HORIZONTAL_PANEL = 2;
		/** Show only the map. PanelView is not used. */
		public static final int ONLY_MAP = 3;
	};

	/** Scaled to be applied to all sizes on screen. */
	private float scale = 0;
	/** Constant TAG to be used during development. */
	private static final String TAG = "SmallPanelView";
	/** Plane data. */
	private PlaneData lastPlaneData = new PlaneData();
	/** The available instruments. */
	private ArrayList<Instrument> instruments;
	/** Number of columns in the panel. */
	private int cols;
	/** Number of rows in the panel. */
	private int rows;
	/** identifier of the current distribution. */
	private int distribution;
	/** The image set to load. Only configurable in the XML layout. */
	private String imageSet = null;

	/* Constructors */
	public PanelView(Context context) {
		super(context);
	}

	public PanelView(Context context, AttributeSet attrs) {
		super(context, attrs);

		instruments = new ArrayList<Instrument>();

		// Check the instrument distribution and bitmap quality
		// that was declared in the XML
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PanelView);
		imageSet = "low";
		int distribution = 0;
		for (int i = 0; i < a.getIndexCount(); i++) {
			switch (a.getIndex(i)) {
			case R.styleable.PanelView_distribution:
				distribution = a.getInt(i, 0);
				break;
			case R.styleable.PanelView_imgset:
				imageSet = a.getString(i);
			default:
			}
		}

		setDistribution(distribution);
	}

	/**
	 * Sets the distribution of this panel.
	 * 
	 * @param The
	 *            distribution to use
	 */
	public void setDistribution(int distribution) {

		for (Instrument i : instruments) {
			i.recycle();
		}

		instruments.clear();

		Context context = getContext();
		switch (distribution) {
		case Distribution.SIMPLE_VERTICAL_PANEL:
			cols = 2;
			rows = 3;
			instruments.add(new Attitude(0, 0, context));
			instruments.add(new TurnSlip(1, 0, context));
			instruments.add(new Speed(0, 1, context));
			instruments.add(new RPM(1, 1, context));
			instruments.add(new Altimeter(0, 2, context));
			instruments.add(new ClimbRate(1, 2, context));
			break;
		case Distribution.SIMPLE_HORIZONTAL_PANEL:
			cols = 3;
			rows = 2;
			instruments.add(new RPM(0, 0, context));
			instruments.add(new Attitude(1, 0, context));
			instruments.add(new TurnSlip(2, 0, context));
			instruments.add(new Speed(0, 1, context));
			instruments.add(new Altimeter(1, 1, context));
			instruments.add(new ClimbRate(2, 1, context));
			break;
		case Distribution.HORIZONTAL_PANEL:
			cols = 6;
			rows = 1;
			instruments.add(new Attitude(0, 0, context));
			instruments.add(new TurnSlip(1, 0, context));
			instruments.add(new Speed(2, 0, context));
			instruments.add(new RPM(3, 0, context));
			instruments.add(new Altimeter(4, 0, context));
			instruments.add(new ClimbRate(5, 0, context));
		default: // this includes Distribution.NO_MAP
		}
		
		
		// load the instruments. This could be in a different thread, but IN MY
		// DEVICES, loading does not take long
		for (Instrument i : instruments) {
			try {
				i.loadImages(imageSet);
			} catch (Exception e) {
				myLog.w(TAG,
						"Cannot load instrument: " + myLog.stackToString(e));
			}
		}
		
		this.rescaleInstruments();
		
		this.distribution = distribution;
	}
	
	/** @return The currently displayed distribution. */
	public int getDistribution() {
		return this.distribution;
	}

	/**
	 * Rescale bitmaps. Call after a new size is detected and at the beginning
	 * of the execution.
	 */
	private void rescaleInstruments() {
		if (getWidth() > 0) {
			// scale to match the available size. All instrumewnts should be
			// visible.
			scale = Math.min(
					1.0f * getWidth() / (cols * Instrument.getGridSize()),
					1.0f * getHeight()/ (rows * Instrument.getGridSize()));

			// prevent spurious scales
			// if (Math.abs(scale - 1) < 0.1) {
			// scale = 1;
			// }

			myLog.d(TAG, "Scale: " + scale);
			for (Instrument i : instruments) {
				i.setScale(scale);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rescaleInstruments();
	}

	/**
	 * @param pd
	 *            The last received PlaneData
	 */
	public void setPlaneData(PlaneData pd) {
		this.lastPlaneData = pd;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(distribution == Distribution.ONLY_MAP) {
			return;
		}
		
		super.onDraw(canvas);

		for (Instrument i : instruments) {
			i.onDraw(canvas, lastPlaneData);
		}
	}
}
