package ru.job4j.todo;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class EncodingUtils {

    public static void setConsoleEncodingUTF8() {
        PrintStream printStream = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.setOut(printStream);
    }
}
