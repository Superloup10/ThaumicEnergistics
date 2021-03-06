package thaumicenergistics.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import thaumicenergistics.gui.GuiEssentiaLevelEmitter;
import thaumicenergistics.network.packet.AbstractClientPacket;
import thaumicenergistics.registries.EnumCache;
import appeng.api.config.RedstoneMode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketClientEssentiaEmitter
	extends AbstractClientPacket
{
	private static final byte MODE_FULL_UPDATE = 0;

	private static final byte MODE_UPDATE_WANTED = 1;

	private static final byte MODE_UPDATE_REDSTONE = 2;

	private RedstoneMode redstoneMode;

	private long wantedAmount;

	@SideOnly(Side.CLIENT)
	@Override
	protected void wrappedExecute()
	{
		// Get the current screen being displayed to the user
		Gui gui = Minecraft.getMinecraft().currentScreen;

		// Ensure it is a GuiEssentiaLevelEmitter
		if( gui instanceof GuiEssentiaLevelEmitter )
		{

			switch ( this.mode )
			{
				case PacketClientEssentiaEmitter.MODE_FULL_UPDATE:
					// Full update
					( (GuiEssentiaLevelEmitter)gui ).onServerUpdateWantedAmount( this.wantedAmount );
					( (GuiEssentiaLevelEmitter)gui ).onServerUpdateRedstoneMode( this.redstoneMode );
					break;

				case PacketClientEssentiaEmitter.MODE_UPDATE_WANTED:
					// Update wanted amount
					( (GuiEssentiaLevelEmitter)gui ).onServerUpdateWantedAmount( this.wantedAmount );
					break;

				case PacketClientEssentiaEmitter.MODE_UPDATE_REDSTONE:
					// Update redstone mode
					( (GuiEssentiaLevelEmitter)gui ).onServerUpdateRedstoneMode( this.redstoneMode );
					break;
			}
		}
	}

	/**
	 * Creates a packet containing the emitter state.
	 * 
	 * @param redstoneMode
	 * @param wantedAmount
	 * @param player
	 * @return
	 */
	public PacketClientEssentiaEmitter createFullUpdate( final RedstoneMode redstoneMode, final long wantedAmount, final EntityPlayer player )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientEssentiaEmitter.MODE_FULL_UPDATE;

		// Set the redstone mode
		this.redstoneMode = redstoneMode;

		// Set the wanted amount
		this.wantedAmount = wantedAmount;

		return this;
	}

	public PacketClientEssentiaEmitter createRedstoneModeUpdate( final RedstoneMode redstoneMode, final EntityPlayer player )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientEssentiaEmitter.MODE_UPDATE_REDSTONE;

		// Set the redstone mode
		this.redstoneMode = redstoneMode;

		return this;
	}

	/**
	 * Create a packet to update a client with a new wanted amount.
	 * 
	 * @param wantedAmount
	 * @param player
	 * @return
	 */
	public PacketClientEssentiaEmitter createWantedAmountUpdate( final long wantedAmount, final EntityPlayer player )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientEssentiaEmitter.MODE_UPDATE_WANTED;

		// Set the wanted amount
		this.wantedAmount = wantedAmount;

		return this;
	}

	@Override
	public void readData( final ByteBuf stream )
	{
		switch ( this.mode )
		{
			case PacketClientEssentiaEmitter.MODE_FULL_UPDATE:
				// Read the redstone mode ordinal
				this.redstoneMode = EnumCache.AE_REDSTONE_MODES[stream.readInt()];

				// Read the wanted amount
				this.wantedAmount = stream.readLong();
				break;

			case PacketClientEssentiaEmitter.MODE_UPDATE_WANTED:
				// Read the wanted amount
				this.wantedAmount = stream.readLong();
				break;

			case PacketClientEssentiaEmitter.MODE_UPDATE_REDSTONE:
				// Read the redstone mode ordinal
				this.redstoneMode = EnumCache.AE_REDSTONE_MODES[stream.readInt()];
				break;
		}
	}

	@Override
	public void writeData( final ByteBuf stream )
	{
		switch ( this.mode )
		{
			case PacketClientEssentiaEmitter.MODE_FULL_UPDATE:
				// Write the redstone mode ordinal
				stream.writeInt( this.redstoneMode.ordinal() );

				// Write the wanted amount
				stream.writeLong( this.wantedAmount );
				break;

			case PacketClientEssentiaEmitter.MODE_UPDATE_WANTED:
				// Write the wanted amount
				stream.writeLong( this.wantedAmount );
				break;

			case PacketClientEssentiaEmitter.MODE_UPDATE_REDSTONE:
				// Write the redstone mode ordinal
				stream.writeInt( this.redstoneMode.ordinal() );

				break;
		}
	}
}
