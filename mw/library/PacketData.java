package mw.library;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class PacketData extends DataOutputStream {

	public PacketData(int initialSize) {
		super(new ByteArrayOutputStream(initialSize));
	}

	public byte[] toByteArray() {
		return ((ByteArrayOutputStream) this.out).toByteArray();
	}
}
