from flask import Flask, request, jsonify
from personalize import generate_personalized_prompt
from utils import extract_keywords, summarize_response, determine_severity
from llama_client import query_llama
from feed import get_feed
from db import init_db, save_conversation, get_db_connection

app = Flask(__name__)
init_db()

@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json()
    user_input = data.get("input", "")
    user_id = data.get("user_id", "default_user")

    if not user_input:
        return jsonify({"error": "No input received"}), 400

    keywords = extract_keywords(user_input)
    personalized_prompt = generate_personalized_prompt(user_id, user_input, keywords)
    ai_response = query_llama(personalized_prompt)
    summary = summarize_response(ai_response)
    severity = determine_severity(user_input, ai_response)

    from utils import contains_critical_symptom
    if contains_critical_symptom(user_input) or severity == "high":
        ai_response += "\n\n🚨 *See a Doctor!! This might be a serious condition.*"

    save_conversation(user_id, user_input, ai_response, summary, severity)

    return jsonify({
        "message": ai_response,
        "summary": summary,
        "severity": severity
    })

@app.route("/feed", methods=["GET"])
def feed():
    print("🔁 Outgoing feed response:", get_feed()) 
    return jsonify(get_feed())

@app.route("/history", methods=["GET"])
def history():
    user_id = request.args.get("user_id")
    conn = get_db_connection()
    rows = conn.execute(
        "SELECT sender, message FROM chat_history WHERE user_id = ? ORDER BY timestamp",
        (user_id,)
    ).fetchall()
    conn.close()
    return jsonify([dict(row) for row in rows])

if __name__ == "__main__":
    from db import init_db
    init_db()
    print("🔥 Flask is booting...")
    app.run(host="0.0.0.0", port=5000, debug=True)

