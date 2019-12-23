package com.railwaymodelingsystem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {
    public static boolean checkBlockName(String blockName){
        Pattern p = Pattern.compile("^[а-яА-Я0-9]{1,6}$");
        Matcher m = p.matcher(blockName);
        return m.matches();
    }

    public static boolean checkWayNumber(String wayNumber){
        Pattern p = Pattern.compile("^[0-9]{1,2}$");
        Matcher m = p.matcher(wayNumber);
        return m.matches();
    }
}
