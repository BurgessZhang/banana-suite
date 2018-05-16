package com.burgess.banana.common.serizlizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.serizlizer
 * @file BananaFSTSerializer.java
 * @time 2018-05-16 16:32
 * @desc fst序列化
 */
public class BananaFSTSerializer implements BananaSerializer {

    @Override
    public String name() {
        return "fst";
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = null;
        FSTObjectOutput fout = null;
        try {
            out = new ByteArrayOutputStream();
            fout = new FSTObjectOutput(out);
            fout.writeObject(obj);
            fout.flush();
            return out.toByteArray();
        } finally {
            if(fout != null)
                try {
                    fout.close();
                } catch (IOException e) {}
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws IOException {
        if(bytes == null || bytes.length == 0)
            return null;
        FSTObjectInput in = null;
        try {
            in = new FSTObjectInput(new ByteArrayInputStream(bytes));
            return in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if(in != null)
                try {
                    in.close();
                } catch (IOException e) {}
        }
    }
}
