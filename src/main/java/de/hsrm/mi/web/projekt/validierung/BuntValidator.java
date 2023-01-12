package de.hsrm.mi.web.projekt.validierung;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BuntValidator implements ConstraintValidator<Bunt, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        if (value == "") return true;

        Pattern pattern = Pattern.compile("^#[0-9a-fA-F]+$");
        Matcher matcher = pattern.matcher(value);
        boolean matchFound = matcher.find();
        if(value.startsWith("#")) {
            if(matchFound) {
                String s1 = "", s2 = "", s3 = "";
                if (value.length() == 4) {
                    s1 = value.substring(1);
                    s2 = value.substring(2);
                    s3 = value.substring(3);
                }
                else if (value.length() == 7) {
                    s1 = value.substring(1, 3);
                    s2 = value.substring(3, 5);
                    s3 = value.substring(5);
                } else {
                    return false;
                }
                if (s1.equals(s2) || s1.equals(s3) || s2.equals(s3)) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }
}
