package adeo.leroymerlin.cdp.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Band {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Member> members;

	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
