package com.bkav.edoc.sdk;

import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.edxml.mineutil.ExtractMime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ParserEdxml {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:/SendDocument.000.00.00.G22.cccb5533-2c1b-4770-8d12-71c8e5cacf2a.edxml");
        FileInputStream fileInputStream = new FileInputStream(file);
        Envelop envelop = ExtractMime.getInstance().parser(fileInputStream);
    }
}
