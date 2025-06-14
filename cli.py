import os
import requests
import json

print("📂 Current working directory:", os.getcwd())

BASE_URL = "http://localhost:8080/api"  # 改成你的后端地址
TOKEN_FILE = "../../.jwt_token"

def save_token(token):
    with open(TOKEN_FILE, "w") as f:
        f.write(token)

def load_token():
    if os.path.exists(TOKEN_FILE):
        with open(TOKEN_FILE, "r") as f:
            return f.read().strip()
    return None

def login():
    username = input("Username: ")
    password = input("Password: ")
    try:
        res = requests.post(
            f"{BASE_URL}/auth/login",
            json={"username": username, "password": password}
        )
        if res.status_code == 200:
            token = res.json().get("token")
            if token:
                save_token(token)
                print("✅ Login successful.")
            else:
                print("❌ No token in response.")
        else:
            print("❌ Login failed:", res.text)
    except Exception as e:
        print("❌ Request error:", e)

def register():
    role = input("Role: ")
    username = input("Username: ")
    password = input("Password: ")
    email = input("Email: ")
    try:
        res = requests.post(
            f"{BASE_URL}/{role}/register",
            json={"name": username, "password": password, "email": email}
        )
        if res.status_code == 200 or res.status_code == 201:
            print("✅ Registration successful.")
        else:
            print("❌ Registration failed:", res.text)
    except Exception as e:
        print("❌ Request error:", e)

def json_from_class():
    try:
        with open("request_bodies.json", "r", encoding="utf-8") as f:
            class_map = json.load(f)
    except FileNotFoundError:
        print("❌ Missing request_bodies.json. Run reader first.")
        return

    class_names = list(class_map.keys())
    print("📚 Available request body classes:")
    for i, cls in enumerate(class_names):
        print(f"{i + 1}. {cls}")

    try:
        choice = int(input("Select class by number: ")) - 1
        class_name = class_names[choice]
    except (IndexError, ValueError):
        print("❌ Invalid choice.")
        return

    fields = class_map[class_name]
    data = {}
    for key, typ in fields.items():
        val = input(f"{key} ({typ}): ").strip()
        data[key] = val  # 你可以根据 typ 做类型转换

    url = input("Enter relative URL (e.g., /register): ").strip()
    token = load_token()
    headers = {
        "Content-Type": "application/json"
    }
    if token:
        headers["Authorization"] = f"Bearer {token}"

    try:
        res = requests.post(f"{BASE_URL}{url}", headers=headers, json=data)
        print(f"✅ Response status: {res.status_code}")
        try:
            response_json = res.json()
            print("📦 Response JSON:")
            print(json.dumps(response_json, indent=2, ensure_ascii=False))
        except ValueError:
            print("📄 Response Text:")
            print(res.text)
    except Exception as e:
        print("❌ Request error:", e)

def custom_json_request():
    token = load_token()
    if not token:
        print("⚠️ You need to login first.")
        return

    url = input("Enter relative URL (e.g., /user/profile): ").strip()
    data_str = input("Enter JSON body (e.g., {\"username\": \"Amy\"}): ").strip()

    try:
        data = json.loads(data_str)
    except json.JSONDecodeError:
        print("❌ Invalid JSON.")
        return

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    try:
        res = requests.post(f"{BASE_URL}{url}", headers=headers, json=data)
        print(f"✅ Response status: {res.status_code}")
        try:
            response_json = res.json()
            print("📦 Response JSON:")
            print(json.dumps(response_json, indent=2, ensure_ascii=False))
        except ValueError:
            # 不是 JSON 响应
            print("📄 Response Text:")
            print(res.text)
    except Exception as e:
        print("❌ Request error:", e)

def call_request_without_body():
    token = load_token()
    if not token:
        print("⚠️ You need to login first.")
        return

    url = input("Enter relative URL (e.g., /logout): ").strip()
    method = input("Enter method (GET/POST/PUT/DELETE) [default: GET]: ").strip().upper() or "GET"

    headers = {
        "Authorization": f"Bearer {token}"
    }

    try:
        response = requests.request(method, f"{BASE_URL}{url}", headers=headers)
        print(f"✅ Response status: {response.status_code}")

        try:
            response_json = response.json()
            print("📦 Response JSON:")
            print(json.dumps(response_json, indent=2, ensure_ascii=False))
        except ValueError:
            print("📄 Response Text:")
            print(response.text)
    except Exception as e:
        print("❌ Request error:", e)

def help_command():
    print("""
            📖 可用命令说明：

            login         - 登录并保存 JWT（将自动用于后续请求）
            register      - 注册新用户
            json          - 手动输入参数，发送带请求体的 POST 请求（需 JWT）
            json-class    - 从 request_bodies.json 选择类名，自动生成请求体并发送 POST 请求（需 JWT）
            call          - 发送无请求体的请求，支持 GET/POST/PUT/DELETE，可加查询参数（需 JWT）
            help          - 查看所有可用命令
            exit          - 退出 CLI
            """)

def main():
    print("🔧 Welcome to the CLI. Type one of: login | register | json | json-class | help | call | exit")

    while True:
        cmd = input("\n>>> ").strip().lower()

        if cmd == "login":
            login()
        elif cmd == "register":
            register()
        elif cmd == "json":
            custom_json_request()
        elif cmd == "call":
            call_request_without_body()
        elif cmd == "json-class":
            json_from_class()
        elif cmd == "help":
            help_command()
        elif cmd == "exit":
            print("👋 Exiting.")
            break
        else:
            print("❓ Unknown command. Type 'help' for help.")

if __name__ == "__main__":
    main()
