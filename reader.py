import os
import re
import json

JAVA_DIR = "src/main/java/database/cms/DTO/request"  # 根据实际路径调整
OUTPUT_FILE = "request_bodies.json"

RECORD_PATTERN = re.compile(r"record\s+(\w+)\s*\(([^)]*)\)")

def parse_fields(field_str):
    fields = {}
    for part in field_str.split(","):
        part = part.strip()
        if not part:
            continue
        # 忽略注解，提取“类型 字段名”
        part = re.sub(r"@\w+\s*", "", part)
        match = re.match(r"([\w<>]+)\s+(\w+)", part)
        if match:
            type_, name = match.groups()
            fields[name] = type_
    return fields

def extract_records():
    result = {}
    for root, _, files in os.walk(JAVA_DIR):
        for file in files:
            if file.endswith(".java"):
                path = os.path.join(root, file)
                with open(path, "r", encoding="utf-8") as f:
                    content = f.read()
                    matches = RECORD_PATTERN.findall(content)
                    for class_name, field_str in matches:
                        result[class_name] = parse_fields(field_str)
    return result

if __name__ == "__main__":
    records = extract_records()
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        json.dump(records, f, indent=2, ensure_ascii=False)
    print(f"✅ Extracted {len(records)} records to {OUTPUT_FILE}")
