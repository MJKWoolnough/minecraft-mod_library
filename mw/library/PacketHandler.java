package mw.library;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public abstract class PacketHandler implements IPacketHandler {

	private String	channel;

	public PacketHandler(String channel) {
		this.channel = channel;
	}

	abstract public void handlePacket(byte packetId, ByteArrayDataInput in, Player player);

	@Override
	public final void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
		this.handlePacket((byte) in.readUnsignedByte(), in, player);
		return;
	}

	public final void sendPacket(PacketData pd) {
		this.sendPacket(pd, false, null);
	}

	public final void sendPacket(PacketData pd, boolean toPlayer) {
		this.sendPacket(pd, toPlayer, null);
	}

	public final void sendPacket(PacketData pd, boolean toPlayer, Player player) {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = this.channel;
		packet.data = pd.toByteArray();
		packet.length = pd.size();
		if (toPlayer) {
			if (player == null) {
				PacketDispatcher.sendPacketToAllPlayers(packet);
			} else {
				PacketDispatcher.sendPacketToPlayer(packet, player);
			}
		} else {
			PacketDispatcher.sendPacketToServer(packet);
		}
	}
}
