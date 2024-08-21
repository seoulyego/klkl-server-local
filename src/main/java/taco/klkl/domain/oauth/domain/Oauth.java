package taco.klkl.domain.oauth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.user.domain.User;

@Getter
@Entity(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

	@Id
	@Column(name = "oauth_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@Column(
		name = "user_oauth_id",
		nullable = false
	)
	private Long oauthId;

	private Oauth(User user, Long oauthId) {
		this.user = user;
		this.oauthId = oauthId;
	}

	public static Oauth of(User user, Long oauthId) {
		return new Oauth(user, oauthId);
	}
}
