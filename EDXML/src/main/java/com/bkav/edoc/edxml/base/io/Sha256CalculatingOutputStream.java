package com.bkav.edoc.edxml.base.io;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Sha256CalculatingOutputStream extends FilterOutputStream {
    private final Hasher hasher = Hashing.sha256().newHasher();

    public Sha256CalculatingOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void write(byte[] arrayOfByte, int value1, int value2) throws IOException {
        this.out.write(arrayOfByte, value1, value2);
        this.hasher.putBytes(arrayOfByte, value1, value2);
    }

    public void write(int value) throws IOException {
        this.out.write(value);
        this.hasher.putByte((byte)(value & 255));
    }

    public void close() throws IOException {
        super.close();
    }

    public String getHashCode() {
        return BaseEncoding.base16().lowerCase().encode(this.hasher.hash().asBytes());
    }
}
