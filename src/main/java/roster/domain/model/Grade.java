package roster.domain.model;

public enum Grade {
//    INFANT_TODDLER("IT"),
//    PRE_SCHOOL("PR"),
//    PRE_KINDERGARTEN("PK"),
//    TRANSITIONAL_KINDERGARTEN("TK"),
//    KINDERGARTEN("KG"),
    YEAR_1("1"),
    YEAR_2("2"),
    YEAR_3("3"),
    YEAR_4("4"),
    YEAR_5("5"),
    YEAR_6("6"),
    YEAR_7("7"),
    YEAR_8("8"),
    YEAR_9("9"),
    YEAR_10("10"),
    YEAR_11("11"),
    YEAR_12("12"),
    YEAR_13("13");
//    POST_SECONDARY("PS"),
//    UNGRADED("UG"),
//    OTHER("Other"),
//    INVALID_GRADE("INVALID");

    private final String gradeValue;

    private Grade(String gradeValue) {
        this.gradeValue = gradeValue;
    }

    public String getGradeValue() {
        return gradeValue;
    }
}
