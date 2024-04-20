package msa.reservation.domain.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("관리자", true,true),
    USER("일반유저",false,true),
    VIEWER("뷰어",false,true);
    
    private final String description;   // 설명
    
    private final Boolean update;       // 업데이트 가능 여부
    
    private final Boolean buy;          // 구매 가능 여부

    UserRole(String description, Boolean update,Boolean buy){
        this.description = description;
        this.update = update;
        this.buy = buy;
    }
}
