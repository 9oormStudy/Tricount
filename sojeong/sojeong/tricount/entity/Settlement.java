package clonecoding.tricount.entity;

import lombok.Data;

import java.util.List;

@Data
public class Settlement {
    private Long id;
    private String title;
    private List<Member> participants;
}
