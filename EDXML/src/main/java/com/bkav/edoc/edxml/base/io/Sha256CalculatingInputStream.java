package com.bkav.edoc.edxml.base.io;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Sha256CalculatingInputStream extends FilterInputStream {
    private final Hasher hasher = Hashing.sha256().newHasher();

    public Sha256CalculatingInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public int read() throws IOException {
        int i = this.in.read();
        if (i != -1) {
            this.hasher.putByte((byte)(i & 255));
        }

        return i;
    }

    public int read(byte[] arrayOfByte, int paramInt1, int paramInt2) throws IOException {
        int i = this.in.read(arrayOfByte, paramInt1, paramInt2);
        this.hasher.putBytes(arrayOfByte, paramInt1, paramInt2);
        return i;
    }

    public String getHashCode() {
        return BaseEncoding.base16().lowerCase().encode(this.hasher.hash().asBytes());
    }
}
