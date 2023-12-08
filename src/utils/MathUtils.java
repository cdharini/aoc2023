package utils;

import java.util.List;

public class MathUtils {
    public static long getGCD(long num1, long num2)
    {
        if (num2 == 0)
            return num1;
        return getGCD(num2, num1 % num2);
    }

    public static long getLCM(List<Long> arr)
    {
        long lcm = arr.get(0);
        for (int i = 1; i < arr.size(); i++) {
            long num1 = lcm;
            long num2 = arr.get(i);
            long gcd_val = getGCD(num1, num2);
            lcm = (num1 * num2) / gcd_val;
        }
        return lcm;
    }
}
