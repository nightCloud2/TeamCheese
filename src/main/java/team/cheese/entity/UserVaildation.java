package team.cheese.entity;

public class UserVaildation {
    public static String vailed(String id){
        if(id.trim().length() != id.length()) {
            return "아이디에 띄어쓰기를 넣을 수 없습니다.";
        }
        if(hasMiddleSpace(id)) {
            return "아이디에 띄어쓰기를 넣을 수 없습니다.";
        }
        if(isNumeric(id)) {
            return "아이디에는 반드시 영어와 숫자가 섞여있어야 합니다.";
        }
        if(isAlphabetic(id)) {
            return "아이디에는 반드시 영어와 숫자가 섞여있어야 합니다.";
        }
        if(containsSpecialCharacter(id)) {
            return "아이디에 한글 또는 특수문자는 불가능합니다.";
        }

        return "사용 가능한 아이디입니다.";

    }
    public static boolean hasMiddleSpace(String str) {
        return str.matches(".+\\s+.+");
    }

    public static boolean isNumeric(String str) {
        return str.matches("^[0-9]+$");
    }

    public static boolean isAlphabetic(String str) {
        return str.matches("^[A-Za-z]+$");
    }

    public static boolean containsSpecialCharacter(String str) {
        return str.matches(".*[^A-Za-z0-9].*");
    }
}
