package thaumicenergistics.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import thaumicenergistics.gui.GuiPriority;
import thaumicenergistics.network.packet.AbstractClientPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketClientPriority
	extends AbstractClientPacket
{
	private static final byte MODE_SEND = 0;

	private int priority;

	@Override
	protected void readData( final ByteBuf stream )
	{
		if( this.mode == PacketClientPriority.MODE_SEND )
		{
			// Read the priority
			this.priority = stream.readInt();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void wrappedExecute()
	{
		// Ensure we have a player
		if( this.player == null )
		{
			return;
		}

		// Get the gui
		Gui gui = Minecraft.getMinecraft().currentScreen;

		// Ensure they are looking at the priority gui
		if( !( gui instanceof GuiPriority ) )
		{
			return;
		}

		if( this.mode == PacketClientPriority.MODE_SEND )
		{
			// Set the priority
			( (GuiPriority)gui ).onServerSendPriority( this.priority );
		}
	}

	@Override
	protected void writeData( final ByteBuf stream )
	{
		if( this.mode == PacketClientPriority.MODE_SEND )
		{
			// Write the priority
			stream.writeInt( this.priority );
		}
	}

	public PacketClientPriority createSendPriority( final int priority, final EntityPlayer player )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientPriority.MODE_SEND;

		// Set the priority
		this.priority = priority;

		return this;
	}

}
