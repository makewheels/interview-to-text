package com.github.makewheels.interviewtotext.util;

import com.github.makewheels.interviewtotext.bean.Sentence;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUtil {
    private static String addZero(String str) {
        String[] split = str.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        StringBuilder stringBuilder = new StringBuilder(5);
        if (hour <= 9) {
            stringBuilder.append("0");
        }
        stringBuilder.append(hour);
        stringBuilder.append(":");
        if (minute <= 9) {
            stringBuilder.append("0");
        }
        stringBuilder.append(minute);
        return stringBuilder.toString();
    }

    public static void writeFile(String[] line, File file) {
        List<Sentence> sentenceList = new ArrayList<>(line.length);
        for (String str : line) {
            String time = StringUtils.substringBetween(str, "[", "]");
            String content = str.substring(time.length() + 2).trim();
            if (content.endsWith("。")) {
                content = content.substring(0, content.length() - 1);
            }
            String[] timeSplit = time.split(",");
            String start = timeSplit[0].split("\\.")[0];
            String end = timeSplit[1].split("\\.")[0];
            int person = Integer.parseInt(timeSplit[2]);

            start = addZero(start);
            end = addZero(end);

            Sentence sentence = new Sentence();
            sentence.setStartTimeString(start);
            sentence.setEndTimeString(end);
            sentence.setPerson(person);
            sentence.setContent(content);
            sentenceList.add(sentence);
        }

        List<String> fileLines = new ArrayList<>(sentenceList.size());
        for (Sentence sentence : sentenceList) {
            String mdLine;
            if (sentence.getPerson() == 0) {
                mdLine = "<font color=\"green\">**[" + sentence.getStartTimeString() + "]** "
                        + sentence.getContent() + "</font>";
            } else {
//                mdLine = "<div align=\"right\"><font color= \"blue\">[**" + sentence.getStartTimeString() + "**] "
//                        + sentence.getContent() + "</font></div>";
                mdLine = "<font color= \"blue\">**[" + sentence.getStartTimeString() + "]** "
                        + sentence.getContent() + "</font>";
            }
            fileLines.add(mdLine);
        }
        for (String mdLine : fileLines) {
            System.out.println(mdLine);
        }
        try {
            FileUtils.writeLines(file, fileLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
