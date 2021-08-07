package com.fureniku.econ.blocks;

import net.minecraft.util.IStringSerializable;

public enum EnumMeta implements IStringSerializable {
	id0(0, "0"),
	id1(1, "1"),
	id2(2, "2"),
	id3(3, "3"),
	id4(4, "4"),
	id5(5, "5"),
	id6(6, "6"),
	id7(7, "7"),
	id8(8, "8"),
	id9(9, "9"),
	id10(10, "10"),
	id11(11, "11"),
	id12(12, "12"),
	id13(13, "13"),
	id14(14, "14"),
	id15(15, "15");
	
	private static final EnumMeta[] META_LOOKUP = new EnumMeta[values().length];
	private final int meta;
	private final String name;
	
	private EnumMeta(int meta, String name) {
		this.meta = meta;
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public int getMetadata() {
        return this.meta;
    }
	
	public static EnumMeta byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        
        return META_LOOKUP[meta];
    }
	
	static {
        for (EnumMeta type: values()) {
            META_LOOKUP[type.getMetadata()] = type;
        }
    }
}
