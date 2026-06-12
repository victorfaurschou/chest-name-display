#!/bin/sh
# Publishes the mod to CurseForge.
# Required env: CURSEFORGE_TOKEN, PROJECT_ID, VERSION, MINECRAFT_VERSION, JAR

if [ -z "${CURSEFORGE_TOKEN}" ]; then
    printf 'CURSEFORGE_TOKEN is not set\n' >&2
    exit 1
fi
if [ -z "${PROJECT_ID}" ]; then
    printf 'PROJECT_ID is not set\n' >&2
    exit 1
fi

versions=$(curl -sf "https://minecraft.curseforge.com/api/game/versions" \
    -H "X-Api-Token: ${CURSEFORGE_TOKEN}" \
    -H "User-Agent: victorfaurschou/storage-container-labels")

mc_id=$(printf '%s' "$versions" | python3 -c \
    'import json,sys,os; vs=json.load(sys.stdin); print(next(v["id"] for v in vs if v["name"]==os.environ["MINECRAFT_VERSION"]))')

fabric_id=$(printf '%s' "$versions" | python3 -c \
    'import json,sys; vs=json.load(sys.stdin); print(next(v["id"] for v in vs if v["slug"]=="fabric"))')

response=$(curl -sf -X POST "https://minecraft.curseforge.com/api/projects/${PROJECT_ID}/upload-file" \
    -H "X-Api-Token: ${CURSEFORGE_TOKEN}" \
    -H "User-Agent: victorfaurschou/storage-container-labels" \
    -F "metadata={\"changelog\":\"See GitHub release notes.\",\"changelogType\":\"text\",\"releaseType\":\"release\",\"gameVersions\":[${mc_id},${fabric_id}]}" \
    -F "file=@${JAR}")

printf '%s\n' "$response"
printf '%s' "$response" | python3 -c 'import json,sys; r=json.load(sys.stdin); sys.exit(0 if "id" in r else 1)'
