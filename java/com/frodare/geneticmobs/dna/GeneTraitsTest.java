package com.frodare.geneticmobs.dna;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeneTraitsTest {

	@Test
	public void usedBiopoints() {
		GeneTraits g = new GeneTraits();
		g.health = 255;
		g.lifeSpan = 255;
		g.regeneration = 255;
		g.speed = 255;
		g.strength = 255;
		assertEquals(1275, g.usedBiopoints());

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ff00000000000000", 16));
		assertEquals(0, g.usedBiopoints());

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffff000000000000", 16));
		assertEquals(255, g.usedBiopoints());
	}

	@Test
	public void normalizeTo() {
		GeneTraits g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffff0000000000", 16));
		assertEquals(510, g.usedBiopoints());
		g.normalizeTo(10);
		assertEquals(10, g.usedBiopoints());
		assertEquals(5, g.strength);
		assertEquals(5, g.speed);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffffff00000000", 16));
		g.normalizeTo(90);
		assertEquals(90, g.usedBiopoints());
		assertEquals(30, g.strength);
		assertEquals(30, g.speed);
		assertEquals(30, g.lifeSpan);
		g.normalizeTo(1);
		assertEquals(1, g.usedBiopoints());
		assertEquals(0, g.strength);
		assertEquals(0, g.speed);
		assertEquals(0, g.lifeSpan);
		assertEquals(1, g.health);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffffff10010000", 16));
		g.normalizeTo(100);
		assertEquals(100, g.usedBiopoints());
		assertEquals(33, g.strength);
		assertEquals(33, g.speed);
		assertEquals(33, g.lifeSpan);
		assertEquals(1, g.health);

		g = GeneTraits.fromLong(Long.parseUnsignedLong("ffffffffffff0000", 16));
		g.normalizeTo(5);
		assertEquals(5, g.usedBiopoints());
		assertEquals(1, g.strength);
		assertEquals(1, g.speed);
		assertEquals(1, g.lifeSpan);
		assertEquals(1, g.health);
		assertEquals(1, g.regeneration);

		g.normalizeTo(100);
		assertEquals(100, g.usedBiopoints());
		assertEquals(20, g.strength);
		assertEquals(20, g.speed);
		assertEquals(20, g.lifeSpan);
		assertEquals(20, g.health);
		assertEquals(20, g.regeneration);
	}

	@Test
	public void fromLong() {
		GeneTraits g = GeneTraits.fromLong(Long.parseUnsignedLong("ffeeddccbbaa0000", 16));
		assertEquals("ff", Integer.toHexString(g.bytes[0] * 0xff));
		assertEquals("ee", g.bytes[1]);

		assertEquals("ee", Integer.toHexString(g.bytes[1]));


		assertEquals("dd", Integer.toHexString(g.bytes[2]));
		assertEquals("cc", Integer.toHexString(g.bytes[3]));
		assertEquals("bb", Integer.toHexString(g.bytes[4] * 0xff));
		assertEquals("aa", Integer.toHexString(g.bytes[5] * 0xff));
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
