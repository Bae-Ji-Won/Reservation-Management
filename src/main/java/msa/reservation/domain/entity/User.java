package msa.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import msa.reservation.domain.constant.UserRole;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Entity
@Table(name ="\"user\"",
        indexes = {
            @Index(columnList = "createdAt")
        }
)
@Getter
@ToString(callSuper = true)
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = NOW() where id=?")       // jpa에서 delete사용하여 deledt 쿼리문 동작 시 자동으로 user DB에 deleted_at칸에는 현재 시간이 자동으로 들어감
@Where(clause = "deleted_at is NULL")  // where문 쿼리 동작 시 deleted_at이 null인 값 즉, 아직 삭제되지 않은 값만 가져옴
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 10, nullable = false)
    private String userName;

    @Column(name = "password", length = 100, nullable = false)
    private String password;


    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "call_number", length = 100, nullable = false)
    private String callNumber;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;          // 기본 권한은 일반 유저 부여

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    protected User(){

    }

    private User(String userName,String password, String email,String address,String callNumber){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.callNumber = callNumber;
        this.createdBy = userName;
        this.updatedBy = userName;
    }

    public static User of(String userName,String password, String email,String address,String callNumber){
        return new User( userName, password,  email, address, callNumber);
    }



}
