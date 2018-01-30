package com.themastergeneral.ctdgenerators.blocks.te;

import cofh.redstoneflux.api.IEnergyProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import com.themastergeneral.ctdgenerators.helpers.IEnergyInfo;

public class TileLavaGenerator extends TileEntity implements IEnergyProvider, ITickable 
{
	private int increasePerTick = 128;
	
	private int maxRF = 32000;
	private int currentRF;
	private int cooldown;
	
	private ItemStack[] inventory;
	private String customName;
	
	public TileLavaGenerator() {
		
	}
	
	public String getCustomName() {
		return customName;
	}
	
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

	    NBTTagList list = new NBTTagList();
	    nbt.setInteger("currentRF", this.currentRF);
	    nbt.setInteger("cooldown", this.cooldown);
	    nbt.setInteger("ipt", this.increasePerTick);

	    if (this.getCustomName() != null) {
	        nbt.setString("CustomName", this.getCustomName());
	    }
	    return super.writeToNBT(nbt);
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
	    this.currentRF = nbt.getInteger("currentRF");
	    this.cooldown = nbt.getInteger("cooldown");
	    this.increasePerTick = nbt.getInteger("ipt");

	    if (nbt.hasKey("CustomName", 8)) {
	        this.setCustomName(nbt.getString("CustomName"));
	    }
	    super.readFromNBT(nbt);
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.currentRF;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return maxRF;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		currentRF -= maxExtract;
		return maxExtract;
	}

	@Override
	public void update() {
		Block blockside = this.getBlockType().getBlockState().
		if (!this.world.isRemote) { //changed this
			if(canUse()) {
				if(this.cooldown <= 0) {
					if(this.inventory[0].getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
						this.cooldown = Config.CoalBlockCooldown;
						this.increasePerTick = Config.CoalRF;
					} else {
						this.cooldown = Config.CoalCooldown;
						this.increasePerTick = Config.CoalRF;
					}
					//this.decrStackSize(0, 1);
					this.inventory[0].stackSize -= 1; //added this
					if(this.inventory[0].stackSize <= 0) { //added this
						this.inventory[0] = null;
					}
				}
			}
			if(this.cooldown > 0) {
				this.cooldown--;
				if(this.currentRF < this.maxRF) {
					this.currentRF += this.increasePerTick;
				}
			}
			this.markDirty();
		}
	}
	
	private boolean canUse() {
		if(this.inventory[0] == null) {
			return false;
		}
		else {
			if(this.inventory[0].getItem() == Items.COAL || this.inventory[0].getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
				if(this.currentRF < this.maxRF) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	} //added these

}
