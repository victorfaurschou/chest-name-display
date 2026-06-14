#!/bin/sh
# Publishes the mod to Modrinth.
# Required env: MODRINTH_TOKEN, PROJECT_ID, VERSION, MINECRAFT_VERSION, JAR

if [ -z "${MODRINTH_TOKEN}" ]; then
    printf 'MODRINTH_TOKEN is not set\n' >&2
    exit 1
fi
if [ -z "${PROJECT_ID}" ]; then
    printf 'PROJECT_ID is not set\n' >&2
    exit 1
fi


mc_prefix=$(printf '%s' "$MINECRAFT_VERSION" | cut -d'.' -f1,2)
game_versions=$(curl -sf "https://api.modrinth.com/v2/tag/game_version" \
    -H "User-Agent: victorfaurschou/storage-container-labels" | python3 -c "
import json, sys
prefix = '${mc_prefix}'
vs = json.load(sys.stdin)
matching = [v['version'] for v in vs if v['version_type'] == 'release' and (v['version'] == prefix or v['version'].startswith(prefix + '.'))]
print(json.dumps(matching))
")

data=$(printf '{"name":"%s","version_number":"%s","project_id":"%s","file_parts":["jar"],"game_versions":%s,"loaders":["fabric"],"version_type":"release","status":"listed","dependencies":[{"project_id":"P7dR8mSH","dependency_type":"required"},{"project_id":"mOgUt4GM","dependency_type":"optional"},{"project_id":"9s6osm5g","dependency_type":"optional"}],"featured":true}' \
    "$VERSION" "$VERSION" "$PROJECT_ID" "$game_versions")

response=$(curl -s -X POST "https://api.modrinth.com/v2/version" \
    -H "Authorization: ${MODRINTH_TOKEN}" \
    -H "User-Agent: victorfaurschou/storage-container-labels" \
    -F "data=${data}" \
    -F "jar=@${JAR}")

printf '%s\n' "$response"
printf '%s' "$response" | python3 -c 'import json,sys; r=json.load(sys.stdin); sys.exit(0 if "id" in r else 1)'

curl -sf -X PATCH "https://api.modrinth.com/v2/project/${PROJECT_ID}" \
    -H "Authorization: ${MODRINTH_TOKEN}" \
    -H "User-Agent: victorfaurschou/storage-container-labels" \
    -H "Content-Type: application/json" \
    -d "$(python3 -c "
import json, re, sys
lines = sys.stdin.read().splitlines()
summary_lines = []
for line in lines[2:]:
    if line == '':
        break
    summary_lines.append(line)
summary = re.sub(r'\*{1,2}|_{1,2}', '', ' '.join(summary_lines))
with open('README.md') as f:
    body = f.read()
print(json.dumps({'description': summary, 'body': body}))
" < README.md)"
