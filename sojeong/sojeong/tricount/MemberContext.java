package clonecoding.tricount;

import clonecoding.tricount.entity.Member;

public class MemberContext {
    
    private static ThreadLocal<Member> memberThreadLocal = new ThreadLocal<>();

    public static void setMember(Member member) {
        memberThreadLocal.set(member);
    }

    public static Member getMember() {
        return memberThreadLocal.get();
    }
}
