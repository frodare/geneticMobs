package com.frodare.geneticmobs.dna;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

import com.frodare.geneticmobs.dna.GeneTraits.Trait;

public class GeneTraitsTest {

	@Test
	public void usedBiopoints() {
		GeneTraits g = new GeneTraits();

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ff00000000000000", 16));
		assertEquals(0, g.usedBiopoints());

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffff000000000000", 16));
		assertEquals(255, g.usedBiopoints());
	}

	@Test
	public void getTraitPropertyValue() {
		GeneTraits g = new GeneTraits();
		g.bytes[1] = 0;
		assertEquals(0, g.getTraitPropertyValue(1, 0, 100));

		g.bytes[1] = GeneTraits.compact(255);
		assertEquals(100, g.getTraitPropertyValue(1, 0, 100));

		g.bytes[1] = GeneTraits.compact(255);
		assertEquals(100, g.getTraitPropertyValue(1, 90, 100));

		g.bytes[1] = GeneTraits.compact(0);
		assertEquals(90, g.getTraitPropertyValue(1, 90, 100));

		g.bytes[1] = GeneTraits.compact(127);
		assertEquals(95, g.getTraitPropertyValue(1, 90, 100));

	}

	@Test
	public void init() {
		GeneTraits g = GeneTraits.fromLong(Long.parseUnsignedLong("ffff000000000000", 16));
		g.init();
		assertEquals(5, g.strength);
		assertEquals(5, g.speed);
		assertEquals(20, g.lifeSpan);
		assertEquals(6, g.health);
		assertEquals(0, g.regeneration);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ff00ff0000000000", 16));
		g.init();
		assertEquals(2, g.strength);
		assertEquals(15, g.speed);
		assertEquals(20, g.lifeSpan);
		assertEquals(6, g.health);
		assertEquals(0, g.regeneration);

	}

	@Test
	public void traitAnnotations() {
		GeneTraits g = new GeneTraits();
		Map<Field, Trait> traits = g.getTraitAnnotations();
		assertEquals(5, traits.size());
		for (Trait t : traits.values()) {
			if (t.byteIndex() == 1) {
				assertEquals(2, t.min());
				assertEquals(5, t.max());
			}
		}
	}

	@Test
	public void normalizeTo() {
		GeneTraits g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffff0000000000", 16));
		assertEquals(510, g.usedBiopoints());
		g.normalizeTo(10);
		assertEquals(10, g.usedBiopoints());
		assertEquals(5, g.bytes[1]);
		assertEquals(5, g.bytes[2]);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffffff00000000", 16));
		g.normalizeTo(90);
		assertEquals(90, g.usedBiopoints());
		assertEquals(30, g.bytes[1]);
		assertEquals(30, g.bytes[2]);
		assertEquals(30, g.bytes[3]);
		g.normalizeTo(1);
		assertEquals(1, g.usedBiopoints());
		assertEquals(0, g.bytes[1]);
		assertEquals(0, g.bytes[2]);
		assertEquals(0, g.bytes[3]);
		assertEquals(1, g.bytes[4]);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffffff10010000", 16));
		g.normalizeTo(100);
		assertEquals(100, g.usedBiopoints());
		assertEquals(33, g.bytes[1]);
		assertEquals(33, g.bytes[2]);
		assertEquals(33, g.bytes[3]);
		assertEquals(1, g.bytes[4]);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffffffffff0000", 16));
		g.normalizeTo(5);
		assertEquals(5, g.usedBiopoints());
		assertEquals(1, g.bytes[1]);
		assertEquals(1, g.bytes[2]);
		assertEquals(1, g.bytes[3]);
		assertEquals(1, g.bytes[4]);
		assertEquals(1, g.bytes[5]);

		g.normalizeTo(100);
		assertEquals(100, g.usedBiopoints());
		assertEquals(20, g.bytes[1]);
		assertEquals(20, g.bytes[2]);
		assertEquals(20, g.bytes[3]);
		assertEquals(20, g.bytes[4]);
		assertEquals(20, g.bytes[5]);
	}

	@Test
	public void fromLong() {
		GeneTraits g = GeneTraits.fromLong(Long.parseUnsignedLong("ffeeddccbbaa0000", 16));
		assertEquals("ff", Integer.toHexString(GeneTraits.expand(g.bytes[0])));
		assertEquals("ee", Integer.toHexString(GeneTraits.expand(g.bytes[1])));
		assertEquals("dd", Integer.toHexString(GeneTraits.expand(g.bytes[2])));
		assertEquals("cc", Integer.toHexString(GeneTraits.expand(g.bytes[3])));
		assertEquals("bb", Integer.toHexString(GeneTraits.expand(g.bytes[4])));
		assertEquals("aa", Integer.toHexString(GeneTraits.expand(g.bytes[5])));
	}

	@Test
	public void toLong() {
		GeneTraits g = new GeneTraits();
		assertEquals(0, g.toLong());

		g.bytes[0] = (byte) 0xff;
		assertEquals("ff00000000000000", Long.toHexString(g.toLong()));

		g.bytes[1] = (byte) 0xee;
		assertEquals("ffee000000000000", Long.toHexString(g.toLong()));

		g.bytes[2] = (byte) 0xdd;
		assertEquals("ffeedd0000000000", Long.toHexString(g.toLong()));

		g.bytes[3] = (byte) 0xcc;
		assertEquals("ffeeddcc00000000", Long.toHexString(g.toLong()));

		g.bytes[4] = (byte) 0xbb;
		assertEquals("ffeeddccbb000000", Long.toHexString(g.toLong()));

		g.bytes[5] = (byte) 0xaa;
		assertEquals("ffeeddccbbaa0000", Long.toHexString(g.toLong()));
	}

	@Test
	public void compactExpand() {
		byte b1 = (byte) 0xFF;
		byte b2 = GeneTraits.compact(GeneTraits.expand(b1));
		assertEquals(b1, b2);
	}

	@Test
	public void compactQuantize() {
		assertEquals(0xFF, compactExpand(0xFF));
		assertEquals(0xFF, compactExpand(0xFFFF));
		assertEquals(0x0F, compactExpand(0xFF0F));
	}

	protected int compactExpand(int i1) {
		return GeneTraits.expand(GeneTraits.compact(i1));
	}

}
