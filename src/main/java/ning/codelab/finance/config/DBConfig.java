package ning.codelab.finance.config;

import org.skife.config.Config;
import org.skife.config.Default;

public interface DBConfig {

	@Config("connection.url")
	public abstract String getUrl();

	@Config("connection.user")
	@Default("")
	public abstract String getUser();

	@Config("connection.pass")
	@Default("")
	public abstract String getPass();
}
