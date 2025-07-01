import sqlite3
from datetime import datetime

def get_db_connection():
    conn = sqlite3.connect("calmcheck.db")
    conn.row_factory = sqlite3.Row
    return conn

def init_db():
    conn = get_db_connection()
    conn.execute("""
        CREATE TABLE IF NOT EXISTS chat_history (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id TEXT,
            sender TEXT,
            message TEXT,
            summary TEXT,
            severity TEXT,
            timestamp TEXT
        );
    """)
    conn.commit()
    conn.close()

def save_conversation(user_id, user_input, ai_response, summary, severity):
    conn = get_db_connection()
    now = datetime.now().isoformat()

    conn.execute(
        "INSERT INTO chat_history (user_id, sender, message, timestamp) VALUES (?, ?, ?, ?)",
        (user_id, "user", user_input, now)
    )
    conn.execute(
        "INSERT INTO chat_history (user_id, sender, message, summary, severity, timestamp) VALUES (?, ?, ?, ?, ?, ?)",
        (user_id, "bot", ai_response, summary, severity, now)
    )
    conn.commit()
    conn.close()


def get_recent_history(user_id, limit=3):
    conn = sqlite3.connect("calmcheck.db")
    cur = conn.cursor()
    cur.execute("""
        SELECT input, response FROM history
        WHERE user_id = ?
        ORDER BY timestamp DESC
        LIMIT ?
    """, (user_id, limit))
    rows = cur.fetchall()
    conn.close()
    return [{"input": row[0], "response": row[1]} for row in rows]
