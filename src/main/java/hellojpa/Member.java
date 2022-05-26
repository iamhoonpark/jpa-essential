package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

/** table과 mapping할 Entity 객체 */
@Entity // JPA가 관리해야 하는 애라고 인식
public class Member {

    @Id // PK
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
