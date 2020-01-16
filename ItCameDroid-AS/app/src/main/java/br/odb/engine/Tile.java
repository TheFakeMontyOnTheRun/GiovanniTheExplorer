package br.odb.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Tile extends br.odb.engine.Bitmap {
	private int tipo;

	public Tile(Resources resources, int resId) {
		super(resources, resId);
		// TODO Auto-generated constructor stub
		tipo = resId;
	}

	public Tile(int baseTypeId, Bitmap androidBitmap) {
		// TODO Auto-generated constructor stub
		super(androidBitmap);
		tipo = baseTypeId;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setTile(int type, android.graphics.Bitmap tile) {
		// TODO Auto-generated method stub
		super.setAndroidBitmap(tile);
		tipo = type;
	}

}
