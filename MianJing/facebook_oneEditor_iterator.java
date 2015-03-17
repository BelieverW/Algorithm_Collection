发信人: yuxrose (鱼香肉丝), 信区: JobHunting
标  题: FB这道店面题怎么做？听说挂了很多人...
发信站: BBS 未名空间站 (Wed Mar 11 04:18:58 2015, 美东)

在一亩三分地上看到的，听说挂了很多人。咱板上牛人多，发上来和大家讨论一下呀
class IntFileIterator {
  boolean hasNext();
  int next();
}

class{
  public boolean isDistanceZeroOrOne(IntFileIterator a, IntFileIterator b)；

}
// return if the distance between a and b is at most 1..
// Distance: minimum number of modifications to make a=b
// Modification:
//   1. change an int in a
//   2. insert an int to a
//   3. remove an int from a

这题就是one edit distance的变形题，难点在于给的Iterator，事先不知道两个file
的长度，也不允许用extra space（所以不能转成两个string再比），那么一个个往前
跑的时候就得三种情况都考虑。。。。



只用constant内存的DP解法，自测能过所有test case

    public boolean isDistanceZeroOrOne(IntFileIterator a, IntFileIterator b)
{
        int up = 1;
        int left = 1;
        int diagnal = 0;
        int dist = 0;
        int currA = 0;
        int currB = 0;
        int prevA = 0;
        int prevB = 0;
       
        if (a.hasNext() && b.hasNext()) {
            prevA = a.next();
            prevB = b.next();
            if (prevA == prevB) {
                dist = 0;
            } else {
                dist = 1;
            }
        }

        while (a.hasNext() || b.hasNext()) {
            diagnal = dist;
            if (a.hasNext()) { // a has more numbers
                currA = a.next();
                up = Math.min(dist + 1, up + matchCost(currA, prevB)); //
insert into a, or match a, b
                if (b.hasNext()) { //b has more numbers
                    currB = b.next();
                    left = Math.min(dist + 1, left + matchCost(currB, prevA)
); //delete head of a, or match a, b
                    dist = Math.min(up + 1, left + 1); //delete or insert
                    dist = Math.min(diagnal + matchCost(currA, currB), dist)
; //match
                } else { //b is out of numbers
                    ++left;
                    ++dist; //delete head of a
                    break;
                }
            } else { // a is out of numbers, b has to have more numbers
                currB = b.next();
                left = Math.min(dist + 1, left + matchCost(currB, prevA)); /
/delete head of a, or match a, b
                ++up;
                ++dist;
                break;
            }
           
            if (dist > 1 && up > 1 && left > 1) {
                return false;
            }
           
            prevA = currA;
            prevB = currB;
        }
       
        dist = Math.min(dist, up);
        dist = Math.min(dist, left);
       
        while (a.hasNext()) {
            a.next();
            ++dist;
        }

        while (b.hasNext()) {
            b.next();
            ++dist;
        }

        return dist <= 1;
    }



//////////////////////////////////

JUnit test 代码

public class SolutionTest {

    @Test
    public void test() {
        int[] a;
        int[] b;
        boolean expect;
        Solution s = new Solution();

        //both empty
        a = new int[] {};
        b = new int[] {};
        expect = true;
        testSolution(s, a, b, expect);

        //empty and 1 element
        a = new int[] {};
        b = new int[] {1};
        expect = true;
        testSolution(s, a, b, expect);

        //empty and 2 element all 0s
        a = new int[] {};
        b = new int[] {0, 0};
        expect = false;
        testSolution(s, a, b, expect);

        //empty and 2 element
        a = new int[] {};
        b = new int[] {1, 2};
        expect = false;
        testSolution(s, a, b, expect);

        //one element and 2 elements, miss last element
        a = new int[] {1};
        b = new int[] {1, 2};
        expect = true;
        testSolution(s, a, b, expect);

        //one element and 2 elements, miss first element
        a = new int[] {2};
        b = new int[] {1, 2};
        expect = true;
        testSolution(s, a, b, expect);

        //one element and 2 elements, no equal elements
        a = new int[] {3};
        b = new int[] {1, 2};
        expect = false;
        testSolution(s, a, b, expect);

        //different element in the middle
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {1, 2, 3, 4, 5, 4, 8, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //different element in the middle, but equals next element
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {1, 2, 3, 4, 5, 8, 8, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //different element at the end
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 32};
        expect = true;
        testSolution(s, a, b, expect);

        //different element at the end, but equals previous element
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 7};
        expect = true;
        testSolution(s, a, b, expect);

        //different element at the beginning, but equals next element
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {2, 2, 3, 4, 5, 9, 8, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //different element at the beginning, but equals next element
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {22, 2, 3, 4, 5, 9, 8, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //miss first element
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b = new int[] {2, 3, 4, 5, 9, 8, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //miss last element
        a = new int[] {1, 2, 3, 4, 5, 9, 8, 7};
        b = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //one difference and miss one
        a = new int[] {1, 2, 3, 4, 5, 4, 7, 6};
        b = new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        expect = false;
        testSolution(s, a, b, expect);

        //two differences and miss one
        a =  new int[] {1, 2, 3, 4, 5, 4, 8, 7, 1, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        expect = false;
        testSolution(s, a, b, expect);

        //one difference and miss two
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 4, 8, 7, 6, 3, 2};
        expect = false;
        testSolution(s, a, b, expect);

        //miss one
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //miss one
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 1, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //miss one, and equals next
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 7, 6};
        expect = true;
        testSolution(s, a, b, expect);

        //miss one, and equals next
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 7};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 1, 7};
        expect = true;
        testSolution(s, a, b, expect);

        //miss two
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6, 3, 2};
        expect = false;
        testSolution(s, a, b, expect);

        //miss two
        a =  new int[] {1, 2, 3, 4, 5, 9, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 1, 6};
        expect = false;
        testSolution(s, a, b, expect);

        //miss three
        a =  new int[] {1, 2, 3, 4, 5, 9, 7, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6, 3, 2};
        expect = false;
        testSolution(s, a, b, expect);

        //one extra, miss two
        a =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 1, 6};
        b =  new int[] {1, 2, 3, 4, 5, 9, 8, 7, 6, 3, 2};
        expect = false;
        testSolution(s, a, b, expect);
    }
   
    private void testSolution(Solution s, int[] a1, int[] b1, boolean expect
) {
        IntFileIterator a = new IntFileIterator(a1);
        IntFileIterator b = new IntFileIterator(b1);
        a.print();
        b.print();
        boolean result = s.isDistanceZeroOrOne(a, b);
        System.out.println("expected: " + expect + ": got: " + result);
        System.out.println("");
        assertEquals(expect, result);
       
       
        b = new IntFileIterator(a1);
        a = new IntFileIterator(b1);
        a.print();
        b.print();
        result = s.isDistanceZeroOrOne(a, b);
        System.out.println("expected: " + expect + ": got: " + result);
        System.out.println("");
        assertEquals(expect, result);

    }

}


///////////////////////
我来给你试着解释一下。这个题说到底是一个有限自动机的问题。我们可以定义一下四
种状态：

0： 到目前为止A和B的字母都相同。
1： 到目前为止A和B的字母除了一个以外都相同。
2： 到目前为止A比B少一个字母，其他的都相同。
3： 到目前为止B比A少一个字母，其他的都相同。

初始状态设为S0 = {0}，因为A和B都是空，所以是相等的。下面是从S_n到S_{n+1}的转
换：

* Let S_{n+1} = {}
* If 0 is in S_n, then
  - put 2 and 3 in S_{n+1}. 
  - If A[n] == B[n], then put 0 in S_{n+1}, otherwise put 1 in S_{n+1}
* If 1 is in S_n, then
  - if A[n] == B[n], then put 1 in S_{n+1}
* If 2 is in S_n, then
  - if A[n-1] == B[n], then put 2 in S_{n+1}
* If 3 is in S_n, then
  - if A[n] == B[n-1], then put 3 in S_{n+1}

Any time if S_n is {} for some n, return false.  Otherwise, return true
while both A and B deplete.

//////////////
