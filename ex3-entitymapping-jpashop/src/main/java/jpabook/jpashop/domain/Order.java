package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS") // ORDER가 DB의 예약어이기 때문에 복수형
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    /* 누가 주문했는지 알아야 하기 때문에 memberId 를 선언
    * 그러나 JpaMain 객체의 코드를 보면 조금 잘못 설계되었다는 것을 알 수 있음
    * 즉, Member 객체를 가진 멤버필드가 필요함 */
    @Column(name = "MEMBER_ID")
    private Long memberId;

    private Member member;

    private LocalDateTime oderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getOderDate() {
        return oderDate;
    }

    public void setOderDate(LocalDateTime oderDate) {
        this.oderDate = oderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
