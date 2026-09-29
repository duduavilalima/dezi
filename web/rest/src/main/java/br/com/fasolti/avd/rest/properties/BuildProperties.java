package br.com.fasolti.avd.rest.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@PropertySource("classpath:build.properties")
public class BuildProperties {

	@Value("${DEZI_PROJECT_GROUP:dezi}")
	private String projectGroup;

	@Value("${DEZI_PROJECT_NAME:rest}")
	private String projectName;

	@Value("${DEZI_PROJECT_PHASE:}")
	private String projectPhase;

	@Value("${DEZI_GIT_REF_TYPE:}")
	private String gitRefType;

	@Value("${DEZI_GIT_REF_NAME:}")
	private String gitRefName;

	@Value("${DEZI_GIT_COMMIT_SHA:}")
	private String gitCommitSha;

	@Value("${DEZI_GIT_COMMIT_SHORT_SHA:}")
	private String gitCommitShortSha;

	@Value("${version}")
	private String version;

	@Value("${build.date}")
	private String buildTimestampStr;

	private ZonedDateTime buildTimestamp;

	@PostConstruct
	private void initialize() {
		buildTimestamp = ZonedDateTime.parse(buildTimestampStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss'Z'").withZone(ZoneId.of("UTC")));
	}

	public String getProjectGroup() {
		return projectGroup;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectPhase() {
		return projectPhase;
	}

	public String getGitRefType() {
		return gitRefType;
	}

	public String getGitRefName() {
		return gitRefName;
	}

	public String getGitCommitSha() {
		return gitCommitSha;
	}

	public String getGitCommitShortSha() {
		return gitCommitShortSha;
	}

	public String getVersion() {
		return version;
	}

	public String getBuildTimestampStr() {
		return buildTimestampStr;
	}

	public ZonedDateTime getBuildTimestamp() {
		return buildTimestamp;
	}
}
