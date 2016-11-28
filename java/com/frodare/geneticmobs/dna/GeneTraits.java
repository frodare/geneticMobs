package com.frodare.geneticmobs.dna;

import java.nio.ByteBuffer;

/**
 * Traits Gene Byte Format (single java long)
 * 
 * byte 0 DNA version byte 1 Strength (2-5 hit points) affects size of mob byte
 * 2 Speed (0.5-1.5 m/s) byte 3 Life Span (10-40 seconds) byte 4 Health (6-13)
 * byte 5 Regeneration (1-4) byte 6 byte 7
 * 
 * @author frodare
 *
 */
public class GeneTraits {

	public static final int BIOPOINTS = 255;

	/**
	 * Byte 0
	 * 
	 * DNA Version
	 */
	public int version;

	/**
	 * Byte 1
	 * 
	 * The strength of the mob. This value affects the size of the mob and its
	 * attack damage.
	 * 
	 * Hitpoints: 2-5
	 */
	public int strength;

	/**
	 * Byte 2
	 * 
	 * 0.5-1.5 m/s
	 */
	public int speed;

	/**
	 * Byte 3
	 * 
	 * 20-60 seconds
	 */
	public int lifeSpan;

	/**
	 * Byte 4
	 * 
	 * 6-13
	 */
	public int health;

	/**
	 * Byte 5
	 * 
	 * 0 - 4 points per 400 ticks (20 seconds)
	 */
	public int regeneration;

	protected byte[] bytes = new byte[8];

	public static GeneTraits fromLong(long l) {
		GeneTraits g = new GeneTraits();
		ByteBuffer b = ByteBuffer.allocate(8);
		b.putLong(l);
		g.bytes = b.array();
		/*
		 * g.version = expand(b.get(0)); g.strength = expand(b.get(1)); g.speed
		 * = expand(b.get(2)); g.lifeSpan = expand(b.get(3)); g.health =
		 * expand(b.get(4)); g.regeneration = expand(b.get(5));
		 */
		g.normalizeTo(BIOPOINTS);
		return g;
	}

	public long toLong() {
		/*
		 * byte[] a = new byte[8]; a[0] = compact(version); a[1] =
		 * compact(strength); a[2] = compact(speed); a[3] = compact(lifeSpan);
		 * a[4] = compact(health); a[5] = compact(regeneration);
		 */
		return ByteBuffer.wrap(bytes).getLong();
	}

	public void normalizeTo(int biopoints) {
		double s = usedBiopoints();

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Math.round(biopoints * (bytes[i] / s));
		}

		/*
		 * health = (int) Math.round(biopoints * (health / s)); strength = (int)
		 * Math.round(biopoints * (strength / s)); speed = (int)
		 * Math.round(biopoints * (speed / s)); lifeSpan = (int)
		 * Math.round(biopoints * (lifeSpan / s)); regeneration = (int)
		 * Math.round(biopoints * (regeneration / s));
		 */

		int i = usedBiopoints();
		/* health -= (i - biopoints); */
		bytes[4] -= (i - biopoints);
	}

	public int usedBiopoints() {
		int points = 0;
		for (byte b : bytes) {
			points += b;
		}
		return points;

		// return strength + speed + lifeSpan + health + regeneration;
	}


	protected static byte compact(int i) {
		return (byte) i;
	}

	protected static int expand(byte b) {
		return (int) b & 0xFF;
	}

}
