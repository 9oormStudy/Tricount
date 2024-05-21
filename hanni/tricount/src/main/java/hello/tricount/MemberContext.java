package hello.tricount;

import hello.tricount.model.Member;

public class MemberContext {
    // 하나의 스레드에서 전역으로 사용할 수 있는 변수
    private static final ThreadLocal<Member> memberThreadLocal = new ThreadLocal<>();

    public static void setMember(Member member) {
        memberThreadLocal.set(member);
    }

    public static Member getMember() {
        return memberThreadLocal.get();
    }
}
