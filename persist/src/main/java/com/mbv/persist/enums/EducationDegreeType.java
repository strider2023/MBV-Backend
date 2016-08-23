package com.mbv.persist.enums;

/**
 * Created by arindamnath on 24/02/16.
 */
public enum EducationDegreeType {
    BACHELORS, MASTERS, DIPLOMA, PROFESSIONAL_EDUCATION;
    public static EducationDegreeType valueOf(int ordinal) {
        EducationDegreeType retVal = null;
        for (EducationDegreeType deviceType : EducationDegreeType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
