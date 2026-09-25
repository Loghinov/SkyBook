#!/bin/bash
# Codemap generator for Spring Boot project
# Lists all source code files and builds a hierarchical map

PROJ_ROOT="/Users/sloghinov/IdeaProjects/testProjectWithClaude"
OUTPUT_FILE="${PROJ_ROOT}/agents/TEMP/codemap.txt"

# Use find (since not a git repo per env) to get all source files
# Exclude: target/, .git/, .idea/, .mvn/, node_modules/, *.class
find "$PROJ_ROOT" \
  -type f \
  \( -name "*.java" \
  -o -name "*.html" \
  -o -name "*.xml" \
  -o -name "*.properties" \
  -o -name "*.md" \
  -o -name "*.json" \
  -o -name "*.sh" \) \
  ! -path "*/target/*" \
  ! -path "*/.git/*" \
  ! -path "*/.idea/*" \
  ! -path "*/.mvn/*" \
  ! -path "*/.claude/*" \
  ! -name "*.class" \
  | sed "s|^$PROJ_ROOT/||" \
  | sort > "$OUTPUT_FILE"

echo "Codemap generated: $OUTPUT_FILE"
echo "Total files: $(wc -l < "$OUTPUT_FILE")"
cat "$OUTPUT_FILE"