package ir.radsense.raadconnectdemo;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.Hashtable;

public class Typefaces {
	public static final int IRAN_ULTRA_LIGHT = 1;
	public static final int IRAN_LIGHT = 2;
	public static final int IRAN_MEDIUM = 3;
	public static final int IRAN_BOLD = 4;

	private static final Hashtable<String, Typeface> cache = new Hashtable<>();

	public static Typeface get(Context context, String name) {
		synchronized (cache) {
			if (!cache.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(context.getAssets(),
						String.format("fonts/%s.ttf", name));
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}

	public static Typeface get(Context context, int font) {
		String fontName;
		switch (font) {
			case IRAN_ULTRA_LIGHT:
				fontName = "IRAN Sans UltraLight";
				break;
			case IRAN_LIGHT:
				fontName = "IRAN Sans Light";
				break;
			case IRAN_MEDIUM:
				fontName = "IRAN Sans Medium";
				break;
			case IRAN_BOLD:
				fontName = "IRAN Sans Bold";
				break;
			default:
				fontName = "IRAN Sans Light";
		}
		return get(context, fontName);
	}

	public static Typeface getDefault(Context context) {
		return get(context, IRAN_LIGHT);
	}

	public static void setTypeface(Context context, int font,  TextView... views) {
		for (TextView v : views) {
			v.setTypeface(Typefaces.get(context, font));
		}
	}
}