package br.DroidLib;

public abstract class Constants
{

	public
	enum ETextures {
		None,
		Floor,
		Wall,
		Ceiling,
		Hero,
		Monters,
		Explosion
	};

	public static final int MILISSECONDS = 1;
	public static final int SECONDS = 1000*MILISSECONDS;
	public 	static final int MINUTES = 60*SECONDS;
	public static final int BASETILEHEIGHT = 42;
	public static final int BASETILEWIDTH = 54;
}
