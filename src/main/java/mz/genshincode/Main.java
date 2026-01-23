package mz.genshincode;

import mz.genshincode.data.GenshinDataAssets;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main
{
    public static void println(String file) throws IOException
    {
        try(DataInputStream input = new DataInputStream(Files.newInputStream(Paths.get(file))))
        {
            System.out.println(GenshinDataAssets.load(input));
        }
    }

    public static void main(String[] args) throws IOException
    {
        println("example.gia");
    }
}
