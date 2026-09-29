FROM flyway/flyway:6.1-alpine

ARG DEZI_CI
ARG CI_PIPELINE_ID
ARG CI_PIPELINE_IID
ARG GITLAB_USER_LOGIN
ARG CI_PIPELINE_SOURCE
ARG PROJECT_NAME
ARG DEZI_PHASE
ARG CI_PROJECT_NAMESPACE
ARG CI_PROJECT_NAME
ARG REF_TYPE
ARG CI_COMMIT_REF_NAME
ARG CI_COMMIT_SHA
ARG CI_COMMIT_SHORT_SHA

ENV DEZI_CI="${DEZI_CI}" \
	DEZI_CI_PIPELINE_ID="${CI_PIPELINE_ID}" \
	DEZI_CI_PIPELINE_IID="${CI_PIPELINE_IID}" \
	DEZI_CI_USER_LOGIN="${GITLAB_USER_LOGIN}" \
	DEZI_CI_PIPELINE_SOURCE="${CI_PIPELINE_SOURCE}" \
	DEZI_PROJECT_GROUP="dezi" \
	DEZI_PROJECT_NAME="${PROJECT_NAME}" \
	DEZI_PROJECT_PHASE="${DEZI_PHASE}" \
	DEZI_GIT_NAMESPACE="${CI_PROJECT_NAMESPACE}" \
	DEZI_GIT_NAME="${CI_PROJECT_NAME}" \
	DEZI_GIT_REF_TYPE="${REF_TYPE}" \
	DEZI_GIT_REF_NAME="${CI_COMMIT_REF_NAME}" \
	DEZI_GIT_COMMIT_SHA="${CI_COMMIT_SHA}" \
	DEZI_GIT_COMMIT_SHORT_SHA="${CI_COMMIT_SHORT_SHA}"

LABEL fasolti="" \
	fasolti.ci="${DEZI_CI}" \
	fasolti.ci.id="${CI_PIPELINE_ID}" \
	fasolti.ci.iid="${CI_PIPELINE_IID}" \
	fasolti.ci.user="${GITLAB_USER_LOGIN}" \
	fasolti.ci.source="${CI_PIPELINE_SOURCE}" \
	fasolti.project="" \
	fasolti.project.group="dezi" \
	fasolti.project.name="${PROJECT_NAME}" \
	fasolti.project.phase="${DEZI_PHASE}" \
	fasolti.project.git.group="${CI_PROJECT_NAMESPACE}" \
	fasolti.project.git.name="${CI_PROJECT_NAME}" \
	fasolti.project.git.ref-type="${REF_TYPE}" \
	fasolti.project.git.ref="${CI_COMMIT_REF_NAME}" \
	fasolti.project.git.commit="${CI_COMMIT_SHA}" \
	fasolti.project.git.commit-short="${CI_COMMIT_SHORT_SHA}"

ADD target/dezi-db-migration.tgz /flyway/sql/
