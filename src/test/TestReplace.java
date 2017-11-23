package test;

public class TestReplace {
    public static void main(String[] args){
        String rawStr = "123.345.7";
        String replaceStr = rawStr.replace(".","");
        System.out.println(replaceStr);
        String replaceStr2 = replaceStr.replace(".","");
        System.out.println(replaceStr2);
    }
}
