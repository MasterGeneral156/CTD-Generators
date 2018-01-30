package com.themastergeneral.ctdgenerators.items;

import net.minecraft.creativetab.CreativeTabs;

import com.themastergeneral.ctdcore.item.CTDItem;

public class BaseItem extends CTDItem {
	public BaseItem(String name, String modid) {
		super(name, modid);
		this.setCreativeTab(CreativeTabs.MISC);
	}

}
