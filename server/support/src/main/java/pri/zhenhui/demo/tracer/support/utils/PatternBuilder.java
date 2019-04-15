package pri.zhenhui.demo.tracer.support.utils;


import java.util.ArrayList;
import java.util.regex.Pattern;

public class PatternBuilder {

    private final ArrayList<String> fragments = new ArrayList<>();

    public PatternBuilder optional() {
        return optional(1);
    }

    public PatternBuilder optional(int count) {
        fragments.add(fragments.size() - count, "(?:");
        fragments.add(")?");
        return this;
    }

    public PatternBuilder expression(String s) {
        s = s.replaceAll("\\|$", "\\\\|"); // special case for delimiter

        fragments.add(s);
        return this;
    }

    public PatternBuilder text(String s) {
        fragments.add(s.replaceAll("([\\\\\\.\\[\\{\\(\\)\\*\\+\\?\\^\\$\\|])", "\\\\$1"));
        return this;
    }

    public PatternBuilder number(String s) {
        s = s.replace("dddd", "d{4}").replace("ddd", "d{3}").replace("dd", "d{2}");
        s = s.replace("xxxx", "x{4}").replace("xxx", "x{3}").replace("xx", "x{2}");

        s = s.replace("d", "\\d").replace("x", "[0-9a-fA-F]").replaceAll("([\\.])", "\\\\$1");
        s = s.replaceAll("\\|$", "\\\\|").replaceAll("^\\|", "\\\\|"); // special case for delimiter

        fragments.add(s);
        return this;
    }

    public PatternBuilder any() {
        fragments.add(".*?");
        return this;
    }

    public PatternBuilder binary(String s) {
        fragments.add(s.replaceAll("(\\p{XDigit}{2})", "\\\\$1"));
        return this;
    }

    public PatternBuilder or() {
        fragments.add("|");
        return this;
    }

    public PatternBuilder groupBegin() {
        return expression("(?:");
    }

    public PatternBuilder groupEnd() {
        return expression(")");
    }

    public PatternBuilder groupEnd(String s) {
        return expression(")" + s);
    }

    public Pattern compile() {
        return Pattern.compile(toString(), Pattern.DOTALL);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String fragment : fragments) {
            builder.append(fragment);
        }
        return builder.toString();
    }

}
