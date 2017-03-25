package com.sjsu.client;

import com.google.common.collect.Lists;
import com.sjsu.datastore.DummyData;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * Class is responsible for reading input from console
 */

public class InputReader {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine() throws IOException {
        return bufferedReader.readLine();
    }

    public static DateTime readDate() throws IOException, ParseException {
        String line = bufferedReader.readLine();

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yy");

        return dateTimeFormatter.parseDateTime(line);
    }

    public static DateTime readTime() throws IOException, ParseException {
        String line = bufferedReader.readLine();

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");

        return dateTimeFormatter.parseDateTime(line);
    }

    public static int readInt() throws IOException, ParseException {
        String line = bufferedReader.readLine();
        return Integer.valueOf(line);
    }

    public static String readPassword() throws IOException {
        Console console = System.console();
        if (console != null) {
            char[] chars = console.readPassword();
            return new String(chars);
        } else {
            return bufferedReader.readLine();
        }
    }

    public static String readAddressSelection() {
        String selectedOption = "";
        try {
            List<String> cityNames = Lists.newArrayList(DummyData.getGpsLocations().keySet());
            Collections.sort(cityNames);
            int i = 0;
            for (String cityName : cityNames) {
                i = i + 1;
                System.out.print(i + ". " + cityName + "\t|\t");
            }
            int selectedCityIndex = InputReader.readInt();
            selectedOption = cityNames.get(selectedCityIndex - 1);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return selectedOption;
    }

    public static void main(String aty[]) {

        try {
            System.out.println("Enter date (mm/dd/yy)");
            System.out.println("I read date as : " + InputReader.readDate());

            System.out.println("Enter time (HH:mm)");
            System.out.println("I read time as : " + InputReader.readTime());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
