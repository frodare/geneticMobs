package com.frodare.geneticmobs.dna;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	@Trait(min = 2, max = 5, byteIndex = 1)
	public int strength;

	/**
	 * Byte 2
	 * 
	 * 0.5-1.5 m/s /10
	 */
	@Trait(min = 5, max = 15, byteIndex = 2)
	public int speed;

	/**
	 * Byte 3
	 * 
	 * 20-60 seconds
	 */
	@Trait(min = 20, max = 60, byteIndex = 3)
	public int lifeSpan;

	/**
	 * Byte 4
	 * 
	 * 6-13
	 */
	@Trait(min = 6, max = 13, byteIndex = 4)
	public int health;

	/**
	 * Byte 5
	 * 
	 * 0 - 4 points per 400 ticks (20 seconds)
	 */
	@Trait(min = 0, max = 4, byteIndex = 5)
	public int regeneration;

	protected byte[] bytes = new byte[8];

	public void init() {
		normalizeTo(BIOPOINTS);
		processAnnotations();
	}

	private void processAnnotations() {
		for(Entry<Field, Trait> e : getTraitAnnotations().entrySet()){
			try {
				Trait t = e.getValue();
				e.getKey().set(this, getTraitPropertyValue(t.byteIndex(), t.min(), t.max()));
			} catch (Exception ignore) {

			}
		}
	}

	protected Map<Field, Trait> getTraitAnnotations() {
		Map<Field, Trait> annotations = new HashMap<Field, GeneTraits.Trait>();
		for (Field f : this.getClass().getDeclaredFields()) {
			for (Annotation a : f.getAnnotations()) {
				if (a instanceof Trait) {
					annotations.put(f, (Trait)a);
				}
			}
		}
		return annotations;
	}
	
	protected int getTraitPropertyValue(int byteIndex, int min, int max) {
		
		int value = expand(bytes[byteIndex]);
		
		int range = max - min;
		
		return (int) Math.round(range * (value / 256d)) + min;
		
		
	}

	public static GeneTraits fromLong(long l) {
		GeneTraits g = new GeneTraits();
		ByteBuffer b = ByteBuffer.allocate(8);
		b.putLong(l);
		b.flip();
		b.get(g.bytes);
		return g;
	}

	public long toLong() {
		return ByteBuffer.wrap(bytes).getLong();
	}

	public void normalizeTo(int biopoints) {
		double s = usedBiopoints();

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Math.round(biopoints * (expand(bytes[i]) / s));
		}

		int i = usedBiopoints();
		bytes[4] -= (i - biopoints);
	}

	public int usedBiopoints() {
		int points = 0;
		for (int i = 1; i < bytes.length; i++) {
			points += expand(bytes[i]);
		}
		return points;
	}

	protected static byte compact(int i) {
		return (byte) i;
	}

	protected static int expand(byte b) {
		return (int) b & 0xFF;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Trait {
		int min();

		int max();

		int byteIndex();
	}
}
